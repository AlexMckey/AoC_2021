val Regs = "xyzw"
trait Cmd(cmd: String) {
  def exec(op1: Int, op2: Int): Int
}
case class OneOpCmd(cmd: String, reg: String) extends Cmd(cmd) {
  override def exec(op1: Int, op2: Int): Int = op1
}
case class BinOpCmd(cmd: String, reg: String, par: String) extends Cmd(cmd) {
  override def exec(op1: Int, op2: Int): Int = cmd match {
    case "add" => op1 + op2
    case "mul" => op1 * op2
    case "div" => op1 / op2
    case "mod" => op1 % op2
    case "eql" => if op1 == op2 then 1 else 0
    case _ => 0
  }
}
class ALU {
  import scala.collection.mutable
  val regs: mutable.Map[String, Int] = mutable.Map.empty
  var input: Seq[Int] = Seq.empty
  var program: Seq[Cmd] = Seq.empty
  private def step(cmd: Cmd): Unit = cmd match {
    case OneOpCmd(_, r) => //only Inp
      regs(r) = input.head
      input = input.tail
    case BinOpCmd(_, r, p) =>
      val op1 = regs(r)
      val op2 = if Regs.contains(p)
      then regs(p)
      else p.toInt
      regs(r) = cmd.exec(op1,op2)
  }
  def run(): Unit = program.foreach(step)
}
object ALU {
  private val r = "(inp|add|mul|div|mod|eql) ([xyzw])(?: (-?\\d+|[xyzw]))?".r
  def fromProgram(s: String): ALU = {
    val p = s.split('\n')
      .map {
        case r(cs, rs, ps) =>
          if cs == "inp" then OneOpCmd(cs, rs)
          else BinOpCmd(cs, rs, ps)
      }
    val res = ALU()
    res.program = p
    res
  }
}
val r = "(inp|add|mul|div|mod|eql) ([xyzw])(?: (-?\\d+|[xyzw]))?".r
val s1 = "inp x\nmul x -1"
val p1 = s1.split('\n')
  .map {
    case r(cs, rs, ps) =>
      if cs == "inp" then OneOpCmd(cs, rs)
      else BinOpCmd(cs, rs, ps)
  }
p1

val a1 = ALU.fromProgram(s1)
a1.input = List(-5)
a1.regs
a1.run()
a1.regs("x")
Regs.contains("x")