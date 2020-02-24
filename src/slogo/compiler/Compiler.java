package slogo.compiler;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;
import org.reflections.Reflections;
import slogo.compiler.control.MakeUserInstructionCommand;
import slogo.compiler.exceptions.CompilerException;
import slogo.compiler.exceptions.InvalidSyntaxException;
import slogo.compiler.exceptions.StackOverflowException;
import slogo.compiler.types.CommandType;

public class Compiler {

  private static final String LANGUAGES_PACKAGE_EXTENSION = "slogo.resources.languages.";
  private static final String RESOURCES_PACKAGE = LANGUAGES_PACKAGE_EXTENSION;
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String SYNTAX_FILE = "Syntax";

  private List<Entry<String, Pattern>> myTypes;
  private List<Entry<String, Pattern>> myCommands;

  public static int MAX_RECURSION_DEPTH = 1000;

  public Compiler() {
    myTypes = new ArrayList<>();
    myCommands = new ArrayList<>();
    addPatterns(DEFAULT_LANGUAGE, myCommands); //FIXME add support for multiple languages
    addPatterns(SYNTAX_FILE, myTypes);
    //FIXME add resource file validator
    //FIXME add error msg strings

    initAllCommands();
  }

  private void initAllCommands() { //FIXME uses the reflections library
    Reflections reflections = new Reflections("");//Compiler.class.getPackageName());
    Set<Class<? extends Command>> allClasses = reflections.getSubTypesOf(Command.class);

    for (Class c : allClasses) {
      try {
        //System.out.println(c);
        Command a = (Command) c.getConstructor(String.class).newInstance(Command.INITIALIZATION);
        a.register();
      } catch (Exception e) {
        //do nothing. This is okay because I'm just trying to initialize every command class
        //exceptions thrown by the constructors et al are okay
        System.out.println("Maybe something bad happened? " + e);
      }
    }
  }

  public void setLanguage(String lang) {
    myCommands.clear();
    addPatterns(lang, myCommands);
  }

  //fixme allow for read from file? or make new multiline parser
  //for multiline parser, split by newline regex, delete comments (regex), then join all by spaces
  //then execute as one line3
  public String execute(String input) {
    input = "[ " + String.join(" ", input.split(getNewline())) + " ]";
    try {
      Command comm = parse(input);
      if (!comm.isComplete()) {
        throw new InvalidSyntaxException("Input (" + input + ") not a complete command.");
      }
      if (comm.containsDefinition()) {
        comm =rerunParsing(comm, input);
      }
      return "" + comm.execute();
    } catch (CompilerException e) {
      throw e;
      //return e.toString();
    }
  }

  private Command rerunParsing(Command comm, String input) {
    Command def = comm.findFirstDef();
    if (def != null) {
      def.execute(); //FIXME
    }
    comm = parse(input);
    if (!comm.isComplete()) {
      comm.recPrint(); //fixme
      throw new InvalidSyntaxException("Input (" + input + ") not a complete command.");
    }
    return comm;
  }


  public Command parse(String input) {
    boolean defineFlag = false;
    ArrayDeque<Command> stack = new ArrayDeque<>();
    for (String word : input.split(getWhitespace())) {
      Command comm = getCommandFromString(word);
      if (defineFlag) {
        if (comm instanceof CommandType) {
          ((CommandType) comm).setBeingDefined(true);
          //FIXME you're a bad person and you should feel bad
          defineFlag = false;
        } else {
          throw new InvalidSyntaxException("Cannot redefine builtin function '" + word + "'");
        }
      }
      if (comm instanceof MakeUserInstructionCommand) {
        defineFlag = true;
      }
      stack.push(comm);
      Command ret = collapseStack(stack);
      if (ret != null) {
        return ret;
      }
      if (stack.size() >= MAX_RECURSION_DEPTH) {
        throw new StackOverflowException(
            "Max recursion depth: (" + MAX_RECURSION_DEPTH + ") exceeded.");
      }
    }
    return stack.getLast();
  }

  private Command collapseStack(ArrayDeque<Command> stack) {
    while (stack.peek().isComplete()) {
      Command arg = stack.pop();
      if (stack.peek() == null) {
        if (arg.isComplete()) {
          return arg;
        }
        throw new InvalidSyntaxException(
            "Ran out of commands to parse before finishing given commands.");
      }
      stack.peek().addArg(arg);
    }
    return null;
  }

  private String getWhitespace() {
    for (Entry<String, Pattern> e : myTypes) {
      if (e.getKey().equals("Whitespace")) {
        return e.getValue().toString();
      }
    }
    throw new CompilerException("Invalid Syntax resource file - whitespace not found.");
  }

  private String getNewline() {
    for (Entry<String, Pattern> e : myTypes) {
      if (e.getKey().equals("Newline")) {
        return e.getValue().toString();
      }
    }
    throw new CompilerException("Invalid Syntax resource file - whitespace not found.");
  }

  private Command getCommandFromString(String str) {
    str = str.toLowerCase();
    String type = getSymbol(str, myTypes);
    if (!type.equals("Command")) { //FIXME magic val
      return TypeFactory.createCommand(type, str);
    }
    try {
      String commType = getSymbol(str, myCommands);
      return CommandFactory.createCommand(commType, str);
    } catch (InvalidSyntaxException e) {
      return TypeFactory.createCommand("Command", str); //FIXME magic val
    }
  }

  /**
   * Adds the given resource file to this language's recognized types
   */
  public void addPatterns(String filename, List<Entry<String, Pattern>> list) {
    ResourceBundle resources = ResourceBundle.getBundle(RESOURCES_PACKAGE + filename);
    for (String key : Collections.list(resources.getKeys())) {
      String regex = resources.getString(key);
      list.add(new SimpleEntry<>(key,
          // THIS IS THE IMPORTANT LINE
          Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
    }
  }

  /**
   * Returns language's type associated with the given text if one exists
   */
  public String getSymbol(String text, List<Entry<String, Pattern>> list) {
    for (Entry<String, Pattern> e : list) {
      if (match(text, e.getValue())) {
        return e.getKey();
      }
    }
    throw new InvalidSyntaxException("Identifier (" + text + ") not recognized.");
  }


  // Returns true if the given text matches the given regular expression pattern
  private boolean match(String text, Pattern regex) {
    // THIS IS THE IMPORTANT LINE
    return regex.matcher(text).matches();
  }


}