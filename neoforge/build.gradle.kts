plugins {
    java
    idea
    id("com.gradleup.shadow")
    id("net.neoforged.moddev") version "2.0.141"
}

base {
    archivesName.set("${project.property("mod_name")}-NeoForge")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

neoForge {
    version = property("neoforge_version") as String

    runs {
        create("client") {
            type = "client"

            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
            systemProperty("forge.enabledGameTestNamespaces", "treasury")
        }

        create("server") {
            type = "server"

            systemProperty("forge.logging.markers", "REGISTRIES")
            systemProperty("forge.logging.console.level", "debug")
            systemProperty("forge.enabledGameTestNamespaces", "treasury")
            programArgument("--nogui")
        }

        create("gameTestServer") {
            type = "gameTestServer"

            systemProperty("forge.enabledGameTestNamespaces", "treasury")
        }

        create("data") {
            type = "data"

            programArguments.addAll(
                "--mod", "treasury",
                "--all",
                "--output", file("src/generated/resources").absolutePath,
                "--existing", file("src/main/resources").absolutePath
            )
        }
    }
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

dependencies {
    implementation("net.neoforged:neoforge:${project.property("neoforge_version")}")
    compileOnly("io.github.lijinhong11:Treasury:1.0.2")
    implementation("io.github.lijinhong11:MDatabase:1.1.1")

    implementation(project(":common", configuration = "namedElements"))
    shadow(project(":common", configuration = "namedElements"))
    shadow("io.github.lijinhong11:MDatabase:1.1.1")
}

tasks.processResources {
    val prop = mapOf(
        "group" to project.property("group"),
        "version" to project.property("version"),
        "id" to project.property("id"),
        "mod_name" to project.property("mod_name"),
        "license" to project.property("license"),
        "description" to project.property("description"),
        "authors" to project.property("authors"),

        "minecraft_version" to project.property("minecraft_version"),

        "neoforge_mc_version_range" to project.property("neoforge_mc_version_range"),
        "neoforge_version" to project.property("neoforge_version"),
        "neoforge_version_range" to project.property("neoforge_version_range")
    )
    
    inputs.properties(prop)

    filesMatching("META-INF/mods.toml") {
        expand(prop)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations = listOf(project.configurations.shadow.get())
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}
