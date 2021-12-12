import scala.collection.mutable

case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def near: Seq[Pos] = Pos.near8.map(_ + this)
}
object Pos {
  val zero: Pos = Pos()
  private val r = -1 to 1
  val near8: Seq[Pos] = r.flatMap(x => r.map(y => Pos(x,y)))
    .filterNot(_ == Pos.zero)
}
val r = -1 to 1
r.flatMap(x => r.map(y => Pos(x,y)))

val s = "5483143223\n2745854711\n5264556173\n6141336146\n6357385478\n4167524645\n2176841721\n6882881134\n4846848554\n5283751526"
val s_ = "11111\n19991\n19191\n19991\n11111"
val board: Map[Pos, Int] = s.split('\n')
  .zipWithIndex
  .flatMap((s,y) => s
    .zipWithIndex
    .map((c: Char,x) => Pos(x,y) -> c.asDigit))
  .toMap
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
printBoard(board)

import scala.annotation.tailrec
def step(board: Map[Pos,Int]): (Int, Map[Pos,Int]) = {
  @tailrec def rec(board: Map[Pos,Int], flash: List[Pos], flashed: Set[Pos]): (Map[Pos,Int],Set[Pos]) = {
    if flash.isEmpty then (board,flashed)
    else if flashed.contains(flash.head) then
      rec(board,flash.tail,flashed)
    else {
      val pos = flash.head
      var newBoard = board + (pos -> 0)
      val neighbors = pos.near.filter(newBoard.contains).filterNot(flashed.contains)
      //newBoard = neighbors.foldLeft(newBoard)((b,p) => b + (p -> (b(p) + 1)))
      newBoard = newBoard
        ++ neighbors
          .groupMapReduce(identity)(_ => 1)(_ + _)
          .map((p,i) => p -> (newBoard(p) + i))
      val newFlash = neighbors.filter(newBoard(_) > 9).filterNot(flashed.contains)
        ++: flash
      val newFlashed = flashed + pos
      rec(newBoard,newFlash,newFlashed)
    }
  }
  val updatedBoard = board.view.mapValues(_ + 1).toMap
  val (newBoard, flashed) = rec(updatedBoard, updatedBoard.filter(_._2 > 9).keys.toList, Set.empty)
  (flashed.size,newBoard)
}

val (c1,b1) = step(board)
printBoard(b1)
val (c2,b2) = step(b1)
printBoard(b2)
