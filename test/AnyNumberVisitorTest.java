import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.AnyNumberVisitor;
import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;

/**
 * A class to hold tests and examples for the AnyNumberVisitor class.
 */
public class AnyNumberVisitorTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  Map<Coord, Cell> m;
  AnyNumberVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    m = new HashMap<>();
    v = new AnyNumberVisitor(m);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNull() {
    AnyNumberVisitor v = new AnyNumberVisitor(null);
  }

  @Test
  public void visitCellBooleanTrue() {
    assertEquals(false, v.visitCellBoolean(true));
  }

  @Test
  public void visitCellBooleanFalse() {
    assertEquals(false, v.visitCellBoolean(false));
  }

  @Test
  public void visitCellDouble0() {
    assertEquals(true, v.visitCellDouble(0));
  }

  @Test
  public void visitCellDoubleFive() {
    assertEquals(true, v.visitCellDouble(5));
  }

  @Test
  public void visitCellDoubleTenLong() {
    assertEquals(true, v.visitCellDouble(10.000004));
  }

  @Test
  public void visitReferenceNoNum() {
    m.put(cr.a1, cl.cellBooleanFalse);
    m.put(cr.a2, cl.cellStringBlerner);
    List<Coord> list = new ArrayList<>(Arrays.asList(cr.a1, cr.a2));
    assertEquals(false, v.visitReference(list));
  }

  @Test
  public void visitReferenceNum() {
    m.put(cr.a1, cl.cellDoubleTwo);
    m.put(cr.a2, cl.cellStringDog);
    m.put(cr.a3, cl.cellBooleanFalse);
    List<Coord> list = new ArrayList<>(Arrays.asList(cr.a1, cr.a2, cr.a3));
    assertEquals(true, v.visitReference(list));
  }

  @Test
  public void visitReferenceFunction() {
    m.put(cr.a1, cl.cellBooleanFalse);
    m.put(cr.a2, cl.cellStringDog);
    m.put(cr.a3, cl.sumOverNums);
    List<Coord> list = new ArrayList<>(Arrays.asList(cr.a1, cr.a2, cr.a3));
    assertEquals(true, v.visitReference(list));
  }

  @Test
  public void visitCellStringEmpty() {
    assertEquals(false, v.visitCellString(""));
  }

  @Test
  public void visitCellStringFoo() {
    assertEquals(false, v.visitCellString("foo"));
  }

  @Test
  public void visitCellStringNumber() {
    assertEquals(false, v.visitCellString("Hi Mom 5"));
  }

  @Test
  public void visitCellFunctionNoNum() {
    assertEquals(false, v.visitCellFunction(cl.concatOverStrings));
  }

  @Test
  public void visitCellFunctionNum() {
    assertEquals(true, v.visitCellFunction(cl.sumOverNums));
  }
}