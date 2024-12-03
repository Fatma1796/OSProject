package trial;

public class SlaveCore extends Thread {
    private final int coreId;
    private final SharedMemory memory;
    private Process currentProcess;
    private volatile boolean isIdle = true;
    private volatile boolean terminate = false;

    public SlaveCore(int coreId, SharedMemory memory) {
        this.coreId = coreId;
        this.memory = memory;
    }

    public synchronized void terminate() {
        terminate = true;
        notify();
    }

    public synchronized void assignProcess(Process process) {
        currentProcess = process;
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
        while (currentProcess.hasNextInstruction()) {
            Instruction instruction = currentProcess.getNextInstruction();
            executeInstruction(instruction);
        }
        System.out.println("Core " + coreId + " completed Process " + currentProcess.getProcessId());
        currentProcess = null;
    }

    public int getCoreId() {
        return coreId;
    }


    private void executeInstruction(Instruction instruction) {
        String command = instruction.getCommand();
        String[] args = instruction.getArgs();

        try {
            switch (command) {
                case "assign":
                    char var = args[0].charAt(0);
                    int value = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                    memory.assign(var, value);
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
