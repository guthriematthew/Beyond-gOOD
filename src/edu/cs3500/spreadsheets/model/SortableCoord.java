package edu.cs3500.spreadsheets.model;

/**
 * A Coord that also has the property of being sorted. This makes model standardization easier.
 */
public class SortableCoord extends Coord implements Comparable<Coord> {

  /**
   * Creates a sortable coord with the given Row and Column.
   * @param col the given column.
   * @param row the given row.
   */
  public SortableCoord(int col, int row) {
    super(col, row);
  }

  @Override
  public int compareTo(Coord c) {
    if (this.col == c.col) {
      return this.row - c.row;
    } else {
      return this.col - c.col;
    }

  }
}
