package model

data class Version(val major: Int, val minor: Int, val patch: Int, val preRelease: PreRelease?, val buildMetadata: BuildMetadata?) {
    constructor(major: Int, minor: Int, patch: Int) : this(major, minor, patch, null, null)
    constructor(major: Int, minor: Int, patch: Int, preRelease: PreRelease?) : this(major, minor, patch, preRelease, null)
    constructor(major: Int, minor: Int, patch: Int, buildMetadata: BuildMetadata?) : this(major, minor, patch, null, buildMetadata)
}