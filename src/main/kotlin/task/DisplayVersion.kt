package task

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "This task only prints the version to stdout")
abstract class DisplayVersion : DefaultTask() {
    @get:Input
    abstract val version: Property<String>

    init {
        group = "Versioning"
        description = "Displays the current project version."
    }

    @TaskAction
    fun displayVersion() {
        println(version.get())
    }
}
