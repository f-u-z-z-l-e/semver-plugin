package extension

import org.gradle.api.Project
import org.gradle.api.provider.Property

@Suppress("UnstableApiUsage")
open class SemVerExtension(project: Project) {

val prefix: Property<String> = project.objects.property(String::class.java)
val releaseBranch: Property<String> = project.objects.property(String::class.java)
val preRelease: Property<String> = project.objects.property(String::class.java)
val tagMessage: Property<String> = project.objects.property(String::class.java)

}