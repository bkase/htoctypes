package backend

import backend.JSAbstractSyntaxTree._
import scala.annotation.tailrec

object CtypesWriter {

  private def declare(x: Ident): String =
    "const " + write(x) + " = "

  val ctypesd = "ctypes."

  val newc = "new " + ctypesd

  def write(node: Node): String = {
    node match {
      case Root(defns) =>
        defns.map(write).reduce( _ + ";\n" + _ ) + ";"

      case Typedef(alias, original) =>
        declare(alias) + write(original)
      // case Enum(ident, choices) => ???
      case Function(s @ StringLit(ident), abi, returnTyp, args) =>
        declare(ident) + ctypesd + "declare(" +
          write(s) + ", " + write(abi) + ", " + write(returnTyp) + args.map(write _).foldLeft("")(_ + ", " + _) +
          ")"

      // abi
      case Cdecl => ctypesd + "default_abi"
      case Stdcall => ???

      // property
      case Property(name, typ) => "{ " + write(name) + ": " + write(typ) + " }"

      // typs
      case Pointer(typ) => write(typ) + ".ptr"
      case Struct(name, propsOpt) => {
        def structBody(props: List[Property]): String =
          "[\n  " + props.map(write _).reduce(_ + ",\n  " + _) + "\n]"

        propsOpt match {
          case None =>
            newc + "StructType(" + write(name) + ")"
          case Some(props) =>
            newc + "StructType(" + write(name) + ", " + structBody(props) + ")"
        }
      }
      case FuncPointer(abi, retTyp, argsTyps) => {
        ctypesd + "FunctionType(" +
          write(abi) + ", " + write(retTyp) +
          argsTyps.map(write _).foldLeft("")(_ + ", " + _) + ")"
      }

      case IntType => ctypesd + "int"
      case ShortType => ctypesd + "int16_t"
      case FloatType => ctypesd + "float32_t"
      case DoubleType => ctypesd + "float64_t"
      case CharType => ctypesd + "char"
      case VoidType => ctypesd + "void_t"

      case LongTyp(typ) => typ match {
        case None | Some(IntType) => ctypesd + "int64_t"
        case Some(LongTyp(None)) => ??? // TODO: How do you do this in ctypes?
        case Some(UnsignedTyp(None)) | Some(UnsignedTyp(Some(IntType))) => ctypesd + "uint64_t"
        case Some(_) => throw new RuntimeException("Malformed long type: " + typ)
      }
      case UnsignedTyp(typ) => typ match {
        case None | Some(IntType) => ctypesd + "uint32_t"
        case Some(ShortType) => ctypesd + "uint16_t"
        case Some(CharType) => ctypesd + "uint8_t"
        case Some(LongTyp(None)) | Some(LongTyp(Some(IntType))) => ctypesd + "uint64_t"
        case Some(_) => throw new RuntimeException("Malformed unsigned type: " + typ)
      }

      case StringLit(i) => "\"" + write(i) + "\""
      case Ident(x) => x
    }
  }
}
