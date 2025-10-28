import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RecursiveFileSearchCLI - Command-line interface for the RecursiveFileSearch
 * program.
 * 
 * This class handles:
 * - Command-line argument parsing
 * - User interaction and display
 * - Result formatting and output
 * 
 * The actual search logic is in RecursiveFileSearch.java
 * 
 * Usage:
 * java RecursiveFileSearchCLI [OPTIONS] <directory> <filename> [<filename2>
 * ...]
 * 
 * Options:
 * -i, --ignore-case Case-insensitive search
 * -m, --multiple Search for multiple files
 * -c, --count Count occurrences only
 * -h, --help Show help message
 * 
 * @author Lab Eight
 * @version 3.0 (Refactored - CLI Only)
 */
public class RecursiveFileSearchCLI {

    /**
     * Displays the search results to the console (single file).
     * 
     * @param fileName   the name of the file that was searched for
     * @param foundFiles the list of files that were found
     */
    private static void displayResults(String fileName, List<File> foundFiles) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Search Results for: \"" + fileName + "\"");
        System.out.println("=".repeat(60));

        if (foundFiles.isEmpty()) {
            System.out.println("\nFile not found: \"" + fileName + "\"");
            System.out.println("The file was not found in the specified directory or its subdirectories.");
        } else {
            System.out.println("\nFound " + foundFiles.size() + " occurrence(s) of \"" + fileName + "\":\n");

            for (int i = 0; i < foundFiles.size(); i++) {
                System.out.println((i + 1) + ". " + foundFiles.get(i).getAbsolutePath());
            }
        }

        System.out.println("\n" + "=".repeat(60));
    }

    /**
     * Displays the search results for multiple files.
     * 
     * @param results the map of file names to found files
     */
    private static void displayMultipleResults(Map<String, List<File>> results) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Multiple File Search Results");
        System.out.println("=".repeat(60));

        int totalFound = 0;
        for (Map.Entry<String, List<File>> entry : results.entrySet()) {
            String fileName = entry.getKey();
            List<File> files = entry.getValue();

            System.out.println("\n \"" + fileName + "\" - Found " + files.size() + " occurrence(s)");

            if (!files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    System.out.println("   " + (i + 1) + ". " + files.get(i).getAbsolutePath());
                }
                totalFound += files.size();
            } else {
                System.out.println("   (Not found)");
            }
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("Total: " + totalFound + " file(s) found across all searches");
        System.out.println("=".repeat(60));
    }

    /**
     * Displays the count results.
     * 
     * @param fileName the name of the file that was counted
     * @param count    the number of occurrences found
     */
    private static void displayCountResults(String fileName, int count) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Count Results");
        System.out.println("=".repeat(60));
        System.out.println("\nFile: \"" + fileName + "\"");
        System.out.println("Occurrences: " + count);
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
        System.out.println("  java RecursiveFileSearchCLI [OPTIONS] <directory> <filename> [<filename2> ...]");
        System.out.println("\nOptions:");
        System.out.println("  -i, --ignore-case    Case-insensitive search");
        System.out.println("  -m, --multiple       Search for multiple files");
        System.out.println("  -c, --count          Count occurrences only (don't list all paths)");
        System.out.println("  -h, --help           Show this help message");
        System.out.println("\nArguments:");
        System.out.println("  directory            Directory path to search in");
        System.out.println("  filename             Name(s) of file(s) to search for");
        System.out.println("\nExamples:");
        System.out.println("  java RecursiveFileSearchCLI \"C:\\Docs\" \"report.txt\"");
        System.out.println("  java RecursiveFileSearchCLI -i \"C:\\Docs\" \"REPORT.txt\"");
        System.out.println("  java RecursiveFileSearchCLI -m \"C:\\Docs\" \"file1.txt\" \"file2.pdf\"");
        System.out.println("  java RecursiveFileSearchCLI -i -c \"C:\\Projects\" \"Main.java\"");
        System.out.println("\nFlags can be combined:");
        System.out.println("  -i -m    : Search multiple files (case-insensitive)");
        System.out.println("  -i -c    : Count occurrences (case-insensitive)");
        System.out.println("  -m -c    : Count multiple files");
        System.out.println("  -i -m -c : Count multiple files (case-insensitive)");
        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    /**
     * Main method to run the file search from command line.
     * 
     * Supports multiple modes:
     * - Single file search (default)
     * - Multiple file search (-m flag)
     * - Count mode (-c flag)
     * - Case-insensitive search (-i flag)
     * 
     * @param args command line arguments with optional flags
     */
    public static void main(String[] args) {
        // Check if help is requested or no arguments
        if (args.length == 0 || args[0].equals("-h") || args[0].equals("--help")) {
            displayUsage();
            return;
        }

        // Parse command-line arguments
        boolean caseSensitive = true; // default: case-sensitive
        boolean multipleMode = false; // default: single file
        boolean countMode = false; // default: list all files
        String directory = null;
        List<String> fileNames = new ArrayList<>();

        // Parse flags and arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            if (arg.equals("-i") || arg.equals("--ignore-case")) {
                caseSensitive = false;
            } else if (arg.equals("-m") || arg.equals("--multiple")) {
                multipleMode = true;
            } else if (arg.equals("-c") || arg.equals("--count")) {
                countMode = true;
            } else if (arg.equals("-h") || arg.equals("--help")) {
                displayUsage();
                return;
            } else if (arg.startsWith("-")) {
                System.err.println("\nError: Unknown option: " + arg);
                displayUsage();
                return;
            } else {
                // Non-flag argument
                if (directory == null) {
                    directory = arg;
                } else {
                    fileNames.add(arg);
                }
            }
        }

        // Validate arguments
        if (directory == null) {
            System.err.println("\nError: Directory path is required!");
            displayUsage();
            return;
        }

        if (fileNames.isEmpty()) {
            System.err.println("\nError: At least one file name is required!");
            displayUsage();
            return;
        }

        // Display search configuration
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Recursive File Search");
        System.out.println("=".repeat(60));
        System.out.println("Directory: " + directory);
        System.out.println("Mode: " + (multipleMode ? "Multiple Files" : "Single File"));
        System.out.println("Case Sensitive: " + caseSensitive);
        System.out.println("Count Only: " + countMode);
        if (multipleMode || fileNames.size() > 1) {
            System.out.println("Files: " + String.join(", ", fileNames));
        } else {
            System.out.println("File: " + fileNames.get(0));
        }
        System.out.println("=".repeat(60));

        try {
            File dir = new File(directory);
            long startTime = System.currentTimeMillis();

            // Execute based on mode
            if (multipleMode || fileNames.size() > 1) {
                // Multiple file search
                System.out.println("\nSearching for " + fileNames.size() + " file(s)...");
                Map<String, List<File>> results = RecursiveFileSearch.searchMultipleFiles(dir, fileNames,
                        caseSensitive);

                if (countMode) {
                    // Display count for each file
                    System.out.println("\n" + "=".repeat(60));
                    System.out.println("Count Results (Multiple Files)");
                    System.out.println("=".repeat(60));
                    int totalCount = 0;
                    for (Map.Entry<String, List<File>> entry : results.entrySet()) {
                        int count = entry.getValue().size();
                        System.out.println("\"" + entry.getKey() + "\": " + count + " occurrence(s)");
                        totalCount += count;
                    }
                    System.out.println("\nTotal: " + totalCount + " file(s) found");
                    System.out.println("=".repeat(60));
                } else {
                    displayMultipleResults(results);
                }
            } else {
                // Single file search
                String fileName = fileNames.get(0);
                System.out.println("\nSearching...");

                if (countMode) {
                    int count = RecursiveFileSearch.countFileOccurrences(dir, fileName, caseSensitive);
                    displayCountResults(fileName, count);
                } else {
                    List<File> results = RecursiveFileSearch.searchFile(dir, fileName, caseSensitive);
                    displayResults(fileName, results);
                }
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Search completed in " + (endTime - startTime) + " ms\n");

        } catch (IllegalArgumentException e) {
            System.err.println("\nError: " + e.getMessage());
            displayUsage();

        } catch (SecurityException e) {
            System.err.println("\nSecurity Error: " + e.getMessage());
            System.err.println("You may not have permission to access the specified directory.");

        } catch (Exception e) {
            System.err.println("\nUnexpected Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
