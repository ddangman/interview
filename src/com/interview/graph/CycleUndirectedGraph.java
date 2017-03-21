package com.interview.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * Date 10/11/2014
 * @author Tushar Roy
 *
 * Given an undirected graph find cycle in this graph.
 *
 * Solution
 * This can be solved in many ways.
 * Below is the code to solve it using disjoint sets and DFS.
 *
 * Runtime and space complexity for both the techniques is O(v)
 * where v is total number of vertices in the graph.
 */
public class CycleUndirectedGraph<T> {

    public boolean hasCycleUsingDisjointSets(Graph<T> graph){
        DisjointSet disjointSet = new DisjointSet();
        
        for(Vertex<T> vertex : graph.getAllVertex()){
            // make single set for every vertex
            disjointSet.makeSet(vertex.getId());
        }
        
        // iterate every edge to find cycle
        // by the time edge visited = number of vertices,
        //   a cycle would be found. Thus time complexity
        //   is limited to number of vertices. O(v)
        for(Edge<T> edge : graph.getAllEdges()){
            long parent1 = disjointSet.findSet(edge.getVertex1().getId());
            long parent2 = disjointSet.findSet(edge.getVertex2().getId());
            // if both parents belong to same set, 
            // current edge has created a cycle
            if(parent1 == parent2){
                return true;
            }
            // combine disjoined vertices into same set
            disjointSet.union(edge.getVertex1().getId(), edge.getVertex2().getId());
        }
        return false;
    }
    
    public boolean hasCycleDFS(Graph<T> graph){
        Set<Vertex<T>> visited = new HashSet<Vertex<T>>();
        for(Vertex<T> vertex : graph.getAllVertex()){
            if(visited.contains(vertex)){
                continue;
            }
            // enter recursion until all vertices are explored
            boolean flag = hasCycleDFSUtil(vertex, visited, null);
            if(flag){
                return true;
            }
        }
        return false;
    }
    
    public boolean hasCycleDFSUtil(Vertex<T> vertex, Set<Vertex<T>> visited,Vertex<T> parent){
        // log vertex as visited
        visited.add(vertex);
        for(Vertex<T> adj : vertex.getAdjacentVertexes()){
            //prevents returning to previous vertex
            if(adj.equals(parent)){
                continue;
            }
            // if visitedSet already contains adjacentVertex,
            // there is another way to adjacentVertex
            if(visited.contains(adj)){
                // cycle found
                return true;
            }
            // pass currentVertex to prevent adjacentVertex from
            // revisiting currentVertex
            boolean hasCycle = hasCycleDFSUtil(adj,visited,vertex);
            if(hasCycle){
                return true;
            }
        }
        return false; // depth-first search complete,
    } // method will be called again if other vertices need to be explored
    
    public static void main(String args[]){
        
        CycleUndirectedGraph<Integer> cycle = new CycleUndirectedGraph<Integer>();
        Graph<Integer> graph = new Graph<Integer>(false);
        
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(0, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 1);
        boolean isCycle = cycle.hasCycleDFS(graph);
        System.out.println(isCycle);
        isCycle = cycle.hasCycleUsingDisjointSets(graph);
        System.out.print(isCycle);
        
    }
    
}
