package day14

import AoC_Lib.*
import IterableExtensions.groupCount

case class Rule(pattern: String, insert: String){
  def first: String = s"${pattern.head}$insert"
  def second: String = s"$insert${pattern.tail}"
}

val pm = """(\w)(\w) -> (\w)""".r

object Day14 {
  val Array(init: String, rs: String) = inputStr(day = 14).split("\n\n")
  val rules: Map[String,List[String]] = rs.split('\n')
    .map{ case pm(first,second,insert) =>
      s"$first$second" -> List(s"$first$insert",s"$insert$second")}
    .toMap

  def iterByStrings(init: String, cnt: Int): String =
    Iterator.iterate(init){ s =>
        val sb = new StringBuilder()
        sb.append(s.head)
        s.sliding(2).foreach(p => sb.append(rules(p).last))
        sb.toString()}
      .drop(cnt)
      .next()

  def calcCnt(polymerTemplate: String, count: Int): Map[Char,Long] = {
    def iterMap(init: Map[String,Long]): Map[String,Long] =
      Iterator.iterate(init){ m =>
        m.toList.flatMap{ (pattern,cnt) =>
          rules(pattern).map(_ -> cnt)
        }.groupMapReduce(_._1)(_._2)(_ + _)
      }.drop(count).next()
    val start = polymerTemplate.sliding(2).toList
      .groupMapReduce(identity)(_ => 1L)(_ + _)
    val polymer = iterMap(start)
    val charCounts = polymer.toList
      .map{ (c,i) => c.head -> i }
      .groupMapReduce(_._1)(_._2)(_ + _)
      .map{ (c,i) => c -> (if polymerTemplate.last == c then i + 1 else i) }
    charCounts.toMap
  }

  def part1: Int = {
    val res = iterByStrings(init, 10)
      .groupMapReduce(identity)(_ => 1)(_ + _).values
    res.max - res.min
  }

  def part1_new: Long = {
    val res = calcCnt(init,10).values
    res.max - res.min
  }

  def part2: Long = {
    val res = calcCnt(init,40).values
    res.max - res.min
  }

  @main
  def ExtendedPolymerization(): Unit = {
    Console.println(part1)     // 2027
    Console.println(part1_new) // 2027
    Console.println(part2)     // 2265039461737
  }
}
