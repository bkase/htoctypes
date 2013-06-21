package frontend

import scala.util.parsing.input.{Position, Reader}
import scala.util.parsing.combinator.lexical.Scanners

trait UpgradableTokens[T1, T2] {
  self: Scanners =>
  val transform: T1 => T2

  def lex(r: Reader[Char]): Reader[T2] = new Converter(new self.Scanner(r).asInstanceOf[Reader[T1]])

  private class Converter(in: Reader[T1]) extends Reader[T2] {

    val firstElem = transform(in.first)
    def first: T2 = firstElem

    def rest: Reader[T2] = new Converter(in.rest)

    def pos: Position = in.pos

    def atEnd: Boolean = in.atEnd

    override def offset: Int = in.offset
  }
}
