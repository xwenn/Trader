package utils;

/**
 * This class represents the record of a price of a single stock item in a day.
 */
public class PriceRecord {
  private final double open;
  private final double close;
  private final double highest;
  private final double lowest;

  /**
   * Construct a utils.PriceRecord according to the given open, close, lowest and highest prices.
   *
   * @param open    the specified open price
   * @param close   the specified closing price
   * @param lowest  the specified lowest price
   * @param highest the specified highest price
   */
  public PriceRecord(double open, double close, double lowest, double highest) {
    this.open = open;
    this.close = close;
    this.highest = highest;
    this.lowest = lowest;
  }

  /**
   * Return the open price of this price record.
   *
   * @return the open price of this price record
   */
  public double getOpenPrice() {
    return open;
  }

  /**
   * Return the closing price of this price record.
   *
   * @return the closing price of this price record
   */
  public double getClosePrice() {
    return close;
  }

  /**
   * Return the lowest price of this price record.
   *
   * @return the lowest price of this price record
   */
  public double getLowestDayPrice() {
    return lowest;
  }

  /**
   * Return the highest price of this price record.
   *
   * @return the highest price of this price record
   */
  public double getHighestDayPrice() {
    return highest;
  }


  @Override
  public String toString() {
    return "open: " + open + ", high: " + highest + ", low: " + lowest + ", close: " + close;
  }
}

