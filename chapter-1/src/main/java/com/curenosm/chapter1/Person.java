package com.curenosm.chapter1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

  // Copy constructor
  public Person(Person p) {
    this.name = p.name;
  }

  private String name;
}
