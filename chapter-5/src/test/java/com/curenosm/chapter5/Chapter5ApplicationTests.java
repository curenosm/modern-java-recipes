package com.curenosm.chapter5;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Chapter5ApplicationTests {

  @Test
  void contextLoads () {
    List<String> strings = Arrays.asList("this", null, "is", "a", null, "list", "of", "strings", null);
    List<String> nonNull = strings.stream()
      .filter(Objects::nonNull)
      .toList();

    List<String> expected = Arrays.asList("this", "is", "a", "list", "of", "strings");
    assertTrue(Objects.deepEquals(nonNull, expected));
  }

  @Test
  void testFiltering () {
    List<String> strings = Arrays.asList("this", null, "is", "a", null, "list", "of", "strings", null);
    List<String> nonNull = Chapter5Application.getNonNullElements(strings);;

    List<String> expected = Arrays.asList("this", "is", "a", "list", "of", "strings");
    assertTrue(Objects.deepEquals(nonNull, expected));
  }
}
