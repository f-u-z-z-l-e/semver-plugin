import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    id("org.jetbrains.intellij") version "0.4.9"
    id("org.jetbrains.kotlin.jvm") version("1.3.21")
    id("com.gradle.plugin-publish") version "0.10.0"
    id("org.gradle.kotlin.kotlin-dsl") version "1.1.3"
}

description = "fuzzle gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("semVerPlugin") {
            id = "ch.fuzzle.gradle.semver"
            displayName = "fuzzle gradle semantic versioning plugin"
            description = "This plugin add tasks to facilitate versioning of fuzzle projects."
            implementationClass = "SemanticVersioning"
        }
    }
}

pluginBundle {
    website = "https://github.com/f-u-z-z-l-e/semver-plugin"
    vcsUrl = "https://github.com/f-u-z-z-l-e/semver-plugin"
    tags = listOf("gradle", "build")
}

tasks.wrapper {
    gradleVersion = "5.4.1"
    distributionType = DistributionType.ALL
    distributionSha256Sum = "14cd15fc8cc8705bd69dcfa3c8fefb27eb7027f5de4b47a8b279218f76895a91"
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