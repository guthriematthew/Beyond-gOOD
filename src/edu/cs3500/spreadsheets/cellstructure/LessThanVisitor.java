package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * A class that visits cells and returns whether or not the first is greater.
 */
public class LessThanVisitor implements CellVisitor<Boolean> {
  private Map<Coord, Cell> map;
  private Map<Coord, Cache> cache;


  /**
   * Creates an instance of a visitor with the given map.
   */
  public LessThanVisitor(Map map, Map cache) {
    if (map == null || cache == null) {
      throw new IllegalArgumentException("SumVisitor initialized with null");
    }
    this.map = map;
    this.cache = cache;
  }

  @Override
  public Boolean visitCellBoolean(boolean b) {
    throw new IllegalArgumentException("Boolean given to less than");
  }

  @Override
  public Boolean visitCellDouble(double d) {
    return false;
  }

  @Override
  public Boolean visitReference(List<Coord> l) {
    throw new IllegalArgumentException("Reference given to less than");
  }

  @Override
  public Boolean visitColumnReference(ColumnReference columnReference) {
    throw new IllegalArgumentException("Column Reference given to less than");
  }

  @Override
  public Boolean visitCellString(String s) {
    throw new IllegalArgumentException("String given to less than");
  }

  @Override
  public Boolean visitCellFunction(Function f) {
    List<Cell> list = f.getArgs();
    if (list.size() != 2) {
      throw new IllegalArgumentException("Less than only accepts two arguments.");
    }
    return list.get(0).accept(new EvaluateVisitor(map, cache)).accept(new LessThanValueVisitor())
            < list.get(1).accept(new EvaluateVisitor(map, cache))
            .accept(new LessThanValueVisitor());
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof LessThanVisitor)) {
      return false;
    }
    LessThanVisitor that = (LessThanVisitor) o;
    return that.map.equals(this.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.map);
  }
}
