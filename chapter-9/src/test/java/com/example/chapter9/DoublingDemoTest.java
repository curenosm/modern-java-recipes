package com.example.chapter9;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 2, jvmArgs = {"-Xms4G", "-Xmx4G"})
public class DoublingDemoTest {

  public static void main (String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
      .include(DoublingDemoTest.class.getSimpleName())
      .forks(1)
      .build();
    new Runner(opt).run();
  }

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

  @Benchmark
  public int doubleAndSumSequential() {
    return IntStream.of(3, 2, 4, 1, 6, 9)
      .map(DoublingDemoTest::doubleIt)
      .sum();
  }

  @Benchmark
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
