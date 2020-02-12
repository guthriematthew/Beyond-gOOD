import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.cs3500.spreadsheets.cellstructure.ShowVisitor;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;

import static org.junit.Assert.assertEquals;

/**
 * Class to test file with many nested functions. Specifically the Fibonacci numbers.
 * Test will fail if configurations are set improperly.
 */
public class ExpensiveFileTest {

  // the working directory must be "Homework 6 for this test to run"
  @Test
  public void TestFile() {
    FileReader f;
    try {
      f = new FileReader("resources\\testExpensive.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }

    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    WorksheetModel w = WorksheetReader.read(builder, f);

    assertEquals("1134903170.000000",
            w.getCellValue(new Coord(1, 45)).accept(new ShowVisitor()));
    assertEquals("20365011074.000000",
            w.getCellValue(new Coord(1, 51)).accept(new ShowVisitor()));
    assertEquals("365435296162.000000",
            w.getCellValue(new Coord(1, 57)).accept(new ShowVisitor()));
  }
}
