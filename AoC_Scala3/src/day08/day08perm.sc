import AoC_Lib.*

val s = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"

val indicator: IndexedSeq[Set[Char]] = IndexedSeq("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
  .map(_.toSet)
val signals: Seq[(Seq[Set[Char]], Seq[Set[Char]])] = List(s)
  .map{
    case s"$is | $os" =>
      def parse(s: String): Seq[Set[Char]] = s.words.map(_.toSet)
      (parse(is), parse(os))
  }

val digits = indicator.toSet
val chars = digits.flatten.toSeq
val assignments = chars.permutations.map(_.zip(chars).toMap).toSeq

signals.map {
  case (l, r) =>
    val assignment = assignments.find(candidate => l.view.map(_.map(candidate)).forall(digits.contains)).get
    r.map(_.map(assignment)).map(indicator.indexOf).mkString.toInt
}.sum