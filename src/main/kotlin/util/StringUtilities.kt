package util

fun getStringOrDefault(string: Any?, default: String): String {
    return if (string is String && string.isNotBlank()) string
    else default
}

fun String.toPreReleaseString(): String {
    val stringList = this.split('-')
    return if (stringList.size > 1) stringList[0] + stringList.subList(1, stringList.size).joinToString("") {
        when {
            it[0].isDigit() -> "_$it"
            else -> it.capitalize()
        }
    } else this
}