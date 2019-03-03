class CompareVersion : Comparator<Version> {
    override fun compare(o1: Version?, o2: Version?): Int {
        return when {
            o1?.major !== o2?.major -> compare(o1?.major, o2?.major)
            o1?.minor !== o2?.minor -> compare(o1?.minor, o2?.minor)
            o1?.patch !== o2?.patch -> compare(o1?.patch, o2?.patch)
            else -> 0
        }
    }

    private fun compare(a: Int?, b: Int?): Int {
        if (a != null && b != null) return a.compareTo(b)
        throw RuntimeException("Unable to compare version.")
    }

}
