package utils;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * This class is a JUnit test class for CalendarUtil.
 */
public class CalendarUtilTest {

  /**
   * Tests whether method nextBusinessDayBefore works properly.
   */
  @Test
  public void testNextBusinessDayBefore() throws Exception {
    /*
    date = end
     */
    // date is business day
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 23);
    assertEquals(0, date1.compareTo(CalendarUtil.nextBusinessDayBefore(date1, date1)));

    // date is not business day
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 24);
    assertNull(CalendarUtil.nextBusinessDayBefore(date2, date2));

    /*
    date is 1 day before end
     */
    // date is business day
    assertEquals(0, date1.compareTo(CalendarUtil.nextBusinessDayBefore(date1, date2)));

    // date is not business day
    Calendar date3 = new GregorianCalendar(2017, 6 - 1, 17);
    Calendar date4 = new GregorianCalendar(2017, 6 - 1, 18);
    assertNull(CalendarUtil.nextBusinessDayBefore(date3, date4));


    try {
      Calendar date5 = new GregorianCalendar(2017, 8, 18);
      CalendarUtil.nextBusinessDayBefore(date1, date5);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.nextBusinessDayBefore(null, date1);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.nextBusinessDayBefore(date1, null);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.nextBusinessDayBefore(new GregorianCalendar(2200, 6 - 1, 5),
              new GregorianCalendar(2200, 6, 6));
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.nextBusinessDayBefore(date4, date3);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }
  }


  @Test
  public void testIsValidDay() {
    assertTrue(CalendarUtil.isValidDay(2017, 6, 23));
    assertTrue(CalendarUtil.isValidDay(2017, 5, 31));
    assertTrue(CalendarUtil.isValidDay(2016, 2, 29));
    assertFalse(CalendarUtil.isValidDay(2017, 2, 29));
    assertFalse(CalendarUtil.isValidDay(2017, 6, 31));
    assertFalse(CalendarUtil.isValidDay(2017, 13, 31));
  }

  @Test
  public void testDuration() {
    Calendar date1 = new GregorianCalendar(2017, 2 - 1, 1);
    Calendar date2 = new GregorianCalendar(2017, 2 - 1, 2);
    Calendar date3 = new GregorianCalendar(2017, 3 - 1, 1);
    Calendar date4 = new GregorianCalendar(2000, 2 - 1, 1);
    Calendar date5 = new GregorianCalendar(2000, 3 - 1, 1);
    assertEquals(1, CalendarUtil.duration(date1, date1));
    assertEquals(2, CalendarUtil.duration(date1, date2));
    assertEquals(29, CalendarUtil.duration(date1, date3)); // test Feb of common year
    assertEquals(30, CalendarUtil.duration(date4, date5)); // test Feb of leap year

    try {
      CalendarUtil.duration(null, date1);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.duration(date1, null);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.duration(null, null);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    try {
      CalendarUtil.duration(date2, date1);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }
  }

  @Test
  public void testBusinessDay() throws Exception {
    // saturday
    Calendar date = new GregorianCalendar(2017, 6 - 1, 24);
    // public holiday
    Calendar date1 = new GregorianCalendar(2016, 7 - 1, 4);
    // friday
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 23);
    // future day: 10 years from now
    Calendar future = new GregorianCalendar(2017, 8 - 1, 10);

    assertFalse(CalendarUtil.isBusinessDay(date));
    assertFalse(CalendarUtil.isBusinessDay(date1));
    assertTrue(CalendarUtil.isBusinessDay(date2));
    assertFalse(CalendarUtil.isBusinessDay(future));
  }

  @Test
  public void testNextBusinessDay() throws Exception {
    // normal weekend
    // Saturday
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 10);
    // Sunday
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 11);
    // Next Monday
    Calendar nextOfDate12 = new GregorianCalendar(2017, 6 - 1, 12);
    assertEquals(0, nextOfDate12.compareTo(CalendarUtil.nextBusinessDay(date1)));
    assertEquals(0, nextOfDate12.compareTo(CalendarUtil.nextBusinessDay(date2)));

    // long weekend
    // Saturday
    Calendar date3 = new GregorianCalendar(2017, 5 - 1, 27);
    // Sunday
    Calendar date4 = new GregorianCalendar(2017, 5 - 1, 28);
    // Memorial Day, Monday
    Calendar date5 = new GregorianCalendar(2017, 5 - 1, 29);
    // Next Tuesday
    Calendar nextOfDate345 = new GregorianCalendar(2017, 5 - 1, 30);
    assertEquals(0, nextOfDate345.compareTo(CalendarUtil.nextBusinessDay(date3)));
    assertEquals(0, nextOfDate345.compareTo(CalendarUtil.nextBusinessDay(date4)));
    assertEquals(0, nextOfDate345.compareTo(CalendarUtil.nextBusinessDay(date5)));

    // future date
    Calendar date6 = new GregorianCalendar(2222, 5 - 1, 27);
    try {
      CalendarUtil.nextBusinessDay(date6);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }

    // past weekend, but next business day has not come
    // if Monday June 26, 2017 has past, please uncomment the following two lines to check
    Calendar date7 = new GregorianCalendar(2017, 6 - 1, 24);
    assertEquals(null, CalendarUtil.nextBusinessDay(date7));
  }

  /**
   * Tests whether method copyDate works properly.
   */
  @Test
  public void testCopyDate() {
    Calendar date = new GregorianCalendar(2000, 1 - 1, 2);
    Calendar copy = CalendarUtil.copyDate(date);
    assertEquals(0, date.compareTo(copy));

    try {
      CalendarUtil.copyDate(null);
      fail("an exception should have been thrown");
    } catch (IllegalArgumentException e) {
      //
    }
  }

  /**
   * Tests whether isFutureDay works properly.
   */
  @Test
  public void testIsFutureDay() throws Exception {
    Calendar future = Calendar.getInstance();
    future.add(Calendar.YEAR, 10); // 10 years from now
    Calendar pastBizDay = new GregorianCalendar(2017, 6 - 1, 22);
    Calendar pastNonBizDay = new GregorianCalendar(2017, 6 - 1, 18);
    assertTrue(CalendarUtil.isFutureDay(future));
    assertFalse(CalendarUtil.isFutureDay(pastBizDay));
    assertFalse(CalendarUtil.isFutureDay(pastNonBizDay));
  }

}