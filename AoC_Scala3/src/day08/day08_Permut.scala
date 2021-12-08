package day08

import AoC_Lib.*
import IterableExtensions.groupCount

object day08_Permut {
  val indicator: IndexedSeq[Set[Char]] = IndexedSeq("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
    .map(_.toSet)
  val signals: Seq[(Seq[Set[Char]], Seq[Set[Char]])] = inputStrs(day = 8)
    .map{
      case s"$is | $os" =>
        def parse(s: String): Seq[Set[Char]] = s.words.map(_.toSet)
        (parse(is), parse(os))
    }

  def part1: Int = {
    val uniqIndLens: Set[Int] = indicator.map(_.size)
      .groupCount(identity)
      .filter(_._2 == 1).keySet

    signals
      .map(_._2.map(_.size)
        .count(uniqIndLens.contains))
      .sum
  }

  def part2: Int = {
    val digits = indicator.toSet
    val chars = digits.flatten.toSeq
    val perms = chars.permutations.map(_.zip(chars).toMap).toSeq
    signals.map {
        case (iSignal, oSignal) =>
          val decode = perms
            .find(decs => iSignal.view
              .map(_.map(decs))
              .forall(digits.contains))
            .get
          oSignal.map(_.map(decode))
            .map(indicator.indexOf)
            .mkString
            .toInt
      }.sum
  }

  @main
  def SevenSegmentSearchPermutation(): Unit = {
    Console.println(part1) // 239
    Console.println(part2) // 946346
  }
}