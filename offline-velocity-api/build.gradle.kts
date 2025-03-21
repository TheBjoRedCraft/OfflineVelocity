plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.velocity.api)
}

kotlin {
    jvmToolchain(21)
}