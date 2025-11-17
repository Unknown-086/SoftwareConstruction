/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {

    // Testing strategy for Graph<String>
    // Test core functionality: creating graphs, adding/removing vertices,
    // adding/updating/deleting edges, and querying vertices/edges

    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();

    @Test(expected = AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    // Tests for add()

    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("adding new vertex should return true", graph.add("A"));
        assertTrue("graph should contain added vertex", graph.vertices().contains("A"));
    }

    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("adding duplicate vertex should return false", graph.add("A"));
    }

    // Tests for set()

    @Test
    public void testSetNewEdge() {
        Graph<String> graph = emptyInstance();
        int prevWeight = graph.set("A", "B", 5);
        assertEquals("previous weight should be 0 for new edge", 0, prevWeight);
        assertTrue("vertices should be auto-created", graph.vertices().contains("A"));
        assertTrue("vertices should be auto-created", graph.vertices().contains("B"));
    }

    @Test
    public void testSetUpdateEdge() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        int prevWeight = graph.set("A", "B", 10);
        assertEquals("should return previous weight", 5, prevWeight);
        assertEquals("new weight should be set", 10, (int) graph.targets("A").get("B"));
    }

    @Test
    public void testSetRemoveEdge() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        int prevWeight = graph.set("A", "B", 0);
        assertEquals("should return previous weight", 5, prevWeight);
        assertFalse("edge should be removed", graph.targets("A").containsKey("B"));
    }

    // Tests for remove()

    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertTrue("removing existing vertex should return true", graph.remove("A"));
        assertFalse("vertex should no longer be in graph", graph.vertices().contains("A"));
    }

    @Test
    public void testRemoveVertexWithEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("B", "C", 3);
        assertTrue(graph.remove("B"));
        assertFalse("vertex should be removed", graph.vertices().contains("B"));
        assertEquals("A should have no outgoing edges", 0, graph.targets("A").size());
        assertEquals("C should have no incoming edges", 0, graph.sources("C").size());
    }

    // Tests for vertices()

    @Test
    public void testVertices() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        Set<String> vertices = graph.vertices();
        assertEquals("should return correct number of vertices", 2, vertices.size());
        assertTrue(vertices.contains("A"));
        assertTrue(vertices.contains("B"));
    }

    // Tests for sources()

    @Test
    public void testSources() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "C", 5);
        graph.set("B", "C", 3);
        Map<String, Integer> sources = graph.sources("C");
        assertEquals("should have two sources", 2, sources.size());
        assertEquals("weight from A should be correct", 5, (int) sources.get("A"));
        assertEquals("weight from B should be correct", 3, (int) sources.get("B"));
    }

    // Tests for targets()

    @Test
    public void testTargets() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 3);
        graph.set("A", "C", 6);
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("should have two targets", 2, targets.size());
        assertEquals("weight to B should be correct", 3, (int) targets.get("B"));
        assertEquals("weight to C should be correct", 6, (int) targets.get("C"));
    }

}
