plugins {
    java
    id("fabric-loom")
}

repositories {
    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    compileOnly("io.github.lijinhong11:Treasury:1.0.2")
    implementation("io.github.lijinhong11:MDatabase:1.1.1")

    compileOnly("com.google.code.gson:gson:2.13.2")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}

tasks.remapJar {
    enabled = false
}

tasks.remapSourcesJar {
    enabled = false
}