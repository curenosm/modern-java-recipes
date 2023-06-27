package com.curenosm.chapter8;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class PaydayAdjuster implements TemporalAdjuster {

  @Override
  public Temporal adjustInto (Temporal temporal) {
    LocalDate date = LocalDate.from(temporal);
    int day;
    if (date.getDayOfMonth() < 15) {
      day = 15;
    } else {
      day = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
    }
    date = date.withDayOfMonth(day);
    if (date.getDayOfWeek() == DayOfWeek.SATURDAY
        || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
      date = date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
    }

    return temporal.with(date);
  }
}
