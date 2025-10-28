import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Essential test suite for PermutationGenerator.
 * Tests both recursive and iterative algorithms with key scenarios.
 *
 * @author Abdul Hadi
 * @version 1.0
 */
@DisplayName("PermutationGenerator Tests")
public class PermutationGeneratorTest {

    // ==================== RECURSIVE ALGORITHM TESTS ====================

    @Test
    @DisplayName("Recursive: Should generate 6 permutations for 'abc'")
    void testRecursiveBasic() {
        List<String> result = PermutationGenerator.generatePermutations("abc");
        assertEquals(6, result.size(), "abc should have 6 permutations");
        Set<String> expected = Set.of("abc", "acb", "bac", "bca", "cab", "cba");
        assertEquals(expected, new HashSet<>(result), "Should contain all 6 unique permutations");
    }

    @Test
    @DisplayName("Recursive: Should handle empty string")
    void testRecursiveEmpty() {
        List<String> result = PermutationGenerator.generatePermutations("");
        assertEquals(0, result.size(), "Empty string should have 0 permutations");
    }

    @Test
    @DisplayName("Recursive: Should throw exception for null input")
    void testRecursiveNull() {
        assertThrows(IllegalArgumentException.class,
                () -> PermutationGenerator.generatePermutations(null),
                "Null input should throw IllegalArgumentException");
    }

    // ==================== ITERATIVE ALGORITHM TESTS ====================

    @Test
    @DisplayName("Iterative: Should generate 6 permutations for 'abc'")
    void testIterativeBasic() {
        List<String> result = PermutationGenerator.generatePermutationsIterative("abc");
        assertEquals(6, result.size(), "abc should have 6 permutations");
        Set<String> expected = Set.of("abc", "acb", "bac", "bca", "cab", "cba");
        assertEquals(expected, new HashSet<>(result), "Should contain all 6 unique permutations");
    }

    @Test
    @DisplayName("Iterative: Should handle empty string")
    void testIterativeEmpty() {
        List<String> result = PermutationGenerator.generatePermutationsIterative("");
        assertEquals(0, result.size(), "Empty string should have 0 permutations");
    }

    @Test
    @DisplayName("Iterative: Should throw exception for null input")
    void testIterativeNull() {
        assertThrows(IllegalArgumentException.class,
                () -> PermutationGenerator.generatePermutationsIterative(null),
                "Null input should throw IllegalArgumentException");
    }

    // ==================== DUPLICATE HANDLING TESTS ====================

    @Test
    @DisplayName("Should exclude duplicates for 'aab' by default")
    void testExcludeDuplicates() {
        List<String> result = PermutationGenerator.generatePermutations("aab");
        assertEquals(3, result.size(), "aab should have 3 unique permutations");
        Set<String> expected = Set.of("aab", "aba", "baa");
        assertEquals(expected, new HashSet<>(result), "Should contain only unique permutations");
    }

    @Test
    @DisplayName("Should include duplicates for 'aab' when allowDuplicates=true")
    void testIncludeDuplicates() {
        List<String> result = PermutationGenerator.generatePermutations("aab", true);
        assertEquals(6, result.size(), "aab should have 6 total permutations with duplicates");
    }

    @Test
    @DisplayName("Should handle 'aaa' - all same characters")
    void testAllSameCharacters() {
        List<String> result = PermutationGenerator.generatePermutations("aaa", false);
        assertEquals(1, result.size(), "aaa should have 1 unique permutation");
        assertEquals("aaa", result.get(0), "Should be 'aaa'");
    }

    // ==================== ALGORITHM COMPARISON TESTS ====================

    @Test
    @DisplayName("Both algorithms should produce same results")
    void testAlgorithmConsistency() {
        List<String> recursive = PermutationGenerator.generatePermutations("abc");
        List<String> iterative = PermutationGenerator.generatePermutationsIterative("abc");
        assertEquals(recursive.size(), iterative.size(),
                "Both algorithms should produce same number of permutations");
        assertEquals(new HashSet<>(recursive), new HashSet<>(iterative),
                "Both algorithms should produce same set of permutations");
    }

    @Test
    @DisplayName("Both algorithms handle duplicates the same way")
    void testDuplicateConsistency() {
        List<String> recursive = PermutationGenerator.generatePermutations("aab", false);
        List<String> iterative = PermutationGenerator.generatePermutationsIterative("aab", false);
        assertEquals(recursive.size(), iterative.size(),
                "Both should produce same count for aab");
        assertEquals(new HashSet<>(recursive), new HashSet<>(iterative),
                "Both should produce same unique set");
    }

    // ==================== EDGE CASES ====================

    @Test
    @DisplayName("Should handle single character")
    void testSingleCharacter() {
        List<String> result = PermutationGenerator.generatePermutations("a");
        assertEquals(1, result.size(), "Single character should have 1 permutation");
        assertEquals("a", result.get(0), "Should be 'a'");
    }

    @Test
    @DisplayName("Should handle two characters")
    void testTwoCharacters() {
        List<String> result = PermutationGenerator.generatePermutations("ab");
        assertEquals(2, result.size(), "Two characters should have 2 permutations");
        assertTrue(result.contains("ab") && result.contains("ba"),
                "Should contain 'ab' and 'ba'");
    }

    @Test
    @DisplayName("Should generate 24 permutations for 'abcd'")
    void testFourCharacters() {
        List<String> result = PermutationGenerator.generatePermutations("abcd");
        assertEquals(24, result.size(), "abcd should have 24 permutations (4!)");
    }
}
