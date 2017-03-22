package com.interview.graph;

import java.util.*;

/**
 * Date 04/14/2014
 * @author Tushar Roy
 *
 * Ford fulkerson method Edmonds Karp algorithm for finding max flow
 *
 * Capacity - Capacity of an edge to carry units from source to destination vertex
 * Flow - Actual flow of units from source to destination vertex of an edge
 * Residual capacity - Remaining capacity on this edge i.e capacity - flow
     Note that there can be a path from u to v in the residual network, 
	 even though there is no path from u to v in the original network. 
	 Since flows in opposite directions cancel out, 
	 decreasing the flow from v to u is the same as increasing the flow from u to v.
 * AugmentedPath - Path from source to sink which has residual capacity greater than 0
 *
 * Time complexity is O(VE^2) using breadth-first Ford Fulkerson + Edmonds Karp
   Every augmenting path has at least one critical edge
   Critical edge: decreasing the capacity of this edge results in a decrease in max flow
   An edge can be critical at most Vertices/2 times. 
     It's opposite edge can be critical the same number of times.
   Maximum pair of vertices is O(E), since there must be an edge between vertices
   Total number of critical edges is O(VE)
   Since we can implement each iteration in O(E) time when we find the augmenting path
   by breadth-first search, total running time of Edmonds Karp algorithm is O(VE^2)
   
   Using depth-first search (Ford Fulkerson)
   Worst case, we may add 1 unit of flow in every iteration O( maxFlow * Edges )
 *
 * References:
 * http://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
 * https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm
 */
public class FordFulkerson {

	// capacity[startVertex][endVertex]
    public int maxFlow(int capacity[][], int source, int sink){

        //declare and initialize residual capacity as total available capacity initially.
        int residualCapacity[][] = new int[capacity.length][capacity[0].length];
        for (int i = 0; i < capacity.length; i++) {
            for (int j = 0; j < capacity[0].length; j++) {
                residualCapacity[i][j] = capacity[i][j];
            }
        }

        //this is parent map for storing BFS parent
        Map<Integer,Integer> parent = new HashMap<>();

        //stores all the augmented paths
        List<List<Integer>> augmentedPaths = new ArrayList<>();

        //max flow we can get in this network
        int maxFlow = 0;

        //see if augmented path can be found from source to sink.
        while(BFS(residualCapacity, parent, source, sink)){
            List<Integer> augmentedPath = new ArrayList<>();
            int flow = Integer.MAX_VALUE;
            //find minimum residual capacity in augmented path
            //also add vertices to augmented path list
            int v = sink;
			// retrace steps back from sink to source using parent
            while(v != source){
                augmentedPath.add(v);
                int u = parent.get(v);
				// find minimum residual capacity
				// since flow == minimum edge in a particular path
                if (flow > residualCapacity[u][v]) {
                    flow = residualCapacity[u][v];
                }
                v = u;
            }
			// add all vertices to augumented path
            augmentedPath.add(source);
            Collections.reverse(augmentedPath);
            augmentedPaths.add(augmentedPath);

            //add min capacity to max flow
            maxFlow += flow;

            //decrease residual capacity by min capacity from u to v in augmented path
            // and increase residual capacity by min capacity from v to u
            v = sink;
			// retrace steps back from sink to source again
			// decrementing residualCapacity from u->v &&
			// incrementing residualCapacity from v->u
            while(v != source){
                int u = parent.get(v);
                residualCapacity[u][v] -= flow;
                residualCapacity[v][u] += flow;
                v = u;
            }
        }
        printAugmentedPaths(augmentedPaths);
        return maxFlow;
    }

    /**
     * Prints all the augmented path which contribute to max flow
     */
    private void printAugmentedPaths(List<List<Integer>> augmentedPaths) {
        System.out.println("Augmented paths");
        augmentedPaths.forEach(path -> {
            path.forEach(i -> System.out.print(i + " "));
            System.out.println();
        });
    }

    /**
     * Breadth first search to find augmented path
     */
    private boolean BFS(int[][] residualCapacity, Map<Integer,Integer> parent,
            int source, int sink){
	    // visited vertices
        Set<Integer> visited = new HashSet<>();
		// breadth first search queue
		// used to explore children
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited.add(source);
        boolean foundAugmentedPath = false;
        //see if we can find augmented path from source to sink
        while(!queue.isEmpty()){
			// poll from top of queue
            int u = queue.poll();
            for(int v = 0; v < residualCapacity.length; v++){
                //explore the vertex only if it is not visited and 
				//its residual capacity is greater than 0
				//  meaning it has potential to be an augumented path
                if(!visited.contains(v) &&  residualCapacity[u][v] > 0){
                    //add in parent map saying v got explored by u
                    parent.put(v, u);
                    //add v to visited
                    visited.add(v);
                    //add v to queue for BFS
                    queue.add(v);
                    //if sink is found then augmented path is found
                    if ( v == sink) {
                        foundAugmentedPath = true;
                        break;
                    }
                }
            }
        }
        //returns if augmented path is found from source to sink or not
        return foundAugmentedPath;
    }
    
    public static void main(String args[]){
        FordFulkerson ff = new FordFulkerson();
        int[][] capacity = {{0, 3, 0, 3, 0, 0, 0},
                            {0, 0, 4, 0, 0, 0, 0},
                            {3, 0, 0, 1, 2, 0, 0},
                            {0, 0, 0, 0, 2, 6, 0},
                            {0, 1, 0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0, 0, 9},
                            {0, 0, 0, 0, 0, 0, 0}};

        System.out.println("\nMaximum capacity " + ff.maxFlow(capacity, 0, 6));
    }
}
