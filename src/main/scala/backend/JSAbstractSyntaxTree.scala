package backend

object JSAbstractSyntaxTree {
  sealed abstract class Node

  case class Root(defs: List[TypeDefinition]) extends Node

  sealed abstract class TypeDefinition extends Node
  case class Typedef(ident: Ident, original: Typ) extends TypeDefinition
  case class Function(ident: Ident, abi: Abi, returnTyp: Typ, argTypes: List[Typ]) extends TypeDefinition
  // case class Enum(ident: Ident, choices: List[Ident]) extends TypeDefinition

  sealed abstract class Abi extends Node
  case object Cdecl extends Abi
  case object Stdcall extends Abi

  case class Property(ident: Ident, typ: Typ) extends Node

  sealed abstract class Typ extends Node
  case class Pointer(typ: Typ) extends Typ
  case class Struct(ident: Ident, propsOpt: Option[List[Property]]) extends Typ
  case class FuncPointer(abi: Abi, retTyp: Typ, argsTypes: List[Typ]) extends Typ
  // TODO: Enum type

  trait Longable
  trait Unsignable
  case object IntType extends Typ with Unsignable with Longable
  case object ShortType extends Typ with Unsignable
  case object FloatType extends Typ
  case object DoubleType extends Typ
  case object CharType extends Typ with Unsignable
  case object VoidType extends Typ

  case class LongTyp(typOpt: Option[Typ with Longable]) extends Typ with Unsignable with Longable
  case class UnsignedTyp(typOpt: Option[Typ with Unsignable]) extends Typ with Longable

  case class Ident(name: String) extends Typ
}
