import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellString;
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
 * A class to hold tests and examples for the CellString class.
 */
public class CellStringTest {

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
    CellString c1 = new CellString("foo");
    CellString c2 = new CellString("bar");
    Double d = 0.0;
    assertEquals(d, c1.accept(new SumVisitor(this.m, this.c)));
    assertEquals(d, c2.accept(new SumVisitor(this.m, this.c)));
  }

  @Test
  public void acceptProduct() {
    CellString c1 = new CellString("foo");
    CellString c2 = new CellString("bar");
    Double d = 0.0;
    assertEquals(d, c1.accept(new ProductVisitor(this.m, false, this.c)));
    assertEquals(d, c2.accept(new ProductVisitor(this.m, false, this.c)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void acceptLessThan() {
    CellString c1 = new CellString("foo");
    c1.accept(new LessThanVisitor(this.m, this.c));
  }

  @Test
  public void acceptConcat() {
    CellString c1 = new CellString("foo");
    CellString c2 = new CellString("bar");
    CellString c3 = new CellString("\"GET BLERNED\"");
    assertEquals("foo", c1.accept(new ConcatVisitor(this.m)));
    assertEquals("bar", c2.accept(new ConcatVisitor(this.m)));
    assertEquals("\"GET BLERNED\"", c3.accept(new ConcatVisitor(this.m)));
  }

  @Test
  public void testEqualsTrue() {
    CellString c1 = new CellString("foo");
    CellString c2 = new CellString("foo");
    assertEquals(c1, c2);
  }

  @Test
  public void testEqualsFalse() {
    CellString c1 = new CellString("foo");
    CellString c2 = new CellString("bar");
    assertNotEquals(c1, c2);
  }
}