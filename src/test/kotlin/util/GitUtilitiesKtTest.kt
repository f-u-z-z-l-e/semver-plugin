package util

import org.junit.jupiter.api.Assertions.assertTrue
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
        assertTrue(!branchName.isNullOrEmpty(), "Branch name could not be acquired.")
    }

    @Test
    fun `Return head CommitInfo object`() {
        // given
        val projectDir = File("../../../../")

        // when
        val commitInfo = getHeadCommitInfo(projectDir)

        // then
        assertTrue(!commitInfo.message.isNullOrEmpty(), "Head commit message could be acquired,")
        assertTrue(!commitInfo.sha.isNullOrEmpty(), "Head commit sha could be acquired,")
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
        assertTrue(currentVersion.toString() == "nonexistent_prefix0.0.0", "Default version could not be acquired.")
    }

    /** This test only works as long as nobody messes with the tags in this repository with the prefix 'test'. */
    @Test
    fun `Return current Version object with 'test' prefix`() {
        // given
        val projectDir = File("../../../../")

        // when
        val currentVersion = getCurrentVersion(projectDir, "test")

        // then
        assertTrue(currentVersion.toString() == "test0.1.0", "Current version with prefix 'test' could not be acquired.")
    }

}