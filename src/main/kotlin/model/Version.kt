package model

import util.matchSemanticVersion

data class Version(val version: String, val prefix: String?) {
    private val matcher = matchSemanticVersion(version, prefix)
    val major: String
    val minor: String
    val patch: String
    val preRelease: String?
    private val buildMetadata: String?

    init {
        matcher.matches()
        major = matcher.group(1)
        minor = matcher.group(2)
        patch = matcher.group(3)
        preRelease = matcher.group(4)?.substring(1)
        buildMetadata = matcher.group(5)?.substring(1)
    }

    override fun toString(): String {
        var result = "$major.$minor.$patch"

        if (preRelease != null) result = with(result) { plus("-$preRelease") }
        if (buildMetadata != null) result = with(result) { plus("+$buildMetadata") }

        return result
    }
}