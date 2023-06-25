package com.curenosm.chapter5;

public interface Employee {

  String getFirst();

  String getLast();
  void convertCaffeineToCodeForMoney();

  default String getName() {
    return String.format("%s %s", getFirst(), getLast());
  }

}
