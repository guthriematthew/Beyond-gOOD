package edu.cs3500.spreadsheets.cellstructure;

import java.util.Objects;

/**
 * A class representing a cell storing a string.
 */
public class CellString implements Value {
  private final String value;

  /**
   * Constructs a cell with the given string value.
   *
   * @param s the value of the string cell.s
   */
  public CellString(String s) {
    this.value = s;
  }

  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitCellString(this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CellString)) {
      return false;
    }
    CellString that = (CellString) o;
    return this.value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.value);
  }

  @Override
  public String toString() {
    return this.accept(new ShowVisitor());
  }
}
