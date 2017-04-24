package com.marketlogicsoftware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Entry point of the application. File operations goes here.
 */
public class App {

  private static final String INPUT_FILE = "input.txt";

  public static void main(String[] Args) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE))) {
      String openHoursLine = reader.readLine();
      OpenHoursRule openHoursRule = OpenHoursRule.fromString(openHoursLine);
      Scheduler scheduler = Scheduler.withRules(openHoursRule);
      scheduler.bookAllInBookingOrder(reader);
      System.out.println(scheduler.output());
    }
  }
}
