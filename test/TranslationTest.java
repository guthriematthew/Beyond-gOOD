import org.junit.Test;

import java.io.StringReader;

import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * A class that test the translation of Strings to Sexps, and finally to Cell types. Many tests are
 * many using a ShowVisitor. This was done to both make expected output easier to create and to have
 * the expected output be more readable, thus ensuring the correct expected output is being
 * returned.
 */
public class TranslationTest {

  private WorksheetModel w;

  @Test
  public void testNumberBasic() {
    StringReader fileMock = new StringReader("A1 3");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("3.000000",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testNumberWithEquals() {
    StringReader fileMock = new StringReader("A1 =3");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("3.000000",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testNumberAsString() {
    StringReader fileMock = new StringReader("A2 \"3\"");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("\"3\"",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("\"3\"",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testStringBasic() {
    StringReader fileMock = new StringReader("B1 \"hello\"");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("\"hello\"",
            w.getCellExpression(new Coord(2, 1)).accept(new ShowVisitor()));
    assertEquals("\"hello\"",
            w.getCellValue(new Coord(2, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testStringBasic2() {
    StringReader fileMock = new StringReader("B1 \"hello\"");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("\"hello\"",
            w.getCellExpression(new Coord(2, 1)).accept(new ShowVisitor()));
    assertEquals("\"hello\"",
            w.getCellValue(new Coord(2, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testStringWithEquals() {
    StringReader fileMock = new StringReader("B1 =\"hello\"");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("\"hello\"",
            w.getCellExpression(new Coord(2, 1)).accept(new ShowVisitor()));
    assertEquals("\"hello\"",
            w.getCellValue(new Coord(2, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testBooleanTrue() {
    StringReader fileMock = new StringReader("A2 true");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("true",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("true",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testBooleanTrueWithEquals() {
    StringReader fileMock = new StringReader("A2 =true");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("true",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("true",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testBooleanFalse() {
    StringReader fileMock = new StringReader("A2 false");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("false",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testBooleanFalseWithEquals() {
    StringReader fileMock = new StringReader("A2 =false");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("false",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunction() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 4)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM 3.000000 4.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("7.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunctionSingleArg() {
    StringReader fileMock = new StringReader("A1 =(SUM 3)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM 3.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunctionNoArg() {
    StringReader fileMock = new StringReader("A1 =(SUM)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunctionWithString() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 \"word\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM 3.000000 \"word\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunctionWithBoolean() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 true)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM 3.000000 true)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testSumFunctionWithSingleReference() {
    StringReader fileMock = new StringReader("A1 15\nA2 =(SUM 3 A1)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(SUM 3.000000 A1)",
            w.getCellExpression(new Coord(1, 2)).accept(new ShowVisitor()));
    assertEquals("18.000000",
            w.getCellValue(new Coord(1, 2)).accept(new ShowVisitor()));
  }

  @Test
  public void testProductFunctionSomeNonNumeric() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT 3 \"word\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT 3.000000 \"word\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("3.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testProductFunction() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT 3 4)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT 3.000000 4.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("12.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testProductFunctionNoNumeric() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT \"s\" \"4\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT \"s\" \"4\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("0.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testProductFunctionWithSingleReference() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT \"s\" 4 A5)\nA2 =3\nA5 =A2");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT \"s\" 4.000000 A5)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("12.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testNestedFunctions() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT (SUM 2 1) (SUM 1 3))");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT (SUM 2.000000 1.000000) (SUM 1.000000 3.000000))",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("12.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanBasicTrue() {
    StringReader fileMock = new StringReader("A1 =(< 3 4)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 3.000000 4.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("true",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanBasicFalse() {
    StringReader fileMock = new StringReader("A1 =(< 4 3)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 4.000000 3.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanWithSingleReferenceTrue() {
    StringReader fileMock = new StringReader("A1 =(< 3 A2)\nA2 5");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 3.000000 A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("true",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanWithSingleReferenceFalse() {
    StringReader fileMock = new StringReader("A1 =(< 3 A2)\nA2 1");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 3.000000 A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanWithSingleReferenceChange() {
    StringReader fileMock = new StringReader("A1 =(< 3 A2)\nA2 5");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 3.000000 A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("true",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
    w.setCell(new Coord(1, 2), "1");
    assertEquals("(< 3.000000 A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testLessThanStrictInequality() {
    StringReader fileMock = new StringReader("A1 =(< 3 3)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(< 3.000000 3.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("false",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanTooFewArgs() {
    StringReader fileMock = new StringReader("A1 =(< 4)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(1, 1)));
    assertNull(w.getCellValue(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanTooManyArgs() {
    StringReader fileMock = new StringReader("A1 =(< 2 3 4)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(1, 1)));
    assertNull(w.getCellValue(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanWithString() {
    StringReader fileMock = new StringReader("A1 =(< 4 \"word\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(1, 1)));
    assertNull(w.getCellValue(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLessThanWithBoolean() {
    StringReader fileMock = new StringReader("A1 =(< 4 true)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(1, 1)));
    assertNull(w.getCellValue(new Coord(1, 1)));
  }

  @Test
  public void testConcatBasic() {
    StringReader fileMock = new StringReader("A1 =(CONCAT \"foo\" \"bar\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" \"bar\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foobar\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatWithReference() {
    StringReader fileMock = new StringReader("A1 =(CONCAT foo A2)\nA2 bar");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foobar\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatWithReferenceChange() {
    StringReader fileMock = new StringReader("A1 =(CONCAT foo A2)\nA2 bar");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foobar\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
    w.setCell(new Coord(1, 2), "foo");
    assertEquals("(CONCAT \"foo\" A2)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foofoo\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatSingleArg() {
    StringReader fileMock = new StringReader("A1 =(CONCAT \"foo\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foo\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatNArgs() {
    StringReader fileMock = new StringReader("A1 =(CONCAT \"foo\" \"bar\" \"s\")");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" \"bar\" \"s\")",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foobars\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatWithNumber() {
    StringReader fileMock = new StringReader("A1 =(CONCAT \"foo\" 5)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" 5.000000)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foo5.000000\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testConcatWithBoolean() {
    StringReader fileMock = new StringReader("A1 =(CONCAT \"foo\" false)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertEquals("(CONCAT \"foo\" false)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("\"foofalse\"",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test
  public void testComplexNestedFunctions() {
    StringReader fileMock = new StringReader("A1 =(PRODUCT (SUM 2 A3) (SUM 1 3) A5)"
            + "\n A3 =(SUM 2 3)\nA4 5\nA5 =A4");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);

    assertEquals("(PRODUCT (SUM 2.000000 A3) (SUM 1.000000 3.000000) A5)",
            w.getCellExpression(new Coord(1, 1)).accept(new ShowVisitor()));
    assertEquals("140.000000",
            w.getCellValue(new Coord(1, 1)).accept(new ShowVisitor()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDetectDirectCycle() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 A1)\n");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(1, 1)));
    assertNull(w.getCellValue(new Coord(1, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDetectIndirectCycle() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 B1)\nB1 =A1");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(2, 1)));
    assertNull(w.getCellValue(new Coord(2, 1)));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDetectIndirectCycleStepRemoved() {
    StringReader fileMock = new StringReader("A1 =(SUM 3 B1)\nB1 =C3\nC3 =(SUM 3 A1)");
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    w = WorksheetReader.read(builder, fileMock);
    assertNull(w.getCellExpression(new Coord(3, 3)));
    assertNull(w.getCellValue(new Coord(3, 3)));
  }
}
