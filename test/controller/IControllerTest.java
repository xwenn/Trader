package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import model.trader.IModel;
import model.trader.Model;
import view.trader.GraphView;
import view.trader.IView;
import view.trader.TextView;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test class for IController.
 */
public class IControllerTest {

  private IController controller;
  private IModel model;
  private IView textView;
  private IView graphical;
  private Appendable out;
  private Readable in;

  private String menu;
  private String succ = "success!\n";
  private String fail = "failed!\n";
  private String invalid = "invalid input\n";

  /**
   * Set up some objects for testing.
   */
  @Before
  public void setUp() {
    model = new Model();
    textView = new TextView();
    graphical = new GraphView();
    menu = "N  - Create a new basket and give it a specific name\n" +
            "A  - Add shares of stocks to an existing basket using its ticker symbol\n" +
            "P  - Print the contents and values of an existing basket\n" +
            "TB - Trend of a particular basket within a specific data range\n" +
            "TS - Trend of a particular stock within a specific data range\n" +
            "G  - Open graphical view\n" +
            "I - try different investment strategies\n" +
            "Q  - Quit program\n" +
            "Select an option and hit enter\n" + "\n";

    out = new StringBuffer();
  }

  /**
   * Tests option Q.
   */
  @Test
  public void testQ() throws Exception {
    in = new StringReader("Q\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    assertEquals(menu, out.toString());
  }

  @Test
  public void testq() throws Exception {
    in = new StringReader("q\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    assertEquals(menu, out.toString());
  }

  /**
   * Test option N.
   */
  @Test
  public void testN() throws Exception {
    in = new StringReader("N\nfirstBasket\n06/30/2015\nQ\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu + "enter basket name:\n"
            + "enter basket creation time in a mm/dd/yyyy format:\n"
            + succ + menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option N.
   */
  @Test
  public void testN1() throws Exception {
    in = new StringReader("N\nfirstBasket\n02/29/2015\nQ\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu + "enter basket name:\n"
            + "enter basket creation time in a mm/dd/yyyy format:\n"
            + invalid + menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option N.
   */
  @Test
  public void testN2() throws Exception {
    in = new StringReader("N\nfirstBasket\n06/03/2013\nN\nsecondBasket\n02/24/2017\nQ\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }
    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option N. create same baskets.
   */
  @Test
  public void testN3() throws Exception {
    in = new StringReader("N\nfirstBasket\n03/02/2017\nN\nfirstBasket\n05/02/2018\nQ\n");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }
    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += fail;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option A.
   */
  @Test
  public void testA() throws Exception {
    in = new StringReader("N\nfirstBasket\n05/02/2013\nA\nGOOG\n200\nfirstBasket\nQ");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option A.
   */
  @Test
  public void testA2() throws Exception {
    in = new StringReader("N\nfirstBasket\n03/02/2011\n" +
            "A\nGOOG\n200\nfirstBasket\nA\nAAPL\n100\nfirstBasket\nQ");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option A.
   */
  @Test
  public void testA3() throws Exception {
    in = new StringReader("N\nfirstBasket\n03/02/2011\n" +
            "A\nGOOG\n200\nfirstBasket\nA\nGOOG\n100\nfirstBasket\nQ");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option A.
   */
  @Test
  public void testA4() throws Exception {
    in = new StringReader("N\nfirstBasket\n03/02/2011\nA\nGOOG\n200\nsecondBasket\nQ");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += fail;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option A.
   */
  @Test
  public void testA5() throws Exception {
    in = new StringReader("N\nfirstBasket\n03/02/2011\nA\nAAPL\n0\nfirstBasket\nQ");

    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;
    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += fail;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option P.
   */
  @Test
  public void testP() throws Exception {
    in = new StringReader("n\nhello\n03/02/2011\nn\nworld\n03/02/2012\na\ngoog\n200\n"
            + "hello\na\naapl\n100\nhello\na\ngoog\n150\nworld\np\nhello\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += "hello: Apple * 100, Alphabet * 200\n";
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option P.
   */
  @Test
  public void testP2() throws Exception {
    in = new StringReader("n\nhello\n03/02/2011\nn\nworld\n03/01/2010\na\ngoog\n200\n"
            + "hello\na\naapl\n100\nhello\na\ngoog\n150\nworld\np\nHello\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);

    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += "invalid input\n";
    expected += menu;

    assertEquals(expected, out.toString());
  }


  /**
   * Test option TB.
   */
  @Test
  public void testTB() throws Exception {
    in = new StringReader("N\nthreeStocks\n03/02/2011\nA\nAAPL\n10\nthreeStocks\nA\nMSFT\n"
            + "20\nthreeStocks\nA\nGOOG\n30\nthreeStocks\nTB\nthreeStocks\n05/01/2017\n"
            + "06/01/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "trend: " + String.format("%.4f\n", 77.7818);
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test Option TB.
   */
  @Test
  public void testTB2() throws Exception {
    in = new StringReader("N\nthreeStocks\n03/02/2011\nA\nAAPL\n10\nthreeStocks\nA\nMSFT\n"
            + "20\nthreeStocks\nA\nGOOG\n30\nthreeStocks\nTB\ntwoStocks\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += "enter basket creation time in a mm/dd/yyyy format:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter ticker symbol:\n";
    expected += "enter stock share:\n";
    expected += "enter destination basket:\n";
    expected += succ;
    expected += menu;

    expected += "enter basket name:\n";
    expected += fail;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test Option TB.
   */
  @Test
  public void testTB3() throws Exception {
    in = new StringReader("TB\nthreeStocks\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter basket name:\n";
    expected += fail;
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option TS.
   */
  @Test
  public void testTS() throws Exception {
    in = new StringReader("TS\nAAPL\n03/02/2016\n04/02/2016\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter stock ticker symbol:\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "trend: " + String.format("%.4f\n", 0.4400);
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option TS.
   */
  @Test
  public void testTS2() throws Exception {
    in = new StringReader("TS\nAAPL\n03/02/2016\n04/31/2016\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter stock ticker symbol:\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += invalid;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option TS.
   */
  @Test
  public void testTS3() throws Exception {
    in = new StringReader("TS\nAAPL\n03/02/2016\n01/31/2016\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter stock ticker symbol:\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += fail;
    expected += menu;

    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: when all arguments are valid.
   */
  @Test
  public void testI() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n" +
            "02/01/2017\n05/01/2017\n20\n06/01/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "enter period (number of days between investments), must >= 7:\n";
    expected += "enter the date on which the profit will be calculated, in a mm/dd/yyyy format:\n";
    expected += "estimated profits: 5446.5400\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: date to be calculate is not valid - before end date.
   */
  @Test
  public void testI1() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n" +
            "02/01/2017\n05/01/2017\n20\n04/28/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "enter period (number of days between investments), must >= 7:\n";
    expected += "enter the date on which the profit will be calculated, in a mm/dd/yyyy format:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: date to be calculate is not valid - non business day.
   */
  @Test
  public void testI2() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n" +
            "02/01/2017\n05/01/2017\n20\n06/25/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "enter period (number of days between investments), must >= 7:\n";
    expected += "enter the date on which the profit will be calculated, in a mm/dd/yyyy format:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: period is not valid - < 7 days.
   */
  @Test
  public void testI3() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n" +
            "02/01/2017\n05/01/2017\n5\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "enter period (number of days between investments), must >= 7:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: endDate is not valid - before startDate.
   */
  @Test
  public void testI4() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n" +
            "02/01/2017\n01/01/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "enter end date, in a mm/dd/yyyy format:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: startDate is not valid - future date.
   */
  @Test
  public void testI5() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n02/01/2222\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: startDate is not valid - non business day.
   */
  @Test
  public void testI6() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n10000\n01/01/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "enter start date, in a mm/dd/yyyy format:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: invest amount of money is invalid - less than 0.
   */
  @Test
  public void testI7() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nA\n-100\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "enter total amount of money to invest (>= 0):\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: strategy option is invalid.
   */
  @Test
  public void testI8() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nq\nB\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "choose an investment strategy: A - dollar-cost averaging\n";
    expected += "invalid strategy\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: stock symbol is invalid.
   */
  @Test
  public void testI9() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAAPL\n10\nAA12\n10\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "enter ticker symbol ('q' to stop adding):\n";
    expected += "enter share:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: stock symbol is invalid.
   */
  @Test
  public void testI10() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2017\nAA12\n10\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "enter ticker symbol:\n";
    expected += "enter share:\n";
    expected += "invalid input\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: creation date is invalid - non business day.
   */
  @Test
  public void testI11() throws Exception {
    in = new StringReader("I\nbasket1\n01/01/2017\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "failed!\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }

  /**
   * Test option I: creation date is invalid - future date.
   */
  @Test
  public void testI12() throws Exception {
    in = new StringReader("I\nbasket1\n02/01/2222\nQ\n");
    controller = new Controller(System.in, in, out, model, textView, graphical);
    try {
      controller.controllerGo();
    } catch (IOException e) {
      //
    }

    String expected = menu;
    expected += "enter new basket name:\n";
    expected += "enter creation date in a mm/dd/yyyy format:\n";
    expected += "failed!\n";
    expected += menu;
    assertEquals(expected, out.toString());
  }


  /**
   * Test option G.
   */
  public static void main(String[] args) throws Exception {
    // Test each option by remove the comment mark of each line of code

    // test option 1 + 2 + 3: prices
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n3\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 4: 50-day moving averages
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n4\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 5: 200-day moving averages
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n5\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 6: prices AND 50-day moving averages
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n6\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 7: prices AND 200-day moving averages
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n7\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 8: 50-day moving averages AND 200-day moving averages
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n8\n03/01/2017\n06/01/2017\n0\nQ\n");

    // test option 1 + 2 + 9: draw blank graph
    // Readable in = new StringReader("N\nbskt\n01/01/2017\nA\nAAPL\n50\nbskt\nG\n1\na\nGOOG\n
    // 2\na\nbskt\n9\n0\nQ\n");

    // show the graph required in this assignment
    Readable in = new StringReader("G\n1\na\nWFC\n6\n01/01/2015\n01/01/2017\n0\nQ\n");

    Appendable out = new StringBuffer();
    IModel model = new Model();
    IView text = new TextView();
    IView graph = new GraphView();
    IController con = new Controller(System.in, in, out, model, text, graph);
    con.controllerGo();
  }

}