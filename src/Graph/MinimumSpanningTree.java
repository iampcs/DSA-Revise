package Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/* Spanning Tree - For a connected and undirected graph, a spanning tree of that graph is a subgraph that is a tree and connects all the vertices together. A single graph can have many spanning trees.
   A minimum spanning tree (MST) for a "weighted, connected and undirected graph" is a spanning tree with weight less than or equal to the weight of every other spanning tree.
   A minimum spanning tree has (V – 1) edges where V is the number of vertices in the given graph.

   We have two greedy algorithms to find MST
   1. Prim’s algorithm
   2. Kruskal’s algorithm
 */
public class MinimumSpanningTree {
    public static void main(String[] args) {

    }

    /* Minimum Spanning Tree - Given a weighted, undirected and connected graph of V vertices and E edges. The task is to find the sum of weights of the edges of the Minimum Spanning Tree.
       https://practice.geeksforgeeks.org/problems/minimum-spanning-tree/1#
       Input : [[[1,4],[2,5]],  Represent Edges 0--(4)--1 , 0--(5)--2 , (weight of edge)
                [[0,4],[2,10]],
                [[0,5],[1,10]]]
       Output : 9
       Explanation: 0-1, 0-2

       We will solve this using Prim's Algorithm - It is a greedy algorithm - Algorithm
       1. Maintain a minHeap of Edges and a visited array to keep track of already visited nodes
       2. Add any node as first node from where we will start exploring - As this is first node - We could create an imaginary edge to this node and add it to minHeap
       3. Until minHeap is not empty - we have edges
          3.1 Pop an edge - This edge will contain a source(already visited - that's why this edge is in minHeap), a destination and weight of edge
          3.2 Have we already visited this destination ? Skip this edge if we did
          3.3 Add destination to visited Array
          3.4 Add all edges from this destination to minHeap
          3.5 We are using this edge for our MST - add to result
      4. Return result

     */
    static int spanningTree(int V, ArrayList<ArrayList<ArrayList<Integer>>> adjList)
    {
        boolean[] visited = new boolean[V];
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.weight));
        int mstWeight = 0;
        //Lets start with vertex 0 - add imaginary edge to 0
        minHeap.add(new Edge(-1,0,0));

        while (!minHeap.isEmpty()){
            Edge currentEdge = minHeap.poll();
            //We have already visited this vertex - skip
            if(visited[currentEdge.destination]) continue;

            visited[currentEdge.destination] = true;
            //Add all outgoing edges from edge destination
            for(ArrayList<Integer> edge : adjList.get(currentEdge.destination)){
                minHeap.add(new Edge(currentEdge.destination, edge.get(0),edge.get(1)));
            }
            //This condition to skip the imaginary edge we added - We could have added all edges from 0 to minHeap and start too - but this is more neat
            if(currentEdge.source != -1) mstWeight += currentEdge.weight;
        }

        return mstWeight;
    }

    static class Edge {
        int source;
        int destination;
        int weight;

        public Edge(int source, int destination, int weight){
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }
}

