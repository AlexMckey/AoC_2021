val s = "NNCB\n" +
        "\n" +
        "CH -> B\n" +
        "HH -> N\n" +
        "CB -> H\n" +
        "NH -> C\n" +
        "HB -> C\n" +
        "HC -> B\n" +
        "HN -> C\n" +
        "NN -> C\n" +
        "BH -> H\n" +
        "NC -> B\n" +
        "NB -> B\n" +
        "BN -> B\n" +
        "BB -> N\n" +
        "BC -> B\n" +
        "CC -> N\n" +
        "CN -> C"

val (initPoly, rs) = s.split("\n\n")
val rules: Map<Pair<Char,Char>,List<Pair<Char,Char>>> = rs.lines()
    .map { it
        .split(" -> ")
        .let { (it.first().first() to it.first().last()) to
                listOf(it.first().first() to it.last().first(), it.last().first() to it.first().last()) }}
    .toMap()
rules
val init = initPoly.zipWithNext().groupingBy { it }.eachCount()
init
val r1 = init.flatMap { kv -> rules[kv.key]!!.map { it to kv.value } }
    .groupBy({it.first},{it.second}).mapValues { it.value.sum() }
r1
val r2 = r1.flatMap { kv -> rules[kv.key]!!.map { it to kv.value } }
    .groupBy({it.first},{it.second}).mapValues { it.value.sum() }
r2
val res = r2.map { it.key.second to it.value }
    .groupBy({it.first},{it.second}).mapValues { it.value.sum() }
res + (initPoly[0] to res[initPoly[0]]!!+1)
val o = generateSequence(init) { map ->
    map.flatMap { kv -> rules[kv.key]!!.map { it to kv.value } }
        .groupBy({it.first},{it.second}).mapValues { it.value.sum() }
}.drop(10).first().map { it.key.second to it.value }
    .groupBy({it.first},{it.second}).mapValues { it.value.sum() }
    .let { it + (initPoly[0] to it[initPoly[0]]!!+1) }
    .let { it.maxOf { it.value } - it.minOf { it.value } }
o
fun makePoly(polyPattern: String, cnt: Int): Long =
    generateSequence(polyPattern
        .zipWithNext()
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong()})
    { map -> map
            .flatMap { kv -> rules[kv.key]!!
                .map { it to kv.value } }
        .groupBy({it.first},{it.second})
        .mapValues { it.value.sum() }
    }.drop(10)
        .first()
        .map { it.key.second to it.value }
        .groupBy({it.first},{it.second})
        .mapValues { it.value.sum() }
        .let { it + (polyPattern[0] to it[polyPattern[0]]!! + 1) }
        .let { it.maxOf { it.value } - it.minOf { it.value } }
makePoly(initPoly,10)