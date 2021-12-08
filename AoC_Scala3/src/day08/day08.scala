package day08

import AoC_Lib.*
import IterableExtensions.groupCount

object day08 {
  val indicator: Map[Set[Char],Int] = List("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
    .map(_.toSet).zipWithIndex.toMap // set(abcefg) -> "0", set(cf) -> "1", etc...
  val uniqIndLens: Set[Int] = indicator.map(_._1.size)
    .groupCount(identity)
    .filterNot(_._2 > 1).keySet // List(2,3,4,7) is a count segments for "1", "7", "4" and "8" > digit with unique count segments

  class Signal(input: String){
    val Array(iSignal, oSignal) = input
      .replace(" | ", "|")
      .split('|')
      .map(_.split(' '))
    def decode: Int = {
      val sl = iSignal.map(s => s.length -> s.toSet)
        .groupMapReduce(_._1)(_._2)(_ intersect _)  // length segments -> intersect all segments for this length
                                                    // (sample: ("2" == 'acdeg') x ("3" == 'acdfg') x ("5" == 'abdfg') => 5 -> 'adg')
      val a = sl(3) diff sl(2)   // 'a' segment => ("7" == 'acf') - ("1" == 'cf')
      val d = sl(5) diff sl(6)   // 'd' segment => ("2" x "3" x "5" == 'adg') - ("6" x "9" x "0" == 'abgf')
      val b = sl(4) diff sl(2) diff d  // 'b' segment => (("4" == 'bcdf') - ("1" == 'cf') == 'bd') - 'd'
      val f = sl(6) diff sl(5) diff b  // 'f' segment => (("6" x "9" x "0" == 'abgf') - ("2" x "3" x "5" == 'adg') == 'bf') - 'b'
      val g = sl(6) intersect sl(5) diff a  // 'g' segment => (("6" x "9" x "0" == 'abgf') x ("2" x "3" x "5" == 'adg') == 'ag') - 'a'
      val c = sl(2) diff f  // 'c' segment => ("1" == 'cf') - 'f'
      val e = sl(7) diff sl(4) diff a diff g  // 'f' segment => (("8" == 'abcdefg') - ("4" == 'bcdf') == 'aeg') - 'a' - 'g'

      val decs = List(a,b,c,d,e,f,g).map(_.head).zip("abcdefg").toMap
      oSignal.map(_.map(decs).toSet)
        .map(indicator).mkString.toInt
    }
  }


  val signals: Seq[Signal] = inputStrs(day = 8)
    .map(Signal(_))

  def part1: Int = signals
    .map(_.oSignal.map(_.length)
      .count(uniqIndLens.contains))
    .sum

  def part2: Int = signals
    .map(_.decode)
    .sum

  @main
  def SevenSegmentSearch(): Unit = {
    Console.println(part1) // 239
    Console.println(part2) // 946346
  }
}