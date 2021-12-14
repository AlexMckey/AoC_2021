package day04.bingo

import AoC_Lib.SomeDay

typealias ListGrid<T> = List<List<T>>

object Day04: SomeDay(2021,4) {
    override val title = "Giant Squid"

    private fun <T> ListGrid<T>.transpose(): ListGrid<T> =
        this.filter { it.isNotEmpty() }.let { ys ->
            return when (ys.isNotEmpty()) {
                true -> listOf(ys.map { it.first() })
                    .plus(ys.map { it.drop(1) }.transpose())
                else -> emptyList()
            }
        }

    fun makeBoards(bs: List<String>): List<Board> =
        bs.map { it
            .lines()
            .map { it
                .trim()
                .replace("  ", " ")
                .split(' ')
                .map { it.toInt() }}
            .let {
                val rs = it.map { it.toSet() }
                val cs = it.transpose().map { it.toSet() }
                Board(rs,cs) }}

    fun gameBingo(ns: List<Int>, bs: List<Board>): List<Pair<Int,Int>> =
        bs.map { b -> ns
            .scan(b){ accb, n -> accb.mark(n) }
            .withIndex()
            .first { it.value.isWon() }
            .let { it.index to it.value.sumMarked() * ns[it.index-1] } }

    override fun first(data: String): Any? {
        val ss = data.split("\n\n")
        val nums = ss.first().split(',').map(String::toInt)
        val boards = makeBoards(ss.drop(1))
        val res = gameBingo(nums,boards)
        return res.minByOrNull { it.first }!!.second
    } // 65325 Time: 73ms

    override fun second(data: String): Any? {
        val ss = data.split("\n\n")
        val nums = ss.first().split(',').map(String::toInt)
        val boards = makeBoards(ss.drop(1))
        val res = gameBingo(nums,boards)
        return res.maxByOrNull { it.first }!!.second
    } // 4624 Time: 19ms
}

fun main() = SomeDay.mainify(Day04)