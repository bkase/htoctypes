package frontend

import scala.util.parsing.combinator.syntactical.TokenParsers

import frontend.{HAbstractSyntaxTree => AST}
import frontend.HAbstractSyntaxTree.{Unsignable, Longable}
// such that left-recursive grammars
import scala.util.parsing.combinator.PackratParsers

object HParser extends TokenParsers with PackratParsers {
  type Tokens = HTokens.HTokens
  val lexical: Tokens = HTokens.tokens

  import lexical._

  lazy val parser: PackratParser[AST.Root] = phrase (
    rep(
      (structDefinition | typedefDefinition | enumDefinition | functionDeclaration ) <~ Semicolon
    ) ^^ { case structs => AST.Root(structs) }
  )

  lazy val functionDeclaration: PackratParser[AST.Function] = (
    typ ~ (ident <~ RParen) ~ (repsep(prop, Comma) <~ LParen) ^^ {
      case r ~ i ~ args => AST.Function(r, AST.Cdecl, i, args)
    }
  )

  lazy val enumDefinition: PackratParser[AST.Enum] = (
    (Enum ~> ident <~ RCurly) ~ repsep(ident, Comma) <~ LCurly ^^ {
      case i ~ cs => AST.Enum(i, cs)
    }
  )

  lazy val typedefDefinitionP: PackratParser[AST.Typedef] = (
      typ ~ ident ^^ { case t ~ i => AST.Typedef(t, i) }
    | structDefinition ~ ident ^^ { case s ~ i => AST.Typedef(s, i)}
    | Struct ~> structBody ~ ident ^^ { case b ~ i => AST.Typedef(AST.Struct(None, b), i) }
  )

  lazy val typedefDefinition: PackratParser[AST.Typedef] = (
    Typedef ~> typedefDefinitionP
  )

  lazy val structBody: PackratParser[List[AST.Property]] = (
    RCurly ~>
      (rep(prop <~ Semicolon))
    <~ LCurly ^^ {
      case props => props
    }
  )

  lazy val structDefinition: PackratParser[AST.Struct] = (
    Struct ~> ident ~ structBody ^^ {
      case i ~ b => AST.Struct(Some(i), b)
    }
  )

  lazy val ident: PackratParser[AST.Ident] = positioned(
    elem("identifier", _.isInstanceOf[Ident])
      ^^ { case Ident(n) =>  AST.Ident(n) }
  )

  lazy val functionPtr: PackratParser[AST.Property] = (
    typ ~ (RParen ~> Star ~> ident <~ LParen) ~
      (RParen ~> repsep(typ, Comma) <~ LParen) ^^ {
      case retTyp ~ i ~ argsTypes => AST.FuncPointer(retTyp, i, argsTypes)
    }
  )

  lazy val prop: PackratParser[AST.Property] = (
      typ ~ ident ^^ { case t ~ i =>  AST.SimpleProperty(t, i)}
    | functionPtr
  )

  lazy val unsignedTyp: PackratParser[AST.Typ with Longable] = (
    Unsigned ~> opt(unsignableTyp) ^^ {
      case typOpt => AST.UnsignedTyp(typOpt)
    }
  )

  lazy val longTyp: PackratParser[AST.Typ with Longable with Unsignable] = (
    LongType ~> opt(longableTyp) ^^ {
      case typOpt => AST.LongTyp(typOpt)
    }
  )

  lazy val longableTyp: PackratParser[AST.Typ with Longable] = (
      IntType ^^^ AST.IntType
    | longTyp
    | unsignedTyp
  )

  lazy val unsignableTyp: PackratParser[AST.Typ with Unsignable] = (
      IntType ^^^ AST.IntType
    | CharType ^^^ AST.CharType
    | ShortType ^^^ AST.ShortType
    | longTyp
  )

  lazy val typ: PackratParser[AST.Typ] =  (
      typ <~ Star ^^ { case t => AST.Pointer(t) }
    | FloatType ^^^ AST.FloatType
    | DoubleType ^^^ AST.DoubleType
    | CharType ^^^ AST.CharType
    | VoidType ^^^ AST.VoidType
    | ShortType ^^^ AST.ShortType
    | IntType ^^^ AST.IntType
    | longTyp
    | unsignedTyp
    | Struct ~> ident ^^ { case i => AST.StructType(i) }
    | Const ~> typ
    | ident
  )
}
