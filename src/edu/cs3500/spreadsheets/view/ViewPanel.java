package edu.cs3500.spreadsheets.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.cs3500.spreadsheets.BeyondGood;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

import static edu.cs3500.spreadsheets.BeyondGood.fileExtension;

/**
 * A panel that holds the components of a GUIWorksheetView. This panel is designed to be held by a
 * JFrame, which interacts with a controller and delegates to this panel. A GUIView or EditableGUI
 * can hold this panel, but it is the job of the JFrame to either allow editing or suppress that
 * functionality.
 */
public class ViewPanel extends JPanel {
  public static final int CELL_WIDTH = 80;
  public static final int CELL_HEIGHT = 30;
  //Ideal size for cells are 80x30. Cells smaller than this are not guaranteed to work properly.
  WorksheetReadOnlyModel m;
  // These ints are the single point of truth for focus across all GUIView components.
  int focusX;
  int focusY;
  Coord relativePos;
  private MenuPanel menu;
  private SpreadSheetPanel sheet;
  private JFrame parent;
  private int lastWidth;
  private int lastHeight;

  /**
   * A basic constructor for a ViewPanel.
   *
   * @param m      The Worksheet that is held by the Controller. This ViewPanel will have read-only
   *               access.
   * @param parent The JFrame that is holding this panel.
   */
  ViewPanel(WorksheetReadOnlyModel m, JFrame parent) {
    this.relativePos = new Coord(1, 1);
    this.focusX = 1;
    this.focusY = 1;
    this.parent = parent;
    this.m = m;
    BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
    this.setLayout(layout);

    this.sheet = new SpreadSheetPanel(new SpringLayout(), this);
    this.add(sheet);
    this.menu = new MenuPanel(new SpringLayout(), this);
    this.add(menu);
    this.setVisible(true);
    this.menu.setVisible(true);
    this.sheet.setVisible(true);
    lastWidth = this.getWidth();
    lastHeight = this.getHeight();
    this.updateMenu();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int width = this.parent.getWidth();
    int height = this.parent.getHeight();
    int menuHeight = Math.toIntExact(Math.round(height * 2.0 / 7.0));
    int sheetHeight = height - menuHeight;
    Dimension menuSize = new Dimension(width, menuHeight);
    Dimension sheetSize = new Dimension(width, sheetHeight);
    this.menu.setPreferredSize(menuSize);
    this.sheet.setPreferredSize(sheetSize);
    this.menu.paintComponent(g);
    this.sheet.paintComponent(g);
    if (lastHeight != height || lastWidth != width) {
      this.revalidate();
      lastWidth = width;
      lastHeight = height;
    }
  }


  /**
   * Updates the MenuPanel information to be correct based on the given cell in focus in the
   * SpreadSheetPanel. Updates the navBox to be the Coord of the cell that is in focus, and the
   * equationBar to contain the contents of the cell in focus, or the empty String if the cell in
   * focus is empty.
   */
  void updateMenu() {
    Coord cellInFocus = new Coord(relativePos.col + focusX - 1,
            relativePos.row + focusY - 1);

    menu.updateNavBox(cellInFocus.toString());

    if (m.getNonEmptyCells().contains(cellInFocus)) {
      try {
        menu.updateEquationBar(m.getCellExpression(cellInFocus).toString());
      } catch (IllegalArgumentException e) {
        menu.updateEquationBar(e.getMessage());
      }
    } else {
      menu.updateEquationBar("");
    }
  }

  /**
   * Updates the SpreadSheetPanel to have the top-left cell be at the given Coord and in focus. Used
   * by the MenuPanel to change scroll using the NavBox.
   *
   * @param c The Coord that will be in the top left corner.
   */
  void updateSpreadsheetScroll(Coord c) {
    relativePos = c;
    this.sheet.repaint();
  }

  //Calculates the offset of the spreadsheet relative to the JFrame. Takes into account the size
  //of the MenuPanel and row and column headers.
  Coord getOffset() {
    int offsetHeight = this.menu.getHeight() + CELL_HEIGHT;
    int offsetWidth = CELL_WIDTH;
    return new Coord(offsetWidth, offsetHeight);
  }

  //Changes the focus to the cell at the given x and y
  void shiftFocus(int x, int y) {
    this.focusX = x;
    this.focusY = y;
    this.sheet.revalidate();
    this.updateMenu();
  }

  //Returns the equation bar text for use in the Controller. The returned String is immutable.
  String getEquationBarText() {
    return menu.equationBarText();
  }

  //Adds a KeyListener to the SpreadSheetPanel to listen for the delete key being pressed.
  void acceptKeyListener(KeyListener keyListener) {
    this.sheet.addKeyListener(keyListener);
  }

  //Adds the accept and reject buttons for edit-ability to the MenuBar.
  void addEquationBarButtons(JButton acceptButton, JButton rejectButton) {
    this.menu.add(acceptButton);
    this.menu.add(rejectButton);
    // This is being cast because it is an invariant that a MenuPanel will use a SpringLayout
    SpringLayout layout = (SpringLayout) this.menu.getLayout();
    layout.putConstraint(SpringLayout.NORTH, acceptButton, 40,
            SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.NORTH, rejectButton, 40,
            SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, rejectButton, acceptButton.getWidth() + 10,
            SpringLayout.EAST, acceptButton);
    layout.putConstraint(SpringLayout.WEST, acceptButton, rejectButton.getWidth() + 10,
            SpringLayout.WEST, this);
  }

  //Adds a JMenuBar to the MenuPanel. Used for adding the File system.
  void addMenuBar(JMenuBar menuBar) {
    this.menu.add(menuBar);
  }

  //Returns the cell that is currently selected.
  Coord getSelectedCell() {
    int col = this.relativePos.col + this.focusX - 1;
    int row = this.relativePos.row + this.focusY - 1;
    return new Coord(col, row);
  }

  //Opens the popup window for saving a file.
  void savePopup() {
    JFileChooser fileSystem = new JFileChooser(".\\resources");
    int option = fileSystem.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      try {
        FileWriter file = new FileWriter(fileSystem.getSelectedFile());
        TextualWorksheet saveView = new TextualWorksheet(this.m, file);
        saveView.render();
        saveGraphs(this.m, fileSystem.getSelectedFile().getName());
        file.close();
      } catch (IOException e) {
        makeErrorBox();
      }
    }
  }

  //Opens the popup window for opening a file.
  void openPopup() {
    JFileChooser fileSystem = new JFileChooser(".\\resources");
    int option = fileSystem.showOpenDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      String[] args = new String[3];
      args[0] = "-in";
      args[1] = "resources/" + fileSystem.getSelectedFile().getName();
      args[2] = "-edit";
      try {
        BeyondGood.main(args);
        this.parent.dispose();
      } catch (IllegalArgumentException | IllegalStateException e) {
        makeErrorBox();
      }
    }
  }

  // Opens an error box if either openPopup() or savePopup() encounter an error.
  private void makeErrorBox() {
    JDialog errorPopup = new JDialog(this.parent, true);
    errorPopup.setSize(250, 50);
    errorPopup.add(new JLabel(" File could not be found or was unreadable. "));
    errorPopup.setResizable(false);
    errorPopup.setVisible(true);
  }

  private void saveGraphs(WorksheetReadOnlyModel model, String fileName) {
    String modifiedName = fileExtension(fileName);
    FileWriter f;
    PrintWriter p;
    try {
      f = new FileWriter("resources//" + modifiedName);
      p = new PrintWriter(f);
      for (int i = 0; i < model.getCharts().size(); i++) {
        p.print(model.getCharts().get(i).toString());
        if (i < model.getCharts().size() - 1) {
          p.print(" ");
        }
      }
      p.close();
      f.close();
    } catch (IOException e) {
      // The given file could not be saved but there is no need the throw an error here
    }
  }
}
