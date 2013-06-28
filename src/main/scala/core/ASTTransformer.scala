package core

import frontend.{HAbstractSyntaxTree => H}
import backend.{JSAbstractSyntaxTree => JS}

object ASTTransformer {

  def transformTyp(typ: H.Typ): JS.Typ = {

  }

  def transformProperty(prop: H.Property): JS.Property = {
    prop match {
      case H.SimpleProperty(typ, ident) => JS.Property(transformIdent(ident), transformTyp(typ))
      case H.FuncPointer(retTyp, ident, argsTypes) =>
        JS.Property(transformIdent(ident),
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

  def transformDefinition(node: H.TypeDefinition): JS.TypeDefinition = {
    node match {
      case H.Struct(identOpt, props) => identOpt match {
        case Some(ident) =>
          JS.Typedef(transformIdent(ident),
                     JS.Struct(transformIdent(ident), Some(props.map(transformProperty _))))
        case None => x
      }
      case H.Function(retTyp, abi, ident, args) => {
        val typs = args.map(transformProperty _).map{ case JS.Property(_, t) => t }
        JS.Function(transformIdent(ident), transformAbi(abi), transformTyp(retTyp), typs)
      }
    }
  }

  def transformRoot(node: H.Root): JS.Root = {
    node match {
      case H.Root(defs: List[H.TypeDefinition]) => JS.Root(defs.map(transformDefinition(_)))
    }
  }
}
