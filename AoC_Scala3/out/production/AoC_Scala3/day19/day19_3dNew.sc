import scala.annotation.tailrec

case class Pos3D(x: Int, y: Int, z: Int) {
  def +(that: Pos3D): Pos3D = Pos3D(x + that.x, y + that.y, z + that.z)
  def *:(k: Int): Pos3D = Pos3D(k * x, k * y, k * z)
  def unary_- : Pos3D = -1 *: this
  def -(that: Pos3D): Pos3D = this + (-that)
  def manhattanDistance(that: Pos3D): Int =
    (x - that.x).abs + (y - that.y).abs + (z - that.z).abs
  def <=(that: Pos3D): Boolean =
    x <= that.x && y <= that.y && z <= that.z
  def min(that: Pos3D): Pos3D =
    Pos3D(x min that.x, y min that.y, z min that.z)
  def max(that: Pos3D): Pos3D =
    Pos3D(x max that.x, y max that.y, z max that.z)
  def facing: Seq[Pos3D] = Seq(
    this,
    Pos3D(x,-y,-z),
    Pos3D(x,-z,y),
    Pos3D(-y,-z,x),
    Pos3D(y,-z,-x),
    Pos3D(-x,-z,-y),
  )
  private def rotating: Seq[Pos3D] = Seq(
    this,
    Pos3D(-y,x,z),
    Pos3D(-x,-y,z),
    Pos3D(y,-x,z)
  )
  def allOrientation: Seq[Pos3D] = facing.flatMap(f => f.rotating)
}
object Pos3D {
  val zero: Pos3D = Pos3D(0, 0, 0)
  extension (s: String)
    def toPos3D: Pos3D = s match {
      case s"$x,$y,$z" => Pos3D(x.toInt,y.toInt,z.toInt)
      case _ => throw IllegalArgumentException("Conversion to Pos3D fail")
    }
}

val s = "--- scanner 0 ---\n404,-588,-901\n528,-643,409\n-838,591,734\n390,-675,-793\n-537,-823,-458\n-485,-357,347\n-345,-311,381\n-661,-816,-575\n-876,649,763\n-618,-824,-621\n553,345,-567\n474,580,667\n-447,-329,318\n-584,868,-557\n544,-627,-890\n564,392,-477\n455,729,728\n-892,524,684\n-689,845,-530\n423,-701,434\n7,-33,-71\n630,319,-379\n443,580,662\n-789,900,-551\n459,-707,401\n\n--- scanner 1 ---\n686,422,578\n605,423,415\n515,917,-361\n-336,658,858\n95,138,22\n-476,619,847\n-340,-569,-846\n567,-361,727\n-460,603,-452\n669,-402,600\n729,430,532\n-500,-761,534\n-322,571,750\n-466,-666,-811\n-429,-592,574\n-355,545,-477\n703,-491,-529\n-328,-685,520\n413,935,-424\n-391,539,-444\n586,-435,557\n-364,-763,-893\n807,-499,-711\n755,-354,-619\n553,889,-390\n\n--- scanner 2 ---\n649,640,665\n682,-795,504\n-784,533,-524\n-644,584,-595\n-588,-843,648\n-30,6,44\n-674,560,763\n500,723,-460\n609,671,-379\n-555,-800,653\n-675,-892,-343\n697,-426,-610\n578,704,681\n493,664,-388\n-671,-858,530\n-667,343,800\n571,-461,-707\n-138,-166,112\n-889,563,-600\n646,-828,498\n640,759,510\n-630,509,768\n-681,-892,-333\n673,-379,-804\n-742,-814,-386\n577,-820,562\n\n--- scanner 3 ---\n-589,542,597\n605,-692,669\n-500,565,-823\n-660,373,557\n-458,-679,-417\n-488,449,543\n-626,468,-788\n338,-750,-386\n528,-832,-391\n562,-778,733\n-938,-730,414\n543,643,-506\n-524,371,-870\n407,773,750\n-104,29,83\n378,-903,-323\n-778,-728,485\n426,699,580\n-438,-605,-362\n-469,-447,-387\n509,732,623\n647,635,-688\n-868,-804,481\n614,-800,639\n595,780,-596\n\n--- scanner 4 ---\n727,592,562\n-293,-554,779\n441,611,-461\n-714,465,-776\n-743,427,-804\n-660,-479,-426\n832,-632,460\n927,-485,-438\n408,393,-506\n466,436,-512\n110,16,151\n-258,-428,682\n-393,719,612\n-211,-452,876\n808,-476,-593\n-575,615,604\n-485,667,467\n-680,325,-822\n-627,-443,-432\n872,-547,-609\n833,512,582\n807,604,487\n839,-516,451\n891,-625,532\n-652,-548,-490\n30,-46,-14"
val sensors = s.split("\n\n").map(_.split('\n').tail.map(Pos3D.toPos3D).toSet)
val p0s0 = sensors(0).head
p0s0.allOrientation
p0s0.allOrientation.size

case class Transform(scanner: Pos3D, beacons: Set[Pos3D])

def posOrientations(pos: Pos3D): Seq[Pos3D] = {
  val Pos3D(x, y, z) = pos
  Seq(
    Pos3D(x, y, z),
    Pos3D(-y, x, z),
    Pos3D(-x, -y, z),
    Pos3D(y, -x, z),

    Pos3D(-x, y, -z),
    Pos3D(y, x, -z),
    Pos3D(x, -y, -z),
    Pos3D(-y, -x, -z),

    Pos3D(-z, y, x),
    Pos3D(-z, x, -y),
    Pos3D(-z, -y, -x),
    Pos3D(-z, -x, y),

    Pos3D(z, y, -x),
    Pos3D(z, x, y),
    Pos3D(z, -y, x),
    Pos3D(z, -x, -y),

    Pos3D(x, -z, y),
    Pos3D(-y, -z, x),
    Pos3D(-x, -z, -y),
    Pos3D(y, -z, -x),

    Pos3D(x, z, -y),
    Pos3D(-y, z, -x),
    Pos3D(-x, z, y),
    Pos3D(y, z, x),
  )
}
posOrientations(p0s0) diff p0s0.allOrientation
(posOrientations(p0s0) intersect p0s0.allOrientation).size
def scannerOrientations(scanner: Set[Pos3D]): Seq[Set[Pos3D]] = {
  scanner.toSeq.map(posOrientations).transpose.map(_.toSet)
}
def myScannerOrientations(scanner: Set[Pos3D]): Seq[Set[Pos3D]] = {
  scanner.toSeq.map(_.allOrientation).transpose.map(_.toSet)
}

scannerOrientations(sensors.head) diff myScannerOrientations(sensors.head)
(scannerOrientations(sensors.head) intersect myScannerOrientations(sensors.head)).size


def findTransformIfIntersects(left: Set[Pos3D], right: Set[Pos3D]): Option[Transform] = {
  (for {
    p1 <- left
    rightReoriented <- scannerOrientations(right)
    p2 <- rightReoriented
    d = p1 - p2
    moved = rightReoriented.map(_ + d)
    if (moved intersect left).size >= 12
  } yield Transform(d,moved))
    .headOption
}
findTransformIfIntersects(sensors.head,sensors(1))
val left = sensors(0)
val right = sensors(1)
val rightAllOrientation = scannerOrientations(right)
val rightReoriented = rightAllOrientation.head
val p1 = left.head
val p2 = rightReoriented.head
val d = p1 - p2
val moved = rightReoriented.map(_ + d)
moved intersect left
left.map{ p1 =>
  right.map(_.allOrientation).flatMap{ _.map { p2 =>
      val d = p1 - p2
      val moved = rightReoriented.map(_ + d)
      val intersect = moved intersect left
      intersect.size >= 12
    }
  }
}

def matchScanner(scanner1: Set[Pos3D], scanner2: Set[Pos3D]) = {
  (for {
    rightReoriented <- scannerOrientations(scanner2).iterator
    p1 <- scanner1.iterator
    p2 <- rightReoriented.iterator
    d = p1 - p2 // this way fits with examples
    //intersect2 = scanner2 & scanner1.map(_ - d)
    intersect2 = rightReoriented.map(_ + d) intersect scanner1 // faster this way because & filters left and looks up right
    () = println(intersect2.size)
    if intersect2.size >= 12
  } yield (rightReoriented, d)).nextOption()
}
matchScanner(sensors.head,sensors(1))

extension [A](it: Iterator[A]) {
  def headOption: Option[A] = if (it.nonEmpty) Some(it.next()) else None
  def head: A = headOption.get

  def lastOption: Option[A] = it.reduceOption((_, x) => x)
  def last: A = lastOption.get

  def apply(i: Int): A = it.drop(i).head
}

def matchNotMyScanner(beacons: Set[Pos3D], scanner: Set[Pos3D]): Option[(Set[Pos3D], Pos3D)] = {
  /*(for {
    orientedScanner <- scannerOrientations(scanner).iterator
    p1 <- beacons.iterator
    p2 <- orientedScanner.iterator
    d = p1 - p2 // this way fits with examples
    if orientedScanner.view.map(_ + d).filter(beacons).sizeIs >= 12 // iterate over smaller scanner2, avoid any intermediate collections
  } yield (orientedScanner.map(_ + d), d)).headOption*/
  // TODO: is this guaranteed to be correct? could differences corresponding to different translations combine to >= 12 without neither being?
  (for {
    orientedScanner <- scannerOrientations(scanner).iterator
    ds = (for {
      p1 <- beacons.iterator
      p2 <- orientedScanner.iterator
      d = p1 - p2 // this way fits with examples
    } yield d).to(LazyList)
      .groupMapReduce(identity)(_ => 1)(_ + _)
    (d, cnt) <- ds.iterator
    if cnt >= 12
  } yield (orientedScanner.map(_ + d), d)).headOption
}
matchNotMyScanner(sensors.head,sensors(1)).toList