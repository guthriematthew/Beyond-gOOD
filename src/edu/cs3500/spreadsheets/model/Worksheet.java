package edu.cs3500.spreadsheets.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.DependencyVisitor;
import edu.cs3500.spreadsheets.cellstructure.EvaluateVisitor;
import edu.cs3500.spreadsheets.cellstructure.Value;
import edu.cs3500.spreadsheets.sexp.Parser;
import edu.cs3500.spreadsheets.sexp.SexpToCellVisitor;
import edu.cs3500.spreadsheets.sexp.SexpToValueVisitor;

/**
 * A class that represents a worksheet of cells.  It holds all of the data including expression,
 * cached values, and errors.
 */
public class Worksheet implements WorksheetModel, WorksheetReadOnlyModel {
  private HashMap<Coord, Cell> expressions;
  private HashMap<Coord, Cache> cache;
  private HashMap<Coord, String> errorList;
  private List<Chart> charts;

  /**
   * Builds a default empty spreadsheet.
   */
  public Worksheet() {
    this.expressions = new HashMap<>();
    this.cache = new HashMap<>();
    this.errorList = new HashMap<>();
    this.charts = new ArrayList<>();
  }

  @Override
  public void setCell(Coord givenCoord, String inputString) {
    if (givenCoord == null) {
      throw new IllegalArgumentException("Null coord value given to setCell");
    }
    this.expressions.remove(givenCoord);
    this.cache.remove(givenCoord);
    this.errorList.remove(givenCoord);
    if (inputString != null) {
      try {
        storeCell(givenCoord, inputString);
      } catch (IllegalArgumentException e) {
        this.setCellError(givenCoord, inputString);
      }
    }
  }

  @Override
  public void addChart(Chart chart) {
    this.charts.add(chart);
  }

  private void storeCell(Coord givenCoord, String inputString) {
    Cell resultCell = this.parseCell(inputString);

    HashSet<Coord> dep = resultCell.accept(new DependencyVisitor(new HashSet<>(),
            expressions, cache, givenCoord));
    if (dep.contains(givenCoord)) {
      this.setCellError(givenCoord, inputString);
    } else {
      try {
        cache.put(givenCoord, new Cache(resultCell.accept(
                new EvaluateVisitor(expressions, cache)), givenCoord, dep, cache));

        resultCell.accept(new EvaluateVisitor(this.expressions, this.cache));
        expressions.put(givenCoord, resultCell);
      } catch (IllegalArgumentException e) {
        this.setCellError(givenCoord, inputString);
      }
    }
  }

  private Cell parseCell(String inputString) {
    if (inputString.startsWith("=")) {
      return Parser.parse(inputString.substring(1)).accept(new SexpToCellVisitor(this));
    } else {
      return Parser.parse(inputString).accept(new SexpToValueVisitor());
    }
  }

  @Override
  public Cell getCellExpression(Coord c) {
    if (!(expressions.containsKey(c))) {
      if (errorList.containsKey(c)) {
        throw new IllegalArgumentException(errorList.get(c));
      } else {
        return null;
      }
    } else {
      return expressions.get(c);
    }
  }

  @Override
  public Value getCellValue(Coord c) {
    if (cache.containsKey(c)) {
      Cell cell = expressions.get(c);
      Value v = cell.accept(new EvaluateVisitor(this.expressions, this.cache));
      return v;
    } else if (!(expressions.containsKey(c))) {
      if (errorList.containsKey(c)) {
        throw new IllegalArgumentException("Cell has an error");
      } else {
        return null;
      }
    } else {
      Value tempVal = expressions.get(c).accept(new EvaluateVisitor(expressions, cache));
      HashSet<Coord> dependency = tempVal.accept(
              new DependencyVisitor(new HashSet<>(), expressions, c));
      cache.put(c, new Cache(tempVal, c, dependency, cache));
      return tempVal;
    }

  }

  @Override
  public List<Coord> getNonEmptyCells() {
    List<Coord> nonEmptyCells = new ArrayList<>(this.expressions.keySet());
    nonEmptyCells.addAll(new ArrayList<>(this.errorList.keySet()));
    return this.sortCoords(nonEmptyCells);
  }

  @Override
  public List<Chart> getCharts() {
    return this.charts;
  }

  //Sorts a given list of coords
  private List<Coord> sortCoords(List<Coord> cellsToSort) {
    List<SortableCoord> sortableNonEmptyCells = new ArrayList<>();
    for (Coord c : cellsToSort) {
      sortableNonEmptyCells.add(new SortableCoord(c.col, c.row));
    }
    Collections.sort(sortableNonEmptyCells);
    List<Coord> sortedNonEmptyCells = new ArrayList<>();
    for (SortableCoord s : sortableNonEmptyCells) {
      sortedNonEmptyCells.add(new Coord(s.col, s.row));
    }
    return sortedNonEmptyCells;
  }

  private void setCellError(Coord c, String error) {
    this.setCell(c, null);
    this.errorList.put(c, error);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Worksheet)) {
      return false;
    }
    Worksheet that = (Worksheet) o;
    return that.cache.equals(this.cache) && that.expressions.equals(this.expressions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.cache, this.expressions);
  }

}
