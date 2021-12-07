package day04

import AoC_Lib.*

import scala.annotation.tailrec

object day04 {
  private val ss: Seq[String] = inputStr(day = 4).splitByBlankLines()

  def part1: Int = {
    @tailrec
    def recHelper(ns: List[Int], bs: List[Board]): Int = {
      val n = ns.head
      val newBoards = bs.map(_.nextNum(n))
      newBoards.find(_.isWin) match {
        case Some(b) => b.Sum * n
        case None => recHelper(ns.tail, newBoards)
      }
    }
    val nums: Array[Int] = ss.head.split(',').map(_.toInt)
    val boards: Seq[Board] = ss.tail.map(Board.fromStr)
    recHelper(nums.toList,boards.toList)
  }

  def part2: Int = {
    @tailrec
    def recHelper(ns: List[Int], bs: List[Board]): Int = {
      val n = ns.head
      val newBoards = bs.map(_.nextNum(n))
      val restBoards = newBoards.filterNot(_.isWin)
      if restBoards.isEmpty then
        bs.head.Sum * n
      else recHelper(ns.tail,restBoards)
    }
    val nums: Array[Int] = ss.head.split(',').map(_.toInt)
    val boards: Seq[Board] = ss.tail.map(Board.fromStr)
    recHelper(nums.toList,boards.toList)
  }

  @main
  def GiantSquid(): Unit = {
    Console.println(part1) // 65325
    Console.println(part2) // 4624
  }

}
