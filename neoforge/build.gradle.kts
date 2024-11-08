plugins {
    id("cradle.loader")
    id("net.neoforged.moddev")
}

val neoforgeVersion: String by project
val parchmentVersion: String by project
val parchmentMappings: String by project
val modID: String by project

neoForge {
    version = neoforgeVersion
    val at = project(":common").file("src/main/resources/META-INF/accesstransformer.cfg")
    if(at.exists()) {
        accessTransformers.from(at)
    }
    parchment {
        minecraftVersion = parchmentVersion
        mappingsVersion = parchmentMappings
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", modID)
            ideName = "NeoForge ${this.name.capitalize()} (${project.path})"
        }
        create("client") {
            client()
        }
        create("data") {
            data()
        }
        create("server") {
            server()
        }
    }
    mods {
        create(modID) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}