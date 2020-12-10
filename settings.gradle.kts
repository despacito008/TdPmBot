rootProject.name = "TdPmBot"

includeBuild("ktlib") {
    dependencySubstitution {
        fun include(name: String) = substitute(module("io.nekohasekai.ktlib:$name")).with(project(":$name"))
        include("ktlib-td-cli")
        include("ktlib-compress")
        include("ktlib-db")
        include("ktlib-td-http-api")
    }
}

includeBuild("proguard") {
    dependencySubstitution {
        substitute(module("io.nekohasekai:proguard")).with(project(":"))
    }
}