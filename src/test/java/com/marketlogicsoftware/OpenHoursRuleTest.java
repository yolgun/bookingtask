package com.marketlogicsoftware;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalTime;
import org.junit.Test;

public class OpenHoursRuleTest {

  private OpenHoursRule validRule = OpenHoursRule.fromString("0900 1730");

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowIAE() {
    OpenHoursRule.fromString("ILLEGAL");
  }

  @Test
  public void shouldConstruct() {
    OpenHoursRule rule = OpenHoursRule.fromString("0900 1730");

    assertEquals("", rule.getOpeningTime(), LocalTime.parse("09:00"));
    assertEquals("", rule.getClosingTime(), LocalTime.parse("17:30"));
  }

  @Test
  public void shouldNotAllowAfterOfficeHours() {
    Booking booking = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 17:00 2");

    assertEquals(false, validRule.test(booking));
  }

  @Test
  public void shouldNotAllowMultiDayBookings() {
    Booking booking = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 09:00 24");

    assertEquals(false, validRule.test(booking));
  }

  @Test
  public void shouldAllowInOfficeHours() {
    Booking bookingAtOpening = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 09:00 2");
    Booking bookingAtClosing = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 16:30 1");

    assertEquals(true, validRule.test(bookingAtOpening));
    assertEquals(true, validRule.test(bookingAtClosing));
  }


}
