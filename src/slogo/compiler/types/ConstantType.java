package slogo.compiler.types;

import slogo.compiler.parser.Command;

/**
 * @author Maverick Chung mc608
 * <p>
 * Purpose: A command for numeric values
 */
public class ConstantType extends TypeCommand {

  private double value;

  public ConstantType(String val) {
    super(val);
    desiredArgs = 0;
    if (val.equals(Command.INITIALIZATION)) {
      return;
    }
    try {
      value = Double.parseDouble(val);
    } catch (NumberFormatException e) {
      System.out.println("bad number given: " + val); //FIXME bandaid
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double executeCommand() {
    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCompleteSub() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "const:" + value;
  }
}
