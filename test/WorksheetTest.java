import org.junit.Before;
import org.junit.Test;

import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * A class to hold tests for the Worksheet class using mock input. Many tests are many using
 * a ShowVisitor. This was done to both make expected output easier to create and to have the
 * expected output be more readable, thus ensuring the correct expected output is being returned.
 */
public class WorksheetTest {

  ExampleCells cl;
  ExampleCoords cr;
  Worksheet w;

  @Before
  public void initData() {
    w = new Worksheet();
    cl = new ExampleCells();
    cr = new ExampleCoords();
  }

  @Test(expected = IllegalArgumentException.class)
  public void constructorBadCoord() {
    w.setCell(null, "foo");
  }

  @Test
  public void constructorString() {
    w.setCell(cr.a3, null);
    assertNull(w.getCellValue(cr.a3));
    assertNull(w.getCellExpression(cr.a3));
  }

  @Test
  public void constructorEmptyWorksheet() {
    Worksheet worksheet = new Worksheet();
    assertEquals(this.w, worksheet);
  }

  @Test
  public void setCellIsItThere() {
    w.setCell(new Coord(1,1), "\"am i Here?\"");
    assertEquals("\"am i Here?\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"am i Here?\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellBooleanTrue() {
    w.setCell(cr.a1, "true");
    assertEquals("true", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("true", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellBooleanEqualTrue() {
    w.setCell(cr.a1, "=true");
    assertEquals("true", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("true", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellBooleanFalse() {
    w.setCell(cr.a1, "false");
    assertEquals("false", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellBooleanEqualFalse() {
    w.setCell(cr.a1, "=false");
    assertEquals("false", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellDoubleZero() {
    w.setCell(cr.c10, "0");
    assertEquals("0.000000", w.getCellExpression(cr.c10).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.c10).accept(new ShowVisitor()));
  }

  @Test
  public void setCellDoubleFive() {
    w.setCell(cr.c10, "5");
    assertEquals("5.000000", w.getCellExpression(cr.c10).accept(new ShowVisitor()));
    assertEquals("5.000000", w.getCellValue(cr.c10).accept(new ShowVisitor()));
  }

  @Test
  public void setCellDoubleEqualZero() {
    w.setCell(cr.c10, "=0");
    assertEquals("0.000000", w.getCellExpression(cr.c10).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.c10).accept(new ShowVisitor()));
  }

  @Test
  public void setCellDoubleEqualFive() {
    w.setCell(cr.c10, "=5");
    assertEquals("5.000000", w.getCellExpression(cr.c10).accept(new ShowVisitor()));
    assertEquals("5.000000", w.getCellValue(cr.c10).accept(new ShowVisitor()));
  }

  @Test
  public void setCellStringFoo() {
    w.setCell(cr.a1, "foo");
    assertEquals("\"foo\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"foo\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellStringBlerner() {
    w.setCell(cr.a1, "\"GET BLERNED\"");
    assertEquals("\"GET BLERNED\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"GET BLERNED\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellStringEqualFoo() {
    w.setCell(cr.a1, "=foo");
    assertEquals("\"foo\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"foo\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellStringEqualBlerner() {
    w.setCell(cr.a1, "=\"GET BLERNED\"");
    assertEquals("\"GET BLERNED\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"GET BLERNED\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellReferenceNotMade() {
    w.setCell(cr.a1, "=A2");
    assertEquals("A2", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellReferenceMade() {
    w.setCell(cr.a2, "1");
    w.setCell(cr.a1, "=(SUM A2 1)");
    assertEquals("(SUM A2 1.000000)", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("2.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellReferenceCycle() {
    w.setCell(cr.a2, "=(SUM A1 1)");
    w.setCell(cr.a1, "=(SUM A2 1)");
    w.getCellValue(new Coord(1,1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellChangeReferenceCycle() {
    w.setCell(cr.a1, "=1");
    w.setCell(cr.a2, "=(SUM A1 1)");
    w.setCell(cr.a3, "=(SUM A2 1)");
    w.setCell(cr.a1, "=(SUM A3 1)");
    w.getCellValue(new Coord(1, 1));
  }

  @Test
  public void setCellMultiReference() {
    w.setCell(cr.a1, "1");
    w.setCell(cr.b1, "1");
    w.setCell(cr.c1, "1");
    w.setCell(cr.a2, "=(SUM A1:C1)");
    assertEquals("(SUM A1:C1)", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("3.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void setCellSumOverNums() {
    w.setCell(cr.a1, "=(SUM 1.000000 2.000000 3.000000 4.000000 5.000000)");
    assertEquals("(SUM 1.000000 2.000000 3.000000 4.000000 5.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("15.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellSumOverNumsAndOther() {
    w.setCell(cr.a1, "=(SUM 1.000000 true 3.000000 \"foo\" 5.000000)");
    assertEquals("(SUM 1.000000 true 3.000000 \"foo\" 5.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("9.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellSumOverOther() {
    w.setCell(cr.a1, "=(SUM true \"bar\" \"3\")");
    assertEquals("(SUM true \"bar\" \"3\")",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellFunctionSumWithSelf() {
    w.setCell(cr.a1, "1");
    w.setCell(cr.a2, "=(SUM A1 A1)");
    assertEquals("(SUM A1 A1)",
            w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("2.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void setCellProductOverNums() {
    w.setCell(cr.a1, "=(PRODUCT 1.000000 2.000000 3.000000 4.000000 5.000000)");
    assertEquals("(PRODUCT 1.000000 2.000000 3.000000 4.000000 5.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("120.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellProductOverNumsAndOther() {
    w.setCell(cr.a1, "=(PRODUCT 1.000000 2.000000 \"foo\" 4.000000 false)");
    assertEquals("(PRODUCT 1.000000 2.000000 \"foo\" 4.000000 false)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("8.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellProductOverOther() {
    w.setCell(cr.a1, "=(PRODUCT \"foo\" false \"bar\" false)");
    assertEquals("(PRODUCT \"foo\" false \"bar\" false)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellLessThanTrue() {
    w.setCell(cr.a1, "=(< 1.000000 2.000000)");
    assertEquals("(< 1.000000 2.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("true", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellLessThanFalse() {
    w.setCell(cr.a1, "=(< 2.000000 1.000000)");
    assertEquals("(< 2.000000 1.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellLessThanEqual() {
    w.setCell(cr.a1, "=(< 2.000000 2.000000)");
    assertEquals("(< 2.000000 2.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellLessThanBadFirst() {
    w.setCell(cr.a1, "=(< true 2.000000)");
    w.getCellValue(cr.a1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellLessThanBadSecond() {
    w.setCell(cr.a1, "=(< 2.000000 \"false\")");
    w.getCellValue(cr.a1);
  }

  @Test
  public void setCellConcatOverStrings() {
    w.setCell(cr.a1, "=(CONCAT \"Who is Joe?\" \"It smells like updog in here.\" \"GET BLERNED\")");
    assertEquals("(CONCAT \"Who is Joe?\" \"It smells like updog in here.\" \"GET BLERNED\")",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"Who is Joe?It smells like updog in here.GET BLERNED\"",
            w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellConcatOverStringsAndOther() {
    w.setCell(cr.a1, "=(CONCAT \"Who is Joe?\" true \"GET BLERNED\" 5)");
    assertEquals("(CONCAT \"Who is Joe?\" true \"GET BLERNED\" 5.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"Who is Joe?trueGET BLERNED5.000000\"",
            w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellConcatOverOther() {
    w.setCell(cr.a1, "=(CONCAT false true 5 6)");
    assertEquals("(CONCAT false true 5.000000 6.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"falsetrue5.0000006.000000\"",
            w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellLessThanSumConcatProduct() {
    w.setCell(cr.a1, "=(< (SUM 1 2 (CONCAT \"foo\" 5)) (PRODUCT 2 3 4))");
    assertEquals("(< (SUM 1.000000 2.000000 (CONCAT \"foo\" 5.000000)) "
                    + "(PRODUCT 2.000000 3.000000 4.000000))",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("true", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellFunctionSingleReference() {
    w.setCell(cr.a2, "=(SUM 1 2 3)");
    assertEquals("(SUM 1.000000 2.000000 3.000000)",
            w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("6.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
    w.setCell(cr.a1, "=(PRODUCT A2 2 3 4)");
    assertEquals("(PRODUCT A2 2.000000 3.000000 4.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("144.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellFunctionMultiReference() {
    w.setCell(cr.a2, "=(SUM 1 2 3)");
    assertEquals("(SUM 1.000000 2.000000 3.000000)",
            w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("6.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
    w.setCell(cr.a3, "=5");
    assertEquals("5.000000",
            w.getCellExpression(cr.a3).accept(new ShowVisitor()));
    assertEquals("5.000000", w.getCellValue(cr.a3).accept(new ShowVisitor()));
    w.setCell(cr.b4, "=(SUM 3)");
    assertEquals("(SUM 3.000000)",
            w.getCellExpression(cr.b4).accept(new ShowVisitor()));
    assertEquals("3.000000", w.getCellValue(cr.b4).accept(new ShowVisitor()));
    w.setCell(cr.a1, "=(PRODUCT A2:B4 6)");
    assertEquals("(PRODUCT A2:B4 6.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("540.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
  }

  @Test
  public void setCellFunctionNoEquals() {
    w.setCell(cr.a2, "(SUM 1 2 3)");
    assertEquals("\"(SUM 1.000000 2.000000 3.000000)\"",
            w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("\"(SUM 1.000000 2.000000 3.000000)\"",
            w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellFakeFunction() {
    w.setCell(cr.a1, "=(DIVIDE 4 2)");
    w.getCellValue(cr.a1);
  }


  @Test
  public void setCellAgain() {
    w.setCell(cr.a1, "=(SUM 1 2 3 4 5)");
    assertEquals("15.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("(SUM 1.000000 2.000000 3.000000 4.000000 5.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    w.setCell(cr.a1, "foobar");
    assertEquals("\"foobar\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"foobar\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));

  }

  @Test(expected = IllegalArgumentException.class)
  public void setCellNullCoord() {
    w.setCell(null, "foo");
  }

  @Test
  public void setCellNullCell() {
    w.setCell(cr.a1, "foo");
    assertEquals("\"foo\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"foo\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    w.setCell(cr.a1, null);
    assertNull(w.getCellValue(cr.a1));
    assertNull(w.getCellExpression(cr.a1));

  }

  @Test
  public void getCellExpressionString() {
    w.setCell(cr.a1, "\"foo\"");
    w.setCell(cr.a2, "\"bar\"");
    assertEquals("\"foo\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"bar\"", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellExpressionDouble() {
    w.setCell(cr.a1, "0");
    w.setCell(cr.a2, "-5.0000004");
    w.setCell(cr.a3, "10.02");
    assertEquals("0.000000", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("-5.000000", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("10.020000", w.getCellExpression(cr.a3).accept(new ShowVisitor()));
  }

  @Test
  public void getCellExpressionBoolean() {
    w.setCell(cr.a1, "true");
    w.setCell(cr.a2, "false");
    assertEquals("true", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellExpressionBooleanNotLower() {
    w.setCell(cr.a1, "TrUe");
    w.setCell(cr.a2, "FALSE");
    assertEquals("\"TrUe\"", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"FALSE\"", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellExpressionReference() {
    w.setCell(cr.a1, "=B1");
    w.setCell(cr.a2, "=B2:B3");
    assertEquals("B1", w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("B2:B3", w.getCellExpression(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellExpressionFunction() {
    w.setCell(cr.a1, "=(SUM 1 2 3)");
    w.setCell(cr.a2, "=(PRODUCT 1 2 3)");
    w.setCell(cr.a3, "=(< 1 2)");
    w.setCell(cr.a4, "=(CONCAT \"foo\" \"bar\")");
    assertEquals("(SUM 1.000000 2.000000 3.000000)",
            w.getCellExpression(cr.a1).accept(new ShowVisitor()));
    assertEquals("(PRODUCT 1.000000 2.000000 3.000000)",
            w.getCellExpression(cr.a2).accept(new ShowVisitor()));
    assertEquals("(< 1.000000 2.000000)",
            w.getCellExpression(cr.a3).accept(new ShowVisitor()));
    assertEquals("(CONCAT \"foo\" \"bar\")",
            w.getCellExpression(cr.a4).accept(new ShowVisitor()));

  }

  @Test
  public void getCellValueString() {
    w.setCell(cr.a1, "\"foo\"");
    w.setCell(cr.a2, "\"bar\"");
    assertEquals("\"foo\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"bar\"", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellValueDouble() {
    w.setCell(cr.a1, "0");
    w.setCell(cr.a2, "-5.0000004");
    w.setCell(cr.a3, "10.02");
    assertEquals("0.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("-5.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
    assertEquals("10.020000", w.getCellValue(cr.a3).accept(new ShowVisitor()));
  }

  @Test
  public void getCellValueBoolean() {
    w.setCell(cr.a1, "true");
    w.setCell(cr.a2, "false");
    assertEquals("true", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("false", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellValueBooleanNotLower() {
    w.setCell(cr.a1, "TrUe");
    w.setCell(cr.a2, "FALSE");
    assertEquals("\"TrUe\"", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("\"FALSE\"", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test
  public void getCellValueReference() {
    w.setCell(cr.a1, "=B1");
    w.setCell(cr.a2, "=B2:B3");
    assertEquals("0.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("0.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCellValueBadReference() {
    w.setCell(cr.a2, "=C4:A1");
    w.getCellValue(cr.a2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCellValueBadReferenceRow() {
    w.setCell(cr.a2, "=D1:A1");
    w.getCellValue(cr.a2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void getCellValueBadReferenceCol() {
    w.setCell(cr.a2, "=A4:A1");
    w.getCellValue(cr.a2);
  }

  @Test
  public void getCellValueFunction() {
    w.setCell(cr.a1, "=(SUM 1 2 3)");
    w.setCell(cr.a2, "=(PRODUCT 1 2 3)");
    w.setCell(cr.a3, "=(< 1 2)");
    w.setCell(cr.a4, "=(CONCAT \"foo\" \"bar\")");
    assertEquals("6.000000", w.getCellValue(cr.a1).accept(new ShowVisitor()));
    assertEquals("6.000000", w.getCellValue(cr.a2).accept(new ShowVisitor()));
    assertEquals("true", w.getCellValue(cr.a3).accept(new ShowVisitor()));
    assertEquals("\"foobar\"", w.getCellValue(cr.a4).accept(new ShowVisitor()));
  }

}