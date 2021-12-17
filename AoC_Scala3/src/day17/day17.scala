package day17

import AoC_Lib.*

import scala.annotation.tailrec

object day17 {
  val target: Box = inputStr(day = 17) match {
    case s"target area: x=$xMin..$xMax, y=$yMin..$yMax" =>
      Box(Pos(xMin.toInt,yMin.toInt),Pos(xMax.toInt,yMax.toInt))
  }

  def iterateY(yVel: Int): Iterator[(Int,Int)] = Iterator.iterate((0, yVel)) { (y,v) => (y + v) -> (v - 1) }
  def inboundY(t: Box)(yVel: Int): Boolean = iterateY(yVel)
    .map(_._1)
    .dropWhile(y => y > t.max.y)
    .next() >= t.min.y

  def part1: Int = (target.min.y to target.min.y.abs)
    .filter(inboundY(target))
    .map(iterateY(_)
      .map(_._1)
      .takeWhile(_ >= target.min.y)
      .max)
    .max

  def next(p: Pos, v: Pos): (Pos, Pos) = (p + v, v - Pos(v.x.sign, 1))

  @tailrec
  def reaches(p: Pos, v: Pos, maxY: Int = 0): Option[Int] =
    if target.contains(p) then Some(maxY)
    else if v.y < 0 && p.y < target.min.y then None
    else
      val (np, nv) = next(p, v)
      reaches(np, nv, maxY max np.y)

  val velocities: Seq[Int] = for {
    x <- 1 to target.max.x
    y <- target.min.y to target.min.y.abs
    v = Pos(x, y)
    maxY <- reaches(Pos.zero, v)
  } yield maxY

  def part2: Int = velocities.size

  @main
  def TrickShot(): Unit = {
    Console.println(part1)     // 23005
    Console.println(part2)     // 2040
  }
}