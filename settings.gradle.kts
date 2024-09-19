pluginManagement {
    repositories {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/BKPlusResearch/BKPlus-Ads")
            credentials {
                username = "bkplus.firebase.apero@gmail.com"
                password = "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"
            }
        }
        maven {
            url = uri("https://android-sdk.is.com/")
        }
        //AppFlyer
        maven {
            url = uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
        }
        maven {
            url = uri("https://artifact.bytedance.com/repository/pangle/")
        }
        maven {
            url = uri("https://repository.liferay.com/nexus/content/repositories/public/")
        }
    }
}

rootProject.name = "LockScreenApp"
include(":app")
 