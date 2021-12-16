package AoC_Lib

import scala.annotation.tailrec

trait Grid[T] {
  def heuristicDistance(from: T, to: T): Int

  def getNeighbours(state: T): List[T]

  def moveCost(from: T, to: T): Int
}

def aStarSearch[T](start: T, finish: T, grid: Grid[T]): Option[Int] = {
  case class NodeInfo(costFromStart: Int, estimatedTotalCost: Int)

  @tailrec
  def loop(closed: Set[T], open: Map[T, NodeInfo]): Option[Int] = {
    if (open.isEmpty) return None
    val (current, NodeInfo(currentCostFromStart, estimatedTotalCost)) = open.minBy(_._2.estimatedTotalCost)
    if (current == finish) return Some(estimatedTotalCost)
    loop(
      closed + current,
      grid.getNeighbours(current)
        .filterNot(closed.contains)
        .foldLeft(open - current) {
          case (open, neighbor) =>
            val neighborCostFromStart = currentCostFromStart + grid.moveCost(current, neighbor)
            if (open.get(neighbor).exists(_.costFromStart <= neighborCostFromStart)) open
            else open.updated(neighbor,
              NodeInfo(neighborCostFromStart, neighborCostFromStart + grid.heuristicDistance(neighbor, finish)))
        })
  }

  loop(Set(), Map(start -> NodeInfo(0, grid.heuristicDistance(start, finish))))
}
