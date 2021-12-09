package day09

import AoC_Lib.*
import scala.annotation.tailrec

object day09 {
  val board: Map[Pos, Int] = inputStrs(day = 9)
    .zipWithIndex
    .flatMap((s,y) => s
      .zipWithIndex
      .map((c,x) => Pos(x,y) -> c.asDigit))
    .toMap
    .withDefaultValue(9)

  val lowPoints: Map[Pos,Int] = board
    .filter((p, i) => p.near4
      .map(board)
      .forall(_ > i))
    .toMap

  def part1: Int = lowPoints.values.map(_ + 1).sum

  def part2: Int = {
    def findBasin(p: Pos): Int = {
      @tailrec def rec(cur: Set[Pos], acc: Set[Pos] = Set.empty): Set[Pos] =
        if cur.isEmpty then acc
        else rec(cur.flatMap(_.near4.filter(board(_) < 9)) diff acc, acc union cur)
      rec(Set(p)).size
    }
    lowPoints.keys
      .map(findBasin)
      .toSeq
      .sorted(using Ordering.Int.reverse)
      .take(3)
      .product
  }

  @main
  def SmokeBasin(): Unit = {
    Console.println(part1) // 239
    Console.println(part2) // 946346
  }
}
