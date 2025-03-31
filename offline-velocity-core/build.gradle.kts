import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
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
    api(project(":offline-velocity-api"))
}

tasks.withType<ShadowJar> {
    dependsOn(":offline-velocity-api:shadowJar")
    exclude("kotlin/**")
}

kotlin {
    jvmToolchain(21)
}