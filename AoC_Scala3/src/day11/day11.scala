package day11

import AoC_Lib.*
import scala.annotation.tailrec

object day11 {
  val board: Map[Pos, Int] = inputStrs(day = 11)
    .zipWithIndex
    .flatMap((s,y) => s
      .zipWithIndex
      .map((c,x) => Pos(x,y) -> c.asDigit))
    .toMap

  import scala.annotation.tailrec
  def oneStep(board: Map[Pos,Int]): (Int, Map[Pos,Int]) = {
    @tailrec def rec(board: Map[Pos,Int], flash: List[Pos], flashed: Set[Pos]): (Map[Pos,Int],Set[Pos]) = {
      if flash.isEmpty then (board,flashed)
      else if flashed.contains(flash.head) then
        rec(board,flash.tail,flashed)
      else {
        val pos = flash.head
        var newBoard = board + (pos -> 0)
        val neighbors = pos.near.filter(newBoard.contains).filterNot(flashed.contains)
        newBoard = neighbors.foldLeft(newBoard)((b,p) => b + (p -> (b(p) + 1)))
        val newFlash = neighbors.filter(newBoard(_) > 9).filterNot(flashed.contains)  ++: flash
        val newFlashed = flashed + pos
        rec(newBoard,newFlash,newFlashed)
      }
    }
    val updatedBoard = board.view.mapValues(_ + 1).toMap
    val (newBoard, flashed) = rec(updatedBoard, updatedBoard.filter(_._2 > 9).keys.toList, Set.empty)
    (flashed.size,newBoard)
  }

  def printBoard(board: Map[Pos, Int]): Unit = {
    (board.minBy(_._1.x)._1.x to board.maxBy(_._1.x)._1.x)
      .foreach{ y =>
        (board.minBy(_._1.y)._1.y to board.maxBy(_._1.y)._1.y)
          .foreach{ x =>
            print(board(Pos(x,y)))
          }
        println()
      }
  }

  def iterateSteps(fromBoard: Map[Pos,Int]): Iterator[(Int, Map[Pos, Int])] =
    Iterator.iterate((0,fromBoard))((_, board) => oneStep(board))

  def goStep(fromBoard: Map[Pos,Int], steps: Int): Int = iterateSteps(fromBoard).map(_._1).take(steps+1).sum
  def findSynchro(fromBoard: Map[Pos,Int]): Int = iterateSteps(fromBoard).indexWhere(_._1 == fromBoard.size)

  def part1: Int = goStep(board, 100)

  def part2: Int = findSynchro(board)

  @main
  def DumboOctopus(): Unit = {
    Console.println(part1) // 1673
    Console.println(part2) // 279
  }
}

