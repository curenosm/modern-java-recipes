package com.curenosm.chapter6;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Chapter6Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter6Application.class, args);
  }

  /**
   * Notice that Optional are immutable but may not be a final reference, in this case it's
   * immutable because we have no methods to do anything with Optional instances, moreover we don't
   * even have a public Constructor.
   */
  @Bean
  @Order(1)
  public ApplicationRunner creatingOptional () {
    return args -> {
      Optional<Integer> integer = Optional.of(
        1); // Throws an exception in case the parameter is null
      Optional<Object> o = Optional.ofNullable(null); // This does not


    };
  }

  @Bean
  @Order(2)
  public ApplicationRunner retrievingOptional () {
    return args -> {
      Optional<Integer> integer = Optional.of(
        1); // Throws an exception in case the parameter is null
      Optional<Object> o = Optional.ofNullable(null); // This does not

      Integer integer1 = integer.get(); // only use this in case you're sure the optional isn't empty
      // o.get(); // would return a NoSuchElementException

      Optional<String> firstEven = Stream.of("five", "even", "string", "values")
        .filter(s -> s.length() % 2 == 0)
        .findFirst();

      System.out.println(firstEven.isPresent() ? firstEven.get() : "No even length strings");
      System.out.println(firstEven.orElse("No even length strings"));
      System.out.println(firstEven.orElseGet(() -> String.valueOf(Math.random())));
      o.ifPresent(System.out::println);
      firstEven.ifPresent(System.out::println);
    };
  }

  @Bean
  @Order(4)
  public ApplicationRunner optionalFlatMapVsMap() {
    return args -> {
      Manager mrSlate = new Manager("Mr. Slate");
      Department d = new Department();
      d.setBoss(mrSlate);
      System.out.println("Boss: " + d.getBoss());

      Department d1 = new Department();
      System.out.println("Boss: " + d.getBoss());
      System.out.println("Name: " + d.getBoss().orElse(new Manager("Unknown")).getName());
      System.out.println("Name: " + d1.getBoss().orElse(new Manager("Unknown")).getName());

      // The Map operation will only be applied if the Optional is not empty
      System.out.println("Name: " + d.getBoss().map(Manager::getName));
      System.out.println("Name: " + d1.getBoss().map(Manager::getName));

      Company co = new Company();
      co.setDepartment(d);
      System.out.println("Company Dept: " + co.getDepartment());
      System.out.println("Company Depth Manager: " + co.getDepartment().map(Department::getBoss));


      System.out.println(
        co.getDepartment()
          .flatMap(Department::getBoss)
          .map(Manager::getName)
      );

      Optional<Company> company = Optional.of(co);

      System.out.println(
        company
          .flatMap(Company::getDepartment)
          .flatMap(Department::getBoss)
          .map(Manager::getName)
      );

    };
  }

  @Bean
  @Order(5)
  public ApplicationRunner mappingOptionals() {
    return args -> {

    };
  }

  private List<Employee> findEmployeesByIds(List<Integer> ids) {
    return ids
      .stream()
      .map(this::findEmployeeById)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .toList();
  }

  private List<Employee> findEmployeesByIds2(List<Integer> ids) {
    return ids
      .stream()
      .map(this::findEmployeeById)
      .flatMap(optional ->
        optional
          .map(Stream::of)
          .orElseGet(Stream::empty))
      .collect(Collectors.toList());
  }

  private Optional<Employee> findEmployeeById(Integer id) {
    return Optional.empty();
  }

}
