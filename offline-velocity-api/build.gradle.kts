plugins {
    kotlin("jvm")
    `maven-publish`
}

repositories {
    mavenCentral()
    gradlePluginPortal()

    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly(libs.velocity.api)
    implementation(libs.kotlin.coroutines)
}

kotlin {
    jvmToolchain(21)
}