import AoC_Lib.toInts

val s = "199\n" +
        "200\n" +
        "208\n" +
        "210\n" +
        "200\n" +
        "207\n" +
        "240\n" +
        "269\n" +
        "260\n" +
        "263"
val i = s.lines().map(String::toInt)
i.zipWithNext(Int::compareTo).count{ it < 0}
i.windowed(3,1).map { it.sum() }
    .zipWithNext(Int::compareTo).count{ it < 0}
i.zip(i.drop(3))
    .count{ it.first < it.second }