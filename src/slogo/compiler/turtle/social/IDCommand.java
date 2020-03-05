package slogo.compiler.turtle.social;

import slogo.compiler.turtle.query.TurtleQuery;
import slogo.compiler.types.ListStartType;

public class IDCommand extends TurtleQuery {

  public IDCommand(String declaration) {
    super(declaration);
    desiredArgs = 0;
  }

  @Override
  public double executeTurtle() {
    return memory.getCurrentTurtleID();
  }
}
