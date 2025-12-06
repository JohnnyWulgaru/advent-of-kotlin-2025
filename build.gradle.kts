plugins {
    kotlin("jvm") version "2.2.21"
    application
}

kotlin {
    jvmToolchain(24)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

val day: String? by project

application {
    mainClass.set("Day${day?.padStart(2, '0') ?: "01"}Kt")
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
}
