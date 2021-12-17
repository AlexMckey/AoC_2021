case class Pos(x: Int = 0, y: Int = 0) extends Ordering[Pos] {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def *:(k: Int): Pos =  Pos(k * x, k * y)
  def <=(that: Pos): Boolean =
    x <= that.x && y <= that.y
  override def toString: String = s"[$x,$y]"
  override def compare(p1: Pos, p2: Pos): Int =
    if p1.y == p2.y then p2.x - p1.x else p2.y - p1.y
}
object Pos {
  val zero: Pos = Pos()
}
case class Box(min: Pos, max: Pos) {
  def contains(pos: Pos): Boolean =
    min <= pos && pos <= max
}

val s = "target area: x=20..30, y=-10..-5"
val target = s match {
  case s"target area: x=$xMin..$xMax, y=$yMin..$yMax" =>
    Box(Pos(xMin.toInt,yMin.toInt),Pos(xMax.toInt,yMax.toInt))
}
target
def step(vel: Pos, target: Box): Option[(Pos,Int)] =
  Iterator.unfold((vel,Pos.zero,0)) { case (v, p, y) =>
    if target.contains(p) then None
    else if p.y < target.min.y then None
    else
      val newP = p + v
      val newV = v + Pos(-1 * v.x.sign, -1)
      Some((newP, y max newP.y) -> (newV, newP, y max newP.y))
  }.find{ case (p,_) => target.contains(p)}
step(Pos(7,2),target)
step(Pos(6,3),target)
step(Pos(9,0),target)
step(Pos(6,9),target)
step(Pos(17,-4),target)
step(Pos(-20,-20),target)
val vs = (-20 to 20).flatMap{ xv =>
  (-20 to 20).map{ yv =>
    Pos(xv,yv)
  }
}
vs.map(v => v -> step(v,target))
  .collect{case (v,Some(p)) => v -> p._2 }
  .maxBy(_._2)
Iterator.unfold((Pos(6,9),Pos.zero,0)) { case (v, p, y) =>
  if target.contains(p) then None
  else if p.y < target.min.y then None
  else
    val newP = p + v
    val newV = v + Pos(-1 * v.x.sign, -1)
    Some((newP, y max newP.y) -> (newV, newP, y max newP.y))
}.toList
def iyv(yv: Int): Boolean =
  Iterator.iterate((0, yv)) { (y, v) => (y + v) -> (v - 1)}
    .map(_._1)
    .dropWhile(y => y > target.max.y)
    .next() >= target.min.y
def ixv(xv: Int): Boolean = {
  val xRes = Iterator.iterate((0, xv)) { (x, v) => (x + v) -> (v - 1 * v.sign)}
    .dropWhile((x,v) => x < target.min.x && v != 0)
    .next()._1
  xRes >= target.min.x && xRes <= target.max.x
}
Iterator.iterate((0, 9)) { (y, v) =>
  (y + v) -> (v - 1)
}.map(_._1).dropWhile(y => y > target.max.y).next() >= target.min.y
iyv(9)
def simulateY(initialYVelocity: Int): Boolean = {
  Iterator.iterate((0, initialYVelocity))((y, yVelocity) => (y + yVelocity, yVelocity - 1))
    .map(_._1)
    .takeWhile(_ >= target.min.y)
    .exists(_ <= target.max.y)
}
def simulateX(initialXVelocity: Int): Boolean = {
  Iterator.iterate((0, initialXVelocity))((x, xVelocity) => (x + xVelocity, xVelocity + (if (xVelocity > 0) -1 else 0)))
    .map(_._1)
    .takeWhile(_ <= target.max.x)
    .exists(_ >= target.min.x)
}
simulateY(9)
(-100 to 100).filter(iyv)
(-100 to 100).filter(simulateY)

-5 - 1 * -5.sign
5 - 1 * 5.sign
0 - 1 * 0.sign
ixv(7)
ixv(1)
ixv(0)
(0 to 100).filter(ixv)

def iterateX(xVel: Int): Iterator[(Int,Int)] = Iterator.iterate((0, xVel)) { (x,v) => (x + v) -> (v - 1 * v.sign) }
def inboundX(t: Box)(xVel: Int): Boolean = {
  val xRes = iterateX(xVel)
    .dropWhile((x,v) => x < t.min.x && v != 0)
    .next()._1
  xRes >= t.min.x && xRes <= t.max.x
}

(0 to 100).filter(inboundX(target))

def iterateY(yVel: Int): Iterator[(Int,Int)] = Iterator.iterate((0, yVel)) { (y,v) => (y + v) -> (v - 1) }
def inboundY(t: Box)(yVel: Int): Boolean = iterateY(yVel)
  .map(_._1)
  .dropWhile(y => y > t.max.y)
  .next() >= t.min.y

target.max.y
target.min.y.abs
(target.min.y to target.min.y.abs).filter(inboundY(target))
  .map(iterateY(_).map(_._1).takeWhile(_ >= target.min.y).max).max

def iterateXY(vel: Pos): Iterator[Pos] =
  (iterateX(vel.x).map(_._1) zip iterateY(vel.y).map(_._1)).map(Pos.apply)
def inboundXY(t: Box)(vel: Pos): Boolean = {
  val p = iterateXY(vel)
    .dropWhile(p => p.x < t.min.x || p.y > t.max.y)
    .next()
  t.contains(p)
}
val res = iterateXY(Pos(6,10))
  .dropWhile(p => p.x < target.min.x || p.y > target.max.y)
  .next()
res
target.contains(res)
inboundXY(target)(Pos(6,9))
(0 to target.max.x)
  .flatMap(x => (target.min.x to target.min.x.abs)
    .map(y => Pos(x,y)))
  .map(inboundXY(target))
