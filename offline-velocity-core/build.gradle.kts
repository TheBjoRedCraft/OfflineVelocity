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

    api(project(":offline-velocity-api"))

}

kotlin {
    jvmToolchain(21)
}