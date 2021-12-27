import AoC_Lib.MapGrid
import AoC_Lib.NearDir
import AoC_Lib.Pos
import AoC_Lib.toMapGrid

fun step(fromBoard: MapGrid<Int>): Pair<MapGrid<Int>,Int> {
    tailrec fun rec(board: MapGrid<Int>, toFlash: List<Pos>, visited: Set<Pos>): Pair<MapGrid<Int>,Set<Pos>> {
        return when {
            toFlash.isEmpty() -> board to visited
            visited.contains(toFlash.first()) -> rec(board, toFlash.drop(1), visited)
            else -> {
                val p = toFlash.first()
                val b = board + (p to 0)
                val ns = p.near(NearDir.All).filter { board.containsKey(it) }.filterNot { visited.contains(it) }
                val nb = b + ns.groupingBy { it }.eachCount().mapValues { b[it.key]!! + it.value }
                val ntf = ns.filter { nb[it]!! > 9 }.filterNot { visited.contains(it) } + toFlash
                val nv = visited + p
                rec(nb, ntf, nv)
            }
        }
    }
    val b = fromBoard.mapValues { it.value + 1 }
    return rec(b, b.filter { it.value > 9 }.keys.toList(), emptySet())
        .let { it.first to it.second.size }
}
val s = "5483143223\n2745854711\n5264556173\n6141336146\n6357385478\n4167524645\n2176841721\n6882881134\n4846848554\n5283751526"
val s_ = "11111\n19991\n19191\n19991\n11111"
val board: MapGrid<Int> = s.split('\n')
    .toMapGrid { it.digitToInt() }
fun printBoard(board: MapGrid<Int>): Unit {
    (board.minOf { it.key.x } .. board.maxOf { it.key.x })
        .forEach{ y ->
            (board.minOf{ it.key.y } .. board.maxOf { it.key.y })
                .forEach{ x ->
                    print(board[Pos(x,y)]!!)
                }
            println()
        }
}
printBoard(board)
val (b0,i0) = step(board)
printBoard(b0)
val (b1,i1) = step(b0)
printBoard(b1)