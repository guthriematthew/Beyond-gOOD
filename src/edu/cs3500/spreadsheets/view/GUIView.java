package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SpringLayout;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * A basic implementation of a {@link GUIWorksheetView} using Swing. This view has a menu,
 * containing navigation box and a equation bar. The navigation box can be given a {@link Coord},
 * and the view will then move to have the cell at that Coord be in focus. The equation bar displays
 * the raw input of the selected cell, or displays nothing if the cell is blank. Cells can be
 * navigated using the navigation box or with the arrow keys. Cells cannot be edited in this
 * implementation.
 */
public class GUIView extends JFrame implements GUIWorksheetView {
  private ViewPanel view;

  /**
   * Creates a  JFrame with the given title.
   *
   * @param title the title of the JFrame.
   */
  public GUIView(String title, WorksheetReadOnlyModel m) {
    super(title);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setSize(1000, 700);
    this.setLayout(new SpringLayout());
    this.view = new ViewPanel(m, this);
    this.view.setVisible(true);
    this.add(view);
    this.setIconImage(new ImageIcon("resources\\excelIcon.png").getImage());
    this.setVisible(true);
  }

  @Override
  public void render() {
    this.setVisible(true);
  }

  @Override
  public void acceptKeyListener(KeyListener keyListener) {
    throw new IllegalStateException("This class does not implement this behavior.");
  }

  @Override
  public void acceptActionListener(ActionListener actionListener) {
    throw new IllegalStateException("This class does not implement this behavior.");
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
  public void openPopup() {
    throw new IllegalStateException("This class does not implement this behavior.");
  }

  @Override
  public void savePopup() {
    throw new IllegalStateException("This class does not implement this behavior.");
  }

  @Override
  public void acceptMouseListener(MouseListener mouseListener) {
    throw new IllegalStateException("This class does not implement this behavior.");
  }

  @Override
  public Coord getOffset() {
    return this.view.getOffset();
  }

  @Override
  public void shiftFocus(int x, int y) {
    throw new IllegalStateException("This class should not implement this behavior");
  }
}
