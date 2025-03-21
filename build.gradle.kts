plugins {
    kotlin("jvm") version "2.0.20-Beta1"
    id("com.github.johnrengelman.shadow") version "8.1.1"

    kotlin("kapt") version "2.1.10"

    `maven-publish`
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

    kapt("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("dev.hsbrysk:caffeine-coroutines:2.0.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-api:2.21.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-velocity-core:2.21.0")
}

kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    archiveFileName = "offline-velocity-${project.version}.jar"
}

allprojects {
    group = "dev.thebjoredcraft"
    version = "2.0.0"
}
