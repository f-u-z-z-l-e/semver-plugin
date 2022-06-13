import extension.SemVerExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import task.DisplayVersion
import task.TagHeadCommitTask
import util.*

class SemVerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("semver", SemVerExtension::class.java, project)

            tasks.register("tagHeadCommit", TagHeadCommitTask::class.java)
            tasks.register("displayVersion", DisplayVersion::class.java)

            // this task needs git to be configured outside of gradle.
            tasks.register("pushTagToOrigin", Exec::class.java) { task ->
                task.group = "Versioning"
                task.description = "Pushes the tags to origin."
                task.commandLine = listOf("git", "push", "origin", "--tags")
            }

            /**
             * If we do this in afterEvaluate, this plugin must come before the maven-publish plugin in the Gradle build
             * script. Doing it in afterProject is more robust, no need to pay attention to the sequence of plugins.
             */
            gradle.afterProject {
                resolveNextVersion(project)
            }
        }
    }

    /**
     * Calculates the project version according to semantic versioning.
     */
    private fun resolveNextVersion(project: Project) {
        val semVerExtension = project.extensions.getByType(SemVerExtension::class.java)

        /** either a string containing the prefix or an empty string */
        val prefixString = getStringOrDefault(semVerExtension.prefix.orNull, "")

        /** makes sure that the release branch is defined, if not we assume that 'master' is the release branch */
        val releaseBranchString = getStringOrDefault(semVerExtension.releaseBranch.orNull, "master")

        /** either a string containing the preRelease or an empty string */
        val preReleaseString = getStringOrDefault(semVerExtension.preRelease.orNull, "")

        /** the latest version the git repository knows about */
        val version = getCurrentVersion(project.projectDir, prefixString, semVerExtension.buildMetadataSeparator.orNull)

        /** information about HEAD */
        val headCommitInfo = getHeadCommitInfo(project.projectDir)

        /** commit message from HEAD */
        val headCommitMessage = headCommitInfo.message ?: ""

        /** sha from HEAD */
        val headCommitSha = headCommitInfo.sha ?: ""

        when {
            version.taggedCommitShaMatches(headCommitSha) -> version.incrementPatchVersion() // version from commit message already manifested in tag
            headCommitMessage.contains("#major", true) -> version.incrementMajorVersion()
            headCommitMessage.contains("#minor", true) -> version.incrementMinorVersion()
            else -> version.incrementPatchVersion()
        }

        /** clear preRelease & buildMetadata from version on release branch */
        if (getBranchName(project.projectDir).equals(releaseBranchString, true)) {
            version.preRelease = null
            version.buildMetadata = null
        } else {
            /** set preRelease value from extension if present, otherwise set branch name */
            version.preRelease =
                preReleaseString.ifBlank { getBranchName(project.projectDir).toPreReleaseString() }
            version.buildMetadata = headCommitSha
        }

        if (project.version == Project.DEFAULT_VERSION) {
            project.version = version.toString()
        }

        project.extensions.extraProperties.set("gitCommitInfo", headCommitInfo)
    }
}