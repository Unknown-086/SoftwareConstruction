/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>
 * PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {

    private final List<Vertex> vertices = new ArrayList<>();

    // Abstraction function:
    // AF(vertices) = a directed weighted graph where each Vertex in vertices
    // represents a vertex in the graph with its label, and the edges of the graph
    // are represented by the targets maps in each Vertex object
    //
    // Representation invariant:
    // vertices != null
    // all elements in vertices are non-null
    // no two Vertex objects in vertices have the same label
    // for each Vertex v in vertices:
    // - all targets in v exist as vertices in the graph
    // - all sources in v exist as vertices in the graph
    //
    // Safety from rep exposure:
    // vertices is private and final, never returned directly
    // vertices() returns a new Set containing only the String labels
    // sources() and targets() return defensive copies of the edge maps
    // Vertex class is package-private and not exposed to clients

    /**
     * Create an empty graph.
     */
    public ConcreteVerticesGraph() {
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert vertices != null : "vertices list cannot be null";

        Set<String> labels = new HashSet<>();
        for (Vertex v : vertices) {
            assert v != null : "vertex cannot be null";
            assert !labels.contains(v.getLabel()) : "duplicate vertex label";
            labels.add(v.getLabel());
        }

        // Check that all referenced vertices exist
        for (Vertex v : vertices) {
            for (String target : v.getTargets().keySet()) {
                assert labels.contains(target) : "target vertex must exist in graph";
            }
            for (String source : v.getSources().keySet()) {
                assert labels.contains(source) : "source vertex must exist in graph";
            }
        }
    }

    /**
     * Find a vertex by its label.
     * 
     * @param label the label to search for
     * @return the Vertex with the given label, or null if not found
     */
    private Vertex findVertex(String label) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(label)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public boolean add(String vertex) {
        if (findVertex(vertex) != null) {
            return false;
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    @Override
    public int set(String source, String target, int weight) {
        // Ensure source and target vertices exist
        Vertex sourceVertex = findVertex(source);
        if (sourceVertex == null) {
            sourceVertex = new Vertex(source);
            vertices.add(sourceVertex);
        }

        Vertex targetVertex = findVertex(target);
        if (targetVertex == null) {
            targetVertex = new Vertex(target);
            vertices.add(targetVertex);
        }

        int previousWeight = 0;

        if (weight > 0) {
            // Add or update edge
            previousWeight = sourceVertex.setTarget(target, weight);
            targetVertex.setSource(source, weight);
        } else {
            // Remove edge
            previousWeight = sourceVertex.removeTarget(target);
            targetVertex.removeSource(source);
        }

        checkRep();
        return previousWeight;
    }

    @Override
    public boolean remove(String vertex) {
        Vertex toRemove = findVertex(vertex);
        if (toRemove == null) {
            return false;
        }

        // Remove all edges to and from this vertex
        for (Vertex v : vertices) {
            v.removeTarget(vertex);
            v.removeSource(vertex);
        }

        vertices.remove(toRemove);
        checkRep();
        return true;
    }

    @Override
    public Set<String> vertices() {
        Set<String> result = new HashSet<>();
        for (Vertex v : vertices) {
            result.add(v.getLabel());
        }
        return Collections.unmodifiableSet(result);
    }

    @Override
    public Map<String, Integer> sources(String target) {
        Vertex targetVertex = findVertex(target);
        if (targetVertex == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(targetVertex.getSources());
    }

    @Override
    public Map<String, Integer> targets(String source) {
        Vertex sourceVertex = findVertex(source);
        if (sourceVertex == null) {
            return Collections.emptyMap();
        }
        return new HashMap<>(sourceVertex.getTargets());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices:\n");
        for (Vertex v : vertices) {
            sb.append("  ").append(v.toString()).append("\n");
        }
        return sb.toString();
    }

}

/**
 * A mutable vertex in a directed weighted graph.
 * Each vertex has a label and maintains maps of its incoming and outgoing
 * edges.
 * 
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>
 * PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {

    private final String label;
    private final Map<String, Integer> targets = new HashMap<>();
    private final Map<String, Integer> sources = new HashMap<>();

    // Abstraction function:
    // AF(label, targets, sources) = a vertex labeled 'label' with:
    // - outgoing edges to each vertex in targets.keySet() with weights
    // targets.values()
    // - incoming edges from each vertex in sources.keySet() with weights
    // sources.values()
    //
    // Representation invariant:
    // label != null and label is not empty
    // targets != null, sources != null
    // all keys in targets and sources are non-null
    // all values in targets and sources are positive (> 0)
    //
    // Safety from rep exposure:
    // label is private, final, and immutable (String)
    // targets and sources are private and final
    // getTargets() and getSources() return defensive copies
    // all modifications go through controlled methods

    /**
     * Create a new vertex with the given label.
     * 
     * @param label the label for this vertex, must be non-null and non-empty
     */
    public Vertex(String label) {
        if (label == null || label.isEmpty()) {
            throw new IllegalArgumentException("label must be non-null and non-empty");
        }
        this.label = label;
        checkRep();
    }

    /**
     * Check that the rep invariant is maintained.
     */
    private void checkRep() {
        assert label != null && !label.isEmpty() : "label must be non-null and non-empty";
        assert targets != null : "targets map cannot be null";
        assert sources != null : "sources map cannot be null";

        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
            assert entry.getKey() != null : "target label cannot be null";
            assert entry.getValue() > 0 : "edge weight must be positive";
        }

        for (Map.Entry<String, Integer> entry : sources.entrySet()) {
            assert entry.getKey() != null : "source label cannot be null";
            assert entry.getValue() > 0 : "edge weight must be positive";
        }
    }

    /**
     * Get the label of this vertex.
     * 
     * @return the label of this vertex
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the outgoing edges from this vertex.
     * 
     * @return a map from target vertex labels to edge weights
     */
    public Map<String, Integer> getTargets() {
        return new HashMap<>(targets);
    }

    /**
     * Get the incoming edges to this vertex.
     * 
     * @return a map from source vertex labels to edge weights
     */
    public Map<String, Integer> getSources() {
        return new HashMap<>(sources);
    }

    /**
     * Add or update an outgoing edge from this vertex.
     * 
     * @param target the target vertex label
     * @param weight the weight of the edge, must be positive
     * @return the previous weight of the edge, or 0 if it didn't exist
     */
    public int setTarget(String target, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive");
        }
        Integer prev = targets.put(target, weight);
        checkRep();
        return prev == null ? 0 : prev;
    }

    /**
     * Add or update an incoming edge to this vertex.
     * 
     * @param source the source vertex label
     * @param weight the weight of the edge, must be positive
     * @return the previous weight of the edge, or 0 if it didn't exist
     */
    public int setSource(String source, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive");
        }
        Integer prev = sources.put(source, weight);
        checkRep();
        return prev == null ? 0 : prev;
    }

    /**
     * Remove an outgoing edge from this vertex.
     * 
     * @param target the target vertex label
     * @return the previous weight of the edge, or 0 if it didn't exist
     */
    public int removeTarget(String target) {
        Integer prev = targets.remove(target);
        checkRep();
        return prev == null ? 0 : prev;
    }

    /**
     * Remove an incoming edge to this vertex.
     * 
     * @param source the source vertex label
     * @return the previous weight of the edge, or 0 if it didn't exist
     */
    public int removeSource(String source) {
        Integer prev = sources.remove(source);
        checkRep();
        return prev == null ? 0 : prev;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(label).append(": [");
        boolean first = true;
        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
            if (!first)
                sb.append(", ");
            sb.append("→").append(entry.getKey()).append("(").append(entry.getValue()).append(")");
            first = false;
        }
        sb.append("]");
        return sb.toString();
    }

}
