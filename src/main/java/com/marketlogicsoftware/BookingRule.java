package com.marketlogicsoftware;

/**
 * This class is used to test if a booking is allowed.
 */
public interface BookingRule {

  boolean test(Booking booking);
}
