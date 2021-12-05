package AoC_Lib

data class Line(val p1: Pos, val p2: Pos) {
    override fun toString(): String = "$p1 -> $p2"

    fun isVH(): Boolean = p1.x == p2.x || p1.y == p2.y

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

fun Array<Pos>.toLines(): List<Line> = this.toList().toLines()
fun List<Pos>.toLines(): List<Line> = this.zipWithNext(::Line)