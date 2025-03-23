plugins {
    kotlin("jvm") version "2.0.20-Beta1"
    kotlin("kapt") version "2.1.10" apply false
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

allprojects {
    group = "dev.thebjoredcraft"
    version = "2.0.0"
}
