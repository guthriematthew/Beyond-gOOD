package edu.cs3500.spreadsheets.cellstructure;


/**
 * An interface representing a cell in a worksheet.
 */
public interface Cell {

  /**
   * Accepts a visitor, allowing it to do operations on cells.
   *
   * @param visitor The visitor object that will perform the task.
   * @param <R>     Return type.
   * @return the output determined by the visitor.
   */
  <R> R accept(CellVisitor<R> visitor);

}
