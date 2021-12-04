package day01

import AoC_Lib.*

object day01_Zip {
  val nums: Seq[Int] = inputInts("input01.txt")

  def calcDeeps(is: Seq[(Int,Int)]): Int =
    is.count { (a1, a2) => a1 < a2 }

  @main
  def SonarSweepWithZip(): Unit = {
    val res1 = calcDeeps(nums.zip(nums.tail))
    Console.println(res1) // 1448
    val res2 = calcDeeps(nums.zip(nums.drop(3)))
    Console.println(res2) // 1471
  }
}
