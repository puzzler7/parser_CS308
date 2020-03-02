package slogo.compiler.math;

import slogo.compiler.parser.Command;

public class RemainderCommand extends Command {

  public RemainderCommand(String declaration) {
    super(declaration);
    desiredArgs = 2;
  }

  @Override
  public double execute() {
    return args.get(0).execute() % args.get(1).execute();
  }

  @Override
  public Command createCommand(String declaration) {
    return new RemainderCommand(declaration);
  }
}
