package edu.cs3500.spreadsheets.cellstructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class that represents a function of a specific operator and arguments.
 */
public class Function implements Formula {
  private final String operator;
  private final List<Cell> arguments;

  /**
   * Constructs a function with a given operator and arguments.
   *
   * @param operator  the desired operator.
   * @param arguments the desired arguments as a list.
   */
  public Function(String operator, List<Cell> arguments) {
    if (arguments == null) {
      throw new IllegalArgumentException("Function given a null list of args");
    }
    this.operator = operator;
    this.arguments = arguments;
  }

  @Override
  public <R> R accept(CellVisitor<R> visitor) {
    return visitor.visitCellFunction(this);
  }

  /**
   * Gets a copy of the arguments of this function for use in visitors.
   *
   * @return A copy of the arguments of this function.
   */
  List<Cell> getArgs() {
    return new ArrayList<>(arguments);
  }

  /**
   * Gets a copy of the operation of this function for use in visitors.
   *
   * @return A copy of the operation of this function.
   */
  String getOp() {
    String copy = new String(this.operator.toCharArray());
    return copy;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Function)) {
      return false;
    }
    Function that = (Function) o;

    boolean equalArgs = true;
    if (this.arguments.size() == that.arguments.size()) {
      for (int i = 0; i < this.arguments.size(); i++) {
        equalArgs = equalArgs && this.arguments.get(i).equals(that.arguments.get(i));
      }
    } else {
      equalArgs = false;
    }


    return this.operator.equals(that.operator) && equalArgs;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.arguments, this.operator);
  }


  @Override
  public String toString() {
    return "=" + this.accept(new ShowVisitor());
  }
}
