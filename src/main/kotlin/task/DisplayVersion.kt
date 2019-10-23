package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

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
