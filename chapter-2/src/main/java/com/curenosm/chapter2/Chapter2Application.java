package com.curenosm.chapter2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
public class Chapter2Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter2Application.class, args);
  }


  @Bean
  public ApplicationRunner staticMethodsInInterfaces () {
    return args -> {
      // For each es un consumer
      //
      List<String> strings = Arrays.asList(
        "this", "is", "a", "list");

      strings.forEach(new Consumer<String>() {
        @Override
        public void accept (String o) {
          System.out.println(o);
        }
      });

      strings.forEach(s -> System.out.println(s)); // Lambda expression is the signature
      strings.forEach(System.out::println);

      Stream<String> stream = Stream.of("1", "2", "3");
      Stream<String> stream2 = Stream.of("1", "2", "3");
      Stream<String> stream3 = Stream.of("1", "6", "3");

      Optional<String> op = Optional.of("sop");
      op.ifPresent(System.out::println);
      stream.forEach(System.out::println);

      // peek() Returns a stream with the same elements that stream2
      stream2
        .peek(System.out::println)
        .forEach(System.out::println);

      stream3.forEachOrdered(System.out::println);

    };
  }


  @Bean
  public ApplicationRunner suppliers () {
    return args -> {
      DoubleSupplier randomSupplier = new DoubleSupplier() {
        @Override
        public double getAsDouble () {
          return Math.random();
        }
      };

      randomSupplier = () -> Math.random();
      randomSupplier = Math::random;
      log.info("{}", randomSupplier);

      Optional<Double> opt = Optional.of(0.5);
      Double val = opt.orElseGet(Math::random);

      List<String> names = Arrays.asList(
        "Zoe", "Simon", "Peter", "Sarah", "Jayne"
      );

      Optional<String> first = names.stream()
        .filter(name -> name.startsWith("C"))
        .findFirst();

      System.out.println(first);
      System.out.println(first.orElse(
        String.format(
          "No result found in %s",
          names.stream().collect(Collectors.joining(", "))
        )
      ));

      System.out.println(first.orElseGet(() ->
        String.format(
          "No result found in %s",
          String.join(", ", names)
          )
      ));
    };
  }


  @Bean
  public ApplicationRunner predicates() {
    return args -> {
      String[] names = Stream.of("buen", "nombre")
        .toList()
        .toArray(new String[]{});

      System.out.println(Arrays.toString(names));

    };
  }


  @Bean
  public ApplicationRunner functions() {
    return args -> {
      String[] names = Stream.of("buen", "nombre")
        .toList()
        .toArray(new String[]{});

      List<Integer> nameLengths = Arrays.stream(names)
        .map(new Function<String, Integer>() {
          @Override
          public Integer apply (String s) {
            return s.length();
          }
        })
        .toList();

      nameLengths = Arrays
          .stream(names)
          .map(s -> s.length())
          .toList();

      /*
      Primitive variations

        - IntFunction
        - DoubleFunction
        - LongFunction
        - ToIntFunction
        - ToDoubleFunction
        - ToLongFunction
        - DoubleToIntFunction
        ...

        - UnaryOperator
        - BinaryOperator
        - IntBinaryOperator
        ...

        - BiFunction
        - ToIntBiFunction
        - ToLongBiFunction
        ...
       */

      nameLengths = Arrays
        .stream(names)
        .map(String::length)
        .toList();

      System.out.printf("nameLengths = %s%n", nameLengths);

    };
  }


}
