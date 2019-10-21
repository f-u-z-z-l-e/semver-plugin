import extension.SemVerExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import task.NextVersionTask
import task.TagHeadCommitTask

class SemVerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("semver", SemVerExtension::class.java, project)

            afterEvaluate {
                val nextVersionTask = tasks.create("nextVersion", NextVersionTask::class.java)
                nextVersionTask.resolveNextVersion()

                tasks.create("tagHeadCommit", TagHeadCommitTask::class.java)

                // this task needs git to be configured outside of gradle.
                tasks.register<Exec>("pushTagToOrigin") {
                    group = "Versioning tasks"
                    description = "Pushes the tags to origin."
                    commandLine = listOf("git", "push", "origin", "--tags")
                }
            }
        }
    }

}