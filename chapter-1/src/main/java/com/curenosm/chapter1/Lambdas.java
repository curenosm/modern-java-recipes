package com.curenosm.chapter1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Lambdas {

  @Bean
  public CommandLineRunner examples () {
    return args -> {
      log.info("{}", Arrays.toString(args));

      Runnable t = new Runnable() {
        @Override
        public void run () {
          System.out.println("Running thread as anonymous class");
        }
      };

      t.run();

      Runnable tLambda = () -> log.info("Running as lambda implementation");
      new Thread(tLambda).start();

      File directory = new File("./src/main/java");

      // Filenames reader
      String[] names = directory.list(
        new FilenameFilter() {
          @Override
          public boolean accept (File dir, String name) {
            return name.endsWith(".java");
          }
        }
      );
      System.out.println(Arrays.toString(names));

      String[] names2 = directory.list((dir, name) -> name.endsWith(".java"));
      String[] names3 = directory.list((File dir, String name) -> name.endsWith(".java"));

      assert names2 != null && names3 != null;
      System.out.println(Arrays.asList(names2));
      System.out.println(Arrays.asList(names3));

      Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);

      stream.forEach(n -> System.out.println(n));
//      stream.forEach(System.out::println);

      Consumer<Integer> consumer = System.out::println;
//      stream.forEach(consumer);

      Stream.generate(Math::random)
        .limit(10)
        .forEach(System.out::println);

      List<String> strings = Arrays.asList("this", "is", "a", "list", "of", "strings");
      List<String> sorted = strings.stream()
        .sorted(String::compareTo)
        .collect(Collectors.toList());

      List<String> sorted2 = strings.stream()
        .sorted(String::compareTo)
        .toList();

      Stream.of("this", "is", "a", "list", "of", "strings")
        .map(x -> x.length()) // Instance method via class name1
        .forEach(System.out::println); // Instance method via object reference

      Stream.of("this", "is", "a", "list", "of", "strings")
        .map(String::length) // Instance method via class name
        .forEach(System.out::println); // Instance method via object reference

      // Recipe 3
      List<Person> people = List.of(
        new Person("Misael"),
        new Person("Santiago")
      );

      List<String> peopleNames = people.stream()
        .map(Person::getName)
        .toList();

      List<String> peopleNames2 = people.stream()
        .map(Person::getName)
        .toList();

      List<Person> people2 = peopleNames2.stream()
        .map(Person::new) // Constructor reference
        .toList();

      // List to stream and viceversa
      Person before = new Person("Grace Hopper");

      List<Person> peoplee = Stream.of(before)
        .toList();

      Person after = peoplee.get(0);
      assert before == after;

      before.setName("Grace Murray Hopper");
      assert "Grace Murray Hopper".equals(after.getName());

      List<Person> people3 = Stream.of(before)
        .map(Person::new)
        .toList();

      after = people3.get(0);
      assert !(before == after);
      assert before.equals(after);
      before.setName("Rear Admiral Dr. Grace Murray Hopper");

      assert names != null;
      List<Person> people1 = Arrays.stream(names)
        .map(name -> name.split(" "))
        .map(Person::new)
        .toList();

    };
  }

  @Bean
  public ApplicationRunner examples2 () {
    return args -> {

      File directory = new File("./src/main/java");

      // Filenames reader
      String[] names = directory.list(
        new FilenameFilter() {
          @Override
          public boolean accept (File dir, String name) {
            return name.endsWith(".java");
          }
        }
      );

      // Array of person references
      assert names != null;
      Person[] people = Arrays.stream(names)
        .map(Person::new)
        .toArray(Person[]::new);

      // Recipe 4

    };
  }

  /**
   * Problem: You want to provide an implementation of a method inside an interface
   *
   * @return Nothing.
   */
  @Bean
  public ApplicationRunner defaultMethodsInInterfaces () {
    return args -> {
      List<Integer> nums = Arrays.asList(3, 1, 1, 5, 67, 8);
      boolean removed = nums.removeIf(n -> n <= 0);
      log.info("Elements were {} removed ", removed ? "" : "NOT");
      nums.forEach(System.out::println);
    };
  }

  /**
   * Recipe 1.6 Static methods in interfaces This way we make sure that an implementation is always
   * taken by default even if a class implements two interfaces with different implementations of
   * the same interface. Static methods in interfaces remove the need to create separate utility
   * classes, though that option is still available.
   */
  @Bean
  public ApplicationRunner staticMethodsInInterfaces () {
    return args -> {
      // Sorting strings
      List<String> bonds = Arrays.asList("Connery", "Lazenby", "Moore", "Dalton");
      List<String> sorted = bonds.stream()
        .sorted(Comparator.naturalOrder())
        .toList();
      log.info("{}", sorted);

      sorted = bonds.stream()
        .sorted(Comparator.reverseOrder())
        .toList();
      log.info("{}", sorted);

      sorted = bonds.stream()
        .sorted(Comparator.comparing(String::toLowerCase))
        .toList();
      log.info("{}", sorted);

      sorted = bonds.stream()
        .sorted(
          Comparator
            .comparingInt(String::length)
            .thenComparing(Comparator.naturalOrder())) // Lexicographical order
        .toList();
      log.info("{}", sorted);


    };
  }

}
