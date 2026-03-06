rootProject.name = "spring-ai-tutorial"
pluginManagement {
    repositories {
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/snapshot")
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.spring.io/milestone")
        maven("https://repo.spring.io/snapshot")
    }
}