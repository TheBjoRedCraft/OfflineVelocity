import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.0"
    `maven-publish`

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
    kapt(libs.google.autoservice)

    implementation(libs.kotlin.coroutines)
    implementation(libs.configurate.hocon)
    implementation(libs.mccoroutine.velocity.api)
    implementation(libs.mccoroutine.velocity.core)

    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    implementation("com.mysql:mysql-connector-j:9.2.0")
    implementation("org.postgresql:postgresql:42.7.2")


    implementation(libs.bundles.exposed) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
    }

    implementation(project(":offline-velocity-core"))
    api(libs.hikari)

}

tasks.withType<ShadowJar> {
    dependsOn(":offline-velocity-core:shadowJar")
    exclude("kotlin/**")
}

kotlin {
    jvmToolchain(21)
}