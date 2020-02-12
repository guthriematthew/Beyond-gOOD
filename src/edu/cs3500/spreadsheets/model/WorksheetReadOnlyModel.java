package edu.cs3500.spreadsheets.model;

import java.util.List;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.Value;

/**
 * A model for a spreadsheet which allows only for information to be read. This model cannot be
 * edited or changed, and must be originally built as a {@link WorksheetModel} to assign values to
 * specific Coords. Cells can either be returned as their raw contents or in an evaluated form. If
 * an erroneous Cell, such as a circular reference or a non-existant function, was assigned a Coord,
 * that Cell is still stored in the model, but attempting to get the expression or value of these
 * Cells throws an error. See method documentation formore details. This interface is extended by
 * {@link WorksheetModel} to add editing functionality.
 */
public interface WorksheetReadOnlyModel {
  /**
   * Gets the Cell of the given Coord in the spreadsheet. To get the String representation of
   * this Cell, use the toString method. The Cell will be returned as it was given to the model,
   * unevaluated.
   *
   * @param c the coordinate of the cell whose value is desired.
   * @return the Cell at that coordinate, or null if the cell is not set to any Cell.
   * @throws IllegalArgumentException if the Cell at the given Coord has an error/is invalid.
   *         The error's text will be the String originally given to the model for this Coord.
   */
  Cell getCellExpression(Coord c) throws IllegalArgumentException;

  /**
   * Gets the Value at the given Coord from the spreadsheet. To get the String representation of
   * this Value, use the toString method. If the Cell at the given Coord is a Function or a
   * Reference, it will first be evaluated to a Value (model representation of a Boolean, double,
   * or String) then returned. Other forms of data are already evaluated, and will be returned as
   * they were originally given to the model.
   *
   * @param c the coordinate of the cell whose value is desired.
   * @return the Value of the Cell at the coord, or null if the cell is not set to any Cell.
   * @throws IllegalArgumentException if the Cell at the given Coord has an error/is invalid.
   *         The error's text will be the generic String: "Cell has an error", as the String
   *         originally given cannot be evaluated.
   */
  Value getCellValue(Coord c) throws IllegalArgumentException;

  /**
   * Gets the list of Coords for all cells with a Value, Formula, or invalid Cell (error). All
   * other Coords will return null when given to getCellExpression or getCellValue.
   * @return The list of Coords for all cells with a Value, Formula, or invalid Cell.
   */
  List<Coord> getNonEmptyCells();

  /**
   * Access the list of charts stored in he model.
   * @return a list of all charts stored in the model.
   */
  List<Chart> getCharts();
}
