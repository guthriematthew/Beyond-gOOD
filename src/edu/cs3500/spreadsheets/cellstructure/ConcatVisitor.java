package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A visitor that concatenates strings.
 */
public class ConcatVisitor implements CellVisitor<String> {
  private Map<Coord, Cell> map;

  /**
   * Creates an instance of a visitor with the given map.
   */
  public ConcatVisitor(Map map) {
    if (map == null) {
      throw new IllegalArgumentException("ConcatVisitor initialized with null");
    }
    this.map = map;
  }

  @Override
  public String visitCellBoolean(boolean b) {
    Boolean val = b;
    return val.toString();
  }

  @Override
  public String visitCellDouble(double d) {
    return String.format("%.6f", d);
  }

  @Override
  public String visitReference(List<Coord> l) {
    String s = "";
    for (Coord c : l) {
      if (c != null && map.containsKey(c)) {
        s = s.concat(map.get(c).accept(this));
      }
    }
    return s;
  }

  @Override
  public String visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }

  @Override
  public String visitCellString(String s) {
    return s;
  }

  @Override
  public String visitCellFunction(Function f) {
    List<Cell> list = f.getArgs();
    String s = "";
    for (Cell c : list) {
      s = s.concat(c.accept(this));
    }
    return s;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ConcatVisitor)) {
      return false;
    }
    ConcatVisitor that = (ConcatVisitor) o;
    return that.map.equals(this.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.map);
  }
}
