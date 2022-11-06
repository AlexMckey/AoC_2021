package day19.sensors

import AoC_Lib.SomeDay

object Day19: SomeDay(2021, 19) {
    override val title = "Beacon Scanner"

    override fun first(data: String): Any? = 0
    // 4008 Time: 70ms

    override fun second(data: String): Any? = 0
    // 4667 Time: 166ms
}

fun main() = SomeDay.mainify(Day19)