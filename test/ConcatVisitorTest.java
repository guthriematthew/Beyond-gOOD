import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.ConcatVisitor;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static junit.framework.TestCase.assertEquals;

/**
 * A class to hold tests and examples for the ConcatVisitor class.
 */
public class ConcatVisitorTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  Map<Coord, Cell> m;
  ConcatVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    m = new HashMap<>();
    v = new ConcatVisitor(m);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull() {
    ConcatVisitor v = new ConcatVisitor(null);
  }

  @Test
  public void visitCellBooleanTrue() {
    assertEquals("true", v.visitCellBoolean(true));
  }


  @Test
  public void visitCellBooleanFalse() {
    assertEquals("false", v.visitCellBoolean(false));
  }

  @Test
  public void visitCellDouble0() {
    assertEquals("0.000000", v.visitCellDouble(0));
  }

  @Test
  public void visitCellDouble5() {
    assertEquals("5.000000", v.visitCellDouble(5));
  }

  @Test
  public void visitCellDouble5Long() {
    assertEquals("5.000000", v.visitCellDouble(5.000000005));
  }

  @Test
  public void visitReference() {
    m.put(cr.a1, new CellString("foo"));
    m.put(cr.a2, new CellBoolean(true));
    m.put(cr.a3, new CellDouble(5));
    assertEquals("footrue5.000000",
            v.visitReference(new ArrayList<>(Arrays.asList(cr.a1, cr.a2, cr.a3))));

  }

  @Test
  public void visitUndefReference() {
    m.put(cr.a1, new CellString("foo"));
    m.put(cr.a3, cl.referenceNotDefined);
    m.put(cr.a2, new CellBoolean(true));
    assertEquals("footrue",
            v.visitReference(new ArrayList<>(Arrays.asList(cr.a1, cr.a2, cr.a3))));

  }


  @Test
  public void visitCellStringEmpty() {
    assertEquals("", v.visitCellString(""));
  }

  @Test
  public void visitCellStringFoo() {
    assertEquals("foo", v.visitCellString("foo"));
  }

  @Test
  public void visitCellFunction() {
    assertEquals("\"foo\"\"bar\"",
            v.visitCellFunction(new Function("CONCAT",
                    new ArrayList<Cell>(Arrays.asList(cl.cellStringFoo, cl.cellStringBar)))));
  }
}