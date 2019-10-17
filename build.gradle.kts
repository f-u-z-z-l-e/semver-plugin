import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.3.50"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.10.1"
    id("ch.fuzzle.gradle.semver") version "0.1.6-fixci+8ed9e31c"
}

description = "gradle semantic versioning plugin"
group = "ch.fuzzle.gradle.semver"

dependencies {
    implementation(kotlin("stdlib", "1.3.50"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:5.5.1.201910021850-r")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.5.2")
    testImplementation("org.hamcrest:hamcrest-library:2.1")
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("semVerPlugin") {
            id = "ch.fuzzle.gradle.semver"
            displayName = "gradle semantic versioning plugin"
            description = "This plugin add tasks to facilitate semantic versioning of gradle projects."
            implementationClass = "SemVerPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/f-u-z-z-l-e/semver-plugin"
    vcsUrl = "https://github.com/f-u-z-z-l-e/semver-plugin"
    tags = listOf("gradle", "build", "semver", "versioning", "git", "kotlin")
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

semver {
    //    prefix.value( "test")
//    preRelease.value("rc1")
//    releaseBranch.value("master")
//    tagMessage.value("Tagged automatically.")
}
