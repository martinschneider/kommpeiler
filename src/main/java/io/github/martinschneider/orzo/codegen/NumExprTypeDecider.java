package io.github.martinschneider.orzo.codegen;

import static io.github.martinschneider.orzo.lexer.tokens.Type.BYTE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.CHAR;
import static io.github.martinschneider.orzo.lexer.tokens.Type.DOUBLE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.FLOAT;
import static io.github.martinschneider.orzo.lexer.tokens.Type.INT;
import static io.github.martinschneider.orzo.lexer.tokens.Type.LONG;
import static io.github.martinschneider.orzo.lexer.tokens.Type.SHORT;

import io.github.martinschneider.orzo.lexer.tokens.DoubleNum;
import io.github.martinschneider.orzo.lexer.tokens.Identifier;
import io.github.martinschneider.orzo.lexer.tokens.IntNum;
import io.github.martinschneider.orzo.lexer.tokens.Token;
import io.github.martinschneider.orzo.parser.productions.Expression;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class NumExprTypeDecider {
  public String getType(VariableMap variables, Expression expr) {
    Set<String> types = new HashSet<>();
    for (Token token : expr.tokens) {
      if (token instanceof IntNum) {
        long intValue = ((BigInteger) token.val).longValue();
        if (intValue > Integer.MAX_VALUE) {
          types.add(LONG);
        } else {
          types.add(INT);
        }
      } else if (token instanceof DoubleNum) {
        types.add(DOUBLE);
      } else if (token instanceof Identifier) {
        Identifier id = (Identifier) token;
        VariableInfo var = variables.get(token);
        if (var != null) {
          if (id.arrSel != null) {
            types.add(var.arrType);
          } else {
            types.add(var.type);
          }
        }
      }
    }
    return getSmallestType(types);
  }

  private String getSmallestType(Set<String> types) {
    if (types.contains(DOUBLE)) {
      return DOUBLE;
    } else if (types.contains(FLOAT)) {
      return FLOAT;
    } else if (types.contains(LONG)) {
      return LONG;
    } else if (types.contains(INT)) {
      return INT;
    } else if (types.contains(SHORT)) {
      return SHORT;
    } else if (types.contains(BYTE)) {
      return BYTE;
    } else {
      return CHAR;
    }
  }
}