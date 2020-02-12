package edu.cs3500.spreadsheets.view;

import java.io.IOException;

/**
 * Represents a view for a Worksheet. Could be textual or in gui form.
 */
public interface WorksheetView {
  /**
   * Renders the Worksheet. Rendering depends on implementation by each view implementing this
   * interface.
   */
  void render() throws IOException;
}
