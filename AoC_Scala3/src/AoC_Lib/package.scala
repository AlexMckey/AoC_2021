import scala.io.Source
import scala.util.Using

package object AoC_Lib {
  //val DataDir = """d:\YandexDisk\DevsExercises\AdventOfCode\2021\PuzzleInput\"""
  val DataDir = """d:\DevsExercises\AdventOfCode\2021\PuzzleInput"""
  def input(day: Int): Iterator[String] = Using.Manager{ use => use(Source.fromFile(f"$DataDir/input$day%02d.txt", "utf-8")).getLines}.get
  def inputStr(day: Int): String = Using.Manager{ use => use(Source.fromFile(f"$DataDir/input$day%02d.txt", "utf-8")).mkString.trim}.get
  def inputInts(day: Int): Seq[Int] = Using.Manager{ use => use(Source.fromFile(f"$DataDir/input$day%02d.txt", "utf-8")).getLines.map(_.toInt).toList}.get
  def inputStrs(day: Int): Seq[String] = Using.Manager{ use => use(Source.fromFile(f"$DataDir/input$day%02d.txt", "utf-8")).getLines.toList}.get
  extension (s: String)
    def splitByBlankLines(): Seq[String] = s.split("\n\n")
}
