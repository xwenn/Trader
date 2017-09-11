package model.trader;

/**
 * This interface represents all the operations a basket of stocks should support.
 */

import java.util.Calendar;
import java.util.Map;

public interface IBasket {

  /**
   * Sets the investment strategy of this basket if applicable.
   * @param strategy the investment strategy of this basket
   * @throws IllegalArgumentException if the specified strategy is @code null
   */
  void setStrategy(Strategy strategy) throws IllegalArgumentException;

  /**
   * Calculates the profits on the specified date
   * using this basket's investment strategy from startDate to endDate if applicable.
   * @param startDate the start date of the date range of investment (inclusive)
   * @param endDate the end date of the date range of investment (inclusive)
   * @param money money invested in each investment
   * @param period the number of days of a set period, must >= 7
   * @param date the date the profit on which you want to compute
   * @return if applicable, returns profits gained using this basket's investment strategy
   *         from start date to end date, or @code null if not applicable
   * @throws Exception if data source is not ready correctly, or date range / total money is invalid
   */
  Double calcProfits(Calendar startDate, Calendar endDate, double money, int period, Calendar date)
          throws Exception;

  /**
   * Returns a copy of the creation time of this basket.
   * @return a copy of the creation time of this basket
   */
  Calendar getCreationTime();

  /**
   * Add the given number of shares of the given stock to this basket.
   *
   * @param stockSymbol the symbol of the stock to be added
   * @param share       the number of shares of the specified stock
   * @throws Exception if the given symbol is invalid, or if the given number of shares is less
   *                   than 1
   */
  void put(String stockSymbol, int share) throws Exception;

  /**
   * Remove the stock specified by the given symbol from this basket.
   *
   * @param stockSymbol the symbol of the stock to be removed
   * @throws Exception if the given symbol is invalid
   */
  void remove(String stockSymbol) throws Exception;

  /**
   * Return a map whose keys are the stocks in this basket, and values are the shares of the
   * corresponding stocks.
   *
   * @return a map whose keys are the stocks in this basket, and values are the shares of the
   *         corresponding stocks
   * @throws Exception when the map is not copied correctly
   */
  Map<IStock, Integer> getStocks() throws Exception;

  /**
   * Return true if the this basket contains a stock with the specified symbol.
   *
   * @param stockSymbol the specified symbol of the stock to be checked
   * @return true if this basket contains a stock with the specified symbol, false otherwise
   * @throws Exception when the given symbol is invalid
   */
  boolean containsStock(String stockSymbol) throws Exception;

  /**
   * Adds a specified number of shares of the specified stock into this basket.
   *
   * @param stockSymbol the ticker symbol of the stock. not case sensitive
   * @param share       the number of shares to add. must > 0
   * @throws Exception if there is something wrong with reading and retrieving data, or if the
   *                   ticker symbol is invalid, or the specified stock is not in the basket, or
   *                   the number of shares to add is <= 0
   */
  void incrementShareOf(String stockSymbol, int share) throws Exception;

  /**
   * Calculates and returns closing price of the stocks in this stocks on the specified date.
   *
   * @param date a Calendar object indicating the date of that day. It should at least contain a
   *             date of month, a month, and a year.
   * @return a price record that stores the total price of this stock
   * @throws Exception when the data source file is not read correctly, or the specified date is not
   *                   a business day, or the price data of that date is inaccessible (e.g., given
   *                   the date of today or tomorrow)
   */
  Double getClosingPrice(Calendar date) throws Exception;

  /**
   * Return a map whose values are the date represented by an 8-digit integer, and values are the
   * closing prices of the corresponding business date in the given date range. Non-business days
   * are skipped.
   *
   * @param startDate start date of the date range
   * @param endDate   end date of the date range
   * @return a map whose values are the date represented by an 8-digit integer, and values are the
   *         closing prices of the corresponding business date in the given date range
   * @throws Exception if the given end date is prior to the start date, or if this basket is empty,
   *                   or the data source file is not read correctly
   */
  Map<Integer, Double> getClosingPrices(Calendar startDate, Calendar endDate) throws Exception;

  /**
   * Determine the trend of this stocks in the given date range by returning the slope of the
   * line fitted to the closing prices in these dates. A positive slope means this stock trends
   * up within the date range.
   *
   * @param startDate the start date of the desired date range
   * @param endDate   the end date of the desired date range
   * @return the slope of the fitting line of the closing prices in the date range
   * @throws IllegalArgumentException if end date is prior to start date or if this basket is empty
   *                                  (no stock has been added to this basket)
   */
  double trends(Calendar startDate, Calendar endDate) throws Exception;

  /**
   * Return the moving average of closing prices of the last specified number of business days,
   * starting at the specified date.
   *
   * @param k         the specified number of days that need to be averaged
   * @param startDate the specified date whose moving average will be return
   * @return the moving average of closing prices of the last specified number of business days,
   *         starting at the specified date
   * @throws Exception when the given number of days is less than 1, or when this basket is empty,
   *                   or when the source file is not read correctly
   */
  double movingAverage(int k, Calendar startDate) throws Exception;

  /**
   * Return the specified number of days moving averages of this basket in the specified date.
   * range.
   *
   * @param startDate the start date of the date range
   * @param endDate   the end date of the date range
   * @param days      the specified number of days that need to be averaged
   * @return the specified number of days moving averages of this basket in the specified date
   * @throws Exception if the source file is not read correctly, or if the specified number of days
   *                   is less than 1, or if the given end date is prior to the start date, or if
   *                   this basket is empty
   */
  Map<Integer, Double> getNDaysMovingAverages(Calendar startDate, Calendar endDate, int days)
          throws Exception;

  @Override
  String toString();


}