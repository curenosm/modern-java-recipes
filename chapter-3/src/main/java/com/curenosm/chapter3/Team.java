package com.curenosm.chapter3;

import java.text.NumberFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
public class Team {

  private static final NumberFormat nf = NumberFormat.getCurrencyInstance();
  private int id;
  private String name;
  private double salary;

  public String toString() {
    return String.format("Team {id=%s, name='%s', salary=%s}", id, name, nf.format(salary));
  }

}
