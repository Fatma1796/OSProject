package trial;

/**
 * SharedMemory handles global memory accessible by all processes.
 */
public class SharedMemory {
    private final int[] memory = new int[26]; // Maps variables 'a' to 'z'.

    /**
     * Assigns a value to a variable in memory.
     *
     * @param variable the variable name ('a' to 'z')
     * @param value    the value to assign
     */
    public synchronized void assign(char variable, int value) {
        int index = getIndex(variable);
        memory[index] = value;
    }

    /**
     * Retrieves the value of a variable from memory.
     *
     * @param variable the variable name ('a' to 'z')
     * @return the value assigned to the variable
     */
    public synchronized int get(char variable) {
        int index = getIndex(variable);
        return memory[index];
    }

    /**
     * Prints the current state of the memory.
     */
    public synchronized void printMemoryState() {
        System.out.print("Memory State: ");
        for (char var = 'a'; var <= 'z'; ++var) {
            int value = memory[getIndex(var)];
            if (value != 0) { // Print only non-zero variables for clarity
                System.out.print(var + "=" + value + " ");
            }
        }
        System.out.println();
    }

    private int getIndex(char variable) {
        int index = variable - 'a';
        if (index < 0 || index >= memory.length) {
            throw new IllegalArgumentException("Invalid variable: " + variable);
        }
        return index;
    }
}
