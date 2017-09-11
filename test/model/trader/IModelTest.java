package model.trader;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This class is a JUint test class for IModel.
 */
public class IModelTest {
  private IModel model0;
  private IModel model1;

  /**
   * Set up some objects for testing.
   */
  @Before
  public void setUp() {
    // set up a model with no baskets
    model0 = new Model();
    model1 = new Model();
  }

  @Test
  public void testConstructor() {
    // test whether the model constructor successfully constructs a model with zero baskets
    assertEquals("", model0.toString());
    assertEquals("", model1.toString());
  }

  /**
   * Tests whether void createEmptyBasket(String basketName) works properly.
   */
  @Test
  public void testCreateEmptyBasket() {
    // create a basket named "recreation"
    model0.createEmptyBasket("recreation");
    // test if there is one and only one empty basket in model
    String expected = "recreation: \n";
    assertEquals(expected, model0.toString());

    // create a basket named "education"
    model0.createEmptyBasket("communication");
    // test if there are two and only two empty baskets in model
    expected = "communication: \nrecreation: \n";
    assertEquals(expected, model0.toString());

    // create a basket named "technology"
    model0.createEmptyBasket("technology");
    // test if there are three and only three empty baskets in model
    expected = "communication: \nrecreation: \ntechnology: \n";
    assertEquals(expected, model0.toString());
  }

  /**
   * Tests whether void createEmptyBasket(String basketName, Calendar date) works properly.
   */
  @Test
  public void testCreateEmptyBasketWithCreationDate() throws Exception {
    Calendar creationDate = new GregorianCalendar(2017, 4 - 1, 21);
    // create a basket named "recreation"
    model1.createEmptyBasket("recreation", creationDate);
    // test if there is one and only one empty basket in model
    String expected = "recreation: \n";
    assertEquals(expected, model1.toString());

    // create a basket named "education"
    model1.createEmptyBasket("communication", creationDate);
    // test if there are two and only two empty baskets in model
    expected = "communication: \nrecreation: \n";
    assertEquals(expected, model1.toString());

    // create a basket named "technology"
    model1.createEmptyBasket("technology", creationDate);
    // test if there are three and only three empty baskets in model
    expected = "communication: \nrecreation: \ntechnology: \n";
    assertEquals(expected, model1.toString());
  }

  /**
   * Test whether the createEmptyBasket(String basketName) throws exception as expected.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testCreateEmptyBasketException() {
    model0.createEmptyBasket("recreation");
    model0.createEmptyBasket("recreation");
  }

  /**
   * Test whether the createEmptyBasket(String basketName, Calendar date) throws exception
   * as expected.
   */
  @Test (expected = IllegalArgumentException.class)
  public void testCreateEmptyBasketWithDateException() throws Exception {
    Calendar creationDate = new GregorianCalendar(2000, 0, 1);
    model0.createEmptyBasket("recreation", creationDate);
    model0.createEmptyBasket("recreation", creationDate);
  }

  /**
   * Tests whether void addStockInto(String stockSymbol, int share, String basketName)
   * works properly when given valid input.
   * @throws Exception if the data source file is not read correctly
   */
  @Test
  public void testAddStockInto() throws Exception {
    model0.createEmptyBasket("recreation");
    model0.createEmptyBasket("technology");
    model0.createEmptyBasket("communication");

    String expected;

    /*
    Basket "recreation": add 100 * AAPL, 300 * DIS
    Basket "technology": add 200 * GOOG, 200 * TSLA
    Basket "communication": empty
     */

    // add 100 shares of "AAPL" into basket "recreation", case doesn't matter
    model0.addStockInto("aaPL", 100, "recreation");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 100";
    expected += "\n";
    expected += "technology: ";
    expected += "\n";
    assertEquals(expected, model0.toString());

    // add 300 shares of "DIS" into basket "recreation"
    model0.addStockInto("DIS", 300, "recreation");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 100, Walt * 300";
    expected += "\n";
    expected += "technology: ";
    expected += "\n";
    assertEquals(expected, model0.toString());

    // add 200 shares of "GOOG" into basket "technology"
    model0.addStockInto("GOOG", 200, "technology");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 100, Walt * 300";
    expected += "\n";
    expected += "technology: Alphabet * 200";
    expected += "\n";
    assertEquals(expected, model0.toString());

    // add 150 shares of "TSLA"into basket "technology"
    model0.addStockInto("TSLA", 200, "technology");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 100, Walt * 300";
    expected += "\n";
    expected += "technology: Alphabet * 200, Tesla, * 200";
    expected += "\n";
    assertEquals(expected, model0.toString());

    /*
    Adds certain shares of a stock to a basket which already has that stock
     */
    // add 30 shares of "AAPL" into basket "recreation"
    model0.addStockInto("AAPL", 30, "recreation");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 130, Walt * 300";
    expected += "\n";
    expected += "technology: Alphabet * 200, Tesla, * 200";
    expected += "\n";
    assertEquals(expected, model0.toString());

    /*
    Adds certain shares of a stock to any basket other than the one that already has it
     */
    // add 100 shares of "GOOG" into "recreation"
    model0.addStockInto("GOOG", 100, "recreation");
    expected = "communication: ";
    expected += "\n";
    expected += "recreation: Apple * 130, Walt * 300, Alphabet * 100";
    expected += "\n";
    expected += "technology: Alphabet * 200, Tesla, * 200";
    expected += "\n";
    assertEquals(expected, model0.toString());
  }

  /**
   * Following three tests test whether method AddIntoBasket throws IllegalArgumentException
   * correctly when given an invalid share.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShareAddIntoBasket() throws Exception {
    model0.createEmptyBasket("abasket");
    model0.addStockInto("GOOG", 0, "abasket");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShareAddIntoBasket2() throws Exception {
    model0.createEmptyBasket("abasket");
    model0.addStockInto("GOOG", -1, "abasket");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidShareAddIntoBasket3() throws Exception {
    model0.createEmptyBasket("aaa");
    model0.addStockInto("GOOG", 100, "aaa");
    model0.addStockInto("GOOG", 0, "aaa");
  }

  /**
   * Tests whether method AddIntoBasket throws IllegalArgumentException correctly when given
   * an invalid ticker symbol.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidSymbolAddIntoBasket() throws Exception {
    model0.createEmptyBasket("abasket");
    model0.addStockInto("GOOGLE", 300, "abasket");
  }

  /**
   * Tests whether method AddIntoBasket throws IllegalArgumentException correctly when
   * given an invalid basket name.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBasketNameAddIntoBasket() throws Exception {
    model0.createEmptyBasket("technology");
    model0.addStockInto("GOOG", 300, "Technology");
  }

  /**
   * Tests whether basketTrends() works properly when given valid inputs.
   * @throws Exception if the data source file is not read correctly
   */
  @Test
  public void testBasketTrends() throws Exception {
    model0.createEmptyBasket("threeStocks");

    model0.addStockInto("AAPL", 10, "threeStocks");
    model0.addStockInto("MSFT", 20, "threeStocks");
    model0.addStockInto("GOOG", 30, "threeStocks");

    // June 1, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 1);
    // June 13, 2017 Tuesday
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 13);
    // June 13, 2014
    Calendar date3 = new GregorianCalendar(2014, 6 - 1, 13);
    // October 17, 2014
    Calendar date4 = new GregorianCalendar(2014, 10 - 1, 17);

    // trends up true
    assertTrue(model0.basketTrends("threeStocks", date3, date2) > 0);
    assertTrue(model0.basketTrends("threeStocks", date4, date1) > 0);

    // trends up false
    assertFalse(model0.basketTrends("threeStocks", date3, date4) > 0);
    assertFalse(model0.basketTrends("threeStocks", date1, date2) > 0);

    // starts and ends on the same date
    assertEquals(0,
            model0.basketTrends("threeStocks", date3, date3), 0.0001);
  }

  /**
   * Tests whether basketTrends correctly throws IllegalArgumentException for empty baskets.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyBasketTrends() throws Exception {
    model0.createEmptyBasket("emptyBasket");

    // June 13, 2017 Tuesday
    Calendar date = new GregorianCalendar(2017, 6 - 1, 13);

    model0.basketTrends("emptyBasket", date, date);
  }

  /**
   * Test whether basketTrends() method throws IllegalArgumentException when given an
   * invalid basket name.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testInvalidBasketName() throws Exception {
    model0.createEmptyBasket("oneStock");
    model0.addStockInto("AAPL", 10, "threeStocks");

    // June 1, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 1);
    // June 13, 2017
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 13);

    model0.basketTrends("threeStocks", date1, date2); // no such basket
  }

  /**
   * Test if the basketTrends() method throws an IllegalArgumentException when the specified end
   * date is prior to the start date.
   * @throws Exception if the data source file is not read correctly
   */
  @Test (expected = IllegalArgumentException.class)
  public void testInvalidDateRange() throws Exception {
    model0.createEmptyBasket("threeStocks");
    model0.addStockInto("AAPL", 10, "threeStocks");
    model0.addStockInto("MSFT", 20, "threeStocks");
    model0.addStockInto("GOOG", 30, "threeStocks");

    // June 1, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 1);
    // June 13, 2017
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 13);

    model0.basketTrends("threeStocks", date2, date1);
  }

  /**
   * Test whether basketTrends() throws IllegalArgumentException when there is no baskets in the
   * model.
   * @throws Exception if the data source file is not read correctly
   */
  @Test(expected = IllegalArgumentException.class)
  public void testNoBasketBasketTrends() throws Exception {
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 13);
    model0.basketTrends("threeStocks", date1, date2);
  }

  /**
   * Tests whether method getBasketValue works properly.
   */
  @Test
  public void testGetBasketValue() throws Exception {
    model0.createEmptyBasket("a");
    Calendar date;
    Double price;

    // get the price of an empty basket on business day: June 12, 2017
    date = new GregorianCalendar(2017, 5, 12);
    price = model0.getBasketValue("a", date);
    assertEquals(0, price, 0.0001);

    model0.addStockInto("aapl", 100, "a");
    model0.addStockInto("goog", 200, "a");
    model0.addStockInto("amzn", 150, "a");

    // get the price on a business day: June 12, 2017
    date = new GregorianCalendar(2017, 5, 12);
    price = model0.getBasketValue("a", date);
    assertEquals(347858.5, price, 0.0001);

    // get the price on a weekend: June 10, 2017
    date = new GregorianCalendar(2017, 5, 10);
    price = model0.getBasketValue("a", date);
    assertEquals(null, price);

    // get the price on a holiday: July 4, 2016
    date = new GregorianCalendar(2016, 6, 4);
    price = model0.getBasketValue("a", date);
    assertEquals(null, price);

    // get the price of a future day: August 10, 2019
    date = new GregorianCalendar(2019, 7, 10);
    price = model0.getBasketValue("a", date);
    assertEquals(null, price);

    // get the price of a basket that does not exist in this model
    try {
      date = new GregorianCalendar(2017, 5, 12);
      model0.getBasketValue("b", date);
    } catch (IllegalArgumentException e) {
      //
    }
  }

  /**
   * Tests whether getBasketValues works properly with models with empty baskets.
   */
  @Test
  public void testGetBasketValues() throws Exception {
    model0.createEmptyBasket("a");
    Map<Integer, Double> expectedMap = new TreeMap<>();
    Map<Integer, Double> resultMap;

    /*
    Dates
     */
    // June 12, 2017: Monday
    Calendar date1 = new GregorianCalendar(2017, 5, 12);

    // June 16, 2017: Friday
    Calendar date2 = new GregorianCalendar(2017, 5, 16);

    // June 7, 2017: Wednesday
    Calendar date3 = new GregorianCalendar(2017, 5, 7);

    // June 3, 2017: Saturday
    Calendar date4 = new GregorianCalendar(2017, 5, 3);

    // June 4, 2017: Sunday
    Calendar date5 = new GregorianCalendar(2017, 5, 4);

    /*
    Tests
     */
    // test a single weekday. value on that day should be 0
    resultMap = model0.getBasketValues("a", date1, date1);
    assertEquals(true, resultMap.isEmpty());

    expectedMap.clear();

    // test a single weekend. no data on that day, therefore value should be empty
    resultMap = model0.getBasketValues("a", date4, date4);
    assertEquals(true, resultMap.isEmpty());

    expectedMap.clear();

    // test a date range of monday through friday
    resultMap = model0.getBasketValues("a", date1, date2);
    assertEquals(true, resultMap.isEmpty());

    expectedMap.clear();

    // test a date range of Wed through Mon
    resultMap = model0.getBasketValues("a", date3, date1);
    assertEquals(true, resultMap.isEmpty());

    expectedMap.clear();

    // test a date range of Sun through Wed
    resultMap = model0.getBasketValues("a", date5, date3);
    assertEquals(true, resultMap.isEmpty());

    expectedMap.clear();

    // test a date from of Sat through Sun
    resultMap = model0.getBasketValues("a", date4, date5);
    assertEquals(true, resultMap.isEmpty());
  }

  /**
   * Tests whether getBasketValues works properly with models with non-empty baskets.
   */
  @Test
  public void testNonEmptyBasketsGetBasketValues() throws Exception {
    /*
    Constructs 2 non-empty baskets.
     */
    model0.createEmptyBasket("a");
    model0.addStockInto("aapl", 100, "a");
    model0.addStockInto("goog", 200, "a");

    /*
    Constructs dates.
     */
    Calendar sat = new GregorianCalendar(2017, 5, 3);
    Calendar sun = new GregorianCalendar(2017, 5, 4);
    Calendar mon = new GregorianCalendar(2017, 5, 5);
    Calendar tue = new GregorianCalendar(2017, 5, 6);
    Calendar wed = new GregorianCalendar(2017, 5, 7);
    Calendar thu = new GregorianCalendar(2017, 5, 8);
    Calendar fri = new GregorianCalendar(2017, 5, 9);
    Calendar nextSat = new GregorianCalendar(2017, 5, 10);
    Calendar nextSun = new GregorianCalendar(2017, 5, 11);
    Calendar nextMon = new GregorianCalendar(2017, 5, 12);

    /*
    Tests on baskets.
     */
    Map<Integer, Double> resultMap;
    Map<Integer, Double> expectedMap = new TreeMap<>();

    // startDate = endDate, biz-day
    resultMap =  model0.getBasketValues("a", tue, tue);
    expectedMap.put(20170606, 210759.00);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // startDate = endDate, non-biz
    resultMap = model0.getBasketValues("a", sat, sat);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // test a date range:
    // startDate = biz day, endDate = biz day, no non-biz in between
    resultMap = model0.getBasketValues("a", tue, fri);
    expectedMap.put(20170606, 210759.0);
    expectedMap.put(20170607, 211753.0);
    expectedMap.put(20170608, 212181.0);
    expectedMap.put(20170609, 204864.0);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // test a date range:
    // startDate = biz day, endDate = biz day, with non-biz days in between
    resultMap = model0.getBasketValues("a", fri, nextMon);
    expectedMap.put(20170609, 204864.0);
    expectedMap.put(20170612, 203122.0);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // test a date range:
    // startDate = non-biz, endDate = biz
    resultMap = model0.getBasketValues("a", sat, mon);
    expectedMap.put(20170605, 212129.0);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // test a date range:
    // startDate = biz, endDate = non-biz
    resultMap = model0.getBasketValues("a", fri, nextSat);
    expectedMap.put(20170609, 204864.0);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();

    // test a date range:
    // weekends
    resultMap = model0.getBasketValues("a", sat, sun);
    assertEquals(expectedMap, resultMap);

    expectedMap.clear();
  }

  @Test
  public void test50DaysMovingAverageOfBasket() throws Exception {
    IModel testModel = new Model();
    testModel.createEmptyBasket("testBasket");

    // business day
    Calendar testDate1 = new GregorianCalendar(2017, 5 - 1, 1);
    // non business day
    Calendar testDate2 = new GregorianCalendar(2017, 5 - 1, 6);

    // test empty basket
    try {
      testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate1);
      fail();
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test basket with one stock
    testModel.addStockInto("AAPL", 100, "testBasket");
    assertEquals(14104.56,
            testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate1),
            0.001);
    assertEquals(14192.56,
            testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate2),
            0.001);

    // test basket with two stock
    testModel.addStockInto("GOOG", 200, "testBasket");
    assertEquals(181853.2,
            testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate1),
            0.001);
    assertEquals(183463.0,
            testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate2),
            0.001);
  }

  @Test
  public void test200DaysMovingAverageOfBasket() throws Exception {
    IModel testModel = new Model();
    testModel.createEmptyBasket("testBasket");

    // business day
    Calendar testDate1 = new GregorianCalendar(2017, 5 - 1, 1);
    // non business day
    Calendar testDate2 = new GregorianCalendar(2017, 5 - 1, 6);

    // test empty basket
    try {
      testModel.fiftyDaysMovingAverageOfBasket("testBasket", testDate1);
      fail();
    } catch (IllegalArgumentException e) {
      // do nothing
    }

    // test basket with one stock
    testModel.addStockInto("AAPL", 100, "testBasket");
    assertEquals(12039.04,
            testModel.twoHundredsDaysMovingAverageOfBasket("testBasket", testDate1),
            0.001);
    assertEquals(12134.85,
            testModel.twoHundredsDaysMovingAverageOfBasket("testBasket", testDate2),
            0.001);

    // test basket with two stock
    testModel.addStockInto("GOOG", 200, "testBasket");
    assertEquals(171172.4,
            testModel.twoHundredsDaysMovingAverageOfBasket("testBasket", testDate1),
            0.001);
    assertEquals(172038.7,
            testModel.twoHundredsDaysMovingAverageOfBasket("testBasket", testDate2),
            0.001);
  }

  @Test
  public void test50DaysMovingAveragesOfBasket() throws Exception {
    IModel testModel = new Model();
    testModel.createEmptyBasket("testBasket");
    Map<Integer, Double> resultMap;
    Map<Integer, Double> expectedMap = new TreeMap<>();

    Calendar testDate1 = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar testDate2 = new GregorianCalendar(2017, 6 - 1, 4);
    Calendar testDate3 = new GregorianCalendar(2017, 6 - 1, 10);

    // test empty basket
    resultMap = testModel.fiftyDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate2);
    assertTrue(resultMap.isEmpty());

    // test basket with one stock
    testModel.addStockInto("AAPL", 100, "testBasket");
    expectedMap.put(20170601, 14712.82);
    expectedMap.put(20170602, 14740.88);
    // followed by a weekend
    assertEquals(expectedMap, testModel.fiftyDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate2));
    expectedMap.put(20170605, 14766.9);
    expectedMap.put(20170606, 14794.52);
    expectedMap.put(20170607, 14823.5);
    expectedMap.put(20170608, 14845.88);
    expectedMap.put(20170609, 14855.6);
    // followed by a weekend
    assertEquals(expectedMap, testModel.fiftyDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate3));

    expectedMap.clear();

    // test basket with two stocks
    testModel.addStockInto("GOOG", 200, "testBasket");
    expectedMap.put(20170601, 191763.94);
    expectedMap.put(20170602, 192376.04);
    // followed by a weekend
    assertEquals(expectedMap, testModel.fiftyDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate2));
    expectedMap.put(20170605, 193066.46);
    expectedMap.put(20170606, 193742.64);
    expectedMap.put(20170607, 194417.9);
    expectedMap.put(20170608, 195090.24);
    expectedMap.put(20170609, 195573.64);
    // followed by a weekend
    assertEquals(expectedMap, testModel.fiftyDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate3));
  }

  @Test
  public void test200DaysMovingAveragesOfBasket() throws Exception {
    IModel testModel = new Model();
    testModel.createEmptyBasket("testBasket");
    Map<Integer, Double> resultMap;
    Map<Integer, Double> expectedMap = new TreeMap<>();

    Calendar testDate1 = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar testDate2 = new GregorianCalendar(2017, 6 - 1, 4);
    Calendar testDate3 = new GregorianCalendar(2017, 6 - 1, 10);

    // test empty basket
    resultMap = testModel.twoHundredsDaysMovingAveragesOfBasket("testBasket",
            testDate1, testDate2);
    assertTrue(resultMap.isEmpty());

    // test basket with one stock
    testModel.addStockInto("AAPL", 100, "testBasket");
    expectedMap.put(20170601, 12575.61);
    expectedMap.put(20170602, 12598.64);
    // followed by a weekend
    assertEquals(expectedMap, testModel.twoHundredsDaysMovingAveragesOfBasket(
            "testBasket", testDate1, testDate2));
    expectedMap.put(20170605, 12621.0);
    expectedMap.put(20170606, 12643.68);
    expectedMap.put(20170607, 12666.69);
    expectedMap.put(20170608, 12689.93);
    expectedMap.put(20170609, 12709.99);
    // followed by a weekend
    assertEquals(expectedMap, testModel.twoHundredsDaysMovingAveragesOfBasket(
            "testBasket", testDate1, testDate3));

    expectedMap.clear();

    // test basket with two stocks
    testModel.addStockInto("GOOG", 200, "testBasket");
    expectedMap.put(20170601, 175707.62);
    expectedMap.put(20170602, 175929.11);
    // followed by a weekend
    assertEquals(expectedMap, testModel.twoHundredsDaysMovingAveragesOfBasket(
            "testBasket", testDate1, testDate2));
    expectedMap.put(20170605, 176155.24);
    expectedMap.put(20170606, 176376.99);
    expectedMap.put(20170607, 176605.66);
    expectedMap.put(20170608, 176840.16);
    expectedMap.put(20170609, 177037.97);
    // followed by a weekend
    assertEquals(expectedMap, testModel.twoHundredsDaysMovingAveragesOfBasket(
            "testBasket", testDate1, testDate3));
  }

  // the following are tests about a single stock

  /**
   * Test if getStockClosingPrice works properly.
   */
  @Test
  public void testGetStockClosingPrice() throws Exception {
    assertEquals(145.42,
            model0.getStockClosingPrice("AAPL",
                    new GregorianCalendar(2017, 5, 12)),
            0.0001);

    assertEquals(902.36,
            model0.getStockClosingPrice("AMZN",
                    new GregorianCalendar(2017, 3, 11)),
            0.0001);
  }

  /**
   * Test if getStockClosingPrices throws exception when given a non-business day.
   */
  @Test (expected = Exception.class)
  public void testGetStockClosingPriceException() throws Exception {
    assertEquals(145.42,
            model0.getStockClosingPrice("AAPL",
                    new GregorianCalendar(2017, 5, 11)),
            0.0001);
  }

  /**
   * Test if getStockClosingPrices throws exception when given an invalid stock symbol.
   */
  @Test (expected = Exception.class)
  public void testGetStockClosingPriceException2() throws Exception {
    assertEquals(145.42,
            model0.getStockClosingPrice("AP12",
                    new GregorianCalendar(2017, 5, 12)),
            0.0001);
  }

  /**
   * Test if getStockClosingPrices() works properly.
   */
  @Test
  public void testGetStockClosingPrices() throws Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.getStockClosingPrices("AAPL", start1, end1).isEmpty());

    // 5/27/2017, Saturday
    Calendar start2 = new GregorianCalendar(2017, 5 - 1, 27);
    // 6/10/2017, Saturday
    Calendar end2 = new GregorianCalendar(2017, 6 - 1, 10);

    Map<Integer, Double> expected = new TreeMap<>();
    // 5/29/2017, Monday, Memorial Day
    expected.put(20170530, 153.67);
    expected.put(20170531, 152.76);
    expected.put(20170601, 153.18);
    expected.put(20170602, 155.45);
    // 6/3 and 6/4 2017, weekend
    expected.put(20170605, 153.93);
    expected.put(20170606, 154.45);
    expected.put(20170607, 155.37);
    expected.put(20170608, 154.99);
    expected.put(20170609, 148.98);

    assertEquals(expected, model0.getStockClosingPrices("AAPL", start2, end2));
  }

  /**
   * Test if getStockClosingPrices() throws exception when end date is prior to start day.
   */
  @Test (expected = Exception.class)
  public void testGetStockClosingPricesException() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.getStockClosingPrices("AAPL", end1, start1).isEmpty());
  }

  /**
   * Test if getStockClosingPrices() throws exception when given an invalid symbol.
   */
  @Test (expected = Exception.class)
  public void testGetStockClosingPricesException2() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.getStockClosingPrices("AA12", start1, end1).isEmpty());
  }

  /**
   * Test if fiftyDaysMovingAverageOfStock() works properly.
   */
  @Test
  public void testStock50MA() throws Exception {
    assertEquals(147.13, model0.fiftyDaysMovingAverageOfStock("AAPL",
            new GregorianCalendar(2017, 5, 1)), 0.01);
  }

  /**
   * Test if twoHundredsDaysMovingAverageOfStock() works properly.
   */
  @Test
  public void testStock200MA() throws Exception {
    assertEquals(125.76, model0.twoHundredsDaysMovingAverageOfStock("AAPL",
            new GregorianCalendar(2017, 5, 1)), 0.01);
  }

  /**
   * Test if fiftyDaysMovingAveragesOfStock() works properly.
   */
  @Test
  public void testStock50MARange() throws Exception {
    Calendar testDate1 = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar testDate2 = new GregorianCalendar(2017, 6 - 1, 4);
    Calendar testDate3 = new GregorianCalendar(2017, 6 - 1, 10);

    Map<Integer, Double> expectedMap = new TreeMap<>();
    expectedMap.put(20170601, 147.13);
    expectedMap.put(20170602, 147.41);
    assertEquals(expectedMap, model0.fiftyDaysMovingAveragesOfStock("AAPL",
            testDate1, testDate2));
    expectedMap.put(20170605, 147.67);
    expectedMap.put(20170606, 147.95);
    expectedMap.put(20170607, 148.24);
    expectedMap.put(20170608, 148.46);
    expectedMap.put(20170609, 148.56);

    assertEquals(expectedMap, model0.fiftyDaysMovingAveragesOfStock("AAPL",
            testDate1, testDate3));
  }

  // invalid date range
  @Test (expected = Exception.class)
  public void testStock50MARangeException() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.fiftyDaysMovingAveragesOfStock("AAPL", end1, start1).isEmpty());
  }

  // invalid stock symbol
  @Test (expected = Exception.class)
  public void testStock50MARangeException2() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.fiftyDaysMovingAveragesOfStock("AA12", start1, end1).isEmpty());
  }


  /**
   * Test if twoHundredsDaysMovingAveragesOfStock() works properly.
   */
  @Test
  public void testStoc2000MARange() throws Exception {
    Calendar testDate1 = new GregorianCalendar(2017, 6 - 1, 1);
    Calendar testDate2 = new GregorianCalendar(2017, 6 - 1, 4);
    Calendar testDate3 = new GregorianCalendar(2017, 6 - 1, 10);

    Map<Integer, Double> expectedMap = new TreeMap<>();
    expectedMap.put(20170601, 125.76);
    expectedMap.put(20170602, 125.99);
    assertEquals(expectedMap, model0.twoHundredsDaysMovingAveragesOfStock("AAPL",
            testDate1, testDate2));
    expectedMap.put(20170605, 126.21);
    expectedMap.put(20170606, 126.44);
    expectedMap.put(20170607, 126.67);
    expectedMap.put(20170608, 126.90);
    expectedMap.put(20170609, 127.10);

    assertEquals(expectedMap, model0.twoHundredsDaysMovingAveragesOfStock("AAPL",
            testDate1, testDate3));
  }

  // invalid date range
  @Test (expected = Exception.class)
  public void testStock200MARangeException() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.twoHundredsDaysMovingAveragesOfStock("AAPL", end1, start1).isEmpty());
  }

  // invalid stock symbol
  @Test (expected = Exception.class)
  public void testStock200MARangeException2() throws  Exception {
    // January 1st, 1980
    Calendar start1 = new GregorianCalendar(1980, 1 - 1, 1);
    // December 11th, 1980
    Calendar end1 = new GregorianCalendar(1980, 12 - 1, 11);

    assertTrue(model0.twoHundredsDaysMovingAveragesOfStock("AA12", start1, end1).isEmpty());
  }

  /**
   * Test if stockTrend() works properly.
   */
  @Test
  public void testStockTrend() throws Exception {
    // June 12, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 12);
    // June 8, 2017
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 8);
    // June 8, 2016
    Calendar date3 = new GregorianCalendar(2016, 6 - 1, 8);
    // June 8, 2015
    Calendar date4 = new GregorianCalendar(2015, 6 - 1, 8);

    // trends up true
    assertTrue(model0.stockTrend("AAPL", date3, date2) > 0);
    assertTrue(model0.stockTrend("AAPL", date4, date2) > 0);

    // trends up false
    assertFalse(model0.stockTrend("AAPL", date2, date1) > 0);
    assertFalse(model0.stockTrend("AAPL", date4, date3) > 0);

    // starts and ends on the same date
    assertFalse(model0.stockTrend("AAPL", date3, date3) > 0);
  }

  // invalid date range
  @Test (expected = Exception.class)
  public void testStockTrendException() throws Exception {
    // June 12, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 12);
    // June 8, 2017
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 8);
    assertFalse(model0.stockTrend("AAPL", date1, date2) > 0);
  }

  // invalid stock symbol
  @Test (expected = Exception.class)
  public void testStockTrendException2() throws Exception {
    // June 12, 2017
    Calendar date1 = new GregorianCalendar(2017, 6 - 1, 12);
    // June 8, 2017
    Calendar date2 = new GregorianCalendar(2017, 6 - 1, 8);
    assertFalse(model0.stockTrend("AA12", date2, date1) > 0);
  }

  /**
   * Test if removeBasket(String basketName) works properly.
   */
  @Test
  public void testRemoveBasket() throws Exception {
    IModel testModel = new Model();
    testModel.createEmptyBasket("basket1", new GregorianCalendar(2017,
            1 - 1, 3));
    testModel.createEmptyBasket("basket2", new GregorianCalendar(2017,
            1 - 1, 3));
    assertEquals("basket1: \nbasket2: \n", testModel.toString());

    // remove
    testModel.removeBasket("basket1");
    assertEquals("basket2: \n", testModel.toString());

  }
}