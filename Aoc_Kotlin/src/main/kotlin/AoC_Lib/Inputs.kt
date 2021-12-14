package AoC_Lib

fun String.toInts(): List<Int> = this.lines().map(String::toInt)
fun String.toLongs(): List<Long> = this.lines().map(String::toLong)
fun String.toStrs(): List<String> = this.lines()
fun String.replaceAll(pairs: List<Pair<Char,Char>>) = pairs
    .fold(this){ acc, (from, to) -> acc.replace(from,to) }
fun String.words(): List<String> = this.split(' ')