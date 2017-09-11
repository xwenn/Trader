package utils;

import java.util.List;

/**
 * Has utility methods to fit and calculate the trend of discrete data provided in an array.
 */
public class FittingUtil {

  /**
   * Calculate and return the slope of the fitting line that joins the first data point to the last
   * data point.
   *
   * @param values the array that stores the data
   * @return the slope of the fitting line that joints the first data point to the last date point
   */
  public static double twoEndFittingTrend(List<Double> values)
          throws IllegalArgumentException {
    if (values == null || values.isEmpty()) {
      throw new IllegalArgumentException("input data must be a non empty list");
    }

    double start = values.get(0);
    double end = values.get(values.size() - 1);

    if (values.size() == 1) {
      return 0;
    }

    return (end - start) / (double) (values.size() - 1);
  }
}