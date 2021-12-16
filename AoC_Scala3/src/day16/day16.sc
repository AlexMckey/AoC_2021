def hexCharToBin(c: Char): String = {
  val bs = Integer.parseInt(c.toString,16).toBinaryString
  "0".repeat(4 - bs.length) + bs
}
def hexToBin(s: String): String = s.map(hexCharToBin).mkString
var s = "D2FE28"
var bs = hexToBin(s)
var (v,ts1) = bs.splitAt(3)
var (t,ts2) = ts1.splitAt(3)
val ns = Integer.parseInt(Iterator.unfold((ts2,false)){ (s,b) =>
  if b then None
  else
    val (n,ts) = s.splitAt(5)
    if n.head == '0' then Some(n.tail -> (ts,true))
    else Some(n.tail -> (ts,false))
}.toList.mkString,2)
var (bn1,ts3) = ts2.splitAt(5)
var (b1,n1) = bn1.splitAt(1)
var (bn2,ts4) = ts3.splitAt(5)
var (b2,n2) = bn2.splitAt(1)
var (bn3,ts5) = ts4.splitAt(5)
var (b3,n3) = bn3.splitAt(1)
var n = Integer.parseInt(n1+n2+n3,2)
s = "38006F45291200"
bs = hexToBin(s)
var (v,ts1) = bs.splitAt(3)
var (t,ts2) = ts1.splitAt(3)
var (l,ts3) = ts2.splitAt(1)
var (n1,ts4) = ts3.splitAt(15)
trait Packet(v: Int, t: Int){
  def eval: Long
}
trait Oper(t: Int, sp: List[Packet]) {
  def eval: Long = t match {
    case 0 => sp.map(_.eval).sum
    case 1 => sp.map(_.eval).product
    case 2 => sp.map(_.eval).min
    case 3 => sp.map(_.eval).max
    case 5 => if sp.head.eval > sp.tail.head.eval then 1 else 0
    case 6 => if sp.head.eval < sp.tail.head.eval then 1 else 0
    case 7 => if sp.head.eval == sp.tail.head.eval then 1 else 0
  }
}
case class LiteralValue(v: Int, t: Int, n: Long) extends Packet(v,t) {
  override def eval: Long = n
}
case class OperatorWithLength(v: Int, t: Int, l: Int, sp: List[Packet]) extends Packet(v,t) with Oper(t,sp)
case class OperatorWithCount(v: Int, t: Int, cnt: Int, sp: List[Packet]) extends Packet(v,t) with Oper(t,sp)
object Packet {
  def parse(s: String): (Packet,String) = {
    val (v,bs1) = s.splitAt(3)
    val (t,bs2) = bs1.splitAt(3)
    val ver = Integer.parseInt(v,2)
    val typ = Integer.parseInt(t,2)
    if typ == 4 then
      val ns = Iterator.unfold((bs2,false)){ (s,b) =>
        if b then None
        else
          val (n,ts) = s.splitAt(5)
          if n.head == '0' then Some(n.tail -> (ts,true))
          else Some(n.tail -> (ts,false))
      }.toList
      val (_, bs4) = bs2.splitAt(ns.size * 5)
      val n = BigInt(ns.mkString,2).toLong
      LiteralValue(ver,typ,n) -> bs4
    else
      val (lt,bs5) = bs2.splitAt(1)
      if lt == "0" then
        val (ls,bs6) = bs5.splitAt(15)
        val len = Integer.parseInt(ls,2)
        val (ps,bs7) = bs6.splitAt(len)
        val pks = Iterator.unfold(ps){ s =>
          if s.isEmpty then None
          else Some(Packet.parse(s))
        }.toList
        OperatorWithLength(ver,typ,len,pks) -> bs7
      else
        val (cs,bs8) = bs5.splitAt(11)
        val cnt = Integer.parseInt(cs,2)
        val ps = Iterator.unfold(bs8){ s =>
          if s.isEmpty then None
          else
            val (p,ts) = Packet.parse(s)
            Some((p, s.length-ts.length) -> ts)
        }.take(cnt).toList
        val (pks,ls) = ps.unzip
        val (_,bs9) = bs8.splitAt(ls.sum)
        OperatorWithCount(ver,typ,cnt,pks) -> bs9
  }
}
def verSum(p: Packet): Int = p match {
    case LiteralValue(v, _, _) => v
    case OperatorWithLength(v, _, _, ps) => v + ps.map(verSum).sum
    case OperatorWithCount(v, _, _, ps) => v + ps.map(verSum).sum
}
Packet.parse(hexToBin("D2FE28"))
Packet.parse("11010001010")
Packet.parse("0101001000100100")
val ps = "110100010100101001000100100"
Packet.parse(ps)
val pks = Iterator.unfold(ps){ s =>
  if s.isEmpty then None
  else
    var (p,ts) = Packet.parse(s)
    Some(p -> ts)
}.toList
Packet.parse(hexToBin("38006F45291200"))
Packet.parse(hexToBin("EE00D40C823060"))
Packet.parse(hexToBin("8A004A801A8002F478"))
verSum(Packet.parse(hexToBin("8A004A801A8002F478"))._1)
verSum(Packet.parse(hexToBin("620080001611562C8802118E34"))._1)
verSum(Packet.parse(hexToBin("C0015000016115A2E0802F182340"))._1)
verSum(Packet.parse(hexToBin("A0016C880162017C3686B18A3D4780"))._1)
Integer.parseInt("101101011011100101111111101001101011",2)
BigInt("101101011011100101111111101001101011",2).toLong

Packet.parse(hexToBin("C200B40A82"))._1.eval
Packet.parse(hexToBin("04005AC33890"))._1.eval
Packet.parse(hexToBin("880086C3E88112"))._1.eval
Packet.parse(hexToBin("CE00C43D881120"))._1.eval
Packet.parse(hexToBin("D8005AC2A8F0"))._1.eval
Packet.parse(hexToBin("F600BC2D8F"))._1.eval
Packet.parse(hexToBin("9C005AC2F8F0"))._1.eval
Packet.parse(hexToBin("9C0141080250320F1802104A08"))._1.eval