package trial;

/**
 * Represents a single instruction with a command and its arguments.
 */
public class Instruction {
    private final String command;
    private final String[] args;

    /**
     * Constructs an Instruction with a given command and arguments.
     *
     * @param command the command name
     * @param args    the arguments for the command
     */
    public Instruction(String command, String[] args) {
        this.command = command;
        this.args = args.clone(); // Prevents external modifications
    }

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args.clone(); // Protects internal array
    }

//    @Override
//    public String toString() {
//        return "Instruction{" +
//                "command='" + command + '\'' +
//                ", args=" + String.join(", ", args) +
//                '}';
//    }

    @Override
    public String toString() {
        if (args.length > 0) {
            return command + " " + String.join(" ", args);
        }
        return command;
    }
}
