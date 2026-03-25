package model

import util.matchSemanticVersion

private const val MAJOR_GROUP = 1
private const val MINOR_GROUP = 2
private const val PATCH_GROUP = 3
private const val PRE_RELEASE_GROUP = 4
private const val BUILD_METADATA_GROUP = 5

data class Version(
    val version: String,
    val prefix: String?,
    val buildMetadataSeparator: String?,
    val taggedCommitSha: String = "",
) {
    private val matcher = matchSemanticVersion(version, prefix)
    var major: Int
    var minor: Int
    var patch: Int
    var preRelease: String?
    var buildMetadata: String?

    init {
        matcher.matches()
        major = matcher.group(MAJOR_GROUP).toInt()
        minor = matcher.group(MINOR_GROUP).toInt()
        patch = matcher.group(PATCH_GROUP).toInt()
        preRelease = matcher.group(PRE_RELEASE_GROUP)
        buildMetadata = matcher.group(BUILD_METADATA_GROUP)
    }

    override fun toString(): String {
        val prefixString = if (prefix.isNullOrBlank()) "" else prefix
        val buildMetadataSeparator = buildMetadataSeparator ?: "+"
        var result = "$prefixString$major.$minor.$patch"

        if (!preRelease.isNullOrBlank()) result = with(result) { plus("-$preRelease") }
        if (!buildMetadata.isNullOrBlank()) result = with(result) { plus("$buildMetadataSeparator$buildMetadata") }

        return result
    }

    fun incrementMajorVersion() {
        major += 1
        minor = 0
        patch = 0
    }

    fun incrementMinorVersion() {
        minor += 1
        patch = 0
    }

    fun incrementPatchVersion() {
        patch += 1
    }

    fun taggedCommitShaMatches(otherSha: String?): Boolean {
        if (otherSha?.isEmpty() != false) {
            return false
        }
        return taggedCommitSha.startsWith(otherSha)
    }
}
