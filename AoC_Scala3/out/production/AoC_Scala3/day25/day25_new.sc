case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def <=(that: Pos): Boolean = x <= that.x && y <= that.y
  def max(that: Pos): Pos = Pos(x max that.x, y max that.y)
}

def parseCucumberSea(s: Seq[String]): ((Set[Pos],Set[Pos]),Pos) = {
  val cs = s
    .zipWithIndex
    .flatMap { (s,y) =>
      s.zipWithIndex
        .map { (c,x) => Pos(x,y) -> c }
    }.toMap
  val max = cs.keySet.reduce(_ max _)
  val (goR,goD) = cs.filterNot(_._2 == '.').partition(_._2 == '>')
  (goR.keySet,goD.keySet) -> max
}

val ss = "v...>>.vv>\n.vv>>.vv..\n>>.>v>...v\n>>v>>.>.v.\nv>v.vv.v..\n>.>>..v...\n.vv..>.>v.\nv.v..>>v.v\n....v..v.>".split('\n')
//val ss = "...>>>>>...".split('\n')
//val ss = "..........\n.>v....v..\n.......>..\n..........".split('\n')
//val ss = "...>...\n.......\n......>\nv.....>\n......>\n.......\n..vvv..".split('\n')

val (init,max) = parseCucumberSea(ss)

def printBoard(board: (Set[Pos],Set[Pos])): Unit = {
  println()
  (0 to max.y)
    .foreach{ y =>
      (0 to max.x)
        .foreach{ x =>
          val p = Pos(x,y)
          print(if board._1.contains(p) then '>' else if board._2.contains(p) then 'v' else '.')
        }
      println()
    }
}

extension (p: Pos)
  def goRight: Pos = if p.x == max.x then p.copy(x = 0) else p + Pos(1,0)
  def goDown: Pos = if p.y == max.y then p.copy(y = 0) else p + Pos(0,1)

def halfStep(workSet: Set[Pos], otherSet: Set[Pos], f: (Pos) => Pos): Set[Pos] = {
  val go = workSet.filterNot( p => (workSet union otherSet).contains(f(p)))
  go.foldLeft(workSet){ (acc,p) => acc excl p incl f(p) }
}

printBoard(init)
val (cucsR,cucsD) = init
//val goR = cucsR.filterNot( p => (cucsR union cucsD).contains(p.goRight) )
//val newCucsR = goR.foldLeft(cucsR){ (acc,p) => acc excl p incl p.goRight }
val newCucsR = halfStep(cucsR,cucsD,_.goRight)
//val goD = cucsD.filterNot{ p => (newCucsR union cucsD).contains(p.goDown) }
//val newCucsD = goD.foldLeft(cucsD){ (acc,p) => acc excl p incl p.goDown }
val newCucsD = halfStep(cucsD,newCucsR,_.goDown)
val nextCucs = (newCucsR,newCucsD)
printBoard(nextCucs)
val newD = cucsD excl Pos(0,0) incl Pos(0,0).goDown
newD diff cucsD
cucsD diff newD

def step(cucs: (Set[Pos],Set[Pos])): ((Set[Pos],Set[Pos]),Boolean) = {
  def halfStep(workSet: Set[Pos], otherSet: Set[Pos], f: Pos => Pos): Set[Pos] = {
    val go = workSet.filterNot{ p => (workSet union otherSet) contains f(p) }
    go.foldLeft(workSet){ (acc,p) => acc excl p incl f(p) }
  }
  val (cucsR,cucsD) = cucs
  val newCucsR = halfStep(cucsR,cucsD,_.goRight)
  val newCucsD = halfStep(cucsD,newCucsR,_.goDown)
  (newCucsR,newCucsD) -> (newCucsR == cucsR && newCucsD == cucsD)
}

def go(init: (Set[Pos],Set[Pos])): Iterator[(Boolean,(Set[Pos],Set[Pos]))] = Iterator.unfold(init) {
  cucs =>
    val (newCucs,changes) = step(cucs)
    if changes then None
    else Some(changes -> newCucs,newCucs)
}

val res = go(init).takeWhile(_._1 == false).toList
res.foreach(x => printBoard(x._2))
res.size+1