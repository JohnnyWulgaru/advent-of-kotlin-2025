plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "advent-of-kotlin-2025"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
