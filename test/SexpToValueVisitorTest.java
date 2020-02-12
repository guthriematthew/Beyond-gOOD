import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.SBoolean;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SString;
import edu.cs3500.spreadsheets.sexp.SSymbol;
import edu.cs3500.spreadsheets.sexp.SexpToValueVisitor;

import static org.junit.Assert.assertEquals;

/**
 * A class to hold tests for the SexpToValueVisitor class using mock input.
 */
public class SexpToValueVisitorTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  SexpToValueVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    v = new SexpToValueVisitor();
  }

  @Test
  public void visitBooleanTrue() {
    assertEquals(new CellBoolean(true), v.visitBoolean(true));
  }

  @Test
  public void visitBooleanFalse() {
    assertEquals(new CellBoolean(false), v.visitBoolean(false));
  }

  @Test
  public void visitNumberZero() {
    assertEquals(new CellDouble(0), v.visitNumber(0));
  }

  @Test
  public void visitNumberPositiveRegular() {
    assertEquals(new CellDouble(8), v.visitNumber(8));
  }

  @Test
  public void visitNumberPositiveLong() {
    assertEquals(new CellDouble(20.0000005), v.visitNumber(20.0000005));
  }

  @Test
  public void visitNumberNegativeRegular() {
    assertEquals(new CellDouble(-5), v.visitNumber(-5));
  }

  @Test
  public void visitNumberNegativeLong() {
    assertEquals(new CellDouble(-1.0000005), v.visitNumber(-1.0000005));
  }

  @Test
  public void visitSListSum() {
    assertEquals(new CellString("(SUM 4.000000 3.000000)"),
            v.visitSList(new ArrayList<>(Arrays.asList(
                    new SSymbol("SUM"), new SNumber(4), new SNumber(3)))));
  }

  @Test
  public void visitSListProduct() {
    assertEquals(new CellString("(PRODUCT 5.000000 2.000000 false)"),
            v.visitSList(new ArrayList<>(Arrays.asList(
                    new SSymbol("PRODUCT"), new SNumber(5),
                    new SNumber(2), new SBoolean(false)))));
  }

  @Test
  public void visitSListLessThan() {
    assertEquals(new CellString("(< 4.000000 3.000000)"),
            v.visitSList(new ArrayList<>(Arrays.asList(new SSymbol("<"), new SNumber(4),
                    new SNumber(3)))));

  }

  @Test
  public void visitSListConcat() {
    assertEquals(new CellString("(CONCAT \"GET BLERNED\" \"It smells like updog in here.\")"),
            v.visitSList(new ArrayList<>(Arrays.asList(new SSymbol("CONCAT"),
                    new SString("GET BLERNED"),
                    new SString("It smells like updog in here.")))));
  }


  @Test
  public void visitStringEmpty() {
    assertEquals(new CellString(""), v.visitString(""));
  }

  @Test
  public void visitStringFoo() {
    assertEquals(new CellString("foo"), v.visitString("foo"));
  }

  @Test
  public void visitStringSlashesAndQuotes() {
    assertEquals(
            new CellString("I love the backslash character: \\\" - Ben Lerner, probably."),
            v.visitString(
                    "I love the backslash character: \\\" - Ben Lerner, probably."));
  }
}