package AoC_Lib

import kotlin.math.abs

enum class NearDir {
    Axis
    {
        override val ds: List<Pos> = allds.filter { it.x == 0 || it.y == 0 }
    },
    Diagonal
    {
        override val ds: List<Pos> = allds.filter { it.x != 0 && it.y != 0 }
    },
    All
    {
        override val ds: List<Pos> = allds
    };
    abstract val ds: List<Pos>
    private val r = -1..1
    protected val allds: List<Pos> = r.flatMap { x -> r.map { y -> Pos(x,y) } }
        .minus(Pos(0,0))
}

abstract class Nears<T> {
    abstract fun near(dir: NearDir = NearDir.All): List<T>
}

data class Pos(var x: Int = 0, var y: Int = 0): Nears<Pos>() {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    operator fun plus(other: Pair<Int, Int>) = Pos(other.first + x, other.second + y)
    operator fun minus(other: Pos) = Pos(x - other.x, y - other.y)
    operator fun plusAssign(other: Pos) = run { x += other.x; y += other.y }
    operator fun minusAssign(other: Pos) = run { x -= other.x; y -= other.y }
    operator fun unaryMinus() = Pos(-x, -y)
    operator fun times(other: Int) = Pos(x * other, y * other)
    operator fun times(other: Pos) = Pos(x * other.x, y * other.y)
    operator fun timesAssign(other: Int) = run { x *= other; y *= other }
    infix fun max(other: Pos): Pos = Pos(x.coerceAtLeast(other.x), y.coerceAtLeast(other.y))
    infix fun min(other: Pos): Pos = Pos(x.coerceAtMost(other.x), y.coerceAtMost(other.y))
    operator fun rem(other: Pos): Pos = Pos(x % other.x, y % other.y)
    fun wrapAround(max: Pos): Pos = this % (max + Pos(1,1))

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

    override fun near(dir: NearDir): List<Pos> = dir.ds.map { it + this }
    operator fun compareTo(other: Pos): Int {
        val d = this - other
        return if (d.x == 0) d.y else d.x
    }
}

fun Pos.toPair(): Pair<Int,Int> = this.x to this.y
fun Pos.inBounds(xRange: IntRange, yRange: IntRange) = this.x in xRange && this.y in yRange