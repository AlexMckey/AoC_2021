import AoC_Lib.Pos
import AoC_Lib.toMapGrid
import day20.image.Day20.all9
import day20.image.Day20.max
import day20.image.Day20.min

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
val nearNum = generateSequence(1){ it * 2}.take(9).toList().reversed()
nearNum
fun Set<Pos>.min() = this.reduce(Pos::min)
fun Set<Pos>.max() = this.reduce(Pos::max)
fun Pos.all9(): List<Pos> = (-1 .. 1)
    .flatMap { y -> (-1 .. 1).map { x -> Pos(x,y) } }
    .map { it + this }
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
fun convert(image: Map<Pos,Char>, decode: String, step: Int): Map<Pos,Char> {
    val min = image.keys.min()
    val max = image.keys.max()
    val blink = decode.last() == '.' && decode.first() == '#'
    val defChar = if (blink && step % 2 != 0) decode.last() else decode.first()
    return (min.x-1 .. max.x+1)
        .flatMap { y ->
            (min.y-1 .. max.y+1)
                .map { x ->
                    val p = Pos(x,y)
                    val bs = p.all9()
                        .map{ image.getOrDefault(it, defChar) }
                        .joinToString("")
                        .replace('.','0')
                        .replace('#','1')
                    val i = Integer.parseInt(bs,2)
                    p to decode[i]
                }
        }.toMap()
}
val i1 = convert(image,decode,0)
printImage(i1)
val i2 = convert(i1,decode,1)
printImage(i2)
i2.count{ it.value == '#' }

val fi = generateSequence(image to 0){ (im,i) ->
    convert(im,decode,i) to i + 1
}.drop(50).first().first
printImage(fi)
fi.count{ it.value == '#' }