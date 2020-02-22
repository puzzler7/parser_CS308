package compiler;

public class ForCommand extends Command {

  public ForCommand(String declaration) {
    super(declaration);
  }

  @Override
  double execute() {
    double ret = 0;
    String varName = ((VariableType) args.get(0).args.get(0)).getName(); //FIXME bad bad bad maybe do a tostring?
    double start = args.get(0).args.get(1).execute();
    double end = args.get(0).args.get(2).execute() + .000000001; //FIXME magic val
    double inc = args.get(0).args.get(3).execute();
    for (double i = start; i <= end; i+=inc) {
      Memory.setVariable(varName, i);
      ret = args.get(1).execute();
    }
    return ret;
  }

  @Override
  boolean isCompleteSub() { //FIXME instanceofs everywhere
    if (!(args.size() == 2 && args.get(0) instanceof ListStartType && args
        .get(1) instanceof ListStartType)) {
      return false; //has 2 args, both lists
    }
    return args.get(0).args.size()==5 && args.get(0).args.get(0) instanceof VariableType; //list has 4 args, 1st is variable
  }

  @Override
  Command createCommand(String declaration) {
    return new ForCommand(declaration);
  }
}
