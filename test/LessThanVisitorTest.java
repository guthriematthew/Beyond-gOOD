import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.LessThanVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * A class to test the methods of the ProductVisitor class.
 */
public class LessThanVisitorTest {
  private ExampleCells ex;
  private Map<Coord, Cell> m;
  private Map<Coord, Cache> h;

  @Before
  public void setup() {
    ex = new ExampleCells();
    m = new HashMap<>();
    m.put(new Coord(1, 1), ex.cellDoubleFour);
    m.put(new Coord(1, 3), ex.cellDoubleTwo);
    m.put(new Coord(1, 4), ex.cellDoubleThree);
    h = new HashMap<>();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor() {
    new LessThanVisitor(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoCache() {
    new LessThanVisitor(m, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoMap() {
    new LessThanVisitor(null, h);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitCellBooleanTrue() {
    ex.cellBooleanTrue.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisitCellBooleanFalse() {
    ex.cellBooleanFalse.accept(new LessThanVisitor(m, h));
  }

  @Test
  public void visitCellDoubleZero() {
    assertFalse(ex.cellDoubleZero.accept(new LessThanVisitor(m, h)));
  }

  @Test
  public void visitCellDoubleNegative() {
    assertFalse(ex.cellDoubleNegOne.accept(new LessThanVisitor(m, h)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitSingleReference() {
    ex.referenceA5.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitMultiReference() {
    ex.referenceA2.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitUndefReference() {
    ex.referenceNotDefined.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellString() {
    ex.cellStringDog.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunction() {
    ex.productOverNums.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionSomeNonNum() {
    ex.productOverNumsAndOther.accept(new LessThanVisitor(m, h));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionNonNum() {
    ex.productOverOther.accept(new LessThanVisitor(m, h));
  }

  @Test
  public void visitCellFunctionStrictInequality() {
    assertFalse(ex.lessThanEqual.accept(new LessThanVisitor(m, h)));
  }

  @Test
  public void visitCellFunctionFalse() {
    assertFalse(ex.lessThanFalse.accept(new LessThanVisitor(m, h)));
  }

  @Test
  public void visitCellFunctionTrue() {
    assertTrue(ex.lessThanTrue.accept(new LessThanVisitor(m, h)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionOther() {
    assertTrue(ex.lessThanOther.accept(new LessThanVisitor(m, h)));
  }
}