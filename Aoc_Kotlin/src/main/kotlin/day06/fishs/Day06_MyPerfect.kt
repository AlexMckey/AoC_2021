package day06.fishs

import AoC_Lib.SomeDay
import AoC_Lib.toInts

object Day06MyPerfect: SomeDay(2021,6) {
    override val title = "Lanternfish"

    private fun calcPopulation(init: List<Int>, d: Int): Long {
        val m = init.groupingBy { it }.eachCount()
        val a = Array(9)
            { i -> m.getOrDefault(i,0).toLong() }
        (0 until d).forEach { a[(it+7)%9] += a[it%9] }
        return a.sum()
    }

    override fun first(data: String): Any? {
        val ls = data.replace(',','\n').toInts()
        return calcPopulation(ls,80)
    } // 375482 Time: 66ms

    override fun second(data: String): Any? {
        val ls = data.replace(',','\n').toInts()
        return calcPopulation(ls,256)
    } // 1689540415957 Time: 5ms
}

fun main() = SomeDay.mainify(Day06MyPerfect)