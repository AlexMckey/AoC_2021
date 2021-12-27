val l = listOf(3,4,3,1,2)
val m = l.groupingBy { it }.eachCount()
m[0]
val a: Array<Int> = Array(9){ i -> m.getOrDefault(i,0) }
a.toList()
fun nextState(state: Array<Int>, step: Int = 1): Array<Int> =
    if (step == 0) state
    else {
        val newGen = state[0]
        for (i in 0 .. 7) state[i] = state[i+1]
        state[8] = newGen
        state[6] += newGen
        nextState(state, step - 1)
    }
nextState(a).toList() //1
nextState(a).toList() //2
nextState(a).toList() //3
nextState(a).toList() //4
nextState(a).toList() //5
nextState(a).toList() //6
nextState(a).toList() //7
nextState(a).toList() //8
nextState(a).toList() //9
nextState(a).toList() //10
nextState(a,8).toList() //18
data class LanterFishPopulation(val init: List<Int>){
    val a: Array<Long> = Array(9) { i ->
        val m = init.groupingBy { it }.eachCount()
        (if (m.containsKey(i)) m[i]?.toLong() else 0)!! }
    fun nextDay(step: Int = 1): Array<Long> =
        if (step == 0) a
        else {
            val newGen = a[0]
            for (i in 0 .. 7) a[i] = a[i+1]
            a[8] = newGen
            a[6] += newGen
            nextDay(step - 1)
        }
    fun population(): Long = a.sum()
}
val fishs = LanterFishPopulation(l)
fishs.nextDay(256)
fishs.a.toList()
fishs.population()
