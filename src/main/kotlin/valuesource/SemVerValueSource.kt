package valuesource

import org.gradle.api.provider.Property
import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import util.getBranchName
import util.getCurrentVersion
import util.getHeadCommitInfo
import util.toPreReleaseString
import java.io.File
import java.io.Serializable

/**
 * A Gradle ValueSource that computes the next semantic version by reading Git state.
 *
 * All JGit calls happen inside [obtain], which Gradle treats as a configuration cache input.
 * This avoids "external process started during configuration time" errors.
 */
abstract class SemVerValueSource : ValueSource<SemVerResult, SemVerValueSource.Params> {

    interface Params : ValueSourceParameters {
        val projectDir: Property<File>
        val prefix: Property<String>
        val releaseBranch: Property<String>
        val preRelease: Property<String>
        val buildMetadataSeparator: Property<String>
    }

    override fun obtain(): SemVerResult {
        val projectDir = parameters.projectDir.get()
        val prefixString = parameters.prefix.getOrElse("")
        val releaseBranchString = parameters.releaseBranch.getOrElse("master")
        val preReleaseString = parameters.preRelease.getOrElse("")
        val buildMetadataSeparator = parameters.buildMetadataSeparator.orNull

        val version = getCurrentVersion(projectDir, prefixString, buildMetadataSeparator)
        val headCommitInfo = getHeadCommitInfo(projectDir)

        val headCommitMessage = headCommitInfo.message ?: ""
        val headCommitSha = headCommitInfo.sha ?: ""

        when {
            version.taggedCommitShaMatches(headCommitSha) -> version.incrementPatchVersion()
            headCommitMessage.contains("#major", true) -> version.incrementMajorVersion()
            headCommitMessage.contains("#minor", true) -> version.incrementMinorVersion()
            else -> version.incrementPatchVersion()
        }

        if (getBranchName(projectDir).equals(releaseBranchString, true)) {
            version.preRelease = null
            version.buildMetadata = null
        } else {
            version.preRelease =
                preReleaseString.ifBlank { getBranchName(projectDir).toPreReleaseString() }
            version.buildMetadata = headCommitSha
        }

        return SemVerResult(
            version.toString(),
            headCommitInfo.message,
            headCommitInfo.sha,
            headCommitInfo.author,
            headCommitInfo.email,
        )
    }
}

/**
 * Serializable result from [SemVerValueSource] that can be stored in the configuration cache.
 */
data class SemVerResult(
    val version: String,
    val commitMessage: String?,
    val commitSha: String?,
    val commitAuthor: String?,
    val commitEmail: String?,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
