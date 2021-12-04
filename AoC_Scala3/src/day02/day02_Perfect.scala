package day02

import AoC_Lib.inputStrs

object day02_Perfect {
  val cmds: Seq[(String, Int)] = inputStrs("input02.txt")
    .map(_.split(' '))
    .collect{ case Array(cmd, num) => cmd -> num.toInt}

  @main
  def DivePerfect(): Unit = {
    val (aim, len, dpth) = cmds.foldLeft((0,0,0)){
      case ((aim, len, dpth), (cmd, num)) =>
        cmd match {
          case "forward" => (aim, len + num, dpth + num * aim)
          case "up" => (aim - num, len, dpth)
          case "down" => (aim + num, len, dpth)
        }
    }
    Console.println(len * aim) // 2215080
    Console.println(len * dpth) // 1864715580
  }
}
