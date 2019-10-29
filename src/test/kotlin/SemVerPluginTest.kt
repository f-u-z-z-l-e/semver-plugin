import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files.isDirectory

class SemVerPluginTest : AbstractPluginTest() {

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        assertTrue(isDirectory(projectDir.toPath()))
        assertTrue(isDirectory(remoteDir.toPath()))

        createInitialCommit()
        createRemote()
    }

    @Test
    @Throws(Exception::class)
    fun `Apply version to project`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("properties")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("version: 0.0.1"))
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))
    }

    @Test
    fun `Tag repository with latest version`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("tagHeadCommit")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/0.0.1" }
        assertThat(tags.size, equalTo(1))
    }

    @Test
    fun `Push tag to remote repository`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")
        pushCommit()

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("tagHeadCommit", "pushTagToOrigin")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("2 actionable tasks: 2 executed"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/0.0.1" }
        assertThat(tags.size, equalTo(1))
    }

    @Test
    fun `Display version to cmd`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("displayVersion")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))
        assertThat(result.output, containsString("0.0.1"))
    }

    @Test
    fun `Increase minor version with commit message`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Increase #minor version.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("tagHeadCommit", "pushTagToOrigin", "displayVersion")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("3 actionable tasks: 3 executed"))
        assertThat(result.output, containsString("0.1.0"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/0.1.0" }
        assertThat(tags.size, equalTo(1))
    }

    @Test
    fun `Increase major version with commit message`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Increase #major version.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("tagHeadCommit", "pushTagToOrigin", "displayVersion")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("3 actionable tasks: 3 executed"))
        assertThat(result.output, containsString("1.0.0"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/1.0.0" }
        assertThat(tags.size, equalTo(1))
    }

    @Test
    fun `Display pre-release and build metadata on branch`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")
        createBranch("pre-release-test")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("displayVersion")
                .forwardOutput()
                .withJaCoCo()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))
        assertThat(result.output, containsString("0.0.1-preReleaseTest+"))
    }
}
