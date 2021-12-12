package day12.cavePaths

import AoC_Lib.*

object Day12: SomeDay(2021,12) {
    override val title = "Passage Pathing"

    private fun bfs(cave: Map<String, List<String>>, start: String, end: String, filterNeighbors: (String, List<String>) -> Boolean): List<List<String>> {
        tailrec fun rec(queue: List<List<String>>, visited: List<List<String>>): List<List<String>> {
            val neighbors: List<List<String>> = queue
                .filterNot { it.first() == end }
                .flatMap { path -> cave[path.first()]!!
                    .filter { neighbor -> filterNeighbors(neighbor, path)}
                .map { neighbor -> listOf(neighbor) + path }}
            val newVisited: List<List<String>> = visited + queue
            val newQueue: List<List<String>> = neighbors - visited.toSet()
            return if (newQueue.isEmpty()) newVisited
            else rec(newQueue, newVisited)
        }
        return rec(listOf(listOf(start)), emptyList())
    }

    override fun first(data: String): Any? {
        val cave = data.toStrs()
            .flatMap { it.split('-')
                .let {a -> listOf(a[0] to a[1],
                    a[1] to a[0]) }}
            .groupBy({ it.first },{ it.second })
        fun fp1(n: String, p: List<String>): Boolean =
            n[0].isUpperCase() || n !in p
        return bfs(cave, "start", "end", ::fp1)
            .count{ it.first() == "end" }
    } // 3369 Time: 60ms

    override fun second(data: String): Any? {
        val cave = data.toStrs()
            .flatMap { it.split('-')
                .let {a -> listOf(a[0] to a[1],
                    a[1] to a[0]) }}
            .groupBy({ it.first },{ it.second })
        fun fp2(n: String, p: List<String>): Boolean =
            when {
                n == "start" -> false
                n.first().isUpperCase() -> true
                n !in p -> true
                else -> p.filter { it.first().isLowerCase() }
                    .groupBy { it }
                    .none { it.value.size == 2 }

            }
        return bfs(cave, "start", "end", ::fp2)
            .count{ it.first() == "end" }
    } // 85883 Time: 642ms
}

fun main() = SomeDay.mainify(Day12)