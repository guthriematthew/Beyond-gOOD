package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A class that visits a formula and checks if there are any numbers.
 */
public class AnyNumberVisitor implements CellVisitor<Boolean> {
  private Map<Coord, Cell> map;

  /**
   * Creates an instance of a visitor with the given map.
   */
  public AnyNumberVisitor(Map map) {
    if (map == null) {
      throw new IllegalArgumentException("SumVisitor initialized with null");
    }
    this.map = map;
  }

  @Override
  public Boolean visitCellBoolean(boolean b) {
    return false;
  }

  @Override
  public Boolean visitCellDouble(double d) {
    return true;
  }

  @Override
  public Boolean visitReference(List<Coord> l) {
    boolean hasNum = false;
    for (Coord c : l) {
      if (map.containsKey(c)) {
        hasNum = hasNum || map.get(c).accept(this);
      }
    }
    return hasNum;
  }

  @Override
  public Boolean visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }


  @Override
  public Boolean visitCellString(String s) {
    return false;
  }

  @Override
  public Boolean visitCellFunction(Function f) {
    List<Cell> list = f.getArgs();
    boolean b = false;
    for (Cell c : list) {
      b = b || c.accept(this);
    }
    return b;
  }
}
