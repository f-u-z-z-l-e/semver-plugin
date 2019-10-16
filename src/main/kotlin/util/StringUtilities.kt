package util

fun getStringOrDefault(string: Any?, default: String): String {
    return if (string is String && string.isNotBlank()) string
    else default
}