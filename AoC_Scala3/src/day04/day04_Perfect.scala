package day04

import AoC_Lib.*

import scala.annotation.tailrec

object day04_Perfect {
  val ss: Seq[String] = inputStr("input04.txt").splitByBlankLines()

  val nums: Array[Int] = ss.head.split(',').map(_.toInt)
  val boards: Seq[Array[Array[Int]]] = ss.tail
    .map(_.split('\n')
      .map(_.trim
        .replace("  ", " ")
        .split(' ')
        .map(_.toInt)))

  def boardScores(nums: Seq[Int], boards: Seq[Array[Array[Int]]]): Seq[Int] =
    nums.foldLeft(Seq.empty[Int], Set.empty[Int], boards) {
      case ((scores, marked, active), num) =>
        val newMarked     = marked + num
        val (won, newActive) = active.partition(b => (b ++ b.transpose).exists(_.forall(newMarked.contains)))
        val newScores     = won.map(board => num * board.flatten.filterNot(newMarked.contains).sum)
        (scores ++ newScores, newMarked, newActive)
    }._1

  @main
  def GiantSquidPerfect(): Unit = {
    val res = boardScores(nums,boards)
    Console.println(res.head) // 65325
    Console.println(res.last) // 4624
  }
}
