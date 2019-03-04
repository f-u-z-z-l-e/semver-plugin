import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    id("org.gradle.kotlin.kotlin-dsl") version "1.1.3"
    id("org.jetbrains.kotlin.jvm").version("1.3.20")
    id("com.gradle.plugin-publish") version "0.10.0"
}

description = "fuzzle gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.0")
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
    gradleVersion = "5.2.1"
    distributionType = DistributionType.ALL
    distributionSha256Sum = "9dc729f6dbfbbc4df1692665d301e028976dacac296a126f16148941a9cf012e"
}