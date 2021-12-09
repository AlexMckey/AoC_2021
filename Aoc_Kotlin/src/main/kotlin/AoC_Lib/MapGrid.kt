package AoC_Lib

typealias MapGrid<T> = Map<Pos,T>

fun <T> Iterable<String>.toMapGrid(f: (Char) -> T) = this
    .flatMapIndexed { col, line ->
        line.mapIndexed { row, char ->
            Pos(row, col) to f(char)
        }
    }
    .associate { it }

//fun <T,K> Iterable<Iterable<T>>.toMapGrid(f: (T) -> K) = this
//    .flatMapIndexed { col, line ->
//        line.mapIndexed { row, char ->
//            Pos(row, col) to f(char)
//        }
//    }
//    .associate { it }

fun <T : Any> Pos.inBounds(grid: MapGrid<T>) = grid.containsKey(this)