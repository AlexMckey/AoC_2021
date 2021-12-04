package day03.bintrans

import AoC_Lib.SomeDay
import AoC_Lib.toStrs

object Day03: SomeDay(2021,3) {
    override val title = "Binary Diagnostic"

    override fun first(data: String): Any? {
        val report = data.toStrs()
        val gammaBin = report
            .first()
            .indices
            .map { i -> report
                .map { it[i] } }
            .map {
                if (it.count { it == '1' } * 2 > report.size) '1'
                else '0' }
            .joinToString("")
        val gamma = Integer.parseInt(gammaBin,2)
        val epsilon = (1 shl report.first().length) - gamma - 1
        return gamma * epsilon
    } // 4160394 Time: 33ms

    enum class Rate(val checkBits: (Int, Int) -> Boolean) {
        Oxygen({ b1,b0 -> b1 >= b0 }),
        CO2({ b1, b0 -> b1 < b0 })
    }

    override fun second(data: String): Any? {
        fun calcRate(rep: List<String>, r: Rate): Int =
            Integer.parseInt(
                rep.first().indices.fold(rep){
                    l, i -> if (l.size == 1) l
                            else {
                                val bp = l.partition { it[i] == '1' }
                                val res = if (r.checkBits(bp.first.size, bp.second.size))
                                    bp.first
                                else bp.second
                                res
                            }
            }.joinToString(""),2)

        val report = data.toStrs()
        val oxygen = calcRate(report,Rate.Oxygen)
        val CO2 = calcRate(report,Rate.CO2)
        return oxygen * CO2
    } // 4125600 Time: 4ms
}

fun main() = SomeDay.mainify(Day03)