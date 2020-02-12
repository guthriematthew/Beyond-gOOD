import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.SumVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * A class to tests the methods of the SumVisitor.
 */
public class SumVisitorTest {
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
  public void testConstructorNull() {
    new SumVisitor(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoMap() {
    new SumVisitor(null, h);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoCache() {
    new SumVisitor(m, null);
  }

  @Test
  public void testVisitCellBooleanTrue() {
    Double d = 0.0;
    assertEquals(d, ex.cellBooleanTrue.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellBooleanFalse() {
    Double d = 0.0;
    assertEquals(d, ex.cellBooleanFalse.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellDoubleFour() {
    Double d = 4.0;
    assertEquals(d, ex.cellDoubleFour.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellDoubleOne() {
    Double d = 1.0;
    assertEquals(d, ex.cellDoubleOne.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellDoubleZero() {
    Double d = 0.0;
    assertEquals(d, ex.cellDoubleZero.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitSingleReference() {
    Double d = 4.0;
    assertEquals(d, ex.referenceA5.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitMultiReference() {
    Double d = 5.0;
    assertEquals(d, ex.referenceA2.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitUndefReference() {
    Double d = 0.0;
    assertEquals(d, ex.referenceNotDefined.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellString() {
    Double d = 0.0;
    assertEquals(d, ex.cellStringDog.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitAnotherCellString() {
    Double d = 0.0;
    assertEquals(d, ex.cellStringFoo.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellFunctionAllNums() {
    Double d = 15.0;
    assertEquals(d, ex.sumOverNums.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellFunctionWithNonNum() {
    Double d = 7.0;
    assertEquals(d, ex.sumOverNumsAndOther.accept(new SumVisitor(m, h)));
  }

  @Test
  public void testVisitCellFunctionWithAllNonNum() {
    Double d = 0.0;
    assertEquals(d, ex.sumOverOther.accept(new SumVisitor(m, h)));
  }
}