plugins {
    kotlin("jvm") version "2.0.20-Beta1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.thebjoredcraft"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

kotlin {
    jvmToolchain(21)
}
