package com.marketlogicsoftware;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by YunusOlgun on 4/24/2017.
 */
public class OpenHoursRule implements BookingRule{

  private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmm");
  private final LocalTime openingTime;
  private final LocalTime closingTime;

  public OpenHoursRule(LocalTime openingTime, LocalTime closingTime) {
    this.openingTime = openingTime;
    this.closingTime = closingTime;
  }

  public static OpenHoursRule fromString(String args) {
    String[] splitted = args.split(" ");
    if (splitted.length == 2) {
      return new OpenHoursRule(formatter.parseLocalTime(splitted[0]), formatter.parseLocalTime(splitted[1]));
    } else {
      throw new IllegalArgumentException(args + " can not be parsed");
    }
  }

  public LocalTime getOpeningTime() {
    return openingTime;
  }

  public LocalTime getClosingTime() {
    return closingTime;
  }

  @Override
  public boolean test(Booking booking) {
    return isBeforeOrEquals(booking.getStartTime().toLocalTime())
        && isAfterOrEquals(booking.getEndTime().toLocalTime())
        && isSameDay(booking);
  }

  private boolean isBeforeOrEquals(LocalTime bookingTime) {
    return openingTime.isBefore(bookingTime) || openingTime.equals(bookingTime);
  }

  private boolean isAfterOrEquals(LocalTime bookingTime) {
    return closingTime.isAfter(bookingTime) || closingTime.equals(bookingTime);
  }

  private boolean isSameDay(Booking booking) {
    return booking.getStartTime().toLocalDate().equals(booking.getEndTime().toLocalDate());
  }

  @Override
  public String toString() {
    return "OpenHoursRule{" +
        "openingTime=" + openingTime +
        ", closingTime=" + closingTime +
        '}';
  }
}
