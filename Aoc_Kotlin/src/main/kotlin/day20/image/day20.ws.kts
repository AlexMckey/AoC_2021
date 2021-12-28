import AoC_Lib.Pos
import AoC_Lib.toMapGrid

val s = "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##\n" +
        "#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###\n" +
        ".######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.\n" +
        ".#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....\n" +
        ".#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..\n" +
        "...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....\n" +
        "..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#\n" +
        "\n" +
        "#..#.\n" +
        "#....\n" +
        "##..#\n" +
        "..#..\n" +
        "..###"
val ss = s.split("\n\n")
val decode = ss.first().replace("\n","")
val images = ss.last().split('\n')
val image = images.toMapGrid { it }
image
val defaultPixel = '.'
val nearNum = generateSequence(1){ it * 2}.take(9).toList()
nearNum
fun Set<Pos>.min() = this.reduce(Pos::min)
fun Set<Pos>.max() = this.reduce(Pos::max)
fun Pos.all9(): List<Pos> = (-1 .. 1).flatMap { y -> (-1 .. 1).map { x -> Pos(x,y) } }.map { it + this }
Pos.Zero.all9()
fun printImage(image: Map<Pos,Char>): Unit {
    val minBox = image.keys.min()
    val maxBox = image.keys.max()
    (minBox.x .. maxBox.x)
        .forEach { y ->
            (minBox.y .. maxBox.y)
                .forEach { x ->
                    print(image.getOrDefault(Pos(x,y),'.'))
                }
            println()
        }
}
printImage(image)
fun convert(image: Map<Pos,Char>): Map<Pos, Char> /*: Map<Pos,Char>*/ {
    val minBox = image.keys.min()
    val maxBox = image.keys.max()

    return image.filterValues { it == '#' }.keys.flatMap{ p ->
        p.all9().zip(nearNum)
    }.groupBy({ it.first },{ it.second })
        .mapValues { it.value.sum() }
        .mapValues { decode[it.value] }
    .toMap()
}
val i1 = convert(image)
printImage(i1)
val i2 = convert(i1)
printImage(i2)
i2.count{ it.value == '#' }

val fi = generateSequence(image){i ->
    convert(i)
}.drop(50).first()
printImage(fi)
fi.count{ it.value == '#' }