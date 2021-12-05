package day05

import AoC_Lib.*
import Pos.*

object day05 {
  val ls: Seq[Line] = inputStrs("input05.txt")
    .map(_.split(" -> ")
      .map(_.toPos))
    .map{ case Array(p1, p2) => Line(p1,p2) }

  def overlappingPoints(ls: Seq[Line], filter: Line => Boolean): Int =
    ls.filter(filter)
      .flatMap(_.expand)
      .groupBy(identity)
      .count(_._2.length > 1)

  def part1: Int = overlappingPoints(ls, _.isVH)
  def part2: Int = overlappingPoints(ls, _ => true)

  @main
  def HydrothermalVenture(): Unit = {
    Console.println(part1) // 5835
    Console.println(part2) // 17013
  }
}
