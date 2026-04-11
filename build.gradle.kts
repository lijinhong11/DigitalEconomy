plugins {
    java
    id("com.gradleup.shadow") version "9.4.1" apply false
    id("fabric-loom") version "1.10-SNAPSHOT" apply false
}

subprojects {
    apply {
        repositories {
            mavenCentral()
            mavenLocal()
            maven("https://maven.fabricmc.net")
            maven("https://jitpack.io")
            // Add repositories here if you depend on other mods
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {

}