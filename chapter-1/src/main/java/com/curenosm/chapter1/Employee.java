package com.curenosm.chapter1;

import lombok.Data;

public interface Employee {
  String getFirst();
  String getLast();
  void convertCaffeineToCodeForMoney();

  /**
   * This implementation can make reference to the abstract methods
   *
   * @return Name of the employee.
   */
  default String getName() {
    return String.format("%s %s", getFirst(), getClass());
  }
}
