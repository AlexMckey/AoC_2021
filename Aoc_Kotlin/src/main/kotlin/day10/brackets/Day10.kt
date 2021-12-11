package day10.brackets

import AoC_Lib.*
import java.util.*

object Day10: SomeDay(2021,10) {
    override val title = "Syntax Scoring"

    abstract class Chunks
    object Correct: Chunks()
    data class Incomplete(val chars: List<Char>): Chunks()
    data class Corrupted(val char: Char): Chunks()

    val Brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val CorruptedScore = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val IncompletedScore = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)

    fun checkBrackets(s: String): Chunks {
        tailrec fun rec(s: String, stack: Deque<Char> = ArrayDeque()): Chunks {
            return when {
                s.isEmpty() && stack.isEmpty() ->
                    Correct
                s.isEmpty() && stack.isNotEmpty() ->
                    Incomplete(stack.toList())
                s.first() in Brackets.keys -> {
                    val c = s.first()
                    stack.push(c)
                    rec(s.drop(1), stack) }
                s.first() == Brackets[stack.peekFirst()] -> {
                    stack.pop()
                    rec(s.drop(1), stack)}
                else -> Corrupted(s.first())
            }
        }
        return rec(s)
    }

    override fun first(data: String): Any? =
        data.toStrs()
            .map(::checkBrackets)
            .filterIsInstance<Corrupted>()
            .sumOf { CorruptedScore.getOrDefault(it.char, 0) }
    // 469755 Time: 63ms

    override fun second(data: String): Any? =
        data.toStrs().map(::checkBrackets)
            .filterIsInstance<Incomplete>()
            .map { it.chars.fold(0L) { acc, c ->
                acc * 5 + IncompletedScore.getOrDefault(c,0) }
            }.sorted()
            .let { it[it.size / 2] }
    // 2762335572 Time: 10ms
}

fun main() = SomeDay.mainify(Day10)