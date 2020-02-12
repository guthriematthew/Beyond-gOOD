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
 * Class to test file with many examples of valid inputs.
 */
public class BasicTextFileTest {

  // the working directory must be "Homework 6 for this test to run"
  @Test
  public void TestFile() {
    FileReader f;
    try {
      f = new FileReader("resources\\testSuccess.txt");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }

    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    WorksheetModel w = WorksheetReader.read(builder, f);

    assertEquals("\"askfjsaffk\"",
            w.getCellValue(new Coord(1, 3)).accept(new ShowVisitor()));
    assertEquals("0.000000",
            w.getCellValue(new Coord(2, 3)).accept(new ShowVisitor()));
    assertEquals("5.000000",
            w.getCellValue(new Coord(2, 5)).accept(new ShowVisitor()));
    assertEquals("18.000000",
            w.getCellValue(new Coord(705, 1110)).accept(new ShowVisitor()));
  }
}
