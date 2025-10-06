/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * Testing strategy for Extract methods:
     * 
     * getTimespan():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - tweet timestamps: all same, different (chronological order), different
     * (reverse chronological)
     * - edge cases: empty list (underdetermined postcondition)
     * 
     * getMentionedUsers():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - mentions per tweet: 0, 1, >1
     * - mention format: valid @username, invalid formats (email addresses,
     * incomplete @)
     * - username case: same username with different cases, different usernames
     * - mention position: start of text, middle, end of text
     * - surrounding characters: valid separators (space, punctuation), invalid
     * (alphanumeric)
     * - duplicate mentions: same user mentioned multiple times in same tweet or
     * across tweets
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Tests for getTimespan()

    @Test
    public void testGetTimespanEmptyList() {
        List<Tweet> emptyList = Arrays.asList();
        Timespan timespan = Extract.getTimespan(emptyList);
        // Underdetermined postcondition - just ensure it doesn't crash and returns
        // valid timespan
        assertNotNull("timespan should not be null for empty list", timespan);
        assertNotNull("start should not be null", timespan.getStart());
        assertNotNull("end should not be null", timespan.getEnd());
    }

    @Test
    public void testGetTimespanSingleTweet() {
        List<Tweet> singleTweet = Arrays.asList(tweet1);
        Timespan timespan = Extract.getTimespan(singleTweet);

        assertEquals("expected start time to be tweet timestamp", d1, timespan.getStart());
        assertEquals("expected end time to be tweet timestamp", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanTwoTweetsReverseOrder() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet1));

        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweetsSameTime() {
        Tweet tweet3 = new Tweet(3, "charlie", "same time as tweet1", d1);
        List<Tweet> tweets = Arrays.asList(tweet1, tweet3);
        Timespan timespan = Extract.getTimespan(tweets);

        assertEquals("expected start time", d1, timespan.getStart());
        assertEquals("expected end time", d1, timespan.getEnd());
    }

    @Test
    public void testGetTimespanMultipleTweetsUnordered() {
        Tweet tweet3 = new Tweet(3, "charlie", "latest tweet", d3);
        List<Tweet> tweets = Arrays.asList(tweet2, tweet1, tweet3);
        Timespan timespan = Extract.getTimespan(tweets);

        assertEquals("expected start time to be earliest", d1, timespan.getStart());
        assertEquals("expected end time to be latest", d3, timespan.getEnd());
    }

    // Tests for getMentionedUsers()

    @Test
    public void testGetMentionedUsersEmptyList() {
        List<Tweet> emptyList = Arrays.asList();
        Set<String> mentionedUsers = Extract.getMentionedUsers(emptyList);

        assertTrue("expected empty set for empty list", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));

        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersSingleMention() {
        Tweet tweetWithMention = new Tweet(3, "alice", "hey @bob how are you?", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMention));

        assertEquals("expected one mention", 1, mentionedUsers.size());
        assertTrue("expected mention of bob (case insensitive)",
                mentionedUsers.contains("bob") || mentionedUsers.contains("Bob") || mentionedUsers.contains("BOB"));
    }

    @Test
    public void testGetMentionedUsersMultipleMentionsOneTweet() {
        Tweet tweetWithMentions = new Tweet(3, "alice", "@bob and @charlie should see this @david", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithMentions));

        assertEquals("expected three mentions", 3, mentionedUsers.size());
        // Test case insensitive containment
        boolean containsBob = mentionedUsers.contains("bob") || mentionedUsers.contains("Bob")
                || mentionedUsers.contains("BOB");
        boolean containsCharlie = mentionedUsers.contains("charlie") || mentionedUsers.contains("Charlie")
                || mentionedUsers.contains("CHARLIE");
        boolean containsDavid = mentionedUsers.contains("david") || mentionedUsers.contains("David")
                || mentionedUsers.contains("DAVID");

        assertTrue("expected mention of bob", containsBob);
        assertTrue("expected mention of charlie", containsCharlie);
        assertTrue("expected mention of david", containsDavid);
    }

    @Test
    public void testGetMentionedUsersMultipleTweets() {
        Tweet tweet3 = new Tweet(3, "alice", "hey @bob", d1);
        Tweet tweet4 = new Tweet(4, "charlie", "@alice and @bob are cool", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));

        assertEquals("expected two unique mentions", 2, mentionedUsers.size());
        boolean containsBob = mentionedUsers.contains("bob") || mentionedUsers.contains("Bob")
                || mentionedUsers.contains("BOB");
        boolean containsAlice = mentionedUsers.contains("alice") || mentionedUsers.contains("Alice")
                || mentionedUsers.contains("ALICE");

        assertTrue("expected mention of bob", containsBob);
        assertTrue("expected mention of alice", containsAlice);
    }

    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet tweet3 = new Tweet(3, "alice", "hey @Bob", d1);
        Tweet tweet4 = new Tweet(4, "charlie", "also @bob and @BOB", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));

        assertEquals("expected one unique mention regardless of case", 1, mentionedUsers.size());
        boolean containsBobSomeCase = mentionedUsers.contains("bob") || mentionedUsers.contains("Bob")
                || mentionedUsers.contains("BOB");
        assertTrue("expected some case of bob", containsBobSomeCase);
    }

    @Test
    public void testGetMentionedUsersEmailNotMention() {
        Tweet tweetWithEmail = new Tweet(3, "alice", "contact me at alice@mit.edu", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithEmail));

        assertTrue("email addresses should not be considered mentions", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersAtSymbolAlone() {
        Tweet tweetWithAtSymbol = new Tweet(3, "alice", "just @ symbol or @ ", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetWithAtSymbol));

        assertTrue("@ symbol alone should not be considered mention", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersAtStartAndEnd() {
        Tweet tweetMentions = new Tweet(3, "alice", "@start talking and end with @end", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetMentions));

        assertEquals("expected two mentions", 2, mentionedUsers.size());
        boolean containsStart = mentionedUsers.contains("start") || mentionedUsers.contains("Start")
                || mentionedUsers.contains("START");
        boolean containsEnd = mentionedUsers.contains("end") || mentionedUsers.contains("End")
                || mentionedUsers.contains("END");

        assertTrue("expected mention at start", containsStart);
        assertTrue("expected mention at end", containsEnd);
    }

    @Test
    public void testGetMentionedUsersInvalidSurrounding() {
        Tweet tweetInvalid = new Tweet(3, "alice", "email@domain.com and user@name but @valid mention", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetInvalid));

        assertEquals("expected one valid mention", 1, mentionedUsers.size());
        boolean containsValid = mentionedUsers.contains("valid") || mentionedUsers.contains("Valid")
                || mentionedUsers.contains("VALID");
        assertTrue("expected mention of valid", containsValid);
    }

    @Test
    public void testGetMentionedUsersPunctuation() {
        Tweet tweetPunctuation = new Tweet(3, "alice", "Hey @bob! How are you @charlie?", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetPunctuation));

        assertEquals("expected two mentions with punctuation", 2, mentionedUsers.size());
        boolean containsBob = mentionedUsers.contains("bob") || mentionedUsers.contains("Bob")
                || mentionedUsers.contains("BOB");
        boolean containsCharlie = mentionedUsers.contains("charlie") || mentionedUsers.contains("Charlie")
                || mentionedUsers.contains("CHARLIE");

        assertTrue("expected mention of bob", containsBob);
        assertTrue("expected mention of charlie", containsCharlie);
    }

    @Test
    public void testGetMentionedUsersAlphanumericSurrounding() {
        Tweet tweetAlphanum = new Tweet(3, "alice", "abc@def and 123@456 but @valid", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetAlphanum));

        assertEquals("expected one valid mention", 1, mentionedUsers.size());
        boolean containsValid = mentionedUsers.contains("valid") || mentionedUsers.contains("Valid")
                || mentionedUsers.contains("VALID");
        assertTrue("expected mention of valid", containsValid);
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}
