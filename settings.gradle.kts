plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "offline-velocity"
include("offline-velocity-api")
include("offline-velocity-core")
include("offline-velocity-velocity")
include("offline-velocity-example")
