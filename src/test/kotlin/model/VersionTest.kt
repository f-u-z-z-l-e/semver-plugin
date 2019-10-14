package model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class VersionTest {

    @Test
    fun `Test Version toString with Prefix`() {
        val version = Version("v1.0.1", "v")
        assertEquals("v1.0.1", version.toString())
    }

    @Test
    fun `Test Version toString without Prefix`() {
        val version = Version("1.0.1", null)
        assertEquals("1.0.1", version.toString())
    }

    @Test
    fun `Throw exception when no match found`() {
        assertThrows(IllegalStateException::class.java) {
            Version("v1.0.1", null)
        }
    }

}
