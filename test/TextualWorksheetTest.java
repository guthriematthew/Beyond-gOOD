import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.view.TextualWorksheet;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 * A class to test the saving of files as TextualWorksheet views.
 */
public class TextualWorksheetTest {
  Worksheet w;
  ExampleCells cl;
  ExampleCoords cr;

  @Before
  public void initData() {
    cl = new ExampleCells();
    cr = new ExampleCoords();
    w = new Worksheet();
  }


  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullBoth() {
    TextualWorksheet t = new TextualWorksheet(null, null);
    try {
      t.render();
    } catch (IOException e) {
      fail("null field(s) accepted");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullWorksheet() {
    TextualWorksheet t = new TextualWorksheet(null, new StringBuilder());
    try {
      t.render();
    } catch (IOException e) {
      fail("null field(s) accepted");
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullAppendable() {
    TextualWorksheet t = new TextualWorksheet(this.w, null);
    try {
      t.render();
    } catch (IOException e) {
      fail("null field(s) accepted");
    }
  }

  @Test
  public void renderSumEquals() {
    w.setCell(cr.a1, "=(SUM 1 2 3)");
    StringBuilder actual = new StringBuilder();
    TextualWorksheet t = new TextualWorksheet(this.w, actual);
    try {
      t.render();
    } catch (IOException e) {
      fail("Append should succeed");
    }
    String expected = "A1 " +
            "=(SUM 1.000000 2.000000 3.000000)\n";
    assertEquals(expected, actual.toString());
  }

  @Test
  public void renderSumNoEquals() {
    w.setCell(cr.a1, "(SUM 1 2 3)");
    StringBuilder actual = new StringBuilder();
    TextualWorksheet t = new TextualWorksheet(this.w, actual);
    try {
      t.render();
    } catch (IOException e) {
      fail("Append should succeed");
    }
    String expected = "A1 " +
            "\"(SUM 1.000000 2.000000 3.000000)\"\n";
    assertEquals(expected, actual.toString());
  }

  @Test
  public void renderProductEquals() {
    w.setCell(cr.a1, "=(PRODUCT 1 2 3)");
    StringBuilder actual = new StringBuilder();
    TextualWorksheet t = new TextualWorksheet(this.w, actual);
    try {
      t.render();
    } catch (IOException e) {
      fail("Append should succeed");
    }
    String expected = "A1 " +
            "=(PRODUCT 1.000000 2.000000 3.000000)\n";
    assertEquals(expected, actual.toString());
  }

  @Test
  public void testToStringSingleLine() {
    w.setCell(cr.a1, "=(SUM 1 2 3)");
    StringBuilder actual = new StringBuilder();
    TextualWorksheet t = new TextualWorksheet(this.w, actual);
    assertEquals("A1 =(SUM 1.000000 2.000000 3.000000)\n", t.toString());
  }

  @Test
  public void testToStringMultipleLines() {
    w.setCell(cr.a1, "=(SUM 1 2 3)");
    w.setCell(cr.b3, "3");
    w.setCell(cr.c1, "=A1");
    StringBuilder actual = new StringBuilder();
    TextualWorksheet t = new TextualWorksheet(this.w, actual);
    assertEquals("A1 =(SUM 1.000000 2.000000 3.000000)\nB3 3.000000\nC1 =A1\n",
            t.toString());
  }

}