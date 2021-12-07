val l = List(3,4,3,1,2)
val m = l.groupMapReduce(identity)(_=>1)(_+_).withDefaultValue(0)
val a = Array.ofDim[Long](9)
a
m.foreach((i,v) => a(i) = v)
a
def calcPopulation(l: List[Int], d: Int): Long = {
  val m = l.groupMapReduce(identity)(_=>1)(_+_).withDefaultValue(0)
  val a = Array.ofDim[Long](9)
  m.foreach((i,v) => a(i) = v)
  (1 to d).foreach{ i =>
    val borns = a(0)
    a.tail.copyToArray(a)
    a(8) = borns
    a(6) += borns
    val b = a.drop(9-(i+8)%9-1) ++ a.take(9-(i+8)%9-1)
    println(s"${b.toList}")
  }
  a.sum
}
calcPopulation(l,18)
a.tail.copyToArray(a)
a
(0 to 80).map( i => (i+8)%9)
(0 to 80).map( i => (i+6)%9)
def calcFishCount(l: List[Int], d: Int): Array[Long] = {
  val m = l.groupMapReduce(identity)(_ => 1)(_ + _)
  val a = Array.ofDim[Long](9)
  m.foreach{ (i, v) => a(i) = v }
  (0 to d-1).foreach { i =>
    val borns = a(i % 9)
    //a((i + 9) % 9) = borns
    a((i + 7) % 9) += borns
    //println(a.toList)
  }
  a
}
calcFishCount(l,80).sum