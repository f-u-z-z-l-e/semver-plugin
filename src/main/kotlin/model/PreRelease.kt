package model

data class PreRelease(val preRelease: String) : Comparable<PreRelease> {

    override fun compareTo(other: PreRelease)= when {
        preRelease < other.preRelease -> -1
        preRelease > other.preRelease -> 1
        else -> 0
    }

}
