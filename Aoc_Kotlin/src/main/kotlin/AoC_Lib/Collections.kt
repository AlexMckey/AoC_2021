package AoC_Lib

fun <T> List<List<T>>.transpose(): List<List<T>> {
    this.filter { it.isNotEmpty() }.let { ys ->
        return when (ys.isNotEmpty()) {
            true -> listOf(ys.map { it.first() })
                .plus(ys.map { it.drop(1) }.transpose())
            else -> emptyList()
        }
    }
}