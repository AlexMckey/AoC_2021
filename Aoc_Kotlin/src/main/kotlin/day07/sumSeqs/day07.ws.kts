import kotlin.math.absoluteValue
import kotlin.math.roundToInt

val s = "16,1,2,0,4,2,7,1,2,14"
val ns = s.split(',').map(String::toInt)
val mediana = ns.sorted()[ns.size/2]
val avg = ns.average().roundToInt()
val fuel1Opt = ns.sumOf { (it - mediana).absoluteValue }
val fuel1 = (ns.minOrNull()!! .. ns.maxOrNull()!!)
    .map { i -> i to ns.sumOf { (i - it).absoluteValue } }
fuel1Opt
fuel1.minOf { it.second }
val fuel2Opt = ns.sumOf { (1 .. (it - avg).absoluteValue).sum() }
val fuel2Opt_ = ns.sumOf { i ->
        val m = (i - avg).absoluteValue
        (m * m + m) / 2
    }
val fuel2 = (ns.minOrNull()!! .. ns.maxOrNull()!!)
    .map{ i -> i to ns.sumOf { (1 .. (it - avg).absoluteValue).sum() }}
fuel2Opt
fuel2Opt_
fuel2.minOf { it.second }