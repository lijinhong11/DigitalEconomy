plugins {
    java
    idea
    id("com.gradleup.shadow")
    id("fabric-loom")
}

base {
    archivesName.set("${project.property("mod_name")}-Fabric")
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create("fabric") {
            sourceSet(sourceSets["main"])
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}+${project.property("minecraft_version")}")

    compileOnly("io.github.lijinhong11:Treasury:1.0.2")
    implementation("io.github.lijinhong11:MDatabase:1.1.1")

    implementation(project(":common", configuration = "namedElements"))
    shadow(project(":common", configuration = "namedElements"))
    shadow("io.github.lijinhong11:MDatabase:1.1.1")
}

tasks.processResources {
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand(project.properties)
    }
}

val targetJavaVersion = 17

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"

    options.release.set(targetJavaVersion)
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations = listOf(project.configurations.shadow.get())
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)

    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }

    withSourcesJar()
}
