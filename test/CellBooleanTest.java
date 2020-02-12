import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
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
 * A class to hold tests and examples for the CellBoolean class.
 */
public class CellBooleanTest {

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
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(false);
    Double d1 = 0.0;
    assertEquals(d1, c1.accept(new SumVisitor(m, c)));
    assertEquals(d1, c2.accept(new SumVisitor(m, c)));
  }

  @Test
  public void acceptProduct() {
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(false);
    Double d1 = 0.0;
    assertEquals(d1, c1.accept(new ProductVisitor(m, false, c)));
    assertEquals(d1, c2.accept(new ProductVisitor(m, false, c)));
    Double d2 = 1.0;
    assertEquals(d2, c1.accept(new ProductVisitor(m, true, c)));
    assertEquals(d2, c2.accept(new ProductVisitor(m, true, c)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void acceptLessThanTrue() {
    CellBoolean c1 = new CellBoolean(true);
    Double d1 = 0.0;
    c1.accept(new LessThanVisitor(this.m, this.c));
  }

  @Test(expected = IllegalArgumentException.class)
  public void acceptLessThanFalse() {
    CellBoolean c1 = new CellBoolean(false);
    Double d1 = 0.0;
    c1.accept(new LessThanVisitor(this.m, this.c));
  }

  @Test
  public void acceptConcat() {
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(false);
    assertEquals("true", c1.accept(new ConcatVisitor(m)));
    assertEquals("false", c2.accept(new ConcatVisitor(m)));
  }

  @Test
  public void testEqualsTrue() {
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(true);
    CellBoolean c3 = new CellBoolean(false);
    CellBoolean c4 = new CellBoolean(false);
    assertEquals(c1, c2);
    assertEquals(c3, c4);
  }

  @Test
  public void testEqualsFalse() {
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(false);
    assertNotEquals(c1, c2);
    assertNotEquals(c2, c1);
  }

  @Test
  public void testHashCode() {
    CellBoolean c1 = new CellBoolean(true);
    CellBoolean c2 = new CellBoolean(false);
    assertEquals(1, c1.hashCode());
    assertEquals(0, c2.hashCode());
  }
}