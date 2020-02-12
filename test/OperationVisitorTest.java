import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.sexp.OperationVisitor;
import edu.cs3500.spreadsheets.sexp.SNumber;
import edu.cs3500.spreadsheets.sexp.SSymbol;

import static org.junit.Assert.assertEquals;

/**
 * A class to hold tests and examples for the OperationVisitor class.
 */
public class OperationVisitorTest {
  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  OperationVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    v = new OperationVisitor();
  }

  @Test(expected = IllegalStateException.class)
  public void visitBooleanTrue() {
    v.visitBoolean(true);
  }

  @Test(expected = IllegalStateException.class)
  public void visitBooleanFalse() {
    v.visitBoolean(true);
  }

  @Test(expected = IllegalStateException.class)
  public void visitNumber0() {
    v.visitNumber(0);
  }

  @Test(expected = IllegalStateException.class)
  public void visitNumber5Long() {
    v.visitNumber(5.00000001);
  }

  @Test(expected = IllegalStateException.class)
  public void visitSList() {
    v.visitSList(new ArrayList<>(Arrays.asList(new SSymbol("<"), new SNumber(4),
            new SNumber(3))));
  }

  @Test
  public void visitSymbolEmpty() {
    assertEquals("", v.visitSymbol(""));
  }

  @Test
  public void visitSymbolFoo() {
    assertEquals("foo", v.visitSymbol("foo"));
  }

  @Test
  public void visitSymbolBlerner() {
    assertEquals("GET BLERNED", v.visitSymbol("GET BLERNED"));
  }

  @Test(expected = IllegalStateException.class)
  public void visitStringEmpty() {
    v.visitString("");
  }

  @Test(expected = IllegalStateException.class)
  public void visitStringFoo() {
    v.visitString("foo");
  }
}