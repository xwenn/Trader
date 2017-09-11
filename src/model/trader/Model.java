package model.trader;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import model.trader.IBasket;
import model.trader.IModel;

/**
 * This class represents a model. The model only cares about processing the given data,
 * and does not care about where the data come from and how to show them to users.
 * The model has a stock and a basket of stocks.
 */
public class Model implements IModel {
  // to store a bunch of strategized baskets that can be accessed to with their names
  private Map<String, IBasket> basketsMap;

  /**
   * Constructs and initializes a model with an empty basketsMap.
   */
  public Model() {
    basketsMap = new TreeMap<>(); // initializes the basket map to an empty map
  }


  /*
  Operations on investment strategies
   */

  @Override
  public double profit(String basketName, Strategy strategy, Calendar start, Calendar end,
                       double money, int period, Calendar date) throws Exception {
    if (!this.containsBasket(basketName)) {
      throw new IllegalArgumentException();
    }

    IBasket calledBasket = basketsMap.get(basketName);

    IBasket strategized = new StrategizedBasket(calledBasket.getCreationTime());
    for (Map.Entry<IStock, Integer> pair : calledBasket.getStocks().entrySet()) {
      strategized.put(pair.getKey().getSymbol(), pair.getValue());
    }

    strategized.setStrategy(strategy);
    return strategized.calcProfits(start, end, money, period, date);
  }

  /*
  Operations on a single stock
   */
  @Override
  public boolean isValidStockSymbol(String symbol) throws Exception {
    try {
      new Stock(symbol);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  @Override
  public double getStockClosingPrice(String stockSymbol, Calendar date) throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }

    IStock stock = new Stock(stockSymbol);
    return stock.getClosingPrice(date);
  }

  @Override
  public Map<Integer, Double> getStockClosingPrices(String stockSymbol, Calendar startDate,
                                                    Calendar endDate) throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }
    IStock stock = new Stock(stockSymbol);
    return stock.getClosingPrices(startDate, endDate);
  }

  @Override
  public double fiftyDaysMovingAverageOfStock(String stockSymbol, Calendar date) throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }
    IStock stock = new Stock(stockSymbol);
    return stock.movingAverage(50, date);
  }

  @Override
  public double twoHundredsDaysMovingAverageOfStock(String stockSymbol, Calendar date)
          throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }

    IStock stock = new Stock(stockSymbol);
    return stock.movingAverage(200, date);
  }

  @Override
  public Map<Integer, Double> fiftyDaysMovingAveragesOfStock(
          String stockSymbol, Calendar startDate, Calendar endDate) throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }
    IStock stock = new Stock(stockSymbol);
    return stock.getNDaysMovingAverages(startDate, endDate, 50);
  }

  @Override
  public Map<Integer, Double> twoHundredsDaysMovingAveragesOfStock(
          String stockSymbol, Calendar startDate, Calendar endDate) throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }
    IStock stock = new Stock(stockSymbol);
    return stock.getNDaysMovingAverages(startDate, endDate, 200);
  }

  @Override
  public double stockTrend(String stockSymbol, Calendar startDate, Calendar endDate)
          throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }
    IStock stock = new Stock(stockSymbol);
    return stock.trends(startDate, endDate);
  }

  /*
  Operations on baskets
   */

  @Override
  public void createEmptyBasket(String basketName) throws IllegalArgumentException {
    if (basketsMap.containsKey(basketName)) {
      throw new IllegalArgumentException("basket with the same name has been created");
    }
    IBasket newBasket = new StrategizedBasket();
    this.basketsMap.put(basketName, newBasket);
  }

  @Override
  public void createEmptyBasket(String basketName, Calendar date)
          throws Exception {

    if (basketsMap.containsKey(basketName)) {
      throw new IllegalArgumentException("basket with the same name has been created");
    }

    IBasket newBasket = new StrategizedBasket(date);
    this.basketsMap.put(basketName, newBasket);
  }

  @Override
  public void addStockInto(String stockSymbol, int share, String basketName) throws Exception {
    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      if (calledBasket.containsStock(stockSymbol)) {
        calledBasket.incrementShareOf(stockSymbol, share);
      } else {
        calledBasket.put(stockSymbol, share);
      }
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public double basketTrends(String basketName, Calendar startDate, Calendar endDate)
          throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }

    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      return calledBasket.trends(startDate, endDate);
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public Double getBasketValue(String basketName, Calendar date) throws Exception {
    IBasket calledBasket = this.basketsMap.get(basketName);

    if (date == null) {
      throw new IllegalArgumentException();
    }

    if (calledBasket != null) {
      try {
        return calledBasket.getClosingPrice(date);
      } catch (IllegalArgumentException e) {
        return null;
      }
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public Map<Integer, Double> getBasketValues(String basketName, Calendar startDate,
                                              Calendar endDate) throws Exception {
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }

    /*
    Make sure the specified basket is available
     */
    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      try {
        return calledBasket.getClosingPrices(startDate, endDate);
      } catch (IllegalArgumentException e) {
        return new TreeMap<>();
      }
    } else {
      throw new IllegalArgumentException("no such basket");
    }

  }

  @Override
  public double fiftyDaysMovingAverageOfBasket(String basketName, Calendar date) throws Exception {
    IBasket calledBasket = this.basketsMap.get(basketName);

    if (date == null) {
      throw new IllegalArgumentException();
    }

    if (calledBasket != null) {
      return calledBasket.movingAverage(50, date);
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public double twoHundredsDaysMovingAverageOfBasket(String basketName, Calendar date)
          throws Exception {
    if (date == null) {
      throw new IllegalArgumentException();
    }
    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      return calledBasket.movingAverage(200, date);
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public Map<Integer, Double> fiftyDaysMovingAveragesOfBasket(
          String basketName, Calendar startDate, Calendar endDate) throws Exception {

    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }

    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      try {
        return calledBasket.getNDaysMovingAverages(startDate, endDate, 50);
      } catch (IllegalArgumentException e) {
        return new TreeMap<>();
      }
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public Map<Integer, Double> twoHundredsDaysMovingAveragesOfBasket(
          String basketName, Calendar startDate, Calendar endDate) throws Exception {

    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException();
    }

    IBasket calledBasket = this.basketsMap.get(basketName);

    if (calledBasket != null) {
      try {
        return calledBasket.getNDaysMovingAverages(startDate, endDate, 200);
      } catch (IllegalArgumentException e) {
        return new TreeMap<>();
      }
    } else {
      throw new IllegalArgumentException("no such basket");
    }
  }

  @Override
  public String toString() {
    String result = "";

    for (Map.Entry<String, IBasket> nameBasketPair : basketsMap.entrySet()) {
      result += nameBasketPair.getKey() + ": " + nameBasketPair.getValue().toString() + "\n";
    }

    return result;
  }

  @Override
  public String basketToString(String basketName) throws IllegalArgumentException {
    IBasket calledBasket = this.basketsMap.get(basketName);
    if (calledBasket == null) {
      throw new IllegalArgumentException("no such basket");
    }

    String str = basketName;
    str += ": ";
    str += calledBasket.toString();

    return str;
  }

  @Override
  public boolean containsBasket(String basketName) {
    return basketsMap.containsKey(basketName);
  }

  @Override
  public void removeBasket(String basketName) {
    basketsMap.remove(basketName);
  }
}
