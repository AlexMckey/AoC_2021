case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def -(other: Pos): Pos = Pos(x - other.x, y - other.y)
  def *(k: Int): Pos = Pos(x*k, y*k)
  def unary_- : Pos = this * -1
}
object Pos {
  val zero = Pos()
}

val is = "forward 5\ndown 5\nforward 8\nup 3\ndown 8\nforward 2"
val cmds = is.split('\n').map{s => s.split(' ')}
  .collect{
    case Array(a1,a2) => a1 -> a2.toInt
  }
val res1 = cmds.foldLeft(Pos.zero){
  (acc, cur) => cur._1 match {
    case "forward" => acc + Pos(cur._2)
    case "up" => acc - Pos(0,cur._2)
    case "down" => acc + Pos(0,cur._2)
  }
}
res1.x*res1.y

val res2 = cmds.scanLeft((0,Pos.zero)){
  (acc, cur) => cur._1 match {
    case "forward" => acc._1 -> (acc._2 + Pos(cur._2, acc._1 * cur._2))
    case "up" => acc._1 - cur._2 -> acc._2
    case "down" => acc._1 + cur._2 -> acc._2
    case _ => acc
  }
}
res2

val s = "forward 5"
val a = s.split(' ')
a(0) -> a(1).toInt