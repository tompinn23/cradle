plugins {
    id("cradle.loader")
    id("fabric-loom")
}

val modID: String by project
val minecraftVersion: String by project
val parchmentVersion: String by project
val parchmentMappings: String by project

val fabricLoaderVersion: String by project
val fabricVersion: String by project

dependencies {
    minecraft("com.mojang:minecraft:$minecraftVersion")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${parchmentVersion}:${parchmentMappings}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricVersion}")
}

loom {

    val aw = project(":common").file("src/main/resources/${modID}.accesswidener")
    if(aw.exists()) {
        accessWidenerPath.set(aw)
    }

    mixin {
        defaultRefmapName.set("${modID}.refmap.json")
    }

    runs {
        create("fabric-client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run/client")
        }
        create("fabric-server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run/server")
        }
    }


}