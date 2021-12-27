data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    operator fun minus(other: Pos) = Pos(other.x - x, other.y - y)
    operator fun plusAssign(other: Pos) = run { x += other.x; y += other.y }
    operator fun minusAssign(other: Pos) = run { x -= other.x; y -= other.y }
    operator fun unaryMinus() = Pos(-x, -y)
    operator fun times(other: Int) = Pos(x * other, y * other)
    operator fun timesAssign(other: Int) = run { x *= other; y *= other }

    companion object {
        val Zero = Pos(0,0)
    }
}

val s = "forward 5\n" +
        "down 5\n" +
        "forward 8\n" +
        "up 3\n" +
        "down 8\n" +
        "forward 2"
val cmds = s.lines()
    .map { it.split(' ') }
    .map { it[0] to it[1].toInt() }
    .map { when (it.first) {
        "forward" -> Pos(it.second,0)
        "up" -> Pos(0,-it.second)
        "down" -> Pos(0,it.second)
        else -> Pos.Zero
    } }
cmds.fold(Pos.Zero, Pos::plus).let { it.x * it.y }

val ss = "forward 5"
ss.split(' ')