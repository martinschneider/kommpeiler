package io.github.martinschneider.kommpeiler.parser;

import static io.github.martinschneider.kommpeiler.scanner.tokens.Symbols.LBRAK;
import static io.github.martinschneider.kommpeiler.scanner.tokens.Symbols.RBRAK;
import static io.github.martinschneider.kommpeiler.scanner.tokens.Token.sym;

import io.github.martinschneider.kommpeiler.parser.productions.ArraySelector;
import io.github.martinschneider.kommpeiler.parser.productions.Expression;
import io.github.martinschneider.kommpeiler.scanner.TokenList;
import java.util.ArrayList;
import java.util.List;

public class ArraySelectorParser implements ProdParser<ArraySelector> {
  private ParserContext ctx;

  public ArraySelectorParser(ParserContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public ArraySelector parse(TokenList tokens) {
    return parse(tokens, false);
  }

  public ArraySelector parse(TokenList tokens, boolean removeTokens) {
    List<Expression> expressions = new ArrayList<>();
    Expression expr;
    int idx = tokens.idx();
    while (tokens.curr().eq(sym(LBRAK))) {
      tokens.next();
      if ((expr = ctx.exprParser.parse(tokens)) != null) {
        expressions.add(expr);
        if (!tokens.curr().eq(sym(RBRAK))) {
          ctx.errors.addParserError("missing ] in array selector");
        }
        tokens.next();
      } else {
        tokens.prev();
        return null;
      }
    }
    if (expressions.isEmpty()) {
      return null;
    }
    // this is used when called from the context of an expression
    if (removeTokens) {
      tokens.list().subList(idx, tokens.idx()).clear();
    }
    return new ArraySelector(expressions);
  }
}