package slogo.compiler.turtle.command;

import slogo.compiler.Command;
import slogo.compiler.turtle.TurtleCommand;

public class ShowTurtleCommand extends TurtleCommand {

  public ShowTurtleCommand(String declaration) {
    super(declaration);
    desiredArgs = 0;
  }

  @Override
  public double executeTurtle() {
    turtle.showTurtle(true);
    return 1;
  }

  @Override
  public Command createCommand(String declaration) {
    return new ShowTurtleCommand(declaration);
  }
}
