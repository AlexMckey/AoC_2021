package AoC_Lib

case class Pos3D(x: Int, y: Int, z: Int) {
  import Pos3D.*
  def Distance(that: Pos3D): Int =
    (x - that.x).abs + (y - that.y).abs + (z - that.z).abs
  def +(that: Pos3D): Pos3D = Pos3D(x + that.x, y + that.y, z + that.z)
  def -(that: Pos3D): Pos3D = Pos3D(x - that.x, y - that.y, z - that.z)
  def *(k: Int): Pos3D = Pos3D(x * k, y * k, z * k)
  def *:(k: Int): Pos3D = Pos3D(x * k, y * k, z * k)
  def *(pos3D: Pos3D): Pos3D = Pos3D(x * pos3D.x, y * pos3D.y, z * pos3D.z)
  private def facing: List[Pos3D] = List(
    this,
    Pos3D(x,-y,-z),
    Pos3D(x,-z,y),
    Pos3D(-y,-z,x),
    Pos3D(y,-z,-x),
    Pos3D(-x,-z,-y),
  )
  private def rotating: List[Pos3D] = List(
    this,
    Pos3D(-y,x,z),
    Pos3D(-x,-y,z),
    Pos3D(y,-x,z)
  )
  def allOrientation: List[Pos3D] = facing.flatMap(f => f.rotating)
}
object Pos3D {
  val zero: Pos3D = Pos3D(0, 0, 0)
  extension (s: String)
    def toPos3D: Pos3D = s match {
      case s"$x,$y,$z" => Pos3D(x.toInt,y.toInt,z.toInt)
      case _ => throw IllegalArgumentException("Conversion to Pos3D fail")
    }
}