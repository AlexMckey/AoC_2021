package day25.cucumbers

import AoC_Lib.SomeDay
import AoC_Lib.Pos
import AoC_Lib.toMapGrid

object Day25: SomeDay(2021, 25) {
    override val title = "Sea Cucumber"

    data class CucumberSea(val goRight: Set<Pos>, val goDown: Set<Pos>){
        lateinit var maxPos: Pos
            private set

        private val Pos.goRight: Pos
            get() = if (this.x == maxPos.x) this.copy(x = 0) else this + Pos(1,0)
        private val Pos.goDown: Pos
            get() = if (this.y == maxPos.y) this.copy(y = 0) else this + Pos(0,1)

        private fun step(): CucumberSea {
            fun halfStep(workSet: Set<Pos>, otherSet: Set<Pos>, f: (Pos) -> Pos): Set<Pos> {
                val go = workSet.filterNot { workSet.contains(f(it)) || otherSet.contains(f(it)) }.toSet()
                return workSet - go + go.map(f)
            }
            val goR = halfStep(goRight,goDown) { it.goRight }
            val goD = halfStep(goDown,goR) { it.goDown }
            val newCS = CucumberSea(goR,goD)
            newCS.maxPos = this.maxPos
            return newCS
        }

        fun findStableState(): Int =
            generateSequence(this to 0){ (state,cnt) ->
                state.step() to (cnt + 1)}
            .zipWithNext { s1, s2 -> (s1.first == s2.first) to s2.second }
            .dropWhile { !it.first }
            .first()
            .second

        override fun toString(): String {
            val sb = StringBuilder()
            sb.appendLine()
            (0 .. maxPos.y).forEach { y ->
                (0 .. maxPos.x).forEach { x ->
                    when {
                        goRight.contains(Pos(x,y)) -> sb.append('>')
                        goDown.contains(Pos(x,y)) -> sb.append('v')
                        else -> sb.append('.')
                    }
                }
                sb.appendLine()
            }
            return sb.toString()
        }
        companion object {
            fun of(s: String): CucumberSea {
                val grid = s.split('\n')
                    .toMapGrid { it }
                val notEmpty = grid.filterNot { it.value == '.' }
                val goR = notEmpty.filter { it.value == '>' }.keys
                val goD = notEmpty.keys - goR
                val cs = CucumberSea(goR,goD)
                cs.maxPos = grid.keys.reduce(Pos::max)
                return cs
            }
        }
    }

    override fun first(data: String): Any? =
        CucumberSea.of(data).findStableState()
    // 308 Time: 500ms

    override fun second(data: String): Any? = null
    //
}

fun main() = SomeDay.mainify(Day25)