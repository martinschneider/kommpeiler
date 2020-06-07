package io.github.martinschneider.orzo.parser;

import static io.github.martinschneider.orzo.lexer.tokens.Token.cmp;
import static io.github.martinschneider.orzo.parser.TestHelper.assertTokenIdx;
import static io.github.martinschneider.orzo.parser.TestHelper.cond;
import static io.github.martinschneider.orzo.parser.TestHelper.exp;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.martinschneider.orzo.error.CompilerErrors;
import io.github.martinschneider.orzo.lexer.Lexer;
import io.github.martinschneider.orzo.lexer.TokenList;
import io.github.martinschneider.orzo.lexer.tokens.Comparators;
import io.github.martinschneider.orzo.parser.productions.Condition;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ConditionParserTest {
  private ConditionParser target = new ConditionParser(ParserContext.build(new CompilerErrors()));

  private static Stream<Arguments> test() throws IOException {
    return Stream.of(
        Arguments.of("", null),
        Arguments.of("y=0", null),
        Arguments.of("y==0", cond(exp("y"), cmp(Comparators.EQUAL), exp("0"))),
        Arguments.of("x<=z", cond(exp("x"), cmp(Comparators.SMALLEREQ), exp("z"))),
        Arguments.of("abc<xyz", cond(exp("abc"), cmp(Comparators.SMALLER), exp("xyz"))),
        Arguments.of(
            "basketball > football",
            cond(exp("basketball"), cmp(Comparators.GREATER), exp("football"))));
  }

  @ParameterizedTest
  @MethodSource
  public void test(String input, Condition expected) throws IOException {
    TokenList tokens = new Lexer().getTokens(input);
    assertEquals(expected, target.parse(tokens));
    assertTokenIdx(tokens, (expected == null));
  }
}