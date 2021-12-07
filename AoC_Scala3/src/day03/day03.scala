package day03

import AoC_Lib.*

import scala.annotation.tailrec

object day03 {
  val report = inputStrs(day = 3).toList

  def part1(rep: List[String]): Int = {
    val bins = report.map(_.toCharArray).transpose
    val gammaBin = bins.map(_.count(_ == '1')
      .compareTo(bins.head.length/2))             // compare with half
      .map(i => (i+1)/2)                          // convert from 1 and -1 to 1 and 0
      .mkString                                   // make binary string
    val gammaRate = Integer.parseInt(gammaBin,2)
    val epsilonRate = math.pow(2,bins.length).toInt - 1 - gammaRate
    gammaRate * epsilonRate
  }

  def part2(rep: List[String]): Int = {
    @tailrec
    def calcRate(bins: List[String], bitCheck: Char = '1', pos: Int = 0): Int = {
      if bins.size == 1 then
        Integer.parseInt(bins.head,2)
      else {
        val bitCnt = bins.map(_(pos)).count(_ == bitCheck) * 2
        val bitSelect = bitCnt.compareTo(bins.length) match {
          case 1 => '1'
          case -1 => '0'
          case _ => bitCheck
        }
        calcRate(bins.filter(_(pos) == bitSelect), bitCheck, pos+1)
      }
    }
    val oxygenRate = calcRate(rep)
    val CO2Rate = calcRate(rep, '0')
    oxygenRate * CO2Rate
  }

  @main
  def BinaryDiagnostic(): Unit = {
    Console.println(part1(report)) // 4160394
    Console.println(part2(report)) // 4125600
  }
}
