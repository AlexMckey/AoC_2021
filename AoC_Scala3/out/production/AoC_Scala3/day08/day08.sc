case class SSInd(val input: Seq[String], val output: Seq[String])
val inds = Map(0 -> "abcefg",
  1 -> "cf",
  2 -> "acdeg",
  3 -> "acdfg",
  4 -> "bcdf",
  5 -> "abdfg",
  6 -> "abdefg",
  7 -> "acf",
  8 -> "abcdefg",
  9 -> "abcdfg")
val uniqSetInd = Set(1,4,7,8)
val uniqInds = inds.filter(i => uniqSetInd.contains(i._1))
val iam = inds.map((n,s) => s.map(_ -> n)).flatten.toSeq
val ium = uniqInds.map((n,s) => s.map(_ -> n)).flatten.toSeq
val allSegments = iam.groupMap(_._1)(_._2)
val uniqSegments = ium.groupMap(_._1)(_._2)
val s = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab |\n" +
        "cdfeb fcadb cdfeb cdbaf"
val ss = s.replace("|\n", "").split(' ')
ss.filter(is => uniqInds.map(_._2.length).toSet.contains(is.length))
val s1 = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb |\n" +
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
val indicators = s1.replace(" |\n","|")
  .split('\n')
  .map(s => s.split('|').map(_.split(' ')))
  .map{ case Array(is, os) => SSInd(is, os)}
uniqInds.values.map(_.length).toSet
indicators.map(_.output.count(si => uniqInds.values.map(_.length).toSet.contains(si.length)))
  .sum
val i0 = indicators(0)
i0.input.filter(si => uniqInds.values.map(_.length).toSet.contains(si.length))
