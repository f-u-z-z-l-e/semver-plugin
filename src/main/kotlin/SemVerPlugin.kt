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

            tasks.register("tagHeadCommit", TagHeadCommitTask::class.java)
            tasks.register("displayVersion", DisplayVersion::class.java)

            // this task needs git to be configured outside of gradle.
            tasks.register("pushTagToOrigin", Exec::class.java) { task ->
                task.group = "Versioning"
                task.description = "Pushes the tags to origin."
                task.commandLine = listOf("git", "push", "origin", "--tags")
            }

            /**
             * If we do this in afterEvaluate, this plugin must come before the maven-publish plugin in the Gradle build
             * script. Doing it in afterProject is more robust, no need to pay attention to the sequence of plugins.
             */
            gradle.afterProject {
                val nextVersionTask = tasks.create("nextVersion", NextVersionTask::class.java)
                nextVersionTask.resolveNextVersion()
            }
        }
    }
}