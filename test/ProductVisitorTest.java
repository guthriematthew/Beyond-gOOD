import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.ProductVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * A class to test the methods of the ProductVisitor class.
 */
public class ProductVisitorTest {
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
    new ProductVisitor(null, true, null);
  }

  @Test
  public void testVisitCellBooleanTrue() {
    Double d = 0.0;
    assertEquals(d, ex.cellBooleanTrue.accept(new ProductVisitor(m, false, h)));
  }

  @Test
  public void testVisitCellBooleanFalse() {
    Double d = 0.0;
    assertEquals(d, ex.cellBooleanFalse.accept(new ProductVisitor(m, false, h)));
  }

  @Test
  public void visitCellDoubleZero() {
    Double d = 0.0;
    assertEquals(d, ex.cellDoubleZero.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitCellDoubleTwo() {
    Double d = 2.0;
    assertEquals(d, ex.cellDoubleTwo.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitCellDoubleNegative() {
    Double d = -1.0;
    assertEquals(d, ex.cellDoubleNegOne.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitSingleReference() {
    Double d = 4.0;
    assertEquals(d, ex.referenceA5.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitAnotherSingleReference() {
    Double d = 3.0;
    assertEquals(d, ex.referenceA3.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitMultiReference() {
    Double d = 6.0;
    assertEquals(d, ex.referenceA2.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitUndefinedReference() {
    Double d = 0.0;
    assertEquals(d, ex.referenceNotDefined.accept(new ProductVisitor(m, false, h)));
  }

  @Test
  public void visitCellString() {
    Double d = 0.0;
    assertEquals(d, ex.cellStringDog.accept(new ProductVisitor(m, false, h)));
  }

  @Test
  public void visitCellAnotherString() {
    Double d = 0.0;
    assertEquals(d, ex.cellStringBar.accept(new ProductVisitor(m, false, h)));
  }

  @Test
  public void visitCellFunction() {
    Double d = 0.0;
    assertEquals(d, ex.productOverNums.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitCellFunctionSomeNonNum() {
    Double d = 10.0;
    assertEquals(d, ex.productOverNumsAndOther.accept(new ProductVisitor(m, true, h)));
  }

  @Test
  public void visitCellFunctionNonNum() {
    Double d = 0.0;
    assertEquals(d, ex.productOverOther.accept(new ProductVisitor(m, false, h)));
  }
}