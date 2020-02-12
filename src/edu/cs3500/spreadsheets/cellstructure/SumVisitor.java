package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * A visitor class that implements sum over a given input.
 */
public class SumVisitor implements CellVisitor<Double> {
  private Map<Coord, Cell> map;
  private Map<Coord, Cache> cache;

  /**
   * Creates an instance of a visitor with the given map.
   */
  public SumVisitor(Map map, Map cache) {
    if (map == null || cache == null) {
      throw new IllegalArgumentException("SumVisitor initialized with null");
    }
    this.map = map;
    this.cache = cache;
  }

  @Override
  public Double visitCellBoolean(boolean b) {
    return 0.0;
  }

  @Override
  public Double visitCellDouble(double d) {
    return d;
  }

  @Override
  public Double visitReference(List<Coord> l) {
    double sum = 0;
    for (Coord c : l) {
      if (map.get(c) != null) {
        sum += map.get(c).accept(this);
      }
    }
    return sum;
  }

  @Override
  public Double visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }

  @Override
  public Double visitCellString(String s) {
    return 0.0;
  }

  @Override
  public Double visitCellFunction(Function f) {
    List<Cell> list = f.getArgs();
    double d = 0;
    for (Cell c : list) {
      d += c.accept(new EvaluateVisitor(map, cache)).accept(this);
    }
    return d;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SumVisitor)) {
      return false;
    }
    SumVisitor that = (SumVisitor) o;
    return that.map.equals(this.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.map);
  }
}
