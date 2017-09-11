package utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import utils.FittingUtil;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for utils.FittingUtil class.
 */
public class FittingUtilTest {

  /**
   * Test if the twoEndFittingTrend() method throws an IllegalArgumentException if the given array
   * is empty.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testTwoEndFittingException() {
    List<Double> list = new ArrayList<>();
    FittingUtil.twoEndFittingTrend(list);
  }

  /**
   * Test if the twoEndFittingTrend() method return expected results.
   */
  @Test
  public void testTwoEndFitting() {
    List<Double> list = new ArrayList<>();
    list.add(153.67);
    assertEquals(0, FittingUtil.twoEndFittingTrend(list), 0.0001);

    list.add(152.76);
    list.add(153.18);
    list.add(155.45);
    assertEquals(0.5933, FittingUtil.twoEndFittingTrend(list), 0.0001);

    list.add(153.93);
    list.add(154.45);
    list.add(155.37);
    list.add(154.99);
    list.add(148.98);
    assertEquals(-0.58625, FittingUtil.twoEndFittingTrend(list), 0.0001);
  }

}