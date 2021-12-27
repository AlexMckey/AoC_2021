data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"

    companion object {
        val Zero = Pos(0,0)
        fun fromString(s: String): Pos = s
            .split(',')
            .map(String::toInt)
            .let { Pos(it.first(), it.last()) }
    }
}
data class Line(val p1: Pos, val p2: Pos) {
    fun isVH(): Boolean = p1.x == p2.x || p1.y == p2.y
    override fun toString(): String = "$p1 -> $p2"
    fun rangeByCoord(coord: (Pos) -> Int): List<Int> =
        (if (coord(p1) > coord(p2))
            coord(p1) downTo coord(p2)
        else coord(p1) .. coord(p2)).toList()

    fun expanded(): List<Pos> {
        val rx = rangeByCoord{ it.x }
        val ry = rangeByCoord{ it.y }
        val px = if (rx.size > 1) rx else List(ry.size){rx.first()}
        val py = if (ry.size > 1) ry else List(rx.size){ry.first()}
        return px.zip(py,::Pos)
    }
}

val s = "0,9 -> 5,9\n" +
        "8,0 -> 0,8\n" +
        "9,4 -> 3,4\n" +
        "2,2 -> 2,1\n" +
        "7,0 -> 7,4\n" +
        "6,4 -> 2,0\n" +
        "0,9 -> 2,9\n" +
        "3,4 -> 1,4\n" +
        "0,0 -> 8,8\n" +
        "5,5 -> 8,2"

val ps = s.lines()
    .map { it.split(" -> ")
        .map(Pos::fromString) }
ps
val ls = ps.map { Line(it.first(), it.last()) }
ls
ps.map{it.toLines()}.flatten()

fun Array<Pos>.toLines(): List<Line> = this.toList().toLines()
fun List<Pos>.toLines(): List<Line> = this.zipWithNext(::Line)

val ls_ = ps.map{ it.toLines() }
ls_

val vhls = ls.filter { it.isVH() }
vhls
val l0 = ls[1]
l0
val rx = (l0.p1.x .. l0.p2.x).toList()
val ry = (l0.p1.y .. l0.p2.y).toList()
rx
ry
l0.rangeByCoord { it.x }
l0.rangeByCoord { it.y }
l0.expanded().toList()
vhls.flatMap(Line::expanded)
    .groupingBy { it }.eachCount().count { it.value > 1 }