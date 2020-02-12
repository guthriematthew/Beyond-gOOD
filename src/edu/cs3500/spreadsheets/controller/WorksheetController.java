package edu.cs3500.spreadsheets.controller;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * A Controller for the interaction between a {@link edu.cs3500.spreadsheets.model.WorksheetModel}
 * and a {@link edu.cs3500.spreadsheets.view.GUIWorksheetView}. This controller must be able to
 * render it's view and implement all the listeners needed by the view. The supported actions
 * include: deleting a cell if the delete key is pressed while the cell is selected,
 * accepting and rejecting equations put into the view's equation bar, and moving the focused
 * cell based on mouse click. See the individual
 * {@link edu.cs3500.spreadsheets.view.GUIWorksheetView} implementation for more information on
 * supported actions.
 */
public interface WorksheetController extends KeyListener, ActionListener, MouseListener {

  /**
   * Renders the view held by this controller.
   */
  void renderSheet();
}
