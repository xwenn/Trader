package model.trader;

import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

/**
 * This class represents a junit test for DollarAverageStrategy class.
 */
public class DollarAverageStrategyTest {

  /**
   * Test the last investment is on non-business day, but next business day is available.
   */
  @Test
  public void test1() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    Calendar start = new GregorianCalendar(2017, 5 - 1, 1);
    Calendar end = new GregorianCalendar(2017, 6 - 1, 3);
    int period = 7;
    double money = 10000;

    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket expected = new Basket();
    expected.put("aapl", 132);
    expected.put("goog", 31);

    IBasket resultBasket;
    double resultMoney;
    resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);

    assertEquals(expected.toString(), resultBasket.toString());
    assertEquals(49252.57, resultMoney, 0.001);
  }

  /**
   * Test the last investment is not on business day, and there is no next business day available.
   */
  @Test
  public void test2() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    Calendar start = new GregorianCalendar(2017, 5 - 1, 1);
    Calendar end = new GregorianCalendar(2017, 6 - 1, 3);
    int period = 7;
    double money = 10000;

    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket expected = new Basket();
    expected.put("aapl", 132);
    expected.put("goog", 31);

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);

    assertEquals(expected.toString(), resultBasket.toString());
    assertEquals(49252.57, resultMoney, 0.001);
  }

  /**
   * Test the last investment is on business day, and on the endDate.
   */
  @Test
  public void test3() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar end = new GregorianCalendar(2017, 6 - 1, 15);
    int period = 7;
    double money = 10000;

    IBasket expected = new Basket();
    expected.put("aapl", 80);
    expected.put("goog", 18);

    Strategy dollarAvg = new DollarAverageStrategy();
    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);


    assertEquals(expected.toString(), resultBasket.toString());
    assertEquals(19714.58, resultMoney, 0.001);
  }


  /**
   * Test the last investment is on business day, but not on the endDate.
   */
  @Test
  public void test4() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar end = new GregorianCalendar(2017, 6 - 1, 16);
    int period = 7;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket expected = new Basket();
    expected.put("aapl", 80);
    expected.put("goog", 18);

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);

    assertEquals(expected.toString(), resultBasket.toString());
    assertEquals(29408.56, resultMoney, 0.001);
  }


  /**
   * Test some of the investments are not on business day and are adjusted.
   */
  @Test
  public void test5() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 5 - 1, 29);
    int period = 7;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket expected = new Basket();
    expected.put("aapl", 245);
    expected.put("goog", 59);

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);


    assertEquals(expected.toString(), resultBasket.toString());
    assertEquals(88339.7699, resultMoney, 0.001);
  }

  /**
   * Test Exception when creation date is a future date.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testException1() throws Exception {
    Calendar creation = new GregorianCalendar(2222, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 5 - 1, 29);
    int period = 7;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);
  }

  /**
   * Test Exception when creation date is a non-business day.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testException2() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 1 - 1, 1);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 5 - 1, 29);
    int period = 7;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);
  }

  /**
   * Test Exception when endDate is before startDate.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testException3() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 2 - 1, 29);
    int period = 7;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);
  }

  /**
   * Test Exception when period is less than 7 days.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testException4() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 5 - 1, 29);
    int period = 5;
    double money = 10000;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);
  }

  /**
   * Test Exception when investment money is negative.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testException5() throws Exception {
    Calendar creation = new GregorianCalendar(2017, 4 - 1, 20);
    IBasket plain = new Basket(creation);
    plain.put("AAPL", 20);
    plain.put("GOOG", 5);

    Calendar start = new GregorianCalendar(2017, 3 - 1, 31);
    Calendar end = new GregorianCalendar(2017, 5 - 1, 29);
    int period = 7;
    double money = -100;

    Strategy dollarAvg = new DollarAverageStrategy();

    IBasket resultBasket = (IBasket)dollarAvg.invest(plain, start, end, money, period).get(0);
    double resultMoney = (Double)dollarAvg.invest(plain, start, end, money, period).get(1);
  }
}