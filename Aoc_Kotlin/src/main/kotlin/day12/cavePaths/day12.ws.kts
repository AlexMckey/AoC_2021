val s = "start-A\n" +
        "start-b\n" +
        "A-c\n" +
        "A-b\n" +
        "b-d\n" +
        "A-end\n" +
        "b-end"
val d = s.split('\n')
    .flatMap { it.split('-')
    .let {a -> listOf(a[0] to a[1],
                      a[1] to a[0]) }}
val cave = d.groupBy({ it.first },{ it.second })
cave

fun bfs(cave: Map<String, List<String>>, start: String, end: String, filterNeighbors: (String, List<String>) -> Boolean): List<List<String>> {
    tailrec fun rec(queue: List<List<String>>, visited: List<List<String>>): List<List<String>> {
        val neighbors = queue
            .filterNot { it.first() == end }
            .flatMap { path -> cave[path.first()]!!
                .filter { neighbor -> filterNeighbors(neighbor, path)}
                .map { neighbor -> listOf(neighbor) + path }}
        val newVisited = visited + queue
        val newQueue = neighbors - visited.toSet()
        return if (newQueue.isEmpty()) newVisited
        else rec(newQueue, newVisited)
    }
    return rec(listOf(listOf(start)), emptyList())
}

fun fp1(n: String, p: List<String>): Boolean =
    n[0].isUpperCase() || n !in p

fun fp2(n: String, p: List<String>): Boolean =
    when {
        n == "start" -> false
        n.first().isUpperCase() -> true
        n !in p -> true
        else -> p.filter { it.first().isLowerCase() }
            .groupBy { it }
            .none { it.value.size == 2 }

    }

val res1 = bfs(cave, "start", "end", ::fp1)
res1.count{ it.first() == "end" }
val res2 = bfs(cave, "start", "end", ::fp2)
res2.count{ it.first() == "end" }