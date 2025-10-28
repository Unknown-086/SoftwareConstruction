import java.util.List;

/**
 * PermutationsCLI - Command-line interface for the String Permutation
 * Generator.
 *
 * Provides a user-friendly CLI with flag-based options for generating
 * permutations
 * using either recursive or iterative algorithms, with performance comparison.
 *
 * CLI Flags:
 * - -d, --allow-duplicates : Include duplicate permutations (default: false)
 * - -i, --iterative : Use iterative algorithm instead of recursive
 * - -c, --compare : Compare both algorithms' performance
 * - -h, --help : Show usage information
 *
 * Examples:
 * java PermutationsCLI abc
 * java PermutationsCLI -d aab
 * java PermutationsCLI -i abc
 * java PermutationsCLI -c abcd
 * java PermutationsCLI -d -i aab
 *
 * @author Lab Eight - Task 2
 * @version 2.0 (CLI Interface)
 */
public class PermutationsCLI {

    /**
     * CLI interface with flag-based arguments.
     * Usage: java PermutationsCLI [OPTIONS] <string>
     *
     * Options:
     * -d, --allow-duplicates : Include duplicate permutations (default: false)
     * -i, --iterative : Use iterative algorithm instead of recursive
     * -c, --compare : Compare both algorithms' performance
     * -h, --help : Show this help message
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0 || hasFlag(args, "-h", "--help")) {
            displayUsage();
            return;
        }

        // Parse flags
        boolean allowDuplicates = hasFlag(args, "-d", "--allow-duplicates");
        boolean useIterative = hasFlag(args, "-i", "--iterative");
        boolean compare = hasFlag(args, "-c", "--compare");

        // Extract the input string (first non-flag argument)
        String input = null;
        for (String arg : args) {
            if (!arg.startsWith("-")) {
                input = arg;
                break;
            }
        }

        if (input == null) {
            System.err.println("Error: No input string provided!");
            displayUsage();
            return;
        }

        try {
            // Display configuration
            System.out.println("\n" + "=".repeat(60));
            System.out.println("String Permutation Generator");
            System.out.println("=".repeat(60));
            System.out.println("Input: \"" + input + "\"");
            System.out.println("Allow Duplicates: " + allowDuplicates);
            System.out.println(
                    "Algorithm: " + (compare ? "Both (comparison)" : (useIterative ? "Iterative" : "Recursive")));
            System.out.println("=".repeat(60));

            if (compare) {
                // Run both algorithms and compare performance
                runComparison(input, allowDuplicates);
            } else if (useIterative) {
                // Run iterative algorithm
                runAlgorithm("Iterative", input, allowDuplicates, true);
            } else {
                // Run recursive algorithm
                runAlgorithm("Recursive", input, allowDuplicates, false);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("\nError: " + e.getMessage());
            displayUsage();
        } catch (Exception e) {
            System.err.println("\nUnexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Run a single algorithm and display results.
     *
     * @param algorithmName   the name of the algorithm (for display)
     * @param input           the input string
     * @param allowDuplicates whether to allow duplicate permutations
     * @param iterative       true for iterative algorithm, false for recursive
     */
    private static void runAlgorithm(String algorithmName, String input, boolean allowDuplicates, boolean iterative) {
        long startTime = System.nanoTime();

        List<String> results;
        if (iterative) {
            results = PermutationGenerator.generatePermutationsIterative(input, allowDuplicates);
        } else {
            results = PermutationGenerator.generatePermutations(input, allowDuplicates);
        }

        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("\n" + algorithmName + " Algorithm Results:");
        System.out.println("Total permutations: " + results.size());
        System.out.println("Time taken: " + String.format("%.3f", durationMs) + " ms");

        if (results.size() <= 50) {
            // Show all permutations horizontally (comma-separated)
            System.out.println("\nPermutations:");
            System.out.println(String.join(", ", results));
        } else {
            // Show first 50 horizontally
            System.out.println("\n(Too many to display - showing first 50)");
            List<String> first50 = results.subList(0, 50);
            System.out.println(String.join(", ", first50));
            System.out.println("...");
        }
    }

    /**
     * Run both algorithms and compare their performance.
     *
     * @param input           the input string
     * @param allowDuplicates whether to allow duplicate permutations
     */
    private static void runComparison(String input, boolean allowDuplicates) {
        System.out.println("\n--- PERFORMANCE COMPARISON ---\n");

        // Run recursive
        System.out.println("Running Recursive Algorithm...");
        long startRecursive = System.nanoTime();
        List<String> recursiveResults = PermutationGenerator.generatePermutations(input, allowDuplicates);
        long endRecursive = System.nanoTime();
        double recursiveMs = (endRecursive - startRecursive) / 1_000_000.0;

        System.out.println("  Permutations found: " + recursiveResults.size());
        System.out.println("  Time: " + String.format("%.3f", recursiveMs) + " ms");

        // Run iterative
        System.out.println("\nRunning Iterative Algorithm...");
        long startIterative = System.nanoTime();
        List<String> iterativeResults = PermutationGenerator.generatePermutationsIterative(input, allowDuplicates);
        long endIterative = System.nanoTime();
        double iterativeMs = (endIterative - startIterative) / 1_000_000.0;

        System.out.println("  Permutations found: " + iterativeResults.size());
        System.out.println("  Time: " + String.format("%.3f", iterativeMs) + " ms");

        // Compare
        System.out.println("\n" + "=".repeat(60));
        System.out.println("COMPARISON SUMMARY");
        System.out.println("=".repeat(60));
        System.out.println("Recursive: " + String.format("%.3f", recursiveMs) + " ms");
        System.out.println("Iterative: " + String.format("%.3f", iterativeMs) + " ms");

        if (recursiveMs < iterativeMs) {
            double speedup = iterativeMs / recursiveMs;
            System.out.println("Winner: Recursive (%.2fx faster)".formatted(speedup));
        } else {
            double speedup = recursiveMs / iterativeMs;
            System.out.println("Winner: Iterative (%.2fx faster)".formatted(speedup));
        }
        System.out.println("=".repeat(60));

        // Show sample results
        if (recursiveResults.size() <= 50) {
            System.out.println("\nSample Permutations (all " + recursiveResults.size() + "):");
            System.out.println(String.join(", ", recursiveResults));
        } else {
            System.out.println("\nSample Permutations (first 50 of " + recursiveResults.size() + "):");
            List<String> first50 = recursiveResults.subList(0, 50);
            System.out.println(String.join(", ", first50));
            System.out.println("...");
        }
    }

    /**
     * Check if a flag is present in arguments.
     *
     * @param args      the command-line arguments
     * @param shortFlag the short flag (e.g., "-d")
     * @param longFlag  the long flag (e.g., "--allow-duplicates")
     * @return true if the flag is present, false otherwise
     */
    private static boolean hasFlag(String[] args, String shortFlag, String longFlag) {
        for (String arg : args) {
            if (arg.equals(shortFlag) || arg.equals(longFlag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Display usage information.
     */
    private static void displayUsage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("String Permutation Generator - Usage");
        System.out.println("=".repeat(60));
        System.out.println("\nUsage:");
        System.out.println("  java PermutationsCLI [OPTIONS] <string>");
        System.out.println("\nOptions:");
        System.out.println("  -d, --allow-duplicates : Include duplicate permutations");
        System.out.println("                          (default: exclude duplicates)");
        System.out.println("  -i, --iterative        : Use iterative algorithm");
        System.out.println("                          (default: recursive)");
        System.out.println("  -c, --compare          : Compare both algorithms' performance");
        System.out.println("  -h, --help             : Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java PermutationsCLI abc");
        System.out.println("  java PermutationsCLI -d aab");
        System.out.println("  java PermutationsCLI -i abc");
        System.out.println("  java PermutationsCLI -c abcd");
        System.out.println("  java PermutationsCLI -d -i aab");
        System.out.println("  java PermutationsCLI -d -c abcde");
        System.out.println("\n" + "=".repeat(60) + "\n");
    }
}