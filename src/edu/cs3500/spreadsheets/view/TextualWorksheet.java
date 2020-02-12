package edu.cs3500.spreadsheets.view;

import java.io.IOException;
import java.util.List;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * A Class which represents a Worksheet as a text file. Rendering this view will save the given
 * Worksheet to the desired appendable,
 */
public class TextualWorksheet implements WorksheetView {
  private WorksheetReadOnlyModel w;
  private Appendable a;

  /**
   * A constructor for a TextualWorksheet.
   *
   * @param w the worksheet to be created.
   * @param a the appendable to which data will be written.
   */
  public TextualWorksheet(WorksheetReadOnlyModel w, Appendable a) {
    if (w == null || a == null) {
      throw new IllegalArgumentException("View cannot be given null");
    }
    this.w = w;
    this.a = a;
  }


  @Override
  public void render() throws IOException {
    a.append(this.toString());
  }

  @Override
  public String toString() {
    List<Coord> nonEmptyCells = w.getNonEmptyCells();
    String result = "";
    for (Coord c : nonEmptyCells) {
      result += c.toString();
      result += " ";
      try {
        result += w.getCellExpression(c).toString();
      } catch (IllegalArgumentException e) {
        result += e.getMessage();
      }
      result += "\n";
    }
    return result;
  }
}
