package util

import model.CommitInfo
import model.Version
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import java.io.File
import java.io.IOException

private const val SHORT_SHA_LENGTH = 8

@Throws(IOException::class)
fun getCurrentVersion(projectDir: File, prefix: String?, buildMetadataSeparator: String?): Version {
    val prefixString = if (prefix.isNullOrBlank()) "" else prefix
    val zeroVersion = Version(prefixString + "0.0.0", prefix, buildMetadataSeparator)

    val repository = getRepository(projectDir)
        ?: return zeroVersion

    val walk = RevWalk(repository)

    val tags = repository.refDatabase.getRefsByPrefix(Constants.R_TAGS)
        .asSequence()
        // if ref is a lightweight (aka unannotated) tag it can be ignored
        .mapNotNull { runCatching { walk.parseTag(it.objectId) }.getOrNull() }
        .filter { it.tagName.startsWith(prefixString) }
        .mapNotNull { tag ->
            runCatching {
                Version(tag.tagName, prefix, buildMetadataSeparator, tag.`object`.id.name)
            }.getOrNull()
        }
        .filter { it.preRelease.isNullOrBlank() && it.buildMetadata.isNullOrBlank() }
        .sortedWith(VersionComparator)
        .toList()

    walk.dispose()

    return tags.lastOrNull() ?: zeroVersion
}

@Throws(IOException::class)
fun getHeadCommitInfo(projectDir: File): CommitInfo {
    val emptyCommitInfo = CommitInfo(null, null, null, null)
    val repository = getRepository(projectDir)
    val headCommit = repository?.resolve(Constants.HEAD)
        ?: return emptyCommitInfo

    return runCatching {
        RevWalk(repository).use { walk ->
            val commit = walk.parseCommit(headCommit)
            val shortSha = commit.id.name.substring(IntRange(0, SHORT_SHA_LENGTH - 1))
            walk.dispose()
            CommitInfo(
                commit.fullMessage,
                shortSha,
                commit.authorIdent.name,
                commit.authorIdent.emailAddress,
            )
        }
    }.getOrElse { e ->
        throw IOException("Failed to read HEAD commit info", e)
    }
}

@Throws(IOException::class)
fun getBranchName(projectDir: File): String {
    val repository = getRepository(projectDir)
    return repository?.branch ?: ""
}

fun tagHeadCommit(projectDir: File, version: String, message: String) {
    val repository = getRepository(projectDir)
        ?: throw IllegalArgumentException("No Git repository found (JGit needs a .git directory).")

    Git(repository)
        .tag()
        .setName(version)
        .setMessage(message)
        .setAnnotated(true)
        .setForceUpdate(true)
        .call()
}

@Throws(IOException::class)
fun getRepository(projectDir: File): Repository? =
    runCatching {
        FileRepositoryBuilder()
            .findGitDir(projectDir.absoluteFile)
            .build()
    }.getOrElse { e ->
        if (e is IllegalArgumentException && isJGitException(e)) {
            null
        } else {
            throw e
        }
    }

/**
 * JGit throws an IllegalArgumentException if there is no Git repo (message with JGit 6.1.0.202203080745-r is
 * "One of setGitDir or setWorkTree must be called").
 */
private fun isJGitException(e: Exception): Boolean =
    e.message?.lowercase()?.contains("gitdir") == true
            && e.stackTrace?.any { it.className.lowercase().contains("jgit") } == true
