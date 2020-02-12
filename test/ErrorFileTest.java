import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Class to test file with errors to test how the code responds.
 */
public class ErrorFileTest {

  //This test will fail if the configuration is not properly set up for the given file.
  @Test
  public void TestFileError1() {
    FileReader f;
    try {
      f = new FileReader("resources\\testErrors.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();

    WorksheetModel w = WorksheetReader.read(builder, f);
    try {
      w.getCellValue(new Coord(1, 9));
      fail("Value should not be reachable");
    } catch (IllegalArgumentException e) {
      try {
        w.getCellExpression(new Coord(1, 9));
      } catch (IllegalArgumentException g) {
        assertEquals(g.getMessage(), "=A9");
      }
    }
  }

  //This test will fail if the configuration is not properly set up for the given file.
  @Test
  public void TestFileError2() {
    FileReader f;
    try {
      f = new FileReader("resources\\testErrors.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();

    WorksheetModel w = WorksheetReader.read(builder, f);
    try {
      w.getCellValue(new Coord(1, 6));
      fail("Value should not be reachable");
    } catch (IllegalArgumentException e) {
      try {
        w.getCellExpression(new Coord(1, 6));
      } catch (IllegalArgumentException g) {
        assertEquals(g.getMessage(), "=(< 3 4 5)");
      }
    }
  }

  //This test will fail if the configuration is not properly set up for the given file.
  @Test
  public void TestFileError3() {
    FileReader f;
    try {
      f = new FileReader("resources\\testErrors.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();

    WorksheetModel w = WorksheetReader.read(builder, f);
    try {
      w.getCellValue(new Coord(1, 10));
      fail("Value should not be reachable");
    } catch (IllegalArgumentException e) {
      try {
        w.getCellExpression(new Coord(1, 10));
      } catch (IllegalArgumentException g) {
        assertEquals(g.getMessage(), "=(< true A1)");
      }
    }
  }

  //This test will fail if the configuration is not properly set up for the given file.
  @Test
  public void TestFileError4() {
    FileReader f;
    try {
      f = new FileReader("resources\\testErrors.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();

    WorksheetModel w = WorksheetReader.read(builder, f);
    try {
      w.getCellValue(new Coord(1, 13));
      fail("Value should not be reachable");
    } catch (IllegalArgumentException e) {
      try {
        w.getCellExpression(new Coord(1, 13));
      } catch (IllegalArgumentException g) {
        assertEquals(g.getMessage(), "=A11");
      }
    }
  }
}