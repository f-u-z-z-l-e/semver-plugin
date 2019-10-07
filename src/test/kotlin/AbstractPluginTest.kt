import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

abstract class AbstractPluginTest {

    lateinit var projectDir: File
    lateinit var buildFile: File
    private lateinit var git: Git

    @Throws(GitAPIException::class)
    fun createInitialCommit() {
        git = Git.init().setDirectory(projectDir).call()
        git.add().addFilepattern("*").call()
        git.commit()
                .setSign(false)
                .setMessage("Add build.gradle file.")
                .call()
    }

    @Throws(IOException::class)
    fun writeFile(destination: File, content: String) {
        BufferedWriter(FileWriter(destination)).use { it.write(content) }
    }

}
