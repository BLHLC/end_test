pluginManagement {
    repositories {
        // 阿里云 Gradle 插件镜像
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
        // 阿里云 Google 镜像
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // 阿里云公共仓库镜像
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        // 阿里云中央仓库镜像
        maven { url = uri("https://maven.aliyun.com/repository/central") }

        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 阿里云 Google 镜像
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        // 阿里云公共仓库镜像
        maven { url = uri("https://maven.aliyun.com/repository/public") }
        // 阿里云中央仓库镜像
        maven { url = uri("https://maven.aliyun.com/repository/central") }
        // 阿里云 Gradle 插件镜像
        maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }

        google()
        mavenCentral()
    }
}

rootProject.name = "end"
include(":app")
 