import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PermutationGenerator - Core library for generating string permutations.
 *
 * Provides both recursive and iterative algorithms for generating permutations,
 * with optional duplicate filtering.
 *
 * Features:
 * - Recursive algorithm (swap-based with backtracking)
 * - Iterative algorithm (based on Heap's algorithm)
 * - Option to include or exclude duplicate permutations
 * - Efficient duplicate filtering during recursion
 *
 * Public API:
 * - generatePermutations(String input) // Recursive, no duplicates
 * - generatePermutations(String input, boolean allowDuplicates) // Recursive,
 * configurable
 * - generatePermutationsIterative(String input) // Iterative, no duplicates
 * - generatePermutationsIterative(String input, boolean allowDuplicates) //
 * Iterative, configurable
 *
 * Time Complexity: O(n! × n) for both algorithms
 * Space Complexity: O(n! × n) for storing results
 *
 * @author Lab Eight - Task 2
 * @version 2.0 (Core Library)
 */
public class PermutationGenerator {

    // ==================== RECURSIVE ALGORITHM ====================

    /**
     * Generate all permutations using recursion (backward compatible).
     * Duplicates are excluded by default.
     *
     * @param input the input string (may not be null)
     * @return a List<String> of unique permutations
     * @throws IllegalArgumentException if input is null
     */
    public static List<String> generatePermutations(String input) {
        return generatePermutations(input, false); // Default: no duplicates
    }

    /**
     * Generate all permutations using recursion with duplicate control.
     *
     * Algorithm: Swap-based recursion with backtracking
     * - For each position, try swapping with each remaining character
     * - Recursively generate permutations for the rest
     * - Backtrack by swapping back
     * - Use HashSet to skip duplicate swaps when excluding duplicates
     *
     * @param input           the input string (may not be null)
     * @param allowDuplicates whether to include duplicate permutations
     * @return a List<String> of permutations
     * @throws IllegalArgumentException if input is null
     */
    public static List<String> generatePermutations(String input, boolean allowDuplicates) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        List<String> results = new ArrayList<>();
        if (input.isEmpty()) {
            return results; // Empty list for empty input
        }

        char[] chars = input.toCharArray();
        if (allowDuplicates) {
            permuteWithDuplicates(chars, 0, results);
        } else {
            permuteNoDuplicates(chars, 0, results);
        }
        return results;
    }

    /**
     * Recursive helper: allows duplicate permutations.
     * Standard swap-based permutation generation.
     *
     * @param arr     the character array to permute
     * @param index   the current position being fixed
     * @param results the list to collect results
     */
    private static void permuteWithDuplicates(char[] arr, int index, List<String> results) {
        if (index == arr.length - 1) {
            results.add(new String(arr));
            return;
        }

        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            permuteWithDuplicates(arr, index + 1, results);
            swap(arr, index, i); // backtrack
        }
    }

    /**
     * Recursive helper: excludes duplicate permutations by checking if we've
     * already swapped with the same character at this position.
     *
     * Uses a HashSet to track which characters have been used at each level
     * to avoid generating duplicate permutations.
     *
     * @param arr     the character array to permute
     * @param index   the current position being fixed
     * @param results the list to collect results
     */
    private static void permuteNoDuplicates(char[] arr, int index, List<String> results) {
        if (index == arr.length - 1) {
            results.add(new String(arr));
            return;
        }

        Set<Character> used = new HashSet<>();
        for (int i = index; i < arr.length; i++) {
            // Skip if we've already used this character at this position
            if (used.contains(arr[i])) {
                continue;
            }
            used.add(arr[i]);

            swap(arr, index, i);
            permuteNoDuplicates(arr, index + 1, results);
            swap(arr, index, i); // backtrack
        }
    }

    /**
     * Swap two characters in an array.
     *
     * @param arr the character array
     * @param i   the first index
     * @param j   the second index
     */
    private static void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // ==================== ITERATIVE ALGORITHM ====================

    /**
     * Generate all permutations using iterative algorithm (Heap's algorithm
     * variant).
     * Duplicates are excluded by default.
     *
     * @param input the input string (may not be null)
     * @return a List<String> of unique permutations
     * @throws IllegalArgumentException if input is null
     */
    public static List<String> generatePermutationsIterative(String input) {
        return generatePermutationsIterative(input, false);
    }

    /**
     * Generate all permutations using iterative algorithm with duplicate control.
     * Based on Heap's algorithm (iterative variant).
     *
     * Algorithm:
     * - Use a counter array to track swap state
     * - Systematically generate permutations by controlled swapping
     * - Remove duplicates using HashSet if needed (post-processing)
     *
     * @param input           the input string (may not be null)
     * @param allowDuplicates whether to include duplicate permutations
     * @return a List<String> of permutations
     * @throws IllegalArgumentException if input is null
     */
    public static List<String> generatePermutationsIterative(String input, boolean allowDuplicates) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }

        List<String> results = new ArrayList<>();
        if (input.isEmpty()) {
            return results;
        }

        int n = input.length();
        char[] arr = input.toCharArray();
        int[] counters = new int[n];

        // Add the initial permutation
        results.add(new String(arr));

        int i = 0;
        while (i < n) {
            if (counters[i] < i) {
                // Swap based on parity
                if (i % 2 == 0) {
                    swap(arr, 0, i);
                } else {
                    swap(arr, counters[i], i);
                }

                results.add(new String(arr));
                counters[i]++;
                i = 0; // Reset to beginning
            } else {
                counters[i] = 0;
                i++;
            }
        }

        // Remove duplicates if needed (post-processing approach)
        if (!allowDuplicates) {
            Set<String> unique = new HashSet<>(results);
            results.clear();
            results.addAll(unique);
        }

        return results;
    }
}
