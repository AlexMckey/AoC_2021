package day25

import AoC_Lib.*

object day25 {
  type Cucumbers = (Set[Pos],Set[Pos])

  val (init,max) = parseCucumberSea(inputStrs(day = 25))

  extension (p: Pos)
    def goRight: Pos = if p.x == max.x then p.copy(x = 0) else p + Pos(1,0)
    def goDown: Pos = if p.y == max.y then p.copy(y = 0) else p + Pos(0,1)

  def parseCucumberSea(ss: Seq[String]): (Cucumbers,Pos) = {
    val cs: Map[Pos,Char] = ss.zipWithIndex
      .flatMap { (s,y) =>
        s.zipWithIndex
          .map { (c,x) => Pos(x,y) -> c }
      }.toMap
    val max = cs.keySet.reduce(_ max _)
    val (goR,goD) = cs
      .filterNot(_._2 == '.')
      .partition(_._2 == '>')
    (goR.keySet,goD.keySet) -> max
  }

  private def step(cucumbers: Cucumbers): (Cucumbers,Boolean) = {
    def halfStep(workSet: Set[Pos], otherSet: Set[Pos], f: Pos => Pos): Set[Pos] = {
      workSet
        .filterNot{ p => (workSet contains f(p)) || (otherSet contains f(p)) }
        .foldLeft(workSet){ (acc,p) => acc excl p incl f(p) }
    }
    val (r,d) = cucumbers
    val newR = halfStep(r,d,_.goRight)
    val newD = halfStep(d,newR,_.goDown)
    (newR,newD) -> (newR == r && newD == d)
  }

  def go(init: Cucumbers): List[Boolean] = Iterator.unfold(init) {
    cucumbers =>
      val (newCucs, isChanges) = step(cucumbers)
      if isChanges then None
      else Some(isChanges, newCucs)
  }.toList

  def part1: Int = go(init).size + 1

  @main
  def SeaCucumber(): Unit = {
    Console.println(part1) // 308
  }
}
