package frontend

import scala.util.parsing.combinator.token.Tokens

trait HTokens extends Tokens {
  sealed abstract class HToken extends Token

  case class Ident(name: String) extends HToken{ def chars: String = name }

  case object IntType extends HToken { def chars: String = "int" }
  case object FloatType extends HToken { def chars: String = "float" }
  case object DoubleType extends HToken { def chars: String = "double" }
  case object CharType extends HToken { def chars: String = "char" }
  case object VoidType extends HToken { def chars: String = "void" }
  case object LongType extends HToken { def chars: String = "long" }

  case object Const extends HToken { def chars: String = "const" }
  case object Unsigned extends HToken { def chars: String = "unsigned" }
  case object Enum extends HToken { def chars: String = "enum" }
  case object Extern extends HToken { def chars: String = "extern" }
  case object Struct extends HToken { def chars: String = "struct" }

  case object Star extends HToken { def chars: String = "*" }

  case object LParen extends HToken { def chars: String = "(" }
  case object RParen extends HToken { def chars: String = ")" }
  case object LCurly extends HToken { def chars: String = "{" }
  case object RCurly extends HToken { def chars: String = "}" }
  case object LBrack extends HToken { def chars: String = "[" }
  case object RBrack extends HToken { def chars: String = "]" }
  case object Semicolon extends HToken { def chars: String = ";" }
  case object Comma extends HToken { def chars: String = "," }

  case object Varargs extends HToken { def chars: String = "..." }
}
