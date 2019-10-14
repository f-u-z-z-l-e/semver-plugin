package util

fun getStringOrDefault(string: String?, default: String): String {
    return if (string != null && string.isNotBlank()) string
    else default
}