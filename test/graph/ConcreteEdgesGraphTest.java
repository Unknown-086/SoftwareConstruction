/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    /*
     * Testing ConcreteEdgesGraph...
     */

    // Testing strategy for ConcreteEdgesGraph.toString()
    // Test that toString provides useful output for graphs

    @Test
    public void testToStringWithGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        String result = graph.toString();
        assertTrue("toString should contain vertices", result.contains("A"));
        assertTrue("toString should mention edges", result.toLowerCase().contains("edge"));
    }

    /*
     * Testing Edge...
     */

    // Testing strategy for Edge
    // Test constructor validation, getters, connectsVertices helper, and
    // immutability

    @Test
    public void testEdgeConstructorValid() {
        Edge edge = new Edge("A", "B", 5);
        assertEquals("source should be A", "A", edge.getSource());
        assertEquals("target should be B", "B", edge.getTarget());
        assertEquals("weight should be 5", 5, edge.getWeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorNullSource() {
        new Edge(null, "B", 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorNullTarget() {
        new Edge("A", null, 5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructorInvalidWeight() {
        new Edge("A", "B", 0);
    }

    @Test
    public void testEdgeGetters() {
        Edge edge = new Edge("Source", "Target", 10);
        assertEquals("getSource should work", "Source", edge.getSource());
        assertEquals("getTarget should work", "Target", edge.getTarget());
        assertEquals("getWeight should work", 10, edge.getWeight());
    }

    @Test
    public void testEdgeConnectsVertices() {
        Edge edge = new Edge("A", "B", 5);
        assertTrue("should return true for matching vertices",
                edge.connectsVertices("A", "B"));
        assertFalse("should return false for wrong vertices",
                edge.connectsVertices("C", "B"));
        assertFalse("should return false for reversed (directed)",
                edge.connectsVertices("B", "A"));
    }

    @Test
    public void testEdgeToString() {
        Edge edge = new Edge("A", "B", 5);
        String result = edge.toString();
        assertTrue("toString should contain source", result.contains("A"));
        assertTrue("toString should contain target", result.contains("B"));
        assertTrue("toString should contain weight", result.contains("5"));
    }

}
