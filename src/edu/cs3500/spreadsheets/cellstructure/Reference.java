package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A Class that represents a reference to 1 or more cells.
 */
public class Reference implements Formula {
  private final List<Coord> referencedCells;

  /**
   * A constructor that takes in a list of cells that it references.
   *
   * @param rc a list of cells.
   */
  public Reference(List<Coord> rc) {
    this.referencedCells = rc;
  }


  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitReference(this.referencedCells);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Reference)) {
      return false;
    }
    Reference that = (Reference) o;

    return this.referencedCells.equals(that.referencedCells);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.referencedCells);
  }

  @Override
  public String toString() {
    return "=" + this.accept(new ShowVisitor());
  }
}
