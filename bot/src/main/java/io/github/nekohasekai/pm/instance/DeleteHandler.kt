package io.github.nekohasekai.pm.instance

import io.github.nekohasekai.nekolib.core.client.TdException
import io.github.nekohasekai.nekolib.core.client.TdHandler
import io.github.nekohasekai.nekolib.core.utils.*
import io.github.nekohasekai.pm.MESSAGE_DELETED
import io.github.nekohasekai.pm.MESSAGE_DELETED_BY_ME
import io.github.nekohasekai.pm.database.MessageRecords
import io.github.nekohasekai.pm.database.PmInstance
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select

class DeleteHandler(pmInstance: PmInstance) : TdHandler(), PmInstance by pmInstance {

    val currentBot get() = (MessageRecords.botId eq me.id)

    override suspend fun onDeleteMessages(chatId: Long, messageIds: LongArray, isPermanent: Boolean, fromCache: Boolean) {

        if (!isPermanent || fromCache) return

        val records = database {

            MessageRecords.select { currentBot and (MessageRecords.messageId inList messageIds.toList()) }.toList()

        }

        if (records.isEmpty()) return

        val integration = integration

        if (chatId == admin || chatId == integration?.integration) {

            if (settings?.ignoreDeleteAction == true) return

            // 主人删除消息 对等删除

            var success = 0
            var failed = 0

            records.filter {

                it[MessageRecords.type] in arrayOf(
                        MessageRecords.MESSAGE_TYPE_INPUT_FORWARDED,
                        MessageRecords.MESSAGE_TYPE_OUTPUT_MESSAGE
                )

            }.forEach {

                try {

                    database.write {

                        MessageRecords.deleteWhere { currentBot and (MessageRecords.messageId eq it[MessageRecords.messageId]) }

                    }

                    syncDelete(it[MessageRecords.chatId], it[MessageRecords.targetId]!!)

                    success++

                } catch (e: TdException) {

                    failed++

                }

            }

            if (success + failed > 0) {

                sudo make L.MESSAGE_DELETED_BY_ME.input(success, success + failed) onSuccess deleteDelayIf(settings?.keepActionMessages != true) sendTo chatId

            }

        } else {

            // 用户删除消息, 追加提示 / 删除.

            val twoWaySync = settings?.twoWaySync == true

            records.filter {

                it[MessageRecords.type] in arrayOf(
                        MessageRecords.MESSAGE_TYPE_INPUT_MESSAGE,
                        MessageRecords.MESSAGE_TYPE_OUTPUT_FORWARDED
                )

            }.forEach { record ->

                database.write {

                    MessageRecords.deleteWhere { currentBot and (MessageRecords.messageId eq record[MessageRecords.messageId]) }

                    MessageRecords.select {

                        currentBot and (
                                ((MessageRecords.type eq MessageRecords.MESSAGE_TYPE_INPUT_FORWARDED) or
                                        (MessageRecords.type eq MessageRecords.MESSAGE_TYPE_OUTPUT_MESSAGE)) and
                                        (MessageRecords.targetId eq record[MessageRecords.messageId]))

                    }.forEach {

                        MessageRecords.deleteWhere { currentBot and (MessageRecords.messageId eq it[MessageRecords.messageId]) }

                        if (twoWaySync) {

                            delete(admin, it[MessageRecords.messageId])

                        } else {

                            sudo make L.MESSAGE_DELETED replyAt it[MessageRecords.messageId] sendTo admin

                        }

                    }

                }

            }

        }

    }

}