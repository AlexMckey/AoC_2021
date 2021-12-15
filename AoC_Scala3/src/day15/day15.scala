package day15

import AoC_Lib.*

object day15 {
  val grid: Map[Pos, Int] = inputStrs(day = 15)
    .zipWithIndex
    .flatMap((s,y) => s
      .zipWithIndex
      .map((c,x) => Pos(x,y) -> c.asDigit))
    .toMap

  def dijkstraSearch(grid: Map[Pos,Int], start: Pos, target: Pos): Option[Int] = {
    import scala.collection.*
    val visited: mutable.Map[Pos, Int] = mutable.Map.empty
    val toVisit: mutable.PriorityQueue[(Pos, Int)] = mutable.PriorityQueue.empty(Ordering.by(-_._2))

    toVisit.enqueue(start -> 0)
    while (toVisit.nonEmpty) {
      val (node, dist) = toVisit.dequeue()
      if (!visited.contains(node)) {
        visited(node) = dist
        if (node == target) return Some(dist)
        node.near4
          .filterNot(visited.contains)
          .filterNot(grid(_) <= 0)
          .foreach(p => toVisit.enqueue(p -> (dist + grid(p))))
      }
    }
    None
  }

  val maxPosInGrid: Pos = grid.keySet.reduce(_ max _)

  def part1: Int = dijkstraSearch(grid.withDefaultValue(-1),Pos.zero,maxPosInGrid).get

  val fullgrid: Map[Pos, Int] = grid.withDefault{
    p => if p.x < 0 || p.y < 0 || p.x > (maxPosInGrid.x + 1) * 5 - 1 || p.y > (maxPosInGrid.y + 1) * 5 - 1 then -1
    else (grid(Pos(p.x % (maxPosInGrid.x + 1), p.y % (maxPosInGrid.y + 1))) + p.x / (maxPosInGrid.x + 1) + p.y / (maxPosInGrid.y + 1) - 1) % 9 + 1
  }

  def part2: Long = dijkstraSearch(fullgrid,Pos.zero,5*:(maxPosInGrid+Pos(1,1))-Pos(1,1)).get

  @main
  def Chiton(): Unit = {
    Console.println(part1)     // 656
    Console.println(part2)     // 2979
  }

}
