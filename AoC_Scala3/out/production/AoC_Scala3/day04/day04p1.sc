import AoC_Lib.*

val s = "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\n\n22 13 17 11  0\n 8  2 23  4 24\n21  9 14 16  7\n 6 10  3 18  5\n 1 12 20 15 19\n\n 3 15  0  2 22\n 9 18 13 17  5\n19  8  7 25 23\n20 11 10 24  4\n14 21 16 12  6\n\n14 21 17 24  4\n10 16 15  9 19\n18  8 23 26 20\n22 11 13  6  5\n 2  0 12  3  7"
val ss = s.splitByBlankLines()
var nums = ss.head.split(',').map(_.toInt)
val bss = ss.drop(1).map(_.replace('\n',' ').replace("  "," "))
val bs = bss.map(_.strip().split(' ').map(_.toInt))
val b = bs.head
var bsum = b.sum
var n = nums(0)
var i = b.indexOf(n)
bsum -= n
var c = i % 5
var r = i / 5

class Board private {
  def this(br: Array[Int]) = {
    this()
    board = br
    sum = board.sum
    win = false
  }
  var sum: Int = 0
  var win: Boolean = false
  var board: Array[Int] = Array.empty
  val rowsCnt: Array[Int] = Array.ofDim(5)
  val columnsCnt: Array[Int] = Array.ofDim(5)
  def Sum: Int = sum
  def isWin: Boolean = win
  def nextNum(n: Int): Board = {
    if !board.contains(n) then this
    else {
      val i = board.indexOf(n)
      sum -= n
      columnsCnt(i % 5) += 1
      rowsCnt(i / 5) += 1
      win = columnsCnt.exists(_ >= 5) || rowsCnt.exists( _ >= 5)
      this
    }
  }

  override def toString: String = board.sliding(5,5)
    .map(_.mkString(" "))
    .mkString("\n") + s"\ns: $sum, w: $win"
}

var boards = bs.map(Board(_))
//val b3 = boards(2)
//b3.nextNum(7)
//b3.rowsCnt
//b3.columnsCnt
//b3.nextNum(4)
//b3.nextNum(9)
//b3.nextNum(5)
//b3.nextNum(11)
//b3.nextNum(17)
//b3.nextNum(23)
//b3.nextNum(2)
//b3.nextNum(0)
//b3.nextNum(14)
//b3.nextNum(21)
//b3.nextNum(24)

import scala.annotation.tailrec
@tailrec
def recWin(ns: List[Int], bs: List[Board]): Int = {
  val n = ns.head
  val newBoards = bs.map(_.nextNum(n))
  newBoards.find(_.isWin) match {
    case Some(b) => b.Sum * n
    case None => recWin(ns.tail, newBoards)
  }
}
val res1 = recWin(nums.toList,boards.toList)

nums = ss.head.split(',').map(_.toInt)
boards = bs.map(Board(_))
@tailrec
def recLose(ns: List[Int], bs: List[Board]): Int = {
  val n = ns.head
  val newBoards = bs.map(_.nextNum(n))
  val restBoards = newBoards.filterNot(_.isWin)
  println(s"n: $n, bs:${bs.size}, restBoards: ${restBoards.size}")
  if restBoards.isEmpty then
    bs.last.Sum * n
  else recLose(ns.tail,restBoards)
}
val res1 = recLose(nums.toList,boards.toList)