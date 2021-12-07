package day07.sumSeqs

import AoC_Lib.SomeDay
import AoC_Lib.mediana
import AoC_Lib.toInts
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

object Day07: SomeDay(2021,7) {
    override val title = "The Treachery of Whales"

    override fun first(data: String): Any? {
        val nums = data.replace(',','\n').toInts()
        val mediana = nums.mediana()//nums.sorted()[nums.size/2]
        return nums.sumOf { (it - mediana).absoluteValue }
    } // 343468 Time: 71ms

    override fun second(data: String): Any? {
        val nums = data.replace(',','\n').toInts()
        val avg = nums.average().roundToInt()
        return nums.sumOf { it
            .let { (it - avg).absoluteValue }
            .let { (it * it + it) / 2 }}
    } // 96086306 Time: 18ms
}

fun main() = SomeDay.mainify(Day07)