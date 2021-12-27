import AoC_Lib.*
import day25.cucumbers.Day25withMap

fun printSea(sea: Map<Pos,Char>, maxPos: Pos): Unit {
    val sb = StringBuilder()
    sb.appendLine()
    (0 .. maxPos.y).forEach { y ->
        (0 .. maxPos.x).forEach { x ->
            sb.append(sea.getOrDefault(Pos(x,y),'.'))
        }
        sb.appendLine()
    }
    println(sb.toString())
}

fun Pos.goR(maxPos: Pos): Pos = (this + Pos(1,0)).wrapAround(maxPos)
fun Pos.goD(maxPos: Pos): Pos = (this + Pos(0,1)).wrapAround(maxPos)

val maxPos_ = Pos(9,3)
Pos(9,3).wrapAround(maxPos_)
Pos(10,3).wrapAround(maxPos_)
Pos(10,4).wrapAround(maxPos_)
Pos(0,0).wrapAround(maxPos_)

val s0 = "...>>>>>..."
val m_0 = s0.split('\n')
    .toMapGrid { it }
val maxPos0 = m_0.keys.reduce(Pos::max)
var m0 = m_0.filterNot { it.value == '.' }
printSea(m0, maxPos0)
m0.keys.filter { m0[it] == '>' }
m0.keys.filter { m0[it] == '>' }.map { (it + Pos(1,0)).wrapAround(maxPos0) }
var m0Rgo = m0.keys.filter { m0[it] == '>' }
    .filterNot { m0.containsKey((it + Pos(1,0)).wrapAround(maxPos0)) }
    .toSet()
var m0_ = m0 - m0Rgo + m0Rgo.map { it.goR(maxPos0) to '>' }
var m0Dgo = m0_.keys.filter { m0_[it] == '>' }
    .filterNot { m0_.containsKey(it.goD(maxPos0)) }
    .toSet()
m0 = m0_ - m0Dgo + m0Dgo.map { it.goD(maxPos0) to 'v' }
printSea(m0,maxPos0)
m0Rgo = m0.keys.filter { m0[it] == '>' }
    .filterNot { m0.containsKey((it + Pos(1,0)).wrapAround(maxPos0)) }
    .toSet()
m0_ = m0 - m0Rgo + m0Rgo.map { it.goR(maxPos0) to '>' }
m0Dgo = m0_.keys.filter { m0_[it] == '>' }
    .filterNot { m0_.containsKey(it.goD(maxPos0)) }
    .toSet()
m0 = m0_ - m0Dgo + m0Dgo.map { it.goD(maxPos0) to 'v' }
printSea(m0,maxPos0)

fun step(cucs: Map<Pos,Char>, maxPos: Pos): Pair<Map<Pos,Char>,Int> {
    val cucsGoRight = cucs.keys.filter { cucs[it] == '>' }
        .filterNot { cucs.containsKey(it.goR(maxPos)) }
        .toSet()
    val newCucs = cucs - cucsGoRight + cucsGoRight.map { it.goR(maxPos) to '>' }
    val cucsGoDown = newCucs.keys.filter { cucs[it] == 'v' }
        .filterNot { newCucs.containsKey(it.goD(maxPos)) }
        .toSet()
    val resCucs = newCucs - cucsGoDown + cucsGoDown.map { it.goD(maxPos) to 'v' }
    return resCucs to (cucsGoRight.size + cucsGoDown.size)
}
val r0_1 = step(m0,maxPos0)
printSea(r0_1.first,maxPos0)
println(r0_1.second)
val r0_2 = step(r0_1.first,maxPos0)
printSea(r0_2.first,maxPos0)
println(r0_2.second)

val s1 = "...>...\n" +
        ".......\n" +
        "......>\n" +
        "v.....>\n" +
        "......>\n" +
        ".......\n" +
        "..vvv.."
val m_1 = s1.split('\n')
    .toMapGrid { it }
val maxPos1 = m_1.keys.reduce(Pos::max)
var m1 = m_1.filterNot { it.value == '.' }
printSea(m1, maxPos1)
val r1_1 = step(m1,maxPos1)
printSea(r1_1.first,maxPos1)
println(r1_1.second)
val r1_2 = step(r1_1.first,maxPos1)
printSea(r1_2.first,maxPos1)
println(r1_2.second)

val s2 = "v...>>.vv>\n" +
        ".vv>>.vv..\n" +
        ">>.>v>...v\n" +
        ">>v>>.>.v.\n" +
        "v>v.vv.v..\n" +
        ">.>>..v...\n" +
        ".vv..>.>v.\n" +
        "v.v..>>v.v\n" +
        "....v..v.>"
val m_2 = s2.split('\n')
    .toMapGrid { it }
val maxPos2 = m_2.keys.reduce(Pos::max)
var m2 = m_2.filterNot { it.value == '.' }
printSea(m2, maxPos2)
generateSequence(m2 to 0) { (state, _) ->
    val res = step(state,maxPos2)
    res
}.drop(1).map { it.second }.takeWhile { it != 0 }.toList().size