package day12

import AoC_Lib.*
import scala.annotation.tailrec

type Path = List[String]

object day12 {
  val m: Seq[(String, String)] = inputStrs(day = 12)
    .map(_.split('-'))
    .collect{ case Array(a1,a2) => a1 -> a2 }

  val cave: Map[String, Seq[String]] = (m ++ m.map(_.swap))
    .groupMap(_._1)(_._2)

  def bfs(start: String, filterNeighbors: (String, List[String]) => Boolean): Seq[Path] = {
    @tailrec def rec(queue: List[Path], visited: List[Path]): List[Path] = {
      val neighbors = queue
        .filterNot(_.head == "end")
        .flatMap(path => cave(path.head)
          .filter(neighbor => filterNeighbors(neighbor, path))
          .map(neighbor => neighbor :: path))
      val newVisited = visited ++ queue
      val newQueue = neighbors diff visited
      if (newQueue.isEmpty) newVisited
      else rec(newQueue, newVisited)
    }
    rec(List(List(start)), List.empty)
  }

  def part1: Int = {
    def part1Filter(n: String, p: List[String]): Boolean =
      n.head.isUpper || !p.contains(n)
    bfs("start", part1Filter).count(_.head == "end")
  }

  def part2: Int = {
    def part2Filter(n: String, p: List[String]): Boolean =
      if n == "start" then false
      else if n.head.isUpper then true
      else if !p.contains(n) then true
      else p.filter(_.head.isLower)
        .groupBy(identity)
        .forall(_._2.sizeIs < 2)
    bfs("start",part2Filter).count(_.head == "end")
  }

  @main
  def PassagePathing(): Unit = {
    Console.println(part1) // 3369
    Console.println(part2) // 85883
  }
}