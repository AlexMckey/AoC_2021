case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def -(other: Pos): Pos = Pos(x - other.x, y - other.y)
  def *(k: Int): Pos =  Pos(k * x, k * y)
  def *(rot: Pos): Pos =  Pos(x * rot.x, y * rot.y)
  //def D(that: Pos): Int = (x - that.x).abs + (y - that.y).abs
  def D(other: Pos): Double = {
    val delta = other - this
    math.sqrt(1.0 * delta.x * delta.x + delta.y * delta.y)
  }

  override def toString: String = s"[$x,$y]"
}
object Pos {
  val zero: Pos = Pos()
  extension (s: String)
    def toPos: Pos = {
      val Array(x,y) = s.split(',').map(_.toInt)
      Pos(x,y)
    }
  val axisOffsets: Seq[Pos] = Seq(Pos(0, 1), Pos(-1, 0), Pos(1, 0), Pos(0, -1))
  val diagonalOffsets: Seq[Pos] = Seq(Pos(-1, 1), Pos(1, 1), Pos(-1, -1), Pos(1, -1))
  val allOffsets: Seq[Pos] = axisOffsets ++ diagonalOffsets
  val rotations: Set[Pos] = allOffsets.toSet
}

val s = "--- scanner 0 ---\n0,2\n4,1\n3,3\n\n--- scanner 1 ---\n-1,-1\n-5,0\n-2,1"
val ps = s.split("\n\n")
val scs = ps.map(_.split('\n').tail.map(Pos.toPos))
scs.map(a => a.map(p => p - a.head))
val s1 = scs.head.map(p => scs.head.map(d => p.D(d)).toSet -> p)
val s2 = scs(1).map(p => scs(1).map(d => p.D(d)).toSet -> p)
val u1 = (s1 ++ s2).groupMap(_._1)(_._2)
u1.values.map{ case Array(p1,p2) => p1 - p2}