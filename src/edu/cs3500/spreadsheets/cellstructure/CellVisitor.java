package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.sexp.Sexp;

/**
 * An abstracted function object for processing any {@link Sexp}ressions.
 *
 * @param <R> The return type of this function
 */
public interface CellVisitor<R> {
  /**
   * Process a boolean value.
   *
   * @param b the value
   * @return the desired result
   */
  R visitCellBoolean(boolean b);

  /**
   * Process a numeric value.
   *
   * @param d the value
   * @return the desired result
   */
  R visitCellDouble(double d);

  /**
   * Process a list value.
   *
   * @param l the contents of the list (not yet visited)
   * @return the desired result
   */
  R visitReference(List<Coord> l);

  /**
   * Processes the values used to represent a column reference.
   * @param columnReference The column reference that has accepted this visitor.
   * @return the desired result.
   */
  R visitColumnReference(ColumnReference columnReference);

  /**
   * Process a string value.
   *
   * @param s the value
   * @return the desired result
   */
  R visitCellString(String s);

  /**
   * Process a function value.
   *
   * @param f the function
   * @return the desired result
   */
  R visitCellFunction(Function f);
}
