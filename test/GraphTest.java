import org.jfree.data.general.DefaultPieDataset;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import edu.cs3500.spreadsheets.BeyondGood;
import edu.cs3500.spreadsheets.model.Chart;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.Worksheet;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.EditableGUIView;
import edu.cs3500.spreadsheets.view.GraphingEditableGUIView;

import static junit.framework.TestCase.assertEquals;

/**
 * A class to test the behaviors of charts and their properties including saving and opening.
 */
public class GraphTest {
  private Chart chart;
  private DefaultPieDataset data;
  private Coord s1;
  private Coord s2;
  private HashSet<Coord> dep;
  private int len;

  private WorksheetModel model;
  private GraphingEditableGUIView graphView;

  @Before
  public void setup() {
    s1 = new Coord(1, 1);
    s2 = new Coord(2, 1);
    len = 3;
    dep = new HashSet<>();
    data = new DefaultPieDataset();
    model = new Worksheet();
    EditableGUIView view = new EditableGUIView("Basic View", model);
    graphView = new GraphingEditableGUIView(view, model);
  }

  @Test
  public void testGetTitle() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 0);
    assertEquals("testTitle", chart.getTitle());
  }

  @Test
  public void testGetData() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 0);
    assertEquals(new DefaultPieDataset(), chart.getData());
  }

  @Test
  public void testGetPosition() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 0);
    assertEquals(0, chart.getPosition());
  }

  @Test
  public void testGetPositionAfterUpdate() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 2);
    assertEquals(2, chart.getPosition());
    chart.shiftPosition();
    assertEquals(1, chart.getPosition());
  }

  @Test
  public void testToString() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 0);
    assertEquals("A1 B1 3 testTitle", chart.toString());
  }

  @Test
  public void testModelStoresGraphs() {
    assertEquals(new ArrayList<Chart>(), model.getCharts());
  }

  @Test
  public void testModelAddsGraphs() {
    chart = new Chart(s1, s2, len, data, dep, "testTitle", 2);
    ArrayList<Chart> list = new ArrayList<>();
    list.add(chart);
    model.addChart(chart);
    assertEquals(list, model.getCharts());
  }

  @Test
  public void testModelUpdatesDynamically() {
    dep = new HashSet<>();
    dep.add(new Coord(1, 1));
    chart = new Chart(s1, s2, 1, data, dep, "testTitle", 2);
    ArrayList<Chart> list = new ArrayList<>();
    list.add(chart);
    model.addChart(chart);
    model.setCell(new Coord(1, 1), "hi");
    model.setCell(new Coord(2, 1), "=3");
    assertEquals(list, model.getCharts());
    model.setCell(new Coord(2, 1), "=5");
    try {
      graphView.render();
    } catch (IOException e) {
      //there should be no error here
    }
    assertEquals(list, model.getCharts());
  }

  @Test
  public void testGetFileExtension() {
    String fileName = "file.txt";
    assertEquals("file.ch", BeyondGood.fileExtension(fileName));
  }

  // the working directory must be "Homework" 9 for this test to run
  @Test
  public void TestFileSavedCanBeOpened() {
    FileReader f;
    try {
      f = new FileReader("resources\\testGraphing.ch");
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    Scanner scanner = new Scanner(f);

    assertEquals(scanner.next(), "F2");
    assertEquals(scanner.next(), "G2");
    assertEquals(scanner.next(), "3");
    assertEquals(scanner.next(), "FruitPopularity");
    assertEquals(scanner.next(), "A1");
    assertEquals(scanner.next(), "B1");
    assertEquals(scanner.next(), "1");
    assertEquals(scanner.next(), "Other");
  }
}
