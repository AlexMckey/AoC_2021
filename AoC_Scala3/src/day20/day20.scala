package day20

import AoC_Lib.*

object day20 {

  val Seq(ds,is): Seq[String] = inputStr(day = 20)
    .splitByBlankLines()
  val decoder: String = ds.split('\n').mkString
  val initImage: Map[Pos, Char] = is
    .split('\n')
    .zipWithIndex
    .flatMap((s,y) => s
      .zipWithIndex
      .map((c,x) => Pos(x,y) -> c))
    .toMap
    .withDefaultValue('.')

  def enhanceImage(image: Map[Pos,Char], decode: String = decoder): Map[Pos,Char] = {
    val min = image.keys.reduce(_ min _)
    val max = image.keys.reduce(_ max _)
    val bs = (min-Pos(3,3))
      .all9.map(image).mkString
      .replace('.','0')
      .replace('#','1')
    val i = Integer.parseInt(bs,2)
    val defChar = decode(i)
    (min.x-1 to max.x+1)
      .flatMap { y =>
        (min.y-1 to max.y+1)
          .map { x =>
            val p = Pos(x,y)
            val bs = p.all9.map(image).mkString
              .replace('.','0')
              .replace('#','1')
            val i = Integer.parseInt(bs,2)
            p -> decode(i)
          }
      }.toMap
      .withDefaultValue(defChar)
  }

  def printImage(image: Map[Pos, Char]): Unit = {
    (image.minBy(_._1.x)._1.x-1 to image.maxBy(_._1.x)._1.x+1)
      .foreach{ y =>
        (image.minBy(_._1.y)._1.y-1 to image.maxBy(_._1.y)._1.y+1)
          .foreach{ x =>
            print(image(Pos(x,y)))
          }
        println()
      }
  }

  def part1: Int = {
    val imageFirst = enhanceImage(initImage,decoder)
    val imageSecond = enhanceImage(imageFirst,decoder)
    imageSecond.count(_._2 == '#')
  }

  def part2: Int =
    Iterator.iterate(initImage){ image =>
      enhanceImage(image)
    }.drop(50).next()
      .count(_._2 == '#')

  @main
  def TrenchMap(): Unit = {
    Console.println(part1)     // 5486
    Console.println(part2)     // 20210
  }
}