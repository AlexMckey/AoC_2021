import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.sign

data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    operator fun minus(other: Pos) = Pos(x - other.x, y - other.y)
    operator fun unaryMinus() = Pos(-x, -y)
    operator fun times(other: Int) = Pos(x * other, y * other)
    companion object {
        fun fromString(s: String): Pos = s
            .split(',')
            .map(String::toInt)
            .let { Pos(it.first(), it.last()) }
        val Zero = Pos(0,0)
    }
}
val s = "target area: x=20..30, y=-10..-5"
val r = """target area: x=(\d+)..(\d+), y=(-?\d+)..(-?\d+)""".toRegex()
val (xmin,xmax,ymin,ymax) = r.matchEntire(s)!!.destructured
val pMax = Pos.fromString("$xmax,$ymax")
val pMin = Pos.fromString("$xmin,$ymin")
var xR = xmin.toInt() .. xmax.toInt()
var yR = ymin.toInt() .. ymax.toInt()
fun step(pos: Pos, vel: Pos): Pair<Pos,Pos> = (pos + vel) to (vel - Pos(vel.x.sign,1))
step(Pos.Zero,Pos(6,9))
tailrec fun shoot(pos: Pos, vel: Pos, maxY: Int = 0): Int? {
    return if (pos.x in xR && pos.y in yR) maxY
    else if (vel.y < 0 && pos.y < yR.first) null
    else {
        val (np, nv) = step(pos, vel)
        shoot(np,nv, max(maxY,np.y))
    }
}
var res = (1 .. xR.last).flatMap { xv ->
    (yR.first..yR.first.absoluteValue).mapNotNull { yv ->
        shoot(Pos.Zero, Pos(xv, yv))
    }
}
res.maxOrNull()
res.size
xR = 34 .. 67
yR = -215 .. -186
res = (1 .. xR.last).flatMap { xv ->
    (yR.first..yR.first.absoluteValue).mapNotNull { yv ->
        shoot(Pos.Zero, Pos(xv, yv))
    }
}
res.maxOrNull()
res.size