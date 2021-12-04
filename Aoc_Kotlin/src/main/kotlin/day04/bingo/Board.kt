package day04.bingo

data class Board(val rows: List<Set<Int>>, val cols: List<Set<Int>>) {
    fun mark(n: Int): Board =
        Board(rows.map { it - n }, cols.map { it - n })
    fun isWon(): Boolean = rows.any{ it.isEmpty() } || cols.any { it.isEmpty() }
    fun sumMarked(): Int = rows.sumOf { it.sum() }
}
