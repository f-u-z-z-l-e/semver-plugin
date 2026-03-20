package util

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Regular expression copied from:
 * https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
 */
private const val SEMANTIC_VERSION_REGEX =
    "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)" +
            "(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))" +
            "?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$"

fun semanticVersionPattern(prefix: String?): Pattern =
    if (prefix.isNullOrEmpty()) {
        Pattern.compile("^$SEMANTIC_VERSION_REGEX$")
    } else {
        Pattern.compile("^$prefix$SEMANTIC_VERSION_REGEX$")
    }

fun matchSemanticVersion(
    version: String,
    prefix: String?,
): Matcher = semanticVersionPattern(prefix).matcher(version)
