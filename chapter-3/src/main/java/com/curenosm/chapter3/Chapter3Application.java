package com.curenosm.chapter3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import lombok.Data;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Chapter3Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter3Application.class, args);
  }

  @Bean
  public ApplicationRunner streams() {
    return args -> {
      // Recipe 3.1 Create a stream
      String names = Stream
        .of("Gomez", "Morticia", "Wednesday", "Pugsley")
        .collect(Collectors.joining(","));
      System.out.println(names);

      String[] namesArr = {"Herman", "Lily", "Eddie", "Marilyn", "Grandpa"};
      names = Arrays.stream(namesArr).collect(Collectors.joining(","));
      System.out.println(names);

      List<BigDecimal> nums = Stream
        .iterate(BigDecimal.ONE, n -> n.add(BigDecimal.ONE))
        .limit(10)
        .collect(Collectors.toList());

      System.out.println(nums);
      Stream
        .iterate(LocalDate.now(), ld -> ld.plusDays(1L))
        .limit(10)
        .forEach(System.out::println);

      Stream
        .generate(Math::random)
        .limit(10)
        .forEach(System.out::println);

      List<String> bradyBunch = Arrays.asList("Greg", "Marcia", "Peter", "Jan", "Bobby", "Cindy");
      names = bradyBunch.stream().collect(Collectors.joining(","));
      System.out.println(names);


      // IntStream, DoubleStream, LongStream

      IntStream.range(0, bradyBunch.size()).forEach(System.out::println);
      assert IntStream.rangeClosed(1, 100).count() == 100;

      LongStream.range(0, bradyBunch.size()).forEach(System.out::println);
      assert LongStream.rangeClosed(1, 100).count() == 100;

      List<Integer> ints = IntStream
        .range(10, 15)
        .boxed()
        .toList();
      System.out.println(ints);

      List<Long> longs = LongStream
        .rangeClosed(10, 15)
        .boxed() // Necessary for collectors to convert primitives to List<T>
        .toList();
      System.out.println(longs);


    };
  }



  @Bean
  public ApplicationRunner generatingCollections() {
    return args -> {
      // Recipe 3.2 Generating collections from a primitive stream

      List<Integer> ints = IntStream
        .range(10, 15)
        .boxed()
        .toList();
      System.out.println(ints);

      List<Long> longs = LongStream
        .rangeClosed(10, 15)
        .mapToObj(Long::valueOf)
        .toList();
      System.out.println(longs);

      List<Integer> ints2 = IntStream
        .range(1, 34)
        .collect(ArrayList<Integer>::new, ArrayList::add, ArrayList::addAll);

      double[] doubleArray = DoubleStream
        .iterate(0.0, v -> Math.pow(v + 1, 2))
        .limit(100)
        .toArray();

      int[] intArr = IntStream
        .iterate(0, v -> v + 2)
        .limit(100)
        .toArray();

      System.out.println(Arrays.toString(doubleArray));

    };
  }


  @Bean
  public ApplicationRunner reductionOperations() {
    return args -> {
      // Recipe 3.3 Reduce

      OptionalDouble avg = IntStream
        .range(10, 15)
        .average();

      long count = IntStream
        .range(1, 34)
        .count();

      OptionalDouble max = DoubleStream
        .iterate(0.0, v -> Math.pow(v + 1, 2))
        .limit(100)
        .max();

      OptionalInt intArr = IntStream
        .iterate(0, v -> v + 2)
        .limit(100)
        .min();

      long suma = IntStream
        .range(1, 34)
        .sum();

      double res = DoubleStream
        .iterate(0.0, v -> Math.pow(v + 1, 2))
        .limit(100)
        .reduce(0, Double::sum);

      String[] strings = "this is an arrayt of strings".split(" ");
      int sum1 = Arrays.stream(strings).mapToInt(String::length).sum();


      int sum2 = IntStream
        .rangeClosed(1, 10)
        .reduce(Integer::sum).orElse(0);


      int sum = IntStream
        .rangeClosed(1, 10)
        .reduce(0, (x, y) -> {
          System.out.printf("x=%d, y=%d%n", x, y);
          return x + 2*y;
        });

      // String Concat is inefficient
      String s = Stream.of("this", "is", "a", "list")
        .collect(
          () -> new StringBuilder(),
          (sb, str) -> sb.append(str),
          (sb1, sb2) -> sb1.append(sb2)
        ).toString();

      s = Stream.of("this", "is", "a", "list")
        .collect(
          StringBuilder::new, // Supplier
          StringBuilder::append, // Adding one
          StringBuilder::append // Combine two results
        ).toString();

      s = Stream.of("this", "is", "a", "list")
        .collect(Collectors.joining("_"));

    };



  }

  @Data
  public static class Book {
    private Integer id;
    private String title;
  }

}
