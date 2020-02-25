package slogo.compiler.turtle.command;

import slogo.compiler.Command;
import slogo.compiler.turtle.TurtleCommand;

public class SetTowardsCommand extends TurtleCommand {

  public SetTowardsCommand(String declaration) {
    super(declaration);
  }

  @Override
  public double executeTurtle() {
    double currentHead = turtle.getHeading();
    double x = args.get(0).execute();
    double y = args.get(1).execute();
    turtle.towards(x, y);
    return turtle.getHeading() - currentHead;
  }

  @Override
  public boolean isCompleteSub() {
    return args.size() == 2;
  }

  @Override
  public Command createCommand(String declaration) {
    return new SetTowardsCommand(declaration);
  }
}
