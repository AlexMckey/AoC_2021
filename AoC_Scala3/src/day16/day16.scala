package day16

import AoC_Lib.*

object day16 {
  def hexToBin(s: String): String = s.map { c =>
    val bs = Integer.parseInt(c.toString,16).toBinaryString
    "0".repeat(4 - bs.length) + bs
  }.mkString
  def binToInt(s: String): Int = Integer.parseInt(s,2)

  trait Packet(ver: Int, ID: Int){
    def eval: Long
    def verSum: Int = ver
  }
  case class LiteralValue(ver: Int, ID: Int, n: Long) extends Packet(ver,ID) {
    override def eval: Long = n
  }
  case class Operator(ver: Int, ID: Int, pks: List[Packet]) extends Packet(ver,ID) {
    override def eval: Long = ID match {
      case 0 => pks.map(_.eval).sum
      case 1 => pks.map(_.eval).product
      case 2 => pks.map(_.eval).min
      case 3 => pks.map(_.eval).max
      case 5 => if pks.head.eval > pks.tail.head.eval then 1 else 0
      case 6 => if pks.head.eval < pks.tail.head.eval then 1 else 0
      case 7 => if pks.head.eval == pks.tail.head.eval then 1 else 0
    }
    override def verSum: Int = ver + pks.map(_.verSum).sum
  }

  def unfoldPacket[T](start: String)(f: String => Option[(T,String)]): Iterator[T] =
    Iterator.unfold(start) { s =>
      if s.isEmpty then None
      else f(s)
    }
  object Packet {
    def parse(s: String): (Packet,String) = {
      val (vt,bs) = s.splitAt(6)
      val (v,t) = vt.splitAt(3)
      val ver = binToInt(v)
      val id = binToInt(t)
      if id == 4 then
        val ns = unfoldPacket(bs){ s =>
            val (n,ts) = s.splitAt(5)
            if n.head == '0'
            then Some(n.tail -> "")
            else Some(n.tail -> ts)
          }.toList
        val n = BigInt(ns.mkString,2).toLong
        LiteralValue(ver,id,n) -> bs.splitAt(ns.size * 5)._2
      else
        val (lt,bs4) = bs.splitAt(1)
        if lt == "0" then
          val (ls,bs5) = bs4.splitAt(15)
          val len = binToInt(ls)
          val (ps,bs6) = bs5.splitAt(len)
          val pks = unfoldPacket(ps){ s =>
              Some(Packet.parse(s))
            }.toList
          Operator(ver,id,pks) -> bs6
        else
          val (cs,bs7) = bs4.splitAt(11)
          val cnt = binToInt(cs)
          val ps = unfoldPacket(bs7){ s =>
            val (p,ts) = Packet.parse(s)
            Some((p, s.length-ts.length) -> ts)
          }.take(cnt).toList
          val (pks,ls) = ps.unzip
          Operator(ver,id,pks) -> bs7.splitAt(ls.sum)._2
    }
  }

  val packet: (Packet, String) = Packet.parse(hexToBin(inputStr(day = 16)))

  def part1: Int = packet._1.verSum

  def part2: Long = packet._1.eval

  @main
  def PacketDecoder(): Unit = {
    Console.println(part1)     // 877
    Console.println(part2)     // 194435634456
  }
}
