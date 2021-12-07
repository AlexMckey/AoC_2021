package day07

import AoC_Lib.*

object day07 {
  val hpos: Seq[Int] = inputStr(day = 7)
    .split(',').map(_.toInt)

  def part1: Int = {
    val res = (hpos.min to hpos.max).map(i => i -> hpos.map(v => (v - i).abs).sum)
    res.minBy(_._2)._2
  }

  def calcSum(delta: Int): Int = delta * (delta + 1) / 2
  def part2: Int = {
    val res = (hpos.min to hpos.max).map(i => i -> hpos.map(v => calcSum((v - i).abs)).sum)
    res.minBy(_._2)._2
  }

  @main
  def TheTreacheryOfWhales(): Unit = {
    Console.println(part1) // 343468
    Console.println(part2) // 1689540415957
  }
}