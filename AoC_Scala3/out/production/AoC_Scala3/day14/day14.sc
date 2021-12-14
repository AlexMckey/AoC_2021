val s = "NNCB\n\nCH -> B\nHH -> N\nCB -> H\nNH -> C\nHB -> C\nHC -> B\nHN -> C\nNN -> C\nBH -> H\nNC -> B\nNB -> B\nBN -> B\nBB -> N\nBC -> B\nCC -> N\nCN -> C"
val Array(init, rs) = s.split("\n\n")
val rules = rs.split('\n')
  .map{ case s"$pattern -> $insert" => pattern -> s"$insert${pattern.tail}"}
  .toMap
  .withDefault(s => s.tail)
val r0 = init
val r1 = r0.head + r0.sliding(2).map(rules).mkString
val r2 = r1.head + r1.sliding(2).map(rules).mkString
def iter(init: String, cnt: Int): String =
  Iterator.iterate(init){ s =>
    val sb = new StringBuilder()
    sb.append(s.head)
    s.sliding(2).foreach(p => sb.append(rules(p)))
    sb.toString()}
  .drop(cnt).next()
val res = iter(init,10).groupMapReduce(identity)(_ => 1)(_ + _)
res.maxBy(_._2)._2 - res.minBy(_._2)._2

val rm0 = init.sliding(2).toList
  .groupMapReduce(identity)(_ => 1L)(_ + _)
val rule = rs.split('\n')
  .map{ case s"$pattern -> $insert" =>
    pattern -> List(s"${pattern.head}$insert",s"$insert${pattern.tail}")}
  .toMap
val r1 = rm0.toList.flatMap{ (pattern,cnt) =>
  rule(pattern).map(_ -> cnt)
}.groupMapReduce(_._1)(_._2)(_ + _)
r1.toList.flatMap{ (s: String, i: Long) =>
  List(s.head -> i, s.last -> i) }
  .groupMapReduce(_._1)(_._2)(_ + _)
  .map(p => p._1 -> p._2 / 2)
  .map(p => if (init.head :: init.last :: Nil)
    .contains(p._1) then p._1 -> (p._2 + 1) else p._1 -> p._2)
val r2 = r1.toList.flatMap{ (pattern,cnt) =>
  rule(pattern).map(_ -> cnt)
}.groupMapReduce(_._1)(_._2)(_ + _)
r2.toList.flatMap{ (s,i) => List(s.head -> i, s.last -> i) }
  .groupMapReduce(_._1)(_._2)(_ + _)
  .map(p => p._1 -> p._2 / 2)
  .map(p => if (init.head :: init.last :: Nil)
    .contains(p._1) then p._1 -> (p._2 + 1) else p._1 -> p._2)
val r3 = r2.toList.flatMap{ (pattern,cnt) =>
  rule(pattern).map(_ -> cnt)
}.groupMapReduce(_._1)(_._2)(_ + _)
r3.toList.flatMap{ (s,i) => List(s.head -> i, s.last -> i) }
  .groupMapReduce(_._1)(_._2)(_ + _)
  .map(p => p._1 -> p._2 / 2)
  .map(p => if (init.head :: init.last :: Nil)
    .contains(p._1) then p._1 -> (p._2 + 1) else p._1 -> p._2)

def iterMap(init: Map[String,Long], cnt: Int): Map[String,Long] =
  Iterator.iterate(init){ m =>
      m.toList.flatMap{ (pattern,cnt) =>
        rule(pattern).map(_ -> cnt)
      }.groupMapReduce(_._1)(_._2)(_ + _)
    }
    .drop(cnt).next()
iterMap(rm0,1)
iterMap(rm0,2)
iterMap(rm0,10).toList.flatMap{ (s,i) => List(s.head -> i, s.last -> i) }
  .groupMapReduce(_._1)(_._2)(_ + _)
  .map(p => p._1 -> p._2 / 2)
  .map(p => if (init.head :: init.last :: Nil)
    .contains(p._1) then p._1 -> (p._2 + 1) else p._1 -> p._2)
def calcCnt(init: String, cnt: Int): Map[Char,Long] = {
  val start = init.sliding(2).toList
    .groupMapReduce(identity)(_ => 1L)(_ + _)
  val polyMap = iterMap(start,cnt)
  val cntMap = polyMap.toList.flatMap{ (s,i) => List(s.head -> i, s.last -> i) }
    .groupMapReduce(_._1)(_._2)(_ + _)
    .map(p => p._1 -> p._2 / 2)
    .map(p => if (init.head :: init.last :: Nil)
      .contains(p._1) then p._1 -> (p._2 + 1) else p._1 -> p._2)
  cntMap
}
calcCnt(init,10)
rule.view.mapValues(_.last).toMap
val pm = """(\w)(\w) -> (\w)""".r
rs.split('\n')
  .map{ case pm(first,second,insert) =>
    s"$first$second" -> (s"$first$insert",s"$insert$second")}
  .toMap