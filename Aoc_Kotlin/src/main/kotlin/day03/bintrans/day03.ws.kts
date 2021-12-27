val s = "00100\n" +
        "11110\n" +
        "10110\n" +
        "10111\n" +
        "10101\n" +
        "01111\n" +
        "00111\n" +
        "11100\n" +
        "10000\n" +
        "11001\n" +
        "00010\n" +
        "01010"
val bins = s.lines().toList()
val bts = bins
    .first()
    .indices
    .map { i -> bins
        .map { it[i] } }
    .map {
        if (it.count { it == '1' } * 2 > bins.size) '1'
        else '0' }
    .joinToString("")
val gamma = Integer.parseInt(bts,2)
val epsilon = (1 shl bins.first().length) - gamma - 1
gamma * epsilon

enum class Rate(val checkBits: (Int, Int) -> Boolean) {
    Oxygen({ b1,b0 -> b1 >= b0 }),
    CO2({ b1, b0 -> b1 < b0 })
}

var r = Rate.Oxygen
val p = bins.partition { it[0] == '1' }

bins.first().indices.fold(bins){
    l, i -> if (l.size == 1) l
        else {
            val bp = l.partition { it[i] == '1' }
            val res = if (r.checkBits(bp.first.size, bp.second.size))
                            bp.first
                        else bp.second
            println(res)
            res
        }
}

r = Rate.CO2

bins.first().indices.fold(bins){
        l, i -> if (l.size == 1) l
else {
    val bp = l.partition { it[i] == '1' }
    val res = if (r.checkBits(bp.first.size, bp.second.size))
        bp.first
    else bp.second
    println(res)
    res
}
}