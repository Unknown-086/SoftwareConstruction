package graph;

/**
 * Demo program to test the toString() methods of both graph implementations.
 */
public class ToStringDemo {

    public static void main(String[] args) {
        System.out.println("=== ConcreteVerticesGraph toString() Demo ===\n");
        testConcreteVerticesGraph();

        System.out.println("\n\n=== ConcreteEdgesGraph toString() Demo ===\n");
        testConcreteEdgesGraph();
    }

    private static void testConcreteVerticesGraph() {
        Graph<String> graph = new ConcreteVerticesGraph();

        // Empty graph
        System.out.println("Empty graph:");
        System.out.println(graph.toString());

        // Add some vertices
        graph.add("A");
        graph.add("B");
        graph.add("C");
        System.out.println("\nAfter adding vertices A, B, C:");
        System.out.println(graph.toString());

        // Add some edges
        graph.set("A", "B", 5);
        graph.set("A", "C", 3);
        graph.set("B", "C", 2);
        graph.set("C", "A", 7);
        System.out.println("\nAfter adding edges:");
        System.out.println("  A → B (weight 5)");
        System.out.println("  A → C (weight 3)");
        System.out.println("  B → C (weight 2)");
        System.out.println("  C → A (weight 7)");
        System.out.println("\nGraph representation:");
        System.out.println(graph.toString());

        // Update an edge
        graph.set("A", "B", 10);
        System.out.println("\nAfter updating A → B to weight 10:");
        System.out.println(graph.toString());

        // Remove an edge
        graph.set("B", "C", 0);
        System.out.println("\nAfter removing edge B → C:");
        System.out.println(graph.toString());

        // Remove a vertex
        graph.remove("C");
        System.out.println("\nAfter removing vertex C (and its edges):");
        System.out.println(graph.toString());
    }

    private static void testConcreteEdgesGraph() {
        Graph<String> graph = new ConcreteEdgesGraph();

        // Empty graph
        System.out.println("Empty graph:");
        System.out.println(graph.toString());

        // Add some vertices
        graph.add("X");
        graph.add("Y");
        graph.add("Z");
        System.out.println("\nAfter adding vertices X, Y, Z:");
        System.out.println(graph.toString());

        // Add some edges
        graph.set("X", "Y", 8);
        graph.set("X", "Z", 4);
        graph.set("Y", "Z", 6);
        graph.set("Z", "X", 9);
        System.out.println("\nAfter adding edges:");
        System.out.println("  X → Y (weight 8)");
        System.out.println("  X → Z (weight 4)");
        System.out.println("  Y → Z (weight 6)");
        System.out.println("  Z → X (weight 9)");
        System.out.println("\nGraph representation:");
        System.out.println(graph.toString());

        // Update an edge
        graph.set("X", "Y", 15);
        System.out.println("\nAfter updating X → Y to weight 15:");
        System.out.println(graph.toString());

        // Remove an edge
        graph.set("Y", "Z", 0);
        System.out.println("\nAfter removing edge Y → Z:");
        System.out.println(graph.toString());

        // Remove a vertex
        graph.remove("Z");
        System.out.println("\nAfter removing vertex Z (and its edges):");
        System.out.println(graph.toString());
    }
}
