case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def *:(k: Int): Pos = Pos(k * x, k * y)
  def unary_- : Pos = -1 *: this
  def -(that: Pos): Pos = this + (-that)
  def <=(that: Pos): Boolean = x <= that.x && y <= that.y
}
object Pos {
  val zero = Pos()
}
def printBoard(board: Set[Pos]): Unit = {
  (board.map(_.y).min to board.map(_.y).max)
    .foreach{ y =>
      (board.map(_.x).min to board.map(_.x).max)
        .foreach{ x =>
          print(if board.contains(Pos(x,y)) then '#' else '.')
        }
      println()
    }
}
val s = "6,10\n0,14\n9,10\n0,3\n10,4\n4,11\n6,0\n6,12\n4,1\n0,13\n10,12\n3,4\n3,0\n8,4\n1,10\n2,14\n8,10\n9,0"
val l = s.split('\n')
  .map(_.split(',')
    .map(_.toInt))
  .collect{ case Array(a1,a2) => Pos(a1,a2) }
  .toSet
printBoard(l)
val maxX = l.maxBy(_.x).x
val maxY = l.maxBy(_.y).y
val yfold = Pos(0,7)
val (l1,l2) = l.partition(_.y <= yfold.y)
l2
val nl2 = l2.map(p => p - Pos(0, 2 * (p.y - yfold.y)))
val nl = l1 ++ nl2
printBoard(nl)
nl.size
enum Axis{ case x; case y}
def foldAlong(sheet: Set[Pos], axis: Axis, i: Int): Set[Pos] = {
  val (l1,l2) = sheet.partition(p => if axis == Axis.x then p.x <= i else p.y <= i)
  val nl2 = l2.map{ p => p - (if axis == Axis.x
    then Pos(2 * (p.x - i), 0)
    else Pos(0, 2 * (p.y - i))) }
  l1 ++ nl2
}
def foldAlong_(sheet: Set[(Int,Int)], fold: (Char,Int)): Set[(Int,Int)] =
  sheet.map{ (x,y) => fold match {
    case ('x',v) if x > v => (v - (x - v), y)
    case ('y',v) if y > v => (x, v - (y - v))
    case _ => (x, y)
  }}
val s1 = foldAlong(l, Axis.y, 7)
val s1_ = foldAlong_(l.map(p=> p.x -> p.y), 'y' -> 7)
printBoard(s1)
printBoard(s1_.map(Pos.apply))
val s2 = foldAlong(s1, Axis.x, 5)
printBoard(s2)
