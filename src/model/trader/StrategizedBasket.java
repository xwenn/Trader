package model.trader;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import utils.CalendarUtil;

/**
 * This class represents a strategized basket, i.e., a basket with an investment strategy.
 * It adds features to any IBasket objects so that the wrapped basket can now have a
 * investment strategy.
 */

public class StrategizedBasket implements IBasket {
  private IBasket basket;
  private Strategy strategy;


  public StrategizedBasket() {
    this.basket = new Basket();
    this.strategy = new DollarAverageStrategy();
  }

  /**
   * Constructs a new, empty, strategized basket with the given creation time and strategy.
   * default investment strategy is dollar-average strategy.
   * @param creationTime the creation time of this basket
   * @throws Exception if data source cannot be read correctly. Specifically, throws
   *                    IllegalArgumentException if the specified creation date is not business day
   *                    or is @code null
   */
  public StrategizedBasket(Calendar creationTime) throws Exception {
    // this line will throw an IllegalArgumentException if creation date
    // is not business day or is null
    IBasket newBasket = new Basket(creationTime);
    this.basket = newBasket;
    this.strategy = new DollarAverageStrategy();
  }

  @Override
  public void setStrategy(Strategy strategy) throws IllegalArgumentException {
    if (strategy == null) {
      throw new IllegalArgumentException();
    }
    this.strategy = strategy;
  }

  @Override
  public Double calcProfits(Calendar startDate, Calendar endDate,
                            double money, int period, Calendar date) throws Exception {
    if (money < 0
            || startDate == null
            || endDate == null
            || date == null
            || period < 7
            || startDate.after(endDate)
            || !CalendarUtil.isBusinessDay(startDate)
            || CalendarUtil.isFutureDay(endDate)
            || !CalendarUtil.isBusinessDay(date)
            || date.before(endDate)) {
      throw new IllegalArgumentException();
    }

    List<Object> result = strategy.invest(this.basket, startDate, endDate,
            money, period);
    IBasket resultBasket; // to make the compiler happy
    double investedMoney; // to make the compiler happy
    resultBasket = (IBasket)result.get(0);
    investedMoney = (Double)result.get(1);

    return resultBasket.getClosingPrice(date) - investedMoney;
  }

  @Override
  public Calendar getCreationTime() {
    return this.basket.getCreationTime();
  }

  @Override
  public void put(String stockSymbol, int share) throws Exception {
    this.basket.put(stockSymbol, share);
  }

  @Override
  public void remove(String stockSymbol) throws Exception {
    this.basket.remove(stockSymbol);
  }

  @Override
  public Map<IStock, Integer> getStocks() throws Exception {
    return this.basket.getStocks();
  }

  @Override
  public boolean containsStock(String stockSymbol) throws Exception {
    return this.basket.containsStock(stockSymbol);
  }

  @Override
  public void incrementShareOf(String stockSymbol, int share) throws Exception {
    this.basket.incrementShareOf(stockSymbol, share);
  }

  @Override
  public Double getClosingPrice(Calendar date) throws Exception {
    return this.basket.getClosingPrice(date);
  }

  @Override
  public Map<Integer, Double> getClosingPrices(Calendar startDate, Calendar endDate)
          throws Exception {
    return this.basket.getClosingPrices(startDate, endDate);
  }

  @Override
  public double trends(Calendar startDate, Calendar endDate) throws Exception {
    return this.basket.trends(startDate, endDate);
  }

  @Override
  public double movingAverage(int k, Calendar startDate) throws Exception {
    return this.basket.movingAverage(k, startDate);
  }

  @Override
  public Map<Integer, Double> getNDaysMovingAverages(Calendar startDate, Calendar endDate, int days)
          throws Exception {
    return this.basket.getNDaysMovingAverages(startDate, endDate, days);
  }

  @Override
  public String toString() {
    return this.basket.toString();
  }




}
