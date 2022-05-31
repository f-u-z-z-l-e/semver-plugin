import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.transport.RemoteConfig
import org.eclipse.jgit.transport.URIish
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.io.*
import java.nio.file.Files

abstract class AbstractPluginTest {

    @TempDir
    lateinit var testDir: File

    lateinit var projectDir: File
    lateinit var remoteDir: File

    lateinit var buildFile: File
    lateinit var git: Git

    @BeforeEach
    @Throws(Exception::class)
    fun setupProjectDirectories() {
        projectDir = Files.createDirectory(testDir.toPath().resolve("local")).toFile()
        remoteDir = Files.createDirectory(testDir.toPath().resolve("remote")).toFile()
    }

    @Throws(GitAPIException::class)
    fun createInitialCommit() {
        buildFile = Files.createFile(projectDir.toPath().resolve("build.gradle.kts")).toFile()

        git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern("*").call()
        git.commit()
                .setSign(false)
                .setMessage("Add build.gradle file.")
                .call()
    }

    fun createRemote() {
        val remoteUrl = Files.createDirectory(remoteDir.toPath().resolve(".git")).toUri().toURL()
        val config = git.repository.config
        val remoteConfig = RemoteConfig(config, "origin")
        val uri = URIish(remoteUrl)
        remoteConfig.addURI(uri)
        remoteConfig.update(config)
        config.save()

        Git.init().setDirectory(remoteDir).call()
    }

    @Throws(GitAPIException::class)
    fun createCommit(message: String?) {
        git.add().addFilepattern("*").call()
        git.commit()
                .setSign(false)
                .setMessage(message)
                .call()
    }

    fun pushCommit() {
        git.push().call()
    }

    fun createBranch(branchName: String) {
        git.checkout().setCreateBranch(true).setName(branchName).call()
    }

    @Throws(IOException::class)
    fun writeFile(destination: File, content: String) {
        BufferedWriter(FileWriter(destination)).use { it.write(content) }
    }

    private fun InputStream.toFile(file: File) {
        use { input ->
            file.outputStream().use { input.copyTo(it) }
        }
    }

    fun GradleRunner.withJaCoCo(): GradleRunner {
        javaClass.classLoader.getResourceAsStream("testkit-gradle.properties")?.toFile(File(projectDir, "gradle.properties"))
        return this
    }
}
