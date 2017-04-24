package com.marketlogicsoftware;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BookingTest {

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIAE() {
    Booking.fromString("ILLEGAL", "ILLEGAL");
  }

  @Test
  public void shouldConstruct() {
    Booking booking = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 09:00 2");

    assertEquals(Booking.bookedTimeFormatter.parseDateTime("2015-08-17 10:17:06"), booking.getBookedTime());
    assertEquals("EMP001", booking.getBookedBy());
    assertEquals(Booking.scheduleFormatter.parseDateTime("2015-08-21 09:00"), booking.getStartTime());
    assertEquals(Booking.scheduleFormatter.parseDateTime("2015-08-21 11:00"), booking.getEndTime());
  }
}
