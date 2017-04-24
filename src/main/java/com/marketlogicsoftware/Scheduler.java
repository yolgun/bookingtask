package com.marketlogicsoftware;

import com.google.common.collect.Collections2;
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
import java.util.Set;
import java.util.TreeSet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by YunusOlgun on 4/24/2017.
 */
public class Scheduler {

  private final List<BookingRule> ruleList = new ArrayList<>();
  private RangeSet<DateTime> availableTimes = getEmptyTimeRange();
  private TreeMultimap<LocalDate, Booking> validBookings = TreeMultimap.create();

  private RangeSet<DateTime> getEmptyTimeRange() {
    TreeRangeSet<DateTime> tmp = TreeRangeSet.create();
    return tmp.complement();
  }

  private Scheduler() {
  }

  public static Scheduler withRules(BookingRule... rules) {
    Scheduler scheduler = new Scheduler();
    Arrays.stream(rules)
        .forEach(scheduler::addRule);
    return scheduler;
  }

  private void addRule(BookingRule rule) {
    ruleList.add(rule);
  }

  public boolean bookTry(Booking booking) {
    if (rulesMatch(booking) && isAvailable(booking)) {
      book(booking);
      return true;
    } else {
      return false;
    }
  }

  private boolean isAvailable(Booking booking) {
    return availableTimes.encloses(booking.asRange());
  }

  private boolean rulesMatch(Booking booking) {
    return ruleList.stream().allMatch(rule -> rule.test(booking));
  }

  private void book(Booking booking) {
    availableTimes.remove(booking.asRange());
    validBookings.put(booking.getStartTime().toLocalDate(), booking);
  }

  public Collection<Booking> getBookings() {
    return Collections.unmodifiableCollection(validBookings.values());
  }

  public NavigableMap<LocalDate, Collection<Booking>> getBookingsGrouped() {
    return Collections.unmodifiableNavigableMap(validBookings.asMap());
  }

  public void bookAllInBookingOrder(BufferedReader reader) throws IOException {
    String bookingLine, scheduleLine;
    TreeSet<Booking> bookings = new TreeSet<>(Comparator.comparing(Booking::getBookedTime));
    while ((bookingLine = reader.readLine()) != null && ((scheduleLine = reader.readLine()) != null)) {
      Booking booking = Booking.fromString(bookingLine, scheduleLine);
      bookings.add(booking);
    }
    bookings.forEach(this::bookTry);
  }

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
