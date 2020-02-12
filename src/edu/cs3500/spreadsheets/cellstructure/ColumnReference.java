package edu.cs3500.spreadsheets.cellstructure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * A reference to all the cells in a given column.
 */
public class ColumnReference implements Formula {
  private final List<String> columns;
  private WorksheetReadOnlyModel model;

  /**
   * Creates a column reference withing the given model and of the given string.
   * @param reference the string name of the reference.
   * @param model the model that the reference exists in.
   */
  public ColumnReference(String reference, WorksheetReadOnlyModel model) {
    if (reference == null || model == null) {
      throw new IllegalArgumentException("ColumnReference cannot be instantiated with null args.");
    }
    if (!this.correctlyFormattedReference(reference)) {
      throw new IllegalArgumentException("The given column is not formatted correctly.");
    }
    this.columns = this.parseColumns(reference);
    this.model = model;
  }

  //Determines if a String given to the constructor is formatted correctly for a ColumnReference
  private boolean correctlyFormattedReference(String reference) {
    return this.isColumn(reference) || this.isMultiReference(reference);
  }

  private boolean isMultiReference(String reference) {
    List<String> columns = this.parseColumns(reference);
    if (columns.size() == 1) {
      return this.isColumn(columns.get(0));
    } else {
      return this.isColumn(columns.get(0)) && this.isColumn(columns.get(1))
              && columns.get(0).compareTo(columns.get(1)) <= 0;
    }
  }

  private List<String> parseColumns(String reference) {
    if (this.isColumn(reference)) {
      return new ArrayList<>(Collections.singletonList(reference));
    } else {
      String firstColumn = "";
      String lastColumn = "";
      for (int i = 1; i <= reference.length(); i++) {
        if (!reference.substring(i - 1, i).equals(":")) {
          firstColumn = firstColumn + reference.substring(i - 1, i);
        } else {
          lastColumn = reference.substring(i);
          break;
        }
      }
      return new ArrayList<>(Arrays.asList(firstColumn, lastColumn));
    }
  }

  private boolean isColumn(String column) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    boolean answer = !column.equals("");
    for (int i = 0; i < column.length(); i++) {
      String letter = column.substring(i, i + 1);
      answer = answer && alphabet.contains(letter);
    }
    return answer;
  }

  List<Coord> getColumnReference() {
    List<Coord> allCoords = model.getNonEmptyCells();
    Set<Coord> columnCoords = new LinkedHashSet<>();
    for (Coord c : allCoords) {
      if (this.isInColumnRange(c)) {
        columnCoords.add(c);
      }
    }
    return new ArrayList<>(columnCoords);
  }

  boolean isInColumnRange(Coord c) {
    if (this.getColumns().size() == 1) {
      return this.getColumns().get(0).equals(Coord.colIndexToName(c.col));

    } else {
      return this.getColumns().get(0).compareTo(Coord.colIndexToName(c.col)) <= 0
              && this.getColumns().get(1).compareTo(Coord.colIndexToName(c.col)) >= 0;
    }
  }


  List<String> getColumns() {
    return Collections.unmodifiableList(this.columns);
  }


  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitColumnReference(this);
  }

  @Override
  public String toString() {
    if (this.columns.size() == 1) {
      return "=" + this.columns.get(0);
    } else {
      return "=" + this.columns.get(0) + ":" + this.columns.get(1);
    }
  }

  @Override
  public boolean equals(Object that) {
    if (!(that instanceof ColumnReference)) {
      return false;
    }
    ColumnReference other = (ColumnReference) that;
    return this.columns.equals(other.columns) && this.model.equals(other.model);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.columns);
  }
}
