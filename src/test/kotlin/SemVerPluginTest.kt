import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.GradleRunner
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
    }

}