package day02.dive

import AoC_Lib.SomeDay
import AoC_Lib.toStrs

abstract class Submarine(open var x: Int = 0, open var y: Int = 0) {
    abstract fun forward(v: Int)
    abstract fun down(v: Int)
    fun goCommand(cmd: String, amount: String): Submarine {
        val i = amount.toInt()
        when (cmd) {
            "forward" -> forward(i)
            "up" -> down(-i)
            "down" -> down(i)
            else -> {}
        }
        return this
    }
    fun depth(): Int = x * y
}
data class simpleSubmarine(override var x: Int = 0, override var y: Int = 0): Submarine(x,y) {
    override fun forward(v: Int) { x += v }
    override fun down(v: Int) { y += v }
}
data class realSubmarine(override var x: Int = 0, override var y: Int = 0, var aim: Int = 0): Submarine(x,y) {
    override fun forward(v: Int) {
        x += v
        y += aim * v
    }
    override fun down(v: Int) { aim += v }
}

object Day02_Beauty: SomeDay(2021,2) {
    override val title = "Dive!"

    override fun first(data: String): Any? {
        return data.toStrs()
        .map { it.split(' ') }
        .fold(simpleSubmarine()){
            s, l -> s.goCommand(l.first(),l.last()) as simpleSubmarine
        }.depth()
    } // 2215080 Time: 33ms

    override fun second(data: String): Any? {
        return data.toStrs()
            .map { it.split(' ') }
            .fold(realSubmarine()){
                    s, l -> s.goCommand(l.first(),l.last()) as realSubmarine
            }.depth()
    } // 1864715580 Time: 3ms
}

fun main() = SomeDay.mainify(Day02_Beauty)