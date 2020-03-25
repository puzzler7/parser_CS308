package slogo.compiler.logic;

import slogo.compiler.parser.Command;

/**
 * @author Maverick Chung mc608
 * <p>
 * Purpose: A command for equality logic
 */
public class EqualCommand extends Command {

  public EqualCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
    groupingType = Command.GROUPING_COMPARISON;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double executeCommand() {
    boolean val = args.get(0).execute() == args.get(1).execute();
    if (val) {
      return 1;
    }
    return 0;
  }
}
