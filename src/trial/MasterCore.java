package trial;

import java.util.Queue;
import java.util.LinkedList;

public class MasterCore {
    private final Queue<Process> readyQueue;
    private final SlaveCore[] slaveCores;
    private final SharedMemory memory;
    private final int quantum = 2;
    private int cycleCount = 0;

//    public MasterCore(SharedMemory memory, int numCores) {
//        this.memory = memory;
//        this.readyQueue = new LinkedList<>();
//        this.slaveCores = new SlaveCore[numCores];
//        for (int i = 0; i < numCores; i++) {
//            slaveCores[i] = new SlaveCore(i + 1, memory);
//            slaveCores[i].start();
//        }
//    }

//    public MasterCore(int numCores, SharedMemory memory) {
//        this.memory = memory; // Assign the passed memory to the final variable
//        this.readyQueue = new LinkedList<>(); // Initialize the readyQueue
//        this.slaveCores = new SlaveCore[numCores]; // Initialize slave cores array
//
//        for (int i = 0; i < numCores; i++) {
//            slaveCores[i] = new SlaveCore(i + 1, this.memory); // Pass this.memory to ensure all cores use the same memory
//            slaveCores[i].start(); // Start each core's thread
//        }
//    }

    public MasterCore(SharedMemory memory, int numCores) {
        this.memory = memory;
        this.readyQueue = new LinkedList<>();
        this.slaveCores = new SlaveCore[numCores];
        for (int i = 0; i < numCores; i++) {
            slaveCores[i] = new SlaveCore(i + 1, this.memory);
            slaveCores[i].start();
        }
    }


//    public void terminateAllCores() {
//        // Send a termination signal to each core
//        for (SlaveCore core : slaveCores) {
//            core.terminate();
//        }
//        // Wait for each core to finish
//        for (SlaveCore core : slaveCores) {
//            try {
//                core.join(); // Ensure each core has fully terminated
//            } catch (InterruptedException e) {
//                System.err.println("Error while waiting for core " + core.getCoreId() + " to terminate.");
//            }
//        }
//    }

    public void terminateAllCores() {
        for (SlaveCore core : slaveCores) {
            core.terminate();
        }
        for (SlaveCore core : slaveCores) {
            try {
                core.join();
            } catch (InterruptedException e) {
                System.err.println("Interrupted while waiting for core to finish: " + core.getCoreId());
            }
        }
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    private void sleepAndManageCycles() {
        try {
            Thread.sleep(1000); // Simulate the clock cycle with 1 second of sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("MasterCore scheduling was interrupted.");
        }
    }

//    private void sleepAndManageCycles() {
//        try {
//            Thread.sleep(1000); // Simulate the clock cycle with 1 second of sleep
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // Properly handle the interrupt status
//            System.err.println("MasterCore scheduling was interrupted.");
//        }
//    }

//    public void startScheduling() {
//        int cycleCount = 0;
//        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//            for (SlaveCore core : slaveCores) {
//                if (core.isIdle() && !readyQueue.isEmpty()) {
//                    Process process = readyQueue.poll();
//                    if (process != null) {
//                        core.assignProcess(process, quantum);
//                    }
//                }
//            }
//
//            if (++cycleCount % 2 == 0) {
//                printReadyQueue();
//                redistributeProcesses();
//            }
//
//            sleepAndManageCycles(); // Handles sleep and cycle management
//        }
//
//        terminateAllCores();
//    }

//    private void assignProcessesToCores() {
//        for (SlaveCore core : slaveCores) {
//            if (core.isIdle() && !readyQueue.isEmpty()) {
//                Process process = readyQueue.poll();  // Retrieve and remove the next process
//                if (process != null) {
//                    core.assignProcess(process, quantum);  // Assign the process with a predefined quantum
//                }
//            }
//        }
//    }

//    private void assignProcessesToCores() {
//        for (SlaveCore core : slaveCores) {
//            if (core.isIdle() && !readyQueue.isEmpty()) {
//                Process process = readyQueue.poll();
//                if (process != null && !process.isComplete()) {
//                    core.assignProcess(process, quantum);
//                }
//            }
//        }
//    }

    private void assignProcessesToCores() {
        for (SlaveCore core : slaveCores) {
            if (core.isIdle() && !readyQueue.isEmpty()) {
                Process process = readyQueue.poll();
                if (process != null) {
                    core.assignProcess(process, 1); // Set quantum to 1 for one instruction per cycle
                }
            }
        }
    }

    private void redistributeProcesses() {
        for (SlaveCore core : slaveCores) {
            if (core.isIdle() && !core.getCurrentProcess().isComplete()) {
                readyQueue.add(core.getCurrentProcess());
                core.makeIdle();
            }
        }
    }


//    public void startScheduling() {
//        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//            assignProcessesToCores();
//            if (++cycleCount % 2 == 0) {
//                printReadyQueue();
//            }
//            sleepAndManageCycles();
//        }
//        terminateAllCores();
//        System.out.println("All processes completed and system shutting down.");
//    }

//    public void startScheduling() {
//        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//            assignProcessesToCores();
//            sleepAndManageCycles();
//        }
//        terminateAllCores();
//        System.out.println("All processes completed and system shutting down.");
//    }

    public void startScheduling() {
        int cycleCount = 0; // Initialize cycle counter
        while (!readyQueue.isEmpty() || anyCoreBusy()) {
            assignProcessesToCores();
            sleepAndManageCycles();
            if (++cycleCount % 2 == 0) { // Check if it's time to print the ready queue
                printReadyQueue();
            }
        }
        terminateAllCores();
        System.out.println("All processes completed and system shutting down.");
    }


    private boolean anyCoreBusy() {
        for (SlaveCore core : slaveCores) {
            if (!core.isIdle()) {
                return true; // Check if any core is still busy
            }
        }
        return false;
    }

//    public void startScheduling() {
//        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//            assignProcessesToCores();
//            if (++cycleCount % 2 == 0) {
//                printReadyQueue();
//                redistributeProcesses();
//            }
//            sleepAndManageCycles();
//        }
//        terminateAllCores();
//        System.out.println("All processes completed.");
//    }
//
//    private void redistributeProcesses() {
//        for (SlaveCore core : slaveCores) {
//            if (!core.isIdle()) {
//                Process currentProcess = core.getCurrentProcess();
//                if (currentProcess != null && currentProcess.hasNextInstruction()) {
//                    readyQueue.add(currentProcess);
//                    core.makeIdle();
//                }
//            }
//        }
//    }
//    private boolean anyCoreBusy() {
//        for (SlaveCore core : slaveCores) {
//            if (!core.isIdle()) {
//                return true;
//            }
//        }
//        return false;
//    }



//    public void printReadyQueue() {
//        synchronized (readyQueue) {
//            System.out.print("Ready Queue: ");
//            System.out.println();
//            for (Process process : readyQueue) {
//                String nextInstruction = process.hasNextInstruction() ? process.getInstructions() .peek().toString() : "None";
//                System.out.print("Process " + process.getProcessId() + " [Next: " + nextInstruction + "] ");
//                System.out.println();
//                //System.out.println(process.getInstructions() .peek().toString());
//
//            }
//        }
//    }

//    public void printReadyQueue() {
//        synchronized (readyQueue) {
//            System.out.print("Ready Queue: ");
//            for (Process process : readyQueue) {
//                String nextInstruction = process.hasNextInstruction() ? process.getNextInstruction().toString() : "None";
//                System.out.print("Process " + process.getProcessId() + " [Next: " + nextInstruction + "] ");
//            }
//            System.out.println();
//        }
//    }

    public void printReadyQueue() {
        System.out.print("Ready Queue: ");
        for (Process process : readyQueue) {
            String nextInstruction = process.hasNextInstruction() ? process.getNextInstruction().toString() : "None";
            System.out.print("Process " + process.getProcessId() + " [Next: " + nextInstruction + "] ");
        }
        System.out.println();
    }
}

