package slogo.compiler.math;

import slogo.compiler.exceptions.InvalidArithmeticException;
import slogo.compiler.parser.Command;

public class RemainderCommand extends Command {

  public RemainderCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
    groupingType = Command.GROUPING_RECURSIVE;
  }

  @Override
  public double executeCommand() {
    double denom = args.get(1).execute();
    if (denom == 0) {
      throw new InvalidArithmeticException(errorMsgs.getString("DivideByZero"));
    }
    return args.get(0).execute() % denom;
  }
}
