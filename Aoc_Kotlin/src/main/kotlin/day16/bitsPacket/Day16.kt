package day16.bitsPacket

import AoC_Lib.*
import java.math.BigInteger

object Day16: SomeDay(2021,16) {
    override val title = "Packet Decoder"

    abstract class Pocket(open val ver: Int, open val id: Int){
        open val sumVer: Int
            get() = ver
        abstract fun eval(): Long
    }
    data class Lit(override val ver: Int, override val id: Int, val value: Long): Pocket(ver, id) {
        override fun eval(): Long = value
    }
    data class Op(override val ver: Int, override val id: Int, val subs: List<Pocket>): Pocket(ver, id) {
        override val sumVer: Int
            get() = ver + subs.sumOf { it.sumVer }
        override fun eval(): Long = when (id) {
            0 -> subs.sumOf{ it.eval() }
            1 -> subs.map{ it.eval() }.reduce(Long::times)
            2 -> subs.minOf{ it.eval() }
            3 -> subs.maxOf{ it.eval() }
            5 -> if (subs.first().eval() > subs.drop(1).first().eval()) 1 else 0
            6 -> if (subs.first().eval() < subs.drop(1).first().eval()) 1 else 0
            7 -> if (subs.first().eval() == subs.drop(1).first().eval()) 1 else 0
            else -> 0
        }
    }

    fun hexToBin(hex: String): String =
        BigInteger(hex,16).toString(2)
            .padStart(hex.length * 4,'0')
    fun binToInt(bin: String): Int = Integer.parseInt(bin,2)

    fun parse(s: String): Pair<Pocket,String> {
        var bs = s
        val v = binToInt(bs.take(3))
        val t = binToInt(bs.slice(3 .. 5))
        bs = bs.substring(6)
        if (t == 4) {
            val valueBits = generateSequence("" to bs){ (acc, s) ->
                if (s.isEmpty()) null
                else {
                    val bits = s.take(5)
                    if (bits.first() == '0')
                        acc + bits.substring(1) to ""
                    else acc + bits.substring(1) to s.substring(5)
                }
            }.last().first
            val value = BigInteger(valueBits,2).toLong()
            return Lit(v, t, value) to bs.substring(valueBits.length * 5 / 4)
        } else {
            if (bs.first() == '0') {
                val l = binToInt(bs.slice(1..15))
                val subsBits = bs.slice(16..15 + l)
                val pks = generateSequence(emptyList<Pocket>() to subsBits) { (acc, s) ->
                    if (s.isEmpty()) null
                    else {
                        val (p, ts) = parse(s)
                        acc + p to ts
                    }
                }.last().first
                return Op(v, t, pks) to bs.substring(1 + 15 + l)
            } else {
                val cnt = binToInt(bs.slice(1..11))
                bs = bs.substring(12)
                val (pks,ts) = generateSequence(emptyList<Pocket>() to bs) { (acc, s) ->
                    if (s.isEmpty()) null
                    else {
                        val (p, ts) = parse(s)
                        acc + p to ts
                    }
                }.drop(cnt).first()
                return Op(v, t, pks) to ts
            }
        }
    }

    override fun first(data: String): Any? {
        val binary = hexToBin(data)
        val packet = parse(binary).first
        return packet.sumVer
    } // 877 Time: 24ms

    override fun second(data: String): Any? {
        val binary = hexToBin(data)
        val packet = parse(binary).first
        return packet.eval()
    } // 194435634456 Time: 4ms
}

fun main() = SomeDay.mainify(Day16)