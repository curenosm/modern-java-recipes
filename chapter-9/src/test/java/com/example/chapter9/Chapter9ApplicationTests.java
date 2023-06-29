package com.example.chapter9;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;

class Chapter9ApplicationTests {

  @Test
  void sequentialStreamOf () {
    assertFalse(Stream.of(3, 1, 4 , 1, 5, 9).isParallel());
  }

  @Test
  void sequentialIterateStream() {
    assertFalse(Stream.iterate(1, n -> n + 1).isParallel());
  }

  @Test
  void sequentialGenerateStream() {
    assertFalse(Stream.generate(Math::random).isParallel());
  }

  @Test
  void sequentialCollectionStream() {
    List<Integer> numbers = Arrays.asList(3, 1,4, 1, 5, 9);
    assert numbers.parallelStream().isParallel();
  }

  @Test
  public void parallelStreamMethodOnCollection() {
    List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
    assertFalse(numbers.stream().isParallel());
  }

}
