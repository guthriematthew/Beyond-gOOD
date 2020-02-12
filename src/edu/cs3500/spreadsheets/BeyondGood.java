package edu.cs3500.spreadsheets;


import org.jfree.data.general.DefaultPieDataset;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;

import edu.cs3500.spreadsheets.controller.GUIWorksheetController;
import edu.cs3500.spreadsheets.model.Chart;
import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.ReadOnlyWorksheetBuilder;
import edu.cs3500.spreadsheets.model.SimpleWorksheetBuilder;
import edu.cs3500.spreadsheets.model.WorksheetModel;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;
import edu.cs3500.spreadsheets.model.WorksheetReader;
import edu.cs3500.spreadsheets.view.EditableGUIView;
import edu.cs3500.spreadsheets.view.GUIView;
import edu.cs3500.spreadsheets.view.GUIWorksheetView;
import edu.cs3500.spreadsheets.view.GraphingEditableGUIView;
import edu.cs3500.spreadsheets.view.TextualWorksheet;

/**
 * The main class for our program.
 */
public class BeyondGood {
  /**
   * The main entry point.
   *
   * @param args any command-line arguments
   */
  public static void main(String[] args) {
    /*
      - read the file and build a model from it, 
      - evaluate all the cells, and
      - report any errors, or print the evaluated value of the requested cell.
    */
    String[] parsedArgs = parseArgs(args);

    switch (parsedArgs[3]) {
      case "1":
        typeNewGui();
        break;
      case "2":
        typeInEval(parsedArgs);
        break;
      case "3":
        typeInSave(parsedArgs);
        break;
      case "4":
        typeInGui(parsedArgs);
        break;
      case "5":
        typeInEdit(parsedArgs);
        break;
      case "6":
        typeNewEditableGui();
        break;
      case "7":
        typeNewProviderGui();
        break;
      case "8":
        typeInProviderGui(parsedArgs);
        break;
      default:
        throw new IllegalArgumentException("Console args parsed to invalid flag");
    }
  }

  /**
   * Parses the args given regardless of order.
   *
   * @param args The arguments given to main.
   * @return The file name and the cell to be evaluated, in that order, regardless of input.
   */
  private static String[] parseArgs(String[] args) {
    String[] result = new String[4];//0 is infile //1 is outfile //2 is operation //3 is a type flag

    if (args.length == 1 && args[0].equals("-gui")) {
      result[2] = "gui";//operation
      result[3] = "1";//type flag
    } else if (args.length == 4 && args[2].equals("-eval") && args[0].equals("-in")) {
      result[0] = args[1];//infile
      result[1] = args[3];//outfile
      result[2] = "eval";//operation
      result[3] = "2";//type flag
    } else if (args.length == 4 && args[2].equals("-save") && args[0].equals("-in")) {
      result[0] = args[1];//infile
      result[1] = args[3];//outfile
      result[2] = "save";//operation
      result[3] = "3";//type flag
    } else if (args.length == 3 && args[2].equals("-gui") && args[0].equals("-in")) {
      result[0] = args[1];//infile
      result[2] = "gui";//operation
      result[3] = "4";//type flag
    } else if (args.length == 3 && args[2].equals("-edit") && args[0].equals("-in")) {
      result[0] = args[1];//infile
      result[2] = "edit";//operation
      result[3] = "5";//type flag
    } else if (args.length == 1 && args[0].equals("-edit")) {
      result[2] = "edit";//operation
      result[3] = "6";//type flag
    } else if (args.length == 1 && args[0].equals("-provider")) {
      result[2] = "provider";//operation
      result[3] = "7";//type flag
    } else if (args.length == 3 && args[2].equals("-provider")) {
      result[0] = args[1];//infile
      result[2] = "provider";//operation
      result[3] = "8";//type flag
    } else {
      throw new IllegalArgumentException("Malformed command line arguments given!");
    }

    return result;
  }

  /**
   * Builds a worksheet from a file and evaluates a specified cell.
   *
   * @param parsedArgs the name of the file and cell to evaluate.
   */
  private static void typeInEval(String[] parsedArgs) {
    WorksheetReadOnlyModel w = openFileToReadOnlyWorksheet(parsedArgs[0]);

    String numeric = "0123456789";
    String rowStr = "1";
    String colString = "";
    for (int i = 0; i < parsedArgs[1].length(); i++) {
      if (numeric.contains(parsedArgs[1].substring(i, i + 1))) {
        rowStr = parsedArgs[1].substring(i);
        colString = parsedArgs[1].substring(0, i);
        break;
      }
    }
    int row = Integer.parseInt(rowStr);
    System.out.print(w.getCellValue(new Coord(Coord.colNameToIndex(colString), row)));
  }

  /**
   * Creates a new worksheet and displays it as a gui.
   */
  private static void typeNewGui() {
    ReadOnlyWorksheetBuilder b = new ReadOnlyWorksheetBuilder();
    WorksheetReadOnlyModel w = b.createWorksheet();
    GUIView gui = new GUIView("Big Excel", w);
    gui.render();
  }

  /**
   * Creates a new worksheet and displays it as an editable gui.
   */
  private static void typeNewEditableGui() {
    SimpleWorksheetBuilder b = new SimpleWorksheetBuilder();
    WorksheetModel w = b.createWorksheet();
    EditableGUIView gui = new EditableGUIView("Big Excel", w);
    GUIWorksheetController controller = new GUIWorksheetController(w, gui);
    gui.acceptActionListener(controller);
    gui.acceptKeyListener(controller);
    gui.acceptMouseListener(controller);
    controller.renderSheet();
  }


  /**
   * Creates a worksheet from file and displays it as a gui.
   */
  private static void typeInGui(String[] parsedArgs) {
    WorksheetReadOnlyModel w = openFileToReadOnlyWorksheet(parsedArgs[0]);
    GUIView gui = new GUIView("Big Excel", w);
    gui.render();
  }

  /**
   * Creates a worksheet from file and displays it as an editable gui.
   */
  private static void typeInEdit(String[] parsedArgs) {
    WorksheetModel w = openFileToWorksheet(parsedArgs[0]);
    GUIWorksheetView gui = new GraphingEditableGUIView(new EditableGUIView("Big Excel", w), w);
    GUIWorksheetController controller = new GUIWorksheetController(w, gui);
    gui.acceptKeyListener(controller);
    gui.acceptActionListener(controller);
    gui.acceptMouseListener(controller);
    controller.renderSheet();
  }

  /**
   * Takes a file and saves worksheet to given filename.
   *
   * @param parsedArgs the name of in and save files.
   */
  private static void typeInSave(String[] parsedArgs) {

    WorksheetReadOnlyModel w = openFileToReadOnlyWorksheet(parsedArgs[0]);
    PrintWriter p;
    try {
      p = new PrintWriter(parsedArgs[1]);
      TextualWorksheet t = new TextualWorksheet(w, p);
      t.render();
      saveGraphs(w, parsedArgs[1]);
    } catch (IOException e) {
      throw new IllegalArgumentException("Output filename was invalid");
    }
    p.close();
  }

  /**
   * Creates a new provider view and creates a new worksheet. Supports all desired control
   * features.
   */
  private static void typeNewProviderGui() {
    /*
    IWorksheet w = new AdapterWorksheet(new Worksheet());
    IWorksheetView gui = new BasicWorksheetEditableView(w);
    gui.render();
    */
  }

  /**
   * Takes a file and opens a provider view with it. Supports all desired control features.
   */
  private static void typeInProviderGui(String[] parsedArgs) {
    /*
    IWorksheet w = new AdapterWorksheet(openFileToWorksheet(parsedArgs[0]));
    BasicWorksheetEditableView gui = new BasicWorksheetEditableView(w);
    AdapterController controller = new AdapterController(w, gui);
    gui.addActionListener(controller);
    gui.addMouseListener(controller);
    gui.render();
    */
  }

  /**
   * Creates a worksheet taken from a file.
   *
   * @param fileName string of the file.
   * @return a worksheet model that corresponds to the given data.
   */
  private static WorksheetReadOnlyModel openFileToReadOnlyWorksheet(String fileName) {
    FileReader f;
    try {
      f = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    ReadOnlyWorksheetBuilder builder = new ReadOnlyWorksheetBuilder();
    return WorksheetReader.read(builder, f);
  }

  /**
   * Creates a worksheet taken from a file.
   *
   * @param fileName string of the file.
   * @return a worksheet model that corresponds to the given data.
   */
  private static WorksheetModel openFileToWorksheet(String fileName) {
    FileReader f;
    try {
      f = new FileReader(fileName);
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("Given file was not found.");
    }
    SimpleWorksheetBuilder builder = new SimpleWorksheetBuilder();
    return openGraphs(WorksheetReader.read(builder, f), fileName);
  }

  private static WorksheetModel openGraphs(WorksheetModel model, String fileName) {
    String modifiedName = fileExtension(fileName);
    FileReader f;
    try {
      f = new FileReader(modifiedName);
    } catch (FileNotFoundException e) {
      return model;
    }
    parseGraphs(model, f);
    return model;
  }

  private static void saveGraphs(WorksheetReadOnlyModel model, String fileName) {
    String modifiedName = fileExtension(fileName);
    try {
      PrintWriter p = new PrintWriter(modifiedName);
      for (Chart c : model.getCharts()) {
        p.println(c.toString());
      }
    } catch (IOException e) {
      // The given file could not be saved but there is no need the throw an error here
    }
  }

  /**
   * Parses the intended graph file name from the spreadsheet filename.
   * @param fileName String representing other filename.
   * @return String of the new filename.
   */
  public static String fileExtension(String fileName) {
    int dot = fileName.indexOf('.');
    if (dot != -1) {
      return fileName.substring(0, dot) + ".ch";
    } else {
      return fileName + ".ch";
    }
  }

  private static void parseGraphs(WorksheetModel model, FileReader f) {
    Scanner data = new Scanner(f);
    while (data.hasNextLine()) {
      DefaultPieDataset pieChartData = new DefaultPieDataset();
      HashSet<Coord> tempDep = new HashSet<>();
      Coord s1 = coordFromString(data.next());
      Coord s2 = coordFromString(data.next());
      int length = Integer.parseInt(data.next());
      String title = data.next();

      try {
        for (int i = 0; i < length; i++) {
          Coord c1 = new Coord(s1.col, s1.row + i);
          Coord c2 = new Coord(s2.col, s2.row + i);
          String name = model.getCellValue(c1).toString();
          double value = Double.parseDouble(model.getCellValue(c2).toString());
          pieChartData.setValue(name, value);
          tempDep.add(c1);
          tempDep.add(c2);
        }
      } catch (NullPointerException e) {
        throw new IllegalArgumentException("No data at this position");
      }
      Chart tempChart = new Chart(s1, s2, length, pieChartData, tempDep, title,
              model.getCharts().size());
      model.addChart(tempChart);
    }
  }

  private static Coord coordFromString(String s) {
    String rowStr = "1";
    String colString = "";
    String numerals = "0123456789";
    for (int i = 0; i < s.length(); i++) {
      if (numerals.contains(s.substring(i, i + 1))) {
        rowStr = s.substring(i);
        colString = s.substring(0, i);
        break;
      }
    }
    int row = Integer.parseInt(rowStr);
    return new Coord(Coord.colNameToIndex(colString), row);
  }
}
