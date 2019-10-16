package task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import util.getStringOrDefault
import util.pushVersionTagToOrigin

open class PushTagToOriginTask : DefaultTask() {

    @Input
    val oauthToken: String?

    init {
        group = "Versioning"
        description = "Pushes the created next version tag to origin."
        oauthToken = getStringOrDefault(project.properties["oauthToken"] as String?, "")
    }

    @TaskAction
    fun pushTagToOrigin() {
        pushVersionTagToOrigin(project.projectDir, oauthToken)
    }
}