import org.gradle.testkit.runner.GradleRunner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Files.isDirectory

class SemVerPluginNoGitTest : AbstractPluginTest() {

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        assertTrue(isDirectory(projectDir.toPath()))
        assertTrue(isDirectory(remoteDir.toPath()))

        buildFile = Files.createFile(projectDir.toPath().resolve("build.gradle.kts")).toFile()
    }

    @Test
    fun `Display version to cmd without Git repository present`() {
        // given
        val eol = System.getProperty("line.separator")
        val buildFileContent = """plugins { id("ch.fuzzle.gradle.semver") }$eol"""

        writeFile(buildFile, buildFileContent)

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
}
