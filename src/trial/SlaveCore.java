package trial;

public class SlaveCore extends Thread {
    private final int coreId;
    private final SharedMemory memory;
    private Process currentProcess;
    private volatile boolean isIdle = true;
    private volatile boolean terminate = false;
    private int quantum; // Quantum for the current process

    public SlaveCore(int coreId, SharedMemory memory) {
        this.coreId = coreId;
        this.memory = memory;
    }

    public synchronized void terminate() {
        terminate = true;
        notify();
    }

    public synchronized void assignProcess(Process process, int quantum) {
        currentProcess = process;
        this.quantum = quantum;
        currentProcess.setQuantum(quantum);
        isIdle = false;
        notify();
    }

    public boolean isIdle() {
        return isIdle;
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
                isIdle = true;
            }
        }
        System.out.println("Core " + coreId + " terminated.");
    }

    private void executeCurrentProcess() {
        int executedInstructions = 0;
        while (currentProcess.hasNextInstruction() && executedInstructions < quantum) {
            Instruction instruction = currentProcess.getNextInstruction();
            executeInstruction(instruction);
            executedInstructions++;
        }
        if (currentProcess.hasNextInstruction()) {
            System.out.println("Core " + coreId + " quantum expired for Process " + currentProcess.getProcessId());
        } else {
            System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
            currentProcess = null; // Clear the process only if it's finished
        }
    }

    public int getCoreId() {
        return coreId;
    }

//    private void executeInstruction(Instruction instruction) {
//        String command = instruction.getCommand();
//        String[] args = instruction.getArgs();
//
//        try {
//            switch (command) {
//                case "assign":
//                    char var = args[0].charAt(0);
//                    int value = Integer.parseInt(args[1]);
//                    memory.assign(var, value);
//                    System.out.println("Core " + coreId + " assigned " + var + " = " + value + " (Process " + currentProcess.getProcessId() + ")");
//                    break;
//                case "print":
//                    System.out.println("Core " + coreId + " Output: " + args[0] + " = " + memory.get(args[0].charAt(0)));
//                    break;
//                default:
//                    throw new IllegalArgumentException("Unknown command: " + command);
//            }
//        } catch (Exception e) {
//            System.err.println("Core " + coreId + " Error: " + e.getMessage());
//        }
//    }

    private void executeInstruction(Instruction instruction) {
        String command = instruction.getCommand();
        String[] args = instruction.getArgs();

        try {
            switch (command) {
                case "assign":
                    char var = args[0].charAt(0);
                    if (args.length == 3) { // This means there is an operation involved
                        int operand1 = memory.get(args[1].charAt(0));
                        int operand2 = memory.get(args[2].charAt(0));
                        int result;

                        switch (args[1]) {
                            case "add":
                                result = operand1 + operand2;
                                break;
                            case "subtract":
                                result = operand1 - operand2;
                                break;
                            case "multiply":
                                result = operand1 * operand2;
                                break;
                            case "divide":
                                result = operand1 / operand2;
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported operation: " + args[1]);
                        }

                        memory.assign(var, result);
                        System.out.println("Core " + coreId + " " + args[1] + " " + operand1 + " and " + operand2 + ", assigned " + var + " = " + result + " (Process " + currentProcess.getProcessId() + ")");
                    } else { // No operation, just assignment
                        int value = Integer.parseInt(args[1]);
                        memory.assign(var, value);
                        System.out.println("Core " + coreId + " assigned " + var + " = " + value + " (Process " + currentProcess.getProcessId() + ")");
                    }
                    break;
                case "print":
                    System.out.println("Core " + coreId + " Output: " + args[0] + " = " + memory.get(args[0].charAt(0)));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + command);
            }
        } catch (Exception e) {
            System.err.println("Core " + coreId + " Error: " + e.getMessage());
        }
    }
    }




