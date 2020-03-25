package slogo.compiler.math;

import slogo.compiler.parser.Command;

/**
 * @author Maverick Chung mc608
 * <p>
 * Purpose: A command for addition
 */
public class SumCommand extends Command {

  public SumCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
    groupingType = Command.GROUPING_RECURSIVE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double executeCommand() {
    return args.get(0).execute() + args.get(1).execute();
  }
}
