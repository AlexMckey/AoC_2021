package AoC_Lib

object IterableExtensions {
  
  extension [A] (coll: Iterable[A])

    // https://stackoverflow.com/a/2099896
    def cycle: Iterator[A] = Iterator.continually(coll).flatten

    def groupCount[K](key: A => K): Map[K, Int] = coll.groupMapReduce(key)(_ => 1)(_ + _)

    def second: A = coll.tail.head

}
