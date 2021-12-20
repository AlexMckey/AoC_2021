package day19

import AoC_Lib.*

import scala.annotation.tailrec

object day19 {

  type Scanner = Set[Pos3D]

  def scannerTransformation(scanner: Scanner): Seq[Scanner] =
    scanner.toSeq.map(_.allOrientation).transpose.map(_.toSet)

  case class Transform(scanner: Pos3D, beacons: Scanner)

  def findIntersectedScanners(left: Scanner, right: Scanner): Option[Transform] = {
    (for {
      p1 <- left
      rightReoriented <- scannerTransformation(right)
      p2 <- rightReoriented
      d = p1 - p2
      moved = rightReoriented.map(_ + d)
      if (moved intersect left).size >= 12
    } yield Transform(d,moved))
      .headOption
  }

  case class Solution(scanners: Scanner, beacons: Scanner)

  def solve(scanners: Seq[Scanner]): Solution = {
    @tailrec
    def rec(unmappedSectors: Seq[Scanner], foundScanners: Scanner, exploredSector: Scanner): Solution = {
      if unmappedSectors.isEmpty then Solution(foundScanners, exploredSector)
      else
        val (currentSector, newUnmappedSectors) = unmappedSectors.splitAt(1)
        val transform = findIntersectedScanners(exploredSector, currentSector.head)
        transform match {
          case None => rec(newUnmappedSectors ++ currentSector, foundScanners, exploredSector)
          case Some(scanner,beacons) => rec(newUnmappedSectors, foundScanners + scanner, exploredSector union beacons)
        }
    }
    rec(scanners.tail,Set(Pos3D.zero),scanners.head)

//    @tailrec
//    def helper(scanners: Seq[Scanner], beacons: Scanner, frontier: Scanner, scannerPoss: Scanner): Solution = {
//      val newBeacons = beacons ++ frontier
//      if (scanners.isEmpty)
//        Solution(scannerPoss, newBeacons)
//      else {
//        val (matchedScanners, orientedScanners, matchedScannerPoss) = (for {
//          scanner <- scanners
//          //(scannerPos, orientedScanner) <- findIntersectedScanners(frontier, scanner)
//          foundScanner <- findIntersectedScanners(frontier, scanner)
//        } yield (scanner, foundScanner.beacons, foundScanner.scanner)).unzip3
//        val newScanners = scanners.filterNot(matchedScanners.contains)
//        val newFrontier = orientedScanners.reduce(_ ++ _)
//        val newScannerPoss = scannerPoss ++ matchedScannerPoss
//        helper(newScanners, newBeacons, newFrontier, newScannerPoss)
//      }
//    }
//    helper(scanners.tail, Set.empty, scanners.head, Set(Pos3D.zero))
  }

  def largestScannerDistance(scanners: Seq[Scanner]): Int = {
    val scannerPoss = solve(scanners).scanners
    (for {
      p1 <- scannerPoss.iterator
      p2 <- scannerPoss.iterator
    } yield p1 Distance p2).max
  }

  val scanners: Seq[Scanner] = inputStr(day = 19)
    .splitByBlankLines()
    .map(_.split('\n')
      .tail
      .map(Pos3D.toPos3D)
      .toSet)

  def part1: Int = solve(scanners)._2.size
  def part2: Int = largestScannerDistance(scanners)

  @main
  def BeaconScanner(): Unit = { //slowly, but correct
    Console.println(part1)     // 449
    Console.println(part2)     // 13128
  }
}
