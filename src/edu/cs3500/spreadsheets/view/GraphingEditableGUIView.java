package edu.cs3500.spreadsheets.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.cs3500.spreadsheets.model.Chart;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * An editable gui view that offers all functionality of a regular editable gui view, but
 * additionally offers graphing.
 */
public class GraphingEditableGUIView implements GUIWorksheetView {
  private EditableGUIView view;
  private JMenuItem makeNewGraph;
  private WorksheetReadOnlyModel model;

  /**
   * A constructor that extends the menu of the composed view.
   *
   * @param view the view that this graphing class decorates.
   */
  public GraphingEditableGUIView(EditableGUIView view, WorksheetReadOnlyModel model) {
    if (view == null || model == null) {
      throw new IllegalArgumentException();
    }

    this.view = view;
    this.model = model;
    JMenu graphMenu = new JMenu("Graphs");

    this.makeNewGraph = new JMenuItem("New Graph");
    this.makeNewGraph.setActionCommand("New Graph");
    graphMenu.add(this.makeNewGraph);

    this.view.getJMenuBar().add(graphMenu);

    for (Chart c : model.getCharts()) {
      JFreeChart data = ChartFactory.createPieChart(c.getTitle(), c.getData(),
              true, true, false);
      ChartPanel chart = new ChartPanel(data);
      JFrame chartFrame = new JFrame();
      addCloseBehavior(chartFrame, c);
      chartFrame.setSize(400, 300);
      chartFrame.add(chart);
      chartFrame.setVisible(true);
    }
  }

  @Override
  public void acceptKeyListener(KeyListener keyListener) throws IllegalStateException {
    this.view.acceptKeyListener(keyListener);
  }

  @Override
  public void acceptActionListener(ActionListener actionListener) throws IllegalStateException {
    this.view.acceptActionListener(actionListener);
    this.makeNewGraph.addActionListener(e -> {
      if (e.getActionCommand().equals("New Graph")) {
        try {
          this.makeNewGraphPrompt();
        } catch (IllegalArgumentException e1) {
          makeErrorBox();
        }
      }
    });
  }

  @Override
  public Coord getSelectedCell() {
    return this.view.getSelectedCell();
  }

  @Override
  public String getEquationBarText() {
    return this.view.getEquationBarText();
  }

  @Override
  public void openPopup() throws IllegalStateException {
    this.view.openPopup();
  }

  @Override
  public void savePopup() throws IllegalStateException {
    this.view.savePopup();
  }

  @Override
  public void acceptMouseListener(MouseListener mouseListener) throws IllegalStateException {
    this.view.acceptMouseListener(mouseListener);
  }

  @Override
  public Coord getOffset() {
    return this.view.getOffset();
  }

  @Override
  public void shiftFocus(int x, int y) {
    this.view.shiftFocus(x, y);
  }

  @Override
  public void render() throws IOException {
    this.updateCharts(this.view.getSelectedCell());
    this.view.render();
  }

  /**
   * Creates a graph using the given ranges.
   *
   * @param s1 the first starting coordinate.
   * @param e1 the first ending coordinate.
   * @param s2 the second starting coordinate.
   */
  void makeGraph(Coord s1, Coord e1, Coord s2, String title) {
    DefaultPieDataset pieChartData = new DefaultPieDataset();
    HashSet<Coord> tempDep = new HashSet<>();
    int length = e1.row - s1.row + 1;
    Chart tempChart = new Chart(s1, s2, length, pieChartData, tempDep, title,
            model.getCharts().size());
    model.getCharts().add(tempChart);

    for (int i = 0; i < length; i++) {
      Coord c1 = new Coord(s1.col, s1.row + i);
      Coord c2 = new Coord(s2.col, s2.row + i);
      try {
        String name = model.getCellValue(c1).toString();
        double value = Double.parseDouble(model.getCellValue(c2).toString());
        pieChartData.setValue(name, value);
        tempDep.add(c1);
        tempDep.add(c2);
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("No data at this position");
      }
    }

    JFreeChart data = ChartFactory.createPieChart(title, pieChartData, true, true,
            false);
    ChartPanel chart = new ChartPanel(data);
    JFrame chartFrame = new JFrame();
    addCloseBehavior(chartFrame, tempChart);
    chartFrame.setSize(400, 300);
    chartFrame.add(chart);
    chartFrame.setVisible(true);
  }

  private void makeNewGraphPrompt() {
    new GraphPopup(this.view, this);
  }

  // updates the charts housed by this worksheet.
  private void updateCharts(Coord coord) {
    for (int i = 0; i < model.getCharts().size(); i++) {
      try {
        model.getCharts().get(i).update(this.model, coord);
      } catch (IllegalStateException e) {
        // Do nothing in this case as once a graph is made it is acceptable to modify its dependent
        // cells.
      }
    }
  }

  // Opens an error box if the graph cannot be made encounter an error.
  private void makeErrorBox() {
    JDialog errorPopup = new JDialog(this.view, true);
    errorPopup.setSize(250, 50);
    errorPopup.add(new JLabel("These coordinates do not house valid values"));
    errorPopup.setResizable(false);
    errorPopup.setVisible(true);
  }

  private void addCloseBehavior(JFrame frame, Chart c) {
    frame.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }

      @Override
      public void windowClosing(WindowEvent e) {
        removeChartElement(c);
      }

      @Override
      public void windowClosed(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }

      @Override
      public void windowIconified(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }

      @Override
      public void windowDeiconified(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }

      @Override
      public void windowActivated(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }

      @Override
      public void windowDeactivated(WindowEvent e) {
        // this method is not needed for the intended behavior
        // it is deprecated as such
      }
    });
  }

  // removes the given chart from the list
  private void removeChartElement(Chart c) {
    for (Chart chart : this.model.getCharts()) {
      if (chart.getPosition() > c.getPosition()) {
        chart.shiftPosition();
      }
    }
    this.model.getCharts().remove(c.getPosition());
  }
}
