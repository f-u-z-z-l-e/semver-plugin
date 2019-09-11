package util

import java.util.regex.Matcher
import java.util.regex.Pattern

private const val semanticVersionRegex = "(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(-[a-zA-Z\\d][-a-zA-Z.\\d]*)?(\\+[a-zA-Z\\d][-a-zA-Z.\\d]*)?"

private fun semanticVersionPattern(prefix: String?): Pattern {
    return if (prefix.isNullOrEmpty()) {
        Pattern.compile("^$semanticVersionRegex$")
    } else Pattern.compile("^$prefix$semanticVersionRegex$")

}

fun matchSemanticVersion(version: String, prefix: String?): Matcher {
    return semanticVersionPattern(prefix).matcher(version)
}
