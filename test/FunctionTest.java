import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.cs3500.spreadsheets.cellstructure.Cell;
import edu.cs3500.spreadsheets.cellstructure.ConcatVisitor;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.cellstructure.LessThanVisitor;
import edu.cs3500.spreadsheets.cellstructure.ProductVisitor;
import edu.cs3500.spreadsheets.cellstructure.SumVisitor;
import edu.cs3500.spreadsheets.model.Cache;
import edu.cs3500.spreadsheets.model.Coord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A class for holding tests and examples for the Function class.
 */
public class FunctionTest {

  SumVisitor sum;
  ProductVisitor prod;
  ConcatVisitor cat;
  LessThanVisitor less;
  HashMap<Coord, Cell> m;
  HashMap<Coord, Cache> c;
  ExampleCoords cr;
  ExampleCells cl;

  @Before
  public void initData() {
    m = new HashMap<>();
    c = new HashMap<>();
    sum = new SumVisitor(m, c);
    prod = new ProductVisitor(m, true, c);
    cat = new ConcatVisitor(m);
    less = new LessThanVisitor(m, c);
    cr = new ExampleCoords();
    cl = new ExampleCells();
  }

  @Test
  public void acceptSum() {
    Double d = 15.0;
    assertEquals(d, cl.productOverNums.accept(sum));

  }

  @Test
  public void acceptProduct() {
    Double d = 10.0;
    assertEquals(d, cl.sumOverNumsAndOther.accept(prod));
  }

  @Test
  public void acceptConcat() {
    assertEquals("2.0000005.000000false\"GET BLERNED\"", cl.sumOverNumsAndOther.accept(cat));
  }

  @Test
  public void acceptLessThan() {
    Function f = new Function("SUM",
            new ArrayList<>(Arrays.asList(cl.cellDoubleThree, cl.cellDoubleOne)));
    assertEquals(false, f.accept(less));
  }

  @Test
  public void testEqualsTrue() {
    Function f = new Function("SUM", Arrays.asList(cl.cellDoubleZero,
            cl.cellDoubleOne, cl.cellDoubleTwo, cl.cellDoubleThree,
            cl.cellDoubleFour, cl.cellDoubleFive));
    assertEquals(f, cl.sumOverNums);
  }

  @Test
  public void testEqualsFalse() {
    Function f = new Function("SUM", Arrays.asList(cl.cellDoubleZero,
            cl.cellDoubleOne, cl.cellDoubleTwo, cl.cellDoubleThree,
            cl.cellDoubleFour, cl.cellDoubleFive));
    assertNotEquals(f, cl.sumOverOther);
    assertNotEquals(f, cl.productOverNumsAndOther);
  }
}