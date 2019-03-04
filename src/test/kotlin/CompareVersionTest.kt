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

}

