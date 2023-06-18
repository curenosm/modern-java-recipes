package com.curenosm.chapter1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Slf4j
@Configuration
public class Lambdas {

  @Bean
  public CommandLineRunner examples() {
    return args -> {
      log.info("{}", args);

      Runnable t = new Runnable(){
        @Override
        public void run () {
          System.out.println("Running thread as anonymous class");
        }
      };

      t.run();

      Runnable tLambda = () -> log.info("Running as lambda implementation");
      new Thread(tLambda).start();

      File directory = new File("./src/main/java");

      // Filaname reader
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
      String[] names3 = directory.list((File dir, String  name) -> name.endsWith(".java"));

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
        .sorted((s1, s2) -> s1.compareTo(s2))
        .collect(Collectors.toList());

      List<String> sorted2 = strings.stream()
        .sorted(String::compareTo)
        .collect(Collectors.toList());

      Stream.of("this", "is", "a", "list", "of", "strings")
        .map(x -> x.length()) // Instance method via class name1
        .forEach(x -> System.out.println(x)); // Instance method via object reference

      Stream.of("this", "is", "a", "list", "of", "strings")
        .map(String::length) // Instance method via class name
        .forEach(System.out::println); // Instance method via object reference

      // Recipe 3
      List<Person> people = List.of(
        new Person("Misael"),
        new Person("Santiago")
      );

      List<String> peopleNames = people.stream()
        .map(person -> person.getName())
        .collect(Collectors.toList());

      List<String> peopleNames2 = people.stream()
        .map(Person::getName)
        .collect(Collectors.toList());

      List<Person> people2 = peopleNames2.stream()
        .map(Person::new) // Constructor reference
        .collect(Collectors.toList());


      // List to stream and viceversa
      Person before = new Person("Grace Hopper");

      List<Person> peoplee = Stream.of(before)
        .collect(Collectors.toList());

      Person after = people.get(0);
      assert before == after;

      before.setName("Grace Murray Hopper");
      assert "Grace Murray Hopper".equals(after.getName());

    };
  }

}
