package edu.cs3500.spreadsheets.cellstructure;

/**
 * A class representing a cell storing a boolean.
 */
public class CellBoolean implements Value {
  private final boolean value;

  /**
   * A constructor given the value of the boolean.
   *
   * @param b desired value of boolean.
   */
  public CellBoolean(boolean b) {
    this.value = b;
  }

  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitCellBoolean(this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CellBoolean)) {
      return false;
    }
    CellBoolean that = (CellBoolean)o;
    return that.value == this.value;
  }

  @Override
  public int hashCode() {
    if (this.value) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return this.accept(new ShowVisitor());
  }
}
