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
    //
    // Partition for add(vertex):
    // - graph is empty, graph has vertices
    // - vertex is new, vertex already exists
    //
    // Partition for set(source, target, weight):
    // - weight: 0 (remove edge), positive (add/update edge)
    // - edge exists, edge doesn't exist
    // - source/target: exist in graph, don't exist in graph
    // - self-loops: source == target, source != target
    //
    // Partition for remove(vertex):
    // - vertex exists, vertex doesn't exist
    // - vertex has: no edges, incoming edges only, outgoing edges only, both
    //
    // Partition for vertices():
    // - graph: empty, has one vertex, has multiple vertices
    //
    // Partition for sources(target):
    // - target: not in graph, in graph with no incoming edges,
    // in graph with one source, in graph with multiple sources
    //
    // Partition for targets(source):
    // - source: not in graph, in graph with no outgoing edges,
    // in graph with one target, in graph with multiple targets

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
    public void testAddSingleVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("adding new vertex should return true", graph.add("A"));
        assertTrue("graph should contain added vertex", graph.vertices().contains("A"));
        assertEquals("graph should have one vertex", 1, graph.vertices().size());
    }

    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("adding duplicate vertex should return false", graph.add("A"));
        assertEquals("graph should still have one vertex", 1, graph.vertices().size());
    }

    @Test
    public void testAddMultipleVertices() {
        Graph<String> graph = emptyInstance();
        assertTrue(graph.add("A"));
        assertTrue(graph.add("B"));
        assertTrue(graph.add("C"));
        assertEquals("graph should have three vertices", 3, graph.vertices().size());
        assertTrue(graph.vertices().contains("A"));
        assertTrue(graph.vertices().contains("B"));
        assertTrue(graph.vertices().contains("C"));
    }

    // Tests for set()

    @Test
    public void testSetNewEdgeCreatesVertices() {
        Graph<String> graph = emptyInstance();
        int prevWeight = graph.set("A", "B", 5);
        assertEquals("previous weight should be 0 for new edge", 0, prevWeight);
        assertTrue("source vertex should be added", graph.vertices().contains("A"));
        assertTrue("target vertex should be added", graph.vertices().contains("B"));
    }

    @Test
    public void testSetUpdateEdgeWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        int prevWeight = graph.set("A", "B", 10);
        assertEquals("should return previous weight", 5, prevWeight);
        assertEquals("new weight should be set", 10, (int) graph.targets("A").get("B"));
    }

    @Test
    public void testSetRemoveEdgeWithZeroWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        int prevWeight = graph.set("A", "B", 0);
        assertEquals("should return previous weight", 5, prevWeight);
        assertFalse("edge should be removed", graph.targets("A").containsKey("B"));
        assertTrue("vertices should remain", graph.vertices().contains("A"));
        assertTrue("vertices should remain", graph.vertices().contains("B"));
    }

    @Test
    public void testSetRemoveNonexistentEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        int prevWeight = graph.set("A", "B", 0);
        assertEquals("removing nonexistent edge should return 0", 0, prevWeight);
    }

    @Test
    public void testSetSelfLoop() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "A", 3);
        assertTrue("vertex should exist", graph.vertices().contains("A"));
        assertTrue("self-loop should exist in targets", graph.targets("A").containsKey("A"));
        assertTrue("self-loop should exist in sources", graph.sources("A").containsKey("A"));
        assertEquals("self-loop weight should be correct", 3, (int) graph.targets("A").get("A"));
    }

    @Test
    public void testSetMultipleEdgesFromSameSource() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 2);
        graph.set("A", "C", 3);
        graph.set("A", "D", 4);
        assertEquals("should have 3 outgoing edges", 3, graph.targets("A").size());
        assertTrue(graph.targets("A").containsKey("B"));
        assertTrue(graph.targets("A").containsKey("C"));
        assertTrue(graph.targets("A").containsKey("D"));
    }

    @Test
    public void testSetMultipleEdgesToSameTarget() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "D", 2);
        graph.set("B", "D", 3);
        graph.set("C", "D", 4);
        assertEquals("should have 3 incoming edges", 3, graph.sources("D").size());
        assertTrue(graph.sources("D").containsKey("A"));
        assertTrue(graph.sources("D").containsKey("B"));
        assertTrue(graph.sources("D").containsKey("C"));
    }

    // Tests for remove()

    @Test
    public void testRemoveExistingVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertTrue("removing existing vertex should return true", graph.remove("A"));
        assertFalse("vertex should no longer be in graph", graph.vertices().contains("A"));
        assertEquals("graph should be empty", 0, graph.vertices().size());
    }

    @Test
    public void testRemoveNonexistentVertex() {
        Graph<String> graph = emptyInstance();
        assertFalse("removing nonexistent vertex should return false", graph.remove("A"));
    }

    @Test
    public void testRemoveVertexWithOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("A", "C", 3);
        assertTrue(graph.remove("A"));
        assertFalse("vertex should be removed", graph.vertices().contains("A"));
        assertEquals("B should have no incoming edges", 0, graph.sources("B").size());
        assertEquals("C should have no incoming edges", 0, graph.sources("C").size());
    }

    @Test
    public void testRemoveVertexWithIncomingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "C", 5);
        graph.set("B", "C", 3);
        assertTrue(graph.remove("C"));
        assertFalse("vertex should be removed", graph.vertices().contains("C"));
        assertEquals("A should have no outgoing edges", 0, graph.targets("A").size());
        assertEquals("B should have no outgoing edges", 0, graph.targets("B").size());
    }

    @Test
    public void testRemoveVertexWithBothIncomingAndOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("B", "C", 3);
        graph.set("D", "B", 2);
        assertTrue(graph.remove("B"));
        assertFalse("vertex should be removed", graph.vertices().contains("B"));
        assertFalse("edge A->B should be removed", graph.targets("A").containsKey("B"));
        assertFalse("edge B->C should be removed", graph.sources("C").containsKey("B"));
        assertFalse("edge D->B should be removed", graph.targets("D").containsKey("B"));
    }

    // Tests for vertices()

    @Test
    public void testVerticesEmptyGraph() {
        Graph<String> graph = emptyInstance();
        assertEquals("empty graph should have no vertices",
                Collections.emptySet(), graph.vertices());
    }

    @Test
    public void testVerticesAfterAdding() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        Set<String> vertices = graph.vertices();
        assertEquals("should return correct number of vertices", 3, vertices.size());
        assertTrue(vertices.contains("A"));
        assertTrue(vertices.contains("B"));
        assertTrue(vertices.contains("C"));
    }

    @Test
    public void testVerticesAfterAddingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("X", "Y", 1);
        Set<String> vertices = graph.vertices();
        assertEquals("both vertices should be added", 2, vertices.size());
        assertTrue(vertices.contains("X"));
        assertTrue(vertices.contains("Y"));
    }

    // Tests for sources()

    @Test
    public void testSourcesVertexNotInGraph() {
        Graph<String> graph = emptyInstance();
        Map<String, Integer> sources = graph.sources("A");
        assertEquals("nonexistent vertex should have no sources", 0, sources.size());
    }

    @Test
    public void testSourcesVertexWithNoIncomingEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        Map<String, Integer> sources = graph.sources("A");
        assertEquals("vertex with no incoming edges should have empty sources",
                0, sources.size());
    }

    @Test
    public void testSourcesWithOneSource() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 7);
        Map<String, Integer> sources = graph.sources("B");
        assertEquals("should have one source", 1, sources.size());
        assertTrue("A should be a source", sources.containsKey("A"));
        assertEquals("weight should be correct", 7, (int) sources.get("A"));
    }

    @Test
    public void testSourcesWithMultipleSources() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "D", 2);
        graph.set("B", "D", 5);
        graph.set("C", "D", 8);
        Map<String, Integer> sources = graph.sources("D");
        assertEquals("should have three sources", 3, sources.size());
        assertEquals("weight from A should be correct", 2, (int) sources.get("A"));
        assertEquals("weight from B should be correct", 5, (int) sources.get("B"));
        assertEquals("weight from C should be correct", 8, (int) sources.get("C"));
    }

    // Tests for targets()

    @Test
    public void testTargetsVertexNotInGraph() {
        Graph<String> graph = emptyInstance();
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("nonexistent vertex should have no targets", 0, targets.size());
    }

    @Test
    public void testTargetsVertexWithNoOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("vertex with no outgoing edges should have empty targets",
                0, targets.size());
    }

    @Test
    public void testTargetsWithOneTarget() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 9);
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("should have one target", 1, targets.size());
        assertTrue("B should be a target", targets.containsKey("B"));
        assertEquals("weight should be correct", 9, (int) targets.get("B"));
    }

    @Test
    public void testTargetsWithMultipleTargets() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 3);
        graph.set("A", "C", 6);
        graph.set("A", "D", 9);
        Map<String, Integer> targets = graph.targets("A");
        assertEquals("should have three targets", 3, targets.size());
        assertEquals("weight to B should be correct", 3, (int) targets.get("B"));
        assertEquals("weight to C should be correct", 6, (int) targets.get("C"));
        assertEquals("weight to D should be correct", 9, (int) targets.get("D"));
    }

    // Integration tests

    @Test
    public void testComplexGraphOperations() {
        Graph<String> graph = emptyInstance();

        // Build a graph: A -> B -> C
        // | ^
        // v |
        // D ---+
        graph.set("A", "B", 1);
        graph.set("B", "C", 2);
        graph.set("A", "D", 3);
        graph.set("D", "C", 4);

        assertEquals("should have 4 vertices", 4, graph.vertices().size());
        assertEquals("A should have 2 targets", 2, graph.targets("A").size());
        assertEquals("C should have 2 sources", 2, graph.sources("C").size());

        // Update an edge
        graph.set("A", "B", 10);
        assertEquals("edge weight should be updated", 10, (int) graph.targets("A").get("B"));

        // Remove an edge
        graph.set("D", "C", 0);
        assertFalse("edge D->C should be removed", graph.targets("D").containsKey("C"));
        assertEquals("C should have 1 source now", 1, graph.sources("C").size());

        // Remove a vertex
        graph.remove("B");
        assertEquals("should have 3 vertices", 3, graph.vertices().size());
        assertFalse("A->B edge should be gone", graph.targets("A").containsKey("B"));
    }

}
