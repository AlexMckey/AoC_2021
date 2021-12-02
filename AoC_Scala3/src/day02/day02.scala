package day02

import AoC_Lib.*

object day02 {
  val cmds: Seq[(String, Int)] = inputStrs("input02.txt")
    .map(_.split(' '))
    .map{ a => a(0) -> a(1).toInt}
    .toList

  @main
  def main(): Unit = {
    val res1 = cmds.foldRight(Pos.zero){
      (cur, acc) => cur._1 match {
        case "forward" => acc + Pos(cur._2,0)
        case "up" => acc - Pos(0,cur._2)
        case "down" => acc + Pos(0,cur._2)
        case _ => acc
      }
    }
    Console.println(res1.x * res1.y) // 2215080

    val res2 = cmds.foldLeft((0,Pos.zero)){
      (acc, cur) => cur._1 match {
        case "forward" => acc._1 -> (acc._2 + Pos(cur._2, acc._1 * cur._2))
        case "up" => acc._1 - cur._2 -> acc._2
        case "down" => acc._1 + cur._2 -> acc._2
        case _ => acc
      }
    }
    Console.println(res2._2.x * res2._2.y) // 1864715580
  }
}
