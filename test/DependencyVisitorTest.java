import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.DependencyVisitor;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.cellstructure.Reference;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;

/**
 * A class to hold tests and examples for the DependencyVisitor class.
 */
public class DependencyVisitorTest {
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

  @Test(expected = IllegalArgumentException.class)
  public void constructorNullDependency() {
    v = new DependencyVisitor(null, m, c, cr.a1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorNullDependencyCoord() {
    v = new DependencyVisitor(null, m, cr.a3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorNullMap() {
    v = new DependencyVisitor(d, null, c, cr.a1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorNullMapCoord() {
    v = new DependencyVisitor(d, null, cr.a3);
  }

  @Test
  public void visitCellBooleanTrue() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a1);
    expected.add(cr.c10);
    d.add(cr.a1);
    d.add(cr.c10);
    assertEquals(expected, v.visitCellBoolean((true)));
  }

  @Test
  public void visitCellBooleanFalse() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a1);
    expected.add(cr.c10);
    d.add(cr.a1);
    d.add(cr.c10);
    assertEquals(expected, v.visitCellBoolean((false)));
  }

  @Test
  public void visitCellDouble() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.b1);
    expected.add(cr.b9);
    expected.add(cr.c10);
    d.add(cr.b1);
    d.add(cr.b9);
    d.add(cr.c10);
    assertEquals(expected, v.visitCellDouble(0));
  }

  @Test
  public void visitCellDoubleFive() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.b1);
    expected.add(cr.b9);
    expected.add(cr.c10);
    d.add(cr.b1);
    d.add(cr.b9);
    d.add(cr.c10);
    assertEquals(d, v.visitCellDouble(5));
  }

  @Test
  public void visitCellDoubleEightLong() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.b1);
    expected.add(cr.b9);
    expected.add(cr.c10);
    d.add(cr.b1);
    d.add(cr.b9);
    d.add(cr.c10);
    assertEquals(d, v.visitCellDouble(8.00000006));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitReferenceNull() {
    v.visitReference(null);
  }

  @Test
  public void visitReferenceSimple() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a2);
    expected.add(cr.a1);
    expected.add(cr.a5);
    d.add(cr.a1);
    d.add(cr.a5);
    assertEquals(expected, v.visitReference(new ArrayList<>(Arrays.asList(cr.a2))));
  }

  @Test
  public void visitReferenceDependsOnItself() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a1);
    assertEquals(expected, v.visitReference(new ArrayList<>(Arrays.asList(cr.a1))));
  }

  @Test
  public void visitReferenceInFunctionWithReference() {
    HashSet<Coord> expected = new HashSet<>();
    ArrayList<Coord> dep = new ArrayList<>(Arrays.asList(cr.a1, cr.a2));
    Reference ref = new Reference(dep);
    ArrayList<Cell> refs = new ArrayList<>();
    refs.add(ref);
    Function f = new Function("SUM", refs);
    expected.add(cr.a1);
    expected.add(cr.a2);
    assertEquals(expected,
            v.visitCellFunction(f));
  }

  @Test
  public void visitReferenceInFunctionWithSeveralReferences() {
    HashSet<Coord> expected = new HashSet<>();
    ArrayList<Coord> dep1 = new ArrayList<>(Arrays.asList(cr.a1, cr.a2));
    Reference ref1 = new Reference(dep1);
    ArrayList<Coord> dep2 = new ArrayList<>(Arrays.asList(cr.a3, cr.a5));
    Reference ref2 = new Reference(dep2);
    ArrayList<Cell> refs = new ArrayList<>();
    refs.add(ref1);
    refs.add(ref2);
    Function f = new Function("SUM", refs);
    expected.add(cr.a1);
    expected.add(cr.a2);
    expected.add(cr.a3);
    expected.add(cr.a5);
    assertEquals(expected,
            v.visitCellFunction(f));
  }

  @Test
  public void visitCellStringEmpty() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a6);
    expected.add(cr.b9);
    expected.add(cr.c2);
    d.add(cr.a6);
    d.add(cr.b9);
    d.add(cr.c2);
    assertEquals(expected, v.visitCellString(""));
  }

  @Test
  public void visitCellStringFoo() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a6);
    expected.add(cr.b9);
    expected.add(cr.c2);
    d.add(cr.a6);
    d.add(cr.b9);
    d.add(cr.c2);
    assertEquals(expected, v.visitCellString("foo"));
  }

  @Test
  public void visitCellStringBlerner() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a6);
    expected.add(cr.b9);
    expected.add(cr.c2);
    d.add(cr.a6);
    d.add(cr.b9);
    d.add(cr.c2);
    assertEquals(expected, v.visitCellString("GET BLERNED"));
  }

  @Test
  public void visitCellFunctionSum() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a15);
    expected.add(cr.c14);
    expected.add(cr.b6);
    d.add(cr.a15);
    d.add(cr.c14);
    d.add(cr.b6);
    assertEquals(expected,
            v.visitCellFunction(cl.sumOverNums));
  }

  @Test
  public void visitCellFunctionProduct() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a15);
    expected.add(cr.c14);
    expected.add(cr.b6);
    d.add(cr.a15);
    d.add(cr.c14);
    d.add(cr.b6);
    assertEquals(expected,
            v.visitCellFunction(cl.productOverNumsAndOther));
  }

  @Test
  public void visitCellFunctionLessThan() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a15);
    expected.add(cr.c14);
    expected.add(cr.b6);
    d.add(cr.a15);
    d.add(cr.c14);
    d.add(cr.b6);
    assertEquals(expected,
            v.visitCellFunction(cl.lessThanFalse));
  }

  @Test
  public void visitCellFunctionConcat() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a15);
    expected.add(cr.c14);
    expected.add(cr.b6);
    d.add(cr.a15);
    d.add(cr.c14);
    d.add(cr.b6);
    assertEquals(expected,
            v.visitCellFunction(cl.concatOverStrings));
  }

  @Test
  public void visitCellFunctionNested() {
    HashSet<Coord> expected = new HashSet<>();
    expected.add(cr.a15);
    expected.add(cr.c14);
    expected.add(cr.b6);
    d.add(cr.a15);
    d.add(cr.c14);
    d.add(cr.b6);
    assertEquals(expected,
            v.visitCellFunction(cl.nestedFunctions));
  }
}