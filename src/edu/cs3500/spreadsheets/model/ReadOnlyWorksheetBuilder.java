package edu.cs3500.spreadsheets.model;

/**
 * A builder that creates a read only model.
 */
public class ReadOnlyWorksheetBuilder implements
        WorksheetReader.WorksheetBuilder<WorksheetReadOnlyModel> {
  private Worksheet worksheet;

  /**
   * A constructor that initialized the worksheet field.
   */
  public ReadOnlyWorksheetBuilder() {
    worksheet = new Worksheet();
  }


  @Override
  public WorksheetReader.WorksheetBuilder<WorksheetReadOnlyModel> createCell(
          int col, int row, String contents) {
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
