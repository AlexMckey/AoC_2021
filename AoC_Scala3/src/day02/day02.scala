package day02

import AoC_Lib.*

object day02 {
  val cmds: Seq[Pos] = inputStrs("input02.txt")
    .map(_.split(' '))
    .collect{ case Array(a1,a2) => a1 -> a2.toInt match {
      case ("forward",i) => Pos(i,0)
      case ("up",i) => Pos(0,-i)
      case ("down",i) => Pos(0,i)
      case _ => Pos.zero
    }}.toList

  @main
  def main(): Unit = {
    val res1 = cmds.foldLeft(Pos.zero)(_+_)
    Console.println(res1.x * res1.y) // 2215080

    val res2 = cmds.foldLeft((0,Pos.zero)){
      case ((aim, pos), cur) => cur.x match {
        case 0 => aim + cur.y -> pos
        case _ => aim -> (pos + Pos(cur.x, cur.x * aim))
      }
    }
    Console.println(res2._2.x * res2._2.y) // 1864715580
  }
}
