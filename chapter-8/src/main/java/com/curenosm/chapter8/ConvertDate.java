package com.curenosm.chapter8;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConvertDate {

  public LocalDate convertFromSQLDateToLD(java.sql.Date sqlDate) {
    return sqlDate.toLocalDate();
  }

  public java.sql.Date convertToSQLDateFromLD(LocalDate localDate) {
    return java.sql.Date.valueOf(localDate);
  }

  public LocalDateTime convertFromTimestampToLD(Timestamp timestamp) {
    return timestamp.toLocalDateTime();
  }

  public Timestamp convertToTimestampFromLDT(LocalDateTime localDateTime) {
    return Timestamp.valueOf(localDateTime);
  }

  public LocalDate convertUtilDateToLocalDate(java.util.Date date) {
    return new java.sql.Date(date.getTime()).toLocalDate();
  }

  public ZonedDateTime convertFromCalendar(Calendar c) {
    return ZonedDateTime.ofInstant(c.toInstant(), c.getTimeZone().toZoneId());
  }

  public LocalDateTime convertFromCalendarUsingGetters(Calendar c) {
    return LocalDateTime.of(
      c.get(Calendar.YEAR),
      c.get(Calendar.MONTH),
      c.get(Calendar.DAY_OF_MONTH),
      c.get(Calendar.HOUR),
      c.get(Calendar.MINUTE),
      c.get(Calendar.SECOND));
  }

  public LocalDateTime convertFromUtilDateToLDUsingString(Date date) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    return LocalDateTime.parse(
      df.format(date),
      DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );
  }

  public ZonedDateTime convertFromGregorianCalendar(Calendar c) {
    return ((GregorianCalendar) c).toZonedDateTime();
  }

  public LocalDate convertFromUtilDateJava9(Date date) {
    return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

}
