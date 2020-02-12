import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import edu.cs3500.spreadsheets.controller.GUIWorksheetController;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.EditableGUIView;
import edu.cs3500.spreadsheets.view.TextualWorksheet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * A Class to hold tests for the GUIWorksheetController class.
 */
public class GUIWorksheetControllerTest {

  GUIWorksheetController controller;
  EditableGUIView view;
  WorksheetModel model;
  KeyEvent delete;

  //Method copied from BeyondGood to use in testing.
  private static WorksheetModel openFileToWorksheet(String fileName) {
    FileReader f;
    try {
      f = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    return WorksheetReader.read(builder, f);
  }

  @Before
  public void initData() {
    this.model = new Worksheet();
    this.view = new EditableGUIView("tester", model);
    this.controller = new GUIWorksheetController(model, view);
    this.model.setCell(new Coord(1, 1), "=1");
    this.model.setCell(new Coord(1, 2), "=2");
    this.model.setCell(new Coord(2, 1), "=A1");
    this.delete = new KeyEvent(this.view, 0, 0, 0,
            KeyEvent.VK_DELETE, ' ');
  }

  @Test
  public void keyTyped() {
    this.controller.keyTyped(this.delete);
    assertEquals(this.model.getCellValue((new Coord(1, 1))).toString(),
            "1.000000");
    assertEquals(this.model.getCellExpression((new Coord(1, 1))).toString(),
            "1.000000");
  }

  @Test
  public void keyPressed() {
    this.controller.keyPressed(this.delete);
    assertEquals(this.model.getCellValue((new Coord(1, 1))).toString(),
            "1.000000");
    assertEquals(this.model.getCellExpression((new Coord(1, 1))).toString(),
            "1.000000");
  }

  @Test
  public void keyReleasedDelete() {
    this.controller.keyReleased(this.delete);
    assertEquals(this.model.getCellValue((new Coord(1, 1))),
            null);
    assertEquals(this.model.getCellExpression((new Coord(1, 1))),
            null);
  }

  @Test
  public void keyReleasedNotDelete() {
    KeyEvent k = new KeyEvent(this.view, 0, 0, 0, KeyEvent.VK_D, ' ');
    this.controller.keyReleased(k);
    assertEquals(this.model.getCellValue((new Coord(1, 1))).toString(),
            "1.000000");
    assertEquals(this.model.getCellExpression((new Coord(1, 1))).toString(),
            "1.000000");
  }

  @Test
  public void actionPerformedAccept() {
    this.view.render();
    Coord c = new Coord(1, 1);
    this.model.setCell(c, "=(SUM 1 2 3)");
    assertEquals(this.model.getCellExpression(c).toString(),
            "=(SUM 1.000000 2.000000 3.000000)");
    assertEquals(this.model.getCellValue(c).toString(), "6.000000");
    this.controller.actionPerformed(new ActionEvent(this, 0, "accept"));
    assertEquals(this.model.getCellExpression(c).toString(),
            "1.000000");
    assertEquals(this.model.getCellValue(c).toString(), "1.000000");
  }

  @Test
  public void actionPerformedReject() {
    this.view.render();
    Coord c = new Coord(1, 1);
    this.model.setCell(c, "=(SUM 1 2 3)");
    assertEquals(this.model.getCellExpression(c).toString(),
            "=(SUM 1.000000 2.000000 3.000000)");
    assertEquals(this.model.getCellValue(c).toString(), "6.000000");
    this.controller.actionPerformed(new ActionEvent(this, 0, "reject"));
    assertEquals(this.model.getCellExpression(c).toString(),
            "=(SUM 1.000000 2.000000 3.000000)");
    assertEquals(this.model.getCellValue(c).toString(), "6.000000");
    assertEquals(this.view.getEquationBarText(), "=(SUM 1.000000 2.000000 3.000000)");
  }

  @Test
  public void testBasicSave() throws IOException {
    FileWriter f = new FileWriter("resources\\testSuccessCopy.txt");
    TextualWorksheet t = new TextualWorksheet(model, f);
    t.render();
    WorksheetModel copy = this.openFileToWorksheet("resources\\testSuccessCopy.txt");
    List<Coord> keys = copy.getNonEmptyCells();
    boolean test = true;
    for (Coord key : keys) {
      test = test && (copy.getCellExpression(key) == model.getCellExpression(key))
              && (copy.getCellValue(key) == model.getCellValue(key));
    }
    assertTrue(test);
  }

  @Test
  public void mouseClicked() {
    assertEquals(this.view.getSelectedCell(), new Coord(1, 1));
    MouseEvent event = new MouseEvent(this.view, 0, 0, 0, 200, 75, 1, false);
    this.controller.mouseClicked(event);
    assertEquals(new Coord(1, 1), this.view.getSelectedCell());
  }

  @Test
  public void mousePressed() {
    assertEquals(this.view.getSelectedCell(), new Coord(1, 1));
    MouseEvent event = new MouseEvent(this.view, 0, 0, 0, 200, 75, 1, false);
    this.controller.mousePressed(event);
    assertEquals(new Coord(2, 1), this.view.getSelectedCell());
  }

  @Test
  public void mouseReleased() {
    assertEquals(this.view.getSelectedCell(), new Coord(1, 1));
    MouseEvent event = new MouseEvent(this.view, 0, 0, 0, 200, 75, 1, false);
    this.controller.mouseReleased(event);
    assertEquals(new Coord(1, 1), this.view.getSelectedCell());
  }

  @Test
  public void mouseEntered() {
    assertEquals(this.view.getSelectedCell(), new Coord(1, 1));
    MouseEvent event = new MouseEvent(this.view, 0, 0, 0, 200, 75, 1, false);
    this.controller.mouseEntered(event);
    assertEquals(new Coord(1, 1), this.view.getSelectedCell());
  }
}