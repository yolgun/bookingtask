# Build & Run
- Use `mvn clean package` to build and run tests.
- Use `mvn exec:java` to run the application. It will read bookings from `input.txt` file at top
directory level and prints the result to stdout.

# Summary
- I have used Guava and Joda-Time libraries extensively. Especially booking logic is handled by
Guava's RangeSet. It starts with complement of an empty `TreeRangeSet`, which means it is available
for all bookings. A booking is made successfully if it matches all `BookingRule`s and its interval
 can be enclosed by available times of the scheduler. If a booking is successful it is removed 
 from available times and inserted into valid bookings.
- Again Guava's TreeMultiMap handles group by day operation gracefully.
- Although `com.marketlogicsoftware.SchedulerTest.integrationTest` method is included in unit tests,
it is an integration test working on inputs given at problem description.
- Time complexity is `O(n logn)`, memory is `O(n)`.
