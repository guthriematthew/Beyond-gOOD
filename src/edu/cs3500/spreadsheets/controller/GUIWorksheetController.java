package edu.cs3500.spreadsheets.controller;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.view.EditableGUIView;
import edu.cs3500.spreadsheets.view.GUIWorksheetView;
import edu.cs3500.spreadsheets.view.ViewPanel;

/**
 * An implementation of a {@link WorksheetController}. Controls the interaction between a {@link
 * edu.cs3500.spreadsheets.model.WorksheetModel} and an {@link EditableGUIView}. All actions
 * supported in an {@link EditableGUIView} are supported here. Certain methods are implemented but
 * have no side effects, as they have not been implemented. However, every possible action in a
 * {@link EditableGUIView} is supported by one of the methods at least. See method documentation for
 * more information.
 */
public class GUIWorksheetController implements WorksheetController {
  private WorksheetModel w;
  private GUIWorksheetView v;

  /**
   * Constructs a new controller the interfaces between the given model and EditableGUIView.
   *
   * @param model the given model.
   * @param view  the given view.
   */
  public GUIWorksheetController(WorksheetModel model, GUIWorksheetView view) {
    this.w = model;
    this.v = view;
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given KeyEvent
   */
  public void keyTyped(KeyEvent e) {
    // this method does nothing it must be overridden to implement the interface
    // but the functionality is not needed
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given KeyEvent
   */
  public void keyPressed(KeyEvent e) {
    // this method does nothing it must be overridden to implement the interface
    // but the functionality is not needed
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if (e.getKeyCode() == KeyEvent.VK_DELETE) {
      this.w.setCell(v.getSelectedCell(), null);
      try {
        this.v.render();
      } catch (IOException e1) {
        throw new IllegalStateException("Could not render the view.");
      }
    }
  }


  @Override
  public void actionPerformed(ActionEvent e) {
    try {
      switch (e.getActionCommand()) {
        case ("accept"):
          if (v.getEquationBarText().equals("")) {
            w.setCell(v.getSelectedCell(), null);
          } else {
            w.setCell(v.getSelectedCell(), v.getEquationBarText());
          }
          v.render();
          break;
        case ("reject"):
          v.render();
          break;
        case ("Save As"):
          this.v.savePopup();
          break;
        case ("Open"):
          this.v.openPopup();
          break;
        case ("Exit"):
          System.exit(0);
          break;
        default:
          throw new IllegalArgumentException("Unexpected action was detected");
      }
    } catch (IOException e1) {
      throw new IllegalStateException("The worksheet could not be read");
    }
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    // This method is not used because we only need to provide
    // the functionality of a mouse press.
  }

  @Override
  public void mousePressed(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();

    Coord offset = v.getOffset();
    int offsetWidth = offset.col;
    int offsetHeight = offset.row;

    try {
      if (x >= ViewPanel.CELL_WIDTH) {
        Coord c = new Coord((x - offsetWidth) / ViewPanel.CELL_WIDTH + 1,
                (y - offsetHeight) / ViewPanel.CELL_HEIGHT);
        this.v.shiftFocus(c.col, c.row);
      }
    } catch (IllegalArgumentException e1) {
      // This is left purposely blank because the user should not get an error
      // for clicking outside of the editable cell region. The program should
      // just continue.
    }
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given MouseEvent
   */
  public void mouseReleased(MouseEvent e) {
    // This method is not used because we only need to provide
    // the functionality of a mouse press.
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given MouseEvent
   */
  public void mouseEntered(MouseEvent e) {
    // This method is not used because we only need to provide
    // the functionality of a mouse press.
  }

  /**
   * This method is deprecated and has no side effects.
   *
   * @param e The given MouseEvent
   */
  public void mouseExited(MouseEvent e) {
    // This method is not used because we only need to provide
    // the functionality of a mouse press.
  }

  @Override
  public void renderSheet() {
    try {
      this.v.render();
    } catch (IOException e) {
      throw new IllegalStateException("Could not render the view.");
    }
  }
}
