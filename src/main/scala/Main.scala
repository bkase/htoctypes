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
      |enum x {
      |  RED,
      |  GREEN,
      |  BLUE
      |};
      |
      |struct adb_main_input {
      |  int is_daemon;
      |  int server_port;
      |  int is_lib_call;
      |
      |  int (*spawnIO)(int *);
      |  int (*spawnD)();
      |};
    """.stripMargin
  val reader: Reader[Char] = new CharSequenceReader(test_h)
  val lexer = HLexical.lex(reader)
  HParser.parser(lexer.asInstanceOf[HParser.Input]) match {
    case HParser.Success(root, _) => println(root)
    case f @ HParser.NoSuccess(_, _) => println("Fail " + f)
  }
}
