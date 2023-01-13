package io.github.martinschneider.orzo.parser;

import static io.github.martinschneider.orzo.TestHelper.args;
import static io.github.martinschneider.orzo.TestHelper.assertTokenIdx;
import static io.github.martinschneider.orzo.lexer.tokens.Token.id;
import static io.github.martinschneider.orzo.lexer.tokens.Type.DOUBLE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.INT;
import static io.github.martinschneider.orzo.util.Factory.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.martinschneider.orzo.error.CompilerErrors;
import io.github.martinschneider.orzo.lexer.Lexer;
import io.github.martinschneider.orzo.lexer.TokenList;
import io.github.martinschneider.orzo.parser.productions.AccessFlag;
import io.github.martinschneider.orzo.parser.productions.ParallelDeclaration;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DeclarationParserTest {
  private DeclarationParser target =
      new DeclarationParser(ParserContext.build(new CompilerErrors()));

  private static Stream<Arguments> test() throws IOException {
    return stream(
        args("int x=100;", pDecl(emptyList(), id("x"), INT, expr("100"))),
        args(
            "static boolean b=true;",
            pDecl(list(AccessFlag.ACC_STATIC), id("b"), "boolean", expr("true"))),
        args(
            "final boolean bool=false;",
            pDecl(list(AccessFlag.ACC_FINAL), id("bool"), "boolean", expr("false"))),
        args(
            "private String martin",
            pDecl(list(AccessFlag.ACC_PRIVATE), id("martin"), "String", null)),
        // args("Demo martin", pDecl(null, id("martin"), "Demo", null)),
        // args("Demo martin = new Demo(1);", pDecl(null, id("martin"), "Demo", expr("new
        // Demo(1)"))),
        // args("com.test.Demo martin", pDecl(null, id("martin"), "com.test.Demo", null)),
        args(
            "protected double d=1.23",
            pDecl(list(AccessFlag.ACC_PROTECTED), id("d"), "double", expr("1.23"))),
        args("int i=fac(100)", pDecl(emptyList(), id("i"), INT, expr("fac(100)"))),
        args(
            "int[] a = new int[] {1, 2, 3};",
            pDecl(
                emptyList(),
                id("a"),
                INT,
                1,
                arrInit(INT, 3, list(expr("1"), expr("2"), expr("3"))))),
        args(
            "int[] a = new int[5];",
            pDecl(emptyList(), id("a"), INT, 1, arrInit(INT, 5, emptyList()))),
        args(
            "int a,b,c=1,2,3;",
            pDecl(
                list(
                    decl(emptyList(), id("a"), INT, expr("1")),
                    decl(emptyList(), id("b"), INT, expr("2")),
                    decl(emptyList(), id("c"), INT, expr("3"))))),
        args(
            "double a,b,c=1,2;",
            pDecl(
                list(
                    decl(emptyList(), id("a"), DOUBLE, expr("1")),
                    decl(emptyList(), id("b"), DOUBLE, expr("2")),
                    decl(emptyList(), id("c"), DOUBLE, null)))));
  }

  @MethodSource
  @ParameterizedTest
  public void test(String input, ParallelDeclaration expected) throws IOException {
    TokenList tokens = new Lexer().getTokens(input);
    assertEquals(expected, target.parse(tokens));
    assertTokenIdx(tokens, (expected == null));
  }
}
