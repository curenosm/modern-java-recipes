package com.curenosm.chapter8;

import static com.curenosm.chapter8.Chapter8Application.getRegionNamesForOffset;
import static com.curenosm.chapter8.Chapter8Application.getRegionNamesForZoneId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalQueries;
import java.util.List;
import java.util.stream.IntStream;
import net.bytebuddy.asm.Advice.Local;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class Chapter8ApplicationTests {

  @Test
  void localDatePlus () {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = LocalDate.of(2017, Month.FEBRUARY, 2);

    LocalDate end = start.plusDays(3);
    assert "2017-02-05".equals(end.format(formatter));

    end = start.plusWeeks(5);
    assert "2017-03-09".equals(end.format(formatter));

    end = start.plusMonths(7);
    assert "2017-09-02".equals(end.format(formatter));

    end = start.plusYears(2);
    assert "2019-02-02".equals(end.format(formatter));
  }

  @Test
  void localTimePlus() {
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
    LocalTime start = LocalTime.of(11, 30, 0, 0);

    LocalTime end =  start.plusNanos(1_000_000);
    assert "11:30:00.001".equals(end.format(formatter));

    end = start.plusSeconds(20);
    assert "11:30:20".equals(end.format(formatter));

    end = start.plusMinutes(45);
    assert "12:15:00".equals(end.format(formatter));

    end = start.plusHours(5);
    assert "16:30:00".equals(end.format(formatter));
  }

  @Test
  public void plus_minus() throws Exception {
    Period period = Period.of(2, 3, 4);
    LocalDateTime start = LocalDateTime.of(2017, Month.FEBRUARY, 2, 11, 30);

    LocalDateTime end = start.plus(period);
    assert "2019-05-06T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.plus(3, ChronoUnit.HALF_DAYS);
    assert "2017-02-03T23:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.minus(period);
    assert "2014-10-29T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.minus(2, ChronoUnit.CENTURIES);
    assert "1817-02-02T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.plus(3, ChronoUnit.MILLENNIA);
    assert "5017-02-02T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  @Test
  public void with() throws Exception {
    LocalDateTime start = LocalDateTime.of(2017, Month.FEBRUARY, 2, 11, 30);

    LocalDateTime end = start.withMinute(45);
    assert "2017-02-02T11:45:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.withHour(16);
    assert "2017-02-02T16:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.withDayOfMonth(28);
    assert "2017-02-28T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.withDayOfYear(300);
    assert end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).equals("2017-10-27T11:30:00");

    assertEquals("2017-10-27T11:30:00", end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

    end = start.withYear(2020);
    assert "2020-02-02T11:30:00".equals(end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  /**
   * Here we are expecting the functional interface to throw an exception since
   * 2017 is not a leap year.
   */
  @Test
  public void withInvalidDate() throws Exception {
    assertThrows(DateTimeException.class, () -> {
      LocalDateTime start = LocalDateTime.of(2017, Month.FEBRUARY, 2, 11, 30);
      start.withDayOfMonth(29);
    });
  }

  @Test
  public void temporalField() throws Exception {
    LocalDateTime start = LocalDateTime.of(2017, Month.JANUARY, 31, 11, 30);
    LocalDateTime end = start.with(ChronoField.MONTH_OF_YEAR, 2);

    assertEquals("2017-02-28T11:30:00", end.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
  }

  @Test
  public void  adjustersAndQueries() {
    LocalDateTime start = LocalDateTime.of(2017, Month.JANUARY, 31, 11, 30);
    LocalDateTime end = start.with(TemporalAdjusters.firstDayOfNextMonth());
    assertEquals("2017-02-01T11:30", end.toString());

    end = start.with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
    assertEquals("2017-02-02T11:30", end.toString());

    end = start.with(TemporalAdjusters.previousOrSame(DayOfWeek.THURSDAY));
    assertEquals("2017-01-26T11:30", end.toString());
  }

  @Test
  public void payDay() throws Exception {
    TemporalAdjuster adjuster = new PaydayAdjuster();
    IntStream.rangeClosed(1, 14)
      .mapToObj(day -> LocalDate.of(2017, Month.JULY, day))
      .forEach(date -> assertEquals(14, date.with(adjuster).getDayOfMonth()));

    IntStream.rangeClosed(15, 31)
      .mapToObj(day -> LocalDate.of(2017, Month.JULY, day))
      .forEach(date -> assertEquals(31, date.with(adjuster).getDayOfMonth()));
  }

  @Test
  public void payDayWithMethodRef() throws Exception {
    IntStream.rangeClosed(1, 14)
      .mapToObj(day -> LocalDate.of(2017, Month.JULY, day))
      .forEach(date -> assertEquals(14, date.with(Adjusters::adjustInto).getDayOfMonth()));

    IntStream.rangeClosed(15, 31)
      .mapToObj(day -> LocalDate.of(2017, Month.JULY, day))
      .forEach(date -> assertEquals(31, date.with(Adjusters::adjustInto).getDayOfMonth()));
  }

  @Test
  public void queries() throws Exception {
    assertEquals(ChronoUnit.DAYS, LocalDate.now().query(TemporalQueries.precision()));
    assertEquals(ChronoUnit.NANOS, LocalTime.now().query(TemporalQueries.precision()));
    assertEquals(ZoneId.systemDefault(), ZonedDateTime.now().query(TemporalQueries.zone()));
    assertEquals(ZoneId.systemDefault(), ZonedDateTime.now().query(TemporalQueries.zoneId()));
  }

  @Test
  public void pirateDay() throws Exception {
    IntStream.range(10, 19)
      .mapToObj(n -> LocalDate.of(2017, Month.SEPTEMBER, n))
      .forEach(date -> assertTrue(date.query(Queries::daysUntilPirateDay) <= 9));

    IntStream.rangeClosed(20, 30)
      .mapToObj(n -> LocalDate.of(2017, Month.SEPTEMBER, n))
      .forEach(date -> {
        Long days = date.query(Queries::daysUntilPirateDay);
        assert days >= 354 && days < 365;
      });
  }

  @Test
  public void getRegionNamesForSystemDefault() throws Exception {
    ZonedDateTime now = ZonedDateTime.now();
    ZoneId zoneId = now.getZone();
    List<String> names = getRegionNamesForZoneId(zoneId);
    assertTrue(names.contains(zoneId.getId()));
  }

  @Test
  public void getRegionNamesForGMT() throws Exception {
    List<String> names = getRegionNamesForOffset(0, 0);
    assert names.contains("GMT");
    assert names.contains("Etc/GMT");
    assert names.contains("Etc/UTC");
    assert names.contains("UTC");
    assert names.contains("Etc/Zulu");
  }

  @Test
  public void getRegionNamesForChicago() throws Exception {
    ZoneId chicago = ZoneId.of("America/Chicago");
    List<String> names = getRegionNamesForZoneId(chicago);
    assert names.contains("America/Chicago");
    assert names.contains("US/Central");
    assert names.contains("Canada/Central");
    assert names.contains("Etc/GMT+5") || names.contains("Etc/GMT+6");
  }

}
