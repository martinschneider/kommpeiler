package io.github.martinschneider.orzo.parser;

import static io.github.martinschneider.orzo.TestHelper.args;
import static io.github.martinschneider.orzo.TestHelper.assertTokenIdx;
import static io.github.martinschneider.orzo.lexer.tokens.Token.type;
import static io.github.martinschneider.orzo.lexer.tokens.BasicType.INT;
import static io.github.martinschneider.orzo.util.Factory.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.github.martinschneider.orzo.lexer.Lexer;
import io.github.martinschneider.orzo.lexer.TokenList;
import io.github.martinschneider.orzo.parser.productions.Type;

public class CastParserTest {
  private CastParser target = new CastParser(null);

  private static Stream<Arguments> test() throws IOException {
    return stream(args("", null), args("(int)", Type.of(INT.name().toLowerCase())));
  }

  @ParameterizedTest
  @MethodSource
  public void test(String input, Type expected) throws IOException {
    TokenList tokens = new Lexer().getTokens(input);
    assertEquals(expected, target.parse(tokens));
    assertTokenIdx(tokens, (expected == null));
  }
}
