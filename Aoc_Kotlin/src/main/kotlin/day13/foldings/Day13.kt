package day13.foldings

import AoC_Lib.*
import java.lang.StringBuilder

object Day13: SomeDay(2021,13) {
    override val title = "Transparent Origami"

    data class Folding(val axis: Char, val i: Int)

    private fun foldAlign(sheet: Set<Pos>, fold: Folding): Set<Pos> =
        sheet.map { p -> when {
            fold.axis == 'x' && p.x > fold.i -> Pos(fold.i + fold.i - p.x, p.y)
            fold.axis == 'y' && p.y > fold.i -> Pos(p.x, fold.i + fold.i - p.y)
            else -> p } }.toSet()

    private fun sheetToString(sheet: Set<Pos>): String {
        val maxX = sheet.maxOf { it.x }
        val maxY = sheet.maxOf { it.y }
        val sb = StringBuilder()
            .append('\n')
        (0 .. maxY).forEach { y ->
            (0 .. maxX).forEach { x ->
                sb.append(if (Pos(x,y) in sheet) '#' else '.')}
            sb.append('\n') }
        return sb.toString()
    }

    override fun first(data: String): Any? {
        val (d,f) = data.split("\n\n")
        val dots = d.lines().map(Pos::fromString).toSet()
        val folds = f.lines().map { it.split('=')
            .let { Folding(it.first().last(), it.last().toInt()) } }
        return foldAlign(dots, folds.first()).size
    } // 602 Time: 46ms

    override fun second(data: String): Any? {
        val (d,f) = data.split("\n\n")
        val dots = d.lines().map(Pos::fromString).toSet()
        val folds = f.lines().map { it.split('=')
            .let { Folding(it.first().last(), it.last().toInt()) } }
        return sheetToString(folds.fold(dots,::foldAlign))
    }
    // .##...##..####...##.#..#.####..##..#..#
    // #..#.#..#.#.......#.#..#....#.#..#.#.#.
    // #....#..#.###.....#.####...#..#....##..
    // #....####.#.......#.#..#..#...#....#.#.
    // #..#.#..#.#....#..#.#..#.#....#..#.#.#.
    // .##..#..#.#.....##..#..#.####..##..#..#
    // Time: 6ms
}

fun main() = SomeDay.mainify(Day13)