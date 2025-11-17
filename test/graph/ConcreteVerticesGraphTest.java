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
    // - empty graph
    // - graph with vertices only (no edges)
    // - graph with vertices and edges
    // - graph with multiple edges per vertex

    @Test
    public void testToStringEmptyGraph() {
        Graph<String> graph = emptyInstance();
        String result = graph.toString();
        assertTrue("toString should mention 0 vertices", result.contains("0 vertices"));
    }

    @Test
    public void testToStringGraphWithVerticesOnly() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        String result = graph.toString();
        assertTrue("toString should mention 2 vertices", result.contains("2 vertices"));
        assertTrue("toString should contain vertex A", result.contains("A"));
        assertTrue("toString should contain vertex B", result.contains("B"));
    }

    @Test
    public void testToStringGraphWithEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("B", "C", 3);
        String result = graph.toString();
        assertTrue("toString should contain vertex A", result.contains("A"));
        assertTrue("toString should contain vertex B", result.contains("B"));
        assertTrue("toString should contain vertex C", result.contains("C"));
        assertTrue("toString should contain edge weight 5", result.contains("5"));
        assertTrue("toString should contain edge weight 3", result.contains("3"));
    }

    @Test
    public void testToStringGraphWithMultipleEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("A", "C", 2);
        graph.set("A", "D", 3);
        String result = graph.toString();
        assertTrue("toString should contain all edges", result.contains("B"));
        assertTrue("toString should contain all edges", result.contains("C"));
        assertTrue("toString should contain all edges", result.contains("D"));
    }

    /*
     * Testing Vertex...
     */

    // Testing strategy for Vertex
    // constructor:
    // - valid label
    // - null label (should throw exception)
    // - empty label (should throw exception)
    // getLabel():
    // - returns correct label
    // setTarget/setSource:
    // - add new edge
    // - update existing edge
    // - positive weights only
    // removeTarget/removeSource:
    // - remove existing edge
    // - remove non-existent edge
    // getTargets/getSources:
    // - empty maps initially
    // - maps with edges
    // - defensive copying (mutations don't affect vertex)
    // toString():
    // - vertex with no edges
    // - vertex with one edge
    // - vertex with multiple edges

    @Test
    public void testVertexConstructorValid() {
        Vertex v = new Vertex("A");
        assertEquals("label should match", "A", v.getLabel());
        assertEquals("targets should be empty initially", 0, v.getTargets().size());
        assertEquals("sources should be empty initially", 0, v.getSources().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexConstructorNullLabel() {
        new Vertex(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexConstructorEmptyLabel() {
        new Vertex("");
    }

    @Test
    public void testVertexGetLabel() {
        Vertex v = new Vertex("TestVertex");
        assertEquals("getLabel should return correct label", "TestVertex", v.getLabel());
    }

    @Test
    public void testVertexSetTargetNew() {
        Vertex v = new Vertex("A");
        int prev = v.setTarget("B", 5);
        assertEquals("previous weight should be 0", 0, prev);
        assertTrue("targets should contain B", v.getTargets().containsKey("B"));
        assertEquals("weight to B should be 5", 5, (int) v.getTargets().get("B"));
    }

    @Test
    public void testVertexSetTargetUpdate() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        int prev = v.setTarget("B", 10);
        assertEquals("previous weight should be 5", 5, prev);
        assertEquals("weight to B should be updated to 10", 10, (int) v.getTargets().get("B"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexSetTargetZeroWeight() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVertexSetTargetNegativeWeight() {
        Vertex v = new Vertex("A");
        v.setTarget("B", -5);
    }

    @Test
    public void testVertexSetSourceNew() {
        Vertex v = new Vertex("B");
        int prev = v.setSource("A", 7);
        assertEquals("previous weight should be 0", 0, prev);
        assertTrue("sources should contain A", v.getSources().containsKey("A"));
        assertEquals("weight from A should be 7", 7, (int) v.getSources().get("A"));
    }

    @Test
    public void testVertexSetSourceUpdate() {
        Vertex v = new Vertex("B");
        v.setSource("A", 7);
        int prev = v.setSource("A", 14);
        assertEquals("previous weight should be 7", 7, prev);
        assertEquals("weight from A should be updated to 14", 14, (int) v.getSources().get("A"));
    }

    @Test
    public void testVertexRemoveTargetExisting() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        int prev = v.removeTarget("B");
        assertEquals("previous weight should be 5", 5, prev);
        assertFalse("targets should not contain B", v.getTargets().containsKey("B"));
    }

    @Test
    public void testVertexRemoveTargetNonExistent() {
        Vertex v = new Vertex("A");
        int prev = v.removeTarget("B");
        assertEquals("previous weight should be 0", 0, prev);
    }

    @Test
    public void testVertexRemoveSourceExisting() {
        Vertex v = new Vertex("B");
        v.setSource("A", 5);
        int prev = v.removeSource("A");
        assertEquals("previous weight should be 5", 5, prev);
        assertFalse("sources should not contain A", v.getSources().containsKey("A"));
    }

    @Test
    public void testVertexRemoveSourceNonExistent() {
        Vertex v = new Vertex("B");
        int prev = v.removeSource("A");
        assertEquals("previous weight should be 0", 0, prev);
    }

    @Test
    public void testVertexGetTargetsDefensiveCopy() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        Map<String, Integer> targets = v.getTargets();
        targets.put("C", 10); // modify returned map

        // Original vertex should not be affected
        assertFalse("vertex targets should not be affected", v.getTargets().containsKey("C"));
        assertEquals("vertex should still have 1 target", 1, v.getTargets().size());
    }

    @Test
    public void testVertexGetSourcesDefensiveCopy() {
        Vertex v = new Vertex("B");
        v.setSource("A", 5);
        Map<String, Integer> sources = v.getSources();
        sources.put("C", 10); // modify returned map

        // Original vertex should not be affected
        assertFalse("vertex sources should not be affected", v.getSources().containsKey("C"));
        assertEquals("vertex should still have 1 source", 1, v.getSources().size());
    }

    @Test
    public void testVertexToStringNoEdges() {
        Vertex v = new Vertex("A");
        String result = v.toString();
        assertTrue("toString should contain label", result.contains("A"));
    }

    @Test
    public void testVertexToStringOneEdge() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 5);
        String result = v.toString();
        assertTrue("toString should contain label A", result.contains("A"));
        assertTrue("toString should contain target B", result.contains("B"));
        assertTrue("toString should contain weight 5", result.contains("5"));
    }

    @Test
    public void testVertexToStringMultipleEdges() {
        Vertex v = new Vertex("A");
        v.setTarget("B", 1);
        v.setTarget("C", 2);
        v.setTarget("D", 3);
        String result = v.toString();
        assertTrue("toString should contain all targets", result.contains("B"));
        assertTrue("toString should contain all targets", result.contains("C"));
        assertTrue("toString should contain all targets", result.contains("D"));
    }

}
