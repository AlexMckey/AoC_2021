package day13

import AoC_Lib.*
import scala.annotation.tailrec

type Dot = (Int,Int)
type Fold = (Char,Int)

object day13 {
  val Array(dots,folds) = inputStr(day = 13)
    .split("\n\n")
    //.splitByBlankLines()
  val sheet: Set[Dot] = dots
    .split('\n')
    .map{ case s"$x,$y" => x.toInt -> y.toInt }
    .toSet
  val foldings: Array[Fold] = folds
    .split('\n')
    .map{ case s"fold along $axis=$num" => axis.head -> num.toInt}

  def printSheet(sheet: Set[Dot]): String = {
    val pmax = sheet.max
    val sb = new StringBuilder()
    (0 to pmax._2).foreach{ y =>
        (0 to pmax._1).foreach{ x =>
            sb.append(if sheet.contains((x,y)) then '#' else ' ')}
        sb.append('\n')}
    sb.toString()
  }

  def foldAlong(sheet: Set[Dot], fold: Fold): Set[Dot] =
    sheet.map{ (x,y) => fold match {
      case ('x',v) if x > v => (v - (x - v), y)
      case ('y',v) if y > v => (x, v - (y - v))
      case _ => (x, y)
    }}

  def part1: Int = foldAlong(sheet,foldings.head).size

  def part2: String = printSheet(foldings.foldLeft(sheet)(foldAlong))

  @main
  def TransparentOrigami(): Unit = {
    Console.println(part1) // 602
    Console.println(part2) // 946346
  }
}
