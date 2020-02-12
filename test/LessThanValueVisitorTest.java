import org.junit.Before;
import org.junit.Test;

import edu.cs3500.spreadsheets.cellstructure.LessThanValueVisitor;

import static org.junit.Assert.assertEquals;

/**
 * A class that tests the methods of the LessThanValue visitor.
 */
public class LessThanValueVisitorTest {

  private ExampleCells ex;

  @Before
  public void setup() {
    ex = new ExampleCells();
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellBoolean() {
    ex.cellBooleanFalse.accept(new LessThanValueVisitor());
  }

  @Test
  public void visitCellDoubleNegative() {
    Double d = -1.0;
    assertEquals(d, ex.cellDoubleNegOne.accept(new LessThanValueVisitor()));
  }

  @Test
  public void visitCellDoubleZero() {
    Double d = 0.0;
    assertEquals(d, ex.cellDoubleZero.accept(new LessThanValueVisitor()));
  }

  @Test
  public void visitCellDoubleOne() {
    Double d = 1.0;
    assertEquals(d, ex.cellDoubleOne.accept(new LessThanValueVisitor()));
  }

  @Test
  public void visitCellDoubleTwo() {
    Double d = 2.0;
    assertEquals(d, ex.cellDoubleTwo.accept(new LessThanValueVisitor()));
  }

  @Test
  public void visitCellDoubleThree() {
    Double d = 3.0;
    assertEquals(d, ex.cellDoubleThree.accept(new LessThanValueVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitReference() {
    ex.referenceA2.accept(new LessThanValueVisitor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellString() {
    ex.cellStringDog.accept(new LessThanValueVisitor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionSum() {
    ex.sumOverNumsAndOther.accept(new LessThanValueVisitor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionLessThan() {
    ex.lessThanOther.accept(new LessThanValueVisitor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void visitCellFunctionProd() {
    ex.productOverNumsAndOther.accept(new LessThanValueVisitor());
  }
}