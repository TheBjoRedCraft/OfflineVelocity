plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.0"

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
    implementation(libs.configurate.hocon)
    implementation(libs.mccoroutine.velocity.api)
    implementation(libs.mccoroutine.velocity.core)

    implementation(libs.bundles.exposed) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    }

    api(project(":offline-velocity-core"))
    api(libs.hikari)

}

kotlin {
    jvmToolchain(21)
}