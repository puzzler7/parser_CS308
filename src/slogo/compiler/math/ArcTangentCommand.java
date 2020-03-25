package slogo.compiler.math;

import slogo.compiler.parser.Command;

/**
 * @author Maverick Chung mc608
 * <p>
 * Purpose: A command for arctan
 */
public class ArcTangentCommand extends Command {

  public ArcTangentCommand(String declaration) {
    super(declaration);
    desiredArgs = 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double executeCommand() {
    return Math.toDegrees(Math.atan(args.get(0).execute()));
  }
}
