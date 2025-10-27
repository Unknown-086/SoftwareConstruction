import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * RecursiveFileSearch - A program that recursively searches for files in a directory tree.
 * 
 * This class provides functionality to search for a specified file within a directory
 * and all of its subdirectories using a recursive algorithm.
 * 
 * @author Lab Eight
 * @version 1.0
 */
public class RecursiveFileSearch {

    /**
     * Recursively searches for a file in a directory and its subdirectories.
     * 
     * Base cases:
     * - If directory is null, throw IllegalArgumentException
     * - If fileName is null or empty, throw IllegalArgumentException
     * - If directory doesn't exist or isn't a directory, throw IllegalArgumentException
     * - If directory is empty or not readable, return empty list
     * 
     * Recursive step:
     * - For each item in the directory:
     *   - If it's a file and matches the fileName, add to results
     *   - If it's a directory, recursively search in that directory
     * 
     * @param directory the directory to search in (must be a valid directory)
     * @param fileName the name of the file to search for (must not be null or empty)
     * @return a List of File objects representing all matching files found
     * @throws IllegalArgumentException if directory is null, fileName is null or empty,
     *                                  or directory does not exist or is not a directory
     */
    public static List<File> searchFile(File directory, String fileName) {
        // Base case 1: Validate inputs
        if (directory == null) {
            throw new IllegalArgumentException("Directory cannot be null");
        }
        
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        
        // Base case 2: Check if directory exists and is actually a directory
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory does not exist: " + directory.getPath());
        }
        
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory: " + directory.getPath());
        }
        
        // Initialize result list
        List<File> foundFiles = new ArrayList<>();
        
        // Base case 3: Check if directory is readable
        if (!directory.canRead()) {
            System.err.println("Warning: Cannot read directory: " + directory.getPath());
            return foundFiles; // Return empty list
        }
        
        // Get all files and subdirectories in the current directory
        File[] contents = directory.listFiles();
        
        // Base case 4: Empty directory
        if (contents == null || contents.length == 0) {
            return foundFiles; // Return empty list
        }
        
        // Recursive step: Process each item in the directory
        for (File item : contents) {
            try {
                if (item.isFile()) {
                    // Check if this file matches the search criteria
                    if (item.getName().equals(fileName)) {
                        foundFiles.add(item);
                    }
                } else if (item.isDirectory()) {
                    // Recursively search in subdirectory
                    List<File> subResults = searchFile(item, fileName);
                    foundFiles.addAll(subResults);
                }
            } catch (SecurityException e) {
                // Handle permission issues gracefully
                System.err.println("Warning: Access denied to: " + item.getPath());
            }
        }
        
        return foundFiles;
    }
    
    /**
     * Displays the search results to the console.
     * 
     * @param fileName the name of the file that was searched for
     * @param foundFiles the list of files that were found
     */
    private static void displayResults(String fileName, List<File> foundFiles) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Search Results for: \"" + fileName + "\"");
        System.out.println("=".repeat(60));
        
        if (foundFiles.isEmpty()) {
            System.out.println("\n❌ File not found: \"" + fileName + "\"");
            System.out.println("The file was not found in the specified directory or its subdirectories.");
        } else {
            System.out.println("\n✓ Found " + foundFiles.size() + " occurrence(s) of \"" + fileName + "\":\n");
            
            for (int i = 0; i < foundFiles.size(); i++) {
                System.out.println((i + 1) + ". " + foundFiles.get(i).getAbsolutePath());
            }
        }
        
        System.out.println("\n" + "=".repeat(60));
    }
    
    /**
     * Displays usage information for the program.
     */
    private static void displayUsage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Recursive File Search - Usage");
        System.out.println("=".repeat(60));
        System.out.println("\nUsage:");
        System.out.println("  java RecursiveFileSearch <directory_path> <file_name>");
        System.out.println("\nArguments:");
        System.out.println("  directory_path : The path to the directory to search in");
        System.out.println("  file_name      : The name of the file to search for");
        System.out.println("\nExample:");
        System.out.println("  java RecursiveFileSearch \"C:\\Users\\Documents\" \"report.txt\"");
        System.out.println("  java RecursiveFileSearch \".\" \"Main.java\"");
        System.out.println("\n" + "=".repeat(60) + "\n");
    }
    
    /**
     * Main method to run the file search from command line.
     * 
     * Takes two command-line arguments:
     * 1. directory_path - the directory to search in
     * 2. file_name - the name of the file to search for
     * 
     * @param args command line arguments [directory_path, file_name]
     */
    public static void main(String[] args) {
        // Check if correct number of arguments provided
        if (args.length != 2) {
            System.err.println("\n❌ Error: Incorrect number of arguments!");
            System.err.println("Expected 2 arguments, but got " + args.length);
            displayUsage();
            return;
        }
        
        // Extract command-line arguments
        String directoryPath = args[0];
        String fileName = args[1];
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Recursive File Search");
        System.out.println("=".repeat(60));
        System.out.println("Directory: " + directoryPath);
        System.out.println("File Name: " + fileName);
        System.out.println("=".repeat(60));
        
        try {
            // Create File object for the directory
            File directory = new File(directoryPath);
            
            // Perform the search
            System.out.println("\nSearching...");
            long startTime = System.currentTimeMillis();
            List<File> results = searchFile(directory, fileName);
            long endTime = System.currentTimeMillis();
            
            // Display results
            displayResults(fileName, results);
            
            // Display search time
            System.out.println("Search completed in " + (endTime - startTime) + " ms\n");
            
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments
            System.err.println("\n❌ Error: " + e.getMessage());
            displayUsage();
            
        } catch (SecurityException e) {
            // Handle security/permission issues
            System.err.println("\n❌ Security Error: " + e.getMessage());
            System.err.println("You may not have permission to access the specified directory.");
            
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.err.println("\n❌ Unexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
