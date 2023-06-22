package com.curenosm.chapter3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

// @SpringBootTest
class Chapter3ApplicationTests {

  @Test
  void debuggingStreamsWithPeek () throws Exception {
    assertEquals(1554, Chapter3Application.sumDoublesDivisibleBy3(100, 120));
  }


  @Test
  void testingFunctionalStylePalindromeChecker () throws Exception {
    assertTrue(Stream.of("anitalavalatina", "Madam, in Eden, I'm Adam",
        "Go hang a salami; I'm a lasagna hog", "Flee to me, remote elf",
        "A santa pets rats as Pat taps a star step at NASA")
      .allMatch(Chapter3Application::isPalindromeFunctional));
  }

  @Test
  public void testIsPrimeUsingAllMatch () throws Exception {
    assertTrue(
      IntStream.of(2, 3, 5, 7, 11, 13, 17, 19)
        .allMatch(Chapter3Application::isPrime)
    );
  }

  @Test
  public void testIfPrimeWithComposites() throws Exception {
    assertFalse(
      IntStream.of(4, 8, 12, 16, 24, 66)
        .anyMatch(Chapter3Application::isPrime)
    );
  }

  @Test
  public void testEmptyStreams() throws Exception {
    assertTrue(Stream.empty().allMatch(e -> false));
    assertTrue(Stream.empty().noneMatch(e -> true));
    assertFalse(Stream.empty().anyMatch(e -> true));
  }

}
