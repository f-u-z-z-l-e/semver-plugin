import extension.SemVerExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import task.NextVersionTask
import task.PushTagToOriginTask
import task.TagHeadCommitTask

class SemVerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("semver", SemVerExtension::class.java, project)

            afterEvaluate {
                val nextVersionTask = tasks.create("nextVersion", NextVersionTask::class.java)
                nextVersionTask.resolveNextVersion()

                tasks.create("tagHeadCommit", TagHeadCommitTask::class.java)
                tasks.create("pushTagToOrigin", PushTagToOriginTask::class.java)
            }
        }
    }

}