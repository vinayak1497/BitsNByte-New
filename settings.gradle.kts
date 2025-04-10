pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven(url = "https://www.jitpack.io")
        maven(url = "https://storage.zego.im/maven")
        maven(url = "https://maven.google.com")
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://www.jitpack.io")
        maven(url = "https://storage.zego.im/maven")
        maven(url = "https://maven.google.com")
    }
}

rootProject.name = "Signup Login Realtime"
include(":app")
