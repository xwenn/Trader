package model.trader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.CalendarUtil;

/**
 * This class represents the dollar-average strategy and contains a single method that
 * returns a basket by imposing this investment strategy on the given basket, whatever
 * strategies the given basket itself has.
 */
public class DollarAverageStrategy implements Strategy {

  @Override
  public List<Object> invest(IBasket basket, Calendar startDate, Calendar endDate,
                             double money, int period) throws Exception {
    if (basket == null || startDate == null || endDate == null) {
      throw new IllegalArgumentException("null date(s)");
    }

    // requires a non-empty basket
    if (basket.getStocks().size() == 0) {
      throw new IllegalArgumentException("empty basket");
    }
    // mandates the startDate is a past business day
    if (!CalendarUtil.isBusinessDay(startDate)) {
      throw new IllegalArgumentException("start date should be a past business day");
    }

    // mandates the endDate is a past day, but not necessarily a business day
    if (CalendarUtil.isFutureDay(endDate)) {
      throw new IllegalArgumentException("end date should be a past day");
    }

    // check other arguments
    if (startDate.after(endDate) || money < 0 || period < 7) {
      throw new IllegalArgumentException();
    }

    List<Object> result = new ArrayList<>();
    double totalInvested = 0;

    if (money == 0) {
      result.add(new Basket());
      result.add(totalInvested);
    }


    // calculates the proportion of each stock
    Map<IStock, Double> stockProportionMap = getProportions(basket);

    IBasket newBasket = new Basket();
    Calendar currentDate = CalendarUtil.copyDate(startDate);
    while (currentDate.before(endDate)) {
      Calendar actualInvestDate = CalendarUtil.nextBusinessDayBefore(currentDate, endDate);
      if (actualInvestDate != null) {
        totalInvested += individualInvest(newBasket, stockProportionMap, money, actualInvestDate);
      }
      currentDate.add(Calendar.DAY_OF_MONTH, period);
    }

    if (!currentDate.after(endDate) && CalendarUtil.isBusinessDay(currentDate)) {
      individualInvest(newBasket, stockProportionMap, money, currentDate);
    }


    result.add(newBasket);
    result.add(totalInvested);

    return result;
  }

  /**
   * Invests the specified amount of money according to the stock-proportion map and stock prices
   * on the specified date. The specified date must be a past business day.
   * @param basket a basket of stocks
   * @param proportions a stock-proportion map
   * @param money amount of money for an individual investment
   * @param date the date on which the investment is scheduled
   * @return money invested in this investment
   * @throws Exception if data source cannot be read correctly
   */
  public double individualInvest(IBasket basket, Map<IStock, Double> proportions, double money,
                                Calendar date) throws Exception {
    if (basket == null
            || proportions == null
            || proportions.size() == 0
            || date == null
            || money < 0
            || !CalendarUtil.isBusinessDay(date)) {
      throw new IllegalArgumentException();
    }

    double moneyInvested = 0;

    Map<IStock, Integer> shares = calcShares(proportions, money, date);
    for (Map.Entry<IStock, Integer> pair : shares.entrySet()) {
      IStock stock = pair.getKey();
      int share = pair.getValue();
      moneyInvested += stock.getClosingPrice(date) * share;
      if (basket.containsStock(stock.getSymbol())) {
        basket.incrementShareOf(stock.getSymbol(), share);
      } else {
        basket.put(stock.getSymbol(), share);
      }
    }
    return moneyInvested;
  }


  /**
   * Return a map whose values are the stocks, and values are the shares of the corresponding
   * stock.
   * @param stockProportionMap the map represents the proportion of each stock
   * @param money amount of money that will be used to purchase the stock
   * @param date purchase date
   * @return a map whose values are the stock, and values are the shares of the corresponding
   *         stock
   * @throws Exception if the given money is less than 0, or if the given date is a non-business,
   *         or if the source file is not read correctly
   */
  private Map<IStock, Integer> calcShares(Map<IStock, Double> stockProportionMap,
                                          double money, Calendar date) throws Exception {
    if (stockProportionMap == null
            || stockProportionMap.size() == 0
            || money < 0
            || date == null
            || !CalendarUtil.isBusinessDay(date)) {
      throw new IllegalArgumentException();
    }

    Map<IStock, Integer> stockShareMap = new TreeMap<>();

    for (Map.Entry<IStock, Double> pair : stockProportionMap.entrySet()) {
      IStock stock = pair.getKey();
      Double proportion = pair.getValue();
      Double price = stock.getClosingPrice(date);
      // assuming only whole number of shares can be purchased
      int share = (int)Math.round(proportion * money / price);
      stockShareMap.put(stock, share);
    }

    return stockShareMap;
  }

  /**
   * Return a map whose values are the stocks, and values are the proportion of the corresponding
   * stocks.
   * @param basket the basket to be calculated
   * @return a map whose values are the stocks, and values are the proportion of the corresponding
   *         stocks
   * @throws Exception if the source file is not read correctly
   */
  public Map<IStock, Double> getProportions(IBasket basket) throws Exception {
    Calendar creation = basket.getCreationTime();
    Map<IStock, Double> proportions = new TreeMap<>();

    double sum = basket.getClosingPrice(creation);

    for (Map.Entry<IStock, Integer> pair : basket.getStocks().entrySet()) {
      double price = pair.getKey().getClosingPrice(creation);
      int share = pair.getValue();
      double proportion = share * price / sum;
      proportions.put(pair.getKey(), proportion);

    }

    return proportions;
  }
}
