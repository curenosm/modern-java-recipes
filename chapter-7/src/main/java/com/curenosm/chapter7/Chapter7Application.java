package com.curenosm.chapter7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;


@SpringBootApplication
public class Chapter7Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter7Application.class, args);
  }

  /**
   * Processing a file as a {@code Stream} of {@code String}
   */
  @Bean
  @Order(1)
  public ApplicationRunner processFiles() {
    return args -> {
      try (
        Stream<String> lines = Files.lines(
          Path.of(ClassLoader.getSystemResource("words.txt").toURI())
        )
      ) {
      // try (Stream<String> lines = Files.lines(Paths.get("/absolute/route/to/file.txt"))) {
        lines.filter(s -> s.length() > 20)
          .sorted(Comparator.comparingInt(String::length).reversed())
          .limit(10)
          .forEach(w -> System.out.printf("%s (%d)%n", w, w.length()));
      } catch (IOException e) {
        e.printStackTrace();
      }

      // Since Stream implements AutoCloseable, the resources will be
      // closed once the try-catch has ended.
      try (
        Stream<String> lines = Files.lines(
          Path.of(ClassLoader.getSystemResource("words.txt").toURI())
        )
      ) {
        Map<Integer, Long> map = lines.filter(s -> s.length() > 20)
          .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        map
          .entrySet()
          .stream()
          .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
          .forEach(e -> System.out.printf("Length %d: %d words", e.getKey(), e.getValue()));
      } catch (IOException e) {
        e.printStackTrace();
      }


      // Equivalent example in case we're not dealing with a file
      try (
        Stream<String> lines = new BufferedReader(
//          new FileReader("/usr/share/dict/words")).lines()
          new FileReader(
            Path.of(
              ClassLoader.getSystemResource("words.txt").toURI()
            ).toFile()
          )
        ).lines()
      ) {
        // ...
        lines.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }


  /**
   * Listing the contents of a directory using {@code Files.list},
   * again as Stream is AutoCloseable the method {@code close} will be automatically executed
   * once the try-with-resources block ends.
   *
   * The resulting Stream is weakly consistent which means it is thread safe but does not freeze
   * the directory while iterating, so it (or may not) reflect updates to the directory that
   * occur after returning from this method.
   */
  @Bean
  @Order(2)
  public ApplicationRunner retrievingFilesAsAStream() {
    return args -> {
      try (Stream<Path> list = Files.list(Paths.get("src/main/java"))){
        list.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  /**
   * In case you need to perform a depth-first traversal of the filesystem
   */
  @Bean
  @Order(3)
  public ApplicationRunner walkingTheFilesystem() {
    return args -> {
      Path start = Paths.get("src/main/java");

      try (Stream<Path> paths = Files.walk(start)){
        paths.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }

      try (Stream<Path> paths = Files.walk(start, 3)){
        paths.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

  @Bean
  @Order(4)
  public ApplicationRunner searchingTheFilesystem() {
    return args -> {
      try (
        Stream<Path> paths = Files.find(
          Paths.get("src/main/java"),
          Integer.MAX_VALUE,
          (path, attributes) -> !attributes.isDirectory() && path.toString().contains("fileio")
        )) {
        paths.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    };
  }

}
