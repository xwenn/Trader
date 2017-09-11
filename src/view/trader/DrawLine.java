package view.trader;

import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import javafx.scene.shape.Line;

/**
 * This class shows an example of creating a GUI using Java Swing.
 */

public class DrawLine extends JFrame {

  /**
   * Draw lines in the given list of lines and label the graph according to the start date, end
   * date and max value.
   * @param startDate the start date of the data
   * @param endDate the end date of the data
   * @param plotLine the list of lines to be drawn on the graph
   * @param max the max value of the data
   */
  public DrawLine(Integer startDate, Integer endDate, Map<String, List<Line>> plotLine,
                  double max) {
    //call the constructor of JFrame, let it do what it does.
    super();
    //the X button should close this window, but not the entire program
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    //add the panel to this frame
    DrawPanel drawPanel = new DrawPanel(startDate, endDate, plotLine, max);
    this.add(drawPanel);
    //resize this frame so that it is just big enough to hold the panel
    //the panel sets its own size by overriding getPreferredSize
    this.pack();
    //make the window visible. By default a window is invisible.
    this.setVisible(true);
  }
}