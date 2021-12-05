case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def -(other: Pos): Pos = Pos(x - other.x, y - other.y)
  def *(k: Int): Pos = Pos(x*k, y*k)
  def unary_- : Pos = this * -1
  def min(that: Pos): Pos = Pos(x min that.x, y min that.y)
  def max(that: Pos): Pos = Pos(x max that.x, y max that.y)
  override def toString: String = s"[$x,$y]"
}
object Pos {
  val zero = Pos()
  extension (s: String)
    def toPos: Pos = {
      val Array(x,y) = s.split(',').map(_.toInt)
      Pos(x,y)
  }
}
import Pos.*

import scala.math
case class Line(p1: Pos, p2: Pos) {
  def isVertical: Boolean = p1.x == p2.x
  def isHorizontal: Boolean = p1.y == p2.y
  def isVH = isVertical|| isHorizontal
  def expand: Seq[Pos] = this match {
    case lh if lh.isHorizontal =>
      rangeByCoord(_.x).map(Pos(_,p1.y))
    case lv if this.isVertical =>
      rangeByCoord(_.y).map(Pos(p1.x,_))
    case _ => rangeByCoord(_.x).zip(rangeByCoord(_.y)).map(Pos.apply)
  }
  def rangeByCoord(coord: Pos => Int): Range =
    if coord(p2) > coord(p1) then coord(p1) to coord(p2)
    else coord(p1) to coord(p2) by -1
}

val s = "0,9 -> 5,9\n8,0 -> 0,8\n9,4 -> 3,4\n2,2 -> 2,1\n7,0 -> 7,4\n6,4 -> 2,0\n0,9 -> 2,9\n3,4 -> 1,4\n0,0 -> 8,8\n5,5 -> 8,2"
val ls = s.split('\n')
  .map(_.split(" -> ")
    .map(_.toPos))
  .map{ case Array(p1, p2) => Line(p1,p2) }
val l0 = ls(0)
l0.isHorizontal
val mi0 = math.min(l0.p1.x,l0.p2.x)
val ma0 = math.max(l0.p1.x,l0.p2.x)
(mi0 to ma0).map(Pos(_,l0.p1.y))
ls.filter(_.isVH)
  .flatMap(_.expand)
  .groupBy(identity)
  .count(_._2.length > 1)
val l1 = Line(Pos(1,1),Pos(3,3))
val xrl1 = if (l1.p2.x > l1.p1.x) then l1.p1.x to l1.p2.x
else l1.p1.x to l1.p2.x by -1
val yrl1 = if (l1.p2.y > l1.p1.y) then l1.p1.y to l1.p2.y
else l1.p1.y to l1.p2.y by -1
xrl1.zip(yrl1).map(Pos.apply)
l1.rangeByCoord(_.x).zip(l1.rangeByCoord(_.y)).map(Pos.apply)
val l2 = Line(Pos(7,9),Pos(9,7))
val xrl2 = if (l2.p2.x > l2.p1.x) then l2.p1.x to l2.p2.x
else l2.p1.x to l2.p2.x by -1
val yrl2 = if (l2.p2.y > l2.p1.y) then l2.p1.y to l2.p2.y
else l2.p1.y to l2.p2.y by -1
xrl2.zip(yrl2).map(Pos.apply)
l2.rangeByCoord(_.x).zip(l2.rangeByCoord(_.y)).map(Pos.apply)
l0.rangeByCoord(_.x)
l0.rangeByCoord(_.y)
ls.flatMap(_.expand)
  .groupBy(identity)
  .count(_._2.length > 1)