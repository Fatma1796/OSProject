package trial;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue; // Use ConcurrentLinkedQueue


public class MasterCore {
    private final HashMap<Integer, Integer> aValuesMap = new HashMap<>();
    private final HashMap<Integer, Integer> bValuesMap = new HashMap<>();
    private final HashMap<Character, Integer> otherValuesMap = new HashMap<>();
   // private final Queue<Process> readyQueue = new LinkedList<>();
    private final SlaveCore[] slaveCores;
    private final ConcurrentLinkedQueue<Process> readyQueue = new ConcurrentLinkedQueue<>(); // Use ConcurrentLinkedQueue
    //private boolean queueChanged = false; // Add this flag

    public MasterCore(int coreCount) {
        slaveCores = new SlaveCore[coreCount];
        for (int i = 0; i < coreCount; i++) {
            slaveCores[i] = new SlaveCore(i, this);
            slaveCores[i].start();
        }
    }

    public void addProcess(Process process) {
        readyQueue.add(process);
        printReadyQueue(); // Print queue after assignment

    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }



    public void startScheduling() {
        int processIndex = 0;
        boolean queuePrinted = false;

        while (!allProcessesFinished()) {
            boolean processAssigned = false;

            if (!readyQueue.isEmpty()) {
                Process process = readyQueue.peek();
                if (process != null && !process.isComplete()) {
                    SlaveCore core = slaveCores[processIndex % slaveCores.length];
                    if (core.isIdle()) {
                        process = readyQueue.poll();
                        core.assignProcess(process, 2);
                        processAssigned = true;
                        processIndex++;
                    }
                }
            }

            if (processAssigned) {
                printReadyQueue();
                queuePrinted = true;
            } else if (readyQueue.isEmpty() && !queuePrinted && areAllCoresIdle()) { // Check for empty queue AND idle cores
                printReadyQueue();
                queuePrinted = true;
            } else if (!readyQueue.isEmpty() && !queuePrinted && areAllCoresIdle()){
                printReadyQueue();
                queuePrinted = true;
            }
            else if (!areAllCoresIdle()) {
                queuePrinted = false;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        printReadyQueue(); // Print one last time after the loop
        System.out.println("All processes finished.");
        terminateAllCores();
    }
//    public void startScheduling() {
//        int processIndex = 0;
//
//        while (!allProcessesFinished()) {
//            if (!readyQueue.isEmpty()) {
//                Process process = readyQueue.peek();
//                if (process != null && !process.isComplete()) {
//                    SlaveCore core = slaveCores[processIndex % slaveCores.length];
//                    if (core.isIdle()) {
//                        process = readyQueue.poll();
//                        core.assignProcess(process, 2);
//                        printReadyQueue(); // Print after assigning
//                        processIndex++;
//                    }
//                }
//            } else if (areAllCoresIdle()) { // Only print if queue is empty AND all cores are idle
//                printReadyQueue();
//                try {
//                    Thread.sleep(50); // Add a small delay to prevent excessive looping
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("All processes finished.");
//        terminateAllCores();
//    }
//    public void startScheduling() {
//        int processIndex = 0;
//        while (!allProcessesFinished()) {
//            Process process = readyQueue.peek(); // Peek at the next process
//            if (process != null && !process.isComplete()) {
//                SlaveCore core = slaveCores[processIndex % slaveCores.length];
//                if (core.isIdle()) {
//                    process = readyQueue.poll(); //poll the process here
//                    core.assignProcess(process, 2);
//
//                    printReadyQueue();//print the queue here after assigning a process to the core
//                    processIndex++;
//                }
//            }
//            else if(!readyQueue.isEmpty() && areAllCoresIdle()){ //if the queue is not empty and all cores are idle that mean we are waiting for a core to finish its work
//                printReadyQueue();//print the queue here
//            }
//
//            try {
//                Thread.sleep(50);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("All processes finished.");
//        terminateAllCores();
//    }

    private void terminateAllCores() { // Added this method
        for (SlaveCore core : slaveCores) {
            core.terminate();
        }
    }
    private boolean allProcessesFinished() {
        return readyQueue.isEmpty() && areAllCoresIdle();
    }

    private boolean areAllCoresIdle() {
        for (SlaveCore core : slaveCores) {
            if (!core.isIdle()) {
                return false;
            }
        }
        return true;
    }

    public void updateValueMap(char variable, int value, Process process) {
        if (variable == 'a') {
            aValuesMap.put(process.getProcessId(), value);
        } else if (variable == 'b') {
            bValuesMap.put(process.getProcessId(), value);
        } else {
            otherValuesMap.put(variable, value);
        }
    }

    public Integer getAValueByProcessId(int processId) {
        return aValuesMap.get(processId);
    }

    public Integer getBValueByProcessId(int processId) {
        return bValuesMap.get(processId);
    }

    public Integer getValue(char variable) {
        return otherValuesMap.get(variable);
    }

    public void printValuesForProcessId(int processId) {
        Integer aValue = getAValueByProcessId(processId);
        Integer bValue = getBValueByProcessId(processId);
        System.out.println("Process ID: " + processId);
        System.out.println("a: " + (aValue != null ? aValue : "Not set"));
        System.out.println("b: " + (bValue != null ? bValue : "Not set"));
        System.out.println("Other values: " + otherValuesMap);
    }

    private void printValueMaps() {
        System.out.println("aValuesMap: " + aValuesMap);
        System.out.println("bValuesMap: " + bValuesMap);
        System.out.println("otherValuesMap: " + otherValuesMap);
    }
    public void printReadyQueue() {
        System.out.print("Ready Queue: ");
        if (readyQueue.isEmpty()) {
            System.out.println("Empty");
        } else {
            Iterator<Process> iterator = readyQueue.iterator();
            while (iterator.hasNext()) {
                System.out.print(iterator.next() + (iterator.hasNext() ? " " : ""));
            }
            System.out.println();
        }
    }
//    public void printReadyQueue() {
//        if (queueChanged) { // Only print if the queue has changed
//            System.out.print("Ready Queue: ");
//            if (readyQueue.isEmpty()) {
//                System.out.println("Empty");
//            } else {
//                for (Process process : readyQueue) {
//                    System.out.print(process + " ");
//                }
//                System.out.println();
//            }
//            queueChanged = false; // Reset the flag
//        }
//    }
}


