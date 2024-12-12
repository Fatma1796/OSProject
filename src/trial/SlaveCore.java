//package trial;
//
//public class SlaveCore extends Thread {
//    private final int coreId;
//    private Process currentProcess;
//    private volatile boolean isIdle = true;
//    private volatile boolean terminate = false;
//    private int quantum;
//
//    public SlaveCore(int coreId) {
//        this.coreId = coreId;
//    }
//
//    public synchronized void terminate() {
//        terminate = true;
//        notify();
//    }
//
//    public synchronized void assignProcess(Process process, int quantum) {
//        this.currentProcess = process;
//        this.quantum = quantum;
//        isIdle = false;
//        notify();
//    }
//
//    public boolean isIdle() {
//        return isIdle;
//    }
//
//    public void makeIdle() {
//        this.currentProcess = null;
//        this.isIdle = true;
//    }
//
//    @Override
//    public void run() {
//        while (!terminate) {
//            synchronized (this) {
//                while (isIdle && !terminate) {
//                    try {
//                        wait();
//                    } catch (InterruptedException e) {
//                        System.err.println("Core " + coreId + " interrupted.");
//                        return;
//                    }
//                }
//            }
//
//            if (!terminate && currentProcess != null) {
//                executeCurrentProcess();
//            }
//        }
//        System.out.println("Core " + coreId + " terminated.");
//    }
//
//    private void executeCurrentProcess() {
//        System.out.println("Core " + coreId + " executing Process " + currentProcess.getProcessId());
//
//        int executedInstructions = 0;
//
//        while (executedInstructions < quantum && currentProcess.hasNextInstruction()) {
//            Instruction instruction = currentProcess.getNextInstruction();
//            executeInstruction(instruction, currentProcess.getMemory());
//            executedInstructions++;
//        }
//
//        if (!currentProcess.hasNextInstruction()) {
//            System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
//            currentProcess.markComplete();
//        } else {
//            System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
//        }
//
//        makeIdle();
//    }
//
//    private void executeInstruction(Instruction instruction, SharedMemory memory) {
//        String command = instruction.getCommand();
//        String[] args = instruction.getArgs();
//
//        switch (command) {
//            case "assign":
//                if (args[1].equals("input")) {
//                    handleInput(args[0], memory);
//                } else {
//                    performArithmetic(args[1], args, memory);
//                }
//                break;
//            case "print":
//                handlePrint(args[0], memory);
//                break;
//        }
//    }
//
//    private void handleInput(String variable, SharedMemory memory) {
//        System.out.print("Enter value for " + variable + ": ");
//        int value = new java.util.Scanner(System.in).nextInt();
//        memory.assign(variable.charAt(0), value);
//    }
//
//    private void handlePrint(String variable, SharedMemory memory) {
//        int value = memory.get(variable.charAt(0));
//        System.out.println(variable + " = " + value);
//    }
//
//    private void performArithmetic(String operation, String[] args, SharedMemory memory) {
//        int operand1 = memory.get(args[1].charAt(0));
//        int operand2 = memory.get(args[2].charAt(0));
//        int result = 0;
//
//        switch (operation) {
//            case "add":
//                result = operand1 + operand2;
//                break;
//            case "subtract":
//                result = operand1 - operand2;
//                break;
//            case "multiply":
//                result = operand1 * operand2;
//                break;
//            case "divide":
//                if (operand2 != 0) {
//                    result = operand1 / operand2;
//                } else {
//                    System.out.println("Error: Division by zero");
//                }
//                break;
//        }
//
//        memory.assign(args[0].charAt(0), result);
//    }
//
//            public int getCoreId() {
//        return coreId;
//    }
//}


package trial;

public class SlaveCore extends Thread {
    private final int coreId;
    private final SharedMemory memory;
    private Process currentProcess;
    private volatile boolean isIdle = true;
    private volatile boolean terminate = false;
    private int quantum;

    public SlaveCore(int coreId, SharedMemory memory) {
        this.coreId = coreId;
        this.memory = memory;
    }

    public synchronized void terminate() {
        terminate = true;
        notify();
    }

    public synchronized void assignProcess(Process process, int quantum) {
        this.currentProcess = process;
        this.quantum = quantum;
        isIdle = false;
        notify();
    }

    public boolean isIdle() {
        return isIdle;
    }

    public synchronized Process getCurrentProcess() {
        return currentProcess;
    }

    public void makeIdle() {
        this.currentProcess = null;
        this.isIdle = true;
    }

    @Override
    public void run() {
        while (!terminate) {
            synchronized (this) {
                while (isIdle && !terminate) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        System.err.println("Core " + coreId + " interrupted.");
                        return;
                    }
                }
            }

            if (!terminate && currentProcess != null) {
                executeCurrentProcess();
            }
        }
        System.out.println("Core " + coreId + " terminated.");
    }

    private void executeCurrentProcess() {
        System.out.println("Core " + coreId + " executing Process " + currentProcess.getProcessId());

        int executedInstructions = 0;

        // Execute instructions as per quantum
        while (executedInstructions < quantum && currentProcess.hasNextInstruction()) {
            Instruction instruction = currentProcess.getNextInstruction();
            executeInstruction(instruction);
            executedInstructions++;
        }

        // Check if process is completed
        if (!currentProcess.hasNextInstruction()) {
            System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
            currentProcess.markComplete();
        } else {
            System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
        }

        // Return the process to the ready queue if not completed
        makeIdle();
    }

    private void executeInstruction(Instruction instruction) {
        String command = instruction.getCommand();
        String[] args = instruction.getArgs();

        switch (command) {
            case "assign":
                if (args[1].equals("input")) {
                    handleInput(args[0]);
                } else {
                    performArithmetic(args[1], args);
                }
                break;
            case "add":
            case "subtract":
            case "multiply":
            case "divide":
                performArithmetic(command, args);
                break;
            case "print":
                System.out.println(args[0] + " = " + memory.get(args[0].charAt(0)));
                break;
        }
    }

    private void handleInput(String variable) {
        System.out.print("Enter value for " + variable + ": ");
        int value = new java.util.Scanner(System.in).nextInt();
        memory.assign(variable.charAt(0), value);
    }

    private void performArithmetic(String operation, String[] args) {
        int operand1 = memory.get(args[1].charAt(0));
        int operand2 = memory.get(args[2].charAt(0));
        int result = switch (operation) {
            case "add" -> operand1 + operand2;
            case "subtract" -> operand1 - operand2;
            case "multiply" -> operand1 * operand2;
            case "divide" -> operand2 != 0 ? operand1 / operand2 : 0;
            default -> 0;
        };

        if (operation.equals("divide") && operand2 == 0) {
            System.out.println("Error: Division by zero");
        }
        memory.assign(args[0].charAt(0), result);
    }

        public int getCoreId() {
        return coreId;
    }
}
//
//
//////package trial;
//////import java.util.Scanner;
//////
////
////package trial;
////
////public class SlaveCore extends Thread {
////    private final int coreId;
////    private final SharedMemory memory;
////    private Process currentProcess;
////    private volatile boolean isIdle = true;
////    private volatile boolean terminate = false;
////    private int quantum;
////
////    public SlaveCore(int coreId, SharedMemory memory) {
////        this.coreId = coreId;
////        this.memory = memory;
////    }
////
////    public synchronized void terminate() {
////        terminate = true;
////        notify();
////    }
////
////    public synchronized void assignProcess(Process process, int quantum) {
////        this.currentProcess = process;
////        this.quantum = quantum;
////        isIdle = false;
////        notify();
////    }
////
////    public boolean isIdle() {
////        return isIdle;
////    }
////
////    public synchronized Process getCurrentProcess() {
////        return currentProcess;
////    }
////
////    public void makeIdle() {
////        this.currentProcess = null;
////        this.isIdle = true;
////    }
////
////    public void run() {
////        while (!terminate) {
////            synchronized (this) {
////                while (isIdle && !terminate) {
////                    try {
////                        wait();
////                    } catch (InterruptedException e) {
////                        System.err.println("Core " + coreId + " interrupted.");
////                        return;
////                    }
////                }
////            }
////
////            if (!terminate && currentProcess != null) {
////                executeCurrentProcess();
////            }
////        }
////        System.out.println("Core " + coreId + " terminated.");
////    }
////
////    private void executeCurrentProcess() {
////        System.out.println("Core " + coreId + " executing Process " + currentProcess.getProcessId());
////
////        int executedInstructions = 0;
////        while (executedInstructions < quantum && currentProcess.hasNextInstruction()) {
////            Instruction instruction = currentProcess.getNextInstruction();
////            executeInstruction(instruction);
////            executedInstructions++;
////        }
////
////        if (!currentProcess.hasNextInstruction()) {
////            System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
////        } else {
////            System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
////        }
////
////        makeIdle();
////    }
////
////    private void executeInstruction(Instruction instruction) {
////        String command = instruction.getCommand();
////        String[] args = instruction.getArgs();
////
////        switch (command) {
////            case "assign":
////                if (args[1].equals("input")) {
////                    handleInput(args[0]);
////                } else {
////                    performArithmetic(args[1], args);
////                }
////                break;
////            case "add":
////            case "subtract":
////            case "multiply":
////            case "divide":
////                performArithmetic(command, args);
////                break;
////            case "print":
////                System.out.println(args[0] + " = " + memory.get(args[0].charAt(0)));
////                break;
////        }
////    }
////
////    private void handleInput(String variable) {
////        System.out.print("Enter value for " + variable + ": ");
////        int value = new java.util.Scanner(System.in).nextInt();
////        memory.assign(variable.charAt(0), value);
////    }
////
////    private void performArithmetic(String operation, String[] args) {
////        int operand1 = memory.get(args[1].charAt(0));
////        int operand2 = memory.get(args[2].charAt(0));
////        int result = switch (operation) {
////            case "add" -> operand1 + operand2;
////            case "subtract" -> operand1 - operand2;
////            case "multiply" -> operand1 * operand2;
////            case "divide" -> operand2 != 0 ? operand1 / operand2 : 0;
////            default -> 0;
////        };
////
////        if (operation.equals("divide") && operand2 == 0) {
////            System.out.println("Error: Division by zero");
////        }
////        memory.assign(args[0].charAt(0), result);
////    }
////
////    public int getCoreId() {
////        return coreId;
////    }
////}
//////public class SlaveCore extends Thread {
//////    private final int coreId;
//////    private final SharedMemory memory;
//////    private Process currentProcess;
//////    private volatile boolean isIdle = true;
//////    private volatile boolean terminate = false;
//////    private int quantum; // Quantum for the current process
//////
//////    public SlaveCore(int coreId, SharedMemory memory) {
//////        this.coreId = coreId;
//////        this.memory = memory;
//////    }
//////
//////
//////    public synchronized void terminate() {
//////        terminate = true;
//////        notify();
//////    }
//////
//////    public synchronized void assignProcess(Process process, int quantum) {
//////        currentProcess = process;
//////        this.quantum = quantum;
//////        currentProcess.setQuantum(quantum);
//////        isIdle = false;
//////        notify();
//////    }
//////
//////    public boolean isIdle() {
//////        return isIdle;
//////    }
//////
//////    public void run() {
//////        while (!terminate) {
//////            synchronized (this) {
//////                while (isIdle && !terminate) {
//////                    try {
//////                        wait();
//////                    } catch (InterruptedException e) {
//////                        System.err.println("Core " + coreId + " interrupted.");
//////                        return;
//////                    }
//////                }
//////            }
//////
//////            if (!terminate && currentProcess != null) {
//////                executeCurrentProcess();
//////            }
//////        }
//////        System.out.println("Core " + coreId + " terminated.");
//////    }
//////
//////    private void executeCurrentProcess() {
//////        if (currentProcess.hasNextInstruction() && quantum > 0) {
//////            Instruction instruction = currentProcess.getNextInstruction();
//////            executeInstruction(instruction);
//////            quantum--;
//////            memory.printMemoryState(); // Print memory state after executing each instruction
//////        }
//////        if (!currentProcess.hasNextInstruction() || quantum == 0) {
//////            if (!currentProcess.hasNextInstruction()) {
//////                System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
//////                currentProcess = null;
//////                isIdle = true;
//////            } else {
//////                System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
//////                isIdle = true;
//////            }
//////        }
//////    }
//////
////////    private void executeCurrentProcess() {
////////        if (currentProcess.hasNextInstruction() && quantum > 0) {
////////            Instruction instruction = currentProcess.getNextInstruction();
////////            executeInstruction(instruction);
////////            quantum--;
////////            memory.printMemoryState();
////////        }
////////        if (!currentProcess.hasNextInstruction() || quantum == 0) {
////////            if (!currentProcess.hasNextInstruction()) {
////////                System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
////////                currentProcess = null;
////////                isIdle = true;
////////            } else {
////////                System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
////////                isIdle = true;
////////            }
////////        }
////////    }
//////
////////    @Override
////////    public void run() {
////////        while (!terminate) {
////////            synchronized (this) {
////////                while (isIdle && !terminate) {
////////                    try {
////////                        wait();
////////                    } catch (InterruptedException e) {
////////                        System.err.println("Core " + coreId + " interrupted.");
////////                        return;
////////                    }
////////                }
////////            }
////////
////////            if (!terminate && currentProcess != null) {
////////                executeCurrentProcess();
////////                isIdle = true;
////////            }
////////        }
////////        System.out.println("Core " + coreId + " terminated.");
////////    }
//////
//////
////////    private void executeCurrentProcess() {
////////        int executedInstructions = 0;
////////        System.out.println("Core " + coreId + " executing Process " + currentProcess.getProcessId());
////////        while (currentProcess.hasNextInstruction() && executedInstructions < quantum) {
////////            Instruction instruction = currentProcess.getNextInstruction();
////////            executeInstruction(instruction);
////////            executedInstructions++;
////////            memory.printMemoryState(); // Add this line
////////        }
////////        if (currentProcess.hasNextInstruction()) {
////////            System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
////////        } else {
////////            System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
////////            currentProcess = null; // Clear the process only if it's finished
////////        }
////////    }
//////
//////
//////
//////
////////    private void executeCurrentProcess() {
////////        while (currentProcess.hasNextInstruction()) {
////////            Instruction instruction = currentProcess.getNextInstruction();
////////            executeInstruction(instruction);
////////        }
////////        // Mark process as complete somehow or remove from the active list
////////        currentProcess.markComplete(); // You might need to add this method
////////        isIdle = true;
////////    }
//////
//////
////
//////
//////
//////
//////    public synchronized Process getCurrentProcess() {
//////        return currentProcess;
//////    }
//////
//////    public synchronized void makeIdle() {
//////        this.currentProcess = null;
//////        this.isIdle = true;
//////    }
//////
//////    private synchronized void handleInput(String variable) {
//////        Scanner scanner = new Scanner(System.in);
//////        System.out.print("Enter value for " + variable + ": ");
//////        int value = scanner.nextInt(); // Read integer input
//////        memory.assign(variable.charAt(0), value);
//////        scanner.nextLine(); // Consume the newline left-over
//////    }
//////
//////
//////    private void executeInstruction(Instruction instruction) {
//////        String command = instruction.getCommand();
//////        String[] args = instruction.getArgs();
//////
//////        switch (command) {
//////            case "assign":
//////                if (args.length > 2 && (args[1].equals("add") || args[1].equals("subtract") || args[1].equals("multiply") || args[1].equals("divide"))) {
//////                    performArithmetic(args[1], new String[]{args[0], args[2], args[3]});
//////                } else {
//////                    memory.assign(args[0].charAt(0), Integer.parseInt(args[1]));
//////                }
//////                break;
//////            case "add":
//////            case "subtract":
//////            case "multiply":
//////            case "divide":
//////                performArithmetic(command, args);
//////                break;
//////            case "print":
//////                System.out.println(args[0] + " = " + memory.get(args[0].charAt(0)));
//////                break;
//////        }
//////    }
//////
//////    private void performArithmetic(String operation, String[] args) {
//////        int operand1 = memory.get(args[1].charAt(0));
//////        int operand2 = memory.get(args[2].charAt(0));
//////        int result = switch (operation) {
//////            case "add" -> operand1 + operand2;
//////            case "subtract" -> operand1 - operand2;
//////            case "multiply" -> operand1 * operand2;
//////            case "divide" -> operand2 != 0 ? operand1 / operand2 : 0; // Handle division by zero
//////            default -> 0;
//////        };
//////        if (operation.equals("divide") && operand2 == 0) {
//////            System.out.println("Error: Division by zero");
//////        }
//////        memory.assign(args[0].charAt(0), result);
//////    }
//////
////////    private void executeInstruction(Instruction instruction) {
////////        String command = instruction.getCommand();
////////        String[] args = instruction.getArgs();
////////
////////        switch (command) {
////////            case "assign":
////////                if (args.length > 2 && (args[1].equals("add") || args[1].equals("subtract") || args[1].equals("multiply") || args[1].equals("divide"))) {
////////                    performArithmetic(args[1], new String[]{args[0], args[2], args[3]});
////////                } else {
////////                    memory.assign(args[0].charAt(0), Integer.parseInt(args[1]));
////////                }
////////                break;
////////            case "add":
////////            case "subtract":
////////            case "multiply":
////////            case "divide":
////////                performArithmetic(command, args);
////////                break;
////////            case "print":
////////                System.out.println(args[0] + " = " + memory.get(args[0].charAt(0)));
////////                break;
////////        }
////////    }
////////
////////    private void performArithmetic(String operation, String[] args) {
////////        int operand1 = memory.get(args[1].charAt(0));
////////        int operand2 = memory.get(args[2].charAt(0));
////////        int result = 0;
////////        switch (operation) {
////////            case "add":
////////                result = operand1 + operand2;
////////                break;
////////            case "subtract":
////////                result = operand1 - operand2;
////////                break;
////////            case "multiply":
////////                result = operand1 * operand2;
////////                break;
////////            case "divide":
////////                result = operand2 != 0 ? operand1 / operand2 : 0; // Handle division by zero
////////                if (operand2 == 0) System.out.println("Error: Division by zero");
////////                break;
////////        }
////////        memory.assign(args[0].charAt(0), result);
////////    }
//////
//////
////////    private void executeInstruction(Instruction instruction) {
////////        String command = instruction.getCommand();
////////        String[] args = instruction.getArgs();
////////
////////        switch (command) {
////////            case "assign":
////////                if (args[1].equals("input")) {
////////                        System.out.print("Enter value for " + args[0] + ": ");
////////                        Scanner scanner = new Scanner(System.in);
////////                        int value = scanner.nextInt();
////////                        memory.assign(args[0].charAt(0), value);
////////                } else {
////////                    memory.assign(args[0].charAt(0), Integer.parseInt(args[1]));
////////                }
////////                break;
////////            case "add":
////////            case "subtract":
////////            case "multiply":
////////            case "divide":
////////                performArithmetic(command, args);
////////                break;
////////            case "print":
////////                System.out.println(args[0] + " = " + memory.get(args[0].charAt(0)));
////////                break;
////////        }
////////    }
////////
////////    private void performArithmetic(String command, String[] args) {
////////        int operand1 = memory.get(args[1].charAt(0));
////////        int operand2 = memory.get(args[2].charAt(0));
////////        int result = 0;
////////
////////        switch (command) {
////////            case "add":
////////                result = operand1 + operand2;
////////                break;
////////            case "subtract":
////////                result = operand1 - operand2;
////////                break;
////////            case "multiply":
////////                result = operand1 * operand2;
////////                break;
////////            case "divide":
////////                if (operand2 == 0) {
////////                    System.out.println("Error: Division by zero");
////////                    return;
////////                }
////////                result = operand1 / operand2;
////////                break;
////////        }
////////        memory.assign(args[0].charAt(0), result);
////////        System.out.println("Result of " + command + " (" + args[1] + ", " + args[2] + ") = " + result);
////////    }
////////
//////
//////
////////    private void executeInstruction(Instruction instruction) {
////////        String command = instruction.getCommand();
////////        String[] args = instruction.getArgs();
////////
////////        try {
////////            switch (command) {
////////                case "assign":
////////                    if (args[1].equals("input")) {
////////                        System.out.print("Enter value for " + args[0] + ": ");
////////                        Scanner scanner = new Scanner(System.in);
////////                        int value = scanner.nextInt();
////////                        memory.assign(args[0].charAt(0), value);
////////                    } else {
////////                        // Normal assignment
////////                        char var = args[0].charAt(0);
////////                        int value = Integer.parseInt(args[1]);
////////                        memory.assign(var, value);
////////                        System.out.println("Assigned " + var + " = " + value);
////////                    }
////////                    break;
////////                case "add":
////////                    int addResult = memory.get(args[1].charAt(0)) + memory.get(args[2].charAt(0));
////////                    memory.assign(args[0].charAt(0), addResult);
////////                    System.out.println("Result of addition: " + args[0] + " = " + addResult);
////////                    break;
////////                case "subtract":
////////                    int subtractResult = memory.get(args[1].charAt(0)) - memory.get(args[2].charAt(0));
////////                    memory.assign(args[0].charAt(0), subtractResult);
////////                    System.out.println("Result of subtraction: " + args[0] + " = " + subtractResult);
////////                    break;
////////                case "multiply":
////////                    int multiplyResult = memory.get(args[1].charAt(0)) * memory.get(args[2].charAt(0));
////////                    memory.assign(args[0].charAt(0), multiplyResult);
////////                    System.out.println("Result of multiplication: " + args[0] + " = " + multiplyResult);
////////                    break;
////////                case "divide":
////////                    if (memory.get(args[2].charAt(0)) == 0) {
////////                        System.err.println("Division by zero error.");
////////                    } else {
////////                        int divideResult = memory.get(args[1].charAt(0)) / memory.get(args[2].charAt(0));
////////                        memory.assign(args[0].charAt(0), divideResult);
////////                        System.out.println("Result of division: " + args[0] + " = " + divideResult);
////////                    }
////////                    break;
////////                case "print":
////////                    System.out.println("Output: " + args[0] + " = " + memory.get(args[0].charAt(0)));
////////                    break;
////////                default:
////////                    throw new IllegalArgumentException("Unsupported command: " + command);
////////            }
////////        } catch (Exception e) {
////////            System.err.println("Error executing instruction: " + e.getMessage());
////////        }
////////    }
//////
//////}
//////
//////
//////
//////
