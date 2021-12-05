package day05.ovelapping

import AoC_Lib.SomeDay
import AoC_Lib.Line
import AoC_Lib.Pos
import AoC_Lib.toLines

object Day05: SomeDay(2021,5) {
    override val title = "Hydrothermal Venture"

    private fun makeLines(s: String): List<Line>  =
        s.lines()
            .map { it.split(" -> ")
                .map(Pos::fromString) }
            .flatMap { it.toLines() }

    override fun first(data: String): Any? {
        val ls = makeLines(data)
        return ls.filter(Line::isVH)
            .flatMap(Line::expanded)
            .groupingBy{it}.eachCount()
            .count { it.value > 1 }
    } // 5835 Time: 115ms

    override fun second(data: String): Any? {
        val ls = makeLines(data)
        return ls
            .flatMap(Line::expanded)
            .groupingBy{it}.eachCount()
            .count { it.value > 1 }
    } // 17013 Time: 124ms
}

fun main() = SomeDay.mainify(Day05)