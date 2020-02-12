package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.cs3500.spreadsheets.cellstructure.Value;

/**
 * Represents a cached cell, which is a cell that knows all cells dependent on it. Used for
 * efficient cycle detection and refactoring.
 */
public class Cache {
  private Value value;
  private Coord self;
  private HashSet<Coord> dependency;

  /**
   * Constructor for a Cache cell.
   *
   * @param v The value of the cell to be cached.
   * @param s The coord of the cell to be cached.
   * @param d The set of all references and dependencies for the cached cell.
   * @param h The current cache of all cells to be processed.
   */
  public Cache(Value v, Coord s, HashSet<Coord> d, HashMap<Coord, Cache> h) {
    if (v == null || s == null || d == null || h == null) {
      throw new IllegalArgumentException("Cache give null arguments");
    }
    List<Coord> copy = new ArrayList<>(h.keySet());
    for (Coord c : copy) {
      if (h.get(c).dependency.contains(s)) {
        h.remove(h.get(c).self);
      }
    }
    this.value = v;
    this.self = s;
    this.dependency = d;
  }

  /**
   * Returns the value of a cached cell.
   *
   * @return The value of a cached cell.
   */
  public Value accessCache() {
    return this.value;
  }

  /**
   * Returns the dependencies of a cached cell.
   *
   * @return The dependencies of a cached cell.
   */
  public HashSet<Coord> accessDependency() {
    return this.dependency;
  }
}
