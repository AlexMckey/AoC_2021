case class Pos(x: Int = 0, y: Int = 0) {
  def +(other: Pos): Pos = Pos(x + other.x, y + other.y)
  def -(other: Pos): Pos = Pos(x - other.x, y - other.y)
  def near: Seq[Pos] = Pos.near8.map(_ + this)
  def min(that: Pos): Pos = Pos(x min that.x, y min that.y)
  def max(that: Pos): Pos = Pos(x max that.x, y max that.y)
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
  .filterNot(_._2 == '.')
  .toMap
  .withDefaultValue('.')
val nearNum = Array(256,128,64,32,16,8,4,2,1).reverse

def printBoard(image: Map[Pos, Char]): Unit = {
  val minBox = image.keys.reduce(_ min _)
  val maxBox = image.keys.reduce(_ max _)
  (minBox.x-1 to maxBox.x+1)
    .foreach { y =>
      (minBox.y-1 to maxBox.y+1)
        .foreach { x =>
          print(image(Pos(x,y)))
        }
      println()
    }
}

def convert(board: Map[Pos, Char]): Map[Pos, Char] = {
  val minBox = board.keys.reduce(_ min _)
  val maxBox = board.keys.reduce(_ max _)
  val bs = (minBox - Pos(3,3)).near.map(board).mkString
    .replace('.','0')
    .replace('#','1')
  val i = Integer.parseInt(bs,2)
  val defChar = decodeArr(i)
  image.keys.flatMap{ p =>
      p.near.zip(nearNum)
    }.groupMapReduce(_._1)(_._2)(_ + _)
    .map((p,i) => p -> decodeArr(i))
    .filterNot(_._2 == defChar)
    .toMap
    .withDefaultValue(defChar)
}

image.size

val p1 = Pos(3,0)
image(p1)
val ns = p1.near.zip(nearNum).map((p,i) => p -> decodeArr(i))
  .toMap.withDefaultValue('.')
printBoard(ns)

val res = image.keys.toSeq.flatMap(_.near.zip(nearNum))
  .groupMap(_._1)(_._2)
res(Pos(2,2))

printBoard(image)
image = convert(image)
printBoard(image)
image = convert(image)
printBoard(image)