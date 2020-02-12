package edu.cs3500.spreadsheets.sexp;

import java.util.ArrayList;
import java.util.List;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.cellstructure.Value;

/**
 * Gets the Value of a Sexp, to convert a Sexp into a Cell.
 */
public class SexpToValueVisitor implements SexpVisitor<Value> {
  @Override
  public Value visitBoolean(boolean b) {
    return new CellBoolean(b);
  }

  @Override
  public Value visitNumber(double d) {
    return new CellDouble(d);
  }

  @Override
  public Value visitSList(List<Sexp> l) {
    List<Sexp> copy = new ArrayList<>();
    copy.addAll(l);
    String operation = copy.remove(0).toString();
    List<Cell> c = new ArrayList<>();
    for (Sexp s : copy) {
      c.add(s.accept(this));
    }
    Function f = new Function(operation, c);
    String display = f.accept(new ShowVisitor());
    return new CellString(display);
  }

  @Override
  public Value visitSymbol(String s) {
    return new CellString(s);
  }

  @Override
  public Value visitString(String s) {
    return new CellString(s);
  }
}
