import backend.CtypesWriter
import core.ASTTransformer
import frontend.{HLexical, HParser}

import scala.util.parsing.input.{Reader, CharSequenceReader}

object Main extends App {
  val test_h =
    """
      |typedef struct yo { int z; } pro;
      |typedef struct { int z; } pro2;
      |typedef int x;
      |typedef x y;
      |
      |int some_function(char x, int y);
      |
      |struct adb_main_input {
      |  int is_daemon;
      |  int server_port;
      |  const int is_lib_call;
      |
      |  int (*spawnIO)(int *, char**);
      |  int (*spawnD)();
      |};
    """.stripMargin
  val reader: Reader[Char] = new CharSequenceReader(test_h)
  val lexer = HLexical.lex(reader)
  HParser.parser(lexer.asInstanceOf[HParser.Input]) match {
    case HParser.Success(root, _) => {
      println(CtypesWriter.write(ASTTransformer.transformRoot(root)))
      //println(CtypesWriter.write(Root(List(Typedef(Ident("x"), Ident("y")), Typedef(Ident("z"), Ident("o"))))))
    }
    case f @ HParser.NoSuccess(_, _) => println("Fail " + f)
  }
}
