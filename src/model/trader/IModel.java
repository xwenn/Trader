package model.trader;

import java.util.Calendar;
import java.util.Map;

/**
 * This interface has all the operations a model should offer.
 */

public interface IModel {

  /*
  Operations on investment strategies
   */
  double profit(String basketName, Strategy strategy,
                Calendar start, Calendar end, double money, int period, Calendar date)
          throws Exception;

  /*
  Operations on a single stock
   */

  boolean isValidStockSymbol(String symbol) throws Exception;

  /**
   * Returns the closing price of the stock specified by the stock symbol on the specified day.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param date        the date of the day
   * @return the closing price of the specified stock on the specified day
   * @throws Exception if the data source is not read correctly, or if the symbol is invalid, or
   *                   throws IllegalArgumentException if the specified day is a weekend, future
   *                   day, or public holiday
   */
  double getStockClosingPrice(String stockSymbol, Calendar date) throws Exception;

  /**
   * Returns a map mapping the dates, represented by an integer, with the corresponding closing
   * prices of the stock, specified by its ticker symbol, in the specified date range. Non-business
   * days' will be skipped, i.e. not present in the map.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param startDate   the date of the first day of the date range
   * @param endDate     the date of the last day of the date range
   * @return a map mapping dates with corresponding closing prices of the specified stock in the
   *         specified date range
   * @throws Exception if end date is prior to start date, or if the symbol is invalid, or if if
   *                   the data source is not read correctly
   */
  Map<Integer, Double> getStockClosingPrices(String stockSymbol, Calendar startDate,
                                             Calendar endDate) throws Exception;

  /**
   * Calculates the 50-day moving average of the stock specified by its ticker symbol on
   * the specified date.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param date        the date of that day
   * @return the 50-day moving average
   * @throws Exception if the data source is not read correctly, or throws IllegalArgumentException
   *                   if the specified day is a future day, or if the symbol is invalid,
   */
  double fiftyDaysMovingAverageOfStock(String stockSymbol, Calendar date) throws Exception;

  /**
   * Calculates the 200-day moving average of the stock specified by its ticker symbol
   * on the specified date.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param date        the date of that day
   * @return the 200-day moving average
   * @throws Exception if the data source is not read correctly, or throws IllegalArgumentException
   *                   if the specified day is a future day, or if the symbol is invalid,
   */
  double twoHundredsDaysMovingAverageOfStock(String stockSymbol, Calendar date) throws Exception;

  /**
   * Returns a map mapping the dates, within the specified date range, with corresponding 50-day
   * moving averages of the stock, specified by its ticker symbol. Non-business days' will be
   * skipped, i.e. not present in the map.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param startDate   the date of the first day of the date range
   * @param endDate     the date of the last day of the date range
   * @return a map mapping the dates with corresponding 50-day moving averages of the specified
   *         stock
   * @throws Exception if end date is prior to start date, or if the symbol is invalid, or if if the
   *                   data source is not read correctly
   */
  Map<Integer, Double> fiftyDaysMovingAveragesOfStock(String stockSymbol, Calendar startDate,
                                                      Calendar endDate) throws Exception;

  /**
   * Returns a map mapping the dates, within the specified date range, with corresponding 200-day
   * moving averages of the stock, specified by its ticker symbol. Non-business days' will be
   * skipped, i.e. not present in the map.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param startDate   the date of the first day of the date range
   * @param endDate     the date of the last day of the date range
   * @return a map mapping the dates with corresponding 50-day moving averages of the specified
   *         stock
   * @throws Exception if end date is prior to start date, or if the symbol is invalid, or if if the
   *                   data source is not read correctly
   */
  Map<Integer, Double> twoHundredsDaysMovingAveragesOfStock(String stockSymbol, Calendar startDate,
                                                            Calendar endDate) throws Exception;

  /**
   * Evaluates the trend of the stock specified by its ticker symbol from the
   * startDate to the endDate.
   *
   * @param stockSymbol the ticker symbol of the stock
   * @param startDate   the date of the first day of the date range
   * @param endDate     the date of the last day of the date range
   * @return the slope of the fitting line of the closing prices in the date range
   * @throws Exception if end date is prior to start date, or if the symbol is invalid, or if if the
   *                   data source is not read correctly
   */
  double stockTrend(String stockSymbol, Calendar startDate, Calendar endDate) throws Exception;

  /*
  Operations on a basket
   */

  /**
   * Creates a new, empty basket of stocks with the specified name and creation date.
   * name is case sensitive.
   *
   * @param basketName the name of the new basket
   * @param date       the specified creation date
   * @throws IllegalArgumentException if a basket with the same name has been created
   */
  void createEmptyBasket(String basketName, Calendar date) throws Exception;


  /**
   * Creates a new, empty basket of stocks with the specified name and creation date.
   * name is case sensitive.
   *
   * @param basketName the name of the new basket
   * @throws IllegalArgumentException if a basket with the same name has been created
   */
  void createEmptyBasket(String basketName) throws IllegalArgumentException;

  /**
   * Adds the stock specified by the stock symbol to the basket specified by the name.
   * basket name is case sensitive, while stock symbol is not.
   *
   * @param stockSymbol the stock symbol of the stock you want to add
   * @param basketName  the name of the basket you want add a stock into
   * @throws Exception if the symbol is invalid, or is the share is less than 1, or if the basket
   *                   does not exist, or if if the data source is not read correctly
   */
  void addStockInto(String stockSymbol, int share, String basketName) throws Exception;


  /**
   * Evaluates the trends of stocks in the basket specified by the basket name from the
   * startDate to the endDate.
   *
   * @param basketName the name of the basket you want to get the trends of
   * @param startDate  the date of the first day of the specific date range
   * @param endDate    the date of the last day of the specific date range
   * @return the slope of the fitting line of the closing prices in the date range
   * @throws Exception if end date is prior to start date, or if the basket does not exist, or if if
   *                   the data source is not read correctly
   */
  double basketTrends(String basketName, Calendar startDate, Calendar endDate) throws Exception;

  /**
   * Returns the value (i.e. the closing price) of the basket specified by the basket name on
   * the specified day. If the specified date is not a business day, null is returned.
   *
   * @param basketName the ticker symbol of the stock
   * @param date       the date of the day
   * @return the value of the specified stock on the specified day
   */
  Double getBasketValue(String basketName, Calendar date) throws Exception;

  /**
   * Returns a map mapping the dates with corresponding values (i.e., average of closing prices)
   * of the basket specified by its name in the specified date range. Non-business days' will be
   * skipped, i.e. not present in the map.
   *
   * @param basketName the name of the basket
   * @param startDate  the date of the first day of the date range
   * @param endDate    the date of the last day of the date range
   * @return a map mapping the dates with corresponding values of the basket in the specified date
   *         range
   * @throws Exception if the given end date is prior to the start date, or if this basket is empty,
   *                   or if the basket does not exist, or the data source file is not read
   *                   correctly
   */
  Map<Integer, Double> getBasketValues(String basketName, Calendar startDate, Calendar endDate)
          throws Exception;

  /**
   * Calculates the 50-day moving average of the basket specified by its ticker symbol
   * on the specified date.
   *
   * @param basketName the ticker symbol of the basket
   * @param date       the date of that day
   * @return the 50-day moving average
   * @throws Exception if this basket is empty, or if the basket does not exist, or the data source
   *                   file is not read correctly
   */
  double fiftyDaysMovingAverageOfBasket(String basketName, Calendar date) throws Exception;

  /**
   * Calculates the 200-day moving average of the basket specified by its ticker symbol
   * on the specified date.
   *
   * @param basketName the ticker symbol of the basket
   * @param date       the date of that day
   * @return the 200-day moving average
   * @throws Exception if this basket is empty, or if the basket does not exist, or the data source
   *                   file is not read correctly
   */
  double twoHundredsDaysMovingAverageOfBasket(String basketName, Calendar date) throws Exception;

  /**
   * Returns a map mapping the dates, within the specified date range, with corresponding 50-day
   * moving averages of the basket, specified by its ticker symbol.
   *
   * @param basketName the ticker symbol of the basket
   * @param startDate  the date of the first day of the date range
   * @param endDate    the date of the last day of the date range
   * @return a map mapping the dates with corresponding 50-day moving averages of the specified
   *         stock
   * @throws Exception if the given end date is prior to the start date, or if this basket is empty,
   *                   or if the basket does not exist, or the data source file is not read
   *                   correctly
   */
  Map<Integer, Double> fiftyDaysMovingAveragesOfBasket(String basketName, Calendar startDate,
                                                       Calendar endDate) throws Exception;

  /**
   * Returns a map mapping the dates, within the specified date range, with corresponding 200-day
   * moving averages of the basket, specified by its ticker symbol.
   *
   * @param basketName the ticker symbol of the basket
   * @param startDate  the date of the first day of the date range
   * @param endDate    the date of the last day of the date range
   * @return a map mapping the dates with corresponding 200-day moving averages of the specified
   *         stock
   * @throws Exception if the given end date is prior to the start date, or if this basket is empty,
   *                   or if the basket does not exist, or the data source file is not read
   *                   correctly
   */
  Map<Integer, Double> twoHundredsDaysMovingAveragesOfBasket(String basketName, Calendar startDate,
                                                             Calendar endDate) throws Exception;

  /**
   * Return a string represents the specified basket.
   * @param basketName the name of the specified basket
   * @return a string represents the specified basket
   * @throws IllegalArgumentException if the basket does not exist in this model
   */
  String basketToString(String basketName) throws IllegalArgumentException;

  /**
   * Return true if this model contains the specified basket, false otherwise.
   * @param basketName the name of the specified basket
   * @return true if this model contains the specified basket, false otherwise
   */
  boolean containsBasket(String basketName);

  @Override
  String toString();

  /**
   * Remove the basket with the specified name from the basket map of this model.
   * @param basketName the name of the basket to be removed
   */
  void removeBasket(String basketName);
}
