package backend

object JSTokens {
  sealed abstract class Token {
    def toJS: String
  }

  case object New extends Token { override def toJS = "new" }

  case object Ctypes extends Token { override def toJS = "ctypes" }

  case object Comma extends Token { override def toJS = "," }
  case object Dot extends Token { override def toJS = "." }
  case object Colon extends Token { override def toJS = ":" }

  case object RParen extends Token { override def toJS = "(" }
  case object LParen extends Token { override def toJS = ")" }

  case object RBracket extends Token { override def toJS = "[" }
  case object LBracket extends Token { override def toJS = "]" }

  case class StringLit(s: String) extends Token { override def toJS = "\"" + s + "\"" }
  case class Ident(name: String) extends Token { override def toJS = name }

  case object BoolTyp extends Token { override def toJS = "bool" }
  case object ShortTyp extends Token { override def toJS = "short" }
  case object IntTyp extends Token { override def toJS = "int" }
  case object LongTyp extends Token { override def toJS = "long" }
  case object LongLongTyp extends Token { override def toJS = "long_long" }
  case object UnsignedIntTyp extends Token { override def toJS = "unsigned_int" }
  case object UnsignedShortTyp extends Token { override def toJS = "unsigned_short" }
  case object UnsignedLongTyp extends Token { override def toJS = "unsigned_long" }
  case object UnsignedLongLongTyp extends Token { override def toJS = "unsigned_long_long" }
  case object FloatTyp extends Token { override def toJS = "float" }
  case object DoubleTyp extends Token { override def toJS = "double" }

  case object CharTyp extends Token { override def toJS = "char" }
  case object SignedCharTyp extends Token { override def toJS = "signed_char" }
  case object UnsignedCharTyp extends Token { override def toJS = "unsigned_char" }

  case object SizeTTyp extends Token { override def toJS = "size_t" }
  case object SsizeTTyp extends Token { override def toJS = "ssize_t" }
  case object IntPtrTyp extends Token { override def toJS = "intptr_t" }
  case object UintPtrTyp extends Token { override def toJS = "uintptr_t" }

  case object Jschar extends Token { override def toJS = "jschar" }

  case object VoidTyp extends Token { override def toJS = "void_t" }
  case object VoidPointerTyp extends Token { override def toJS = "voidptr_t" }

  case object Int64Typ extends Token { override def toJS = "int64" }
  case object Uint64Typ extends Token { override def toJS = "uint64" }

  case object StructType extends Token { override def toJS = "StructType" }
  case object PointerType extends Token { override def toJS = "PointerType" }
  case object ArrayType extends Token { override def toJS = "ArrayType" }
  case object FunctionType extends Token { override def toJS = "FunctionType" }

  case object Declare extends Token { override def toJS = "declare" }
}
