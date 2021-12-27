package day21

import AoC_Lib.*

object day21 {

  val p1start :: p2start :: _ : Seq[Int] = inputStrs(day = 21)
    .map(s => s.substring(s.lastIndexOf(' ')+1).toInt)

  private def calcDiceSum(n: Int): Int = (n * 3 - 1) * 3
  private def calcPos(curPos :Int, space: Int): Int = (curPos + space - 1) % 10 + 1

  def playDeterministicDice(p1StartPos: Int, p2StartPos: Int, limit: Int): Long = {
    val (_,_,pScore,_,move) =
      Iterator.iterate((p1StartPos, p2StartPos, 0L, 0L, 0)){
        (p1p, p2p, p1s, p2s, m) =>
          val space = calcDiceSum(m + 1)
          val newPos = calcPos(p1p, space)
          (p2p, newPos, p2s, p1s + newPos, m + 1)}
        .dropWhile((_,_,_,p2s,_) =>  p2s < limit)
        .next()
    pScore * move * 3 // 3 rolls in each move
  }

  def playQuantumDice(p1StartPos: Int, p2StartPos: Int, limit: Int): Long = {
    val cache = scala.collection.mutable.Map.empty[(Int,Int,Long,Long), (Long, Long)]
    val quantumDie = (1 to 3)
      .flatMap(a => (1 to 3)
        .flatMap(b => (1 to 3)
          .map(c => a + b + c)))
      .groupMapReduce(identity)(_ => 1)(_ + _)
      .toSeq
    extension (p: (Long,Long))
      def *(k: Long): (Long,Long) = (p._1 * k, p._2 * k)
      def +(other: (Long,Long)): (Long,Long) = (p._1 + other._1, p._2 + other._2)
    def rec(p1p: Int, p2p: Int, p1s: Long, p2s: Long): (Long, Long) = {
      cache.getOrElseUpdate((p1p, p2p, p1s, p2s), {
          quantumDie.map{ (space, count) =>
              val pos = calcPos(p1p, space)
              val score = p1s + pos
              val (p2Cnt, p1Cnt) = if score >= limit
                then (0L, 1L)
                else rec(p2p, pos, p2s, score)
              (p1Cnt, p2Cnt) * count}
            .reduce(_ + _)
      })
    }
    val res = rec(p1StartPos,p2StartPos,0L,0L)
    res._1 max res._2
  }

  def part1: Long = playDeterministicDice(p1start, p2start, 1000)

  def part2: Long = playQuantumDice(p1start, p2start, 21)

  @main
  def DiracDice(): Unit = {
    Console.println(part1)     // 742257
    Console.println(part2)     // 93726416205179
  }
}
