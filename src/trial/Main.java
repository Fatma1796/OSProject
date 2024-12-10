package trial;

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
        int coreCount = 2; // Adjust as needed for the number of slave cores
        MasterCore masterCore = new MasterCore(sharedMemory, coreCount);
        final int quantum = 2;


        // Create processes from text files
        Process process1 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\program_1.txt",
                1, 0, 10);
        Process process2 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\Program_2.txt",
                2, 11, 20);
        Process process3 = createProcessFromFile(
                "C:\\Users\\fosam\\OneDrive\\Desktop\\Uni\\Semester 3\\Operating Systems\\Program_3.txt",
                3, 21, 30);

        // Add processes to the MasterCore's ready queue
        masterCore.addProcess(process1);
        masterCore.addProcess(process2);
        masterCore.addProcess(process3);
        // Optionally, print the state of the ready queue initially
        masterCore.printReadyQueue();

        // Start scheduling processes with Round Robin
        masterCore.startScheduling();
    }

//    private static Process createProcessFromFile(String filePath, int processId, int memoryStart, int memoryEnd) {
//        Queue<Instruction> instructions = new ArrayDeque<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            Scanner inputScanner = new Scanner(System.in);
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(" ", 2);
//                String command = parts[0];
//                String[] args;
//
//                if (parts.length > 1) {
//                    args = parts[1].split(" ");
//                } else {
//                    args = new String[0];
//                }
//
//                // Handle "assign" commands with user input
//                if (command.equals("assign") && args.length == 2 && args[1].equals("input")) {
//                    System.out.print("Enter value for variable " + args[0] + ": ");
//                    String value = inputScanner.nextLine();
//                    instructions.add(new Instruction(command, new String[]{args[0], value}));
//                } else {
//                    instructions.add(new Instruction(command, args));
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + filePath);
//        }
//
//        return new Process(processId, instructions, memoryStart, memoryEnd);
//    }

//    private static Process createProcessFromFile(String filePath, int processId, int memoryStart, int memoryEnd) {
//        Queue<Instruction> instructions = new ArrayDeque<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            Scanner inputScanner = new Scanner(System.in);
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.trim().split("\\s+", 2); // Split by whitespace, max of 2 parts
//                String command = parts[0];
//                String[] args = (parts.length > 1) ? parts[1].split("\\s+") : new String[0];
//
//                if (command.equals("assign") && args.length > 1 && args[1].equals("input")) {
//                    System.out.print("Enter value for variable " + args[0] + ": ");
//                    String value = inputScanner.nextLine();
//                    instructions.add(new Instruction(command, new String[]{args[0], value}));
//                } else if (args.length > 2) { // For operations like add or subtract
//                    instructions.add(new Instruction(command, args)); // Ensure args are correctly formatted
//                } else {
//                    instructions.add(new Instruction(command, args));
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + filePath);
//        }
//        return new Process(processId, instructions, memoryStart, memoryEnd);
//    }


    private static Process createProcessFromFile(String filePath, int processId, int memoryStart, int memoryEnd) {
        Queue<Instruction> instructions = new ArrayDeque<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Scanner inputScanner = new Scanner(System.in);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+", 2);
                String command = parts[0];
                String[] args;

                if (command.equals("assign") && parts[1].contains("input")) {
                    args = parts[1].split("\\s+");
                    System.out.print("Enter value for " + args[0] + ": ");
                    String value = inputScanner.nextLine();
                    instructions.add(new Instruction(command, new String[]{args[0], value}));
                } else {
                    args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
                    instructions.add(new Instruction(command, args));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
        }
        return new Process(processId, instructions, memoryStart, memoryEnd);
    }


//    private static Process createProcessFromFile(String filePath, int processId, int memoryStart, int memoryEnd) {
//        Queue<Instruction> instructions = new ArrayDeque<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            Scanner inputScanner = new Scanner(System.in);
//
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.trim().split("\\s+", 2);
//                String command = parts[0];
//                String[] args;
//
//                if (command.equals("assign") && parts[1].contains("input")) {
//                    args = parts[1].split("\\s+");
//                    System.out.print("Enter value for " + args[0] + ": ");
//                    String value = inputScanner.nextLine();
//                    instructions.add(new Instruction(command, new String[]{args[0], value}));
//                } else {
//                    args = parts.length > 1 ? parts[1].split("\\s+") : new String[0];
//                    instructions.add(new Instruction(command, args));
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + filePath);
//        }
//        return new Process(processId, instructions, memoryStart, memoryEnd);
//    }



}