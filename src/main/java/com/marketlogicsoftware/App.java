package com.marketlogicsoftware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 */
public class App {

  private static final String INPUT_FILE = "input.txt";

  public static void main(String[] Args) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(INPUT_FILE))) {
      System.out.println(OpenHoursRule.fromString(reader.readLine()));
      String line1, line2;
      while ((line1 = reader.readLine()) != null && (line2 = reader.readLine()) != null) {
        System.out.println(Booking.fromString(line1, line2));
      }
    }
  }
}
