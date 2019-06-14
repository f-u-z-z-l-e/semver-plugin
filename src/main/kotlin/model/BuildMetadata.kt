package model

data class BuildMetadata(val buildMetadata: String) {

    private val prefix = "+"

    override fun toString(): String {
        return "$prefix$buildMetadata"
    }

}


