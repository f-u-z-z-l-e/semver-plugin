import model.BuildMetadata
import model.PreRelease
import model.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CompareVersionTest {

    @Test
    fun compareMajorVersion() {
        val version000 = Version(major = 0, minor = 0, patch = 0)
        val version100 = Version(major = 1, minor = 0, patch = 0)
        assertEquals(1, CompareVersion().compare(version100, version000))
        assertEquals(-1, CompareVersion().compare(version000, version100))
        assertEquals(0, CompareVersion().compare(version000, version000))
        assertEquals(0, CompareVersion().compare(version100, version100))
    }

    @Test
    fun compareMinorVersion() {
        val version010 = Version(major = 0, minor = 1, patch = 0)
        val version020 = Version(major = 0, minor = 2, patch = 0)
        assertEquals(1, CompareVersion().compare(version020, version010))
        assertEquals(-1, CompareVersion().compare(version010, version020))
        assertEquals(0, CompareVersion().compare(version010, version010))
        assertEquals(0, CompareVersion().compare(version020, version020))
    }

    @Test
    fun comparePatchVersion() {
        val version000 = Version(major = 0, minor = 0, patch = 0)
        val version001 = Version(major = 0, minor = 0, patch = 1)
        assertEquals(1, CompareVersion().compare(version001, version000))
        assertEquals(-1, CompareVersion().compare(version000, version001))
        assertEquals(0, CompareVersion().compare(version000, version000))
        assertEquals(0, CompareVersion().compare(version001, version001))
    }

    @Test
    fun compareWithPreRelease() {
        val rc1 = PreRelease("rc1")
        val rc2 = PreRelease("rc2")

        val version000 = Version(major = 0, minor = 0, patch = 0, preRelease = rc1)
        val version001 = Version(major = 0, minor = 0, patch = 0, preRelease = rc2)
        assertEquals(1, CompareVersion().compare(version001, version000))
        assertEquals(-1, CompareVersion().compare(version000, version001))
        assertEquals(0, CompareVersion().compare(version000, version000))
        assertEquals(0, CompareVersion().compare(version001, version001))
    }

    @Test
    fun compareWithBuildMetadata() {
        val rc1 = PreRelease("rc1")

        val buildMetadata1 = BuildMetadata("25c2427")
        val buildMetadata2 = BuildMetadata("715b90e")

        val version000 = Version(major = 0, minor = 0, patch = 0, preRelease = rc1, buildMetadata = buildMetadata1)
        val version001 = Version(major = 0, minor = 0, patch = 0, preRelease = rc1, buildMetadata = buildMetadata2)
        assertEquals(0, CompareVersion().compare(version001, version000))
        assertEquals(0, CompareVersion().compare(version000, version001))
        assertEquals(0, CompareVersion().compare(version000, version000))
        assertEquals(0, CompareVersion().compare(version001, version001))
    }
}

