package com.example.chapter9;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

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

  Chapter9Application demo = new Chapter9Application();

  @Test
  public void parallelStreamMethodOnCollection() {
    List<Integer> numbers = Arrays.asList(3, 1, 4, 1, 5, 9);
    assertFalse(numbers.stream().isParallel());
  }

  @Test
  public void testException() {
    assertThrows(ExecutionException.class, () -> {
      demo.getProduct(666).get();
      fail("Houston, we have a problem...");
    });
  }

  @Test
  public void compose() throws Exception {
    int x = 2;
    int y = 3;
    CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> x)
      .thenCompose(n -> CompletableFuture.supplyAsync(() -> n + y));
    assert 5 == completableFuture.get();
  }

  /*
  For independent Futures
   */
  @Test
  public void combine() throws Exception {
    int x = 2;
    int y = 3;
    CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> x)
      .thenCombine(CompletableFuture.supplyAsync(() -> y), Integer::sum);

    assert 5 == completableFuture.get();
  }



}
