package io.github.martinschneider.orzo.codegen;

import static io.github.martinschneider.orzo.TestHelper.args;
import static io.github.martinschneider.orzo.codegen.ByteUtils.shortToByteArray;
import static io.github.martinschneider.orzo.util.Factory.clazz;
import static io.github.martinschneider.orzo.util.Factory.enumm;
import static io.github.martinschneider.orzo.util.Factory.iface;
import static io.github.martinschneider.orzo.util.Factory.stream;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.github.martinschneider.orzo.error.CompilerErrors;
import io.github.martinschneider.orzo.parser.productions.Clazz;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
public class CodeGeneratorTest {
  public CodeGenerator target = new CodeGenerator(emptyList(), emptyList(), new CompilerErrors());

  @BeforeEach
  public void init() {
    target.out = new Output();
  }

  private static Stream<Arguments> testAccessModifiers() {
    return stream(
        args(
            clazz(
                null,
                emptyList(),
                null,
                "Empty",
                emptyList(),
                Clazz.JAVA_LANG_OBJECT,
                emptyList(),
                emptyList()),
            (short) 0x0021),
        args(
            iface(
                null, emptyList(), null, "Empty", Clazz.JAVA_LANG_OBJECT, emptyList(), emptyList()),
            (short) 0x0601),
        args(enumm("", emptyList(), null, "Empty", emptyList(), emptyList()), (short) 0x4031));
  }

  @ParameterizedTest
  @MethodSource
  public void testAccessModifiers(Clazz clazz, short expectedModifiers) {
    target.accessModifiers(clazz);
    assertArrayEquals(target.out.getBytes(), shortToByteArray(expectedModifiers));
  }
}
