package day06

import AoC_Lib.*
import IterableExtensions.groupCount

object day06 {
  val initState: Seq[Int] = inputStr(day = 6)
    .split(',').map(_.toInt)

  def calcFishCount(l: Seq[Int], d: Int): Array[Long] = {
    val m = l.groupCount(identity)
    val a = Array.ofDim[Long](9)
    m.foreach{ (i, v) => a(i) = v }
    (0 to d-1).foreach { i => a((i + 7) % 9) += a(i % 9) }
    a
  }

  def part1: Long = calcFishCount(initState,80).sum
  def part2: Long = calcFishCount(initState,256).sum

  @main
  def Lanternfish(): Unit = {
    Console.println(part1) // 375482
    Console.println(part2) // 1689540415957
  }
}