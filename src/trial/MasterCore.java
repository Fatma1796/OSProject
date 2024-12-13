// MasterCore.java
package trial;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class MasterCore {
    private final HashMap<Integer, Integer> aValuesMap = new HashMap<>();
    private final HashMap<Integer, Integer> bValuesMap = new HashMap<>();
    private final HashMap<Character, Integer> otherValuesMap = new HashMap<>();
    private final Queue<Process> readyQueue = new LinkedList<>();
    private final SlaveCore[] slaveCores;

    public MasterCore(int coreCount) {
        slaveCores = new SlaveCore[coreCount];
        for (int i = 0; i < coreCount; i++) {
            slaveCores[i] = new SlaveCore(i, this);
            slaveCores[i].start();
        }
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public void startScheduling() {
        while (!readyQueue.isEmpty()) {
            for (SlaveCore core : slaveCores) {
                if (core.isIdle() && !readyQueue.isEmpty()) {
                    core.assignProcess(readyQueue.poll(), 2); // Example quantum value
                }
            }
        }
    }

    public void updateValueMap(char variable, int value, Process process) {
        if (variable == 'a') {
            aValuesMap.put(process.getProcessId(), value);
        } else if (variable == 'b') {
            bValuesMap.put(process.getProcessId(), value);
        } else {
            otherValuesMap.put(variable, value);
        }
        printValueMaps();
    }

    public Integer getAValueByProcessId(int processId) {
        return aValuesMap.get(processId);
    }

    public Integer getBValueByProcessId(int processId) {
        return bValuesMap.get(processId);
    }

    public Integer getValue(char variable) {
        if (variable == 'a') {
            return aValuesMap.values().stream().findFirst().orElse(null);
        } else if (variable == 'b') {
            return bValuesMap.values().stream().findFirst().orElse(null);
        } else {
            return otherValuesMap.get(variable);
        }
    }

    private void printValueMaps() {
        System.out.println("aValuesMap: " + aValuesMap);
        System.out.println("bValuesMap: " + bValuesMap);
        System.out.println("otherValuesMap: " + otherValuesMap);
    }

    public void printReadyQueue() {
        System.out.println("Ready Queue:");
        for (Process process : readyQueue) {
            System.out.println(process);
        }
    }
}








//package trial;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Queue;
//
//public class MasterCore {
//    private final HashMap<Integer, Integer> aValuesMap = new HashMap<>();
//    private final HashMap<Integer, Integer> bValuesMap = new HashMap<>();
//    private final Queue<Process> readyQueue = new LinkedList<>();
//    private final SlaveCore[] slaveCores;
//
//    public MasterCore(int coreCount) {
//        slaveCores = new SlaveCore[coreCount];
//        for (int i = 0; i < coreCount; i++) {
//            slaveCores[i] = new SlaveCore(i, this);
//            slaveCores[i].start();
//        }
//    }
//
//    public void addProcess(Process process) {
//        readyQueue.add(process);
//    }
//
//    public Queue<Process> getReadyQueue() {
//        return readyQueue;
//    }
//
//    public void startScheduling() {
//        while (!readyQueue.isEmpty()) {
//            for (SlaveCore core : slaveCores) {
//                if (core.isIdle() && !readyQueue.isEmpty()) {
//                    core.assignProcess(readyQueue.poll(), 2); // Example quantum value
//                }
//            }
//        }
//    }
//
//    public void updateValueMap(char variable, int value, Process process) {
//        if (variable == 'a') {
//            aValuesMap.put(process.getProcessId(), value);
//        } else if (variable == 'b') {
//            bValuesMap.put(process.getProcessId(), value);
//        }
//        printValueMaps();
//    }
//
//    public Integer getAValueByProcessId(int processId) {
//        return aValuesMap.get(processId);
//    }
//
//    public Integer getBValueByProcessId(int processId) {
//        return bValuesMap.get(processId);
//    }
//
//    public Integer getValue(char variable) {
//        if (variable == 'a') {
//            return aValuesMap.values().stream().findFirst().orElse(null);
//        } else if (variable == 'b') {
//            return bValuesMap.values().stream().findFirst().orElse(null);
//        }
//        return null;
//    }
//
//    private void printValueMaps() {
//        System.out.println("aValuesMap: " + aValuesMap);
//        System.out.println("bValuesMap: " + bValuesMap);
//    }
//
//        public void printReadyQueue() {
//        System.out.println("Ready Queue:");
//        for (Process process : readyQueue) {
//            System.out.println(process);
//        }
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
//
////// MasterCore.java
////package trial;
////
////import java.util.HashMap;
////import java.util.LinkedList;
////import java.util.Queue;
////
////public class MasterCore {
////    private final SharedMemory sharedMemory;
////    private final int coreCount;
////    private final Queue<Process> readyQueue;
////    private final HashMap<Integer, Integer> aValuesMap;
////    private final HashMap<Integer, Integer> bValuesMap;
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
//////    public void updateValueMap(char variable, int value, Process process) {
//////        if (variable == 'a') {
//////            aValuesMap.put(process.getProcessId(), value);
//////        } else if (variable == 'b') {
//////            bValuesMap.put(process.getProcessId(), value);
//////        }
//////    }
////
////    // MasterCore.java
////    // MasterCore.java
////    public void updateValueMap(char variable, int value, Process process) {
////        if (variable == 'a') {
////            aValuesMap.put(process.getProcessId(), value);
////        } else if (variable == 'b') {
////            bValuesMap.put(process.getProcessId(), value);
////        }
////        printValueMaps();
////    }
////
////    private void printValueMaps() {
////        System.out.println("aValuesMap: " + aValuesMap);
////        System.out.println("bValuesMap: " + bValuesMap);
////    }
////
////    public Integer getAValueByProcessId(int processId) {
////        return aValuesMap.get(processId);
////    }
////
////    public Integer getBValueByProcessId(int processId) {
////        return bValuesMap.get(processId);
////    }
////
////    public Queue<Process> getReadyQueue() {
////        return readyQueue;
////    }
////}
////
////
////
////
////
////
////
////
////
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
//////        while (!readyQueue.isEmpty() || anyCoreBusy()) {
//////            for (SlaveCore core : slaveCores) {
//////                if (core.isIdle() && !readyQueue.isEmpty()) {
//////                    Process process = readyQueue.poll();
//////                    core.assignProcess(process, 5); // Assign a quantum of 5 for example
//////                }
//////            }
//////        }
//////        for (SlaveCore core : slaveCores) {
//////            core.terminate();
//////        }
//////    }
//////
//////    private boolean anyCoreBusy() {
//////        for (SlaveCore core : slaveCores) {
//////            if (!core.isIdle()) {
//////                return true;
//////            }
//////        }
//////        return false;
//////    }
//////
//////    public void updateValueMap(char variable, int value, Process process) {
//////        if (variable == 'a') {
//////            aValuesMap.put(process.getProcessId(), value);
//////        } else if (variable == 'b') {
//////            bValuesMap.put(value, process);
//////        }
//////    }
//////
//////    public Integer getAValueByProcessId(int processId) {
//////        return aValuesMap.get(processId);
//////    }
//////
//////    public Integer getBValueByProcessId(int processId) {
//////        return bValuesMap.get(processId);
//////    }
//////
//////    public Process getProcessByBValue(int value) {
//////        return bValuesMap.get(value);
//////    }
//////
//////    public Queue<Process> getReadyQueue() {
//////        return readyQueue;
//////    }
//////}
//////
//////
//////
//////
//////
//////
////////import java.util.HashMap;
////////import java.util.LinkedList;
////////import java.util.Queue;
////////
////////public class MasterCore {
////////    private final SharedMemory sharedMemory;
////////    private final int coreCount;
////////    private final Queue<Process> readyQueue;
////////    private final HashMap<Integer, Integer> aValuesMap;
////////    private final HashMap<Integer, Integer> bValuesMap;
////////    private final SlaveCore[] slaveCores;
////////
////////    public MasterCore(SharedMemory sharedMemory, int coreCount) {
////////        this.sharedMemory = sharedMemory;
////////        this.coreCount = coreCount;
////////        this.readyQueue = new LinkedList<>();
////////        this.aValuesMap = new HashMap<>();
////////        this.bValuesMap = new HashMap<>();
////////        this.slaveCores = new SlaveCore[coreCount];
////////        for (int i = 0; i < coreCount; i++) {
////////            slaveCores[i] = new SlaveCore(i, sharedMemory, this);
////////            slaveCores[i].start();
////////        }
////////    }
////////
////////    public void addProcess(Process process) {
////////        readyQueue.add(process);
////////    }
////////
////////    public void printReadyQueue() {
////////        System.out.println("Ready Queue:");
////////        for (Process process : readyQueue) {
////////            System.out.println(process);
////////        }
////////    }
////////
////////    public void startScheduling() {
////////        while (!readyQueue.isEmpty() || anyCoreBusy()) {
////////            for (SlaveCore core : slaveCores) {
////////                if (core.isIdle() && !readyQueue.isEmpty()) {
////////                    Process process = readyQueue.poll();
////////                    core.assignProcess(process, 5); // Assign a quantum of 5 for example
////////                }
////////            }
////////        }
////////        for (SlaveCore core : slaveCores) {
////////            core.terminate();
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
////////    public void updateValueMap(char variable, int value, Process process) {
////////        if (variable == 'a') {
////////            aValuesMap.put(value, process);
////////        } else if (variable == 'b') {
////////            bValuesMap.put(value, process);
////////        }
////////    }
////////
////////    public Process getProcessByAValue(int value) {
////////        return aValuesMap.get(value);
////////    }
////////
////////    public Process getProcessByBValue(int value) {
////////        return bValuesMap.get(value);
////////    }
////////
////////    public Queue<Process> getReadyQueue() {
////////        return readyQueue;
////////    }
////////}
////////
////////
////////
////////
////////
////////
////////
////////
////////
