package backend

import frontend.HAbstractSyntaxTree._
import scala.annotation.tailrec

object CtypesWriter {

  def declare(x: Ident): String =
    "const " + write(x) + " = "

  val ctypesd = "ctypes."

  val newc = "new " + ctypesd

  def write(node: Node): String = {
    node match {
      case Root(defns) =>
        defns.map(write).reduce( (a, b) => a + ";\n" + b ) + ";"

      case Struct(ident, body) => {
        def structBody(props: List[Property]): String =
          "{\n" + body.map(write _).reduce(_ + ";\n" + _) + "};"

        ident match {
          case None => newc + "StructType(" + structBody(body) + ");"
          case Some(i) => newc + "StructType(" + declare(i) + structBody(body) + ");"
        }
      }
      case Typedef(original, alias) =>
        declare(alias) + write(original)
      case Enum(ident, choices) => ???
      case Function(returnTyp, abi, ident, args) =>
        declare(ident) + ctypesd + "declare(" +
          (write(ident) :: write(abi) :: write(returnTyp) :: args.map(write _)).reduce(_ + ", " + _) +
          ");"

      // abi
      case Cdecl => ctypesd + "default_abi"
      case Stdcall => ???

      // properties
      case SimpleProperty(typ, ident) => ctypesd + write(typ)
      case FuncPointer(retTyp, ident, argTyps) => ???

      case Ident(x) => "\"" + x + "\""
    }
  }
}
