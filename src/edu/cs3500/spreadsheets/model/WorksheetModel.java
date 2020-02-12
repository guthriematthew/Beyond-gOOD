package edu.cs3500.spreadsheets.model;

/**
 * An extension of the {@link WorksheetReadOnlyModel} interface. This interface adds the ability
 * to assign a Cell to a specific Coord, with the Cell given as it's String representation. If the
 * given String cannot be represented as a Cell or is erroneous, such as a circular reference or a
 * non-existant function, the Cell is still stored.
 */
public interface WorksheetModel extends WorksheetReadOnlyModel {

  /**
   * Assigns the given Coord with the Cell representation of the given String. If the
   * given String cannot be represented as a Cell or is erroneous, such as a circular reference or a
   * non-existant function, the Cell is still stored.
   *
   * @param c the coordinate to assign to the Cell.
   * @param s the String representation of the Cell.
   */
  void setCell(Coord c, String s);

  /**
   * Adds a chart to the given model's implementation of charts.
   * @param chart the chart to be added to this model.
   */
  void addChart(Chart chart);
}
