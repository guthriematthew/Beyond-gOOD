import org.junit.Before;
import org.junit.Test;

import edu.cs3500.spreadsheets.cellstructure.ColumnReference;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;

/**
 * A class to test the newly added column reference behavior.
 */
public class ColumnReferenceTest {
  Worksheet w;
  ColumnReference cr;

  @Before
  public void initData() {
    w = new Worksheet();
    cr = new ColumnReference("A", w);
  }

  @Test
  public void basicSumTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "=(SUM A)");
    assertEquals("=(SUM A)",
            w.getCellExpression(new Coord(2, 1)).toString());
    assertEquals("6.000000", w.getCellValue(new Coord(2, 1)).toString());
  }

  @Test
  public void basicMultiColumnSumTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "1");
    w.setCell(new Coord(2, 2), "2");
    w.setCell(new Coord(2, 3), "3");
    w.setCell(new Coord(3, 1), "=(SUM A:B)");
    assertEquals("=(SUM A:B)",
            w.getCellExpression(new Coord(3, 1)).toString());
    assertEquals("12.000000", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test
  public void basicProductTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "4");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "=(PRODUCT A)");
    assertEquals("=(PRODUCT A)",
            w.getCellExpression(new Coord(2, 1)).toString());
    assertEquals("12.000000", w.getCellValue(new Coord(2, 1)).toString());
  }

  @Test
  public void basicMultiColumnProductTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "4");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "1");
    w.setCell(new Coord(2, 2), "6");
    w.setCell(new Coord(2, 3), "8");
    w.setCell(new Coord(3, 1), "=(PRODUCT A:B)");
    assertEquals("=(PRODUCT A:B)",
            w.getCellExpression(new Coord(3, 1)).toString());
    assertEquals("576.000000", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessThanSingleFail() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 1), "2");
    w.setCell(new Coord(2, 1), "=(< A)");
    w.getCellValue(new Coord(2, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void lessThanMultiFail() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(2, 1), "2");
    w.setCell(new Coord(3, 1), "=(< A:B)");
    w.getCellValue(new Coord(3, 1));
  }

  @Test
  public void basicConcatTest() {
    w.setCell(new Coord(1, 1), "foo");
    w.setCell(new Coord(1, 2), "bar");
    w.setCell(new Coord(1, 3), "baz");
    w.setCell(new Coord(2, 1), "=(CONCAT A)");
    assertEquals("=(CONCAT A)",
            w.getCellExpression(new Coord(2, 1)).toString());
    assertEquals("\"foobarbaz\"", w.getCellValue(new Coord(2, 1)).toString());
  }

  @Test
  public void complexSingleConcatTest() {
    w.setCell(new Coord(1, 1), "foo");
    w.setCell(new Coord(2, 2), "bar");
    w.setCell(new Coord(3, 3), "baz");
    w.setCell(new Coord(4, 1), "=(CONCAT A B C \"joe\")");
    assertEquals("=(CONCAT A B C \"joe\")",
            w.getCellExpression(new Coord(4, 1)).toString());
    assertEquals("\"foobarbazjoe\"", w.getCellValue(new Coord(4, 1)).toString());
  }

  @Test
  public void basicMultiColumnConcatTest() {
    w.setCell(new Coord(1, 1), "foo");
    w.setCell(new Coord(2, 2), "bar");
    w.setCell(new Coord(3, 3), "baz");
    w.setCell(new Coord(4, 1), "=(CONCAT A:C)");
    assertEquals("=(CONCAT A:C)",
            w.getCellExpression(new Coord(4, 1)).toString());
    assertEquals("\"foobarbaz\"", w.getCellValue(new Coord(4, 1)).toString());
  }

  @Test
  public void complexMultiColumnConcatTest() {
    w.setCell(new Coord(1, 1), "foo");
    w.setCell(new Coord(2, 2), "bar");
    w.setCell(new Coord(3, 3), "baz");
    w.setCell(new Coord(4, 1), "test");
    w.setCell(new Coord(5, 2), "these");
    w.setCell(new Coord(6, 3), "strings");
    w.setCell(new Coord(7, 1), "=(CONCAT A:C D:E A:F)");
    assertEquals("=(CONCAT A:C D:E A:F)",
            w.getCellExpression(new Coord(7, 1)).toString());
    assertEquals("\"foobarbaztestthesefoobarbaztestthesestrings\"",
            w.getCellValue(new Coord(7, 1)).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void selfReferenceTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(1, 1), "=(SUM A)");
    w.getCellValue(new Coord(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void indirectSingleReferenceTest() {
    w.setCell(new Coord(1, 1), "=(SUM B)");
    assertEquals("=(SUM B)",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.setCell(new Coord(2, 1), "=(SUM A)");
    assertEquals("=(SUM B)",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.getCellValue(new Coord(2, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void foo() {
    w.setCell(new Coord(1, 1), "=D:F");
    assertEquals("=D:F",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.setCell(new Coord(4, 1), "=A:C");
    assertEquals("=D:F",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.getCellValue(new Coord(4, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void bar() {
    w.setCell(new Coord(1, 1), "=B");
    assertEquals("=B",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.setCell(new Coord(2, 1), "=A1");
    assertEquals("=B",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.getCellValue(new Coord(2, 1));
  }

  @Test
  public void baz() {
    w.setCell(new Coord(1, 1), "=10");
    assertEquals("10.000000",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("10.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.setCell(new Coord(2, 1), "=A");
    assertEquals("10.000000",
            w.getCellExpression(new Coord(1, 1)).toString());
    assertEquals("10.000000",
            w.getCellValue(new Coord(1, 1)).toString());
    w.getCellValue(new Coord(2, 1));
  }

  @Test
  public void sameMultiCoordTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "=A:A");
    assertEquals("=A:A",
            w.getCellExpression(new Coord(2, 1)).toString());
    assertEquals("6.000000", w.getCellValue(new Coord(2, 1)).toString());
  }

  @Test
  public void backWardsMultiCoordTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "1");
    w.setCell(new Coord(2, 2), "2");
    w.setCell(new Coord(2, 3), "3");
    w.setCell(new Coord(3, 1), "=B:A");
    assertEquals("\"B:A\"",
            w.getCellValue(new Coord(3, 1)).toString());
    assertEquals("\"B:A\"", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test
  public void unfinishedMultiCoordFrontTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "1");
    w.setCell(new Coord(2, 2), "2");
    w.setCell(new Coord(2, 3), "3");
    w.setCell(new Coord(3, 1), "=:B");
    assertEquals("\":B\"",
            w.getCellValue(new Coord(3, 1)).toString());
    assertEquals("\":B\"", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test
  public void unfinishedMultiCoordBackTest() {
    w.setCell(new Coord(1, 1), "1");
    w.setCell(new Coord(1, 2), "2");
    w.setCell(new Coord(1, 3), "3");
    w.setCell(new Coord(2, 1), "1");
    w.setCell(new Coord(2, 2), "2");
    w.setCell(new Coord(2, 3), "3");
    w.setCell(new Coord(3, 1), "=A:");
    assertEquals("\"A:\"",
            w.getCellValue(new Coord(3, 1)).toString());
    assertEquals("\"A:\"", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test
  public void wordBecomesColumnReference() {
    w.setCell(new Coord(3, 1), "=FOOBAR");
    assertEquals("=FOOBAR",
            w.getCellExpression(new Coord(3, 1)).toString());
    assertEquals("0.000000", w.getCellValue(new Coord(3, 1)).toString());
  }

  @Test
  public void wordDoesNotBecomeColumnReference() {
    w.setCell(new Coord(3, 1), "=NotAllUppercase");
    assertEquals("\"NotAllUppercase\"",
            w.getCellExpression(new Coord(3, 1)).toString());
    assertEquals("\"NotAllUppercase\"",
            w.getCellValue(new Coord(3, 1)).toString());
  }

}