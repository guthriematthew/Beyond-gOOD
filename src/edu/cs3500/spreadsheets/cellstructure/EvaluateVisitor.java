package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * A visitor that gets the value of a cell.
 */
public class EvaluateVisitor implements CellVisitor<Value> {
  private Map<Coord, Cell> map;
  private CellVisitor v;
  private Map<Coord, Cache> cache;

  /**
   * Creates an instance of a visitor with the given map.
   */
  public EvaluateVisitor(Map map, Map cache) {
    if (map == null || cache == null) {
      throw new IllegalArgumentException("SumVisitor initialized with null");
    }
    this.map = map;
    this.cache = cache;
    this.v = new SumVisitor(this.map, this.cache);
  }

  /**
   * Creates an instance of a visitor with the given map.
   */
  public EvaluateVisitor(Map map, CellVisitor v, Map cache) {
    if (map == null || v == null || cache == null) {
      throw new IllegalArgumentException("EvaluateVisitor initialized with null");
    }

    this.map = map;
    this.cache = cache;
    this.v = v;
  }


  @Override
  public Value visitCellBoolean(boolean b) {
    return new CellBoolean(b);
  }

  @Override
  public Value visitCellDouble(double d) {
    return new CellDouble(d);
  }

  @Override
  public Value visitReference(List<Coord> l) {
    if (l == null) {
      throw new IllegalArgumentException("EvaluateVisitor visitReference given null");
    }
    for (Coord c : l) {
      if (c == null) {
        throw new IllegalArgumentException("EvaluateVisitor visitReference given null coord");
      }
    }

    if (l.size() == 1) {
      if (map.containsKey(l.get(0)) && l.get(0) != null) {
        if (cache.containsKey(l.get(0)) && cache.get(l.get(0)) != null) {
          return cache.get(l.get(0)).accessCache();
        }
        return map.get(l.get(0)).accept(new EvaluateVisitor(map, cache));
      } else {
        return new CellDouble(0);
      }
    } else {
      if (v.equals(new SumVisitor(map, cache))) {
        double d = 0;
        for (Coord c : l) {
          if (cache.containsKey(c) && cache.get(c) != null) {
            d += cache.get(c).accessCache().accept(new SumVisitor(map, cache));
          } else if (cache.get(c) != null) {
            d += map.get(c).accept(new EvaluateVisitor(map, cache))
                    .accept(new SumVisitor(map, cache));
          }
        }
        return new CellDouble(d);
      } else if (v.equals(new ProductVisitor(map, true, cache))
              || v.equals(new ProductVisitor(map, false, cache))) {
        double d = 1;
        for (Coord c : l) {
          if (cache.containsKey(c) && cache.get(c) != null) {
            d *= cache.get(c).accessCache().accept(new ProductVisitor(map,
                    cache.get(c).accessCache().accept(new AnyNumberVisitor(map)), cache));
          } else if (cache.get(c) != null) {
            d *= map.get(c).accept(new EvaluateVisitor(map, cache)).accept(new ProductVisitor(map,
                    map.get(c).accept(new AnyNumberVisitor(map)), cache));
          }
        }
        return new CellDouble(d);
      } else if (v.equals(new ConcatVisitor(map))) {
        String s = "";
        for (Coord c : l) {
          if (cache.containsKey(c) && cache.get(c) != null) {
            s = s.concat(cache.get(c).accessCache().accept(new ConcatVisitor(map)));
          } else if (cache.get(c) != null) {
            s = s.concat(map.get(c).accept(new EvaluateVisitor(map, cache))
                    .accept(new ConcatVisitor(map)));
          }
        }
        return new CellString(s);
      } else {
        throw new IllegalArgumentException("Not given a correct operation");
      }
    }
  }

  @Override
  public Value visitColumnReference(ColumnReference columnReference) {
    return this.visitReference(columnReference.getColumnReference());
  }

  @Override
  public Value visitCellString(String s) {
    return new CellString(s);
  }

  @Override
  public Value visitCellFunction(Function f) {
    switch (f.getOp()) {
      case "SUM":
        return new CellDouble(f.accept(new SumVisitor(map, cache)));
      case "PRODUCT":
        return new CellDouble(f.accept(new ProductVisitor(map,
                f.accept(new AnyNumberVisitor(map)), cache)));
      case "<":
        return new CellBoolean(f.accept(new LessThanVisitor(map, cache)));
      case "CONCAT":
        return new CellString(f.accept(new ConcatVisitor(map)));
      default:
        throw new IllegalArgumentException("Not a valid operator");
    }
  }
}
