package day06.fishs

import AoC_Lib.SomeDay
import AoC_Lib.toInts

object Day06: SomeDay(2021,6) {
    override val title = "Lanternfish"

    data class LanternFish(val init: List<Int>) {
        val state: Array<Long> = Array(9) { i ->
            val m = init.groupingBy { it }.eachCount()
            (if (m.containsKey(i)) m[i] else 0)?.toLong()!! }
        fun nextDay(step: Int = 1): Array<Long> =
            if (step == 0) state
            else {
                val newGen = state[0]
                for (i in 0 .. 7) state[i] = state[i+1]
                state[8] = newGen
                state[6] += newGen
                nextDay(step - 1)
            }
        fun population(): Long = state.sum()
    }

    override fun first(data: String): Any? {
        val ls = data.replace(',','\n').toInts()
        val fishs = LanternFish(ls)
        fishs.nextDay(80)
        return fishs.population()
    } // 375482 Time: 105ms

    override fun second(data: String): Any? {
        val ls = data.replace(',','\n').toInts()
        val fishs = LanternFish(ls)
        fishs.nextDay(256)
        return fishs.population()
    } // 1689540415957 Time: 6ms
}

fun main() = SomeDay.mainify(Day06)