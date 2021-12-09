case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def near: Seq[Pos] = Pos.near4.map(_ + this)
}
object Pos {
  val zero = Pos()
  val near4: Seq[Pos] = Seq(Pos(0, 1), Pos(-1, 0), Pos(1, 0), Pos(0, -1))
}
val s = "2199943210\n3987894921\n9856789892\n8767896789\n9899965678"
val board: Map[Pos, Int] = s.split('\n')
  .zipWithIndex
  .flatMap((s,y) => s
    .zipWithIndex
    .map((c: Char,x) => Pos(x,y) -> c.asDigit))
  .toMap
  .withDefaultValue(10)
val lps = board
  .filter((p: Pos,i) => p.near
    .map(board)
    .forall(_>i))
  .toList
lps.map(_._2 + 1).sum
def findBasin(p: Pos): Int = {
  //@tailrec
  def rec(cur: Set[Pos], acc: Set[Pos] = Set.empty): Set[Pos] = {
    if cur.isEmpty then acc
    else rec(cur.flatMap(p => p.near.filter(a => board(a) < 9)) diff acc, acc union cur)
  }
  rec(Set(p)).size
}
val lp0 = lps(0)._1
val b0_0 = Set(lp0)
val b0_1 = b0_0
  .flatMap(p => Pos.near4
    .map(_ + p)
    .filter(a => !b0_0.contains(a) && board(a) < 9)) union b0_0
val b0_2 = b0_1
  .flatMap(p => Pos.near4
    .map(_ + p)
    .filter(a => !b0_1.contains(a) &&  board(a) < 9)) union b0_1
val b0_3 = b0_2
  .flatMap(p => Pos.near4
    .map(_ + p)
    .filter(a => !b0_2.contains(a) && board(a) < 9)) union b0_2
val b0_4 = b0_3
  .flatMap(p => Pos.near4
    .map(_ + p)
    .filter(a => !b0_3.contains(a) && board(a) < 9)) union b0_3
val b0_5 = b0_4
  .flatMap(p => Pos.near4
    .map(_ + p)
    .filter(a => !b0_4.contains(a) && board(a) < 9)) union b0_4
b0_5.size

findBasin(lp0)
val bs: List[Int] = lps.map((p,i) => findBasin(p))
bs.sorted(Ordering.Int.reverse).take(3).reduce(_ * _)
