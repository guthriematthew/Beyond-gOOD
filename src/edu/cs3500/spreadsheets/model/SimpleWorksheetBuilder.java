package edu.cs3500.spreadsheets.model;

/**
 * A class that builds a worksheet corresponding with a given file.
 */
public class SimpleWorksheetBuilder implements WorksheetReader.WorksheetBuilder<WorksheetModel> {
  private Worksheet worksheet;

  /**
   * A constructor that initialized the worksheet field.
   */
  public SimpleWorksheetBuilder() {
    worksheet = new Worksheet();
  }


  @Override
  public WorksheetReader.WorksheetBuilder<WorksheetModel> createCell(int col,
                                                                     int row, String contents) {
    try {
      this.worksheet.setCell(new Coord(col, row), contents);
    } catch (IllegalArgumentException e) {
      System.out.println("Error in cell "
              + Coord.colIndexToName(col) + row + ": " + e.getMessage());
    }
    return this;
  }

  @Override
  public Worksheet createWorksheet() {
    return worksheet;
  }
}
