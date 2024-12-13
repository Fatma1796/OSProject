package trial;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
        System.out.println("Ready Queue:");
        for (Process process : readyQueue) {
            System.out.println(process);
        }
    }
}


