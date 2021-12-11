package day10

import AoC_Lib.*
import scala.annotation.tailrec

object day10 {
  val BracketsPair = Map('(' -> ')', '[' -> ']', '{' -> '}', '<' -> '>')
  val CorruptedScore = Map(')' -> 3, ']' -> 57, '}' -> 1197, '>' -> 25137)
  val IncompleteScore = Map(')' -> 1, ']' -> 2, '}' -> 3, '>' -> 4)

  enum Chunks:
    case Correct
    case Incomplete(chars: List[Char])
    case Corrupted(c: Char)

  def checkBracket(s: String): Chunks = {
    @tailrec def rec(s: String, acc: List[Char] = List.empty): Chunks = {
      if s.isEmpty && acc.isEmpty
        then Chunks.Correct
      else if s.isEmpty && acc.nonEmpty
        then Chunks.Incomplete(acc)
      else if BracketsPair.keySet.contains(s.head)
        then rec(s.tail, s.head +: acc)
      else if BracketsPair(acc.head) == s.head
        then rec(s.tail, acc.tail)
      else Chunks.Corrupted(s.head)
    }
    rec(s)
  }

  val checkedChunks: Seq[Chunks] = inputStrs(day = 10).map(checkBracket)

  def part1: Int = checkedChunks
    .collect { case Chunks.Corrupted(c) => CorruptedScore(c) }
    .sum

  def part2: Long = {
    val incomplete: Seq[Long] = checkedChunks
      .collect { case Chunks.Incomplete(l) => l }
      .map(_
        .map(BracketsPair)
        .map(IncompleteScore)
        .foldLeft(0L){ (acc,i) => acc * 5 + i })
      .sorted
    incomplete(incomplete.size / 2)
  }

  @main
  def SyntaxScoring(): Unit = {
    Console.println(part1) // 469755
    Console.println(part2) // 2762335572
  }
}