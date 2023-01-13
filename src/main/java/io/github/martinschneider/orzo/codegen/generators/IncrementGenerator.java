package io.github.martinschneider.orzo.codegen.generators;

import static io.github.martinschneider.orzo.codegen.OpCodes.IINC;
import static io.github.martinschneider.orzo.codegen.OpCodes.WIDE;
import static io.github.martinschneider.orzo.codegen.generators.OperatorMaps.ARITHMETIC_OPS;
import static io.github.martinschneider.orzo.codegen.generators.OperatorMaps.DUP_OPS;
import static io.github.martinschneider.orzo.lexer.tokens.Operator.MINUS;
import static io.github.martinschneider.orzo.lexer.tokens.Operator.PLUS;
import static io.github.martinschneider.orzo.lexer.tokens.Operator.POST_DECREMENT;
import static io.github.martinschneider.orzo.lexer.tokens.Operator.PRE_DECREMENT;
import static io.github.martinschneider.orzo.lexer.tokens.Operator.PRE_INCREMENT;
import static io.github.martinschneider.orzo.lexer.tokens.Type.INT;

import io.github.martinschneider.orzo.codegen.CGContext;
import io.github.martinschneider.orzo.codegen.DynamicByteArray;
import io.github.martinschneider.orzo.codegen.HasOutput;
import io.github.martinschneider.orzo.codegen.identifier.VariableInfo;
import io.github.martinschneider.orzo.lexer.tokens.Identifier;
import io.github.martinschneider.orzo.lexer.tokens.Operator;
import io.github.martinschneider.orzo.lexer.tokens.Operator;
import io.github.martinschneider.orzo.parser.productions.IncrementStatement;
import io.github.martinschneider.orzo.parser.productions.Method;

public class IncrementGenerator implements StatementGenerator<IncrementStatement> {
  private CGContext ctx;
  private static final String LOGGER_NAME = "increment code generator";

  public IncrementGenerator(CGContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public HasOutput generate(DynamicByteArray out, Method method, IncrementStatement incr) {
    if (incr.expr.tokens.size() != 2
        || !(incr.expr.tokens.get(0) instanceof Identifier)
        || !(incr.expr.tokens.get(1) instanceof Operator)) {
      ctx.errors.addError(
          LOGGER_NAME,
          "invalid unary increment expression",
          new RuntimeException().getStackTrace());
      return out;
    }
    Identifier id = (Identifier) incr.expr.tokens.get(0);
    Operator op = ((Operator) incr.expr.tokens.get(1)).opValue();
    VariableInfo varInfo = ctx.classIdMap.variables.get(id);
    inc(out, varInfo, op, true);
    return out;
  }

  // if evalOnly==true the variable value will be changed but not put/left on the stack
  public HasOutput inc(
      DynamicByteArray out, VariableInfo varInfo, Operator incrOp, boolean evalOnly) {
    boolean pre = false;
    Operator op = PLUS;
    if (incrOp.equals(PRE_DECREMENT) || incrOp.equals(PRE_INCREMENT)) {
      pre = true;
    }
    if (incrOp.equals(PRE_DECREMENT) || incrOp.equals(POST_DECREMENT)) {
      op = MINUS;
    }
    // special handling for integer because there is IINC
    if (varInfo.type.equals(INT)) {
      return incInt(out, varInfo, op.equals(PLUS) ? (byte) 1 : (byte) -1, pre, evalOnly);
    }
    if (pre) {
      ctx.loadGen.load(out, varInfo);
      ctx.pushGen.push(out, varInfo.type, 1);
      out.write(ARITHMETIC_OPS.get(op).get(varInfo.type));
      if (!evalOnly) {
        out.write(DUP_OPS.get(varInfo.type));
      }
      ctx.storeGen.store(out, varInfo);
    } else {
      ctx.loadGen.load(out, varInfo);
      if (!evalOnly) {
        out.write(DUP_OPS.get(varInfo.type));
      }
      ctx.pushGen.push(out, varInfo.type, 1);
      out.write(ARITHMETIC_OPS.get(op).get(varInfo.type));
      ctx.storeGen.store(out, varInfo);
    }
    return out;
  }

  private HasOutput incInt(
      DynamicByteArray out, VariableInfo varInfo, short val, boolean pre, boolean evalOnly) {
    if (!evalOnly && !pre) {
      ctx.loadGen.load(out, varInfo);
    }
    if (varInfo.idx > Byte.MAX_VALUE || val > Byte.MAX_VALUE) {
      out.write(WIDE);
    }
    out.write(IINC);
    if (varInfo.idx > Byte.MAX_VALUE || val > Byte.MAX_VALUE) {
      out.write(varInfo.idx);
      out.write(val);
    } else {
      out.write((byte) varInfo.idx);
      out.write((byte) val);
    }
    if (!evalOnly && pre) {
      ctx.loadGen.load(out, varInfo);
    }
    return out;
  }
}
