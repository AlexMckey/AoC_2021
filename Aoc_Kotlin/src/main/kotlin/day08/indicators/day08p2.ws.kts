val s = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |\n" +
        "fdgacbe cefdb cefbgd gcbe\n" +
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec |\n" +
        "fcgedb cgb dgebacf gc\n" +
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef |\n" +
        "cg cg fdcagb cbg\n" +
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega |\n" +
        "efabcd cedba gadfec cb\n" +
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga |\n" +
        "gecf egdcabf bgf bfgea\n" +
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf |\n" +
        "gebdcfa ecba ca fadegcb\n" +
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf |\n" +
        "cefg dcbef fcge gbcadfe\n" +
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd |\n" +
        "ed bcgafe cdgba cbgef\n" +
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg |\n" +
        "gbdfcae bgc cg cgb\n" +
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc |\n" +
        "fgae cfgab fg bagce"
val signals = s.replace("|\n", "| " )
    .split('\n')
    .map { it
        .split(" | ")
        .let { it.first() to it.last() } }
signals
val indicators = listOf("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
    .mapIndexed{ index, s -> s to index }.toMap()
indicators
val is0 = signals[0].first
val sl = is0.split(' ')
    .map { it.length to it.toSet() }
    .groupBy({it.first} , {it.second})
    .mapValues { it.value.reduce { acc, set ->  acc.intersect(set) } }
sl
val a = sl[3]!! - sl[2]!!
val d = sl[5]!! - sl[6]!!
val b = sl[4]!! - sl[2]!! - d
val f = sl[6]!! - sl[5]!! - b
val g = sl[6]!! intersect sl[5]!! - a
val c = sl[2]!! - f
val e = sl[7]!! - sl[4]!! - a - g
val decs = listOf(a,b,c,d,e,f,g)
    .map{ it.single() }
    .zip("abcdefg".toList())
    .toMap()
val os0 = signals[0].second
os0.split(' ')
    .map { it
        .map{ decs[it]!! }
        .sorted()
        .joinToString("") }
    .map { indicators[it] }
    .joinToString("")
    .toInt()