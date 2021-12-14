package day14.polymerization

import AoC_Lib.*

object Day14: SomeDay(2021,14) {
    override val title = "Extended Polymerization"

    private fun makePoly(polyPattern: String, rules: Map<Pair<Char,Char>,List<Pair<Char,Char>>>, cnt: Int): Long =
        generateSequence(polyPattern
            .zipWithNext()
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong()})
        { map -> map
            .flatMap { kv -> rules[kv.key]!!
                .map { it to kv.value } }
            .groupBy({ it.first } , { it.second })
            .mapValues { it.value.sum() }
        }.drop(cnt)
            .first()
            .map { it.key.second to it.value }
            .groupBy({ it.first } , { it.second })
            .mapValues { it.value.sum() }
            .let { it + (polyPattern[0] to it[polyPattern[0]]!! + 1) }
            .let { it.maxOf { it.value } - it.minOf { it.value } }
    private fun makeRules(rs: String): Map<Pair<Char,Char>,List<Pair<Char,Char>>> =
        rs.lines().associate {
            it
                .split(" -> ")
                .let {
                    (it.first().first() to it.first().last()) to
                            listOf(
                                it.first().first() to it.last().first(),
                                it.last().first() to it.first().last()
                            )
                }
        }

    override fun first(data: String): Any? {
        val (initPoly, rs) = data.split("\n\n")
        return makePoly(initPoly, makeRules(rs),10)
    } // 2027 Time: 35ms

    override fun second(data: String): Any? {
        val (initPoly, rs) = data.split("\n\n")
        return makePoly(initPoly, makeRules(rs),40)
    } // 2265039461737 Time: 6ms
}

fun main() = SomeDay.mainify(Day14)