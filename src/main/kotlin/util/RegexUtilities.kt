package util

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Regular expression copied from:
 * https://semver.org/#is-there-a-suggested-regular-expression-regex-to-check-a-semver-string
 */
private const val semanticVersionRegex = "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?\$"

private fun semanticVersionPattern(prefix: String?): Pattern {
    return if (prefix.isNullOrEmpty()) {
        Pattern.compile("^$semanticVersionRegex$")
    } else Pattern.compile("^$prefix$semanticVersionRegex$")

}

fun matchSemanticVersion(version: String, prefix: String?): Matcher {
    return semanticVersionPattern(prefix).matcher(version)
}
