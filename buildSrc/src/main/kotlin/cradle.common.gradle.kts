plugins {
    id("java-library")
    id("maven-publish")
}
val modName: String by rootProject
val modAuthor: String by rootProject
val modID: String by rootProject
val minecraftVersion: String by rootProject
val minecraftVersionRange: String by rootProject
val fabricVersion: String by rootProject
val fabricLoaderVersion: String by rootProject
val neoforgeVersion: String by rootProject
val neoforgeLoaderVersionRange: String by rootProject

base {
    archivesName = "${modID}-${project.name}-${minecraftVersion}"
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
    maven("https://repo.spongepowered.org/repository/maven-public") {
        content {
            includeGroupAndSubgroups("org.spongepowered")
        }
    }
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.parchmentmc.org/") {
        content {
            includeGroup("org.parchmentmc.data")
        }
    }
}

arrayOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements").forEach {
    configurations.maybeCreate(it).apply {
        outgoing {
            capability("$group:${base.archivesName}:$version")
            capability("$group:$modID-${project.name}-$minecraftVersion:$version")
            capability("$group:$modID:$version")
        }
    }

    tasks.jar {
        manifest {
            attributes(
                "Specification-Title" to modName,
                "Specification-Vendor" to modAuthor,
                "Specification-Version" to archiveVersion,
                "Implementation-Title" to project.name,
                "Implementation-Version" to archiveVersion,
                "Implementation-Vendor" to modAuthor,
                "Built-On-Minecraft" to minecraftVersion,
            )
        }
    }
}


val props = mapOf(
    "version" to version,
    "group" to project.group,
    "minecraftVersion" to minecraftVersion,
    "minecraftVersionRange" to minecraftVersionRange,
    "fabricVersion" to fabricVersion,
    "fabricLoaderVersion" to fabricLoaderVersion,
    "modName" to modName,
    "modAuthor" to modAuthor,
    "modID" to modID,
    "description" to project.description,
    "neoforgeVersion" to neoforgeVersion,
    "neoforgeLoaderVersionRange" to neoforgeLoaderVersionRange,
    "javaVersion" to 21,
)

tasks.processResources {
    filesMatching(listOf("pack.mcmeta", "fabric.mod.json", "META-INF/mods.toml", "META-INF/neoforge.mods.toml", "*.mixins.json")) {
        expand(props)
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = base.archivesName.get()
            from(components["java"])
        }
    }
}
