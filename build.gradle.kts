plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.1.20"
    id("com.gradle.plugin-publish") version "2.1.1"
    // id("ch.fuzzle.gradle.semver") version "1.0.0"  // temporarily disabled to isolate WrapUtil error
    jacoco
    // id("pl.droidsonroids.jacoco.testkit") version "1.0.12"  // temporarily disabled to isolate WrapUtil error
}
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

kotlin {
    jvmToolchain(17)
}

repositories {
    mavenCentral()
}

gradlePlugin {
    website.set("https://github.com/f-u-z-z-l-e/semver-plugin")
    vcsUrl.set("https://github.com/f-u-z-z-l-e/semver-plugin")
    plugins {
        create("semVerPlugin") {
            id = "ch.fuzzle.gradle.semver"
            displayName = "gradle semantic versioning plugin"
            description = "This plugin adds tasks to facilitate semantic versioning of gradle projects."
            implementationClass = "SemVerPlugin"
            tags.set(listOf("build", "semver", "versioning", "git", "kotlin"))
        }
    }
}

tasks {
    // Use the built-in JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(false)
    }
}

// semver {
////    prefix.set( "test")
////    preRelease.set("rc1")
////    releaseBranch.set("master")
////    tagMessage.set("Tagged automatically.")
//    buildMetadataSeparator.set("-")
// }
