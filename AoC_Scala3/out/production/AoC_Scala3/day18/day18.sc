import scala.annotation.tailrec

val s = "[1,2]\n[[1,2],3]\n[9,[8,7]]\n[[1,9],[8,5]]\n[[[[1,2],[3,4]],[[5,6],[7,8]]],9]\n[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]\n[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"
val ns = s.split('\n')
sealed trait Tree {
  def +(other: Tree): Tree = Pair(this,other).reduce
  def addL(num: Int): Tree
  def addR(num: Int): Tree
  def magnitude: Int
  def explode: Option[Tree] = {
    def rec(num: Tree, lev: Int): Option[(Option[Int],Tree,Option[Int])] =
      num match {
        case RegNum(_) => None
        case Pair(RegNum(l),RegNum(r)) if lev >= 4 => Some(Some(l),RegNum(0),Some(r))
        case Pair(l, r) =>
          rec(l,lev+1).map((lAdd, l, rAdd) =>
            (lAdd, Pair(l, rAdd.map(r.addL).getOrElse(r)), None)) orElse
          rec(r,lev+1).map((lAdd, r, rAdd) =>
            (None, Pair(lAdd.map(l.addR).getOrElse(l), r), rAdd))
      }
    rec(this,0).map(_._2)
  }

  def split: Option[Tree] =
    this match {
      case RegNum(v) if v >= 10 => Some(Pair(RegNum(v / 2),RegNum(v - v / 2)))
      case Pair(l,r) => l.split.map(Pair(_,r)) orElse r.split.map(Pair(l,_))
      case _ => None
    }

  @tailrec
  final def reduce: Tree = {
    this.explode match {
      case Some(num) => num.reduce
      case None => this.split match {
        case Some(num) => num.reduce
        case None => this
      }
    }
  }
}
object Tree {
  def parse(s: String): Tree = {
    @tailrec
    def rec(pos: Int = 0, lev: Int = 0): Tree =
      s(pos) match {
        case '[' => rec(pos+1,lev+1)
        case ']' => rec(pos+1,lev-1)
        case ',' if lev == 1 =>
          val (ls, rs) = s.tail.splitAt(pos-1)
          Pair(parse(ls),parse(rs.tail.init))
        case d if d.isDigit && lev == 0 => RegNum(d.asDigit)
        case _ => rec(pos+1,lev)
      }
    rec()
  }
}
case class RegNum(v: Int) extends Tree {
  override def magnitude: Int = v
  override def addL(num: Int): Tree = RegNum(v+num)
  override def addR(num: Int): Tree = RegNum(v+num)
  override def toString: String = v.toString
}
case class Pair(l: Tree, r: Tree) extends Tree {
  def magnitude: Int = 3 * l.magnitude + 2 * r.magnitude
  override def addL(num: Int): Tree = Pair(l.addL(num),r)
  override def addR(num: Int): Tree = Pair(l,r.addR(num))
  override def toString: String = s"[${l.toString},${r.toString}]"
}

extension (nums: Seq[Tree])
  def sum = nums.reduce(_ + _)

import Tree.parse

val ss = "[9,[8,7]]"
val pos = 2
val (l,r) = ss.tail.dropRight(1).splitAt(pos-1)
l
r.tail
val n6 = parse(ns(6))
n6.magnitude
parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude
7 / 2
parse("[[[[[9,8],1],2],3],4]").reduce
parse("[7,[6,[5,[4,[3,2]]]]]").reduce
parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").reduce
parse("[[[[4,3],4],4],[7,[[8,4],9]]]") + parse("[1,1]")
"[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\n[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\n[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\n[7,[5,[[3,8],[1,4]]]]\n[[2,[2,2]],[8,[8,1]]]\n[2,9]\n[1,[[[9,3],9],[[9,0],[0,7]]]]\n[[[5,[7,4]],7],1]\n[[[[4,2],2],6],[8,7]]"
  .split('\n').map(parse).sum
val ns = "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n[[[5,[2,8]],4],[5,[[9,9],0]]]\n[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n[[[[5,4],[7,7]],8],[[8,3],8]]\n[[9,3],[[9,9],[6,[4,9]]]]\n[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]"
  .split('\n').map(parse)
val res = ns.sum
res
res.magnitude
ns.combinations(2)
  .flatMap(l => List(l.sum,l.reverse.sum).map(_.magnitude)).max
