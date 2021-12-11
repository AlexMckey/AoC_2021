import java.util.*
import kotlin.collections.ArrayList

val s = "[({(<(())[]>[[{[]{<()<>>\n" +
        "[(()[<>])]({[<{<<[]>>(\n" +
        "{([(<{}[<>[]}>{[]{[(<()>\n" +
        "(((({<>}<{<{<>}{[]{[]{}\n" +
        "[[<[([]))<([[{}[[()]]]\n" +
        "[{[{({}]{}}([{[{{{}}([]\n" +
        "{<[[]]>}<{[{[{[]{()[[[]\n" +
        "[<(<(<(<{}))><([]([]()\n" +
        "<{([([[(<>()){}]>(<<{{\n" +
        "<{([{{}}[<[[[<>{}]]]>[]]"
val ss = s.split('\n')
enum class Chunks{ Correct, Incomplete, Corrupted }
val Brackets = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
val InverseBrackets = Brackets.map { it.value to it.key }.toMap()
val CorruptedScore = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
val IncomletedScore = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
fun checkBrackets(s: String): Pair<Chunks,List<Char>> {
    tailrec fun rec(s: String, stack: Deque<Char> = ArrayDeque()): Pair<Chunks,List<Char>> {
        return when {
            s.isEmpty() && stack.isEmpty() ->
                Chunks.Correct to emptyList()
            s.isEmpty() && stack.isNotEmpty() ->
                Chunks.Incomplete to stack.toList()
            s.first() in Brackets.keys -> {
                val c = s.first()
                stack.push(c)
                rec(s.drop(1), stack) }
            s.first() == Brackets[stack.peekFirst()] -> {
                stack.pop()
                rec(s.drop(1), stack)}
            else -> Chunks.Corrupted to listOf(s.first())
        }
    }
    return rec(s)
}
checkBrackets(ss.first())
checkBrackets("()")
ss.map(::checkBrackets)
    .filter { it.first == Chunks.Corrupted }
    .map { it.second.first() }
    .sumOf { CorruptedScore.getOrDefault(it, 0) }
ss.map(::checkBrackets)
    .filter { it.first == Chunks.Incomplete }
    .map { it.second
        .fold(0L) { acc, c -> acc * 5 + IncomletedScore.getOrDefault(c,0) } }
    .sorted()