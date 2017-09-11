package view.trader;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import javafx.scene.shape.Line;

/**
 * This class represents a draw panel.
 */
class DrawPanel extends JPanel {
  private final Map<String, List<Line>> plotLine;
  private final Integer startDate;
  private final Integer endDate;
  private final double max;

  /**
   * Draw lines in the given list of lines and label the graph according to the start date, end
   * date and max value.
   * @param startDate the start date of the data
   * @param endDate the end date of the data
   * @param plotLine the list of lines to be drawn on the graph
   * @param max the max value of the data
   */
  public DrawPanel(Integer startDate, Integer endDate, Map<String, List<Line>> plotLine,
                   double max) {
    super();
    //set background to white
    this.setBackground(Color.WHITE);
    this.startDate = startDate;
    this.endDate = endDate;
    this.plotLine = plotLine;
    this.max = max;
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(800, 600);
  }

  @Override
  protected void paintComponent(Graphics g) {

    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;

    if (plotLine.isEmpty()) {
      return;
    }

    Color[] colors = getColors();
    int gapBetweenLabels = 15;  // vertical gap between labels
    int xOfLabels = 500;  // x-coordinate of labels
    int initialYOfLabels = 20;  // y-coordinate of the first label
    int xOfYLables = 50;  // x-coordinate of labels on y-axis
    int yOfXLables = 520;  // y-coordinate of labels on x-axis
    int xOfYAxis = 100;  // x-coordinate of y axis (prices)
    int yOfXAxis = 500;  // y-coordinate of x axis (dates)
    double plotHeight = 400;  // height of the area where plots can be drawn
    double plotWidth = 600;  // width of the area where plots can be drawn


    int labelY = initialYOfLabels;
    int colorIndex = 0;

    // draw x axis and y axis
    g2d.setColor(Color.BLACK);
    g2d.drawLine(xOfYAxis, 0, xOfYAxis, yOfXAxis);
    g2d.drawLine(xOfYAxis, yOfXAxis, 800, yOfXAxis);

    double yUnit = plotHeight / (double) max;
    for (Map.Entry<String, List<Line>> entry : plotLine.entrySet()) {

      g2d.setColor(colors[colorIndex]);
      g2d.drawString(entry.getKey(), xOfLabels, labelY);

      double xUnit = plotWidth / entry.getValue().size();
      for (int i = 0; i < entry.getValue().size(); i++) {
        Line one = entry.getValue().get(i);
        g2d.drawLine((int) (one.getStartX() * xUnit + xOfYAxis),
                (int) (yOfXAxis - (int) one.getStartY() * yUnit),
                (int) (one.getEndX() * xUnit + xOfYAxis),
                (int) (yOfXAxis - (int) one.getEndY() * yUnit));
      }

      labelY += gapBetweenLabels;
      if (colorIndex < colors.length - 1) {
        colorIndex++;
      }
    }

    // draw labels on x axis and y axis
    g2d.setColor(Color.black);
    g2d.drawString(startDate.toString(), xOfYAxis, yOfXLables);
    g2d.drawString(endDate.toString(), xOfYAxis + (int) plotWidth, yOfXLables);
    g2d.drawString(String.valueOf(max), xOfYLables, yOfXAxis - (int) plotHeight);
    g2d.drawString(String.valueOf(0), xOfYLables, yOfXAxis);
    g2d.drawString("Date", xOfYAxis + (int) plotWidth / 2,
            yOfXLables + gapBetweenLabels);
    g2d.drawString("USD", xOfYLables - gapBetweenLabels,
            yOfXAxis - (int) plotHeight / 2);
  }

  private Color[] getColors() {
    return new Color[]{Color.black, Color.blue, Color.red, Color.cyan,
                       Color.yellow, Color.darkGray, Color.green, Color.magenta,
                       Color.orange, Color.gray};
  }
}
