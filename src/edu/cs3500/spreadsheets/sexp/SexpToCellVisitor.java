package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.ColumnReference;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.cellstructure.Reference;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * A visitor that builds a Cell out of an Sexp.
 */
public class SexpToCellVisitor implements SexpVisitor<Cell> {
  private final String NUMERICS = "0123456789";
  private final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private WorksheetReadOnlyModel model;

  public SexpToCellVisitor(WorksheetReadOnlyModel model) {
    Objects.requireNonNull(model);
    this.model = model;
  }

  @Override
  public Cell visitBoolean(boolean b) {
    return new CellBoolean(b);
  }

  @Override
  public Cell visitNumber(double d) {
    return new CellDouble(d);
  }

  @Override
  public Cell visitSList(List<Sexp> l) {
    List<Cell> c = new ArrayList<>();
    for (int i = 1; i < l.size(); i++) {
      c.add(l.get(i).accept(new SexpToCellVisitor(this.model)));
    }
    return new Function(l.get(0).accept(new OperationVisitor()), c);
  }

  @Override
  public Cell visitSymbol(String s) {
    ArrayList l = new ArrayList<Coord>();
    try {
      return new ColumnReference(s, this.model);
    } catch (IllegalArgumentException e) {
      if (isSingleReference(s)) {
        l.add(coordFromString(s));
        Reference tempReference = new Reference(l);
        return tempReference;
      } else if (isDoubleReference(s)) {
        l = parseMultiCoordinate(s);
        if (l.size() == 0) {
          throw new IllegalArgumentException("Given multi-reference is poorly "
                  + "formatted or syntactically incorrect.");
        }
        return new Reference(l);
      } else {
        return new CellString(s);

      }
    }
  }

  @Override
  public Cell visitString(String s) {
    return new CellString(s);
  }

  private boolean isSingleReference(String s) {
    boolean b = s.length() > 1;
    boolean expectingNum = false;
    for (int i = 0; i < s.length(); i++) {
      boolean temp = this.NUMERICS.contains(s.substring(i, i + 1));

      if (temp && i > 0) {
        expectingNum = true;
      }

      b = b && ((!expectingNum && ALPHABET.contains(s.substring(i, i + 1)))
              || (expectingNum && temp));
    }
    return b;
  }

  private boolean isDoubleReference(String s) {
    int index = s.indexOf(":");
    if (index == -1) {
      return false;
    }

    return isSingleReference(s.substring(0, index)) && isSingleReference(s.substring(index + 1));
  }

  private ArrayList<Coord> parseMultiCoordinate(String s) {
    ArrayList<Coord> l = new ArrayList<>();
    int index = s.indexOf(":");
    String firstCoord = s.substring(0, index);
    String lastCoord = s.substring(index + 1);
    Coord first = coordFromString(firstCoord);
    Coord last = coordFromString(lastCoord);

    for (int i = first.col; i <= last.col; i++) {
      for (int j = first.row; j <= last.row; j++) {
        Coord c = new Coord(i, j);
        l.add(c);
      }
    }

    return l;
  }

  private Coord coordFromString(String s) {
    String rowStr = "1";
    String colString = "";
    for (int i = 0; i < s.length(); i++) {
      if (NUMERICS.contains(s.substring(i, i + 1))) {
        rowStr = s.substring(i);
        colString = s.substring(0, i);
        break;
      }
    }

    int row = Integer.parseInt(rowStr);
    return new Coord(Coord.colNameToIndex(colString), row);
  }
}
