

//// SlaveCore.java
package trial;

public class SlaveCore extends Thread {
    private final int coreId;
    private final MasterCore masterCore;
    private Process currentProcess;
    private volatile boolean isIdle = true;
    private volatile boolean terminate = false;
    private int quantum;

    public SlaveCore(int coreId, MasterCore masterCore) {
        this.coreId = coreId;
        this.masterCore = masterCore;
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
            printReadyQueue();
        }

        // Return the process to the ready queue if not completed
        makeIdle();
    }

    private void printReadyQueue() {
        System.out.print("Ready Queue: ");
        for (Process process : masterCore.getReadyQueue()) {
            String nextInstruction = process.hasNextInstruction() ? process.getInstructions().peek().toString() : "None";
            System.out.print("Process " + process.getProcessId() + " [Next: " + nextInstruction + "] ");
        }
        System.out.println();
    }

    private void executeInstruction(Instruction instruction) {
        String command = instruction.getCommand();
        String[] args = instruction.getArgs();

        switch (command) {
            case "assign":
                if (args.length > 1 && args[1].equals("input")) { // Check for args length
                    handleInput(args[0]);
                } else if (args.length > 2) { // Check for arithmetic operations
                    performArithmetic(args[0], args[1], args); // Corrected call
                }
                break;
            case "add":
            case "subtract":
            case "multiply":
            case "divide":
                performArithmetic(args[0], command, args); // Corrected call for direct arithmetic
                break;
            case "print":
                System.out.println(currentProcess.getProcessId() + args[0] + " = " + getOperandValue(args[0].charAt(0)));
                break;
        }
    }

    private void performArithmetic(String targetVariable, String operation, String[] args) { // Added targetVariable
        int operand1 = getOperandValue(args[2].charAt(0)); // Correct index
        int operand2 = getOperandValue(args[3].charAt(0)); // Correct index

        System.out.println("Performing " + operation + " with values: " + operand1 + " and " + operand2);

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

        masterCore.updateValueMap(targetVariable.charAt(0), result, currentProcess); // Use targetVariable
    }

    private void handleInput(String variable) {
        System.out.print("Enter value for " + variable + ": ");
        int value = new java.util.Scanner(System.in).nextInt();
        masterCore.updateValueMap(variable.charAt(0), value, currentProcess);
    }
    private int getOperandValue(char variable) {
        Integer value;
        if (variable == 'a') {
            value = masterCore.getAValueByProcessId(currentProcess.getProcessId());
        } else if (variable == 'b') {
            value = masterCore.getBValueByProcessId(currentProcess.getProcessId());
        } else {
            value = masterCore.getValue(variable);
        }

        if (value == null) {
            System.out.println("Error: Value for variable " + variable + " is not set for process ID " + currentProcess.getProcessId());
            throw new IllegalStateException("Value for variable " + variable + " is not set for process ID " + currentProcess.getProcessId());
        }
        return value;
    }

/*
    private int getOperandValue(char variable) {
        Integer value;
        if (variable == 'a') {
            value = masterCore.getAValueByProcessId(currentProcess.getProcessId());
        } else if (variable == 'b') {
            value = masterCore.getBValueByProcessId(currentProcess.getProcessId());
        } else {
            value = masterCore.getValue(variable);
        }

        // Handle the case when the value is not set for the variable
        if (value == null) {
            System.out.println("Value for variable " + variable + " is not set for process ID " + currentProcess.getProcessId());
            // You can choose a different approach here:
            // - return a default value (e.g., 0)
            // - throw a custom exception
            return 0;
        }
        return value;
    }
*/
}

