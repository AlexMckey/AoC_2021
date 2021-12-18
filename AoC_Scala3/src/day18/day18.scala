package day18

import AoC_Lib.*

object day18 {
  import scala.annotation.tailrec

  sealed trait Tree {
    def +(other: Tree): Tree = Pair(this,other).reduce
    def addL(num: Int): Tree
    def addR(num: Int): Tree
    def magnitude: Int

    def explode: Option[Tree] = {
      def rec(num: Tree, lev: Int): Option[(Option[Int],Tree,Option[Int])] =
        num match {
          case Pair(RegNum(l),RegNum(r)) if lev >= 4 => Some(Some(l),RegNum(0),Some(r))
          case Pair(l, r) =>
            rec(l,lev+1).map((lAdd, l, rAdd) =>
              (lAdd, Pair(l, rAdd.map(r.addL).getOrElse(r)), None)) orElse
              rec(r,lev+1).map((lAdd, r, rAdd) =>
                (None, Pair(lAdd.map(l.addR).getOrElse(l), r), rAdd))
          case _ => None
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

  def part1: Int = inputStrs(day = 18)
      .map(Tree.parse)
      .reduce(_+_)
      .magnitude

  def part2: Int = {
    val ns = inputStrs(day = 18).map(Tree.parse)
    ns.combinations(2).flatMap { l =>
        List(l,l.reverse)
      }.map(_.reduce(_+_).magnitude).max
//    ns.flatMap {
//      a =>
//        ns.filterNot { b => a == b }
//          .map { b => (a + b).magnitude }
//    }.max
  }

  @main
  def Snailfish(): Unit = {
    Console.println(part1)     // 4008
    Console.println(part2)     // 4667
  }
}