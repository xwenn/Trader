package utils;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import model.trader.Stock;

/**
 * This class represents a calendar utility class.
 */

public class CalendarUtil {

  /**
   * Return the next business day of the given date.
   * If the given date is a business day, return a copy of itself.
   * If the given date is not a business day, return:
   * 1. if its next business day is not a future day, return the next business day
   * 2. if its next business day is a future day, return null.
   *
   * @param date the specified date
   * @return the (next) business day as explained in the description
   *         date if it is a non-business day, or return null if the given date is a future date.
   * @throws Exception when 1. source file is not read correctly; or
   *                    2. date is @code null (IllegalArgumentException), or
   *                   3. if date is a future day (IllegalArgumentException)
   */
  public static Calendar nextBusinessDay(Calendar date) throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }
    if (isFutureDay(date)) {
      throw new IllegalArgumentException();
    }
    Calendar copyDate = copyDate(date);
    if (isBusinessDay(copyDate)) {
      return copyDate;
    }
    int count = 0;
    do {
      copyDate.add(Calendar.DAY_OF_MONTH, 1);
      count++;
    }
    while (!isBusinessDay(copyDate) && count < 7);
    if (isBusinessDay(copyDate)) {
      return copyDate;
    } else {
      return null;
    }
  }


  /**
   * Finds the next business day before "end" of the specified "date".
   * If the specified date is a business day, a copy of itself will be returned.
   * If the specified date is not a business day:
   * 1. if there are business days between the specified date and end (inclusive),
   * the next business day will be returned.
   * 2. if there are no business days available before the end, @code null will be returned.
   *
   * @param date a specified date, must be a past day
   * @param end a specified end date, must be a past day
   * @return as explained in the description
   * @throws Exception if stock data source cannot be read correctly, or IllegalArgumentException
   *                   especially when:
   *                   1. date or end is @code null
   *                   2. date is a future day, or end is a future day
   *                   3. date is after end
   */
  public static Calendar nextBusinessDayBefore(Calendar date, Calendar end) throws Exception {
    if (date == null || end == null) {
      throw new IllegalArgumentException();
    }
    if (isFutureDay(date) || isFutureDay(end)) {
      throw new IllegalArgumentException();
    }
    if (date.after(end)) {
      throw new IllegalArgumentException();
    }

    Calendar copyDate = copyDate(date);
    if (isBusinessDay(copyDate)) {
      return copyDate;
    }

    while (!isBusinessDay(copyDate) && copyDate.compareTo(end) < 0) {
      copyDate.add(Calendar.DAY_OF_MONTH, 1);
    }

    if (isBusinessDay(copyDate)) {
      return copyDate;
    } else {
      return null;
    }
  }

  /**
   * Determines if the specified day is a future day.
   * Future day: 1. local current date; 2. dates after local current date.
   *
   * @param date the specified day
   * @return true if the specified day is a future day or false otherwise
   * @throws IllegalArgumentException if the specified date is @code null
   */
  public static boolean isFutureDay(Calendar date) {
    if (date == null) {
      throw new IllegalArgumentException("null date");
    }
    Calendar now = Calendar.getInstance();
    int nowYear = now.get(Calendar.YEAR);
    int dateYear = date.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);
    int dateMonth = date.get(Calendar.MONTH);
    int nowDay = now.get(Calendar.DAY_OF_MONTH);
    int dateDay = date.get(Calendar.DAY_OF_MONTH);

    if (dateYear > nowYear) {
      return true;
    }
    else if (dateYear < nowYear) {
      return false;
    }
    else {
      if (dateMonth > nowMonth) {
        return true;
      } else if (dateMonth < nowMonth) {
        return false;
      } else {
        return dateDay >= nowDay;
      }
    }
  }

  /**
   * Determines if the specified day is a past business day.
   * Past: dates before local current date
   * Business day: dates except: 1. weekends; 2. public holidays
   * @param date the specified date
   * @return true if the specified date is a past business day, and false otherwise.
   * @throws Exception if the data source cannot be read correctly, or the date is @code null
   */
  public static boolean isBusinessDay(Calendar date) throws Exception {
    if (date == null) {
      throw new IllegalArgumentException("null argument");
    }

    if (isFutureDay(date)) {
      return false; // the specified date is a future day
    }

    /*
    now the specified date is a past date
     */

    Map<Integer, Double> prices = new Stock("AAPL").getClosingPrices(date, date);
    return prices.size() != 0;
    // if == 0, the specified date is a past, non-business day
    // if != 0, the specified date is a past, business day
  }

  /**
   * Calculates the duration from startDate to endDate.
   * For example, given June 1, 2017 and June 5, 2017, return 5.
   *
   * @param startDate the first day of the duration (will be counted)
   * @param endDate the last day of the duration (will be counted)
   * @return the duration of the date range
   * @throws IllegalArgumentException if either one of the dates is @code null,
   *                                  or the startDate is after endDate
   */
  public static int duration(Calendar startDate, Calendar endDate)
          throws IllegalArgumentException {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("null date(s)");
    }
    if (startDate.after(endDate)) {
      throw new IllegalArgumentException("start date is after end date");
    }

    int duration = 0;
    Calendar currentDate = copyDate(startDate);
    while (!currentDate.after(endDate)) {
      duration++;
      currentDate.add(Calendar.DAY_OF_MONTH, 1);
    }
    return duration;
  }

  /**
   * Returns a copy of the specified date.
   * @param date a date
   * @return a copy of the given date
   * @throws IllegalArgumentException if the specified date is @code null
   */
  public static Calendar copyDate(Calendar date) {
    if (date == null) {
      throw new IllegalArgumentException("null date");
    }
    return new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Return true if the date is valid, false otherwise.
   * @param year the specified year
   * @param month the specified month
   * @param day the specified date
   * @return true if the date is valid, false otherwise
   */
  public static boolean isValidDay(int year, int month, int day) {
    if (year < 0) {
      return false;
    }

    if (month < 1 || month > 12) {
      return false;
    }

    boolean flag;
    switch (month) {
      case 1:
      case 3:
      case 5:
      case 7:
      case 8:
      case 10:
      case 12:
        flag = day >= 1 && day <= 31;
        break;
      case 2:
        flag = (day >= 1)
                && ((isLeapYear(year) && day <= 29)
                || (!isLeapYear(year) && day <= 28));
        break;
      default:
        flag = day >= 1 && day <= 30;
        break;
    }

    return flag;
  }

  /**
   * Determines if the specified year is a leap year.
   * @param year the specified year
   * @return true if the given year is a leap year, false otherwise
   */
  private static boolean isLeapYear(int year) {
    if (year % 4 != 0) {
      return false;
    } else if (year % 100 != 0) {
      return true;
    } else {
      return year % 400 == 0;
    }
  }

}
