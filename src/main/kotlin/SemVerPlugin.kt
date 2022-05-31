import extension.SemVerExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import task.DisplayVersion
import task.NextVersionTask
import task.TagHeadCommitTask

class SemVerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("semver", SemVerExtension::class.java, project)

            afterEvaluate {
                val nextVersionTask = tasks.create("nextVersion", NextVersionTask::class.java)
                nextVersionTask.resolveNextVersion()

                tasks.register("tagHeadCommit", TagHeadCommitTask::class.java)
                tasks.register("displayVersion", DisplayVersion::class.java)

                // this task needs git to be configured outside of gradle.
                tasks.register("pushTagToOrigin", Exec::class.java) { task ->
                    task.group = "Versioning"
                    task.description = "Pushes the tags to origin."
                    task.commandLine = listOf("git", "push", "origin", "--tags")
                }
            }
        }
    }

}