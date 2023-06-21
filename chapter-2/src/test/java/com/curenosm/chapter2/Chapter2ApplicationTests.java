package com.curenosm.chapter2;

import com.curenosm.chapter2.ImplementPredicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.curenosm.chapter2.ImplementPredicate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;

class Chapter2ApplicationTests {

  private ImplementPredicate demo = new ImplementPredicate();
  private String[] names;

  @BeforeEach
  public void setUp() {
    names = Stream
      .of("Maria", "Wash", "Kaylee", "Sarah", "Simon")
      .sorted()
      .toArray(String[]::new);
  }

  @Test
  void getNamesOfLength5 () throws Exception {
    assertEquals(
      "Maria, Sarah, Simon",
      demo.getNamesOfLength(5, names)
    );
  }

  @Test
  void getNamesStartingWith5 () throws Exception {
    assertEquals(
      "Sarah, Simon",
      demo.getNamesStartingWith("S", names)
    );
  }

  @Test
  void getNamesSatisfyingCondition() throws Exception {
    assertEquals("Maria, Sarah, Simon",
      demo.getNamesSatisfyingCondition(s -> s.length() == 5, names));
    assertEquals("Sarah, Simon",
      demo.getNamesSatisfyingCondition(s -> s.startsWith("S"), names));
    assertEquals("Maria, Sarah, Simon",
      demo.getNamesSatisfyingCondition(LENGTH_FIVE, names));
    assertEquals("Sarah, Simon",
      demo.getNamesSatisfyingCondition(STARTS_WITH_S, names));
  }

  @Test
  void composedPredicate() throws Exception {
    assertEquals("Sarah, Simon",
      demo.getNamesSatisfyingCondition(LENGTH_FIVE.and(STARTS_WITH_S), names));
    assertEquals("Maria, Sarah, Simon",
      demo.getNamesSatisfyingCondition(LENGTH_FIVE.or(STARTS_WITH_S), names));
    assertEquals("Kaylee, Wash",
      demo.getNamesSatisfyingCondition(LENGTH_FIVE.negate(), names));
  }

}
