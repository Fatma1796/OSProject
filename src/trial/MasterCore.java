// MasterCore.java
package trial;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MasterCore {
    private final SharedMemory sharedMemory;
    private final int coreCount;
    private final Queue<Process> readyQueue;
    private final HashMap<Integer, Process> aValuesMap;
    private final HashMap<Integer, Process> bValuesMap;
    private final SlaveCore[] slaveCores;

    public MasterCore(SharedMemory sharedMemory, int coreCount) {
        this.sharedMemory = sharedMemory;
        this.coreCount = coreCount;
        this.readyQueue = new LinkedList<>();
        this.aValuesMap = new HashMap<>();
        this.bValuesMap = new HashMap<>();
        this.slaveCores = new SlaveCore[coreCount];
        for (int i = 0; i < coreCount; i++) {
            slaveCores[i] = new SlaveCore(i, sharedMemory, this);
            slaveCores[i].start();
        }
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public void printReadyQueue() {
        System.out.println("Ready Queue:");
        for (Process process : readyQueue) {
            System.out.println(process);
        }
    }

    public void startScheduling() {
        while (!readyQueue.isEmpty() || anyCoreBusy()) {
            for (SlaveCore core : slaveCores) {
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    Process process = readyQueue.poll();
                    core.assignProcess(process, 5); // Assign a quantum of 5 for example
                }
            }
        }
        for (SlaveCore core : slaveCores) {
            core.terminate();
        }
    }

    private boolean anyCoreBusy() {
        for (SlaveCore core : slaveCores) {
            if (!core.isIdle()) {
                return true;
            }
        }
        return false;
    }

    public void updateValueMap(char variable, int value, Process process) {
        if (variable == 'a') {
            aValuesMap.put(value, process);
        } else if (variable == 'b') {
            bValuesMap.put(value, process);
        }
    }

    public Process getProcessByAValue(int value) {
        return aValuesMap.get(value);
    }

    public Process getProcessByBValue(int value) {
        return bValuesMap.get(value);
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }
}









//// MasterCore.java
//package trial;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Queue;
//
//public class MasterCore {
//    private final SharedMemory sharedMemory;
//    private final int coreCount;
//    private final Queue<Process> readyQueue;
//    private final HashMap<Integer, Process> aValuesMap;
//    private final HashMap<Integer, Process> bValuesMap;
//    private final SlaveCore[] slaveCores;
//
//    public MasterCore(SharedMemory sharedMemory, int coreCount) {
//        this.sharedMemory = sharedMemory;
//        this.coreCount = coreCount;
//        this.readyQueue = new LinkedList<>();
//        this.aValuesMap = new HashMap<>();
//        this.bValuesMap = new HashMap<>();
//        this.slaveCores = new SlaveCore[coreCount];
//        for (int i = 0; i < coreCount; i++) {
//            slaveCores[i] = new SlaveCore(i, sharedMemory, this);
//            slaveCores[i].start();
//        }
//    }
//
//    public void addProcess(Process process) {
//        readyQueue.add(process);
//    }
//
//    public void printReadyQueue() {
//        System.out.println("Ready Queue:");
//        for (Process process : readyQueue) {
//            System.out.println(process);
//        }
//    }
//
//    public void startScheduling() {
//        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//            for (SlaveCore core : slaveCores) {
//                if (core.isIdle() && !readyQueue.isEmpty()) {
//                    Process process = readyQueue.poll();
//                    core.assignProcess(process, 5); // Assign a quantum of 5 for example
//                }
//            }
//        }
//        for (SlaveCore core : slaveCores) {
//            core.terminate();
//        }
//    }
//
////    public void startScheduling() {
////        while (!readyQueue.isEmpty() || anyCoreBusy()) {
////            for (SlaveCore core : slaveCores) {
////                if (core.isIdle() && !readyQueue.isEmpty()) {
////                    Process process = readyQueue.poll();
////                    core.assignProcess(process, 5); // Assign a quantum of 5 for example
////                }
////            }
////        }
////        for (SlaveCore core : slaveCores) {
////            core.terminate();
////        }
////    }
//
//    private boolean anyCoreBusy() {
//        for (SlaveCore core : slaveCores) {
//            if (!core.isIdle()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void updateValueMap(char variable, int value, Process process) {
//        if (variable == 'a') {
//            aValuesMap.put(value, process);
//        } else if (variable == 'b') {
//            bValuesMap.put(value, process);
//        }
//    }
//
//    public Process getProcessByAValue(int value) {
//        return aValuesMap.get(value);
//    }
//
//    public Process getProcessByBValue(int value) {
//        return bValuesMap.get(value);
//    }
//
//    public Queue<Process> getReadyQueue() {
//        return readyQueue;
//    }
//}
//
//
//
//
//
//
//
//
////// MasterCore.java
////package trial;
////
////import java.util.HashMap;
////import java.util.Queue;
////import java.util.LinkedList;
////
////public class MasterCore {
////    private final SharedMemory sharedMemory;
////    private final int coreCount;
////    private final Queue<Process> readyQueue;
////    private final HashMap<Integer, Process> aValuesMap;
////    private final HashMap<Integer, Process> bValuesMap;
////    private final SlaveCore[] slaveCores;
////
////    public MasterCore(SharedMemory sharedMemory, int coreCount) {
////        this.sharedMemory = sharedMemory;
////        this.coreCount = coreCount;
////        this.readyQueue = new LinkedList<>();
////        this.aValuesMap = new HashMap<>();
////        this.bValuesMap = new HashMap<>();
////        this.slaveCores = new SlaveCore[coreCount];
////        for (int i = 0; i < coreCount; i++) {
////            slaveCores[i] = new SlaveCore(i, sharedMemory, this);
////            slaveCores[i].start();
////        }
////    }
////
////    public void addProcess(Process process) {
////        readyQueue.add(process);
////    }
////
////    public void printReadyQueue() {
////        System.out.println("Ready Queue:");
////        for (Process process : readyQueue) {
////            System.out.println(process);
////        }
////    }
////
////    public Queue<Process> getReadyQueue() {
////        return readyQueue;
////    }
////
////    public void startScheduling() {
////        while (!readyQueue.isEmpty() || anyCoreBusy()) {
////            for (SlaveCore core : slaveCores) {
////                if (core.isIdle() && !readyQueue.isEmpty()) {
////                    Process process = readyQueue.poll();
////                    core.assignProcess(process, 2); // Assign a quantum of 2 for example
////                }
////            }
////        }
////        for (SlaveCore core : slaveCores) {
////            core.terminate();
////        }
////    }
////
////    private boolean anyCoreBusy() {
////        for (SlaveCore core : slaveCores) {
////            if (!core.isIdle()) {
////                return true;
////            }
////        }
////        return false;
////    }
////
////    public void updateValueMap(char variable, int value, Process process) {
////        if (variable == 'a') {
////            aValuesMap.put(value, process);
////        } else if (variable == 'b') {
////            bValuesMap.put(value, process);
////        }
////    }
////
////    public Process getProcessByAValue(int value) {
////        return aValuesMap.get(value);
////    }
////
////    public Process getProcessByBValue(int value) {
////        return bValuesMap.get(value);
////    }
////}
////
////
////
////
////
////
//////// MasterCore.java
//////package trial;
//////
//////import java.util.HashMap;
//////import java.util.Queue;
//////import java.util.LinkedList;
//////
//////public class MasterCore {
//////    private final SharedMemory sharedMemory;
//////    private final int coreCount;
//////    private final Queue<Process> readyQueue;
//////    private final HashMap<Integer, Process> aValuesMap;
//////    private final HashMap<Integer, Process> bValuesMap;
//////
//////    public MasterCore(SharedMemory sharedMemory, int coreCount) {
//////        this.sharedMemory = sharedMemory;
//////        this.coreCount = coreCount;
//////        this.readyQueue = new LinkedList<>();
//////        this.aValuesMap = new HashMap<>();
//////        this.bValuesMap = new HashMap<>();
//////    }
//////
//////    public void addProcess(Process process) {
//////        readyQueue.add(process);
//////    }
//////
//////    public void printReadyQueue() {
//////        System.out.println("Ready Queue:");
//////        for (Process process : readyQueue) {
//////            System.out.println(process);
//////        }
//////    }
//////
//////    public void startScheduling() {
//////        // Implement scheduling logic here
//////    }
//////
//////    public void updateValueMap(char variable, int value, Process process) {
//////        if (variable == 'a') {
//////            aValuesMap.put(value, process);
//////        } else if (variable == 'b') {
//////            bValuesMap.put(value, process);
//////        }
//////    }
//////
//////    public Process getProcessByAValue(int value) {
//////        return aValuesMap.get(value);
//////    }
//////
//////    public Process getProcessByBValue(int value) {
//////        return bValuesMap.get(value);
//////    }
//////}
//////
//////
//////
//////
//////
//////
////////
////////package trial;
////////
////////import java.util.Queue;
////////import java.util.LinkedList;
////////
////////public class MasterCore {
////////    private final Queue<Process> readyQueue;
////////    private final SlaveCore[] slaveCores;
////////    private final SharedMemory memory;
////////    private final int quantum = 2;
////////
////////    public MasterCore(SharedMemory memory, int numCores) {
////////        this.memory = memory;
////////        this.readyQueue = new LinkedList<>();
////////        this.slaveCores = new SlaveCore[numCores];
////////        for (int i = 0; i < numCores; i++) {
////////            slaveCores[i] = new SlaveCore(i + 1, this.memory);
////////            slaveCores[i].start();
////////        }
////////    }
////////
////////    public void terminateAllCores() {
////////        for (SlaveCore core : slaveCores) {
////////            core.terminate();
////////        }
////////        for (SlaveCore core : slaveCores) {
////////            try {
////////                core.join();
////////            } catch (InterruptedException e) {
////////                System.err.println("Interrupted while waiting for core to finish: " + core.getCoreId());
////////            }
////////        }
////////    }
////////
////////    public void addProcess(Process process) {
////////        readyQueue.add(process);
////////    }
////////
////////    public void startScheduling() {
////////        while (!readyQueue.isEmpty() || anyCoreBusy()) {
////////            assignProcessesToCores();
////////            sleepAndManageCycles();
////////            printReadyQueue();
////////        }
////////        terminateAllCores();
////////        System.out.println("All processes completed and system shutting down.");
////////    }
////////
////////    private void assignProcessesToCores() {
////////        for (SlaveCore core : slaveCores) {
////////            if (core.isIdle() && !readyQueue.isEmpty()) {
////////                Process process = readyQueue.poll();
////////                if (process != null) {
////////                    core.assignProcess(process, quantum);
////////                }
////////            }
////////        }
////////    }
////////
////////    private boolean anyCoreBusy() {
////////        for (SlaveCore core : slaveCores) {
////////            if (!core.isIdle()) {
////////                return true;
////////            }
////////        }
////////        return false;
////////    }
////////
////////    private void sleepAndManageCycles() {
////////        try {
////////            Thread.sleep(1000); // Simulate clock cycle
////////        } catch (InterruptedException e) {
////////            Thread.currentThread().interrupt();
////////            System.err.println("MasterCore scheduling was interrupted.");
////////        }
////////    }
////////
////////    public void printReadyQueue() {
////////        System.out.print("Ready Queue: ");
////////        for (Process process : readyQueue) {
////////            String nextInstruction = process.hasNextInstruction() ? process.getInstructions().peek().toString() : "None";
////////            System.out.print("Process " + process.getProcessId() + " [Next: " + nextInstruction + "] ");
////////        }
////////        System.out.println();
////////    }
////////}
