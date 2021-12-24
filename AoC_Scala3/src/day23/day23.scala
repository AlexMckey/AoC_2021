package day23

import AoC_Lib.*

import scala.collection.mutable

object day23 {

  val s = "#############\n#...........#\n###B#C#B#D###\n  #A#D#C#A#\n  #########"

  val roomLabels = "ABCD"
  val costs: Map[Char, Int] = roomLabels.zip(Iterator.iterate(1){ x => x * 10}).toMap
  val roomIdx: Map[Char, Int] = roomLabels.zipWithIndex.map{ (c,i) => c -> (i + 1) * 2 }.toMap
  val validHalls: Seq[Int] =  (0 to 10).filterNot(roomIdx.values.toSet.contains)

  case class State(rooms: Map[Char, String], hall: String = ".".repeat(11), cost: Int = 0):
    def isDone: Boolean = rooms.forall{ case (c,s) => s.forall(_ == c) } && hall.forall(_ == '.')
    def roomSpace: Int = State.roomMax
    override def toString: String = {
      val sb: StringBuilder = new StringBuilder()
      sb.append('\n')
      sb.append("#".repeat(13)).append('\n')
      sb.append("#").append(hall).append("#").append('\n')
      rooms.values.transpose.map(c =>
        sb.append("  #").append(c.mkString("#")).append("#  ").append('\n'))
      sb.append("  ").append("#".repeat(9)).append("  ").append('\n')
      sb.toString()
    }
  object State {
    private var roomMax = 2
    def parseBurrow(s: String, large: Boolean = false): State = {
      val largeInserts = List("DCBA","DBAC")
      val ss = s.split('\n')
        .slice(2, 4)
        .map(_.trim.replace("#", ""))
      val rs: Seq[String] = if large then ss.head +: largeInserts :+ ss.last else ss
      val rooms = rs
        .mkString
        .sliding(4, 4)
        .toSeq
        .transpose
        .map(_.mkString)
        .zip(roomLabels)
        .map(_.swap)
        .toMap
      roomMax = if large then 4 else 2
      State(rooms)
    }
  }

  def paths(current: State): Seq[State] =
    val HallToRoom = current.hall.zipWithIndex.filter(_._1 != '.').flatMap(hallwayToRoom(current))
    val RoomToRoom = current.rooms.flatMap(roomToRoom(current))
    val preferred = HallToRoom ++ RoomToRoom
    if preferred.nonEmpty then preferred else current.rooms.flatMap(roomToHallway(current)).toSeq

  def hallwayToRoom(current: State)(amphipod: Char, start: Int): Option[State] =
    val end = roomIdx(amphipod)
    val range = if start < end then (start + 1) to end else end until start

    if current.rooms(amphipod).forall(_ == amphipod) && range.forall(n => current.hall(n) == '.') then
      val newHall = current.hall.updated(start, '.')
      val newRooms = current.rooms.updated(amphipod, current.rooms(amphipod).prepended(amphipod))
      val newCost = current.cost + (range.size + current.roomSpace - current.rooms(amphipod).length) * costs(amphipod)
      Some(State(newRooms, newHall, newCost))
    else None
  end hallwayToRoom

  def roomToRoom(current: State)(label: Char, room: String): Seq[State] =
    if room.forall(_ == label) then return Seq.empty

    val start = roomIdx(label)
    val end = roomIdx(room.head)
    val range = if start < end then start to end else end to start

    if current.rooms(room.head).forall(_ == room.head) && range.forall(n => current.hall(n) == '.') then
      val newRooms: Map[Char, String] = current.rooms
        .updated(label, room.tail)
        .updated(room.head, current.rooms(room.head).prepended(room.head))
      val newCost = current.cost + (range.size + current.roomSpace - current.rooms(label).length + current.roomSpace - current.rooms(room.head).length) * costs(room.head)
      Seq(State(newRooms, current.hall, newCost))
    else Seq.empty
  end roomToRoom

  def roomToHallway(current: State)(label: Char, room: String): Seq[State] =
    if room.forall(_ == label) then return Seq.empty

    val index = roomIdx(label)
    val left = validHalls.filter(_ < index).reverse.takeWhile(n => current.hall(n) == '.')
    val right = validHalls.filter(_ > index).takeWhile(n => current.hall(n) == '.')

    (left.reverse ++ right).map { pos =>
      val newHall = current.hall.updated(pos, room.head)
      val newRooms = current.rooms.updated(label, room.tail)
      val newCost = current.cost + ((pos - index).abs + 1 + current.roomSpace - room.length) * costs(room.head)
      State(newRooms, newHall, newCost)
    }
  end roomToHallway

  def solve(state: State): Option[Int] =
    def rec(current: State, energy: Option[Int]): Option[Int] =
      if current.isDone then Some(current.cost)
      else if energy.exists(_ < current.cost) then None
      else paths(current).flatMap(rec(_, energy)).minOption
    rec(state, None)
  end solve

  //def part1: Int = solve(State.parseBurrow(s)).get                // 12521
  //def part2: Int = solve(State.parseBurrow(s, large = true)).get  // 44169

  def part1: Int = solve(State.parseBurrow(inputStr(day = 23))).get
  def part2: Int = solve(State.parseBurrow(inputStr(day = 23), large = true)).get

  @main
  def Amphipod(): Unit = {
    Console.println(part1) // 18051
    Console.println(part2) // 50245
  }

}
