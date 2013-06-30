import backend.CtypesWriter
import core.ASTTransformer
import frontend.{HLexical, HParser}

import java.io.{File, FileReader}
import scala.io.Source
import scala.util.parsing.input.{Reader, CharSequenceReader}

object Main extends App {
  if (args.length < 1) {
    println("Usage: htoctypes file1.h [file2.h ...]")
    sys.exit(1)
  }

  val files = args.map{ case arg => Source.fromFile(arg) }
  val readers: Array[Reader[Char]] = files.map{ case f => new CharSequenceReader(f.getLines().mkString("\n")) }
  files.foreach{ case f => f.close() }
  val lexers = readers.map{ case r => HLexical.lex(r) }
  lexers.foreach{ case l =>
    HParser.parser(l.asInstanceOf[HParser.Input]) match {
      case HParser.Success(root, _) => {
        println(CtypesWriter.write(ASTTransformer.transformRoot(root)))
        //println(CtypesWriter.write(Root(List(Typedef(Ident("x"), Ident("y")), Typedef(Ident("z"), Ident("o"))))))
      }
      case f @ HParser.NoSuccess(_, _) => println("Fail " + f)
    }
  }
}
