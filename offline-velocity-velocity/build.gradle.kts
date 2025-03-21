plugins {
    kotlin("jvm")
    kotlin("kapt") version "2.1.10"

    id("com.gradleup.shadow") version "9.0.0-beta11"
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.velocity.api)
    kapt(libs.velocity.annotation)

    implementation(libs.kotlin.coroutines)
    implementation(libs.caffeine.coroutines)

    implementation(libs.bundles.mccoroutine.velocity)

    implementation(libs.bundles.exposed) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
        exclude("org.slf4j", "slf4j-api")
    }

}

kotlin {
    jvmToolchain(21)
}