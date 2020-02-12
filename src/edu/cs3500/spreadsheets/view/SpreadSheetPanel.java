package edu.cs3500.spreadsheets.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A panel that is responsible for holding the spreadsheet.
 */
public class SpreadSheetPanel extends JPanel {
  private final ViewPanel parent;
  private FocusPanel focusPanel;

  /**
   * Creates a panel using the given layout.
   *
   * @param layout the LayoutManager for the desired layout.
   */
  SpreadSheetPanel(LayoutManager layout, ViewPanel parent) {
    super(layout);
    this.parent = parent;
    this.setPreferredSize(new Dimension(1000, 500));
    this.setBackground(Color.red);
    this.setFocusable(true);
    this.addKeyListener(new SpreadsheetKeyListener());
    this.focusPanel = new FocusPanel();
    this.add(this.focusPanel);
    this.setBackground(Color.RED);
    this.setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.requestFocus();
    Graphics2D graphics = (Graphics2D) g;
    Dimension bounds = parent.getSize();

    this.setBounds(0, bounds.height - Math.round(bounds.height * 0.8f), bounds.width,
            Math.round(bounds.height * 0.8f));
    this.focusPanel.setBounds(ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT, bounds.width,
            Math.round(bounds.height * 0.8f));

    this.setBackground(Color.white);

    int xRatio = Math.round((ViewPanel.CELL_WIDTH * 35f) / 80f);
    int yRatio = Math.round((ViewPanel.CELL_HEIGHT * 18f) / 30f);

    for (int y = 0; y < this.getHeight(); y += ViewPanel.CELL_HEIGHT) {
      if (y == 0) {
        //creates all column headers
        for (int x = 0; x < this.getWidth(); x += ViewPanel.CELL_WIDTH) {
          if (x != 0) {
            graphics.setClip(x, y, 2 * ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.drawString(
                    Coord.colIndexToName(this.parent.relativePos.col
                            + x / ViewPanel.CELL_WIDTH - 1),
                    x + 5,
                    y + yRatio);
            graphics.clipRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.drawRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.setClip(null);
          }
          graphics.drawRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
        }
      } else {
        for (int x = 0; x < this.getWidth(); x += ViewPanel.CELL_WIDTH) {
          //creates all row headers
          if (x == 0) {
            graphics.setClip(x, y, 2 * ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.drawString(
                    Integer.toString(this.parent.relativePos.row + y / ViewPanel.CELL_HEIGHT - 1),
                    x + 5,
                    y + yRatio);
            graphics.clipRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.drawRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
            graphics.setClip(null);
          }
          //draws text for cells, then clips text to the size of a cell to prevent overflow
          graphics.setClip(x, y, 2 * ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
          graphics.drawString(this.getCellContents(x / ViewPanel.CELL_WIDTH
                          + this.parent.relativePos.col,
                  y / ViewPanel.CELL_HEIGHT + this.parent.relativePos.row - 1),
                  x + ViewPanel.CELL_WIDTH + 5,
                  y + yRatio);
          graphics.clipRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
          graphics.drawRect(x, y, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
          graphics.setClip(null);
        }
      }
    }
  }

  private String getCellContents(int col, int row) {
    List<Coord> coords = this.parent.m.getNonEmptyCells();
    if (coords.contains(new Coord(col, row))) {
      try {
        return this.parent.m.getCellValue(new Coord(col, row)).toString();
      } catch (IllegalArgumentException e) {
        return "#error";
      }
    } else {
      return "";
    }
  }


  /**
   * An internal listener for the keyEvents in the spreadsheet panel. This changes the focused cell
   * based on key inputs from the arrow keys.
   */
  class SpreadsheetKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      // A KeyListener must implement all of these methods to be created.
      // We don't want there to be any response to this type of keyEvent.
    }

    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case (KeyEvent.VK_UP):
          shiftFocus(0, -1);
          break;
        case (KeyEvent.VK_DOWN):
          shiftFocus(0, 1);
          break;
        case (KeyEvent.VK_RIGHT):
          shiftFocus(1, 0);
          break;
        case (KeyEvent.VK_LEFT):
          shiftFocus(-1, 0);
          break;
        default:
          break;
      }
    }

    @Override
    public void keyReleased(KeyEvent e) {
      // A KeyListener must implement all of these methods to be created.
      // We don't want there to be any response to this type of keyEvent.
    }
  }

  /**
   * Moves the focus based on the current focus and the given amount to shift the focus.
   *
   * @param x The amount to shift in the x direction. Negative goes left, positive goes right.
   * @param y The amount to shift in the y direction. Negative goes up, positive goes down.
   */
  private void shiftFocus(int x, int y) {
    int a = this.parent.relativePos.col;
    int b = this.parent.relativePos.row;

    int newFocusX = (this.parent.focusX + x);
    int newFocusY = (this.parent.focusY + y);

    int sheetWidth = this.getWidth() - ViewPanel.CELL_WIDTH;
    int sheetHeight = this.getHeight() - ViewPanel.CELL_HEIGHT;

    int topLeftCol = this.parent.relativePos.col;
    int topLeftRow = this.parent.relativePos.row;

    if (newFocusX * ViewPanel.CELL_WIDTH > sheetWidth) { //move right off screen
      this.parent.relativePos = new Coord(a + 1, b);
    } else if (newFocusX > 0) { //move left or right on screen
      this.parent.focusX = newFocusX;
    } else if (topLeftCol != 1) { // move left off screen, unless you are in column A
      this.parent.relativePos = new Coord(topLeftCol - 1, topLeftRow);
    }

    if ((newFocusY + 1) * ViewPanel.CELL_HEIGHT > sheetHeight) { //move down off screen
      this.parent.relativePos = new Coord(a, b + 1);
    } else if (newFocusY > 0) { //move up or down in screen
      this.parent.focusY = newFocusY;
    } else if (topLeftRow != 1) { //move up off screen, unless you are in first row
      this.parent.relativePos = new Coord(topLeftCol, topLeftRow - 1);
    }

    this.focusPanel.revalidate();
    this.parent.updateMenu();
  }

  /**
   * A class to handle moving the focus box over the spreadsheet.
   */
  private class FocusPanel extends JPanel {
    private FocusPanel() {
      this.setOpaque(true);
      this.setBackground(Color.red);
      this.setSize(SpreadSheetPanel.this.getSize());
    }


    @Override
    public void paintComponent(Graphics g) {
      Graphics2D graphics = (Graphics2D) g;
      graphics.setStroke(new BasicStroke(2.2f));
      graphics.drawRect(SpreadSheetPanel.this.parent.focusX * ViewPanel.CELL_WIDTH
                      - ViewPanel.CELL_WIDTH,
              SpreadSheetPanel.this.parent.focusY * ViewPanel.CELL_HEIGHT
                      - ViewPanel.CELL_HEIGHT, ViewPanel.CELL_WIDTH, ViewPanel.CELL_HEIGHT);
    }

  }

}
