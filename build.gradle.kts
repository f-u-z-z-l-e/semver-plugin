plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.6.21"
    id("com.gradle.plugin-publish") version "1.0.0-rc-2"
    id("ch.fuzzle.gradle.semver") version "0.3.9"
    jacoco
    id("pl.droidsonroids.jacoco.testkit") version "1.0.9"
    `maven-publish`  // TODO REMOVE, just for local tests
}
version="1.0.4"  // TODO REMOVE, just for local tests
description = "gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
}

// Toolchain for Kotlin
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("semVerPlugin") {
            id = "ch.fuzzle.gradle.semver"
            displayName = "gradle semantic versioning plugin"
            description = "This plugin adds tasks to facilitate semantic versioning of gradle projects."
            implementationClass = "SemVerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/f-u-z-z-l-e/semver-plugin"
    vcsUrl = "https://github.com/f-u-z-z-l-e/semver-plugin"
    tags = listOf("gradle", "build", "semver", "versioning", "git", "kotlin")
}

tasks {
    // Use the built-in JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        html.isEnabled = false
    }
}

semver {
//    prefix.set( "test")
//    preRelease.set("rc1")
//    releaseBranch.set("master")
//    tagMessage.set("Tagged automatically.")
    buildMetadataSeparator.set("-")
}
