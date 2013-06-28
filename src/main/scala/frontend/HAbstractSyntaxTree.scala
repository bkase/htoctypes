package frontend

import scala.util.parsing.input.Positional

object HAbstractSyntaxTree {
  sealed abstract class Node
  trait Typish // a type or a struct (either anonymous or not)

  case class Root(defs: List[TypeDefinition]) extends Node

  sealed abstract class TypeDefinition extends Node
  case class Struct(identOpt: Option[Ident], props: List[Property]) extends TypeDefinition with Typish
  case class Typedef(original: Node with Typish, alias: Ident) extends TypeDefinition
  // TODO: Support enums that are assigned to ints
  case class Enum(ident: Ident, choices: List[Ident]) extends TypeDefinition
  case class Function(returnTyp: Typ, abi: Abi, ident: Ident, args: List[Property]) extends TypeDefinition

  sealed abstract class Abi extends Node
  case object Cdecl extends Abi
  case object Stdcall extends Abi

  sealed abstract class Property extends Node
  case class SimpleProperty(typ: Typ, ident: Ident) extends Property
  case class FuncPointer(retTyp: Typ, ident: Ident, argsTypes: List[Typ]) extends Property

  sealed abstract class Typ extends Node with Typish
  case class Pointer(typ: Typ) extends Typ
  case class StructType(ident: Ident) extends Typ
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

  case class Ident(name: String) extends Typ with Positional
}
