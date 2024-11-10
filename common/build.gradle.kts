plugins {
    id("cradle.common")
    id("net.neoforged.moddev")
}

val neoVanillaVersion: String by project
val parchmentVersion: String by project
val parchmentMappings: String by project

neoForge {
    neoFormVersion = neoVanillaVersion

    val at = file("src/main/resources/META-INF/accesstransformer.cfg")
    if(at.exists()) {
        accessTransformers.from(at)
    }
    parchment {
        minecraftVersion = parchmentVersion
        mappingsVersion = parchmentMappings
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    compileOnly("org.spongepowered:mixin:0.8.5")
    // fabric and neoforge both bundle mixinextras, so it is safe to use it in common
    compileOnly("io.github.llamalad7:mixinextras-common:0.3.5")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.3.5")

    implementation("com.google.guava:guava:32.1.2-jre")
}

val commonJava = configurations.create("commonJava")
val commonResources = configurations.create("commonResources")

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.singleFile)
}