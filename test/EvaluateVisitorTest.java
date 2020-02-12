import org.junit.Before;
import org.junit.Test;


import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.ConcatVisitor;
import edu.cs3500.spreadsheets.cellstructure.EvaluateVisitor;
import edu.cs3500.spreadsheets.cellstructure.LessThanVisitor;
import edu.cs3500.spreadsheets.cellstructure.ProductVisitor;

import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.cellstructure.SumVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * A class that tests EvaluateVisitor's functionality and methods.
 */
public class EvaluateVisitorTest {
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
    new EvaluateVisitor(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoMap() {
    new EvaluateVisitor(null, new ShowVisitor(), h);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoCache() {
    new EvaluateVisitor(m, new ShowVisitor(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNoVisitor() {
    new EvaluateVisitor(null, null);
  }

  @Test
  public void testVisitCellBooleanTrue() {
    assertEquals(new CellBoolean(true), ex.cellBooleanTrue.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void testVisitCellBooleanFalse() {
    assertEquals(new CellBoolean(false), ex.cellBooleanFalse.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellDoubleFour() {
    assertEquals(new CellDouble(4), ex.cellDoubleFour.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellDoubleZero() {
    assertEquals(new CellDouble(0), ex.cellDoubleZero.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellDoubleOne() {
    assertEquals(new CellDouble(1), ex.cellDoubleOne.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellDoubleNegative() {
    assertEquals(new CellDouble(-1), ex.cellDoubleNegOne.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitSingleReference() {
    assertEquals(new CellDouble(4), ex.referenceA5.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitMultiReference() {
    assertEquals(new CellDouble(0), ex.referenceA2.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellString() {
    assertEquals(new CellString("\"foo\""), ex.cellStringFoo.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellStringTwo() {
    assertEquals(new CellString("\"Who is Joe?\""),
            ex.cellStringJoe.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellFunction() {
    assertEquals(new CellString("4.000000false"),
            ex.concatOverOther.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellFunctionString() {
    assertEquals(new CellString("\"It smells like updog in here.\"\"GET BLERNED\""),
            ex.concatOverStrings.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellFunctionAnotherString() {
    assertEquals(new CellString("\"It smells like updog in here.\"\"GET BLERNED\"4.000000false"),
            ex.concatOverStringsAndOther.accept(new EvaluateVisitor(m, h)));
  }

  @Test
  public void visitCellFunctionProductSomeNonNum() {
    assertEquals(new CellDouble(10), ex.productOverNumsAndOther.accept(new EvaluateVisitor(m,
            new ProductVisitor(m, true, h), h)));
  }

  @Test
  public void visitCellFunctionProductNonNum() {
    assertEquals(new CellDouble(0), ex.productOverOther.accept(new EvaluateVisitor(m,
            new ProductVisitor(m, false, h), h)));
  }

  @Test
  public void visitCellFunctionSumNonNum() {
    assertEquals(new CellDouble(0), ex.sumOverOther.accept(new EvaluateVisitor(m,
            new SumVisitor(m, h), h)));
  }

  @Test
  public void visitCellFunctionSumNum() {
    assertEquals(new CellDouble(15), ex.sumOverNums.accept(new EvaluateVisitor(m,
            new SumVisitor(m, h), h)));
  }

  @Test
  public void visitCellFunctionConcat() {
    assertEquals(new CellString("\"It smells like updog in here.\"\"GET BLERNED\""),
            ex.concatOverStrings.accept(new EvaluateVisitor(m, new ConcatVisitor(m), h)));
  }

  @Test
  public void visitCellFunctionLessThanFalse() {
    assertEquals(new CellBoolean(false), ex.lessThanEqual.accept(new EvaluateVisitor(m,
            new LessThanVisitor(m, h), h)));
  }

  @Test
  public void visitCellFunctionLessThanTrue() {
    assertEquals(new CellBoolean(true), ex.lessThanTrue.accept(new EvaluateVisitor(m,
            new LessThanVisitor(m, h), h)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionLessThanError() {
    ex.lessThanOther.accept(new EvaluateVisitor(m, new LessThanVisitor(m, h), h));
  }
}