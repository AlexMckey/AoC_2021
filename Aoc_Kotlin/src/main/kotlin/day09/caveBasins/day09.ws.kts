import AoC_Lib.*

fun <T> List<String>.toMapGrid(f: (Char) -> T) = this
    .flatMapIndexed { col, line ->
        line.mapIndexed { row, char ->
            Pos(row, col) to f(char)
        }
    }
    .associate { it }
fun <T : Any> Pos.inBounds(grid: Map<Pos,T>): Boolean = grid.containsKey(this)

val s = "2199943210\n" +
        "3987894921\n" +
        "9856789892\n" +
        "8767896789\n" +
        "9899965678"
val mg = s.split('\n').toMapGrid{ it.digitToInt() }
val exactlyLowest = mg.filter { it.value == 0 }
val lowestPoint = mg.filter { it.value in 1..8 }
    .filter { (p,v) -> p.near(NearDir.Axis)
        .filter { it.inBounds(mg) }
        .all { mg.getOrDefault(it,9) > v } } + exactlyLowest
lowestPoint.values.sumOf { it + 1 }
lowestPoint
val lp0 = setOf(lowestPoint.keys.first())
lp0
lp0.flatMap { p -> p
    .near(NearDir.Axis)
    .filter { it.inBounds(mg) && mg.getOrDefault(it, 9) < 9 } }
val res = lowestPoint.keys.map { p ->
    tailrec fun findBasin(cur: Set<Pos>, acc: Set<Pos> = emptySet()): Set<Pos> =
        if (cur.isEmpty()) acc
        else findBasin(cur.flatMap { p -> p.near(NearDir.Axis)
            .filter { it.inBounds(mg) && !(it in acc) && mg.getOrDefault(it, 9) < 9 } }
            .toSet(), acc union cur)
    findBasin(setOf(p))
}
res.map { it.size }.sortedDescending().take(3).reduce(Int::times)