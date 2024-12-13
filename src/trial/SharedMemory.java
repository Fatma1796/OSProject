//package trial;
//// SharedMemory.java
//import java.util.HashMap;
//import java.util.Map;
//public class SharedMemory {
//    private final HashMap<Character, Integer> memory = new HashMap<>();
//
//    public void assign(char variable, int value) {
//        memory.put(variable, value);
//        printMemory();
//    }
//
//    public Integer get(char variable) {
//        return memory.get(variable);
//    }
//
//    private void printMemory() {
//        System.out.println("Memory: " + memory);
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
////package trial;
////
////import java.util.HashMap;
////import java.util.Map;
////
////public class SharedMemory {
////    private final Map<Character, Integer> memory;
////
////    public SharedMemory() {
////        memory = new HashMap<>();
////    }
////
////    public synchronized void assign(char variable, int value) {
////        memory.put(variable, value);
////    }
////
////    public synchronized int get(char variable) {
////        return memory.getOrDefault(variable, 0);
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
