# TdPm

# TdGroupBot

## 安装

#### 依赖 (Linux)

```shell script
apt install -y openssl git zlib1g libc++-dev default-jdk
```

注： 仅支持 `amd64, i386, arm64`, 否则需自行编译 [LibTDJNi](https://github.com/TdBotProject/LibTDJni) 放置在 libs 文件夹下.  

如遇到找不到 LIBC 库, 请更新系统或编译安装.

### 依赖 (Windows)

需要安装 [Git for Windows](https://gitforwindows.org/) 与 [VC++ 2015](https://github.com/abbodi1406/vcredist/releasesvc)  
您也可使用 `vcpkg` 编译安装 `openssl` 与 `zlib`

## 配置

复制 `.pm.conf` 到 `pm.conf` ( 您也可使用环境遍历指定 )

`BOT_LANG`: 工作语言, 暂仅支持 `zh_CN`, `en_US`, 默认简中.   
`BOT_TOKEN`: 指定 BotToken.  
`PUBLIC`: 是否以公开模式运行 (否则为单例)
`ADMINS`: 管理员列表 空格分隔

单例模式下仅支持设置一位用户作为管理员, 若您不知道账号ID, 可留空, 启动后发送 /id 到机器人获取,  
公共模式下暂无作用.

`BINLOG`: 指定 UserBot 的 binlog, 跳过交互式认证. ( 仅用于环境变量 )

## 管理

`./bot.sh run` 编译安装并进入交互式认证  
`./bot.sh init` 注册 systemd 服务  
`./bot.sh <start/stop/restart>` 启动停止  
`./bot.sh <enable/disable>` 启用禁用 (开机启动)  
`./bot.sh log` 实时日志  
`./bot.sh logs` 打印所有日志

## 使用

查看BOT内提示

## 进度

■ 转发消息到主人 / 提示消息  
■ 回复提示消息发送消息给用户  
■ 回复转发的消息回复对应消息  
■ 回复回复的消息  
■ 删除消息同步  
■ 进入 / 退出对话  
□ 清理对话所有消息  
□ 添加中文翻译  
□ 语言选择  
□ 多主人 / 群组支持  
□ 屏蔽用户 / 防洪水

■ 自动设置命令提示  
■ 创建BOT (步进)  
■ 刪除BOT (步进)  
□ 用户协议  
□ 创建上限限制  
□ BOT管理 (inline按钮)