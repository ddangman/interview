package com.interview.graph;

import java.util.*;

/**
 * Date 11/16/2015
 * @author Tushar Roy
 *
 * Find all simple cycles in directed graph using Johnson's algorithm
 *
 * Time complexity - O(E + V).(c+1) where c is number of cycles found
     strongly connected components are found in O(E + V) time
 * Space complexity - O(E + V + s) where s is sum of length of all cycles.
 *
 * Link to youtube video - https://youtu.be/johyrWospv0
 *
 * References
 * https://github.com/jgrapht/jgrapht/blob/master/jgrapht-core/src/main/java/org/jgrapht/alg/cycle/JohnsonSimpleCycles.java
 */
public class AllCyclesInDirectedGraphJohnson {
    // vertices not to be explored
	// since a simple cycle can not have repeated vertices
    Set<Vertex<Integer>> blockedSet;
    // valueVertices to be unblocked if it's key is unblocked
    // key is vertex upstream of valueVertices
    // valueVertices are all vertices adjacent to key
    Map<Vertex<Integer>, Set<Vertex<Integer>>> blockedMap;
    // stack for backtracking in DFS
    Deque<Vertex<Integer>> stack;
	// inner list is one simple cycle
	// outer list is all simple cycles
    List<List<Vertex<Integer>>> allCycles;

    /**
     * Main function to find all cycles
     */
    public List<List<Vertex<Integer>>> simpleCyles(Graph<Integer> graph) {

        blockedSet = new HashSet<>();
        blockedMap = new HashMap<>();
        stack = new LinkedList<>();
        allCycles = new ArrayList<>();
        long startIndex = 1;
        // use Tarjan's algorithm to find strongly connnected subgraph
        TarjanStronglyConnectedComponent tarjan = new TarjanStronglyConnectedComponent();
		// all vertices will have chance to be start vertex
        while(startIndex <= graph.getAllVertex().size()) {
			//a cycle will never span multiple strongly connected components
			//so divide and conquer this problem
            //  create strongly connected subgraph, ignoring vertices less than startIndex
			//  after all simple cycles are found, startIndex increments
			//  so algorithm now searches for all simple cycles starting/ending with new startIndex
			//  startIndex will be excluded from DFS to prevent discovery of duplicate cycles
            Graph<Integer> subGraph = createSubGraph(startIndex, graph);
			// use Tarjan's algorithm to find strongly connected graph
            List<Set<Vertex<Integer>>> sccs = tarjan.scc(subGraph);
            //this creates graph consisting of strongly connected components only and then returns the
            //least indexed vertex among all the strongly connected component graph.
            //it also ignore one vertex graph since it wont have any cycle.
            Optional<Vertex<Integer>> maybeLeastVertex = leastIndexSCC(sccs, subGraph);
            if(maybeLeastVertex.isPresent()) {
                Vertex<Integer> leastVertex = maybeLeastVertex.get();
				// clear any blocked vertices from previous iteration
                blockedSet.clear();
                blockedMap.clear();
				// initialize recursion where leastVertex is (startVertex, currentVertex)
                findCyclesInSCG(leastVertex, leastVertex);
                startIndex = leastVertex.getId() + 1; // increment start vertex to be greater than leastVertex
            } else {
                break;
            }
        }
        return allCycles;
    }

   private Optional<Vertex<Integer>> leastIndexSCC(List<Set<Vertex<Integer>>> sccs, Graph<Integer> subGraph) {
        long min = Integer.MAX_VALUE;
        Vertex<Integer> minVertex = null;
        Set<Vertex<Integer>> minScc = null;
        for(Set<Vertex<Integer>> scc : sccs) {
            if(scc.size() == 1) {
                continue;
            }
            for(Vertex<Integer> vertex : scc) {
                if(vertex.getId() < min) {
                    min = vertex.getId();
                    minVertex = vertex;
                    minScc = scc;
                }
            }
        }

        if(minVertex == null) {
            return Optional.empty();
        }
        Graph<Integer> graphScc = new Graph<>(true);
        for(Edge<Integer> edge : subGraph.getAllEdges()) {
            if(minScc.contains(edge.getVertex1()) && minScc.contains(edge.getVertex2())) {
                graphScc.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
            }
        }
        return Optional.of(graphScc.getVertex(minVertex.getId()));
    }

    // recursively unblock vertices
    private void unblock(Vertex<Integer> u) {
        blockedSet.remove(u);
        if(blockedMap.get(u) != null) {
            blockedMap.get(u).forEach( v -> {
                if(blockedSet.contains(v)) {
                    unblock(v);
                }
            });
            blockedMap.remove(u);
        }
    }

    // depth-first search
    private boolean findCyclesInSCG(
            Vertex<Integer> startVertex,
            Vertex<Integer> currentVertex) {
        boolean foundCycle = false;
        // add current vertex to stack
        stack.push(currentVertex);
        // block current vertex
        blockedSet.add(currentVertex);

		// explore all neighbors of currentVertex
        for (Edge<Integer> e : currentVertex.getEdges()) {
            Vertex<Integer> neighbor = e.getVertex2();
            //if neighbor is same as start vertex means cycle is found.
            //Store contents of stack in final result.
            if (neighbor == startVertex) {
                List<Vertex<Integer>> cycle = new ArrayList<>();
                stack.push(startVertex);
				// content of cycle is the contents of stack
                cycle.addAll(stack);
                Collections.reverse(cycle);
                stack.pop();
                allCycles.add(cycle); // save results
                foundCycle = true;
            } //explore this neighbor only if it is not in blockedSet.
            else if (!blockedSet.contains(neighbor)) {
				// neighbor will now enter recursion
                boolean gotCycle =
                        findCyclesInSCG(startVertex, neighbor);
                foundCycle = foundCycle || gotCycle;
            }
        }
        //if cycle is found with current vertex then recursively unblock vertex and 
		//all vertices which are dependent on this vertex.
		// this allows all vertices used in previous simple cycle 
		// to be used in discovery of another simple cycle
        if (foundCycle) {
            //remove from blockedSet and then 
			//remove all the other vertices dependent on this vertex from blockedSet
			// dependent vertices are any vertex whose neighbor is currentVertex and
			// can not proceed in DFS while currentVertex is blocking them
            unblock(currentVertex);
        } else {
			/* DFS complete when no more unblocked neighbor found,
			 * when blocked neighbor is unblocked, 
			 * current vertex must be unblocked as well 
			 * since current vector can now go through newly unblocked neighbor
			 * to potentially find new simple cycle */
            //if no cycle is found with current vertex then don't unblock it. 
			//But find all its neighbors and add this vertex to their blockedMap. 
			//If any of those neighbors ever get unblocked then unblock current vertex as well.
            for (Edge<Integer> e : currentVertex.getEdges()) {
                Vertex<Integer> w = e.getVertex2();
                Set<Vertex<Integer>> bSet = getBSet(w);
                bSet.add(currentVertex);
            }
        }
        //remove vertex from the stack recursively
        stack.pop();
        return foundCycle;
    }

    private Set<Vertex<Integer>> getBSet(Vertex<Integer> v) {
        return blockedMap.computeIfAbsent(v, (key) ->
            new HashSet<>() );
    }

    private Graph createSubGraph(long startVertex, Graph<Integer> graph) {
        Graph<Integer> subGraph = new Graph<>(true);
        for(Edge<Integer> edge : graph.getAllEdges()) {
            if(edge.getVertex1().getId() >= startVertex && edge.getVertex2().getId() >= startVertex) {
                subGraph.addEdge(edge.getVertex1().getId(), edge.getVertex2().getId());
            }
        }
        return subGraph;
    }

    public static void main(String args[]) {
        AllCyclesInDirectedGraphJohnson johnson = new AllCyclesInDirectedGraphJohnson();
        Graph<Integer> graph = new Graph<>(true);
        graph.addEdge(1, 2);
        graph.addEdge(1, 8);
        graph.addEdge(1, 5);
        graph.addEdge(2, 9);
        graph.addEdge(2, 7);
        graph.addEdge(2, 3);
        graph.addEdge(3, 1);
        graph.addEdge(3, 2);
        graph.addEdge(3, 6);
        graph.addEdge(3, 4);
        graph.addEdge(6, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 2);
        graph.addEdge(8, 9);
        graph.addEdge(9, 8);

        List<List<Vertex<Integer>>> allCycles = johnson.simpleCyles(graph);
        allCycles.forEach(cycle -> {
            StringJoiner joiner = new StringJoiner("->");
            cycle.forEach(vertex -> joiner.add(String.valueOf(vertex.getId())));
            System.out.println(joiner);
        });
    }

}
