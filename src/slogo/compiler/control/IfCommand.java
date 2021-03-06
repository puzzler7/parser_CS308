package slogo.compiler.control;

import slogo.compiler.parser.Command;

public class IfCommand extends Command {

  public IfCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
    groupingType = Command.GROUPING_ITERATIVE;
  }

  @Override
  public double executeCommand() {
    double val = args.get(0).execute();
    double ret = 0;
    if (val != 0) {
      ret = args.get(1).execute();
    }
    return ret;
  }

  @Override
  public boolean isCompleteSub() {
    return args.size() == desiredArgs && args.get(1).typeEquals("liststart");
  }
}
