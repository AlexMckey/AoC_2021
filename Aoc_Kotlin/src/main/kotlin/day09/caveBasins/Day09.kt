package day09.caveBasins

import AoC_Lib.*

object Day09: SomeDay(2021,9) {
    override val title = "Smoke Basin"

    override fun first(data: String): Any? {
        val cave = data.toStrs().toMapGrid { it.digitToInt() }
        val exactlyLowest = cave.filter { it.value == 0 }
        val lowestPoint = cave.filter { it.value in 1..8 }
            .filter { (p,v) -> p.near(NearDir.Axis)
                .filter { it.inBounds(cave) }
                .all { cave.getOrDefault(it,9) > v }
            } + exactlyLowest
        return lowestPoint.values.sumOf { it+1 }
    } // 494 Time: 136ms

    override fun second(data: String): Any? {
        val cave = data.toStrs().toMapGrid { it.digitToInt() }
        val lowestPoint = cave.filter { it.value in 1..8 }
            .filter { (p,v) -> p.near(NearDir.Axis)
                .filter { it.inBounds(cave) }
                .all { cave.getOrDefault(it,9) > v }
            } + cave.filter { it.value == 0 }
        tailrec fun findBasin(cur: Set<Pos>, acc: Set<Pos> = emptySet()): Set<Pos> =
            if (cur.isEmpty()) acc
            else findBasin(cur.flatMap { p -> p.near(NearDir.Axis)
                .filter { it.inBounds(cave) && it !in acc && cave.getOrDefault(it, 9) < 9 } }
                .toSet(), acc union cur)
        return lowestPoint.keys.map { p -> findBasin(setOf(p)).size }
            .sortedDescending().take(3).reduce(Int::times)
    } // 1048128 Time: 68ms
}

fun main() = SomeDay.mainify(Day09)