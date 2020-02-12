package edu.cs3500.spreadsheets.cellstructure;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

/**
 * Determines the dependencies of a visited cell. Used for efficient cycle detection and caching.
 */
public class DependencyVisitor implements CellVisitor<HashSet<Coord>> {
  private HashSet<Coord> dependency;
  private HashMap<Coord, Cell> map;
  private HashMap<Coord, Cache> cache;
  private Coord givenCoord;
  private HashSet<ColumnReference> columnDependency;

  /**
   * Constructs a new Dependency visitor.
   *
   * @param dependency the list of already found dependencies.
   * @param map        the map of data in the cells.
   */
  public DependencyVisitor(HashSet<Coord> dependency,
                           HashMap<Coord, Cell> map, Coord c) {
    if (dependency == null || map == null || c == null) {
      throw new IllegalArgumentException("DependencyVisitor cannot be instantiated with null.");
    }
    this.dependency = dependency;
    this.map = map;
    if (this.dependency.contains(c)) {
      throw new IllegalArgumentException("Cycles not permitted in inputs");
    }
    this.dependency.add(c);
    this.givenCoord = c;
    this.columnDependency = new HashSet<>();
  }

  /**
   * Constructs a new DependencyVisitor from a non coordinate.
   *
   * @param dependency the cells dependent on it.
   * @param map        the mapping of cells.
   */
  public DependencyVisitor(HashSet<Coord> dependency,
                           HashMap<Coord, Cell> map, HashMap<Coord, Cache> cache,
                           Coord givenCoord) {
    if (dependency == null || map == null || cache == null) {
      throw new IllegalArgumentException("DependencyVisitor cannot be instantiated with null.");
    }
    this.dependency = dependency;
    this.map = map;
    this.cache = cache;
    this.givenCoord = givenCoord;
    this.columnDependency = new HashSet<>();
  }

  @Override
  public HashSet<Coord> visitCellBoolean(boolean b) {
    return dependency;
  }

  @Override
  public HashSet<Coord> visitCellDouble(double d) {
    return dependency;
  }

  @Override
  public HashSet<Coord> visitReference(List<Coord> l) {
    if (l == null) {
      throw new IllegalArgumentException("DependencyVisitor - visitReference - "
              + "given list cannot be null");
    }
    HashSet<Coord> tempMap = new HashSet<>();
    for (Coord c : l) {
      if (cache != null && cache.get(c) != null && cache.containsKey(c)
              && cache.get(c).accessDependency().size() != 0) {
        HashSet<Coord> tester = cache.get(c).accessDependency();
        tempMap.addAll(tester);
      } else if (map.get(c) != null && map.containsKey(c)) {
        tempMap.addAll(map.get(c).accept(this));
      }
      for (ColumnReference columnReference : this.columnDependency) {
        if (columnReference.isInColumnRange(c)) {
          this.dependency.add(c);
        }
      }
      dependency.add(c);
    }
    tempMap.addAll(dependency);
    return tempMap;
  }

  @Override
  public HashSet<Coord> visitColumnReference(ColumnReference columnReference) {
    List<String> columns = columnReference.getColumns();
    if (columns.size() == 1) {
      String column = columns.get(0);
      if (this.columnDependency.contains(column)) {
        throw new IllegalArgumentException("Cycle detected");
      }
      if (column.equals(Coord.colIndexToName(this.givenCoord.col))) {
        this.dependency.add(this.givenCoord);
      }
    } else {
      String firstColumn = columns.get(0);
      String lastColumn = columns.get(1);
      if (firstColumn.compareTo(Coord.colIndexToName(this.givenCoord.col)) <= 0
              && lastColumn.compareTo(Coord.colIndexToName(this.givenCoord.col)) >= 0) {
        this.dependency.add(this.givenCoord);
      }
    }
    this.columnDependency.add(columnReference);
    return this.visitReference(columnReference.getColumnReference());
  }

  @Override
  public HashSet<Coord> visitCellString(String s) {
    return dependency;
  }

  @Override
  public HashSet<Coord> visitCellFunction(Function f) {
    HashSet<Coord> tempMap = new HashSet<>();
    for (Cell c : f.getArgs()) {
      tempMap.addAll(c.accept(new DependencyVisitor(new HashSet<>(dependency), map, cache,
              this.givenCoord)));
    }
    tempMap.addAll(dependency);
    return tempMap;
  }
}
