package AoC_Lib

typealias ListGrid<T> = List<List<T>>
typealias MutableListGrid<T> = List<MutableList<T>>

fun <T> ListGrid<T>.mutable() = this.map { it.toMutableList() }

fun List<String>.toListGrid() = this.map { it.toCharArray().toList() }

fun <T> ListGrid<T>.transpose(): ListGrid<T> =
    this.filter { it.isNotEmpty() }.let { ys ->
        return when (ys.isNotEmpty()) {
            true -> listOf(ys.map { it.first() })
                .plus(ys.map { it.drop(1) }.transpose())
            else -> emptyList()
        }
    }

fun <T> ListGrid<T>.find(value: T): Pos? {
    forEachIndexed { y, row ->
        row.forEachIndexed { x, curr ->
            if (curr == value) {
                return Pos(x, y)
            }
        }
    }
    return null
}

operator fun <T> MutableListGrid<T>.set(v: Pos, value: T) { when {
    v.y !in indices -> throw IndexOutOfBoundsException()
    v.x !in this[v.y].indices -> throw IndexOutOfBoundsException()
    else -> this[v.y][v.x] = value
}
}
operator fun <T> MutableListGrid<T>.set(i: Int, j: Int, value: T)
{ this[i][j] = value }

operator fun <T> ListGrid<T>.get(v: Pos): T? = when {
    v.y !in indices -> null
    v.x !in this[v.y].indices -> null
    else -> this[v.y][v.x]
}
operator fun <T> ListGrid<T>.get(x: Int, y: Int): T? = get(Pos(x,y))

fun <T> ListGrid<T>.inBounds(x: Int, y: Int) = y in indices && x in this[y].indices
operator fun <T> ListGrid<T>.contains(p: Pos) = this.inBounds(p.x,p.y)

@JvmName("boundsString")
fun List<String>.bounds(): Pair<IntRange,IntRange> =
    this.first().indices to this.indices

fun <T> ListGrid<T>.bounds(): Pair<IntRange,IntRange> =
    this.first().indices to this.indices

fun <T : Any> Pos.inBounds(grid: ListGrid<T>) = grid.contains(this)