package day02.dive

import AoC_Lib.Pos
import AoC_Lib.SomeDay
import AoC_Lib.toStrs

object Day02: SomeDay(2021,2) {
    override val title = "Dive!"

    private fun String.toPos(): Pos = this
            .split(" ")
            .let { when (it[0]) {
                "forward" -> Pos(it[1].toInt(),0)
                "up" -> Pos(0, -it[1].toInt())
                "down" -> Pos(0, it[1].toInt())
                else -> Pos.Zero
            }}

    override fun first(data: String): Any? {
        return data.toStrs()
            .map { it.toPos() }
            .fold(Pos.Zero, Pos::plus)
            .let { it.x * it.y }
    } // 2215080 Time: 36ms

    override fun second(data: String): Any? {
        return data.toStrs()
            .map { it.toPos() }
            .fold(0 to Pos.Zero) {
                (aim,pos), cur ->
                    if (cur.x == 0)
                        aim + cur.y to pos
                    else
                        aim to pos + Pos(cur.x,cur.x * aim)
            }
            .let { it.second.x * it.second.y }
    } // 1864715580 Time: 3ms
}

fun main() = SomeDay.mainify(Day02)