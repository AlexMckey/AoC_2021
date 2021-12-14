import java.lang.StringBuilder

data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    operator fun minus(other: Pos) = Pos(other.x - x, other.y - y)
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
data class Folding(val axis: Char, val i: Int)
val s = "6,10\n" +
        "0,14\n" +
        "9,10\n" +
        "0,3\n" +
        "10,4\n" +
        "4,11\n" +
        "6,0\n" +
        "6,12\n" +
        "4,1\n" +
        "0,13\n" +
        "10,12\n" +
        "3,4\n" +
        "3,0\n" +
        "8,4\n" +
        "1,10\n" +
        "2,14\n" +
        "8,10\n" +
        "9,0\n" +
        "\n" +
        "fold along y=7\n" +
        "fold along x=5"
val (d,f) = s.split("\n\n")
val sheet = d.lines()
    .map(Pos::fromString)
    .toSet()
sheet
val fs = f.lines().map { it.split('=')
    .let { Folding(it.first().last(), it.last().toInt()) } }
fs
fun foldAlign(sheet: Set<Pos>, fold: Folding): Set<Pos> =
    sheet.map { p -> when {
        fold.axis == 'x' && p.x > fold.i -> Pos(fold.i + fold.i - p.x, p.y)
        fold.axis == 'y' && p.y > fold.i -> Pos(p.x, fold.i + fold.i - p.y)
        else -> p } }.toSet()
fun sheetToString(sheet: Set<Pos>): String {
    val maxX = sheet.maxOf { it.x }
    val maxY = sheet.maxOf { it.y }
    val sb = StringBuilder()
    (0 .. maxY).forEach { y ->
        (0 .. maxX).forEach { x ->
            sb.append(if (Pos(x,y) in sheet) '#' else '.')}
        sb.append('\n') }
    return sb.toString()
}
sheetToString(sheet)
val r1 = foldAlign(sheet,fs.first())
sheetToString(r1)
val res = fs.fold(sheet) { acc, fold ->
    foldAlign(acc, fold)
}
sheetToString(res)