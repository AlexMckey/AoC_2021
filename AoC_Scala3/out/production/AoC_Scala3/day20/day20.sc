case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def near: Seq[Pos] = Pos.near8.map(_ + this)
  def min(that: Pos): Pos = Pos(x min that.x, y min that.y)
}
object Pos {
  val zero = Pos()
  val near8: Seq[Pos] = Seq(Pos(-1, -1), Pos(0, -1), Pos(1, -1), Pos(-1, 0), Pos(0, 0), Pos(1, 0), Pos(-1, 1), Pos(0, 1), Pos(1, 1))
}
val s = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##\n#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###\n.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.\n.#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....\n.#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..\n...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....\n..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n\n#..#.\n#....\n##..#\n..#..\n..###"
val Array(ds: String, is: String) = s.split("\n\n")
val decodeArr = ds.split('\n').mkString("")
var image: Map[Pos, Char] = is
  .split('\n')
  .zipWithIndex
  .flatMap((s,y) => s
    .zipWithIndex
    .map((c,x) => Pos(x,y) -> c))
  .toMap
  .withDefaultValue('.')
def convert(board: Map[Pos, Char]): Map[Pos, Char] = {
  val p = board.keys.reduce(_ min _) + Pos(-3,-3)
  val bs = p.near.map(board).mkString
    .replace('.','0')
    .replace('#','1')
  val i = Integer.parseInt(bs,2)
  val defChar = decodeArr(i)
  (board.minBy(_._1.x)._1.x-1 to board.maxBy(_._1.x)._1.x+1)
    .flatMap { y =>
      (board.minBy(_._1.y)._1.y-1 to board.maxBy(_._1.y)._1.y+1)
        .map { x =>
          val p = Pos(x,y)
          val bs = p.near.map(board).mkString
            .replace('.','0')
            .replace('#','1')
          val i = Integer.parseInt(bs,2)
          p -> decodeArr(i)
        }
    }.toMap.withDefaultValue(defChar)
}
def printBoard(board: Map[Pos, Char]): Unit = {
  (board.minBy(_._1.x)._1.x-1 to board.maxBy(_._1.x)._1.x+1)
    .foreach{ y =>
      (board.minBy(_._1.y)._1.y-1 to board.maxBy(_._1.y)._1.y+1)
        .foreach{ x =>
          print(board(Pos(x,y)))
        }
      println()
    }
}
printBoard(image)

val p1 = Pos(2,2)
val bs = p1.near.map(image).mkString
  .replace('.','0')
  .replace('#','1')
val i = Integer.parseInt(bs,2)
val dc = decodeArr(i)

image = convert(image)
printBoard(image)
image = convert(image)
printBoard(image)