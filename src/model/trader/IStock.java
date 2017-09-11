package model.trader;
/**
 * This interface represents all the operations a stock should support.
 */

import java.util.Calendar;
import java.util.Map;

public interface IStock extends Comparable<IStock> {

  /**
   * Gets the ticker symbol of this stock. All upper cases.
   *
   * @return the ticker symbol of this stock, all upper cases
   */
  String getSymbol();

  /**
   * Return a map whose keys are dates represented by integers, and values are the n-day moving
   * average of the the corresponding dates of this basket in the given date range. Non-business
   * days are skipped.
   *
   * @param startDate the start date of the date range
   * @param endDate   the end date of the date range
   * @param days      the specified number of days to be averaged
   * @return a map whose keys are dates represented by integers, and values are the n-day moving
   *         average of the the corresponding dates of this basket in the given date range
   * @throws Exception if the source file is not read correctly, or if the end date is prior to the
   *                   start date, of if the given number of days is less than 1.
   */
  Map<Integer, Double> getNDaysMovingAverages(Calendar startDate, Calendar endDate, int days)
          throws Exception;

  /**
   * Gets the name of this stock. Case sensitive
   *
   * @return the name of this stock
   */
  String getName();

  /**
   * Calculates the moving average in the past k days from the specified starting date.
   *
   * @param k         the period of time
   * @param startDate the starting date
   * @return the moving average in the past k days from the specified starting date
   */
  double movingAverage(int k, Calendar startDate) throws Exception;

  /**
   * Determines if there is a buying opportunity on the specified date.
   *
   * @param date a Calendar object indicating the date of that day. It should at least contain a
   *             date of month, a month, and a year.
   * @return true if the specified date has a buying opportunity and false otherwise
   * @throws Exception if the data source is not read correctly, or throws IllegalArgumentException
   *                   if the specified day is a weekend, future day, or public holiday
   */
  boolean isBuyingOpportunity(Calendar date) throws Exception;

  /**
   * Returns the closing price of this stock on the specified day.
   *
   * @param date a Calendar object indicating the date of that day. It should at least contain a
   *             date of month, a month, and a year.
   * @return the closing price of this stock on the specified day
   * @throws Exception if the data source is not read correctly, or throws IllegalArgumentException
   *                   if the specified day is a weekend, future day, or public holiday
   */
  double getClosingPrice(Calendar date) throws Exception;

  /**
   * Get historical closing prices for this stock for the given date range and return them in
   * a map whose keys are dates presented as integers and values are the closing prices of the
   * corresponding dates. Historical prices are available only for business days.
   *
   * @param startDate the start date of the desired date range
   * @param endDate   the end date of the desired date range
   * @return a map whose keys are dates of business days within the given date range and values are
   *         the closing prices of the corresponding dates
   * @throws Exception if end date is prior to start date, or if if the data source
   *         is not read correctly
   */
  Map<Integer, Double> getClosingPrices(Calendar startDate, Calendar endDate) throws Exception;

  /**
   * Determine the trend of this stock in the given date range by returning the slope of the
   * line fitted to the closing prices in these dates. A positive slope means this stock trends
   * up within the date range.
   *
   * @param startDate the start date of the desired date range
   * @param endDate   the end date of the desired date range
   * @return the slope of the fitting line of the closing prices in the date range
   * @throws IllegalArgumentException if end date is prior to start date or if no stock data can be
   *                                  retrieved from the server given the specified date range
   */
  double trends(Calendar startDate, Calendar endDate);

  /**
   * Determines whether this is equal to obj. Equal if obj is also a stock and the ticker symbol
   * of this stock is equal to that of obj. Case is ignored.
   *
   * @param obj other stock
   * @return true if this equals to other, and false otherwise. Result is consistent with compareTo
   *         when obj is a stock object
   */
  @Override
  boolean equals(Object obj);

  /**
   * Compares two stocks by comparing their ticker symbols, ignoring their cases.
   *
   * @param other another stock
   * @return a positive integer if this comes after other, a negative integer if this comes before
   *         other, and 0 if they are equal
   */
  @Override
  int compareTo(IStock other);

}