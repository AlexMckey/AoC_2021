val s = "16,1,2,0,4,2,7,1,2,14"
val ys = s.split(',').map(_.toInt)
val yss = ys.sorted
yss(yss.length/2)
ys.sum / ys.length
val res1 = (ys.min to ys.max).map(i => i -> ys.map(v => (v - i).abs).sum)
res1.minBy(_._2)
def calcSum(delta: Int): Int = delta * (delta + 1) / 2
val res2 = (ys.min to ys.max).map(i => i -> ys.map(v => calcSum((v - i).abs)).sum)
res2.minBy(_._2)