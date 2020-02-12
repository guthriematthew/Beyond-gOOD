package edu.cs3500.spreadsheets.cellstructure;

import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * Visitor over Cells for determining if an output should be prefaced with =.
 */
public class AppendEqualsVisitor implements CellVisitor<Boolean> {
  @Override
  public Boolean visitCellBoolean(boolean b) {
    return false;
  }

  @Override
  public Boolean visitCellDouble(double d) {
    return false;
  }

  @Override
  public Boolean visitReference(List<Coord> l) {
    return true;
  }

  @Override
  public Boolean visitColumnReference(ColumnReference columnReference) {
    return true;
  }

  @Override
  public Boolean visitCellString(String s) {
    return false;
  }

  @Override
  public Boolean visitCellFunction(Function f) {
    return true;
  }
}
