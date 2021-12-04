val s = "199\n200\n208\n210\n200\n207\n240\n269\n260\n263"
val i = s.split('\n').map(_.toInt).toList
val ss = i.sliding(2).toList
ss
  .collect{
    case List(a1,a2) => a1.compare(a2)
  }
  .count(_ < 0)
// 7
i.sliding(3).map(_.sum).sliding(2)
  .map{
    case Seq(a1,a2) => a1.compare(a2)
  }
  .count(_ < 0)
// 5
i.zip(i.tail)
i.zip(i.drop(3))
