package com.curenosm.chapter2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
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
      List<String> sorted = bonds.stream().sorted(Comparator.naturalOrder()).toList();
      log.info("{}", sorted);

      sorted = bonds.stream().sorted(Comparator.reverseOrder()).toList();
      log.info("{}", sorted);

      sorted = bonds.stream().sorted(Comparator.comparing(String::toLowerCase)).toList();
      log.info("{}", sorted);

      sorted = bonds.stream().sorted(Comparator.comparingInt(String::length)
          .thenComparing(Comparator.naturalOrder())) // Lexicographical order
        .toList();
      log.info("{}", sorted);

    };
  }

}
