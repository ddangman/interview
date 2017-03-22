package com.interview.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * http://www.geeksforgeeks.org/detect-cycle-in-a-graph/
 * Time complexity: O(E+V)
 * Space complexity: O(V)
 */
public class CycleInDirectedGraph {

    public boolean hasCycle(Graph<Integer> graph) {
        // unvisited vertices
        Set<Vertex<Integer>> whiteSet = new HashSet<>();
        // currently explored vertices
        Set<Vertex<Integer>> graySet = new HashSet<>();
        // completely explored vertices
        Set<Vertex<Integer>> blackSet = new HashSet<>();

        // initialize whiteSet with all graph vertices
        for (Vertex<Integer> vertex : graph.getAllVertex()) {
            whiteSet.add(vertex);
        }

        // depth-first search whiteSet until empty
        while (whiteSet.size() > 0) {
            Vertex<Integer> current = whiteSet.iterator().next();
            if(dfs(current, whiteSet, graySet, blackSet)) {
                return true;
            }
        }
        return false;
    }

    // depth-first search
    private boolean dfs(Vertex<Integer> current, Set<Vertex<Integer>> whiteSet,
                        Set<Vertex<Integer>> graySet, Set<Vertex<Integer>> blackSet ) {
        //move current to gray set from white set and then explore it.
        moveVertex(current, whiteSet, graySet);
        for(Vertex<Integer> neighbor : current.getAdjacentVertexes()) {
            //if in black set means already explored so continue.
            if (blackSet.contains(neighbor)) {
                continue;
            }
            //if in gray set then cycle found.
            if (graySet.contains(neighbor)) {
                return true;
            }
            // neighbor vertex will enter recursion
            if(dfs(neighbor, whiteSet, graySet, blackSet)) {
                return true;
            }
        }
        //move vertex from gray set to black set when done exploring.
        moveVertex(current, graySet, blackSet);
        return false;
    } // no more neighbors to explore

    private void moveVertex(Vertex<Integer> vertex, Set<Vertex<Integer>> sourceSet,
                            Set<Vertex<Integer>> destinationSet) {
        sourceSet.remove(vertex);
        destinationSet.add(vertex);
    }

    public static void main(String args[]){
        Graph<Integer> graph = new Graph<>(true);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(4, 1);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 4);
        CycleInDirectedGraph cdg = new CycleInDirectedGraph();
        System.out.println(cdg.hasCycle(graph));
    }
}
