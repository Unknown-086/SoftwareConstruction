/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * Testing strategy for Filter methods:
     * 
     * writtenBy():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - username match: no matches, one match, multiple matches
     * - username case: exact case, different case (case insensitive)
     * - username in list: author exists, author doesn't exist
     * 
     * inTimespan():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - tweet timestamps relative to timespan: before, within, after, at boundaries
     * - timespan: zero-length, non-zero length
     * - matches: no tweets in timespan, some tweets in timespan, all tweets in
     * timespan
     * 
     * containing():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - words.size(): 0, 1, >1
     * - word matches: no matches, some matches, all words match
     * - word case: exact case, different case (case insensitive)
     * - word position: start, middle, end of tweet text
     * - word boundaries: whole words vs substrings
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T13:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "another tweet by alyssa", d3);
    private static final Tweet tweet4 = new Tweet(4, "charlie", "hello world from charlie", d4);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Tests for writtenBy()
     */

    @Test
    public void testWrittenByEmptyList() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");

        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");

        assertEquals("expected list of size 2", 2, writtenBy.size());
        assertTrue("expected list to contain tweet1", writtenBy.contains(tweet1));
        assertTrue("expected list to contain tweet3", writtenBy.contains(tweet3));
        assertFalse("expected list not to contain tweet2", writtenBy.contains(tweet2));
    }

    @Test
    public void testWrittenByNoMatches() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "nonexistent");

        assertTrue("expected empty list when no matches", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByCaseInsensitive() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");

        assertEquals("expected singleton list for case insensitive match", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByPreservesOrder() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet3, tweet1), "alyssa");

        assertEquals("expected list of size 2", 2, writtenBy.size());
        assertEquals("expected first tweet to be tweet3", tweet3, writtenBy.get(0));
        assertEquals("expected second tweet to be tweet1", tweet1, writtenBy.get(1));
    }

    @Test
    public void testWrittenBySingleTweetMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "alyssa");

        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenBySingleTweetNoMatch() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1), "bob");

        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    /*
     * Tests for inTimespan()
     */

    @Test
    public void testInTimespanEmptyList() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(testStart, testEnd));

        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
        assertEquals("expected same order", 1, inTimespan.indexOf(tweet2));
    }

    @Test
    public void testInTimespanNoTweetsInTimespan() {
        Instant testStart = Instant.parse("2016-02-17T08:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T09:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));

        assertTrue("expected empty list when no tweets in timespan", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanSomeTweetsInTimespan() {
        Instant testStart = Instant.parse("2016-02-17T10:30:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:30:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3),
                new Timespan(testStart, testEnd));

        assertEquals("expected list of size 2", 2, inTimespan.size());
        assertTrue("expected list to contain tweet2", inTimespan.contains(tweet2));
        assertTrue("expected list to contain tweet3", inTimespan.contains(tweet3));
        assertFalse("expected list not to contain tweet1", inTimespan.contains(tweet1));
    }

    @Test
    public void testInTimespanAtBoundaries() {
        Instant testStart = d1; // exactly tweet1 timestamp
        Instant testEnd = d2; // exactly tweet2 timestamp

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3),
                new Timespan(testStart, testEnd));

        assertEquals("expected list of size 2", 2, inTimespan.size());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
        assertTrue("expected list to contain tweet2", inTimespan.contains(tweet2));
        assertFalse("expected list not to contain tweet3", inTimespan.contains(tweet3));
    }

    @Test
    public void testInTimespanZeroLengthTimespan() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(d1, d1));

        assertEquals("expected list of size 1", 1, inTimespan.size());
        assertTrue("expected list to contain tweet1", inTimespan.contains(tweet1));
    }

    @Test
    public void testInTimespanPreservesOrder() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T14:00:00Z");

        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet4, tweet1, tweet3, tweet2),
                new Timespan(testStart, testEnd));

        assertEquals("expected all tweets", 4, inTimespan.size());
        assertEquals("expected original order preserved", tweet4, inTimespan.get(0));
        assertEquals("expected original order preserved", tweet1, inTimespan.get(1));
        assertEquals("expected original order preserved", tweet3, inTimespan.get(2));
        assertEquals("expected original order preserved", tweet2, inTimespan.get(3));
    }

    /*
     * Tests for containing()
     */

    @Test
    public void testContainingEmptyList() {
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("talk"));

        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingEmptyWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());

        assertTrue("expected empty list when no words to search", containing.isEmpty());
    }

    @Test
    public void testContainingSingleWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));

        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
        assertEquals("expected same order", 1, containing.indexOf(tweet2));
    }

    @Test
    public void testContainingSingleWordNoMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("xyz"));

        assertTrue("expected empty list when no matches", containing.isEmpty());
    }

    @Test
    public void testContainingSingleWordSomeMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet4), Arrays.asList("rivest"));

        assertEquals("expected list of size 2", 2, containing.size());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
        assertTrue("expected list to contain tweet2", containing.contains(tweet2));
        assertFalse("expected list not to contain tweet4", containing.contains(tweet4));
    }

    @Test
    public void testContainingMultipleWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet4),
                Arrays.asList("talk", "world"));

        assertEquals("expected list of size 3", 3, containing.size());
        assertTrue("expected list to contain all tweets with any matching word",
                containing.containsAll(Arrays.asList(tweet1, tweet2, tweet4)));
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("TALK"));

        assertEquals("expected list of size 2 for case insensitive match", 2, containing.size());
        assertTrue("expected list to contain tweet1", containing.contains(tweet1));
        assertTrue("expected list to contain tweet2", containing.contains(tweet2));
    }

    @Test
    public void testContainingWordBoundaries() {
        Tweet tweetWithSubstring = new Tweet(5, "test", "talking about talks", d1);
        List<Tweet> containing = Filter.containing(Arrays.asList(tweetWithSubstring), Arrays.asList("talk"));

        assertTrue("expected empty list for substring match", containing.isEmpty());
    }

    @Test
    public void testContainingWordAtStart() {
        Tweet tweetWordAtStart = new Tweet(5, "test", "talk is important", d1);
        List<Tweet> containing = Filter.containing(Arrays.asList(tweetWordAtStart), Arrays.asList("talk"));

        assertEquals("expected list of size 1", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweetWordAtStart));
    }

    @Test
    public void testContainingWordAtEnd() {
        Tweet tweetWordAtEnd = new Tweet(5, "test", "we should talk", d1);
        List<Tweet> containing = Filter.containing(Arrays.asList(tweetWordAtEnd), Arrays.asList("talk"));

        assertEquals("expected list of size 1", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweetWordAtEnd));
    }

    @Test
    public void testContainingWithPunctuation() {
        Tweet tweetWithPunctuation = new Tweet(5, "test", "let's talk! yes, talk.", d1);
        List<Tweet> containing = Filter.containing(Arrays.asList(tweetWithPunctuation), Arrays.asList("talk"));

        assertEquals("expected list of size 1", 1, containing.size());
        assertTrue("expected list to contain tweet", containing.contains(tweetWithPunctuation));
    }

    @Test
    public void testContainingPreservesOrder() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet4, tweet1, tweet2), Arrays.asList("talk"));

        assertEquals("expected list of size 2", 2, containing.size());
        assertEquals("expected tweet1 before tweet2", tweet1, containing.get(0));
        assertEquals("expected tweet1 before tweet2", tweet2, containing.get(1));
    }

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting (temporarily)
     * your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own that
     * you have put in Filter, because that means you're testing a stronger spec
     * than Filter says. If you need such helper methods, define them in a different
     * class. If you only need them in this test class, then keep them in this test
     * class.
     */

}
