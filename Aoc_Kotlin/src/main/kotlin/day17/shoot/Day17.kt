package day17.shoot

import AoC_Lib.Pos
import AoC_Lib.SomeDay
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

object Day17: SomeDay(2021,17) {
    override val title = "Trick Shot"

    private fun parseTarget(s: String): Pair<IntRange,IntRange>{
        val r = """target area: x=(\d+)..(\d+), y=(-?\d+)..(-?\d+)""".toRegex()
        val (xmin,xmax,ymin,ymax) = r.matchEntire(s)!!.destructured
        val xR = xmin.toInt() .. xmax.toInt()
        val yR = ymin.toInt() .. ymax.toInt()
        return xR to yR
    }

    private fun initialVelocities(xR: IntRange, yR: IntRange): List<Pos> =
        (1 .. xR.last)
            .flatMap { xv ->  (yR.first..yR.first.absoluteValue)
                .map {yv -> Pos(xv,yv) }}

    private tailrec fun shoot(xR: IntRange, yR: IntRange, pos: Pos, vel: Pos, maxY: Int = 0): Int? {
        fun next(pos: Pos, vel: Pos): Pair<Pos,Pos> = (pos + vel) to (vel - Pos(vel.x.sign,1))
        return if (pos.x in xR && pos.y in yR) maxY
        else if (vel.y < 0 && pos.y < yR.first) null
        else {
            val (np, nv) = next(pos, vel)
            shoot(xR, yR, np, nv, max(maxY,np.y))
        }
    }

    override fun first(data: String): Any? {
        val (xR,yR) = parseTarget(data)
        return initialVelocities(xR,yR)
            .mapNotNull { shoot(xR, yR, Pos.Zero, it) }
            .maxOrNull()
    } // 23005 Time: 85ms

    override fun second(data: String): Any? {
        val (xR,yR) = parseTarget(data)
        return initialVelocities(xR,yR)
            .mapNotNull { shoot(xR, yR, Pos.Zero, it) }
            .size
    } // 2040 Time: 37ms
}

fun main() = SomeDay.mainify(Day17)