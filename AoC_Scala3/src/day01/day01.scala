package day01

import AoC_Lib.*

object day01 {
  val nums: Seq[Int] = inputInts("input01.txt")

  def calcDeeps(is: Seq[Int]): Int =
    is.sliding(2).count { case Seq(a1, a2) => a1 < a2 }

  @main
  def SonarSweep(): Unit = {
    val res1 = calcDeeps(nums)
    Console.println(res1) // 1448
    val res2 = calcDeeps(nums.sliding(3).map(_.sum).toSeq)
    Console.println(res2) // 1471
  }
}
