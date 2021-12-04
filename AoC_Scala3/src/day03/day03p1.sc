val s = "00100\n11110\n10110\n10111\n10101\n01111\n00111\n11100\n10000\n11001\n00010\n01010"
val bns = s.split('\n').map(_.toCharArray).transpose.toList
val bin = bns.map(_.count(_ == '1').compareTo(bns.head.length/2))
  .map(i => (i+1)/2)
  .mkString
Integer.parseInt(bin,2)
bns.length
math.pow(2,bns.length).toInt-1
(1 << bns.length) - 1
val (gr,er) = bns
  .map(_
    .groupMapReduce(identity)(_ => 1)(_ + _)
    .toList
    .sortBy(_._2)
    .map(_._1)
  ).map(l => l.head -> l.last)
  .unzip
Integer.parseInt(gr.mkString,2)
Integer.parseInt(er.mkString,2)
