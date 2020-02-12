import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.ConcatVisitor;
import edu.cs3500.spreadsheets.cellstructure.DependencyVisitor;
import edu.cs3500.spreadsheets.cellstructure.LessThanVisitor;
import edu.cs3500.spreadsheets.cellstructure.ProductVisitor;
import edu.cs3500.spreadsheets.cellstructure.SumVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A class to hold tests and examples for the CellDouble class.
 */
public class CellDoubleTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;
  HashMap<Coord, Cell> m;
  HashMap<Coord, Cache> c;
  HashSet<Coord> d;
  DependencyVisitor v;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
    m = new HashMap<>();
    c = new HashMap<>();
    d = new HashSet<>();
    v = new DependencyVisitor(d, m, c, cr.a1);
  }

  @Test
  public void acceptSum() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(1.00000000005);
    CellDouble c5 = new CellDouble(10);
    SumVisitor v = new SumVisitor(m, c);
    Double d1 = 0.0;
    Double d2 = 1.0;
    Double d3 = 5.13;
    Double d4 = 1.00000000005;
    Double d5 = 10.0;
    assertEquals(d1, c1.accept(v));
    assertEquals(d2, c2.accept(v));
    assertEquals(d3, c3.accept(v));
    assertEquals(d4, c4.accept(v));
    assertEquals(d5, c5.accept(v));
  }

  @Test
  public void acceptProductTrue() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(1.00000000005);
    CellDouble c5 = new CellDouble(10);
    ProductVisitor v = new ProductVisitor(m, true, c);
    Double d1 = 0.0;
    Double d2 = 1.0;
    Double d3 = 5.13;
    Double d4 = 1.00000000005;
    Double d5 = 10.0;
    assertEquals(d1, c1.accept(v));
    assertEquals(d2, c2.accept(v));
    assertEquals(d3, c3.accept(v));
    assertEquals(d4, c4.accept(v));
    assertEquals(d5, c5.accept(v));
  }

  @Test
  public void acceptProductFalse() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(1.00000000005);
    CellDouble c5 = new CellDouble(10);
    ProductVisitor v = new ProductVisitor(m, true, c);
    Double d1 = 0.0;
    Double d2 = 1.0;
    Double d3 = 5.13;
    Double d4 = 1.00000000005;
    Double d5 = 10.0;
    assertEquals(d1, c1.accept(v));
    assertEquals(d2, c2.accept(v));
    assertEquals(d3, c3.accept(v));
    assertEquals(d4, c4.accept(v));
    assertEquals(d5, c5.accept(v));
  }

  @Test
  public void acceptLessThan() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(1.00000000005);
    CellDouble c5 = new CellDouble(10);
    LessThanVisitor v = new LessThanVisitor(this.m, this.c);
    assertEquals(false, c1.accept(v));
    assertEquals(false, c2.accept(v));
    assertEquals(false, c3.accept(v));
    assertEquals(false, c4.accept(v));
    assertEquals(false, c5.accept(v));
  }


  @Test
  public void acceptConcat() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(1.00000000005);
    CellDouble c5 = new CellDouble(10);
    ConcatVisitor v = new ConcatVisitor(m);
    assertEquals("0.000000", c1.accept(v));
    assertEquals("1.000000", c2.accept(v));
    assertEquals("5.130000", c3.accept(v));
    assertEquals("1.000000", c4.accept(v));
    assertEquals("10.000000", c5.accept(v));
  }

  @Test
  public void testEqualsTrue() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(0);
    CellDouble c5 = new CellDouble(1.0);
    CellDouble c6 = new CellDouble(5.13);
    CellDouble c7 = new CellDouble(5.1300);
    assertEquals(c1, c4);
    assertEquals(c2, c5);
    assertEquals(c3, c6);
    assertEquals(c7, c6);
  }

  @Test
  public void testEqualsLong() {
    CellDouble c1 = new CellDouble(1.00000000005);
    CellDouble c2 = new CellDouble(1.00000000005);
    assertEquals(c1, c2);
  }

  @Test
  public void testEqualsFalse() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.0);
    CellDouble c3 = new CellDouble(5.13);
    assertNotEquals(c1, c2);
    assertNotEquals(c2, c3);
    assertNotEquals(c1, c3);
  }

  @Test
  public void testHashCode() {
    CellDouble c1 = new CellDouble(0);
    CellDouble c2 = new CellDouble(1.00000000005);
    CellDouble c3 = new CellDouble(5.13);
    CellDouble c4 = new CellDouble(10);
    assertEquals(0, c1.hashCode());
    assertEquals(1, c2.hashCode());
    assertEquals(5, c3.hashCode());
    assertEquals(10, c4.hashCode());
  }
}