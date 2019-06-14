package model

data class PreRelease(val preRelease: String) : Comparable<PreRelease> {

    private val prefix = "-"

    override fun toString(): String {
            return "$prefix$preRelease"
    }

    override fun compareTo(other: PreRelease) = when {
        preRelease < other.preRelease -> -1
        preRelease > other.preRelease -> 1
        else -> 0
    }

}
