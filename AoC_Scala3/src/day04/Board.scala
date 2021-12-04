package day04

class Board private {
  def this(br: Array[Int]) = {
    this()
    board = br
    sum = board.sum
    win = false
  }
  private var sum: Int = 0
  private var win: Boolean = false
  private var board: Array[Int] = Array.empty
  private val rowsCnt: Array[Int] = Array.ofDim(5)
  private val columnsCnt: Array[Int] = Array.ofDim(5)
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
}

object Board {
  def fromStr(str: String): Board = {
    val bs = str
      .replace('\n',' ')
      .replace("  "," ")
      .strip()
      .split(' ')
      .map(_.toInt)
    Board(bs)
  }
}


