package util

import model.CommitInfo
import model.Version
import org.eclipse.jgit.errors.IncorrectObjectTypeException
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevTag
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.io.IOException

@Throws(IOException::class)
fun getCurrentVersion(projectDir: File, prefix: String?): Version {
    val prefixString = if (prefix.isNullOrBlank()) "" else prefix

    val repository = getRepository(projectDir)
    val walk = RevWalk(repository)

    val tags = repository.refDatabase.getRefsByPrefix(Constants.R_TAGS)
            .asSequence()
            .map {
                try {
                    walk.parseTag(it.objectId)
                } catch (e: IncorrectObjectTypeException) {
                    // ref is a lightweight (aka unannotated) tag and thus ignored.
                }
            }
            .filterIsInstance<RevTag>()
            .map { it.tagName }
            .filter { it.startsWith(prefixString) }
            .map {
                try {
                    Version(it, prefix)
                } catch (e: IllegalStateException) {
                    // ignore tags not matching semver regex.
                }
            }
            .filterIsInstance<Version>()
            .sortedWith(VersionComparator)
            .toList()

    walk.dispose()

    if (tags.isEmpty()) return Version(prefixString + "0.0.0", prefix)

    return tags.last()
}

@Throws(IOException::class)
fun getHeadCommitInfo(projectDir: File): CommitInfo {
    val repository = getRepository(projectDir)
    val headCommit = repository.resolve(Constants.HEAD)

    try {
        RevWalk(repository).use { walk ->
            val commit = walk.parseCommit(headCommit)
            val commitInfo = CommitInfo(
                    commit.fullMessage,
                    commit.id.name.substring(IntRange(0, 7)),
                    commit.authorIdent.name,
                    commit.authorIdent.emailAddress
            )

            walk.dispose()
            return commitInfo
        }
    } catch (e: IOException) {
        // exception ignored intentionally.
        return CommitInfo(null, null, null, null)
    }
}

@Throws(IOException::class)
fun getBranchName(projectDir: File): String? {
    val repository = getRepository(projectDir)
    return repository.branch
}

@Throws(IOException::class)
private fun getRepository(projectDir: File): Repository {
    return FileRepositoryBuilder()
            .readEnvironment()
            .findGitDir(File(projectDir.absolutePath + "/.git"))
            .build()
}
