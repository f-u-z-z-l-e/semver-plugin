package util

import java.util.*

fun getStringOrDefault(string: Any?, default: String): String {
    return if (string is String && string.isNotBlank()) string
    else default
}

fun String.toPreReleaseString(): String {
    val stringList = this.split('-')
    return if (stringList.size > 1) stringList[0] + stringList.subList(1, stringList.size).joinToString("") { str ->
        when {
            str[0].isDigit() -> "_$str"
            else -> str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    } else this
}
