import java.util.ArrayList;
import java.util.Arrays;

import edu.cs3500.spreadsheets.cellstructure.CellBoolean;
import edu.cs3500.spreadsheets.cellstructure.CellDouble;
import edu.cs3500.spreadsheets.cellstructure.CellString;
import edu.cs3500.spreadsheets.cellstructure.Function;
import edu.cs3500.spreadsheets.cellstructure.Reference;

/**
 * A class containing examples of Cells to be used in testing.
 */
public class ExampleCells {

  ExampleCoords c = new ExampleCoords();

  CellBoolean cellBooleanTrue = new CellBoolean(true);
  CellBoolean cellBooleanFalse = new CellBoolean(false);
  CellString cellStringFoo = new CellString("\"foo\"");
  CellString cellStringBar = new CellString("\"bar\"");
  CellString cellStringJoe = new CellString("\"Who is Joe?\"");
  CellString cellStringDog = new CellString("\"It smells like updog in here.\"");
  CellString cellStringBlerner = new CellString("\"GET BLERNED\"");
  CellDouble cellDoubleZero = new CellDouble(0);
  CellDouble cellDoubleOne = new CellDouble(1);
  CellDouble cellDoubleTwo = new CellDouble(2);
  CellDouble cellDoubleThree = new CellDouble(3);
  CellDouble cellDoubleFour = new CellDouble(4);
  CellDouble cellDoubleFive = new CellDouble(5);
  CellDouble cellDoubleNegOne = new CellDouble(-1);
  Reference referenceA1 = new Reference(new ArrayList<>(Arrays.asList(c.a2)));
  Reference referenceA2 = new Reference(new ArrayList<>(Arrays.asList(c.a3, c.a4)));
  Reference referenceA3 = new Reference(new ArrayList<>(Arrays.asList(c.a4)));
  Reference referenceA4 = new Reference(new ArrayList<>(Arrays.asList(c.a5)));
  Reference referenceA5 = new Reference(new ArrayList<>(Arrays.asList(c.a1)));
  Reference referenceNotDefined = new Reference(new ArrayList<>(Arrays.asList(c.c10)));
  Function sumOverNums = new Function("SUM",
          new ArrayList<>(Arrays.asList(cellDoubleZero,
                  cellDoubleOne, cellDoubleTwo, cellDoubleThree, cellDoubleFour, cellDoubleFive)));
  Function sumOverNumsAndOther = new Function("SUM",
          new ArrayList<>(Arrays.asList(cellDoubleTwo, cellDoubleFive, cellBooleanFalse,
                  cellStringBlerner)));
  Function sumOverOther = new Function("SUM",
          new ArrayList<>(Arrays.asList(cellBooleanFalse, cellStringBlerner, cellStringDog,
                  cellBooleanFalse)));
  Function productOverNums = new Function("PRODUCT",
          new ArrayList<>(Arrays.asList(cellDoubleZero,
                  cellDoubleOne, cellDoubleTwo, cellDoubleThree, cellDoubleFour, cellDoubleFive)));
  Function productOverNumsAndOther = new Function("PRODUCT",
          new ArrayList<>(Arrays.asList(cellDoubleTwo, cellDoubleFive, cellBooleanFalse,
                  cellStringBlerner)));
  Function productOverOther = new Function("PRODUCT",
          new ArrayList<>(Arrays.asList(cellBooleanFalse, cellStringBlerner, cellStringDog,
                  cellBooleanFalse)));
  Function lessThanTrue = new Function("<",
          new ArrayList<>(Arrays.asList(cellDoubleOne, cellDoubleThree)));
  Function lessThanFalse = new Function("<",
          new ArrayList<>(Arrays.asList(cellDoubleFive, cellDoubleTwo)));
  Function lessThanEqual = new Function("<",
          new ArrayList<>(Arrays.asList(cellDoubleFour, cellDoubleFour)));
  Function lessThanOther = new Function("<",
          new ArrayList<>(Arrays.asList(cellBooleanFalse, cellStringBlerner)));
  Function concatOverStrings = new Function("CONCAT",
          new ArrayList<>(Arrays.asList(cellStringDog, cellStringBlerner)));
  Function concatOverStringsAndOther = new Function("CONCAT",
          new ArrayList<>(Arrays.asList(cellStringDog, cellStringBlerner, cellDoubleFour,
                  cellBooleanFalse)));
  Function concatOverOther = new Function("CONCAT",
          new ArrayList<>(Arrays.asList(cellDoubleFour, cellBooleanFalse)));
  Function nestedFunctions = new Function("PRODUCT",
          new ArrayList<>(Arrays.asList(sumOverNums, productOverNums,
                  lessThanEqual, concatOverStrings)));


}
