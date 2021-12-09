package AoC_Lib

fun <T: Comparable<T>> List<T>.median(): T {
    val l = this.sortedBy { it }
    val s = this.size
    return l[s/2]
}