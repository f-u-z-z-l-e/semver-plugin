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
    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest:2.1")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        register("semVerPlugin") {
            id = "ch.fuzzle.gradle.semver"
            implementationClass = "SemanticVersioning"
        }
    }
}

tasks.wrapper {
    gradleVersion = "5.2.1"
    distributionType = DistributionType.ALL
    distributionSha256Sum = "9dc729f6dbfbbc4df1692665d301e028976dacac296a126f16148941a9cf012e"
}