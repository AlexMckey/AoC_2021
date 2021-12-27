package day08.indicators

import AoC_Lib.SomeDay
import AoC_Lib.toStrs
import AoC_Lib.words

object Day08: SomeDay(2021,8) {
    override val title = "Seven Segment Search"

    override fun first(data: String): Any? {
        val uniqIndLens = listOf(2,3,4,7)
        return data.toStrs()
            .joinToString(" ") { it.substringAfter(" | ") }
            .words()
            .count { it.length in uniqIndLens }
    } // 239 Time: 75ms

    override fun second(data: String): Any? {
        val signals = data.toStrs()
            .map { it.split(" | ")
                .let { it.first() to it.last() } }
        val indicators = listOf("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
            .mapIndexed{ index, s -> s to index }.toMap()
        return signals.map { (input,output) ->
            val sl = input.words()
                .map { it.length to it.toSet() }
                .groupBy({it.first} , {it.second})
                .mapValues { it.value.reduce { acc, set ->  acc.intersect(set) } }
            val a = sl[3]!! - sl[2]!!
            val d = sl[5]!! - sl[6]!!
            val b = sl[4]!! - sl[2]!! - d
            val f = sl[6]!! - sl[5]!! - b
            val g = sl[6]!! intersect sl[5]!! - a
            val c = sl[2]!! - f
            val e = sl[7]!! - sl[4]!! - a - g
            val decs = listOf(a,b,c,d,e,f,g)
                .map{ it.single() }
                .zip("abcdefg".toList())
                .toMap()
            output.split(' ')
                .map { it
                    .map{ decs[it]!! }
                    .sorted()
                    .joinToString("") }
                .map { indicators[it] }
                .joinToString("")
                .toInt()
        }.sum()
    } // 946346 Time: 67ms
}

fun main() = SomeDay.mainify(Day08)