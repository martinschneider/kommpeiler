package io.github.martinschneider.orzo.codegen;

import io.github.martinschneider.orzo.parser.productions.AccessFlag;
import java.util.List;

public class VariableInfo {
  public String name;
  public String type;
  public boolean isField;
  public List<AccessFlag> accFlags;
  public String arrType; // for arrays, type is reference and arrtype is the type of elements in
  // the array
  public short idx;
  // TODO: support other objects than this
  public short objectRef = 0;

  public VariableInfo(
      String name, String type, List<AccessFlag> accFlags, boolean isField, short idx) {
    this(name, type, null, accFlags, isField, idx);
  }

  public VariableInfo(
      String name,
      String type,
      String arrayType,
      List<AccessFlag> accFlags,
      boolean isField,
      short idx) {
    this.name = name;
    this.type = type;
    this.arrType = arrayType;
    this.accFlags = accFlags;
    this.isField = isField;
    this.idx = idx;
  }

  @Override
  public String toString() {
    return "VAR[name=" + name + ", type=" + type + ", arrtype=" + arrType + ", idx=" + idx + "]";
  }
}
