pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net") {
            content {
                includeGroup("net.fabricmc")
                includeGroup("fabric-loom")
            }
        }
        maven("https://repo.spongepowered.org/repository/maven-public") {
            content {
                includeGroupAndSubgroups("org.spongepowered")
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
}

rootProject.name = "cradle"
include("common")
include("fabric")
include("neoforge")
