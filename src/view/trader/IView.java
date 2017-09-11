package view.trader;

import java.util.List;
import java.util.Map;

/**
 * This interface has all the operations a user interface should support. It can be implemented
 * as a text-based UI or a graphical UI.
 */
public interface IView {

  /**
   * Show string passed by the controller.
   *
   * @param s the string to be shown in the view
   */
  void show(String s);

  /**
   * Draw a plot graph according to the given data.
   *
   * @param dates date range of the data
   * @param data  data to be drawn on the graph
   * @throws IllegalArgumentException if the number of days is not compatible with the number of
   *                                  data
   */
  void viewGraph(List<Integer> dates, Map<String, List<Double>> data)
          throws IllegalArgumentException;

}
