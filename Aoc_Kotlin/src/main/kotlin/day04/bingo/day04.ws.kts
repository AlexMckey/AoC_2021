val s = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n" +
        "\n" +
        "22 13 17 11  0\n" +
        " 8  2 23  4 24\n" +
        "21  9 14 16  7\n" +
        " 6 10  3 18  5\n" +
        " 1 12 20 15 19\n" +
        "\n" +
        " 3 15  0  2 22\n" +
        " 9 18 13 17  5\n" +
        "19  8  7 25 23\n" +
        "20 11 10 24  4\n" +
        "14 21 16 12  6\n" +
        "\n" +
        "14 21 17 24  4\n" +
        "10 16 15  9 19\n" +
        "18  8 23 26 20\n" +
        "22 11 13  6  5\n" +
        " 2  0 12  3  7"
val ss = s.split("\n\n")
val nums = ss.first().split(',').map(String::toInt)
nums
val bs = ss
    .drop(1)
    .map { it
        .lines()
        .map { it
            .trim()
            .replace("  ", " ")
            .split(' ')
            .map { it.toInt() }} }
bs.size
bs.first().size
bs.first().first().size

data class Board(val rows: List<Set<Int>>, val cols: List<Set<Int>>) {
    fun mark(n: Int): Board =
        Board(rows.map { it - n }, cols.map { it - n })
    fun isWon(): Boolean = rows.any{ it.isEmpty() } || cols.any { it.isEmpty() }
    fun sumMarked(): Int = rows.sumOf { it.sum() }
}
fun <T> List<List<T>>.transpose(): List<List<T>> {
    this.filter { it.isNotEmpty() }.let { ys ->
        return when (ys.isNotEmpty()) {
            true -> listOf(ys.map { it.first() })
                .plus(ys.map { it.drop(1) }.transpose())
            else -> emptyList()
        }
    }
}
bs.first().transpose()
val rs = bs.first().map { it.toSet() }
val cs = bs.first().transpose().map { it.toSet() }
rs.map { it.toList() }.transpose().map { it.toSet() }
cs

val boards = bs.map {
    val rs = it.map { it.toSet() }
    val cs = it.transpose().map { it.toSet() }
    Board(rs,cs)
}
boards[0].isWon()
boards[0].sumMarked()
boards[0].mark(22)
val b3 = boards[2]
b3

val (idx, sum) = nums
    .scan(b3){ b, n -> b.mark(n) }
    .withIndex()
    .first { it.value.isWon() }
    .let { it.index to it.value.sumMarked() }
idx
nums[idx-1]
sum

boards.map { b -> nums
    .scan(b){ accb, n -> accb.mark(n) }
    .withIndex()
    .first { it.value.isWon() }
    .let { it.index to it.value.sumMarked() * nums[it.index-1] } }