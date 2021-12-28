package day20.image

import AoC_Lib.Pos
import AoC_Lib.SomeDay
import AoC_Lib.toMapGrid

object Day20: SomeDay(2021, 20) {
    override val title = "Trench Map"

    private fun parseInput(s: String): Pair<String,Map<Pos,Char>> {
        val ss = s.split("\n\n")
        val decode = ss.first()
        val image = ss.last()
            .split('\n')
            .toMapGrid { it }
        return decode to image
    }

    fun Set<Pos>.min() = this.reduce(Pos::min)
    fun Set<Pos>.max() = this.reduce(Pos::max)
    fun Pos.all9(): List<Pos> = (-1 .. 1)
        .flatMap { y -> (-1 .. 1)
            .map { x -> Pos(x,y) } }
        .map { it + this }

    private fun convert(image: Map<Pos,Char>, decode: String, step: Int): Map<Pos,Char> {
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

    override fun first(data: String): Any? {
        val (decode, image) = parseInput(data)
        val i1 = convert(image, decode, 1)
        val i2 = convert(i1, decode, 2)
        return i2.count { it.value == '#'}
    }
    // 5486 Time: 85ms

    override fun second(data: String): Any? {
        val (decode, image) = parseInput(data)
        return generateSequence(image to 1){ (im,i) ->
            convert(im,decode,i) to i + 1
        }.drop(50).first().first.count{ it.value == '#' }
    }
    // 20210 Time: 1520ms
}

fun main() = SomeDay.mainify(Day20)