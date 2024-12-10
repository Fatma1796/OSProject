package trial;

import java.util.Queue;

/**
 * Represents a process in the system with a unique ID and a list of instructions.
 */
public class Process {
    private final int processId;
    private final Queue<Instruction> instructions;
    private final int memoryStart;
    private final int memoryEnd;
   // private int programCounter = 0;
    private int quantumRemaining; // Time slice remaining for this process
    private boolean isComplete = false;  // Flag to indicate if the process has completed


    public Process(int processId, Queue<Instruction> instructions, int memoryStart, int memoryEnd) {
        this.processId = processId;
        this.instructions = instructions;
        this.memoryStart = memoryStart;
        this.memoryEnd = memoryEnd;
        this.quantumRemaining = 0; // Initially set to 0, will be set when process is assigned to a core


    }



    public boolean isComplete() {
        return isComplete;
    }

    public int getProcessId() {
        return processId;
    }

//    public boolean hasNextInstruction() {
//        return !instructions.isEmpty();
//    }
//
//    public Instruction getNextInstruction() {
//        if (quantumRemaining > 0) {
//            quantumRemaining--;
//        }
//        return instructions.poll();
//    }

    public void markComplete() {
        this.isComplete = true; // Add this boolean field to the class
    }

    public boolean hasNextInstruction() {
        return !instructions.isEmpty();
    }

    public Instruction getNextInstruction() {
        if (!hasNextInstruction()) {
            return null; // Ensure no invalid instruction is returned
        }
        return instructions.poll();
    }

    public int getMemoryStart() {
        return memoryStart;
    }

    public int getMemoryEnd() {
        return memoryEnd;
    }

    public int getQuantumRemaining() {
        return quantumRemaining;
    }

    public void setQuantum(int quantum) {
        this.quantumRemaining = quantum;
    }

    public void decrementQuantum() {
        if (quantumRemaining > 0) {
            quantumRemaining--;
        }
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId=" + processId +
                ", memoryStart=" + memoryStart +
                ", memoryEnd=" + memoryEnd +
                '}';
    }

    public Queue<Instruction> getInstructions() {
        return instructions;
    }
}