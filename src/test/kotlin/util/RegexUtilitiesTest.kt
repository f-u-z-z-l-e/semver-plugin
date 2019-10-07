package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class RegexUtilitiesTest {

    @Test
    fun `Match minimal SemanticVersion`() {
        val matcher = matchSemanticVersion("1.0.1", null)
        assertEquals(matcher.matches(), true)
        assertEquals("1.0.1", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals(null, matcher.group(4))
        assertEquals(null, matcher.group(5))
    }

    @Test
    fun `Match prefixed minimal SemanticVersion`() {
        val matcher = matchSemanticVersion("v1.0.1", "v")
        assertEquals(matcher.matches(), true)
        assertEquals("v1.0.1", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals(null, matcher.group(4))
        assertEquals(null, matcher.group(5))
    }

    @Test
    fun `Match SemanticVersion with PreRelease`() {
        val matcher = matchSemanticVersion("1.0.1-rc1", null)
        assertEquals(matcher.matches(), true)
        assertEquals("1.0.1-rc1", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals("rc1", matcher.group(4))
        assertEquals(null, matcher.group(5))
    }

    @Test
    fun `Match prefixed SemanticVersion with PreRelease`() {
        val matcher = matchSemanticVersion("v1.0.1-rc1", "v")
        assertEquals(matcher.matches(), true)
        assertEquals("v1.0.1-rc1", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals("rc1", matcher.group(4))
        assertEquals(null, matcher.group(5))
    }

    @Test
    fun `Match SemanticVersion with BuildMetadata`() {
        val matcher = matchSemanticVersion("1.0.1+asdfsfd", null)
        assertEquals(matcher.matches(), true)
        assertEquals("1.0.1+asdfsfd", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals(null, matcher.group(4))
        assertEquals("asdfsfd", matcher.group(5))
    }

    @Test
    fun `Match prefixed SemanticVersion with BuildMetadata`() {
        val matcher = matchSemanticVersion("v1.0.1+asdfsfd", "v")
        assertEquals(matcher.matches(), true)
        assertEquals("v1.0.1+asdfsfd", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals(null, matcher.group(4))
        assertEquals("asdfsfd", matcher.group(5))
    }

    @Test
    fun `Match SemanticVersion with Prerelease and BuildMetadata`() {
        val matcher = matchSemanticVersion("1.0.1-rc1+asdfsfd", null)
        assertEquals(matcher.matches(), true)
        assertEquals("1.0.1-rc1+asdfsfd", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals("rc1", matcher.group(4))
        assertEquals("asdfsfd", matcher.group(5))
    }

    @Test
    fun `Match prefixed SemanticVersion with PreRelease and BuildMetadata`() {
        val matcher = matchSemanticVersion("v1.0.1-rc1+asdfsfd", "v")
        assertEquals(matcher.matches(), true)
        assertEquals("v1.0.1-rc1+asdfsfd", matcher.group(0))
        assertEquals("1", matcher.group(1))
        assertEquals("0", matcher.group(2))
        assertEquals("1", matcher.group(3))
        assertEquals("rc1", matcher.group(4))
        assertEquals("asdfsfd", matcher.group(5))
    }

}

