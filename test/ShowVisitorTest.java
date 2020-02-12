import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.EvaluateVisitor;
import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;

/**
 * A class that tests that the ShowVisitor displays a visited cell.
 */
public class ShowVisitorTest {
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

  @Test
  public void visitCellBooleanTrue() {
    assertEquals("true", ex.cellBooleanTrue.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellBooleanFalse() {
    assertEquals("false", ex.cellBooleanFalse.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellDoubleZero() {
    assertEquals("0.000000", ex.cellDoubleZero.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellDoubleOne() {
    assertEquals("1.000000", ex.cellDoubleOne.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellDoubleThree() {
    assertEquals("3.000000", ex.cellDoubleThree.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellDoubleFive() {
    assertEquals("5.000000", ex.cellDoubleFive.accept(new ShowVisitor()));
  }

  @Test
  public void visitSingleReference() {
    assertEquals("A1", ex.referenceA5.accept(new ShowVisitor()));

  }

  @Test
  public void visitMultiReference() {
    assertEquals("A3:A4", ex.referenceA2.accept(new ShowVisitor()));

  }

  @Test
  public void visitCellString() {
    assertEquals("\"\"GET BLERNED\"\"", ex.cellStringBlerner.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellAnotherString() {
    assertEquals("\"\"foo\"\"", ex.cellStringFoo.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellOneMoreString() {
    assertEquals("\"\"bar\"\"", ex.cellStringBar.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunction() {
    assertEquals("(SUM 2.000000 5.000000 false \"\"GET BLERNED\"\")",
            ex.sumOverNumsAndOther.accept(new ShowVisitor()));

  }

  @Test
  public void visitCellFunctionNumbersSum() {
    assertEquals("(SUM 0.000000 1.000000 2.000000 3.000000 4.000000 5.000000)",
            ex.sumOverNums.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunctionNumbersSumEvaluated() {
    assertEquals("15.000000",
            ex.sumOverNums.accept(new EvaluateVisitor(m, h)).accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunctionNumbersProduct() {
    assertEquals("(PRODUCT 0.000000 1.000000 2.000000 3.000000 4.000000 5.000000)",
            ex.productOverNums.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunctionBoolean() {
    assertEquals("(< 5.000000 2.000000)",
            ex.lessThanFalse.accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunctionBooleanEvaluated() {
    assertEquals("false",
            ex.lessThanFalse.accept(new EvaluateVisitor(m, h)).accept(new ShowVisitor()));
  }

  @Test
  public void visitCellFunctionString() {
    assertEquals("(CONCAT \"\"It smells like updog in here.\"\" \"\"GET BLERNED\"\")",
            ex.concatOverStrings.accept(new ShowVisitor()));
  }
}