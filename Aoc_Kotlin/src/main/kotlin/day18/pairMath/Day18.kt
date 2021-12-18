package day18.pairMath

import AoC_Lib.*

object Day18: SomeDay(2021, 18) {
    override val title = "Snailfish"

    abstract class SnailfishNumber {
        companion object {
            var isExploded: Boolean = false
            var isSplited: Boolean = false
            fun parse(s: String): SnailfishNumber {
                tailrec fun rec(pos: Int = 0, level: Int = 0): SnailfishNumber =
                    when {
                        s[pos] == '[' -> rec(pos+1,level+1)
                        s[pos] == ']' -> rec(pos+1,level-1)
                        s[pos] == ',' && level == 1 -> Pair(
                            parse(s.slice(1 .. pos)),
                            parse(s.slice(pos+1 until s.length)))
                        s[pos].isDigit() && level == 0 -> Num(s[pos].digitToInt())
                        else -> rec(pos+1,level)
                    }
                return rec()
            }
        }
        abstract val magnitude: Int
        abstract fun split(): SnailfishNumber
        abstract fun accumulate(vl: Int, vr: Int): SnailfishNumber
        abstract fun explode(depth: Int): Triple<SnailfishNumber, Int, Int>
        abstract fun reduce(): SnailfishNumber
        fun explode(): SnailfishNumber {
            isExploded = false
            isSplited = false
            return explode(0).first
        }
        operator fun plus(other: SnailfishNumber): SnailfishNumber = Pair(this,other).reduce()
    }

    data class Num(var n: Int = 0): SnailfishNumber(){
        override val magnitude: Int
            get() = n
        override fun split(): SnailfishNumber =
            if (n >= 10 && !isSplited) {
                isSplited = true
                Pair(Num(n / 2), Num(n - n / 2))}
            else this
        override fun accumulate(vl: Int, vr: Int): SnailfishNumber =
            Num(n + vl + vr)
        override fun explode(depth: Int): Triple<SnailfishNumber,Int,Int> =
            Triple(Num(n),0,0)
        override fun reduce(): SnailfishNumber = this
        override fun toString(): String = n.toString()
    }

    data class Pair(var l: SnailfishNumber,
                    var r: SnailfishNumber): SnailfishNumber() {
        override val magnitude: Int
            get() = 3 * l.magnitude + 2 * r.magnitude
        override fun split(): SnailfishNumber {
            val nl = l.split()
            val nr = r.split()
            return Pair(nl, nr)
        }
        override fun accumulate(vl: Int, vr: Int): SnailfishNumber =
            Pair(l.accumulate(vl, 0), r.accumulate(0, vr))
        override fun explode(depth: Int): Triple<SnailfishNumber,Int,Int> = when {
            l is Num && r is Num && depth >= 4 && !isExploded -> {
                isExploded = true
                Triple(Num(0), (l as Num).n, (r as Num).n)
            }
            else -> {
                val (lt,ll,lr) = l.explode(depth+1)
                val (rt,rl,rr) = r.explode(depth+1)
                Triple(Pair(lt.accumulate(0,rl),rt.accumulate(lr,0)),ll,rr)
            }
        }
        override fun reduce(): SnailfishNumber {
            val exploded = explode()
            return if (isExploded)
                exploded.reduce()
            else {
                val splitted = split()
                if (isSplited)
                    splitted.reduce()
                else this
            }
        }
        override fun toString(): String = "[$l,$r]"
    }

    override fun first(data: String): Any? =
        data.toStrs()
            .map(SnailfishNumber::parse)
            .reduce(SnailfishNumber::plus)
            .magnitude
    //

    override fun second(data: String): Any? {
        val nums = data.toStrs().map(SnailfishNumber::parse)
        return nums
            .flatMap { n1 -> nums
                .map { n2 -> (n1 + n2).magnitude } }
            .maxOrNull()
    } //
}

fun main() = SomeDay.mainify(Day18)