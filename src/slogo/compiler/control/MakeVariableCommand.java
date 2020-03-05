package slogo.compiler.control;

import slogo.compiler.parser.Command;
import slogo.compiler.types.VariableType;

public class MakeVariableCommand extends Command {

  public MakeVariableCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
  }

  @Override
  public double execute() {
    double value = args.get(1).execute();
    memory.setVariable(((VariableType) args.get(0)).getName(),
        value); //FIXME refactor args to remove instanceof?
    return value;
  }

  @Override
  public boolean isCompleteSub() {
    return args.size() == desiredArgs && args
        .get(0) instanceof VariableType; //FIXME refactor args to remove instanceof?
  }
}
