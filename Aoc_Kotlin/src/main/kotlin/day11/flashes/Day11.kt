package day11.flashes

import AoC_Lib.*

object Day11: SomeDay(2021,11) {
    override val title = "Dumbo Octopus"

    private fun step(fromBoard: MapGrid<Int>): Pair<MapGrid<Int>,Int> {
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

    override fun first(data: String): Any? {
        val board = data.toStrs().toMapGrid { it.digitToInt() }
        var cnt = 0
        var b = board
        repeat(100) {
            val (nb, c) = step(b)
            b = nb
            cnt += c
        }
        return cnt
    } // 1673 Time: 102ms

    override fun second(data: String): Any? {
        val board = data.toStrs().toMapGrid { it.digitToInt() }
        var cnt = 0
        val size = board.size
        var b = board
        do {
            val (nb, c) = step(b)
            b = nb
            cnt += 1
        } while (c != size)
        return cnt
    } // 279 Time: 46ms
}

fun main() = SomeDay.mainify(Day11)