package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * A visitor class representing a multiplicative operation.
 */
public class ProductVisitor implements CellVisitor<Double> {
  private Map<Coord, Cell> map;
  private boolean hasNumericArg;
  private Map<Coord, Cache> cache;

  /**
   * Creates an instance of a visitor with the given map.
   */
  public ProductVisitor(Map map, boolean hasNum, Map cache) {
    if (map == null || cache == null) {
      throw new IllegalArgumentException("SumVisitor initialized with null");
    }
    this.map = map;
    hasNumericArg = hasNum;
    this.cache = cache;
  }


  @Override
  public Double visitCellBoolean(boolean b) {
    if (hasNumericArg) {
      return 1.0;
    } else {
      return 0.0;
    }
  }

  @Override
  public Double visitCellDouble(double d) {
    return d;
  }

  @Override
  public Double visitReference(List<Coord> l) {
    if (!this.hasNumericArg) {
      return 0.0;
    }

    double prod = 1;
    for (Coord c : l) {
      if (map.get(c) != null) {
        prod *= map.get(c).accept(this);
      }
    }
    return prod;
  }

  @Override
  public Double visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }


  @Override
  public Double visitCellString(String s) {
    if (hasNumericArg) {
      return 1.0;
    } else {
      return 0.0;
    }
  }

  @Override
  public Double visitCellFunction(Function f) {
    List<Cell> list = f.getArgs();
    double d = 1;
    for (Cell c : list) {
      d *= c.accept(new EvaluateVisitor(map, this, cache)).accept(this);
    }
    return d;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ProductVisitor)) {
      return false;
    }
    ProductVisitor that = (ProductVisitor) o;
    return that.map.equals(this.map) && this.hasNumericArg == that.hasNumericArg;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.map, this.hasNumericArg);
  }
}
