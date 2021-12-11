import scala.annotation.tailrec

val s = "[({(<(())[]>[[{[]{<()<>>\n[(()[<>])]({[<{<<[]>>(\n{([(<{}[<>[]}>{[]{[(<()>\n(((({<>}<{<{<>}{[]{[]{}\n[[<[([]))<([[{}[[()]]]\n[{[{({}]{}}([{[{{{}}([]\n{<[[]]>}<{[{[{[]{()[[[]\n[<(<(<(<{}))><([]([]()\n<{([([[(<>()){}]>(<<{{\n<{([{{}}[<[[[<>{}]]]>[]]"
val ss = s.split('\n')
enum Chunks { case Correct; case Incomplete(chars: List[Char]); case Corrupted(c: Char)}
val BracketsPair = Map(')' -> '(', ']' -> '[', '}' -> '{', '>' -> '<')
val CorruptedScore = Map(')' -> 3, ']' -> 57, '}' -> 1197, '>' -> 25137)
val IncompleteScore = Map(')' -> 1, ']' -> 2, '}' -> 3, '>' -> 4)
def checkBracket(s: String): Chunks = {
  @tailrec def rec(s: String, acc: List[Char] = List.empty): Chunks = {
    if s.isEmpty && acc.isEmpty then Chunks.Correct
    else if s.isEmpty && acc.nonEmpty
      then Chunks.Incomplete(acc)
    else if !BracketsPair.keySet.contains(s.head)
      then rec(s.tail, s.head +: acc)
    else if BracketsPair(s.head) == acc.head
      then rec(s.tail, acc.tail)
    else Chunks.Corrupted(s.head)
  }
  rec(s)
}
ss.map(checkBracket)
  .collect { case Chunks.Corrupted(c) => CorruptedScore(c) }
.sum

val incomplete = ss.map(checkBracket)
  .collect { case Chunks.Incomplete(l) => l }

val score = incomplete.map(_
  .map(BracketsPair.map(_.swap))
  .map(IncompleteScore))

score.last.foldLeft(0){ (acc,i) => acc * 5 + i }
val sc = score.map(_.foldLeft(0){ (acc,i) => acc * 5 + i })
  .sorted

sc(sc.size / 2)

