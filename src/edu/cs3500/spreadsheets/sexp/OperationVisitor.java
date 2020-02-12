package edu.cs3500.spreadsheets.sexp;

import java.util.List;

/**
 * Visitor to get the operator out of a function.
 */
public class OperationVisitor implements SexpVisitor<String> {
  @Override
  public String visitBoolean(boolean b) {
    throw new IllegalStateException("Should not be applied to boolean");
  }

  @Override
  public String visitNumber(double d) {
    throw new IllegalStateException("Should not be applied to double");
  }

  @Override
  public String visitSList(List<Sexp> l) {
    throw new IllegalStateException("Should not be applied to List<Sexp>");
  }

  @Override
  public String visitSymbol(String s) {
    return s;
  }

  @Override
  public String visitString(String s) {
    throw new IllegalStateException("Should not be applied to String");
  }

}
