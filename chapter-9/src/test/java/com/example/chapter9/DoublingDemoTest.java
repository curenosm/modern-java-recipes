package com.example.chapter9;

import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class DoublingDemoTest {

  private static final int N = 10_000;

  public static int doubleIt(int n) {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ignore) {}
    return 2 * n;
  }

  public static long iterativeSum() {
    long result = 0;
    for (long i = 1L; i <= N; i++) {
      result += i;
    }
    return result;
  }

  
  public int doubleAndSumSequential() {
    return IntStream.of(3, 2, 4, 1, 6, 9)
      .map(DoublingDemoTest::doubleIt)
      .sum();
  }

  
  public int doubleAndSumParallel() {
    return IntStream.of(3, 1, 4, 1, 5, 9)
      .parallel()
      .map(DoublingDemoTest::doubleIt)
      .sum();
  }


  public long sequentialStreamSum() {
    return Stream.iterate(1L, i -> i + 1)
      .limit(N)
      .reduce(0L, Long::sum);
  }

  public long parallelStreamSum() {
    return Stream.iterate(1L, i -> i + 1)
      .limit(N)
      .parallel()
      .reduce(0L, Long::sum);
  }

  public long sequentialLongStreamSum() {
    return LongStream.rangeClosed(1L, N)
      .reduce(0L, Long::sum);
  }

  public long parallelLongStreamSum() {
    return LongStream.rangeClosed(1L, N)
      .parallel()
      .reduce(0L, Long::sum);
  }
}
