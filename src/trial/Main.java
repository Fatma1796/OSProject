package trial;

import trial.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize shared memory
        SharedMemory sharedMemory = new SharedMemory();

        // Number of cores in the system
        int coreCount = 2; // Adjust as needed
        MasterCore masterCore = new MasterCore(sharedMemory, coreCount);

        // Create processes from text files
        Process process1 = createProcessFromFile(
                "C:\\Users\\ganna\\OneDrive\\operating system\\OSprojectUPDATED\\OSPROJECTrial\\src\\trial\\Program_2.txt",
                1, 0, 10);
        Process process2 = createProcessFromFile(
                "C:\\Users\\ganna\\OneDrive\\operating system\\OSprojectUPDATED\\OSPROJECTrial\\src\\trial\\Program_3.txt",
                2, 11, 20);

        // Add processes to the MasterCore's ready queue
        masterCore.addProcess(process1);
        masterCore.addProcess(process2);

        // Start scheduling processes
        masterCore.startScheduling();
    }

    private static Process createProcessFromFile(String filePath, int processId, int memoryStart, int memoryEnd) {
        Queue<Instruction> instructions = new ArrayDeque<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Scanner inputScanner = new Scanner(System.in);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ", 2);
                String command = parts[0];
                String[] args;

                if (parts.length > 1) {
                    args = parts[1].split(" ");
                } else {
                    args = new String[0];
                }

                // Handle "assign" commands with user input
                if (command.equals("assign") && args.length == 2 && args[1].equals("input")) {
                    System.out.print("Enter value for variable " + args[0] + ": ");
                    String value = inputScanner.nextLine();
                    instructions.add(new Instruction(command, new String[]{args[0], value}));
                } else {
                    instructions.add(new Instruction(command, args));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }

        return new Process(processId, instructions, memoryStart, memoryEnd);
    }
}
