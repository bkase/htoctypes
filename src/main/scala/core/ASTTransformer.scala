package core

import frontend.{HAbstractSyntaxTree => H}
import backend.{JSAbstractSyntaxTree => JS}

object ASTTransformer {

  def transformTyp(typ: H.Typ): JS.Typ = {
    typ match {
      case H.Pointer(typP) => JS.Pointer(transformTyp(typP))
      case H.StructType(ident) =>
        JS.Struct(JS.StringLit(transformIdent(ident)), None)
      case H.IntType => JS.IntType
      case H.ShortType => JS.ShortType
      case H.FloatType => JS.FloatType
      case H.DoubleType => JS.DoubleType
      case H.CharType => JS.CharType
      case H.VoidType => JS.VoidType
      case H.LongTyp(None) => JS.LongTyp(None)
      case H.LongTyp(Some(typP)) => JS.LongTyp(Some(transformTyp(typP)))
      case H.UnsignedTyp(None) => JS.UnsignedTyp(None)
      case H.UnsignedTyp(Some(typP)) => JS.UnsignedTyp(Some(transformTyp(typP)))
      case i @ H.Ident(name) => transformIdent(i)
    }
  }

  def transformProperty(prop: H.Property): JS.Property = {
    prop match {
      case H.SimpleProperty(typ, ident) =>
        JS.Property(JS.StringLit(transformIdent(ident)), transformTyp(typ))
      case H.FuncPointer(retTyp, ident, argsTypes) =>
        JS.Property(JS.StringLit(transformIdent(ident)),
           JS.FuncPointer(JS.Cdecl, transformTyp(retTyp), argsTypes.map(transformTyp _)))
    }
  }

  def transformIdent(ident: H.Ident): JS.Ident = {
    ident match {
      case H.Ident(name) => JS.Ident(name)
    }
  }

  def transformAbi(abi: H.Abi): JS.Abi = {
    abi match {
      case H.Cdecl => JS.Cdecl
      case H.Stdcall => JS.Stdcall
    }
  }

  def typedefStruct(alias: H.Ident, ident: H.Ident, props: List[H.Property]): JS.Typedef =
    JS.Typedef(transformIdent(alias),
               JS.Struct(JS.StringLit(transformIdent(ident)), Some(props.map(transformProperty _))))

  def transformDefinition(node: H.TypeDefinition): JS.TypeDefinition = {
    node match {
      case H.Struct(identOpt, props) => identOpt match {
        case Some(ident) => typedefStruct(ident, ident, props)
        case None => {
          val ident = H.Ident("generated_" + Math.random())
          typedefStruct(ident, ident, props)
        }
      }
      case H.Function(retTyp, abi, ident, args) => {
        val typs = args.map(transformProperty _).map{ case JS.Property(_, t) => t }
        JS.Function(JS.StringLit(transformIdent(ident)), transformAbi(abi), transformTyp(retTyp), typs)
      }
      case H.Typedef(original, alias) => {
        original match {
            // TODO: is struct correct?
          case H.Struct(None, props) => typedefStruct(alias, alias, props)
          case H.Struct(Some(ident), props) => typedefStruct(alias, ident, props)
          case t: H.Typ => JS.Typedef(transformIdent(alias), transformTyp(t))
        }
      }
      case H.Enum(_, _) => ???
    }
  }

  def transformRoot(node: H.Root): JS.Root = {
    node match {
      case H.Root(defs: List[H.TypeDefinition]) => JS.Root(defs.map(transformDefinition(_)))
    }
  }
}
