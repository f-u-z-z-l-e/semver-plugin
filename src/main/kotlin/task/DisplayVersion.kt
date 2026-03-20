package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "This task only prints the version to stdout")
open class DisplayVersion : DefaultTask() {
    init {
        group = "Versioning"
        description = "Displays the current project version."
    }

    @TaskAction
    fun displayVersion() {
        println("${project.version}")
    }
}
