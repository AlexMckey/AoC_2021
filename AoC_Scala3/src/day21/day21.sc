val s = "Player 1 starting position: 4\nPlayer 2 starting position: 8"
val starts = s.split('\n').map(s => s.substring(s.lastIndexOf(' ')+1).toInt)
def newPos(curPos :Int, move: Int): Int = (curPos + move - 1) % 10 + 1
newPos(8,15)
newPos(4,6)
993 / 3
1 -> (1 * 3 - 1) * 3 -> 6
2 -> (2 * 3 - 1) * 3 -> 15
3 -> (3 * 3 - 1) * 3 -> 24
4 -> (4 * 3 - 1) * 3 -> 33
def calcSpace(i: Int): Int = (i * 3 - 1) * 3
calcSpace(1)
calcSpace(2)
calcSpace(3)
Iterator.iterate((4, 8, 0, 0, 0)){
  (p1p,p2p,p1s,p2s,m) =>
    val space = calcSpace(m+1)
    if m % 2 == 0
    then
      val newP1Pos = newPos(p1p,space)
      (newP1Pos, p2p, p1s + newP1Pos, p2s, m + 1)
    else
      val newP2Pos = newPos(p2p,space)
      (p1p, newP2Pos, p1s, p2s + newP2Pos, m + 1)}
  .take(10).toList
def calcRes(p1StartPos: Int, p2StartPos: Int) = {
  val (p1p: Int, p2p: Int, p1s: Int, p2s: Int, move: Int) =
    Iterator.iterate((p1StartPos: Int, p2StartPos: Int, 0, 0, 0)){
      (p1p,p2p,p1s,p2s,m) =>
        val space = calcSpace(m + 1)
        if m % 2 == 0
        then
          val newP1Pos = newPos(p1p,space)
          (newP1Pos, p2p, p1s + newP1Pos, p2s, m + 1)
        else
          val newP2Pos = newPos(p2p,space)
          (p1p, newP2Pos, p1s, p2s + newP2Pos, m + 1)}
    .dropWhile((_,_,p1s,p2s,_) =>  p1s < 1000 && p2s < 1000)
    .next()
  if p1s >= 1000
  then p2s * move * 3
  else p1s * move * 3
}
calcRes(4,8)

val QuantumDie = (1 to 3)
  .flatMap(a => (1 to 3)
    .flatMap(b => (1 to 3)
      .map(c => a + b + c)))
  .groupMapReduce(identity)(_ => 1)(_ + _)
  .toSeq
QuantumDie

val cache = scala.collection.mutable.Map.empty[(Int,Int,Long,Long), (Long, Long)]
def rec(p1p: Int, p2p: Int, p1s: Long, p2s: Long): (Long,Long) =
  cache.getOrElseUpdate((p1p, p2p, p1s, p2s), {
    QuantumDie.map{ (space, count) =>
      val pos = newPos(p1p,space)
      val score = p1s + pos
      val (p2Cnt, p1Cnt) = if score >= 21
      then (0L, 1L)
      else rec(p2p, pos, p2s, score)
      (p1Cnt * count, p2Cnt * count)}
      .reduce{ case ((a, b), (c, d)) => (a + c, b + d) }
  })

rec(4,8,0,0)