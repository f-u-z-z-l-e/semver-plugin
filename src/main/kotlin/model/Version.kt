package model

import util.matchSemanticVersion

data class Version(
    val version: String,
    val prefix: String?,
    val buildMetadataSeparator: String?,
    val taggedCommitSha: String = ""
) {
    private val matcher = matchSemanticVersion(version, prefix)
    var major: Int
    var minor: Int
    var patch: Int
    var preRelease: String?
    var buildMetadata: String?

    init {
        matcher.matches()
        major = matcher.group(1).toInt()
        minor = matcher.group(2).toInt()
        patch = matcher.group(3).toInt()
        preRelease = matcher.group(4)
        buildMetadata = matcher.group(5)
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
