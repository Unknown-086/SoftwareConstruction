/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }

    /*
     * Testing ConcreteVerticesGraph...
     */

    // Testing strategy for ConcreteVerticesGraph.toString()
    // Test that toString provides useful output for empty and non-empty graphs

    @Test
    public void testToStringWithGraph() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        String result = graph.toString();
        assertTrue("toString should contain vertices", result.contains("A"));
        assertTrue("toString should contain vertices", result.contains("B"));
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    // Test constructor, getters, edge operations (add/remove), and defensive
    // copying

    @Test
    public void testVertexConstructorValid() {
        Vertex v = new Vertex("A");
        assertEquals("label should match", "A", v.getLabel());
        assertEquals("targets should be empty initially", 0, v.getTargets().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexConstructorNullLabel() {
        new Vertex(null);
    }

    @Test
    public void testVertexSetTarget() {
        Vertex v = new Vertex("A");
        int prev = v.setTarget("B", 5);
        assertEquals("previous weight should be 0", 0, prev);
        assertEquals("weight to B should be 5", 5, (int) v.getTargets().get("B"));
    }

    @Test
    public void testVertexUpdateTarget() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        int prev = v.setTarget("B", 10);
        assertEquals("previous weight should be 5", 5, prev);
        assertEquals("weight should be updated", 10, (int) v.getTargets().get("B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexSetTargetInvalidWeight() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 0);
    }

    @Test
    public void testVertexRemoveTarget() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        int prev = v.removeTarget("B");
        assertEquals("previous weight should be 5", 5, prev);
        assertFalse("target should be removed", v.getTargets().containsKey("B"));
    }

    @Test
    public void testVertexSetSource() {
        Vertex v = new Vertex("B");
        int prev = v.setSource("A", 7);
        assertEquals("previous weight should be 0", 0, prev);
        assertEquals("weight from A should be 7", 7, (int) v.getSources().get("A"));
    }

    @Test
    public void testVertexRemoveSource() {
        Vertex v = new Vertex("B");
        v.setSource("A", 5);
        int prev = v.removeSource("A");
        assertEquals("previous weight should be 5", 5, prev);
        assertFalse("source should be removed", v.getSources().containsKey("A"));
    }

    @Test
    public void testVertexDefensiveCopy() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        Map<String, Integer> targets = v.getTargets();
        targets.put("C", 10); // modify returned map

        // Original vertex should not be affected
        assertFalse("vertex should not be affected", v.getTargets().containsKey("C"));
    }

}
