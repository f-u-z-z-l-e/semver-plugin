plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.3.71"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.11.0"
    id("ch.fuzzle.gradle.semver") version "0.3.5"
    jacoco
    id("pl.droidsonroids.jacoco.testkit") version "1.0.7"
}

description = "gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(kotlin("stdlib", "1.3.71"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.7.0.202003110725-r")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.6.2")
    testImplementation("org.hamcrest:hamcrest-library:2.2")
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

kotlinDslPluginOptions {
    experimentalWarning.set(false)
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
//    prefix.value( "test")
//    preRelease.value("rc1")
//    releaseBranch.value("master")
//    tagMessage.value("Tagged automatically.")
    buildMetadataSeparator.value("-")
}
