package day12
import AoC_Lib.*
import scala.annotation.tailrec

type Cave = String

object day12_Traverse {
  val m: Seq[(Cave, Cave)] = inputStrs(day = 12)
    .map(_.split('-'))
    .collect{ case Array(a1,a2) => a1 -> a2 }

  val cave: Map[Cave, Seq[Cave]] = (m ++ m.map(_.swap))
    .groupMap(_._1)(_._2)

  def traverse(ruleToVisit: (Cave, Path) => Boolean, path: Path = List("start")): Seq[Path] = {
    if path.head == "end" then Seq(path)
    else cave(path.head)
      .filter(ruleToVisit(_,path))
      .flatMap(next => traverse(ruleToVisit, next :: path))
  }

  def part1: Int =
    traverse((n,p) => n.head.isUpper || !p.contains(n)).length

  def part2: Int = {
    def part2Rule(n: Cave, p: Path): Boolean =
      if n == "start" then false
      else if n.head.isUpper then true
      else if !p.contains(n) then true
      else p.filter(_.head.isLower)
        .groupBy(identity)
        .forall(_._2.sizeIs < 2)
    traverse(part2Rule).length
  }

  @main
  def PassagePathingTraverse(): Unit = {
    Console.println(part1) // 3369
    Console.println(part2) // 85883
  }
}
