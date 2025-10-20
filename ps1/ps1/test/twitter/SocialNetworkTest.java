/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * Testing strategy for SocialNetwork methods:
     * 
     * guessFollowsGraph():
     * Partition the inputs as follows:
     * - tweets.size(): 0, 1, >1
     * - mentions per tweet: 0, 1, >1
     * - tweet authors: single author, multiple authors
     * - mention patterns: no mentions, single mention, multiple mentions,
     * duplicate mentions (same user mentioned multiple times)
     * - self-mentions: author mentions themselves (should not follow themselves)
     * - case sensitivity: usernames in different cases
     * 
     * influencers():
     * Partition the inputs as follows:
     * - followsGraph.size(): 0, 1, >1
     * - followers per user: 0, 1, >1
     * - follower distribution: all users have same followers, different followers
     * - ties: multiple users with same follower count
     * - ordering: verify descending order by follower count
     */

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    /*
     * Tests for guessFollowsGraph()
     */

    // Test Case 1: Empty List of Tweets
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());

        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // Test Case 2: Tweets Without Mentions
    @Test
    public void testGuessFollowsGraphNoMentions() {
        Tweet tweet1 = new Tweet(1, "alice", "Hello world! This is a test tweet.", d1);
        Tweet tweet2 = new Tweet(2, "bob", "Another tweet without any mentions", d2);
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Graph should be empty or contain authors with empty sets
        // Since tweets have no mentions, no follow relationships exist
        for (Set<String> follows : followsGraph.values()) {
            assertTrue("expected no follows for tweets without mentions", follows.isEmpty());
        }
    }

    // Test Case 3: Single Mention
    @Test
    public void testGuessFollowsGraphSingleMention() {
        Tweet tweet = new Tweet(1, "alice", "Hey @bob how are you?", d1);
        List<Tweet> tweets = Arrays.asList(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Alice should follow Bob
        assertTrue("alice should be in the graph", followsGraph.containsKey("alice"));
        Set<String> aliceFollows = followsGraph.get("alice");
        assertEquals("alice should follow 1 person", 1, aliceFollows.size());
        assertTrue("alice should follow bob (case-insensitive)",
                aliceFollows.contains("bob") || aliceFollows.contains("BOB"));
    }

    // Test Case 4: Multiple Mentions
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        Tweet tweet = new Tweet(1, "alice", "Hey @bob and @charlie, let's meet @david!", d1);
        List<Tweet> tweets = Arrays.asList(tweet);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Alice should follow Bob, Charlie, and David
        assertTrue("alice should be in the graph", followsGraph.containsKey("alice"));
        Set<String> aliceFollows = followsGraph.get("alice");
        assertTrue("alice should follow at least 3 people", aliceFollows.size() >= 3);

        // Check for each mentioned user (case-insensitive)
        boolean hasBob = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("bob"));
        boolean hasCharlie = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("charlie"));
        boolean hasDavid = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("david"));

        assertTrue("alice should follow bob", hasBob);
        assertTrue("alice should follow charlie", hasCharlie);
        assertTrue("alice should follow david", hasDavid);
    }

    // Test Case 5: Multiple Tweets from One User
    @Test
    public void testGuessFollowsGraphMultipleTweetsOneUser() {
        Tweet tweet1 = new Tweet(1, "alice", "Hey @bob!", d1);
        Tweet tweet2 = new Tweet(2, "alice", "Hello @charlie!", d2);
        Tweet tweet3 = new Tweet(3, "alice", "Hi @david and @bob again!", d3);
        List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3);

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);

        // Alice should follow Bob, Charlie, and David
        assertTrue("alice should be in the graph", followsGraph.containsKey("alice"));
        Set<String> aliceFollows = followsGraph.get("alice");
        assertTrue("alice should follow at least 3 people", aliceFollows.size() >= 3);

        // Check for unique mentions (bob mentioned twice but should appear once)
        boolean hasBob = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("bob"));
        boolean hasCharlie = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("charlie"));
        boolean hasDavid = aliceFollows.stream().anyMatch(name -> name.equalsIgnoreCase("david"));

        assertTrue("alice should follow bob", hasBob);
        assertTrue("alice should follow charlie", hasCharlie);
        assertTrue("alice should follow david", hasDavid);
    }

    /*
     * Tests for influencers()
     */

    // Test Case 6: Empty Graph for influencers()
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);

        assertTrue("expected empty list", influencers.isEmpty());
    }

    // Test Case 7: Single User Without Followers
    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Bob should be the influencer (has 1 follower: alice)
        // Alice should be less influential (has 0 followers)
        assertFalse("influencers list should not be empty", influencers.isEmpty());
        assertTrue("should have at least 1 user", influencers.size() >= 1);

        // Bob should appear before Alice (or Alice might not appear if she has no
        // followers)
        boolean hasBob = influencers.stream().anyMatch(name -> name.equalsIgnoreCase("bob"));
        assertTrue("bob should be in influencers list", hasBob);
    }

    // Test Case 8: Single Influencer
    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alice", new HashSet<>(Arrays.asList("charlie")));
        followsGraph.put("bob", new HashSet<>(Arrays.asList("charlie")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("charlie")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Charlie should be the top influencer (3 followers)
        assertFalse("influencers list should not be empty", influencers.isEmpty());

        // Find charlie in the list (case-insensitive)
        String topInfluencer = influencers.get(0);
        assertTrue("charlie should be the top influencer",
                topInfluencer.equalsIgnoreCase("charlie"));
    }

    // Test Case 9: Multiple Influencers
    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        // Bob has 3 followers
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("charlie", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("david", new HashSet<>(Arrays.asList("bob")));

        // Eve follows Charlie (charlie has 1 follower)
        followsGraph.put("eve", new HashSet<>(Arrays.asList("charlie")));

        // Frank follows David (david has 1 follower)
        followsGraph.put("frank", new HashSet<>(Arrays.asList("david")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Verify descending order by follower count
        assertFalse("influencers list should not be empty", influencers.isEmpty());
        assertTrue("should have at least 3 influencers", influencers.size() >= 3);

        // Find positions (case-insensitive)
        int bobIndex = -1, charlieIndex = -1, davidIndex = -1;
        for (int i = 0; i < influencers.size(); i++) {
            if (influencers.get(i).equalsIgnoreCase("bob"))
                bobIndex = i;
            if (influencers.get(i).equalsIgnoreCase("charlie"))
                charlieIndex = i;
            if (influencers.get(i).equalsIgnoreCase("david"))
                davidIndex = i;
        }

        // Bob should appear before Charlie and David (bob has 3 followers, others have
        // 1)
        assertTrue("bob should be in the list", bobIndex >= 0);
        assertTrue("charlie should be in the list", charlieIndex >= 0);
        assertTrue("david should be in the list", davidIndex >= 0);
        assertTrue("bob should rank higher than charlie", bobIndex < charlieIndex);
        assertTrue("bob should rank higher than david", bobIndex < davidIndex);
    }

    // Test Case 10: Tied Influence
    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        // Bob has 2 followers
        followsGraph.put("alice", new HashSet<>(Arrays.asList("bob")));
        followsGraph.put("charlie", new HashSet<>(Arrays.asList("bob")));

        // David has 2 followers (same as Bob)
        followsGraph.put("eve", new HashSet<>(Arrays.asList("david")));
        followsGraph.put("frank", new HashSet<>(Arrays.asList("david")));

        List<String> influencers = SocialNetwork.influencers(followsGraph);

        // Both Bob and David should be in the list
        assertFalse("influencers list should not be empty", influencers.isEmpty());
        assertTrue("should have at least 2 influencers", influencers.size() >= 2);

        boolean hasBob = influencers.stream().anyMatch(name -> name.equalsIgnoreCase("bob"));
        boolean hasDavid = influencers.stream().anyMatch(name -> name.equalsIgnoreCase("david"));

        assertTrue("bob should be in influencers list", hasBob);
        assertTrue("david should be in influencers list", hasDavid);

        // Both should appear early in the list (tied for top)
        int bobIndex = -1, davidIndex = -1;
        for (int i = 0; i < influencers.size(); i++) {
            if (influencers.get(i).equalsIgnoreCase("bob"))
                bobIndex = i;
            if (influencers.get(i).equalsIgnoreCase("david"))
                davidIndex = i;
        }

        // Both should be in top positions (order between them is underdetermined)
        assertTrue("bob should be in list", bobIndex >= 0 && bobIndex < influencers.size());
        assertTrue("david should be in list", davidIndex >= 0 && davidIndex < influencers.size());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */

}
