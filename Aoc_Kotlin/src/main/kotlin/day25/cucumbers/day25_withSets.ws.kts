data class Pos(var x: Int = 0, var y: Int = 0) {
    override fun toString(): String = "[x:$x,y:$y]"
    operator fun plus(other: Pos) = Pos(other.x + x, other.y + y)
    infix fun max(other: Pos): Pos =
        Pos(x.coerceAtLeast(other.x), y.coerceAtLeast(other.y))
    companion object {
        val Zero = Pos(0, 0)
    }
}

data class CucumberSea(val goRight: Set<Pos>, val goDown: Set<Pos>){
    lateinit var maxPos: Pos
        private set

    private val Pos.goRight: Pos
        get() = if (this.x == maxPos.x) this.copy(x = 0) else this + Pos(1,0)
    private val Pos.goDown: Pos
        get() = if (this.y == maxPos.y) this.copy(y = 0) else this + Pos(0,1)

    fun step(): CucumberSea {
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
                .flatMapIndexed { col, line ->
                    line.mapIndexed { row, char ->
                        Pos(row, col) to char
                    }
                }
                .associate { it }
            val notEmpty = grid.filterNot { it.value == '.' }
            val goR = notEmpty.filter { it.value == '>' }.keys
            val goD = notEmpty.keys - goR
            val cs = CucumberSea(goR,goD)
            cs.maxPos = grid.keys.reduce(Pos::max)
            return cs
        }
    }
}
val s0 = "...>>>>>..."
s0.split('\n')
    .flatMapIndexed { col, line ->
    line.mapIndexed { row, char ->
        Pos(row, col) to char
    }}
    .associate { it }
val cs0 = CucumberSea.of(s0)
cs0.toString()
cs0.maxPos
cs0.step()
val cs1 = CucumberSea.of("..........\n" +
        ".>v....v..\n" +
        ".......>..\n" +
        "..........")
cs1.step()
val cs2 = CucumberSea.of("...>...\n" +
        ".......\n" +
        "......>\n" +
        "v.....>\n" +
        "......>\n" +
        ".......\n" +
        "..vvv..")
cs2.step()
val cs3 = CucumberSea.of("v...>>.vv>\n" +
        ".vv>>.vv..\n" +
        ">>.>v>...v\n" +
        ">>v>>.>.v.\n" +
        "v>v.vv.v..\n" +
        ">.>>..v...\n" +
        ".vv..>.>v.\n" +
        "v.v..>>v.v\n" +
        "....v..v.>")
val seq = generateSequence(cs3 to 0){ (cucumberSea, cnt) ->
    cucumberSea.step() to cnt + 1
}.zipWithNext().dropWhile{ it.first.first != it.second.first }.first().second.second
seq