/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {

    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();

    // Abstraction function:
    // AF(vertices, edges) = a directed weighted graph where:
    // - the set of vertices in the graph = vertices
    // - for each Edge e in edges, there exists a directed edge from
    // e.getSource() to e.getTarget() with weight e.getWeight()
    //
    // Representation invariant:
    // vertices != null
    // edges != null
    // no null elements in vertices
    // no null elements in edges
    // for all edges e: e.getSource() and e.getTarget() are in vertices
    // no duplicate edges (same source and target pair)
    // all edge weights are positive
    //
    // Safety from rep exposure:
    // vertices and edges are private and final
    // vertices() returns an unmodifiable defensive copy
    // sources() and targets() return new HashMap objects
    // Edge objects are immutable, so returning them is safe
    // Never return vertices or edges directly

    /**
     * Create an empty graph.
     */
    public ConcreteEdgesGraph() {
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert vertices != null : "vertices set cannot be null";
        assert edges != null : "edges list cannot be null";

        for (String v : vertices) {
            assert v != null : "vertex cannot be null";
        }

        for (Edge e : edges) {
            assert e != null : "edge cannot be null";
            assert vertices.contains(e.getSource()) : "edge source must exist in vertices";
            assert vertices.contains(e.getTarget()) : "edge target must exist in vertices";
        }

        // Check no duplicate edges
        for (int i = 0; i < edges.size(); i++) {
            for (int j = i + 1; j < edges.size(); j++) {
                Edge e1 = edges.get(i);
                Edge e2 = edges.get(j);
                assert !e1.connectsVertices(e2.getSource(), e2.getTarget()) : "duplicate edges not allowed";
            }
        }
    }

    /**
     * Find an edge between two vertices.
     * 
     * @param source the source vertex
     * @param target the target vertex
     * @return the Edge from source to target, or null if no such edge exists
     */
    private Edge findEdge(String source, String target) {
        for (Edge e : edges) {
            if (e.connectsVertices(source, target)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public boolean add(String vertex) {
        if (vertices.contains(vertex)) {
            return false;
        }
        vertices.add(vertex);
        checkRep();
        return true;
    }

    @Override
    public int set(String source, String target, int weight) {
        // Ensure vertices exist
        vertices.add(source);
        vertices.add(target);

        Edge existingEdge = findEdge(source, target);
        int previousWeight = 0;

        if (existingEdge != null) {
            previousWeight = existingEdge.getWeight();
            edges.remove(existingEdge);
        }

        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }

        checkRep();
        return previousWeight;
    }

    @Override
    public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) {
            return false;
        }

        vertices.remove(vertex);

        // Remove all edges connected to this vertex
        Iterator<Edge> iterator = edges.iterator();
        while (iterator.hasNext()) {
            Edge e = iterator.next();
            if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
                iterator.remove();
            }
        }

        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        return Collections.unmodifiableSet(new HashSet<>(vertices));
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getTarget().equals(target)) {
                result.put(e.getSource(), e.getWeight());
            }
        }
        return result;
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> result = new HashMap<>();
        for (Edge e : edges) {
            if (e.getSource().equals(source)) {
                result.put(e.getTarget(), e.getWeight());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices and ")
                .append(edges.size()).append(" edges:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");
        for (Edge e : edges) {
            sb.append("  ").append(e.toString()).append("\n");
        }
        return sb.toString();
    }

}

/**
 * An immutable directed weighted edge in a graph.
 * Represents a directed edge from a source vertex to a target vertex with a
 * positive weight.
 * 
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {

    private final String source;
    private final String target;
    private final int weight;

    // Abstraction function:
    // AF(source, target, weight) = a directed edge from vertex 'source'
    // to vertex 'target' with positive weight 'weight'
    //
    // Representation invariant:
    // source != null
    // target != null
    // weight > 0
    //
    // Safety from rep exposure:
    // All fields are private and final
    // source and target are Strings (immutable type)
    // weight is int (primitive type)
    // No mutator methods exist
    // Constructor validates inputs

    /**
     * Create a new directed edge.
     * 
     * @param source the source vertex, must be non-null
     * @param target the target vertex, must be non-null
     * @param weight the weight of the edge, must be positive
     * @throws IllegalArgumentException if source or target is null, or weight is
     *                                  non-positive
     */
    public Edge(String source, String target, int weight) {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive");
        }

        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert source != null : "source cannot be null";
        assert target != null : "target cannot be null";
        assert weight > 0 : "weight must be positive";
    }

    /**
     * Get the source vertex of this edge.
     * 
     * @return the source vertex
     */
    public String getSource() {
        return source;
    }

    /**
     * Get the target vertex of this edge.
     * 
     * @return the target vertex
     */
    public String getTarget() {
        return target;
    }

    /**
     * Get the weight of this edge.
     * 
     * @return the weight of this edge
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Check if this edge connects the given source and target vertices.
     * 
     * @param src the source vertex to check
     * @param tgt the target vertex to check
     * @return true if this edge goes from src to tgt, false otherwise
     */
    public boolean connectsVertices(String src, String tgt) {
        return source.equals(src) && target.equals(tgt);
    }

    @Override
    public String toString() {
        return source + " → " + target + " (" + weight + ")";
    }

}
