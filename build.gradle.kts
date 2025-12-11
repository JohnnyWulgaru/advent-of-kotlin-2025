import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    application
}

kotlin {
    jvmToolchain(24)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.locationtech.jts:jts-core:1.20.0")
    implementation("tools.aqua:z3-turnkey:4.13.0.1")
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
val compileKotlin: KotlinCompile by tasks

compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-XXLanguage:+NestedTypeAliases"))
}