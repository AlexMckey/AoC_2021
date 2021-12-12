import scala.annotation.tailrec

val s__ = "start-A\nstart-b\nA-c\nA-b\nb-d\nA-end\nb-end"
val s_ = "dc-end\nHN-start\nstart-kj\ndc-start\ndc-HN\nLN-dc\nHN-end\nkj-sa\nkj-HN\nkj-dc"
val s = "fs-end\nhe-DX\nfs-he\nstart-DX\npj-DX\nend-zg\nzg-sl\nzg-pj\npj-he\nRW-he\nfs-DX\npj-RW\nzg-RW\nstart-pj\nhe-WI\nzg-he\npj-fs\nstart-RW"
val d = s
  .split('\n')
  .map(_.split('-'))
  .collect{ case Array(a1,a2) => a1 -> a2 }

val g = (d ++ d.map(_.swap)).groupMap(_._1)(_._2)
def bfs(start: String, finish: String) = {
  def rec(q: List[String], v: Set[String], p: List[String]): List[List[String]] = {
    if q.isEmpty || v.contains(finish) then List(p)
    else
      val n = q.head
      val newv = if n.head.isLower && !v.contains(n)
        then v + n
        else v
      val ns = g(n).toSet diff v
      ns.toList.flatMap(x => rec(q.tail :+ x, newv, p :+ n))
  }
  rec(List(start),Set.empty,List.empty).distinct
}
def otherBFS(start: String) = {
  @tailrec
  def rec(queue: List[List[String]], visited: List[List[String]]): List[List[String]] = {
    val neighbors = queue
      .flatMap(path => g(path.head)
        .filter(neighbor => neighbor.head.isUpper || !path.contains(neighbor))
        .map(neighbor => neighbor :: path))
    val newVisited = visited ++ queue
    val newQueue = neighbors diff visited
    if (newQueue.isEmpty) newVisited
    else
      rec(newQueue, newVisited)
  }
  rec(List(List(start)), List.empty)
}
def BFS(start: String) = {
  @tailrec
  def rec(queue: List[List[String]], visited: List[List[String]]): List[List[String]] = {
    val neighbors = queue
      .filterNot(_.head == "end")
      .flatMap(path => g(path.head)
        .filterNot(_ == "start")
        .filter(n => n.head.isUpper
          || !path.contains(n)
          || (path.contains(n) && path.filter(_.head.isLower).groupMapReduce(identity)(_ => 1)(_ + _).forall(_._2 < 2)))
        .map(neighbor => neighbor :: path))
    val newVisited = visited ++ queue
    val newQueue = neighbors diff visited
    if (newQueue.isEmpty) newVisited
    else
      rec(newQueue, newVisited)
  }
  rec(List(List(start)), List.empty)
}
val p1 = bfs("start","end")
p1.size
val p2 = otherBFS("start")
p2.count(_.head == "end")
val p3 = BFS("start")
p3.count(_.head == "end")

def traverse(ruleToVisit: (String, List[String]) => Boolean, path: List[String] = List("start")): Array[List[String]] = {
  if path.head == "end" then Array(path)
  else g(path.head)
    .filter(ruleToVisit(_,path))
    .flatMap(next => traverse(ruleToVisit, next :: path))
}
def part1Rule(s: String, p: List[String]): Boolean = s.head.isUpper || !p.contains(s)

traverse(part1Rule).length

def part2Rule(s: String, p: List[String]): Boolean = {
  if s == "start" then false
  else if s.head.isUpper then true
  else if !p.contains(s) then true
  else p.filter(_.head.isLower)
    .groupBy(identity)
    .forall(_._2.sizeIs < 2)
}

traverse(part2Rule).length