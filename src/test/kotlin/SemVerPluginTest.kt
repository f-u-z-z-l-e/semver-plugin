import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Files.isDirectory

class SemVerPluginTest : AbstractPluginTest() {

    @TempDir
    lateinit var testProjectDir: File

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        assertTrue(isDirectory(testProjectDir.toPath()))
        projectDir = testProjectDir

        createInitialCommit()
    }

    @Test
    @Throws(Exception::class)
    fun shouldApplyVersionToProject() {
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
                .build()

        // then
        assertThat(result.output, containsString("version: 0.0.1"))
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))
    }

    @Test
    fun shouldTagRepositoryWithLatestVersion() {
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
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("1 actionable task: 1 executed"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/0.0.1" }
        assertThat(tags.size, equalTo(1))
    }

    @Test
    fun shouldPushTagToRemoteRepository() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = "plugins { id 'ch.fuzzle.gradle.semver' }$eol"

        writeFile(buildFile, buildFileContent)
        createCommit("Add plugin definition.")

        // when
        val result = GradleRunner.create()
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withArguments("tagHeadCommit", "pushTagToOrigin")
                .forwardOutput()
                .build()

        // then
        assertThat(result.output, containsString("BUILD SUCCESSFUL"))
        assertThat(result.output, containsString("2 actionable tasks: 2 executed"))

        val tags = git.tagList().call().filter { it.name == "refs/tags/0.0.1" }
        assertThat(tags.size, equalTo(1))
    }

}
