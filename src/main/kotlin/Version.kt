data class Version(val major: Int, val minor: Int, val patch: Int, val preRelease: String?, val buildMetadata: String?) {
    constructor(major: Int, minor: Int, patch: Int) : this(major, minor, patch, null, null)
}