package utils;

import java.util.Map;

/**
 * This interface represents all the operations offered by a component that
 * can be used to get stock data.
 */
public interface StockDataRetriever {
  /**
   * Return current price of the specified stock.
   * @param stockSymbol the symbol of the specified stock
   * @return current price of the specified stock
   * @throws Exception if exception occurs when getting current price of the stock from the server
   */
  double getCurrentPrice(String stockSymbol) throws Exception;

  /**
   * Return a string representation of the name of the stock.
   * @param stockSymbol the symbol of the specified stock
   * @return a string representation of the name of the stock
   * @throws Exception if exception occurs when getting the name of the stock from the server
   */
  String getName(String stockSymbol) throws Exception;

  /**
   * Get the historical prices for the specified stock and date range, and return them as a tree
   * whose keys are the dates and values are the price records of the corresponding dates.
   * @param stockSymbol the symbol of the specified stock
   * @param fromDate date of the desired start date
   * @param fromMonth month of the desired start date
   * @param fromYear year of the desired start date
   * @param toDate date of the desired end date
   * @param toMonth month of the desired end date
   * @param toYear year of the desired end date
   * @return a tree whose keys are the dates and values are the price records of the corresponding
   *         dates.
   * @throws Exception if exception occurs when retrieving data from the server
   */
  Map<Integer, PriceRecord> getHistoricalPrices(
          String stockSymbol,
          int fromDate,
          int fromMonth,
          int fromYear,
          int toDate,
          int toMonth,
          int toYear) throws Exception;
}