package trial;

import java.util.Queue;
import java.util.LinkedList;

public class MasterCore {
    private final Queue<Process> readyQueue;
    private final SlaveCore[] slaveCores;
    private final SharedMemory memory;

    public MasterCore(SharedMemory memory, int numCores) {
        this.memory = memory;
        this.readyQueue = new LinkedList<>();
        this.slaveCores = new SlaveCore[numCores];
        for (int i = 0; i < numCores; i++) {
            slaveCores[i] = new SlaveCore(i + 1, memory);
            slaveCores[i].start();
        }
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public void startScheduling() {
        while (!readyQueue.isEmpty() || anyCoreBusy()) {
            for (SlaveCore core : slaveCores) {
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    synchronized (readyQueue) {
                        Process process = readyQueue.poll();
                        if (process != null) {
                            System.out.println("Master assigned Process " + process.getProcessId() + " to Core " + core.getCoreId());
                            core.assignProcess(process);
                        }
                    }
                }
            }

            printReadyQueue();
            memory.printMemoryState();

            try {
                Thread.sleep(1000); // Simulate clock cycle
            } catch (InterruptedException e) {
                System.err.println("Master scheduling interrupted.");
            }
        }

        // Terminate all slave cores after processes are complete
        for (SlaveCore core : slaveCores) {
            core.terminate();
        }

        // Wait for all slave cores to finish
        for (SlaveCore core : slaveCores) {
            try {
                core.join();
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for core " + core.getCoreId() + " to terminate.");
            }
        }
        System.out.println("All processes completed.");
    }

    private boolean anyCoreBusy() {
        for (SlaveCore core : slaveCores) {
            if (!core.isIdle()) {
                return true;
            }
        }
        return false;
    }

    private void printReadyQueue() {
        synchronized (readyQueue) {
            System.out.print("Ready Queue: ");
            for (Process process : readyQueue) {
                System.out.print("P" + process.getProcessId() + " ");
            }
            System.out.println();
        }
    }
}
