package trial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Number of cores in the system
        int coreCount = 2; // Adjust as needed for the number of slave cores
        MasterCore masterCore = new MasterCore(coreCount);

        // Create processes from text files
        Process process1 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\program_1.txt",
                1, masterCore);
        Process process2 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\Program_2.txt",
                2, masterCore);
        Process process3 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\Program_3.txt",
                3, masterCore);

        // Add processes to the MasterCore's ready queue
        masterCore.addProcess(process1);
        masterCore.addProcess(process2);
        masterCore.addProcess(process3);
        // Optionally, print the state of the ready queue initially
        masterCore.printReadyQueue();

        // Print values for process ID 1
        masterCore.printValuesForProcessId(1);

        // Start scheduling processes with Round Robin
        masterCore.startScheduling();
    }

    private static Process createProcessFromFile(String filePath, int processId, MasterCore masterCore) {
        Queue<Instruction> instructions = new ArrayDeque<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Scanner scanner = new Scanner(System.in);
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 2);
                String command = parts[0];
                String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];

                if (command.equals("assign") && args.length > 1 && args[1].equals("input")) {
                    System.out.print("Enter value for " + args[0] + ": ");
                    int value = scanner.nextInt();
                    masterCore.updateValueMap(args[0].charAt(0), value, new Process(processId, instructions));
                } else {
                    instructions.add(new Instruction(command, args));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return new Process(processId, instructions);
    }

//    private static Process createProcessFromFile(String filePath, int processId, MasterCore masterCore) {
//        Queue<Instruction> instructions = new ArrayDeque<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            Scanner scanner = new Scanner(System.in);
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.trim().split("\\s+", 2);
//                String command = parts[0];
//                String[] args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
//
//                if (command.equals("assign") && args.length > 1 && args[1].equals("input")) {
//                    System.out.print("Enter value for " + args[0] + ": ");
//                    int value = scanner.nextInt();
//                    masterCore.updateValueMap(args[0].charAt(0), value, new Process(processId, instructions));
//                } else {
//                    instructions.add(new Instruction(command, args));
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + filePath);
//        }
//        return new Process(processId, instructions);
//    }
}








