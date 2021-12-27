package day15.findPath

import AoC_Lib.*
import java.util.*

object Day15_AStar: SomeDay(2021,15) {
    override val title = "Chiton"

    lateinit var grid: ArrayGrid<Int>

    fun aStar(grid: ArrayGrid<Int>, scale: Int): List<Pos>? {
        val start = Pos.Zero
        val target = Pos(grid.first().size * scale - 1, grid.size * scale - 1)
        val h = { n: Pos -> n.manhattanDistance(target) }

        val fScore = mutableMapOf<Pos,Int>()
        fScore[start] = h(start)

        val openSet = PriorityQueue<Pos>(target.x * target.y, compareBy { point -> fScore[point] })
        openSet.add(start)

        val cameFrom = mutableMapOf<Pos,Pos>()
        val gScore = mutableMapOf<Pos,Int>().withDefault { Int.MAX_VALUE }
        gScore[start] = 0

        while (openSet.isNotEmpty()) {
            val current = openSet.remove()
            if (current == target) return reconstructPath(cameFrom, current)

            current.near(NearDir.Axis)
                .filter { it.inBounds(start.x .. target.x, start.y .. target.y)}
                .forEach { neighbor ->
                    val tentative = (gScore[current] ?: Int.MAX_VALUE) + getRisk(neighbor)
                    if (tentative < (gScore[neighbor] ?: Int.MAX_VALUE)) {
                        cameFrom[neighbor] = current
                        gScore[neighbor] = tentative
                        fScore[neighbor] = tentative + h(neighbor)
                        if (neighbor !in openSet) {
                            openSet.add(neighbor)
                        }
                    }
                }
        }
        return null
    }

    private val riskCache: MutableMap<Pos, Int> = mutableMapOf()

    private fun getRisk(point: Pos): Int {
        val height = grid.size
        val width = grid.first().size
        riskCache[point]?.let { return it }

        if (grid.contains(point)) {
            return grid[point.y][point.x]
                .also { riskCache[point] = it }
        }

        val baseRisk = grid[point.y % height][point.x % width]
        val riskIncrease = point.x / width + point.y / height
        val risk = baseRisk + riskIncrease
        return (if (risk > 9) risk % 10 + 1 else risk)
            .also { riskCache[point] = it }
    }

    private fun reconstructPath(cameFrom: Map<Pos, Pos>, goal: Pos): List<Pos> {
        val totalPath = mutableListOf(goal)
        var current = goal
        while (current in cameFrom.keys) {
            current = cameFrom[current]!!
            totalPath.add(0, current)
        }
        return totalPath
    }

    private fun getLowestRisk(grid: ArrayGrid<Int>, scale: Int): Int =
        aStar(grid, scale)!!
            .drop(1)
            .sumOf { getRisk(it) }

    override fun first(data: String): Any? {
        grid = data.lines()
            .map { it.toCharArray()
                .map { it.digitToInt() }
                .toTypedArray() }
            .toTypedArray()
        return getLowestRisk(grid,1)
    } // 656 Time: 101ms

    override fun second(data: String): Any? {
        grid = data.lines()
            .map { it.toCharArray()
                .map { it.digitToInt() }
                .toTypedArray() }
            .toTypedArray()

        return getLowestRisk(grid,5)
    } // 2979 Time: 3658ms
}

fun main() = SomeDay.mainify(Day15_AStar)