package util

import org.gradle.internal.impldep.org.junit.Assert.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

internal class GitUtilitiesKtTest {

    @Test
    fun `Return checked out branch name`() {
        // given
        val projectDir = File("../../../../")

        // when
        val branchName = getBranchName(projectDir)

        // then
        assertTrue("Branch name could not be acquired.", !branchName.isNullOrEmpty())
    }

    @Test
    fun `Return head CommitInfo object`() {
        // given
        val projectDir = File("../../../../")

        // when
        val commitInfo = getHeadCommitInfo(projectDir)

        // then
        assertTrue("Head commit message could be acquired,", !commitInfo.message.isNullOrEmpty())
        assertTrue("Head commit sha could be acquired,", !commitInfo.sha.isNullOrEmpty())
    }

    /** This test only works as long as there is no tag in this projects git repository with the prefix
     * 'nonexistent_prefix'.
     *  */
    @Test
    fun `Return default Version object with 'nonexistent' prefix`() {
        // given
        val projectDir = File("../../../../")

        // when
        val currentVersion = getCurrentVersion(projectDir, "nonexistent_prefix")

        // then
        assertTrue("Default version could not be acquired.", currentVersion.toString() == "nonexistent_prefix0.0.0")
    }

    /** This test only works as long as nobody messes with the tags in this repository with the prefix 'test'. */
    @Test
    fun `Return current Version object with 'test' prefix`() {
        // given
        val projectDir = File("../../../../")

        // when
        val currentVersion = getCurrentVersion(projectDir, "test")

        // then
        assertTrue("Current version with prefix 'test' could not be acquired.", currentVersion.toString() == "test0.1.0")
    }

}
