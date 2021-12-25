case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def -(other: Pos): Pos = Pos(x - other.x, y - other.y)
  def <=(that: Pos): Boolean = x <= that.x && y <= that.y
  def min(that: Pos): Pos = Pos(x min that.x, y min that.y)
  def max(that: Pos): Pos = Pos(x max that.x, y max that.y)
  def %(that: Pos): Pos = Pos(x % that.x, y % that.y)
  def nearRight:Pos = this + Pos.Right
  def nearDown:Pos = this + Pos.Down
  def nearLeft:Pos = this - Pos.Right
  def nearUp:Pos = this - Pos.Down
}
object Pos {
  val zero = Pos()
  val Right: Pos = Pos(1, 0)
  val Down: Pos = Pos(0, 1)
}
case class Box(min: Pos, max: Pos) {
  def contains(pos: Pos): Boolean = min <= pos && pos <= max
  def wrapAround(pos: Pos): Pos = {
    (pos + max + Pos(1,1)) % (max + Pos(1,1))
  }
}
def parseCucumberSea(s: String): Map[Pos,Char] = s
    .split('\n')
    .zipWithIndex
    .flatMap { (s,y) =>
      s.zipWithIndex
        .map { (c,x) => Pos(x,y) -> c }
    }.toMap
val s = "..........\n.>v....v..\n.......>..\n.........."
val cs: Map[Pos,Char] = s
  .split('\n')
  .zipWithIndex
  .flatMap { (s,y) =>
    s.zipWithIndex
      .map { (c,x) => Pos(x,y) -> c }
  }.toMap

def printBoard(board: Map[Pos, Char]): Unit = {
  println()
  (board.minBy(_._1.y)._1.y to board.maxBy(_._1.y)._1.y)
    .foreach{ y =>
      (board.minBy(_._1.x)._1.x to board.maxBy(_._1.x)._1.x)
        .foreach{ x =>
          print(board(Pos(x,y)))
        }
      println()
    }
}

val box = Box(cs.keySet.reduce(_ min _),cs.keySet.reduce(_ max _))
box.wrapAround(Pos(0,0))
box.wrapAround(Pos(1,1))
box.wrapAround(Pos(9,3))
box.wrapAround(Pos(10,4))
box.wrapAround(Pos(10,3))
box.wrapAround(Pos(9,4))
box.wrapAround(Pos(-1,-1))
box.wrapAround(Pos(-1,0))
box.wrapAround(Pos(0,-1))
var cucs = parseCucumberSea(s)
//val cucs = cs.filterNot(_._2 == '.').withDefault {
//  pos => if box.contains(pos) then '.'
//  else cs(box.wrapAround(pos))
//}
printBoard(cucs)
cucs(Pos(0,0))
cucs(Pos(1,1))
val s1 = "...>...\n.......\n......>\nv.....>\n......>\n.......\n..vvv.."
var cucs1 = parseCucumberSea(s1)
cucs1(Pos(3,0))
cucs1(Pos(0,3))
cucs1(Pos(6,3))
cucs1(Pos(3,6))
def step(cucs: Map[Pos,Char]): (Map[Pos,Char],Int) = {
  val max0 = cucs.keySet.reduce(_ max _)
  var cnt = 0
  val cucsR = cucs
    .filter(_._2 == '>')
    .keySet
    .filter{ p =>
      (if p.nearRight.x > max0.x
      then cucs(p.copy(x = 0))
      else cucs(p.nearRight)) == '.'}
    .foldLeft(cucs){ (acc,p) =>
      cnt += 1
      acc.updated(p,'.').updated(if p.nearRight.x > max0.x
      then p.copy(x = 0)
      else p.nearRight,'>') }
  val cucsD = cucsR
    .filter(_._2 == 'v')
    .keySet
    .filter{ p =>
      (if p.nearDown.y > max0.y
      then cucsR(p.copy(y = 0))
      else cucsR(p.nearDown)) == '.' }
    .foldLeft(cucsR){ (acc,p) =>
      cnt += 1
      acc.updated(p,'.').updated(if p.nearDown.y > max0.y
      then p.copy(y = 0)
      else p.nearDown,'v') }
  cucsD -> cnt
}
val s0 = "...>>>>>..."
var cucs0 = parseCucumberSea(s0)
printBoard(cucs0)
val max0 = cucs0.keySet.reduce(_ max _)
cucs0 = cucs0
  .filter(_._2 == '>')
  .keySet
  .filter{ p =>
    (if p.nearRight.x > max0.x
    then cucs0(p.copy(x = 0))
    else cucs0(p.nearRight)) == '.'}
  .foldLeft(cucs0){ (acc,p) =>
    acc.updated(p,'.').updated(if p.nearRight.x > max0.x
    then p.copy(x = 0)
    else p.nearRight,'>') }
cucs0 = cucs0
  .filter(_._2 == 'v')
  .keySet
  .filter{ p =>
    (if p.nearDown.y > max0.y
    then cucs0(p.copy(y = 0))
    else cucs0(p.nearRight)) == '.' }
  .foldLeft(cucs0){ (acc,p) =>
    acc.updated(p,'.').updated(if p.nearDown.y > max0.y
    then p.copy(y = 0)
    else p.nearRight,'v') }
printBoard(cucs0)
cucs0 = step(cucs0)._1
printBoard(cucs0)
cucs0 = step(cucs0)._1
printBoard(cucs0)
cucs0 = step(cucs0)._1
printBoard(cucs0)

printBoard(cucs)
cucs = step(cucs)._1
printBoard(cucs)
cucs = step(cucs)._1
printBoard(cucs)
cucs = step(cucs)._1
printBoard(cucs)

val s1 = "...>...\n.......\n......>\nv.....>\n......>\n.......\n..vvv.."
var cucs1 = parseCucumberSea(s1)
printBoard(cucs1)
cucs1 = step(cucs1)._1
printBoard(cucs1)
cucs1 = step(cucs1)._1
printBoard(cucs1)
cucs1 = step(cucs1)._1
printBoard(cucs1)
def go(init: Map[Pos,Char]) = Iterator.unfold(init) {
  cucs =>
    val (newCucs,cnt) = step(cucs)
    if cnt == 0 then None
    else Some(cnt,newCucs)
}
val s2 = "v...>>.vv>\n.vv>>.vv..\n>>.>v>...v\n>>v>>.>.v.\nv>v.vv.v..\n>.>>..v...\n.vv..>.>v.\nv.v..>>v.v\n....v..v.>"
val cucs2 = parseCucumberSea(s2)
val res = go(cucs2).toList
res.size