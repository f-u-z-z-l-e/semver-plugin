package task

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import util.tagHeadCommit

@DisableCachingByDefault(because = "This task tags the git repository, which is a side effect")
abstract class TagHeadCommitTask : DefaultTask() {
    @get:Input
    abstract val tagMessage: Property<String>

    @get:Input
    abstract val version: Property<String>

    @get:Internal
    abstract val projectDir: DirectoryProperty

    init {
        group = "Versioning"
        description = "Tags the latest commit with the current project version."
    }

    @TaskAction
    fun tagHeadCommit() {
        tagHeadCommit(projectDir.get().asFile, version.get(), tagMessage.get())
    }
}
