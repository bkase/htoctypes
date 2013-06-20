import frontend.{HLexical, HParser}

import scala.util.parsing.input.{Reader, CharSequenceReader}

object Main extends App {
  println("Hello world")
  val test_h =
    """
      |struct t {
        int x;
        float y;
      };
    """.stripMargin
  val reader: Reader[Char] = new CharSequenceReader(test_h)
  val scanner: HLexical.Scanner = new HLexical.Scanner(reader)

  HParser.parser(scanner.asInstanceOf[HParser.Input]) match {
    case HParser.Success(root, _) => println(root)
    case f @ HParser.NoSuccess(_, _) => println("Fail " + f)
  }
}
