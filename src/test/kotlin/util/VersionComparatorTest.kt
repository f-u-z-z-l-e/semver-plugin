package util

import model.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class VersionComparatorTest {

    @Test
    fun `Compare major version`() {
        val version000 = Version("0.0.0", null, null)
        val version100 = Version("1.0.0", null, null)

        assertEquals(1, VersionComparator.compare(version100, version000))
        assertEquals(-1, VersionComparator.compare(version000, version100))
        assertEquals(0, VersionComparator.compare(version000, version000))
        assertEquals(0, VersionComparator.compare(version100, version100))

        assertEquals("0.0.0", version000.toString())
        assertEquals("1.0.0", version100.toString())
    }

    @Test
    fun `Compare minor version`() {
        val version010 = Version("0.1.0", null, null)
        val version020 = Version("0.2.0", null, null)

        assertEquals(1, VersionComparator.compare(version020, version010))
        assertEquals(-1, VersionComparator.compare(version010, version020))
        assertEquals(0, VersionComparator.compare(version010, version010))
        assertEquals(0, VersionComparator.compare(version020, version020))

        assertEquals("0.1.0", version010.toString())
        assertEquals("0.2.0", version020.toString())
    }

    @Test
    fun `Compare patch version`() {
        val version000 = Version("0.0.0", null, null)
        val version001 = Version("0.0.1", null, null)

        assertEquals(1, VersionComparator.compare(version001, version000))
        assertEquals(-1, VersionComparator.compare(version000, version001))
        assertEquals(0, VersionComparator.compare(version000, version000))
        assertEquals(0, VersionComparator.compare(version001, version001))

        assertEquals("0.0.0", version000.toString())
        assertEquals("0.0.1", version001.toString())
    }

    @Test
    fun `Compare with preRelease`() {
        val version000 = Version("0.0.0-rc1", null, null)
        val version001 = Version("0.0.0-rc2", null, null)

        assertEquals(1, VersionComparator.compare(version001, version000))
        assertEquals(-1, VersionComparator.compare(version000, version001))
        assertEquals(0, VersionComparator.compare(version000, version000))
        assertEquals(0, VersionComparator.compare(version001, version001))

        assertEquals("0.0.0-rc1", version000.toString())
        assertEquals("0.0.0-rc2", version001.toString())
    }

    @Test
    fun `Compare with buildMetadata`() {
        val version000 = Version("0.0.0+25c2427", null, null)
        val version001 = Version("0.0.0+715b90e", null, null)

        assertEquals(0, VersionComparator.compare(version001, version000))
        assertEquals(0, VersionComparator.compare(version000, version001))
        assertEquals(0, VersionComparator.compare(version000, version000))
        assertEquals(0, VersionComparator.compare(version001, version001))

        assertEquals("0.0.0+25c2427", version000.toString())
        assertEquals("0.0.0+715b90e", version001.toString())
    }

    @Test
    fun `Compare with preRelease and buildMetadata`() {
        val version000 = Version("0.0.0-rc1+25c2427", null, null)
        val version001 = Version("0.0.0-rc1+715b90e", null, null)

        assertEquals(0, VersionComparator.compare(version001, version000))
        assertEquals(0, VersionComparator.compare(version000, version001))
        assertEquals(0, VersionComparator.compare(version000, version000))
        assertEquals(0, VersionComparator.compare(version001, version001))

        assertEquals("0.0.0-rc1+25c2427", version000.toString())
        assertEquals("0.0.0-rc1+715b90e", version001.toString())
    }

}

