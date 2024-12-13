
package trial;

import java.util.Queue;

public class Process {
    private final int processId;
    private final Queue<Instruction> instructions;
    private boolean isComplete;

    public Process(int processId, Queue<Instruction> instructions) {
        this.processId = processId;
        this.instructions = instructions;
        this.isComplete = false;
    }

    public int getProcessId() {
        return processId;
    }

    public boolean hasNextInstruction() {
        return !instructions.isEmpty();
    }

    public Instruction getNextInstruction() {
        return instructions.poll();
    }

    public Queue<Instruction> getInstructions() {
        return instructions;
    }

    public void markComplete() {
        isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }

        @Override
    public String toString() {
        return "Process{ " +
                "processId= " + processId + "[Next Instruction: "+ instructions.peek() +"] }";

    }
}








