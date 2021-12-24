package day24

import AoC_Lib.*

import scala.collection.mutable

object day24 {
  def calculateNumber(input: Seq[String], findMax: Boolean = true): Long = {
    val inputStash = scala.collection.mutable.Stack[(Int,Int)]()
    val finalDigits = Array.fill(14){0}

    input.sliding(18, 18).zipWithIndex.foreach { case (block,targetIndex) =>
      val check = block(5).split(' ').last.toInt
      val offset = block(15).split(' ').last.toInt
      if check > 0
      then
        inputStash.push(targetIndex -> offset)
      else
        val (sourceIndex, offset) = inputStash.pop()
        val totalOffset = offset + check
        (totalOffset > 0, findMax) match {
          case (true, true) =>
            finalDigits(sourceIndex) = 9 - totalOffset
            finalDigits(targetIndex) = 9
          case (true, false) =>
            finalDigits(sourceIndex) = 1
            finalDigits(targetIndex) = 1 + totalOffset
          case (false, true) =>
            finalDigits(sourceIndex) = 9
            finalDigits(targetIndex) = 9 + totalOffset
          case _ =>
            finalDigits(sourceIndex) = 1 - totalOffset
            finalDigits(targetIndex) = 1
        }
    }
    finalDigits.mkString.toLong
  }

  lazy val program: Seq[String] = inputStrs(day = 24)

  @main
  def ArithmeticLogicUnit(): Unit = {
    println(calculateNumber(program))                   // 39494195799979
    println(calculateNumber(program, findMax = false))  // 13161151139617
  }
}