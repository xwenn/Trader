package model.trader;
/**
 * This class represents a basket of stocks.
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import utils.CalendarUtil;
import utils.FittingUtil;

public class Basket implements IBasket {
  private Map<IStock, Integer> stocks;
  private Calendar creationTime;

  /**
   * Construct an empty basket, with a default creation date Tuesday, June 20, 2017.
   */
  public Basket() {
    this.stocks = new TreeMap<>();
    // default creation date is Tuesday, June 20, 2017
    creationTime = new GregorianCalendar(2017, 6 - 1, 20);
  }

  /**
   * Construct an empty basket.
   *
   * @param creationTime the creation date of this basket
   * @throws Exception if data source cannot be read correctly. Specifically, if the creation
   *                   time is not a business day or is @code null,
   *                   an IllegalArgumentException will be thrown
   */
  public Basket(Calendar creationTime) throws Exception {
    if (!CalendarUtil.isBusinessDay(creationTime)) {
      throw new IllegalArgumentException();
    }

    this.stocks = new TreeMap<>();
    this.creationTime = creationTime;
  }

  @Override
  public Calendar getCreationTime() {
    int year = this.creationTime.get(Calendar.YEAR);
    int month = this.creationTime.get(Calendar.MONTH);
    int day = this.creationTime.get(Calendar.DAY_OF_MONTH);
    return new GregorianCalendar(year, month, day);
  }

  @Override
  public void incrementShareOf(String stockSymbol, int share) throws Exception {
    if (share <= 0) {
      throw new IllegalArgumentException("at least increment by 1");
    }

    if (stockSymbol == null) {
      throw new IllegalArgumentException("null argument");
    }

    if (!this.containsStock(stockSymbol)) {
      throw new IllegalArgumentException("no such stock in this basket");
    }

    IStock stock = new Stock(stockSymbol);
    int newShare = this.stocks.get(stock) + share;
    this.stocks.put(stock, newShare);
  }

  @Override
  public boolean containsStock(String stockSymbol) throws Exception {
    if (stockSymbol == null) {
      throw new IllegalArgumentException();
    }
    return this.stocks.containsKey(new Stock(stockSymbol));
  }

  @Override
  public Double getClosingPrice(Calendar date) throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }

    // use this tester stock to test whether the given date is a business day or not
    IStock tester = new Stock("AAPL");
    tester.getClosingPrice(date);

    double sumClose = 0;
    for (Map.Entry<IStock, Integer> pair : stocks.entrySet()) {
      sumClose += pair.getKey().getClosingPrice(date) * pair.getValue();
    }
    return sumClose;
  }

  @Override
  public void put(String stockSymbol, int share) throws Exception {
    if (share <= 0) {
      throw new IllegalArgumentException("at least 1 share");
    }
    Stock toPut = new Stock(stockSymbol);
    stocks.put(toPut, share);
  }

  @Override
  public void remove(String stockSymbol) throws Exception {
    Stock toRemove;
    try {
      toRemove = new Stock(stockSymbol);
      stocks.remove(toRemove);
    } catch (IllegalArgumentException e) {
      // do nothing.
    }
  }

  @Override
  public Map<IStock, Integer> getStocks() throws Exception {
    Map<IStock, Integer> copy = new TreeMap<>();

    for (Map.Entry<IStock, Integer> entry : stocks.entrySet()) {
      copy.put(new Stock(entry.getKey().getSymbol()), entry.getValue().intValue());
    }
    return copy;
  }

  @Override
  public Map<Integer, Double> getClosingPrices(Calendar startDate, Calendar endDate)
          throws Exception {

    if (stocks == null || stocks.size() == 0) {
      throw new IllegalArgumentException("cannot get the prices of an empty basket");
    }

    if (endDate.before(startDate)) {
      throw new IllegalArgumentException("end date should not be prior to start date");
    }

    // retrieve historical closing prices for each stock and store in a map
    Map<IStock, Map<Integer, Double>> basketPrices = new TreeMap<>();
    Map<Integer, Double> prices = new TreeMap<>();
    for (Map.Entry<IStock, Integer> aStock : stocks.entrySet()) {
      prices = aStock.getKey().getClosingPrices(startDate, endDate);
      basketPrices.put(aStock.getKey(), prices);
    }

    // initialize an array to store the total closing prices for the basket
    Set<Integer> dates = prices.keySet();
    Map<Integer, Double> closingPrices = new TreeMap<>();

    // calculate total closing prices for the basket and put in the map
    int i = 0;
    Iterator<Integer> it = dates.iterator();
    while (it.hasNext()) {
      Integer date = it.next();
      double sum = 0;
      for (Map.Entry<IStock, Map<Integer, Double>> stockPrices : basketPrices.entrySet()) {
        sum += stockPrices.getValue().get(date) * stocks.get(stockPrices.getKey());
      }
      // round the double to 2 decimal places
      closingPrices.put(date, Math.round(sum * 100) / 100.0);
      i++;
    }

    return closingPrices;
  }


  @Override
  public double trends(Calendar startDate, Calendar endDate) throws Exception {

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
  public double movingAverage(int k, Calendar startDate) throws Exception {
    if (k < 1) {
      throw new IllegalArgumentException("must be at least 1 day");
    }
    if (stocks == null || stocks.isEmpty()) {
      throw new IllegalArgumentException("basket is empty");
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
  public Map<Integer, Double> getNDaysMovingAverages(Calendar startDate,
                                                     Calendar endDate, int days) throws Exception {

    if (days <= 0) {
      throw new IllegalArgumentException("days should be positive");
    }
    Integer startDateInt = startDate.get(Calendar.YEAR) * 10000
            + (startDate.get(Calendar.MONTH) + 1) * 100
            + startDate.get(Calendar.DAY_OF_MONTH);

    // retrieve more than needed from the server
    Calendar retrieveStart = new GregorianCalendar(startDate.get(Calendar.YEAR),
            startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
    retrieveStart.add(Calendar.DAY_OF_YEAR, -2 * days);
    Map<Integer, Double> retrievedData = getClosingPrices(retrieveStart, endDate);
    NavigableMap<Integer, Double> reveredDate =
            ((TreeMap<Integer, Double>) retrievedData).descendingMap();

    Map<Integer, Double> result = new TreeMap<>();
    Integer currDateInt = startDateInt;
    while (currDateInt >= startDateInt) {
      currDateInt = reveredDate.firstKey();
      if (currDateInt >= startDateInt) {
        Double currAverage = firstNAverage(reveredDate, days);
        result.put(currDateInt, currAverage);
      }
      Map.Entry<Integer, Double> removed = reveredDate.pollFirstEntry();
    }

    return result;
  }

  /**
   * Return the average of the first specified number of values in the given map.
   *
   * @param map the specified map
   * @param n   the specified number of values to be averaged
   * @return the average of the first specified number of values in the given map
   * @throws IllegalArgumentException if the given number is greater than the size of the map, or if
   *                                  the given number is not positive
   */
  private Double firstNAverage(Map<Integer, Double> map, int n) throws IllegalArgumentException {
    if (n > map.size()) {
      throw new IllegalArgumentException("n is greater than map size");
    }
    if (n <= 0) {
      throw new IllegalArgumentException("n must be positive");
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
  public String toString() {
    String str = "";
    for (Map.Entry<IStock, Integer> pair : stocks.entrySet()) {
      str += pair.getKey().getName() + " * " + pair.getValue() + ", ";
    }
    if (str.length() < 2) {
      return str;
    }
    return str.substring(0, str.length() - 2);
  }

  @Override
  public void setStrategy(Strategy strategy) throws IllegalArgumentException {
    if (strategy == null) {
      throw new IllegalArgumentException();
    }
    // do nothing.
  }

  @Override
  public Double calcProfits(Calendar startDate, Calendar endDate, double money,
                            int period, Calendar date) throws Exception {
    return null;
  }
}
