package day01.depths

import AoC_Lib.SomeDay
import AoC_Lib.toInts

object Day01: SomeDay(2021,1) {
    override val title = "Sonar Sweep"

    override fun first(data: String): Any? {
        return data.toInts()
            .zipWithNext(Int::compareTo)
            .count{it<0}
    } // 1448 Time: 32ms

    override fun second(data: String): Any? {
        val i = data.toInts()
        return i.zip(i.drop(3))
            .count{it.first < it.second}
    } // 1471 Time: 3ms
}

fun main() = SomeDay.mainify(Day01)