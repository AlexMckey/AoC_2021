package day22

import AoC_Lib.*

import scala.annotation.tailrec

object day22 {
  val r = """(on|off) x=(-?\d+)..(-?\d+),y=(-?\d+)..(-?\d+),z=(-?\d+)..(-?\d+)""".r

  case class ReactorArea(state: String, min: Pos3D, max: Pos3D){
    def intersect(that: ReactorArea): Option[ReactorArea] = {
      val intersectMin = min max that.min
      val intersectMax = max min that.max
      val newState = state match
        case "on" => "off"
        case "off" => "on"
      if intersectMin <= intersectMax
        then Some(ReactorArea(newState , intersectMin, intersectMax))
        else None
    }
    def contains(that: ReactorArea): Boolean = (this intersect that).isEmpty
    def size: Long = (if state == "on" then 1L else -1L) *
      (max.x - min.x + 1) * (max.y - min.y + 1) * (max.z - min.z + 1)
  }

  def parseReactors(ss: Seq[String], limit: Int = Int.MaxValue): Seq[ReactorArea] = ss.map {
    case r(state, x1, x2, y1, y2, z1, z2) =>
      ReactorArea(state, Pos3D(x1.toInt, y1.toInt, z1.toInt),
                         Pos3D(x2.toInt, y2.toInt, z2.toInt))
  }.filterNot(_ contains ReactorArea("off", Pos3D(-limit, -limit, -limit),
                                            Pos3D(limit, limit, limit)))

//  def operateReactors(reactors: Seq[ReactorArea]): Long = {
//    val all = scala.collection.mutable.Set.empty[(Int,Int,Int)]
//    reactors.foreach( r =>
//      (r.min.x to r.max.x).foreach( x =>
//        (r.min.y to r.max.y).foreach( y =>
//          (r.min.z to r.max.z).foreach( z =>
//            r.state match {
//              case "on" => all.add((x,y,z))
//              case "off" => all.remove((x,y,z))
//            }))))
//    all.toSet.size
//  }

  def operateReactorsOpt(reactors: Seq[ReactorArea]): Long = {
    @tailrec
    def rec(reactors: Seq[ReactorArea], sectors: Seq[ReactorArea]): Seq[ReactorArea] =
      if reactors.isEmpty then sectors
      else
        val reactor = reactors.head
        val newSectors = (if reactor.state == "on" then sectors :+ reactor else sectors) ++
          sectors.flatMap(_ intersect reactor)
        rec(reactors.tail, newSectors)
    rec(reactors, Seq.empty).map(_.size).sum
  }


  def part1_opt: Long = operateReactorsOpt(parseReactors(inputStrs(day = 22), limit = 50))
  def part2: Long = operateReactorsOpt(parseReactors(inputStrs(day = 22)))

  @main
  def ReactorReboot(): Unit = {
    Console.println(part1_opt) // 556501
    Console.println(part2)     // 1217140271559773
  }
}
