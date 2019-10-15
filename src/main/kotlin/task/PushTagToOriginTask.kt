package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import util.pushVersionTagToOrigin

open class PushTagToOriginTask : DefaultTask() {

    init {
        group = "Versioning"
        description = "Pushes the created next version tag to origin."
    }

    @TaskAction
    fun pushTagToOrigin() {
        pushVersionTagToOrigin(project.projectDir)
    }
}