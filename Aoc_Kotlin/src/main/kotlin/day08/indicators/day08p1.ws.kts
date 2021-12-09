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
val uniqIndLens = listOf(2,3,4,7)
val os = s.replace("|\n", "| " )
    .split('\n')
    .map { it.split(" | ").last() }
    .joinToString(" ")
os.split(' ').count{ uniqIndLens.contains(it.length) }
