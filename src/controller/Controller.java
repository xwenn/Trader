package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.trader.DollarAverageStrategy;
import model.trader.IModel;
import model.trader.Model;
import model.trader.Strategy;
import utils.CalendarUtil;
import view.trader.GraphView;
import view.trader.IView;
import view.trader.TextView;

/**
 * This class represents a controller.
 */
public class Controller implements IController {

  /**
   * The main method of Controller that runs the controller.
   * @param args comment-line arguments given by user
   * @throws Exception when source file is not read correctly
   */
  public static void main(String[] args) throws Exception {
    Appendable out = new StringBuffer();
    Readable in = new StringReader("");
    IController con = new Controller(System.in, in, out, new Model(), new TextView(),
            new GraphView());
    con.controllerGo();
  }

  private final InputStream streamIn;
  private final Readable in;
  private final Appendable out;
  // give me a model
  private final IModel model;
  // give me a view
  private final IView view;
  // give me a graphical view
  private final IView graphical;

  /**
   * Construct a controller.
   * @param in user inputs
   * @param out outputs
   * @param model the model used by the controller
   * @param view the text view used by the controller
   * @param graphical the graphical view used by the controller
   */
  public Controller(InputStream streamIn, Readable in, Appendable out, IModel model, IView view,
                    IView graphical) {
    // the InputSteam field is used for an interacting jar file. changed to a local field due to the
    // requirement of the java coding style grader.
    this.streamIn = streamIn;
    this.in = in;
    this.out = out;
    this.model = model;
    this.view = view;
    this.graphical = graphical;
  }

  @Override
  public void controllerGo() throws Exception {
    Scanner scan = new Scanner(in);
    String userOption;

    String menu = "N  - Create a new basket and give it a specific name\n" +
            "A  - Add shares of stocks to an existing basket using its ticker symbol\n" +
            "P  - Print the contents and values of an existing basket\n" +
            "TB - Trend of a particular basket within a specific data range\n" +
            "TS - Trend of a particular stock within a specific data range\n" +
            "G  - Open graphical view\n" +
            "I - try different investment strategies\n" +
            "Q  - Quit program\n" +
            "Select an option and hit enter\n";

    // append menu to output stream and print menu
    out.append(menu + "\n");
    view.show(menu);

    userOption = scan.nextLine();

    /*
    Continue prompting user to enter an option until the option is a valid one
     */
    while (!validOption(userOption)) {
      out.append("invalid option\n");
      view.show("invalid option");
      out.append(menu + "\n");
      view.show(menu);
      userOption = scan.nextLine();
    }

    while (!userOption.equalsIgnoreCase("Q")) {
      if (userOption.equalsIgnoreCase("N")) {
        executeN(scan);
      } else if (userOption.equalsIgnoreCase("A")) {
        executeA(scan);
      } else if (userOption.equalsIgnoreCase("P")) {
        executeP(scan);
      } else if (userOption.equalsIgnoreCase("TB")) {
        executeTB(scan);
      } else if (userOption.equalsIgnoreCase("TS")) {
        executeTS(scan);
      } else if (userOption.equalsIgnoreCase("G")) {
        executeG(scan);
      } else if (userOption.equalsIgnoreCase("I")) {
        executeI(scan);
      }

      view.show(menu);
      out.append(menu + "\n");
      userOption = scan.nextLine();
    }
    // exit the loop when user option is "q"
  }

  /**
   * Return true if the given string is a valid option (i.e. a, n, p, tb, ts, g or q), false
   * otherwise.
   *
   * @param opt the specified string
   * @return true if the string is a valid option, false otherwise
   */
  private boolean validOption(String opt) {
    return opt.equalsIgnoreCase("a")
            || opt.equalsIgnoreCase("n")
            || opt.equalsIgnoreCase("p")
            || opt.equalsIgnoreCase("tb")
            || opt.equalsIgnoreCase("ts")
            || opt.equalsIgnoreCase("g")
            || opt.equalsIgnoreCase("q")
            || opt.equalsIgnoreCase("i");
  }

  /**
   * Execute the N option: Create a new basket and give it a specific name.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeN(Scanner scan) throws IOException {
    List<String> args = new ArrayList<>();
    Calendar creationDate;

    this.view.show("enter basket name:");
    this.out.append("enter basket name:\n");
    args.add(scan.nextLine());

    this.view.show("enter basket creation time in a mm/dd/yyyy format:");
    this.out.append("enter basket creation time in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());

    try {
      creationDate = parseDate(args.get(1).split("/"));
    } catch (IllegalArgumentException e) {
      this.view.show("invalid input");
      this.out.append("invalid input\n");
      return;
    }

    try {
      this.model.createEmptyBasket(args.get(0), creationDate);
      this.view.show("success!");
      this.out.append("success!\n");
    } catch (Exception e) {
      view.show("failed!");
      this.out.append("failed!\n");
      // fail when: 1. the basket has already been created;
      // 2. the creation date is not a business day;
      // 3. cannot read data correctly
    }
  }

  /**
   * Execute the A option: Add shares of stocks to an existing basket using its ticker symbol.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeA(Scanner scan) throws IOException {
    List<String> args = new ArrayList<>();

    this.view.show("enter ticker symbol:");
    this.out.append("enter ticker symbol:\n");
    args.add(scan.nextLine());

    this.view.show("enter stock share:");
    this.out.append("enter stock share:\n");
    args.add(scan.nextLine());

    this.view.show("enter destination basket:");
    this.out.append("enter destination basket:\n");
    args.add(scan.nextLine());

    int share = 0;
    try {
      share = Integer.parseInt(args.get(1));
      this.model.addStockInto(args.get(0), share, args.get(2));
      this.view.show("success!");
      this.out.append("success!\n");
    } catch (Exception e) {
      this.view.show("failed!");
      this.out.append("failed!\n");
    }
  }

  /**
   * Execute the P option: Print the contents and values of an existing basket.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeP(Scanner scan) throws IOException {
    List<String> args = new ArrayList<>();

    this.view.show("enter basket name:");
    this.out.append("enter basket name:\n");
    args.add(scan.nextLine());
    try {
      String basketContent = this.model.basketToString(args.get(0));
      this.view.show(basketContent);
      this.out.append(basketContent + "\n");
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
    }
  }

  /**
   * Execute the TB option: Trend of a particular basket within a specific data range.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeTB(Scanner scan) throws IOException {
    List<String> args = new ArrayList<>();
    Calendar startDate;
    Calendar endDate;

    // prompt user to enter basket name, start date, end date
    view.show("enter basket name:");
    this.out.append("enter basket name:\n");
    args.add(scan.nextLine());
    String basketName = args.get(0);
    if (!model.containsBasket(basketName)) {
      view.show("failed!");
      this.out.append("failed!\n");
      return;
    }

    view.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(1).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    view.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(2).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    // when the user input basket name and dates are valid,
    // startDate and endDate will be successfully initialized
    // try to computes the trends
    try {
      double trend = model.basketTrends(basketName, startDate, endDate);
      view.show(String.format("trend: %.4f", trend));
      this.out.append(String.format("trend: %.4f\n", trend));
    } catch (Exception e) {
      view.show("failed!");
      this.out.append("failed!\n");
    }
  }

  /**
   * Execute the TS option: Trend of a particular stock within a specific data range.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeTS(Scanner scan) throws IOException {
    List<String> args = new ArrayList<>();
    Calendar startDate;
    Calendar endDate;

    // prompt user to enter basket name, start date, end date
    view.show("enter stock ticker symbol:");
    this.out.append("enter stock ticker symbol:\n");
    args.add(scan.nextLine());
    String stockSymbol = args.get(0);

    view.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(1).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    view.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(2).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    // When the user input dates are valid,
    // StartDate and endDate will be successfully initialized
    // Try to computes the trends
    try {
      double trend = model.stockTrend(stockSymbol, startDate, endDate);
      view.show(String.format("trend: %.4f", trend));
      this.out.append(String.format("trend: %.4f\n", trend));
    } catch (Exception e) {
      view.show("failed!");
      this.out.append("failed!\n");
    }
  }

  /**
   * Execute the G option: Open graphical view.
   *
   * @param scan scan of user input
   * @throws IOException when the source file is not read correctly
   */
  private void executeG(Scanner scan) throws IOException {
    String menu = "Graphical View Menu\n";
    menu += "1 - add/remove a stock to visualize\n";
    menu += "2 - add/remove one of your baskets to visualize\n";
    menu += "3 - plot historical prices alone for the chosen stocks and baskets\n";
    menu += "4 - plot 50-day Moving Average alone for the chosen stocks and baskets\n";
    menu += "5 - plot 200-day moving average alone for the chosen stocks and baskets\n";
    menu += "6 - plot historical prices AND 50-day moving average\n";
    menu += "7 - plot historical prices AND 200-day moving average\n";
    menu += "8 - plot 50-day moving average AND 200-day moving average\n";
    menu += "9 - draw blank graph\n";
    menu += "0 - Quit the program.\n";
    menu += "Select an option and hit enter.\n";

    graphical.show(menu);

    String userOption = scan.nextLine();
    while (!isValidGraphOption(userOption)) {
      graphical.show(menu);
      userOption = scan.nextLine();
    }

    Set<String> stockSymbols = new TreeSet<>(); // the symbols of stocks to display on the graph
    Set<String> basketNames = new TreeSet<>(); // the names of baskets to display on the graph

    while (!userOption.equalsIgnoreCase("0")) {
      int userOpt = Integer.parseInt(userOption);
      switch (userOpt) {
        case 1:
          execute1(scan, stockSymbols);
          break;
        case 2:
          execute2(scan, basketNames);
          break;
        case 3:
          execute3(scan, stockSymbols, basketNames);
          break;
        case 4:
          execute4(scan, stockSymbols, basketNames);
          break;
        case 5:
          execute5(scan, stockSymbols, basketNames);
          break;
        case 6:
          execute6(scan, stockSymbols, basketNames);
          break;
        case 7:
          execute7(scan, stockSymbols, basketNames);
          break;
        case 8:
          execute8(scan, stockSymbols, basketNames);
          break;
        case 9:
          execute9(scan, stockSymbols, basketNames);
          break;
        default:
          graphical.show(menu);
      }
      graphical.show(menu);
      userOption = scan.nextLine();
    }
    //when user Option = 0, exit the loop and the program.
  }

  private void executeI(Scanner scan) throws Exception {
    // step 1: prompt for the name of new basket and creation time
    view.show("enter new basket name:");
    this.out.append("enter new basket name:\n");
    String basketName = scan.nextLine();
    if (this.model.containsBasket("basketName")) {
      view.show("this basket has already been created");
      this.out.append("this basket has already been created\n");
      return; // exit this method
    }

    view.show("enter creation date in a mm/dd/yyyy format:");
    this.out.append("enter creation date in a mm/dd/yyyy format:\n");
    String creationTimeString = scan.nextLine();
    String[] times = creationTimeString.split("/");
    Calendar creationDate;
    try {
      creationDate = parseDate(times);
    } catch (NumberFormatException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    // create new basket
    try {
      model.createEmptyBasket(basketName, creationDate);
    } catch (Exception e) {
      view.show("failed!");
      this.out.append("failed!\n"); // fail when: 1. basket already exists; 2. non-biz day
      return; // exit this method
    }

    /*
    step 2: keeps prompting user to add stocks into the new basket
    should at least successfully add at least 1 stock into the new basket
    because strategy.invest() does not accept empty baskets

    user should be able to stop adding new stocks when pressing 'q'
    */

    String symbol;
    String share;
    do {
      view.show("enter ticker symbol:");
      out.append("enter ticker symbol:\n");
      symbol = scan.nextLine();
      view.show("enter share:");
      out.append("enter share:\n");
      share = scan.nextLine();
      int shareInt = 0;
      try {
        shareInt = Integer.parseInt(share);
      } catch (NumberFormatException e) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        model.removeBasket(basketName);
        return; //exit
      }
      try {
        model.addStockInto(symbol, shareInt, basketName);
      } catch (IllegalArgumentException e) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        // remove the basket added in the previous step and exit
        model.removeBasket(basketName);
        return; // exit
      }
    }
    while (model.getBasketValue(basketName, creationDate) <= 0);

    while (true) {
      view.show("enter ticker symbol ('q' to stop adding):");
      out.append("enter ticker symbol ('q' to stop adding):\n");
      symbol = scan.nextLine();
      if (symbol.equalsIgnoreCase("q")) {
        break;
      }
      view.show("enter share:");
      out.append("enter share:\n");
      share = scan.nextLine();
      int shareInt = 0;
      try {
        shareInt = Integer.parseInt(share);
      } catch (NumberFormatException e) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        model.removeBasket(basketName);
        return; //exit
      }
      try {
        model.addStockInto(symbol, shareInt, basketName);
      } catch (IllegalArgumentException e) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        model.removeBasket(basketName);
        return; // exit
      }
    }

    // step 3: prompt user to set investment strategy
    view.show("choose an investment strategy: A - dollar-cost averaging");
    this.out.append("choose an investment strategy: A - dollar-cost averaging\n");
    String strategyOption = scan.nextLine();

    if (!strategyOption.equalsIgnoreCase("A")) {
      view.show("invalid strategy");
      this.out.append("invalid strategy\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    Strategy chosen;
    chosen = new DollarAverageStrategy();

    // step 4: prompt user to enter start date, end date, total money, and period
    Calendar startDate;
    Calendar endDate;
    List<String> args = new ArrayList<>();

    // prompt to enter money
    view.show("enter amount of money to invest (>= 0):");
    this.out.append("enter total amount of money to invest (>= 0):\n");
    args.add(scan.nextLine());
    double money;
    try {
      money = Double.parseDouble(args.get(0));
    } catch (NumberFormatException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }
    if (money < 0) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    // prompt to enter date range
    view.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(1).split("/");
    try {
      startDate = parseDate(start);
      if (!CalendarUtil.isBusinessDay(startDate)) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        model.removeBasket(basketName);
        return; // exit this method
      }
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    view.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(2).split("/");
    try {
      endDate = parseDate(end);
      if (CalendarUtil.isFutureDay(endDate)) {
        view.show("invalid input");
        this.out.append("invalid input\n");
        model.removeBasket(basketName);
        return; // exit this method
      }
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    // startDate must be prior or equal to endDate
    if (startDate.compareTo(endDate) > 0) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    // prompt to enter period
    view.show("enter period (number of days between investments), must >= 7:");
    this.out.append("enter period (number of days between investments), must >= 7:\n");
    args.add(scan.nextLine());
    int period;
    try {
      period = Integer.parseInt(args.get(3));
    } catch (NumberFormatException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }
    if (period < 7) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    // prompt to enter a date the profit on which the user wants to compute
    view.show("enter the date on which the profit will be calculated, in a mm/dd/yyyy format:");
    out.append("enter the date on which the profit will be calculated, in a mm/dd/yyyy format:\n");
    String[] dateStrings = scan.nextLine().split("/");
    Calendar date;
    try {
      date = parseDate(dateStrings);
    } catch (IllegalArgumentException e) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    if (date.before(endDate)) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    if (!CalendarUtil.isBusinessDay(date)) {
      view.show("invalid input");
      this.out.append("invalid input\n");
      model.removeBasket(basketName);
      return; // exit this method
    }

    try {
      double profit = model.profit(basketName, chosen, startDate, endDate, money, period, date);
      view.show(String.format("estimated profits: %.4f", profit));
      this.out.append(String.format("estimated profits: %.4f\n", profit));
    } catch (Exception e) {
      view.show("failed!");
      this.out.append("failed!\n");
    }
    model.removeBasket(basketName);
    // exit this method
  }

  /**
   * Prompts the user to enter a stock and his/her decision on whether to remove it or add it.
   *
   * @param scan         scanner
   * @param stockSymbols ticker symbol
   */
  private void execute1(Scanner scan, Set<String> stockSymbols) {
    graphical.show("enter 'r' to remove, enter 'a' to add:");
    String opt = scan.nextLine();
    if (!opt.equalsIgnoreCase("r") && !opt.equalsIgnoreCase("a")) {
      graphical.show("invalid input");
      return;
    }

    graphical.show("enter stock ticker symbol:");
    String symbol = scan.nextLine();

    // checks if this is a valid symbol
    boolean flag = false;
    try {
      flag = model.isValidStockSymbol(symbol);
    } catch (Exception e) {
      graphical.show("cannot read data correctly");
      return;
    }

    if (!flag) {
      graphical.show("invalid ticker symbol");
      return;
    }

    if (opt.equalsIgnoreCase("r")) {
      stockSymbols.remove(symbol);
      graphical.show(symbol + " is removed");
    } else {
      stockSymbols.add(symbol);
      graphical.show(symbol + " is added");

    }
  }

  /**
   * Prompt the user to add or remove basket names from the specified set.
   *
   * @param scan        scanner
   * @param basketNames a set of basket names the user want to plot
   */
  private void execute2(Scanner scan, Set<String> basketNames) {
    graphical.show("enter 'r' to remove, enter 'a' to add:");
    String opt = scan.nextLine();
    if (!opt.equalsIgnoreCase("r") && !opt.equalsIgnoreCase("a")) {
      graphical.show("invalid input");
      return;
    }

    graphical.show("enter the basket name:");
    String basketName = scan.nextLine();

    if (!model.containsBasket(basketName)) {
      graphical.show("invalid basket name");
    } else {
      if (opt.equalsIgnoreCase("r")) {
        basketNames.remove(basketName);
        graphical.show(basketName + " is removed");
      } else {
        basketNames.add(basketName);
        graphical.show(basketName + " is added");

      }
    }
  }

  /**
   * Plots the historical prices of the specified stocks and baskets.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute3(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> prices = model.getStockClosingPrices(stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " prices", prices);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> prices = model.getBasketValues(basketName, startDate, endDate);
        toDraw.put(basketName + " prices", prices);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Plots 50-day Moving Average alone for the chosen stocks and baskets.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute4(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // draw the picture
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfStock(stockSymbol,
                startDate, endDate);
        toDraw.put(stockSymbol + " 50-day moving averages", movingAverage50);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfBasket(basketName,
                startDate, endDate);
        toDraw.put(basketName + " 50-day moving averages", movingAverage50);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Plots 200-day moving average alone for the chosen stocks and baskets.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute5(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // draw the picture
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfStock(
                stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfBasket(
                basketName, startDate, endDate);
        toDraw.put(basketName + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Plots historical prices AND 50-day moving average.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute6(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // draw the picture
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> prices = model.getStockClosingPrices(stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " prices", prices);
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfStock(stockSymbol,
                startDate, endDate);
        toDraw.put(stockSymbol + " 50-day moving averages", movingAverage50);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> prices = model.getBasketValues(basketName, startDate, endDate);
        toDraw.put(basketName + " prices", prices);
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfBasket(basketName,
                startDate, endDate);
        toDraw.put(basketName + " 50-day moving averages", movingAverage50);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Plots historical prices AND 200-day moving average.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute7(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // draw the picture
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> prices = model.getStockClosingPrices(stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " prices", prices);
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfStock(
                stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> prices = model.getBasketValues(basketName, startDate, endDate);
        toDraw.put(basketName + " prices", prices);
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfBasket(
                basketName, startDate, endDate);
        toDraw.put(basketName + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Plots 50-day moving average AND 200-day moving average.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute8(Scanner scan, Set<String> stockSymbols, Set<String> basketNames)
          throws IOException {
    // draw the picture
    // prompt user to enter start date and end date
    Calendar startDate;
    Calendar endDate;

    List<String> args = new ArrayList<>();
    graphical.show("enter start date, in a mm/dd/yyyy format:");
    this.out.append("enter start date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] start = args.get(0).split("/");
    try {
      startDate = parseDate(start);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    graphical.show("enter end date, in a mm/dd/yyyy format:");
    this.out.append("enter end date, in a mm/dd/yyyy format:\n");
    args.add(scan.nextLine());
    String[] end = args.get(1).split("/");
    try {
      endDate = parseDate(end);
    } catch (IllegalArgumentException e) {
      graphical.show("invalid input");
      this.out.append("invalid input\n");
      return; // exit this method
    }

    /*
    Draw the picture
     */
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    Iterator<String> it1 = stockSymbols.iterator();
    while (it1.hasNext()) {
      String stockSymbol = it1.next();
      try {
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfStock(stockSymbol,
                startDate, endDate);
        toDraw.put(stockSymbol + " 50-day moving averages", movingAverage50);
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfStock(
                stockSymbol, startDate, endDate);
        toDraw.put(stockSymbol + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    Iterator<String> it2 = basketNames.iterator();
    while (it2.hasNext()) {
      String basketName = it2.next();
      try {
        Map<Integer, Double> movingAverage50 = model.fiftyDaysMovingAveragesOfBasket(basketName,
                startDate, endDate);
        toDraw.put(basketName + " 50-day moving averages", movingAverage50);
        Map<Integer, Double> movingAverage200 = model.twoHundredsDaysMovingAveragesOfBasket(
                basketName, startDate, endDate);
        toDraw.put(basketName + " 200-day moving averages", movingAverage200);
      } catch (Exception e) {
        graphical.show("invalid input");
        this.out.append("invalid input\n");
        return; // exit this method
      }
    }
    sendDataToDraw(toDraw);
  }

  /**
   * Draw a blank graph and clear data that need to be drawn.
   *
   * @param scan         scan of user input
   * @param stockSymbols stock symbols
   * @param basketNames  basket names
   */
  private void execute9(Scanner scan, Set<String> stockSymbols, Set<String> basketNames) {
    // draw the picture
    stockSymbols.clear();
    basketNames.clear();
    Map<String, Map<Integer, Double>> toDraw = new TreeMap<>();
    sendDataToDraw(toDraw);
  }

  /**
   * Convert the data so that they can be used by graph view.
   *
   * @param toDraw data to be drawn by the graph view
   */
  private void sendDataToDraw(Map<String, Map<Integer, Double>> toDraw) {
    if (toDraw == null) {
      graphical.show("invalid input");
    }
    // convert data to the data needed by view:
    // extract dates from map for drawing the x-axis label of the graph
    List<Integer> dates = new ArrayList<>();
    // extract labels for each plot
    // extract values from map for drawing the actual line
    Map<String, List<Double>> data = new TreeMap<>();

    for (Map.Entry<String, Map<Integer, Double>> entry : toDraw.entrySet()) {
      dates = new ArrayList<>();
      List<Double> values = new ArrayList<>();
      for (Map.Entry<Integer, Double> stockEntry : entry.getValue().entrySet()) {
        dates.add(stockEntry.getKey());
        values.add(stockEntry.getValue());
      }
      data.put(entry.getKey(), values);
    }

    // call method in graphical view to draw the graph
    graphical.viewGraph(dates, data);
  }

  /**
   * Turns string array representing a date into a Calendar object.
   *
   * @return string array representing a date into a Calendar object
   */
  private static Calendar parseDate(String[] dateString) throws IllegalArgumentException {
    Calendar date;
    int year;
    int month;
    int day;

    try {
      month = Integer.parseInt(dateString[0]);
      day = Integer.parseInt(dateString[1]);
      year = Integer.parseInt(dateString[2]);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException();
    }

    if (CalendarUtil.isValidDay(year, month, day)) {
      date = new GregorianCalendar(year, month - 1, day);
      return date;
    } else {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Checks if the given string is one of 0, 1, 2, ... 9.
   *
   * @param opt a string
   * @return true if the given string is one of 0, 1, 2, ...9, and false otherwise
   */
  private static boolean isValidGraphOption(String opt) {
    if (opt.length() > 1) {
      return false;
    }

    try {
      Integer.parseInt(opt);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
