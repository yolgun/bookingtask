package com.marketlogicsoftware;

import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeRangeSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeSet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Main business logic. Responsible for all schedules.
 *
 * I have used Guava RangeSet class to handle booking logic. A newly initialized Scheduler is available all time.
 * Every time a new booking is requested, first it has to match every rule and availableTimes should enclose whole
 * booking interval. If it is true this interval is removed from availableTimes. The booking is recorded to another
 * Guava class, TreeMultimap. TreeMultimap is the natural choice to map days to sets of bookings.
 *
 * Not thread safe.
 */
public class Scheduler {

  private final List<BookingRule> ruleList = new ArrayList<>();
  private final RangeSet<DateTime> availableTimes = getEmptyTimeRange();
  private final TreeMultimap<LocalDate, Booking> validBookings = TreeMultimap.create();

  private Scheduler() {
  }

  /**
   * Static factory method.
   */
  public static Scheduler withRules(BookingRule... rules) {
    Scheduler scheduler = new Scheduler();
    Arrays.stream(rules)
        .forEach(scheduler::addRule);
    return scheduler;
  }

  /**
   * This class is necessary because I usually try to make field members final and type inference of generics is not
   * so smart in Java.
   */
  private RangeSet<DateTime> getEmptyTimeRange() {
    TreeRangeSet<DateTime> tmp = TreeRangeSet.create();
    return tmp.complement();
  }

  private void addRule(BookingRule rule) {
    ruleList.add(rule);
  }


  /**
   * Try to book the given booking.
   *
   * @param booking to be tried
   * @return true if it is successfull.
   */
  public boolean bookTry(Booking booking) {
    if (rulesMatch(booking) && isAvailable(booking)) {
      book(booking);
      return true;
    } else {
      return false;
    }
  }

  /**
   * @return true if the booking matches all rules.
   */
  private boolean rulesMatch(Booking booking) {
    return ruleList.stream().allMatch(rule -> rule.test(booking));
  }

  /**
   * @return true if the booking does not overlap with already made bookings.
   */
  private boolean isAvailable(Booking booking) {
    return availableTimes.encloses(booking.asRange());
  }

  /**
   * Assumes booking does not conflict with existing bookings. Removes it from available times to make sure
   * it does not overlap with future bookings. Add it to validBookings map to query later.
   */
  private void book(Booking booking) {
    availableTimes.remove(booking.asRange());
    validBookings.put(booking.getStartTime().toLocalDate(), booking);
  }

  /**
   * @return an unmodifiable collection of all valid bookings in any order.
   */
  public Collection<Booking> getBookings() {
    return Collections.unmodifiableCollection(validBookings.values());
  }

  /**
   * @return all bookings made grouped by their date.
   */
  public NavigableMap<LocalDate, Collection<Booking>> getBookingsGrouped() {
    return Collections.unmodifiableNavigableMap(validBookings.asMap());
  }

  /**
   * Assumes reader contains one booking representation for every two lines.
   *
   * First adds them to a set in order of booking time. Later tries to book them in order.
   */
  public void bookAllInBookingOrder(BufferedReader reader) throws IOException {
    String bookingLine, scheduleLine;
    TreeSet<Booking> bookings = new TreeSet<>(Comparator.comparing(Booking::getBookedTime));
    while ((bookingLine = reader.readLine()) != null && ((scheduleLine = reader.readLine()) != null)) {
      Booking booking = Booking.fromString(bookingLine, scheduleLine);
      bookings.add(booking);
    }
    bookings.forEach(this::bookTry);
  }

  /**
   * String output of all bookings made up to now.
   *
   * I suggest against using it. Plain text representation of complex objects are fragile.
   * Instead use getBookingsGrouped.
   */
  public String output() {
    StringBuilder sb = new StringBuilder();
    for (Entry<LocalDate, Collection<Booking>> localDateCollectionEntry : getBookingsGrouped().entrySet()) {
      sb.append(localDateCollectionEntry.getKey().toString("YYYY-MM-dd"))
          .append("\n");
      for (Booking booking : localDateCollectionEntry.getValue()) {
        sb.append(booking.getStartTime().toString("HH:mm"))
            .append(" ")
            .append(booking.getEndTime().toString("HH:mm"))
            .append(" ")
            .append(booking.getBookedBy())
            .append("\n");
      }
    }
    return sb.toString();
  }
}
