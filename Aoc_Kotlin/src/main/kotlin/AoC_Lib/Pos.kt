package AoC_Lib

import kotlin.math.abs

data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    operator fun plus(other: Pair<Int, Int>) = Pos(other.first + x, other.second + y)
    operator fun minus(other: Pos) = Pos(other.x - x, other.y - y)
    operator fun plusAssign(other: Pos) = run { x += other.x; y += other.y }
    operator fun minusAssign(other: Pos) = run { x -= other.x; y -= other.y }
    operator fun unaryMinus() = Pos(-x, -y)
    operator fun times(other: Int) = Pos(x * other, y * other)
    operator fun times(other: Pos) = Pos(x * other.x, y * other.y)
    operator fun timesAssign(other: Int) = run { x *= other; y *= other }

    fun manhattanDistance(p2: Pos = Zero): Int {
        return abs(this.x - p2.y) + abs(this.y - p2.y)
    }

    companion object {
        fun toPos(p: Pair<Int,Int>): Pos = Pos(p.first, p.second)
        fun fromString(s: String): Pos = s
            .split(',')
            .map(String::toInt)
            .let { Pos(it.first(), it.last()) }
        val Zero = Pos(0,0)
        val Start = Zero
    }
}

fun Pos.toPair(): Pair<Int,Int> = this.x to this.y