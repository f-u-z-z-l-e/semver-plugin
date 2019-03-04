/**
 * Comparator for Version
 *
 * Note that the properties 'preRelease' and 'buildMetadata'
 * do not effect the comparison at all.
 * */
class CompareVersion : Comparator<Version> {
    override fun compare(o1: Version, o2: Version): Int {
        return when {
            o1.major != o2.major -> o1.major.compareTo(o2.major)
            o1.minor != o2.minor -> o1.minor.compareTo(o2.minor)
            o1.patch != o2.patch -> o1.patch.compareTo(o2.patch)
            else -> 0
        }
    }

}
