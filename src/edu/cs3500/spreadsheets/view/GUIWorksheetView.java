package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A GUI representation of a {@link edu.cs3500.spreadsheets.model.WorksheetModel}. Implementations
 * of this interface may or may not be editable. If certain methods are called on a non editable
 * implementation, an IllegalStateException will be thrown. See individual method JavaDoc for
 * more information. Individual implementations will vary. This interface is reliant on Swing
 * by the use of {@link ActionListener}, {@link KeyListener}, and {@link MouseListener}.
 */
public interface GUIWorksheetView extends WorksheetView {
  /**
   * Accepts a KeyListener for use in EditableGUIView. All other view that implement this method
   * will do nothing and will not accept the given KeyListener
   *
   * @param keyListener The KeyListener to be accepted.
   * @throws IllegalStateException If this GUIWorksheetView does not allow editing of cells.
   */
  void acceptKeyListener(KeyListener keyListener) throws IllegalStateException;

  /**
   * Accepts an ActionListener for use in GUIView and EditableGUIView. Other views tha implement
   * this method will do nothing and not accept the given ActionListener.
   * @param actionListener The ActionListener to be accepted.
   * @throws IllegalStateException If this GUIWorksheetView does not allow editing of cells.
   */
  void acceptActionListener(ActionListener actionListener) throws IllegalStateException;

  /**
   * Gets the cell that is currently the focus of the view.
   *
   * @return the coordinate as a Coord.
   */
  Coord getSelectedCell();

  /**
   * Grants access to the text of the equationBar to allow users to update cells.
   * @return the String in the equation bar.
   */
  String getEquationBarText();


  /**
   * Allows a user to open a popup window to load a text file into the GUI.
   * @throws IllegalStateException If this GUIWorksheetView does not allow editing of cells.
   */
  void openPopup() throws IllegalStateException;

  /**
   * Opens a popup window that allows a user to save their current spreadsheet as a File.
   * @throws IllegalStateException If this GUIWorksheetView does not allow editing of cells.
   */
  void savePopup() throws IllegalStateException;

  /**
   * Accepts a MousesListener for selecting cells via clicking.
   * @param mouseListener The {@link MouseListener} to be accepted.
   * @throws IllegalStateException If this GUIWorksheetView does not allow editing of cells.
   */
  void acceptMouseListener(MouseListener mouseListener) throws IllegalStateException;

  /**
   * Calculates and returns the offset of the spreadsheet as determined by the height of the
   * MenuPanel and the size of row and column headers.
   * @return The offset of the spreadsheet
   */
  Coord getOffset();


  /**
   * Moves the focus based on mouse events.
   *
   * @param x the x coordinate of the movement.
   * @param y the y coordinate of the movement.
   */
  void shiftFocus(int x, int y);
}
