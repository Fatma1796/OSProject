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
    private int programCounter = 0;

    public Process(int processId, Queue<Instruction> instructions, int memoryStart, int memoryEnd) {
        this.processId = processId;
        this.instructions = instructions;
        this.memoryStart = memoryStart;
        this.memoryEnd = memoryEnd;
    }

    public int getProcessId() {
        return processId;
    }

    public boolean hasNextInstruction() {
        return !instructions.isEmpty();
    }

    public Instruction getNextInstruction() {
        programCounter++;
        return instructions.poll();
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getMemoryStart() {
        return memoryStart;
    }

    public int getMemoryEnd() {
        return memoryEnd;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId=" + processId +
                ", memoryStart=" + memoryStart +
                ", memoryEnd=" + memoryEnd +
                '}';
    }
}
