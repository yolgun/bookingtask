package com.marketlogicsoftware;

import com.google.common.collect.Range;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by YunusOlgun on 4/24/2017.
 */
public class Booking implements Comparable<Booking> {

  static final DateTimeFormatter bookedTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss");
  static final DateTimeFormatter scheduleFormatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm");

  private final DateTime bookedTime;
  private final String bookedBy;
  private final DateTime startTime;
  private final DateTime endTime;

  public Booking(DateTime bookedTime, String bookedBy, DateTime startTime, DateTime endTime) {
    this.bookedTime = bookedTime;
    this.bookedBy = bookedBy;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public static Booking fromString(String bookingLine, String scheduleLine) {
    String[] bookingLineSplitted = bookingLine.split(" ");
    String[] scheduleLineSplitted = scheduleLine.split(" ");
    if (bookingLineSplitted.length == 3 && scheduleLineSplitted.length == 3) {
      DateTime bookedTime = bookedTimeFormatter.parseDateTime(bookingLineSplitted[0] + " " + bookingLineSplitted[1]);
      String bookedBy = bookingLineSplitted[2];
      DateTime startTime = scheduleFormatter.parseDateTime(scheduleLineSplitted[0] + " " + scheduleLineSplitted[1]);
      DateTime endTime = startTime.plusHours(Integer.valueOf(scheduleLineSplitted[2]));
      return new Booking(bookedTime, bookedBy, startTime, endTime);
    } else {
      throw new IllegalArgumentException(String.format("Bad arguments: [%s,%s]", bookingLine, scheduleLine));
    }
  }

  public DateTime getBookedTime() {
    return bookedTime;
  }

  public String getBookedBy() {
    return bookedBy;
  }

  public DateTime getStartTime() {
    return startTime;
  }

  public DateTime getEndTime() {
    return endTime;
  }

  public Range<DateTime> asRange() {
    return Range.open(startTime, endTime);
  }

  @Override
  public String toString() {
    return "Booking{" +
        "bookedTime=" + bookedTime +
        ", bookedBy='" + bookedBy + '\'' +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Booking)) {
      return false;
    }

    Booking booking = (Booking) o;

    if (!getBookedTime().equals(booking.getBookedTime())) {
      return false;
    }
    if (!getBookedBy().equals(booking.getBookedBy())) {
      return false;
    }
    if (!getStartTime().equals(booking.getStartTime())) {
      return false;
    }
    return getEndTime().equals(booking.getEndTime());
  }

  @Override
  public int hashCode() {
    int result = getBookedTime().hashCode();
    result = 31 * result + getBookedBy().hashCode();
    result = 31 * result + getStartTime().hashCode();
    result = 31 * result + getEndTime().hashCode();
    return result;
  }

  @Override
  public int compareTo(Booking that) {
    if (this.startTime.compareTo(that.startTime) < 0) {
      return -1;
    } else if (this.startTime.compareTo(that.startTime) > 0) {
      return 1;
    }

    if (this.endTime.compareTo(that.endTime) < 0) {
      return -1;
    } else if (this.endTime.compareTo(that.endTime) > 0) {
      return 1;
    }

    if (this.bookedBy.compareTo(that.bookedBy) < 0) {
      return -1;
    } else if (this.bookedBy.compareTo(that.bookedBy) > 0) {
      return 1;
    }

    if (this.bookedTime.compareTo(that.bookedTime) < 0) {
      return -1;
    } else if (this.bookedTime.compareTo(that.bookedTime) > 0) {
      return 1;
    }
    return 0;
  }
}
