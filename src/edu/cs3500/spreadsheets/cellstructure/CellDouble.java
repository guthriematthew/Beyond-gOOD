package edu.cs3500.spreadsheets.cellstructure;

/**
 * A class representing a cell storing a double.
 */
public class CellDouble implements Value {
  public final double value;

  /**
   * Constructs a double with the given value.
   *
   * @param d the value of the double.
   */
  public CellDouble(double d) {
    this.value = d;
  }


  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitCellDouble(this.value);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof CellDouble)) {
      return false;
    }
    CellDouble that = (CellDouble)o;
    return that.value == this.value;
  }

  @Override
  public int hashCode() {
    Double d = this.value;
    return d.intValue();
  }

  @Override
  public String toString() {
    return this.accept(new ShowVisitor());
  }
}
