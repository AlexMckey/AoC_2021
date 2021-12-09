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
val uniqSetInd: Set[Int] = Set(1,4,7,8)
val uniqInds: Map[Int, String] = inds.filter(i => uniqSetInd.contains(i._1))
val iam: Seq[(Char, Int)] = inds.map((n,s) => s.map(_ -> n)).flatten.toSeq
val ium: Seq[(Char, Int)] = uniqInds.map((n,s) => s.map(_ -> n)).flatten.toSeq
val allSegments = iam.groupMap(_._1)(_._2).view.mapValues(_.toSet).toMap
val uniqSegments = ium.groupMap(_._1)(_._2).view.mapValues(_.toSet).toMap
val s = "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
val (si, so) = s.replace(" | ", "|").splitAt(s.indexOf('|'))
val iSignal = si.dropRight(1).split(' ')
val oSignal = so.split(' ')
val sl = iSignal.map(s => s.length -> s.toSet)
  .groupMapReduce(_._1)(_._2)(_ intersect _)
val slOrig = inds.values.map(s => s.length -> s.toSet)
  .groupMapReduce(_._1)(_._2)(_ intersect _)
val a = sl(3) diff sl(2)
val d = sl(5) diff sl(6)
val b = sl(4) diff sl(2) diff d
val f = sl(6) diff sl(5) diff b
val g = sl(6) intersect sl(5) diff a
val c = sl(2) diff f
val e = sl(7) diff sl(4) diff sl(3) diff g
val decs = List(a,b,c,d,e,f,g).map(_.head).zip("abcdefg").toMap
val decode = inds.map((i,s) => s.toSet -> i)
val res = oSignal.map(s => s.map(decs(_)).toSet)
  .map(decode(_)).mkString("").toInt
val indicators: Map[Set[Char],Int]  = List("abcefg","cf","acdeg","acdfg","bcdf","abdfg","abdefg","acf","abcdefg","abcdfg")
  .map(_.toSet).zipWithIndex.toMap
val uniqIds = indicators.map(_._1.size)
  .groupMapReduce(identity)(_ => 1)(_+_)
  .filterNot(_._2 > 1).map(_._1)
