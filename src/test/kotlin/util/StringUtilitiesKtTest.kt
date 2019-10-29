package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringUtilitiesKtTest {

    @Test
    fun `Do not change single word string`() {
        assertEquals("abc", "abc".toPreReleaseString())
        assertEquals("ABC", "ABC".toPreReleaseString())
    }

    @Test
    fun `Replace hyphen with underscore when string starts with number`() {
        assertEquals("ABC_123", "ABC-123".toPreReleaseString())
        assertEquals("abc_123", "abc-123".toPreReleaseString())
    }

    @Test
    fun `Change snake case to camelCase`() {
        assertEquals("preReleaseVersion", "pre-release-version".toPreReleaseString())
    }

    @Test
    fun `Return default value for null input on getStringOrDefault`() {
        assertEquals("default", getStringOrDefault(null, "default"))
    }

    @Test
    fun `Return default value for any input other than string on getStringOrDefault`() {
        assertEquals("default", getStringOrDefault(1, "default"))
    }

    @Test
    fun `Return default value for blank string input on getStringOrDefault`() {
        assertEquals("default", getStringOrDefault("", "default"))
    }

}