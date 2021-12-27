abstract class SnailfishNumber {
    companion object {
        var isExploded: Boolean = false
        var isSplited: Boolean = false
        fun parse(s: String): SnailfishNumber {
            tailrec fun rec(pos: Int = 0, level: Int = 0): SnailfishNumber =
                when {
                    s[pos] == '[' -> rec(pos+1,level+1)
                    s[pos] == ']' -> rec(pos+1,level-1)
                    s[pos] == ',' && level == 1 -> Pair(parse(s.slice(1 .. pos)), parse(s.slice(pos+1 until s.length)))
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
        return if (isExploded) exploded.reduce()
        else {
            val splitted = split()
            if (isSplited) splitted.reduce()
            else this
        }
    }
    override fun toString(): String = "[${l.toString()},${r.toString()}]"
}
val s = "[1,2]\n" +
        "[[1,2],3]\n" +
        "[9,[8,7]]\n" +
        "[[1,9],[8,5]]\n" +
        "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]\n" +
        "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]\n" +
        "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]"
val ns = s.lines()
ns.map(SnailfishNumber::parse)
ns.map(SnailfishNumber::parse).map { it.magnitude }
("[[1,2],[[3,4],5]]\n" +
"[[[[0,7],4],[[7,8],[6,0]]],[8,1]]\n" +
"[[[[1,1],[2,2]],[3,3]],[4,4]]\n" +
"[[[[3,0],[5,3]],[4,4]],[5,5]]\n" +
"[[[[5,0],[7,4]],[5,5]],[6,6]]\n" +
"[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    .lines()
    .map(SnailfishNumber::parse)
    .map { it.magnitude }
SnailfishNumber.parse("[[[[[9,8],1],2],3],4]").explode()
SnailfishNumber.parse("[7,[6,[5,[4,[3,2]]]]]").explode()
SnailfishNumber.parse("[[6,[5,[4,[3,2]]]],1]").explode()
SnailfishNumber.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").explode()
SnailfishNumber.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").explode()
SnailfishNumber.parse("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]").reduce()
val nums: List<SnailfishNumber> = ("[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
        "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
        "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
        "[[9,3],[[9,9],[6,[4,9]]]]\n" +
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]")
    .split('\n')
    .map(SnailfishNumber::parse)
val res = nums.reduce(SnailfishNumber::plus)
res
res.magnitude
nums.flatMap { n1 -> nums.map { n2 -> (n1 + n2).magnitude } }.maxOrNull()