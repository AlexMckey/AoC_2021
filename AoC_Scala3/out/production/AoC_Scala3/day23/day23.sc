val s = "#############\n#...........#\n###B#C#B#D###\n  #A#D#C#A#\n  #########"
val init: Map[Char,String] = s
  .replace("#","")
  .replace("\n","")
  .replace(".","")
  .replace(" ","")
  .sliding(4,4)
  .toSeq
  .transpose
  .map(_.mkString)
  .zip("ABCD")
  .map(_.swap)
  .toMap + ('.' -> ".".repeat(11))

var state = init

def isDone(state: Map[Char,String]) =
  state.forall{ case (c,s) => s.forall(_ == c) }
isDone(init)

"BB".stripPrefix(".").headOption.contains('B')
"BC".stripPrefix(".").headOption.contains('B')
".B".stripPrefix(".").headOption.contains('B')
"..".stripPrefix(".").headOption.contains('B')
"CB".stripPrefix(".").headOption.contains('B')
".C".stripPrefix(".").headOption.contains('B')
def canMoveFrom(pod: Char, fromRoom: String): Boolean =
  !(fromRoom.forall(_ == '.') ||
    fromRoom.stripPrefix(".").forall(_ == pod))

canMoveFrom('B', "BB")
canMoveFrom('B', "BC")
canMoveFrom('B', ".B")
canMoveFrom('B', "CB")
canMoveFrom('B', ".C")
canMoveFrom('B', "..")
canMoveFrom('D', "DA")

def canMoveTo(pod: Char, toRoom: String): Boolean =
  toRoom.forall(s".$pod".contains)
canMoveTo('B',"BB")
canMoveTo('B',"BC")
canMoveTo('B',".B")
canMoveTo('B',"..")
canMoveTo('B',"CB")
canMoveTo('B',".C")

def roomIdx = "ABCD".zip(2 to 8 by 2).toMap
roomIdx

def roomFirstNonEmpty(room: String): Option[Int] =
  room.zipWithIndex.find(_._1 != '.').map(_._2)
roomFirstNonEmpty("...D")
roomFirstNonEmpty("ACDD")
roomFirstNonEmpty(".DAB")

def roomFirstEmpty(room: String): Option[Int] =
  room.zipWithIndex.findLast(_._1 == '.').map(_._2)
roomFirstEmpty("...D")
roomFirstEmpty("ACDD")
roomFirstEmpty(".DAB")

def clearPath(roomLabel: Char, hallIdx: Int, hall: String): Boolean = {
  val (from,to) = if roomIdx(roomLabel) > hallIdx
  then hallIdx -> roomIdx(roomLabel)
  else roomIdx(roomLabel) -> hallIdx
  hall.slice(from,to).forall(_ == '.')
}
clearPath('A',0,"...........")
clearPath('A',11,"...........")
clearPath('A',0,"A..........")
clearPath('A',0,".C.........")
clearPath('A',11,".C....B....")
clearPath('A',11,".C.........")
clearPath('A',11,"..C........")

def show(state: Map[Char,String]): String = {
  val sb: StringBuilder = new StringBuilder()
  sb.append('\n')
  sb.append("#".repeat(13)).append('\n')
  sb.append("#").append(state('.')).append("#").append('\n')
  state.filterNot(_._1 == '.').values.transpose.map(c =>
    sb.append("###").append(c.mkString("#")).append("###").append('\n'))
  sb.append("#".repeat(13)).append('\n')
  sb.toString()
}
show(state)

val costs = "ABCD".zip(List(1,10,100,1000)).toMap
val rooms = "ABCD"
val cache = scala.collection.mutable.Map.empty[Map[Char,String],Int]

val costs_ = rooms.zip(Iterator.iterate(1){ x => x * 10}).toMap
val roomIndex: Map[Char, Int] = rooms.zipWithIndex.map{ (c,i) => c -> (i + 1) * 2 }.toMap

".C....B....".splitAt(1)
val (leftHall,rightHall) = ".C....B....".splitAt(1)
leftHall + '.' + rightHall.tail
".C".updated(0,'A')

def solve(state: Map[Char,String]): Int = {
  show(state)
  if isDone(state) then return 0
  else if cache.contains(state) then  return cache(state)
  else {
    state('.').zipWithIndex.foreach{ case (c,i) =>
      if rooms.contains(c) && canMoveFrom(c,state(c))
        then if clearPath(c,i,state('.'))
          then {
            val destIdx = roomFirstEmpty(state(c)).get
            val len = destIdx + 1 + (roomIdx(c) - i).abs
            val cost = costs(c) * len
            val newHall = state('.').updated(i, '.')
            val newRoom = state(c).updated(destIdx, c)
            val newState = state + (c -> newRoom) + ('.' -> newHall)
            return cost + solve(newState)
      }
    }
  }
  var res = Int.MaxValue
  state.filterNot(_._1 == '.').foreach{ case (roomLabel,room) =>
    if canMoveFrom(roomLabel,room)
      then {
        val destIdx = roomFirstNonEmpty(room)
        if destIdx.isDefined
          then
            val c = room(destIdx.get)
            val hall = state('.')
            hall.indices.filterNot(roomIdx.values.toSet.contains)
              .foreach{ to =>
                if hall.charAt(to) == '.' && clearPath(roomLabel,to,hall)
                  then {
                    val len = destIdx.get + 1 + (to - roomIdx(roomLabel)).abs
                    val newHall = hall.updated(to, c)
                    val newRoom = state(roomLabel).updated(destIdx.get,'.')
                    val newState = state + (c -> newRoom) + ('.' -> newHall)
                    res = math.min(res, costs(c) * len + solve(newState))
                  }
              }

    }
  }
  cache.update(state, res)
  res
}
//solve(init)

val roomLabels = "ABCD"
def parseBurrow(s: String, large: Boolean = false): Map[Char,String] = {
  val largeInserts = List("DCBA","DBAC")
  val ss = s.split('\n')
    .slice(2, 4)
    .map(_.trim.replace("#",""))
  val rs: Seq[String] = if large then ss.head +: largeInserts :+ ss.last else ss
  val rooms = rs
    .sliding(4,4)
    .toSeq
    .transpose
    .map(_.mkString)
    .zip(roomLabels)
    .map(_.swap)
    .toMap
  rooms
}
parseBurrow(s)
parseBurrow(s,large = true)

val validHalls: Seq[Int] =  (0 to 10).filterNot(roomIdx.values.toSet.contains)
val label = 'B'
val index = roomIdx(label)
val hall = "D........C."
val left = validHalls.filter(_ < index).reverse.takeWhile(n => hall(n) == '.')
val right = validHalls.filter(_ > index).takeWhile(n => hall(n) == '.')
