plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.plugin.publish)
    id("ch.fuzzle.gradle.semver") version "1.0.0"  // temporarily disabled to isolate WrapUtil error

    jacoco
    // id("pl.droidsonroids.jacoco.testkit") version "1.0.12"  // temporarily disabled to isolate WrapUtil error
}

description = "gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jgit)

    testImplementation(gradleTestKit())
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.hamcrest)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        allWarningsAsErrors = true
    }
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
