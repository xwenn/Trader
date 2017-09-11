package model.trader;

import java.util.Calendar;
import java.util.List;

/**
 * This interface represents a certain investment strategy an investor can take.
 */
public interface Strategy {

  /**
   * Buys/sells stocks in the given basket using this investment strategy from start date
   * to end date every set period of time with a set amount of money.
   *
   * @param basket the basket, no matter a plain basket or a strategized basket,
   *               on which this investment strategy is imposed
   * @param startDate the first day of the date range. must be a past, business day. the first
   *                  investment happens this day
   * @param endDate the last day of the date range. must be a past day. may or may not be a
   *                business day
   * @param money amount of money for each investment
   * @param period every "period" day. For example, assume the first investment is on June 1,
   *               every "7" days means the second investment would be on June 8,
   *               the third would be on June 15, etc. If the assigned day is not a business day,
   *               the investment would be postponed to the next business day if applicable.
   *               must be >= 7
   * @return a list of objects, whose first element is the resulting basket on the last day
   *         of the date range, and second element is the total money invested
   *@throws Exception if data source cannot be read correctly,
   *                  or, specifically, throws IllegalArgumentException under these conditions:
   *                  1. at least one of basket, startDate or endDate is @code null
   *                  2. the basket is empty (thus cannot calculate the proportion of stocks
   *                  3. startDate is after endDate (thus duration of investment is 0)
   *                  4. money is less than 0
   *                  5. period is less than 7
   *
   */
  List<Object> invest(IBasket basket, Calendar startDate, Calendar endDate, double money,
                      int period) throws Exception;
}
