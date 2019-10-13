import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files

abstract class AbstractPluginTest {

    lateinit var projectDir: File
    lateinit var buildFile: File
    lateinit var git: Git

    @Throws(GitAPIException::class)
    fun createInitialCommit() {
        buildFile = Files.createFile(projectDir.toPath().resolve("build.gradle")).toFile()

        git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern("*").call()
        git.commit()
                .setSign(false)
                .setMessage("Add build.gradle file.")
                .call()
    }

    @Throws(GitAPIException::class)
    fun createCommit(message: String?) {
        git.add().addFilepattern("*").call()
        git.commit()
                .setSign(false)
                .setMessage(message)
                .call()
    }

    @Throws(IOException::class)
    fun writeFile(destination: File, content: String) {
        BufferedWriter(FileWriter(destination)).use { it.write(content) }
    }

}
