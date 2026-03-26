import extension.SemVerExtension
import model.CommitInfo
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import task.DisplayVersion
import task.TagHeadCommitTask
import util.getStringOrDefault
import valuesource.SemVerValueSource

class SemVerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val semVerExtension = extensions.create("semver", SemVerExtension::class.java, project)

            tasks.register("tagHeadCommit", TagHeadCommitTask::class.java) { task ->
                task.projectDir.set(project.layout.projectDirectory)
            }
            tasks.register("displayVersion", DisplayVersion::class.java)

            // this task needs git to be configured outside the Gradle build tool
            tasks.register("pushTagToOrigin", Exec::class.java) { task ->
                task.group = "Versioning"
                task.description = "Pushes the tags to origin."
                task.commandLine = listOf("git", "push", "origin", "--tags")
            }

            val semVerProvider = providers.of(SemVerValueSource::class.java) { spec ->
                spec.parameters { params ->
                    params.projectDir.set(project.projectDir)
                    params.prefix.set(
                        project.provider { getStringOrDefault(semVerExtension.prefix.orNull, "") }
                    )
                    params.releaseBranch.set(
                        project.provider { getStringOrDefault(semVerExtension.releaseBranch.orNull, "master") }
                    )
                    params.preRelease.set(
                        project.provider { getStringOrDefault(semVerExtension.preRelease.orNull, "") }
                    )
                    params.buildMetadataSeparator.set(
                        project.provider {
                            getStringOrDefault(semVerExtension.buildMetadataSeparator.orNull, "+")
                        }
                    )
                }
            }

            /**
             * afterProject is used instead of afterEvaluate so that this plugin does not need to come
             * before the maven-publish plugin in the Gradle build script.
             */
            gradle.afterProject {
                val result = semVerProvider.get()

                if (project.version == Project.DEFAULT_VERSION) {
                    project.version = result.version
                }

                project.extensions.extraProperties.set(
                    "gitCommitInfo",
                    CommitInfo(result.commitMessage, result.commitSha, result.commitAuthor, result.commitEmail),
                )

                val defaultTagMessage = "Tagged by 'ch.fuzzle.gradle.semver' gradle plugin."
                project.tasks.named("displayVersion", DisplayVersion::class.java) { task ->
                    task.version.set(result.version)
                }
                project.tasks.named("tagHeadCommit", TagHeadCommitTask::class.java) { task ->
                    task.tagMessage.set(
                        getStringOrDefault(semVerExtension.tagMessage.orNull, defaultTagMessage) + "\n"
                    )
                    task.version.set(result.version)
                }
            }
        }
    }
}
