package io.github.martinschneider.orzo.codegen.generators;

import static io.github.martinschneider.orzo.codegen.OpCodes.AASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.ASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.ASTORE_0;
import static io.github.martinschneider.orzo.codegen.OpCodes.ASTORE_1;
import static io.github.martinschneider.orzo.codegen.OpCodes.ASTORE_2;
import static io.github.martinschneider.orzo.codegen.OpCodes.ASTORE_3;
import static io.github.martinschneider.orzo.codegen.OpCodes.BASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.DASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.DSTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.DSTORE_0;
import static io.github.martinschneider.orzo.codegen.OpCodes.DSTORE_1;
import static io.github.martinschneider.orzo.codegen.OpCodes.DSTORE_2;
import static io.github.martinschneider.orzo.codegen.OpCodes.DSTORE_3;
import static io.github.martinschneider.orzo.codegen.OpCodes.FASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.FSTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.FSTORE_0;
import static io.github.martinschneider.orzo.codegen.OpCodes.FSTORE_1;
import static io.github.martinschneider.orzo.codegen.OpCodes.FSTORE_2;
import static io.github.martinschneider.orzo.codegen.OpCodes.FSTORE_3;
import static io.github.martinschneider.orzo.codegen.OpCodes.IASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.ISTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.ISTORE_0;
import static io.github.martinschneider.orzo.codegen.OpCodes.ISTORE_1;
import static io.github.martinschneider.orzo.codegen.OpCodes.ISTORE_2;
import static io.github.martinschneider.orzo.codegen.OpCodes.ISTORE_3;
import static io.github.martinschneider.orzo.codegen.OpCodes.LASTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.LSTORE;
import static io.github.martinschneider.orzo.codegen.OpCodes.LSTORE_0;
import static io.github.martinschneider.orzo.codegen.OpCodes.LSTORE_1;
import static io.github.martinschneider.orzo.codegen.OpCodes.LSTORE_2;
import static io.github.martinschneider.orzo.codegen.OpCodes.LSTORE_3;
import static io.github.martinschneider.orzo.codegen.OpCodes.PUTSTATIC;
import static io.github.martinschneider.orzo.codegen.OpCodes.SASTORE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.BOOLEAN;
import static io.github.martinschneider.orzo.lexer.tokens.Type.BYTE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.CHAR;
import static io.github.martinschneider.orzo.lexer.tokens.Type.DOUBLE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.FLOAT;
import static io.github.martinschneider.orzo.lexer.tokens.Type.INT;
import static io.github.martinschneider.orzo.lexer.tokens.Type.LONG;
import static io.github.martinschneider.orzo.lexer.tokens.Type.REF;
import static io.github.martinschneider.orzo.lexer.tokens.Type.SHORT;

import io.github.martinschneider.orzo.codegen.CGContext;
import io.github.martinschneider.orzo.codegen.DynamicByteArray;
import io.github.martinschneider.orzo.codegen.HasOutput;
import io.github.martinschneider.orzo.codegen.VariableInfo;

public class StoreGenerator {
  public CGContext ctx;

  public StoreGenerator(CGContext ctx) {
    this.ctx = ctx;
  }

  private HasOutput store(DynamicByteArray out, String type, short idx) {
    // ctx.basicGen.convert(out, ctx.opStack.type(), type);
    switch (type) {
      case LONG:
        return storeLong(out, idx);
      case FLOAT:
        return storeFloat(out, idx);
      case DOUBLE:
        return storeDouble(out, idx);
      case INT:
        return storeInteger(out, idx);
      case BYTE:
        return storeInteger(out, idx);
      case SHORT:
        return storeInteger(out, idx);
      case CHAR:
        return storeInteger(out, idx);
      case BOOLEAN:
        return storeInteger(out, idx);
      case REF:
        return storeReference(out, idx);
    }
    return out;
  }

  private HasOutput storeLong(HasOutput out, short idx) {
    if (idx == 0) {
      out.write(LSTORE_0);
    } else if (idx == 1) {
      out.write(LSTORE_1);
    } else if (idx == 2) {
      out.write(LSTORE_2);
    } else if (idx == 3) {
      out.write(LSTORE_3);
    } else {
      ctx.basicGen.wide(out, idx, LSTORE);
    }
    return out;
  }

  private HasOutput storeReference(DynamicByteArray out, short idx) {
    if (idx == 0) {
      out.write(ASTORE_0);
    } else if (idx == 1) {
      out.write(ASTORE_1);
    } else if (idx == 2) {
      out.write(ASTORE_2);
    } else if (idx == 3) {
      out.write(ASTORE_3);
    } else {
      ctx.basicGen.wide(out, idx, ASTORE);
    }
    return out;
  }

  private HasOutput storeDouble(DynamicByteArray out, short idx) {
    if (idx == 0) {
      out.write(DSTORE_0);
    } else if (idx == 1) {
      out.write(DSTORE_1);
    } else if (idx == 2) {
      out.write(DSTORE_2);
    } else if (idx == 3) {
      out.write(DSTORE_3);
    } else {
      ctx.basicGen.wide(out, idx, DSTORE);
    }
    return out;
  }

  private HasOutput storeFloat(DynamicByteArray out, short idx) {
    if (idx == 0) {
      out.write(FSTORE_0);
    } else if (idx == 1) {
      out.write(FSTORE_1);
    } else if (idx == 2) {
      out.write(FSTORE_2);
    } else if (idx == 3) {
      out.write(FSTORE_3);
    } else {
      ctx.basicGen.wide(out, idx, FSTORE);
    }
    return out;
  }

  private HasOutput storeInteger(HasOutput out, short idx) {
    if (idx == 0) {
      out.write(ISTORE_0);
    } else if (idx == 1) {
      out.write(ISTORE_1);
    } else if (idx == 2) {
      out.write(ISTORE_2);
    } else if (idx == 3) {
      out.write(ISTORE_3);
    } else {
      ctx.basicGen.wide(out, idx, ISTORE);
    }
    return out;
  }

  public HasOutput storeInArray(HasOutput out, String type) {
    switch (type) {
      case BYTE:
        out.write(BASTORE);
        break;
      case SHORT:
        out.write(SASTORE);
        break;
      case LONG:
        out.write(LASTORE);
        break;
      case INT:
        out.write(IASTORE);
        break;
      case DOUBLE:
        out.write(DASTORE);
        break;
      case FLOAT:
        out.write(FASTORE);
        break;
      case REF:
        out.write(AASTORE);
        break;
    }
    return out;
  }

  private HasOutput putStatic(DynamicByteArray out, short idx) {
    out.write(PUTSTATIC);
    out.write(idx);
    return out;
  }

  public HasOutput store(DynamicByteArray out, VariableInfo variableInfo) {
    if (variableInfo.isField) {
      return putStatic(out, variableInfo.idx);
    }
    return store(out, variableInfo.type, variableInfo.idx);
  }
}
