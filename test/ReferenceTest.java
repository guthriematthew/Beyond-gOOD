import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.ConcatVisitor;
import edu.cs3500.spreadsheets.cellstructure.LessThanVisitor;
import edu.cs3500.spreadsheets.cellstructure.ProductVisitor;
import edu.cs3500.spreadsheets.cellstructure.Reference;
import edu.cs3500.spreadsheets.cellstructure.SumVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A class for holding tests and examples for the Reference class.
 */
public class ReferenceTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  HashMap<Coord, Cell> m;
  HashMap<Coord, Cell> c;
  ConcatVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    m = new HashMap<>();
    c = new HashMap<>();
    v = new ConcatVisitor(m);
  }

  @Test
  public void acceptSumSingle() {
    m.put(cr.a2, new CellDouble(5));
    Double d = 5.0;
    assertEquals(d, cl.referenceA1.accept(new SumVisitor(m, c)));
  }

  @Test
  public void acceptSumMulti() {
    m.put(cr.a3, new CellDouble(5));
    m.put(cr.a4, new CellDouble(10));
    Double d = 15.0;
    assertEquals(d, cl.referenceA2.accept(new SumVisitor(m, c)));
  }

  @Test
  public void acceptProductSingle() {
    m.put(cr.a2, new CellDouble(5));
    Double d = 5.0;
    assertEquals(d, cl.referenceA1.accept(new ProductVisitor(m, true, c)));
  }

  @Test
  public void acceptProductMulti() {
    m.put(cr.a3, new CellDouble(5));
    m.put(cr.a4, new CellDouble(10));
    Double d = 50.0;
    assertEquals(d, cl.referenceA2.accept(new ProductVisitor(m, true, c)));
  }

  @Test
  public void acceptConcatSingle() {
    m.put(cr.a2, new CellString("foo"));
    assertEquals("foo", cl.referenceA1.accept(new ConcatVisitor(m)));
  }

  @Test
  public void acceptConcatMulti() {
    m.put(cr.a3, new CellString("foo"));
    m.put(cr.a4, new CellString("bar"));
    assertEquals("foobar", cl.referenceA2.accept(new ConcatVisitor(m)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void acceptLessThanSingle() {
    m.put(cr.a2, new CellDouble(5));
    assertEquals(false, cl.referenceA1.accept(new LessThanVisitor(m, c)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void acceptLessThanMulti() {
    m.put(cr.a3, new CellDouble(5));
    m.put(cr.a4, new CellDouble(10));
    assertEquals(true, cl.referenceA2.accept(new LessThanVisitor(m, c)));
  }

  @Test
  public void testEqualsTrue() {
    Reference r = new Reference(new ArrayList<>(Arrays.asList(new Coord(1, 3),
            new Coord(1, 4))));
    assertEquals(r, cl.referenceA2);
  }

  @Test
  public void testEqualsFalse() {
    Reference r = new Reference(new ArrayList<>(Arrays.asList(new Coord(1, 2))));
    assertNotEquals(r, cl.referenceA5);
    Reference r2 = new Reference(new ArrayList<>(Arrays.asList(new Coord(3, 2))));
    assertNotEquals(r2, cl.referenceA5);
  }

}