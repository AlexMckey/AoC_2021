package day25.cucumbers

import AoC_Lib.SomeDay
import AoC_Lib.Pos
import AoC_Lib.toMapGrid

object Day25withMap: SomeDay(2021, 25) {
    override val title = "Sea Cucumber"

    lateinit var maxPos: Pos
        private set

    private val Pos.goR: Pos get() = (this + Pos(1,0)).wrapAround(maxPos)
    private val Pos.goD: Pos get() = (this + Pos(0,1)).wrapAround(maxPos)

    private lateinit var cucumbers: Map<Pos,Char>

    private fun step(cucs: Map<Pos,Char>): Pair<Map<Pos,Char>,Int> {
        val cucsGoRight = cucs.keys.filter { cucs[it] == '>' }
            .filterNot { cucs.containsKey(it.goR) }
            .toSet()
        val newCucs = cucs - cucsGoRight + cucsGoRight.map { it.goR to '>' }
        val cucsGoDown = newCucs.keys.filter { cucs[it] == 'v' }
            .filterNot { newCucs.containsKey(it.goD) }
            .toSet()
        val resCucs = newCucs - cucsGoDown + cucsGoDown.map { it.goD to 'v' }
        return resCucs to (cucsGoRight.size + cucsGoDown.size)
    }

    private fun findStableState(init: Map<Pos,Char>): Int =
        generateSequence(init to 0) { (state, _) ->
            step(state)
        }.drop(1).map { it.second }.takeWhile { it != 0 }.toList().size + 1

    private fun printSea(): Unit {
        val sb = StringBuilder()
        sb.appendLine()
        (0 .. maxPos.y).forEach { y ->
            (0 .. maxPos.x).forEach { x ->
                sb.append(cucumbers.getOrDefault(Pos(x,y),'.'))
            }
            sb.appendLine()
        }
        println(sb.toString())
    }
    private fun parse(s: String): Map<Pos,Char> {
        val grid = s.split('\n')
            .toMapGrid { it }
        val notEmpty = grid.filterNot { it.value == '.' }
        maxPos = grid.keys.reduce(Pos::max)
        return notEmpty
    }

    override fun first(data: String): Any? =
        findStableState(parse(data))
    // 308 Time: 1010ms - Slowly

    override fun second(data: String): Any? = null
    //
}

fun main() = SomeDay.mainify(Day25withMap)