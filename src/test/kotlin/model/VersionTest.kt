package model

import org.gradle.internal.impldep.junit.framework.TestCase.assertFalse
import org.gradle.internal.impldep.junit.framework.TestCase.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class VersionTest {

    @Test
    internal fun `Test Version toString with Prefix`() {
        val version = Version("v1.0.1", "v", null)
        assertEquals("v1.0.1", version.toString())
    }

    @Test
    internal fun `Test Version toString without Prefix`() {
        val version = Version("1.0.1", null, null)
        assertEquals("1.0.1", version.toString())
    }

    @Test
    internal fun `Throw exception when no match found`() {
        assertThrows(IllegalStateException::class.java) {
            Version("v1.0.1", null, null)
        }
    }

    @Test
    internal fun `taggedCommitShaMatches null is false`() {
        assertFalse(
            "Empty taggedCommitSha must not match null",
            Version("v1.0.1", "v", null).taggedCommitShaMatches(null)
        )
        assertFalse(
            "taggedCommitSha must not match null",
            Version("v1.0.1", "v", null, "97904f87f8889a12d11c1ec3a0f8def2f28ce762").taggedCommitShaMatches(null)
        )
    }

    @Test
    internal fun `taggedCommitShaMatches empty String is false`() {
        assertFalse(
            "Empty taggedCommitSha must not match empty String",
            Version("v1.0.1", "v", null).taggedCommitShaMatches("")
        )
        assertFalse(
            "taggedCommitSha must not match empty String",
            Version("v1.0.1", "v", null, "97904f87f8889a12d11c1ec3a0f8def2f28ce762").taggedCommitShaMatches("")
        )
    }

    @Test
    internal fun `taggedCommitShaMatches different SHA is false`() {
        assertFalse(
            "Empty taggedCommitSha must not match SHA",
            Version("v1.0.1", "v", null).taggedCommitShaMatches("97904f8")
        )
        assertFalse(
            "taggedCommitSha must not match other SHA even if substring is contained",
            Version("v1.0.1", "v", null, "97904f87f8889a12d11c1ec3a0f8def2f28ce762").taggedCommitShaMatches("a0f8def")
        )
    }

    @Test
    internal fun `taggedCommitShaMatches success`() {
        assertTrue(
            "taggedCommitSha must match short SHA",
            Version("v1.0.1", "v", null, "97904f87f8889a12d11c1ec3a0f8def2f28ce762").taggedCommitShaMatches("97904f8")
        )
    }
}
