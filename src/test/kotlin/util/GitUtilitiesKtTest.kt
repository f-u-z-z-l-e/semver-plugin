package util

import org.eclipse.jgit.api.Git
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
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


    @Test
    fun `Tag the latest commit with the current version`() {
        // given
        val projectDir = File("../../../../")

        // when
        tagHeadCommit(projectDir, "vv1.0.0", "Some commit message.")

        // then
        assertTag("vv1.0.0")

        // clean up
        Git(getRepository(projectDir)).tagDelete().setTags("vv1.0.0").call()
    }

    @Test
    fun `Versions with PreRelease or BuildMetadata set should not increase version numbers `() {
        // given
        val projectDir = File("../../../../")

        // when
        tagHeadCommit(projectDir, "version1.0.0", "Some commit message.")
        assertTag("version1.0.0")
        assertTrue(getCurrentVersion(projectDir, "version").toString() == "version1.0.0")

        tagHeadCommit(projectDir, "version1.0.1-RC1", "Some commit message.")
        assertTag("version1.0.1-RC1")
        assertTrue(getCurrentVersion(projectDir, "version").toString() == "version1.0.0")

        // clean up
        Git(getRepository(projectDir)).tagDelete().setTags("version1.0.0", "version1.0.1-RC1").call()
    }

    private fun assertTag(tagName: String) {
        val projectDir = File("../../../../")
        val repository = getRepository(projectDir)
        val tags = Git(repository).tagList().call().filter { it.name == "refs/tags/$tagName" }
        assertThat(tags.size, Matchers.equalTo(1))
    }

}
