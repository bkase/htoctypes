package frontend

import scala.util.parsing.combinator.syntactical.TokenParsers

import frontend.{HAbstractSyntaxTree => AST}
import frontend.HAbstractSyntaxTree.{Unsignable, Longable}
import scala.util.parsing.combinator.PackratParsers

object HParser extends TokenParsers /*with PackratParsers*/ {
  type Tokens = HTokens
  val lexical: Tokens = HLexical

  import lexical._

  def parser: Parser[AST.Root] = phrase (
    rep(structDefinition /* | TODO: Enum, Typedef */) ^^ { case structs => AST.Root(structs) }
  )

  def structDefinition: Parser[AST.Struct] = (
    (Struct ~> ident <~ RCurly) ~
      rep(prop <~ Semicolon)
    <~ LCurly <~ Semicolon ^^ {
      case i ~ props => AST.Struct(i, props)
    }
  )

  def ident: Parser[AST.Ident] = positioned(
    elem("identifier", _.isInstanceOf[Ident])
      ^^ { case Ident(n) => AST.Ident(n) }
  )

  def prop: Parser[AST.Property] = (
      typ ~ ident ^^ { case t ~ i =>  AST.SimpleProperty(t, i)}
    | typ ~ (RParen ~> Star ~> ident <~ LParen) ~
        (RParen ~> repsep(typ, Comma)) ^^ {
        case retTyp ~ i ~ argsTypes => AST.FuncPointer(retTyp, i, argsTypes)
      }
  )

  def unsignedTyp: Parser[AST.Typ with Longable] = (
    Unsigned ~> opt(unsignableTyp) ^^ {
      case typOpt => AST.UnsignedTyp(typOpt)
    }
  )

  def longTyp: Parser[AST.Typ with Longable with Unsignable] = (
    LongType ~> opt(longableTyp) ^^ {
      case typOpt => AST.LongTyp(typOpt)
    }
  )

  def longableTyp: Parser[AST.Typ with Longable] = (
      IntType ^^^ AST.IntType
    | longTyp
    | unsignedTyp
  )

  def unsignableTyp: Parser[AST.Typ with Unsignable] = (
      IntType ^^^ AST.IntType
    | CharType ^^^ AST.CharType
    | longTyp
  )

  def typ: Parser[AST.Typ] = (
      typ <~ Star ^^ { case t => AST.Pointer(t) }
    | FloatType ^^^ AST.FloatType
    | DoubleType ^^^ AST.DoubleType
    | CharType ^^^ AST.CharType
    | VoidType ^^^ AST.VoidType
    | longTyp
    | unsignedTyp
    | Struct ~> ident ^^ { case i => AST.StructType(i) }
  )
}
