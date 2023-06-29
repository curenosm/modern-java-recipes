package com.example.chapter9;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@Slf4j
@SpringBootApplication
public class Chapter9Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter9Application.class, args);
  }

  /*
   * Working with parallel streams is beneficial in these cases:
   *   - A large amount of data is processed.
   *   - A time-consuming process for each element.
   *   - A source of data that is easy to divide, and.
   *   - Operations that are stateless and associative.
   */

  public static int doubleIt(int n) {
    try {
      Thread.sleep(100);
    } catch (InterruptedException ignore) {}
    return 2 * n;
  }


  @Bean
  @Order(1)
  public ApplicationRunner timingExample() {
    return args -> {

      System.out.println(Runtime.getRuntime().availableProcessors());

      Instant before = Instant.now();
      int total = IntStream.of(3, 2, 4, 1, 6, 9).map(Chapter9Application::doubleIt).sum();
      Instant after = Instant.now();
      Duration duration = Duration.between(before, after);
      System.out.println("Total of doubles = " + total);
      System.out.println("time = " + duration.toMillis() + "ms");

      log.info("Parallelization:");
      before = Instant.now();
      total = IntStream.of(3, 2, 4, 1, 6, 9)
        .parallel()
        .map(Chapter9Application::doubleIt)
        .sum();
      after = Instant.now();
      duration = Duration.between(before, after);
      System.out.println("Total of doubles = " + total);
      System.out.println("time = " + duration.toMillis() + "ms");

    };
  }

}
