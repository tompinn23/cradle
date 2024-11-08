
plugins {
    id("cradle.common")
}

val modID: String by project

val commonJava = configurations.create("commonJava") {
    isCanBeResolved = true
}
val commonResources = configurations.create("commonResources") {
    isCanBeResolved = true
}


dependencies {
    compileOnly(project(":common")) {
        capabilities {
            requireCapabilities("$group:$modID")
        }
    }
    commonJava(project(path = ":common", configuration = "commonJava"))
    commonResources(project(path = ":common",configuration ="commonResources"))
}

tasks.named<JavaCompile>("compileJava") {
    dependsOn(configurations.getByName("commonJava"))
    source(configurations.getByName("commonJava"))
}

tasks.processResources {
    dependsOn(configurations.getByName("commonResources"))
    from(configurations.getByName("commonResources"))
}

tasks.named<Javadoc>("javadoc") {
    dependsOn(configurations.getByName("commonJava"))
    source(configurations.getByName("commonJava"))
}

tasks.named<Jar>("sourcesJar") {
    dependsOn(configurations.getByName("commonJava"))
    from(configurations.getByName("commonJava"))
    dependsOn(configurations.getByName("commonResources"))
    from(configurations.getByName("commonResources"))
}