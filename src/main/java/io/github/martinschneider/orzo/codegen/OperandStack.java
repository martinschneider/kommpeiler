package io.github.martinschneider.orzo.codegen;

import static io.github.martinschneider.orzo.lexer.tokens.Type.DOUBLE;
import static io.github.martinschneider.orzo.lexer.tokens.Type.LONG;

import java.util.Deque;
import java.util.LinkedList;

public class OperandStack {
  private int maxSize;
  private int size;
  private Deque<String> types = new LinkedList<>();

  public void push(String type) {
    types.push(type);
    size++;
    if (type != null && (type.equals(LONG) || type.equals(DOUBLE))) {
      size++;
    }
    if (size > maxSize) {
      maxSize = size;
    }
  }

  public void pop() {
    if (types.peek() != null && (types.peek().equals(LONG) || types.peek().equals(DOUBLE))) {
      size--;
    }
    size--;
    types.pop();
  }

  public void pop2() {
    pop();
    pop();
  }

  public void pop(int n) {
    for (int i = 0; i < n; i++) {
      pop();
    }
  }

  public String type() {
    return types.peek();
  }

  public int maxSize() {
    return maxSize;
  }
}