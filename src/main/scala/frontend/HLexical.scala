package frontend

import scala.util.parsing.combinator.lexical.{Lexical, Scanners}


object HLexical extends Lexical with HTokens {
  type Token = HLexical.HToken

  def identifier: Parser[Any] = (
    (letter | '_') ~ rep(letter | '_' | digit)
  )

  def pickReserved(possiblyReserved: String): Token = {
    possiblyReserved match {
      case "int" => IntType
      case "float" => FloatType
      case "double" => DoubleType
      case "char" => CharType
      case "void" => VoidType
      case "long" => LongType

      case "const" => Const
      case "unsigned" => Unsigned
      case "enum" => Enum
      case "extern" => Extern
      case "struct" => Struct
      case _ => Ident(possiblyReserved)
    }
  }

  def token: Parser[Token] = (
      identifier ^^ { case chars => pickReserved(chars.toString) }
    | '*' ^^^ Star
    | ')' ^^^ LParen
    | '(' ^^^ RParen
    | '}' ^^^ LCurly
    | '{' ^^^ RCurly
    | '[' ^^^ RBrack
    | ']' ^^^ LBrack
    | ';' ^^^ Semicolon
    | ',' ^^^ Comma
    | '.' ~ '.' ~ '.' ^^^ Varargs
  )

  def whitespace: Parser[Any] = (
    // TODO: Comments
      rep1(whitespaceChar)
  )
}
