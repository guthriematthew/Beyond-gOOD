package edu.cs3500.spreadsheets.model;

import org.jfree.data.general.DefaultPieDataset;

import java.util.HashSet;


/**
 * A class that maps the data of a chart from the data in a spreadsheet.
 */
public class Chart {
  private Coord startingCoord1;
  private Coord startingCoord2;
  private int length;
  private int position;
  private String title;
  private HashSet<Coord> dep;
  private DefaultPieDataset pieChartData;


  /**
   * Creates a Chart with all essential information for chart construction.
   *
   * @param startingCoord1 the first column of data start position.
   * @param startingCoord2 the second column start position.
   * @param length         the length of both columns.
   * @param pieChartData   the data used to build each graph.
   * @param dep            the cells a given graph depends on.
   * @param title          the title of a graph.
   * @param position       the position of a graph within a collection.
   */
  public Chart(Coord startingCoord1, Coord startingCoord2, int length,
               DefaultPieDataset pieChartData, HashSet<Coord> dep, String title,
               int position) {
    if (startingCoord1 == null || startingCoord2 == null || pieChartData == null
            || dep == null) {
      throw new IllegalArgumentException("Chart parameters cannot be null");
    }
    this.startingCoord1 = startingCoord1;
    this.startingCoord2 = startingCoord2;
    this.length = length;
    this.pieChartData = pieChartData;
    this.dep = dep;
    this.title = title;
    this.position = position;
  }

  /**
   * Updates the graph with the values from the current version of the worksheet.
   *
   * @param model the current worksheet.
   */
  public void update(WorksheetReadOnlyModel model, Coord c) {
    if (this.dep.contains(c)) {
      Coord s1 = startingCoord1;
      Coord s2 = startingCoord2;
      pieChartData.clear();
      try {
        for (int i = 0; i < length; i++) {
          String name = model.getCellValue(new Coord(s1.col, s1.row + i)).toString();
          double value = Double.parseDouble(model.getCellValue(new Coord(s2.col, s2.row + i))
                  .toString());
          pieChartData.setValue(name, value);
        }
      } catch (NullPointerException npe) {
        throw new IllegalStateException("Critical Cell was deleted");
      }
    }
  }

  @Override
  public String toString() {
    return startingCoord1.toString() + " " + startingCoord2.toString() + " " + length + " "
            + title;
  }

  /**
   * Lowers the position of this chart in the hierarchy.
   */
  public void shiftPosition() {
    this.position--;
  }

  /**
   * Gets the potion of this chart in a list.
   *
   * @return the integer position of this chart.
   */
  public int getPosition() {
    return this.position;
  }

  /**
   * Gets the data from this chart.
   *
   * @return the data of this chart.
   */
  public DefaultPieDataset getData() {
    return this.pieChartData;
  }

  /**
   * Gets the title of this chart.
   *
   * @return the String title of this chart.
   */
  public String getTitle() {
    return this.title;
  }
}
