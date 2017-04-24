package com.marketlogicsoftware;

/**
 * Created by YunusOlgun on 4/24/2017.
 */
public interface BookingRule {
  boolean test(Booking booking);
}
