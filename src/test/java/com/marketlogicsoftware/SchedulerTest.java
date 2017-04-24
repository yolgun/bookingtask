package com.marketlogicsoftware;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class SchedulerTest {

  private Booking validBooking1 = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 09:00 2");
  private Booking overlappingBooking1 = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 10:00 2");
  private Booking validBooking2 = Booking.fromString("2015-08-17 10:17:06 EMP001", "2015-08-21 11:00 2");


  @Test
  public void shouldBookValidOnes() throws Exception {
    Scheduler scheduler = Scheduler.withRules();

    scheduler.bookTry(validBooking1);
    scheduler.bookTry(overlappingBooking1);
    scheduler.bookTry(validBooking2);

    Booking[] expected = {validBooking1, validBooking2};
    assertThat(scheduler.getBookings(), containsInAnyOrder(expected));
  }

  @Test
  public void shouldNotAllowOverlappingBooking() throws Exception {
    Scheduler scheduler = Scheduler.withRules();

    boolean firstBooking = scheduler.bookTry(validBooking1);
    boolean sameBooking = scheduler.bookTry(overlappingBooking1);

    assertEquals(true, firstBooking);
    assertEquals(false, sameBooking);
  }

  @Test
  public void shouldNotAllowIfRulesFail() throws Exception {
    BookingRule alwaysFail = mock(BookingRule.class);
    Scheduler scheduler = Scheduler.withRules(alwaysFail);

    assertEquals(false, scheduler.bookTry(validBooking1));
  }

  @Test
  public void integrationTest() throws Exception {
    OpenHoursRule openHoursRule = OpenHoursRule.fromString("0900 1730");
    Scheduler scheduler = Scheduler.withRules(openHoursRule);

    String input = "2015-08-17 10:17:06 EMP001\n"
        + "2015-08-21 09:00 2\n"
        + "2015-08-16 12:34:56 EMP002\n"
        + "2015-08-21 09:00 2\n"
        + "2015-08-16 09:28:23 EMP003\n"
        + "2015-08-22 14:00 2\n"
        + "2015-08-17 11:23:45 EMP004\n"
        + "2015-08-22 16:00 1\n"
        + "2015-08-15 17:29:12 EMP005\n"
        + "2015-08-21 16:00 3\n";

    InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      scheduler.bookAllInBookingOrder(reader);
    }

    String expected = "2015-08-21\n"
        + "09:00 11:00 EMP002\n"
        + "2015-08-22\n"
        + "14:00 16:00 EMP003\n"
        + "16:00 17:00 EMP004\n";

    assertEquals(scheduler.output(), expected);
  }
}
