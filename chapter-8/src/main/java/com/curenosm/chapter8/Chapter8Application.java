package com.curenosm.chapter8;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@Slf4j
@SpringBootApplication
public class Chapter8Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter8Application.class, args);
  }

  /**
   * Work with factory methods in classes like {@link Instant}
   * {@link Duration}, {@link Period}, {@link LocalDate}, {@link LocalTime},
   * {@link LocalDateTime}, {@link ZonedDateTime}
   *
   * THEY ALL PRODUCE IMMUTABLE INSTANCES, so they're thread safe
   *
   */
  @Bean
  @Order(1)
  public ApplicationRunner usingBasics() {
    return args -> {
      System.out.println("Instant: " + Instant.now());
      System.out.println("LocalDate: " + LocalDate.now());
      System.out.println("LocalTime: " + LocalTime.now());
      System.out.println("LocalDateTime: " + LocalDateTime.now());
      System.out.println("ZonedDateTime: " + ZonedDateTime.now());

      System.out.println("First landing on the Moon");
      LocalDate moonLandingDate = LocalDate.of(1969, Month.JULY, 20);
      LocalTime moonLandingTime = LocalTime.of(20, 18);
      System.out.println("Date: " + moonLandingDate);
      System.out.println("Time: " + moonLandingTime);

      System.out.println("Neil Armstrong steps onto the surface: ");
      LocalTime walkTime = LocalTime.of(20, 2, 56, 150_000_000);
      LocalDateTime walk = LocalDateTime.of(moonLandingDate, walkTime);
      System.out.println(walk);

      Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
      System.out.println("There are " + availableZoneIds.size() + " region names.");


      LocalDateTime dateTime = LocalDateTime.of(2017, Month.JULY, 4, 13, 20, 10);
      ZonedDateTime nyc = dateTime.atZone(ZoneId.of("America/Mexico_City"));
      System.out.println(nyc);

      ZonedDateTime london = nyc.withZoneSameInstant(ZoneId.of("Europe/London"));
      System.out.println(london);

      System.out.println("Days in Feb in a leap year: " + Month.FEBRUARY.length(true));
      System.out.println("Day of the year for first day of Aug (leap year) " + Month.AUGUST.firstDayOfYear(true));
      System.out.println("Month.of(1): " + Month.of(1));
      System.out.println("Adding two months: " + Month.JANUARY.plus(2));
      System.out.println("Subtracting two months: " + Month.JANUARY.minus(1));

    };
  }

  /**
   * In case you need to convert from {@link java.util.Date} to {@link java.time.LocalDate}
   * or viceversa.
   */
  @Bean
  @Order(3)
  public ApplicationRunner convertingDates() {
    return args -> {
      LocalDate res1 = convertFromUtilDateUsingInstant(new Date());
      LocalDate res2 = convertFromUtilDateUsingInstant(new Date());

      System.out.println(res1);
      System.out.println(res2);

      // Built-in conversion methods in java.sql.Date and java.sql.Timestamp
      LocalDateTime now = LocalDateTime.now();
      String text = now.format(DateTimeFormatter.ISO_DATE_TIME);
      LocalDateTime dateTime = LocalDateTime.parse(text);

      LocalDate date = LocalDate.of(2017, Month.MARCH, 13);
      System.out.printf("Full: %s%n", date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
      System.out.printf("Long: %s%n", date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
      System.out.printf("Medium: %s%n", date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
      System.out.printf("Short: %s%n", date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));

      System.out.printf(
        "France: %s%n",
        date.format(
          DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale.FRANCE)));
      System.out.printf(
        "India: %s%n",
        date.format(
          DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(new Locale("hin", "IN"))));
      System.out.printf(
        "Brazil: %s%n",
        date.format(
          DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(new Locale("pt", "BR"))));
      System.out.printf(
        "Japan: %s%n",
        date.format(
          DateTimeFormatter
            .ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale.JAPAN)));

      Locale loc = new Locale.Builder()
        .setLanguage("sr")
        .setScript("Latn")
        .setRegion("RS")
        .build();
      System.out.printf(
        "Serbian: %s%n",
        date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(loc)));


      // Defining a custom format pattern
      ZonedDateTime moonLanding = ZonedDateTime.of(
        LocalDate.of(1969, Month.JULY, 20),
        LocalTime.of(20, 18),
        ZoneId.of("UTC")
      );
      System.out.println(moonLanding.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu/MMMM/dd hh:mm:ss a zzz GG");
      System.out.println(moonLanding.format(formatter));

      formatter = DateTimeFormatter.ofPattern("uuuu/MMMM/dd hh:mm:dd a VV xxxxx");
      System.out.println(moonLanding.format(formatter));

      // The USA daylight savings moves the clock forward at 2 AM
      ZonedDateTime zdt = ZonedDateTime.of(
        2018,
        3,
        11,
        2,
        30,
        0,
        0,
        ZoneId.of("America/New_York"));

      // Localized date format (daylight savings)
      System.out.println(
        zdt.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
      ); // Actually prints 3:30

    };
  }

  public LocalDate convertFromUtilDateUsingInstant(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  @Bean
  @Order(3)
  public ApplicationRunner findingTZsWithUnusualOffsets(){
    return args -> {
      Instant instant = Instant.now();
      ZonedDateTime current = instant.atZone(ZoneId.systemDefault());
      System.out.printf("Current time is %s%n%n", current);

      System.out.printf("%10s %20s %13s%n", "Offset", "ZoneId", "Time");
      ZoneId.getAvailableZoneIds()
        .stream()
        .map(ZoneId::of)
        .filter(zoneId -> {
          ZoneOffset offset = instant.atZone(zoneId).getOffset();
          return offset.getTotalSeconds() % (60 * 60) != 0;
        })
        .sorted(
          Comparator.comparingInt(
            zoneId -> instant.atZone(zoneId).getOffset().getTotalSeconds()
          )
        ).forEach(zoneId -> {
          ZonedDateTime zdt = current.withZoneSameInstant(zoneId);
          System.out.printf("%10s %25s %10s%n",
            zdt.getOffset(),
            zoneId,
            zdt.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
        });
    };
  }

  @Bean
  @Order(7)
  public ApplicationRunner findingRegionNamesFromOffsets() {
    return args -> {
      List<String> sameOffset = getRegionNamesForZoneId(ZoneId.of("America/Mexico_City"));
      Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

      String pretty = gson.toJson(sameOffset);
      System.out.println(pretty);

    };
  }

  public static List<String> getRegionNamesForZoneId (ZoneId zoneId) {
    LocalDateTime now = LocalDateTime.now();
    ZonedDateTime zdt = now.atZone(zoneId);
    ZoneOffset offset = zdt.getOffset();
    return getRegionNamesForOffset(offset);
  }

  public static List<String> getRegionNamesForOffset(ZoneOffset offset) {
    LocalDateTime now = LocalDateTime.now();
    return ZoneId
      .getAvailableZoneIds()
      .stream()
      .map(ZoneId::of)
      .filter(zoneId -> now.atZone(zoneId).getOffset().equals(offset))
      .map(ZoneId::toString)
      .sorted()
      .toList();
  }

  /**
   * In case you know the offset but not the ZoneId of a region.
   */
  public static List<String> getRegionNamesForOffset(int hours, int minutes) {
    ZoneOffset offset = ZoneOffset.ofHoursMinutes(hours, minutes);
    return getRegionNamesForOffset(offset);
  }

  @Bean
  @Order(8)
  public ApplicationRunner timeBetweenEvents() {
    return args -> {
      LocalDate electionDay = LocalDate.of(2020, Month.NOVEMBER, 3);
      LocalDate today = LocalDate.now();

      System.out.printf("%d day(s) to go...%n", ChronoUnit.DAYS.between(today, electionDay));

      Period until = today.until(electionDay); // Equivalent to Period.between
      System.out.printf(
        "%d year(s), %d month(s), %d day(s)%n",
        until.getYears(),
        until.getMonths(),
        until.getDays()
      );

      Instant start = Instant.now();

      for (long i = 0; i < 1_000_000; i++) {
        double temp = Math.exp(i);
      }

      Instant end = Instant.now();
      System.out.println(getTiming(start, end));
    };
  }

  public static double getTiming(Instant start, Instant end) {
    return Duration.between(start, end).toMillis() / 1000.0;
  }


}
