plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.github.lijinhong11:Treasury:1.0")
    implementation("io.github.lijinhong11:MDatabase:1.1.0")

    compileOnly("com.google.code.gson:gson:2.13.2")

    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}