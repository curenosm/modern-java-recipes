package com.curenosm.chapter8;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class Chapter8Application {

  public static void main (String[] args) {
    SpringApplication.run(Chapter8Application.class, args);
  }

  /**
   * Work with factory methods in classes like {@code Instant}
   * {@code Duration}, {@code Period}, {@code LocalDate}, {@code LocalTime},
   * {@code LocalDateTime}, {@code ZonedDateTime}
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


}
