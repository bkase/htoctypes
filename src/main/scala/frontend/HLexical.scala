package frontend

import scala.util.parsing.combinator.lexical.{StdLexical, Lexical, Scanners}
import scala.util.parsing.combinator.token.StdTokens

// TODO: Make the UpgradableTokens API better so we don't need this
object tokens {
  val std = new Object with StdTokens
  val tok = HTokens.tokens
}
object HLexical extends StdLexical with UpgradableTokens[tokens.std.Token, tokens.tok.Token] {
  import tokens._

  Set(
    "int",
    "float",
    "double",
    "short",
    "char",
    "void",
    "long",
    "const",
    "unsigned",
    "enum",
    "extern",
    "struct",
    "typedef"
  ).foreach(reserved += _)

  Set(
    "*",
    ")",
    "(",
    "}",
    "{",
    "[",
    "]",
    ";",
    ",",
    "."
  ).foreach(delimiters += _)

  val transform: (std.Token) => tok.Token =
    (t1) => {
      t1 match {
        case std.Keyword(k) => {
          k match {
            case "int" => tok.IntType
            case "float" => tok.FloatType
            case "double" => tok.DoubleType
            case "char" => tok.CharType
            case "void" => tok.VoidType
            case "long" => tok.LongType
            case "short" => tok.ShortType

            case "const" => tok.Const
            case "unsigned" => tok.Unsigned
            case "enum" => tok.Enum
            case "extern" => tok.Extern
            case "struct" => tok.Struct
            case "typedef" => tok.Typedef

            case "*" => tok.Star
            case ")" => tok.LParen
            case "(" => tok.RParen
            case "}" => tok.LCurly
            case "{" => tok.RCurly
            case "[" => tok.RBrack
            case "]" => tok.LBrack
            case ";" => tok.Semicolon
            case "," => tok.Comma
            case "..." => tok.Varargs

            case _ => tok.errorToken("Bad keyword: " + k)
          }
        }
        case std.Identifier(i) => {
          tok.Ident(i)
        }
        case std.StringLit(s) => tok.errorToken("String literal: " + s + "found")
        case std.NumericLit(n) => tok.errorToken("Numeric literal: " + n + "found")
        case std.ErrorToken(msg) => tok.errorToken(msg)
      }
    }
}
