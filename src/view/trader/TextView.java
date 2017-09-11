package view.trader;


import java.util.List;
import java.util.Map;

public class TextView implements IView {

  /**
   * Construct an empty text view.
   */
  public TextView() {
    // just empty
  }

  @Override
  public void show(String s) {
    System.out.println(s);
  }

  @Override
  public void viewGraph(List<Integer> dates, Map<String, List<Double>> data)
          throws IllegalArgumentException {
    // do nothing.
  }

}
