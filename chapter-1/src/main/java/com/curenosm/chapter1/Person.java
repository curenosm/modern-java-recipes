package com.curenosm.chapter1;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
public class Person {

  // Copy constructor
  public Person(Person p) {
    this.name = p.name;
  }

  /**
   * Varargs method, receives zero or more
   */
  public Person(String... names) {
    log.info("Varargs constructor: names={}", Arrays.toString(names));
    this.name = Arrays.stream(names)
      .collect(Collectors.joining());
  }

  private String name;
}
