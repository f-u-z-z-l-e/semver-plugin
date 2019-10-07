package util

import model.Version

/**
 * Comparator for model.Version
 *
 * Note that the property 'buildMetadata' does not effect the comparison at all.
 * */
class VersionComparator {
    companion object : Comparator<Version> {
        override fun compare(o1: Version, o2: Version): Int {
            return when {
                o1.major != o2.major -> o1.major.compareTo(o2.major)
                o1.minor != o2.minor -> o1.minor.compareTo(o2.minor)
                o1.patch != o2.patch -> o1.patch.compareTo(o2.patch)
                o1.preRelease != o2.preRelease
                        && o1.preRelease != null
                        && o2.preRelease != null -> o1.preRelease!!.compareTo(o2.preRelease!!)
                else -> 0
            }
        }

    }
}
