plugins {
    kotlin("jvm")
    kotlin("kapt")

    id("com.gradleup.shadow") version "9.0.0-beta11"
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
    kapt(libs.velocity.annotation)

    implementation(libs.kotlin.coroutines)
    implementation(libs.mccoroutine.velocity.api)
    implementation(libs.mccoroutine.velocity.core)

    compileOnly(project(":offline-velocity-api"))
}

kotlin {
    jvmToolchain(21)
}