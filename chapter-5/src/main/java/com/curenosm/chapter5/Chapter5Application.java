package com.curenosm.chapter5;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.logging.Logger;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Chapter5Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter5Application.class, args);
  }

  @Bean
  @Order(1)
  public ApplicationRunner objectsClass() {
    return args -> {
      System.out.println(Objects.hash("Hello", "This is an object", 4L));

      List<String> strings = Arrays.asList("this", null, "is", "a", null, "list", "of", "strings", null);
      List<String> nonNull = strings.stream()
        .filter(Objects::nonNull)
        .toList();


    };
  }

  public static <T> List<T> getNonNullElements(List<T> list) {
    return list.stream()
      .filter(Objects::nonNull)
      .toList();
  }

  /**
   * In case you wanted to access a variable defined outside the scope
   * of a lambda expression.
   *
   */
  @Bean
  @Order(2)
  public ApplicationRunner lambdasAndEffectivelyFinal() {
    return args -> {
      List<Integer> nums = Arrays.asList(3, 1, 4, 5, 9);
      int total = 0;
      for (int n : nums) {
        total += n;
      }

      total = 0;
      // nums.forEach(n -> total += n); // WILL NOT COMPILE
      total = nums.stream().mapToInt(Integer::valueOf).sum();
    };
  }

  @Bean
  @Order(3)
  public ApplicationRunner generateStreamOfRandoms() {
    return args -> {
      Random random = new Random(123_456_789L);
      IntStream stream1 = random
        .ints(0, 1_000_000)
        .limit(10);
      DoubleStream stream2 = random.doubles(
        100L,
        0L,
        1_000_000L);
      System.out.println(stream1);
      System.out.println(stream2);

      random.ints(5)
        .sorted()
        .forEach(System.out::println);

      random.doubles(5, 0, 0.5)
        .sorted()
        .forEach(System.out::println);

      List<Long> longs = random.longs(5)
        .boxed()
        .toList();

      System.out.println(longs);
      LinkedList<Object> listOfInts = random.ints(5, 10, 20)
        .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
      System.out.println(listOfInts);

    };
  }


  /**
   * Memoization
   */
  @Bean
  @Order(4)
  public ApplicationRunner defaultMethodsInMap() {
    return args -> {
      System.out.println("Si entro");

      Map<String, Integer> map = countWords(
        "Sup",
        "Sup", "My", "Homie", "Sup"
      );

      System.out.println(map);
      map.forEach((k, v) -> System.out.println(k + "=" + v));

      Map<String, Integer> fullcount = fullWordCounts(
        "this is just a passage to count every possible word"
      );

      fullcount
        .entrySet()
        .stream()
        .forEach((e) -> System.out.println(e.getKey() + "=" + e.getValue()));

    };
  }

  private static final Map<Long, BigInteger> cache = new ConcurrentHashMap<>();


  /**
   * Example of memoization
   */
  public BigInteger fib(long i) {
    if (i == 0L) return BigInteger.ZERO;
    if (i == 1L) return BigInteger.ONE;
    return cache.computeIfAbsent(i, n -> fib(n - 2).add(fib(n - 1)));
  }


  public Map<String, Integer> countWords(String passage, String... strings) {
    HashMap<String, Integer> wordCounts = new HashMap<>();
    Arrays.stream(strings).forEach(s -> wordCounts.put("s", 0));
    Arrays.stream(passage.split(" ")).forEach(word -> {
      wordCounts.computeIfPresent(word, (key, val) -> val + 1);
    });

    return wordCounts;
  }

  public Map<String, Integer> fullWordCounts(String passage) {
    HashMap<String, Integer> wordCounts = new HashMap<>();
    String testString = passage.toLowerCase().replaceAll("\\W", " ");
    Arrays.stream(testString.split("\\s+")).forEach(word ->
      wordCounts.merge(word, 1, Integer::sum)
    );

    return wordCounts;
  }


  /**
   * This is in case you have a class that implements two different interfaces with a
   * method whose signature is the same. Check {@code Company}, {@code Employee},
   * {@code CompanyEmployee}.
   */
  @Bean
  @Order(5)
  public ApplicationRunner defaultMethodConflict() {
    return args -> {};
  }

  /**
   * Use the {@code forEach} method which was added as a default method to both {@code java.util.Iterable}
   * and {@code java.util.Map} interfaces. A pure function operates without side effects, which means
   * that applying the function with the same parameters always gives the same result. This is not
   * the expected behaviour for a {@code Consumer}
   */
  @Bean
  @Order(6)
  public ApplicationRunner iteratingOverCollectionsAndMaps() {
    return args -> {
      List<Integer> integers = Arrays.asList(1, 3, 6, 2, 8);
      integers.forEach(new Consumer<Integer>() {
        @Override
        public void accept (Integer integer) {
          System.out.println(integer);
        }
      });

      integers.forEach((Integer n) -> System.out.println(n));
      integers.forEach(System.out::println);


      Map<Long, String> map = new HashMap<>();
      map.put(86L, "Don Adams (Maxwell Smart)");
      map.put(99L, "Barbara Feldon");
      map.put(13L, "David Ketchum");

      map.forEach((num, agent) -> System.out.printf("Agent %d, played by %s%n", num, agent));
    };
  }


  private Logger logger = Logger.getLogger(getClass().getName());

  /**
   * In case you wanted to create a log message, but only if the log level ensures it'll be seen.
   * The technique of replacing an argument with a {@code Supplier} of the same type is known
   * as deferred execution, and can be used in any context where object creation might be expensive.
   */
  @Bean
  @Order(7)
  public ApplicationRunner loggingWithASupplier() {
    return args -> {
      ArrayList<String> data = new ArrayList<>();
      logger.info("The data is " + data.toString());
      logger.info(() -> "The data is " + data.toString());
    };
  }

  /**
   * In case you want to apply a series of small, independent functions consecutively use the
   * composition methods defined as default in the {@code Function}, {@code Consumer}
   * and {@code Predicate} interfaces.
   */
  @Bean
  @Order(8)
  public ApplicationRunner closureComposition() {
    return args -> {
      Function<Integer, Integer> add2 = x -> x + 2;
      Function<Integer, Integer> mult3 = x -> x * 3;

      Function<Integer, Integer> mult3add2 = add2.compose(mult3);
      Function<Integer, Integer> add2mult3 = add2.andThen(mult3);

      System.out.println("mult3add2(1) " + mult3add2.apply(1));
      System.out.println("add2mult3(1) " + mult3add2.apply(1));

      Function<String, Integer> parseAndThenAdd2 = add2.compose(Integer::parseInt);
      System.out.println(parseAndThenAdd2.apply("123124"));
      Function<Integer, String> plus2ToString = add2.andThen(Object::toString);

      Logger log = Logger.getLogger(getClass().getName());
      Consumer<String> printer = System.out::println;
      Consumer<String> logger = log::info;

      Consumer<String> printThenLog = printer.andThen(logger);
      Stream.of("this is a stream of strings".split(" ")).forEach(printThenLog);

      IntPredicate triangular = Chapter5Application::isTriangular;
      IntPredicate perfect = Chapter5Application::isPerfect;
      IntPredicate both = triangular.and(perfect);

      IntStream.rangeClosed(1, 10_000)
        .filter(both)
        .forEach(System.out::println);
    };
  }

  public static boolean isTriangular (int n) {
    return Math.sqrt(n) % 1 == 0;
  }

  public static boolean isPerfect (int n) {
    double val = (Math.sqrt(8 * n + 1) - 1) / 2 ;
    return val % 1 == 0;
  }


  /**
   * This recipe is used whenever you need to throw an exception from a code in lambda expression,
   * but you don't want to clutter a block lambda with exception handling code.
   */
  @Bean
  @Order(9)
  public ApplicationRunner extractedMethodForExceptionHandling() {
    return args -> {

    };
  }

}
