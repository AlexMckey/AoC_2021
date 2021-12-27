package day15.findPath

import AoC_Lib.*
import java.util.*

object Day15: SomeDay(2021,15) {
    override val title = "Chiton"

    private fun dijkstraSearch(grid: ArrayGrid<Int>, start: Pos, target: Pos): Int? {
        val visited = mutableMapOf<Pos,Int>()
        val toVisit = PriorityQueue<Pair<Pos,Int>> { o1, o2 -> o1.second - o2.second }

        toVisit.add(start to 0)
        while (toVisit.isNotEmpty()) {
            val (node, dist) = toVisit.poll()
            if (!visited.contains(node)) {
                visited[node] = dist
                if (node == target) return dist
                node.near(NearDir.Axis)
                    .filterNot{ visited.contains(it) }
                    .filter{ grid.contains(it) }
                    .forEach{ p -> toVisit.add(p to (dist + grid[p.x][p.y]))}
            }
        }
        return null
    }

    private fun dpSearch(grid: ArrayGrid<Int>, start: Pos, end: Pos): Int {
        val pq = PriorityQueue<Pair<Pos,Int>> { a, b -> a.second - b.second }
        val risk = Array(grid.size) { Array(grid.size) { Int.MAX_VALUE } }
        risk[0][0] = 0
        pq.add(start to 0)
        while (pq.isNotEmpty()) {
            val (current, dist) = pq.poll()
            current.near(NearDir.Axis)
                .forEach { p ->
                    if (grid.contains(p) && risk[p.x][p.y] > dist + grid[p.x][p.y]) {
                        risk[p.x][p.y] = dist + grid[p.x][p.y]
                        pq.add(p to risk[p.x][p.y])
                    }
                }
        }
        return risk[end.x][end.y]
    }

    override fun first(data: String): Any? {
        val grid = data.lines()
            .map { it.toCharArray()
                .map { it.digitToInt() }
                .toTypedArray() }
            .toTypedArray()
        return dijkstraSearch(grid,Pos.Zero,Pos(grid.lastIndex,grid.last().lastIndex))
    } // 656 Time: 101ms

    override fun second(data: String): Any? {
        val scale = 5
        val grid = data.lines()
            .map { it.toCharArray()
                .map { it.digitToInt() }
                .toTypedArray() }
            .toTypedArray()
        val fullGrid = (0 until grid.size * scale).map { i ->
            (0 until grid.size * scale).map { j ->
                (grid[i % grid.size][j % grid.size] + (i / grid.size + j / grid.size))
                    .let { if (it < 10) it else it - 9 }
            }.toTypedArray()
        }.toTypedArray()
        //return dijkstraSearch(fullGrid,Pos.Zero,Pos(fullGrid.lastIndex,fullGrid.last().lastIndex))
        return dpSearch(fullGrid,Pos.Zero,Pos(fullGrid.lastIndex,fullGrid.last().lastIndex))
    } // 2979 Time: 1026ms
      // 2979 Time: 178ms
}

fun main() = SomeDay.mainify(Day15)