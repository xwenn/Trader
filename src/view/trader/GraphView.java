package view.trader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import javafx.scene.shape.Line;


/**
 * This class represents a graphical view.
 */
public class GraphView implements IView {

  /**
   * Construct a graph view.
   */
  public GraphView() {
    // just empty
  }

  @Override
  public void show(String s) {
    System.out.println(s);
  }


  @Override
  public void viewGraph(List<Integer> dates, Map<String, List<Double>> data)
          throws IllegalArgumentException {

    double maxValue = 0;
    Map<String, List<Line>> lines = new TreeMap<>();

    for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
      lines.put(entry.getKey(), listToLine(entry.getValue()));
      if (maxValue < Collections.max(entry.getValue())) {
        maxValue = Collections.max(entry.getValue());
      }
    }

    if (lines.isEmpty()) {
      DrawLine mainWindow = new DrawLine(0, 0, lines, maxValue);
    } else {
      Integer startDate = dates.get(0);
      Integer endDate = dates.get(dates.size() - 1);
      DrawLine mainWindow = new DrawLine(startDate, endDate, lines, maxValue);
    }
  }

  /**
   * Return a list of Line by converting the data in a list of doubles.
   * @param data the data to be converted by lines
   * @return a list of Line by converting the data in a list of doubles
   */
  private static List<Line> listToLine(List<Double> data) {
    List<Line> plotLine = new ArrayList<>();
    for (int i = 0; i < data.size() - 1; i++) {
      plotLine.add(new Line(i, data.get(i), i + 1, data.get(i + 1)));
    }
    return plotLine;
  }
}
