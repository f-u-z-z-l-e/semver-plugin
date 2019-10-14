package task

import extension.SemVerExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import util.getStringOrDefault
import util.tagHeadCommit

open class TagHeadCommitTask : DefaultTask() {

    @Input
    val tagMessage: String

    private val defaultTagMessage: String = "Tagged by 'ch.fuzzle.gradle.semver' gradle plugin."

    init {
        group = "Versioning"
        description = "Tags the latest commit with the current project version."

        val semVerExtension = project.extensions.getByType(SemVerExtension::class.java)
        this.tagMessage = getStringOrDefault(semVerExtension.tagMessage.orNull, defaultTagMessage) + "\n"
    }

    @TaskAction
    fun tagHeadCommit() {
        tagHeadCommit(project.projectDir, project.version.toString(), tagMessage)
    }

}
