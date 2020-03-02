package slogo.compiler.turtle.command;

import slogo.compiler.Command;
import slogo.compiler.turtle.TurtleCommand;

public class BackwardCommand extends TurtleCommand {

  public BackwardCommand(String declaration) {
    super(declaration);
    desiredArgs = 1;
  }

  @Override
  public double executeTurtle() {
    double val = args.get(0).execute();
    turtle.move(-val);
    return val;
  }

  @Override
  public Command createCommand(String declaration) {
    return new BackwardCommand(declaration);
  }
}
