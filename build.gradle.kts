import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.3.41"
    id("org.jetbrains.intellij") version "0.4.10"
    id("com.gradle.plugin-publish") version "0.10.0"
}

description = "fuzzle gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.3.2.201906051522-r")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
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
    gradleVersion = "5.6.2"
    distributionType = DistributionType.ALL
    distributionSha256Sum = "027fdd265d277bae65a0d349b6b8da02135b0b8e14ba891e26281fa877fe37a2"
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
