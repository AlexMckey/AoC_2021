import java.math.BigInteger

var s = "D2FE28"
fun hexToBin(hex: String): String =
    hex.map { "%4s".format(Integer.toBinaryString(
        Integer.parseInt(it.toString(),16))) }
        .joinToString("")
        .replace(' ','0')
hexToBin(s)
BigInteger(s,16).toString(2).padStart(s.length*4,'0')
fun hexToBinOpt(hex: String): String =
    BigInteger(hex,16).toString(2)
        .padStart(hex.length*4,'0')
fun binToInt(bin: String): Int = Integer.parseInt(bin,2)
var bs = hexToBinOpt(s)
val v = bs.take(3)
val ver = binToInt(v)
ver
bs
bs = bs.substring(3)
val t = bs.take(3)
val id = binToInt(t)
id
bs = bs.substring(3)
bs
fun intBits(s: String): String = generateSequence("" to s){ (acc: String, s: String) ->
    if (s.isEmpty()) null
    else {
        val bits = s.take(5)
        if (bits.first() == '0')
            acc + bits.substring(1) to ""
        else acc + bits.substring(1) to s.substring(5)
    }
}.last().first
BigInteger(intBits(bs),2).toLong()
s = "38006F45291200"
bs = hexToBin(s)
bs = bs.substring(6)
bs.first()
val l = binToInt(bs.slice(1..15))
l
val subs = bs.slice(16..15+l)
subs
bs = bs.substring(1+15+l)
bs
s = "EE00D40C823060"
bs = hexToBin(s)
bs = bs.substring(6)
bs.first()
val cnt = binToInt(bs.slice(1..11))
cnt
bs = bs.substring(12)
bs