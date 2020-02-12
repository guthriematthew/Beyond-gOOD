package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A class that visits cell and prints their values.
 */
public class ShowVisitor implements CellVisitor<String> {

  @Override
  public String visitCellBoolean(boolean b) {
    Boolean temp = b;
    return temp.toString();
  }

  @Override
  public String visitCellDouble(double d) {
    Double temp = d;
    return String.format("%.6f", temp);
  }

  @Override
  public String visitReference(List<Coord> l) {
    if (l.size() == 1) {
      return Coord.colIndexToName(l.get(0).col) + l.get(0).row;
    } else {
      return Coord.colIndexToName(l.get(0).col) + l.get(0).row + ":"
              + Coord.colIndexToName(l.get(l.size() - 1).col) + l.get(l.size() - 1).row;
    }
  }

  @Override
  public String visitColumnReference(ColumnReference columnReference) {
    if (columnReference.getColumns().size() == 1) {
      return columnReference.getColumns().get(0);
    } else {
      return columnReference.getColumns().get(0) + ":"
              + columnReference.getColumns().get(1);
    }
  }

  @Override
  public String visitCellString(String s) {
    return "\"" + s + "\"";
  }

  @Override
  public String visitCellFunction(Function f) {
    String s = "(" + f.getOp();
    List<Cell> l = f.getArgs();
    s += " ";
    for (Cell c : l) {
      s = s.concat(c.accept(this)) + " ";
    }
    s = s.substring(0, s.length() - 1);
    s = s.concat(")");
    return s;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof ShowVisitor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this);
  }
}
