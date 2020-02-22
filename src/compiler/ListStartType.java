package compiler;

public class ListStartType extends Command {

  public ListStartType(String declaration) {
    super(declaration);
  }

  @Override
  double execute() {
    double ret = 0;
    for (int i = 0; i < args.size()-1; i++) {
      Command c = args.get(i);
      ret = c.execute();
    }
    return ret;
  }

  @Override
  boolean isCompleteSub() {
    return args.size()>0 && args.get(args.size()-1) instanceof ListEndType; //FIXME instanceof
  }

  @Override
  Command createCommand(String declaration) {
    return new ListStartType(declaration);
  }
}