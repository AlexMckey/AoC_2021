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
  def <=(that: Pos3D): Boolean = x <= that.x && y <= that.y && z <= that.z
  def min(that: Pos3D): Pos3D = Pos3D(x min that.x, y min that.y, z min that.z)
  def max(that: Pos3D): Pos3D = Pos3D(x max that.x, y max that.y, z max that.z)

}
object Pos3D {
  val zero: Pos3D = Pos3D(0, 0, 0)
  extension (s: String)
    def toPos3D: Pos3D = s match {
      case s"$x,$y,$z" => Pos3D(x.toInt,y.toInt,z.toInt)
      case _ => throw IllegalArgumentException("Conversion to Pos3D fail")
    }
}