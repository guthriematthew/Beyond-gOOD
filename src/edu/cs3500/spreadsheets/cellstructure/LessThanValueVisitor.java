package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A visitor that gets the double value of a cell.
 */
public class LessThanValueVisitor implements CellVisitor<Double> {


  @Override
  public Double visitCellBoolean(boolean b) {
    throw new IllegalArgumentException("Boolean given to less than");
  }

  @Override
  public Double visitCellDouble(double d) {
    return d;
  }

  @Override
  public Double visitReference(List<Coord> l) {
    throw new IllegalArgumentException("Reference given to less than");
  }

  @Override
  public Double visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }

  @Override
  public Double visitCellString(String s) {
    throw new IllegalArgumentException("String given to less than");

  }

  @Override
  public Double visitCellFunction(Function f) {
    throw new IllegalArgumentException("Boolean given to less than");
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof LessThanValueVisitor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this);
  }
}
