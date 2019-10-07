package task

import extension.SemVerExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import util.getBranchName
import util.getCurrentVersion
import util.getHeadCommitInfo

open class NextVersionTask : DefaultTask() {

    @Input
    val prefix: String?

    @Input
    val preRelease: String?

    @Input
    val releaseBranch: String?

    init {
        group = "Versioning"
        description = "Resolves the next version based on the git tags of this repository."

        val semVerExtension = project.extensions.getByType(SemVerExtension::class.java)
        this.prefix = semVerExtension.prefix.orNull
        this.preRelease = semVerExtension.preRelease.orNull
        this.releaseBranch = semVerExtension.releaseBranch.orNull
    }

    @TaskAction
    fun resolveNextVersion() {
        /** either a string containing the prefix or an empty string */
        val prefixString = getStringOrDefault(prefix, "")
        /** makes sure that the release branch is defined, if not we assume that 'master' is the release branch */
        val releaseBranchString = getStringOrDefault(releaseBranch, "master")
        /** either a string containing the preRelease or an empty string */
        val preReleaseString = getStringOrDefault(preRelease, "")
        /** the latest version the git repository knows about */
        val version = getCurrentVersion(project.projectDir, prefixString)
        /** information about HEAD */
        val headCommitInfo = getHeadCommitInfo(project.projectDir)
        /** commit message from HEAD */
        val headCommitMessage = headCommitInfo.message ?: ""
        /** sha from HEAD */
        val headCommitSha = headCommitInfo.sha ?: ""

        when {
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
            version.preRelease = if (preReleaseString.isNotBlank()) preReleaseString else getBranchName(project.projectDir)
            version.buildMetadata = headCommitSha
        }

        if (project.version == Project.DEFAULT_VERSION) {
            project.version = version.toString()
        }
    }

    private fun getStringOrDefault(prefix: String?, default: String): String {
        return if (prefix != null && prefix.isNotBlank()) prefix
        else default
    }

}
