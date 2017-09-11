package model.trader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

import utils.FittingUtil;
import utils.PriceRecord;
import utils.StockDataRetriever;
import utils.WebStockDataRetriever;

/**
 * This class represents a stock. It has a stock symbol and a stock name.
 */
public class Stock implements IStock {
  private final String stockSymbol;
  private final String stockName;

  /**
   * Constructs a stock object and initializes this stock with the specified stock symbol.
   *
   * @param stockSymbol the stock symbol of this stock. model.Stock symbols can be either uppercase
   *                    or lowercase, or a mixture of uppercase and lowercase.
   * @throws Exception if the data source file is not read correctly. Throws
   *                   IllegalArgumentException if there is no such stock, or the symbol does not
   *                   contain characters other than whitespaces
   */
  public Stock(String stockSymbol) throws Exception {
    // check if this symbol is blank
    Scanner sc = new Scanner(stockSymbol);
    if (!sc.hasNext()) {
      throw new IllegalArgumentException("cannot be made up of only whitespaces");
    }

    StockDataRetriever retriever = new WebStockDataRetriever();
    String name = retriever.getName(stockSymbol);

    if (name.equals("N/A")) {
      throw new IllegalArgumentException("invalid stock symbol");
    }

    this.stockSymbol = stockSymbol.toUpperCase();
    this.stockName = name.substring(1);
  }

  @Override
  public double getClosingPrice(Calendar date) throws Exception {
    return getPriceRecord(date).getClosePrice();
  }

  @Override
  public Map<Integer, Double> getClosingPrices(Calendar startDate, Calendar endDate)
          throws IllegalArgumentException {

    if (endDate.before(startDate)) {
      throw new IllegalArgumentException("end date should not be prior to start date");
    }

    int fromDate = startDate.get(Calendar.DAY_OF_MONTH);
    int fromMonth = startDate.get(Calendar.MONTH) + 1; // 0-based to 1-based
    int fromYear = startDate.get(Calendar.YEAR);
    int toDate = endDate.get(Calendar.DAY_OF_MONTH);
    int toMonth = endDate.get(Calendar.MONTH) + 1; // 0-based to 1-based
    int toYear = endDate.get(Calendar.YEAR);

    WebStockDataRetriever retriever = new WebStockDataRetriever();

    Map<Integer, PriceRecord> allPrices;
    try {
      allPrices = retriever.getHistoricalPrices(stockSymbol,
              fromDate, fromMonth, fromYear,
              toDate, toMonth, toYear);
    } catch (Exception e) {
      // historical prices record is empty when exceptions occur in data retriever
      allPrices = new TreeMap<>();
    }

    Map<Integer, Double> closingPrices = new TreeMap<>();
    for (Map.Entry<Integer, PriceRecord> entry : allPrices.entrySet()) {
      int date = entry.getKey();
      double closingPrice = entry.getValue().getClosePrice();
      closingPrices.put(date, closingPrice);
    }

    return closingPrices;
  }

  @Override
  public double movingAverage(int k, Calendar startDate) throws Exception {
    if (k < 1) {
      throw new IllegalArgumentException("must be at least 1 day");
    }
    Map<Integer, Double> map = getNDaysMovingAverages(startDate, startDate, k);
    Calendar date = new GregorianCalendar(startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
    while (map.isEmpty()) {
      date.add(Calendar.DAY_OF_YEAR, -1);
      map = getNDaysMovingAverages(date, date, k);
    }

    double result = 0;
    for (Map.Entry<Integer, Double> entry : map.entrySet()) {
      result = entry.getValue();
    }
    return result;
  }

  @Override
  public boolean isBuyingOpportunity(Calendar date) throws Exception {
    return movingAverage(50, date) > movingAverage(200, date);
  }

  @Override
  public double trends(Calendar startDate, Calendar endDate) throws IllegalArgumentException {

    if (endDate.before(startDate)) {
      throw new IllegalArgumentException("end date should not be prior to start date");
    }

    Map<Integer, Double> closingPrices = getClosingPrices(startDate, endDate);
    List<Double> values = new ArrayList<>();
    for (Map.Entry<Integer, Double> entry : closingPrices.entrySet()) {
      values.add(entry.getValue());
    }

    if (values.isEmpty()) {
      throw new IllegalArgumentException("no closing price data within the date range");
    }

    return FittingUtil.twoEndFittingTrend(values);
  }

  @Override
  public Map<Integer, Double> getNDaysMovingAverages(
          Calendar startDate, Calendar endDate, int days) throws Exception {
    if (days <= 0) {
      throw new IllegalArgumentException("days should be positive");
    }
    Integer startDateInt = startDate.get(Calendar.YEAR) * 10000
            + (startDate.get(Calendar.MONTH) + 1) * 100
            + startDate.get(Calendar.DAY_OF_MONTH);

    // retrieve more than needed from the server
    Calendar retrieveStart = new GregorianCalendar(
            startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH),
            startDate.get(Calendar.DAY_OF_MONTH));
    retrieveStart.add(Calendar.DAY_OF_YEAR, -2 * days);
    Map<Integer, Double> retrievedData = getClosingPrices(retrieveStart, endDate);
    NavigableMap<Integer, Double> reveredDate =
            ((TreeMap<Integer, Double>) retrievedData).descendingMap();


    Map<Integer, Double> result = new TreeMap<>();
    Integer currDateInt = startDateInt + 1;
    while (currDateInt > startDateInt) {
      currDateInt = reveredDate.firstKey();
      if (currDateInt >= startDateInt) {
        Double currAverage = firstNAverage(reveredDate, days);
        result.put(currDateInt, currAverage);
      }
      Map.Entry<Integer, Double> removed = reveredDate.pollFirstEntry();
    }

    return result;
  }

  private Double firstNAverage(Map<Integer, Double> map, int n) {
    if (n > map.size()) {
      throw new IllegalArgumentException("n is greater than map size");
    }
    int i = 0;
    double sum = 0;
    for (Map.Entry<Integer, Double> entry : map.entrySet()) {
      if (i >= n) {
        break;
      }
      sum += entry.getValue();
      i++;
    }
    return Math.round(sum * 100 / n) / 100.0;
  }

  @Override
  public String getName() {
    return this.stockName;
  }

  @Override
  public String getSymbol() {
    return this.stockSymbol;
  }

  @Override
  public String toString() {
    return "stock symbol: " + stockSymbol + ", stock name: " + stockName;
  }

  @Override
  public int compareTo(IStock other) {
    // in alphabetical order, non-case-sensitive
    String thisSymbolUpperCase = this.stockSymbol.toUpperCase();
    String otherSymbolUpperCase = other.getSymbol().toUpperCase();
    return thisSymbolUpperCase.compareTo(otherSymbolUpperCase);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Stock)) {
      return false;
    } else {
      Stock other = (Stock) obj;
      return this.stockSymbol.equalsIgnoreCase(other.stockSymbol);
    }
  }

  // not used, just required by code style grader.
  @Override
  public int hashCode() {
    int hash;
    hash = 3 * stockName.hashCode();
    hash = hash + stockSymbol.hashCode();
    return hash;
  }


  /**
   * Retrieves the price record of this stock on the specified day.
   *
   * @param date the date of the specified day
   * @return the price record of this stock on that day
   * @throws Exception if the data source is not read correctly, or throws IllegalArgumentException
   *                   if the specified day is a weekend, future day, or public holiday
   */
  private PriceRecord getPriceRecord(Calendar date) throws Exception {
    PriceRecord price = null;
    StockDataRetriever retriever = new WebStockDataRetriever();
    Map<Integer, PriceRecord> recordsMap;
    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
    int month = date.get(Calendar.MONTH) + 1;
    int year = date.get(Calendar.YEAR);

    recordsMap = retriever.getHistoricalPrices(stockSymbol, dayOfMonth, month, year,
            dayOfMonth, month, year);

    for (Map.Entry<Integer, PriceRecord> pair : recordsMap.entrySet()) {
      price = pair.getValue();
    }
    if (price == null) {
      throw new IllegalArgumentException("data does not exist");
    }

    return price;
  }

  /**
   * Retrieve closing prices of this stock in the past numDays days starting from startDate.
   *
   * @param numDays   the period of time
   * @param startDate the starting date
   * @return a map mapping the date and the price record of this stock on that day
   * @throws Exception if the data source file is not ready correctly, or numDays < 1
   */
  private Map<Calendar, Double> getClosingPricesOfKDays(int numDays, Calendar startDate)
          throws Exception {
    if (numDays < 1) {
      throw new IllegalArgumentException("must be at least 1 day");
    }
    Map<Calendar, Double> closingPriceMap = new TreeMap<>();
    Map<Calendar, PriceRecord> priceMap = getPricesOfKDays(numDays, startDate);

    for (Map.Entry<Calendar, PriceRecord> pair : priceMap.entrySet()) {
      closingPriceMap.put(pair.getKey(), pair.getValue().getClosePrice());
    }

    return closingPriceMap;
  }

  /**
   * Retrieves price record of this stock in the past numDays days starting from date startDate.
   *
   * @param numDays   the period of time
   * @param startDate the starting date
   * @return a map mapping the date and the price record of this stock on that day
   * @throws Exception if the data source file is not ready correctly or numDays < 1
   */
  private Map<Calendar, PriceRecord> getPricesOfKDays(int numDays, Calendar startDate)
          throws Exception {
    if (numDays < 1) {
      throw new IllegalArgumentException("must be at least 1 day");
    }
    Map<Calendar, PriceRecord> recordsMap = new TreeMap<>();
    int counter = numDays;
    Calendar currDate = new GregorianCalendar(startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

    while (counter > 0) {
      try {
        recordsMap.put(currDate, getPriceRecord(currDate));
        counter--;
      } catch (IllegalArgumentException e) {
        //
      }

      currDate = new GregorianCalendar(currDate.get(Calendar.YEAR),
              currDate.get(Calendar.MONTH), currDate.get(Calendar.DAY_OF_MONTH) - 1);
    }

    return recordsMap;
  }

}
