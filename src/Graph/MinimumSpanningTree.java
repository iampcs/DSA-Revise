package Graph;

import java.util.*;

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
       1. Maintain a minHeap of Edges and a visited set to keep track of already visited nodes
       2. Add any node as first node from where we will start exploring - As this is first node - We could create an imaginary edge to this node and add it to minHeap
       3. Until minHeap is not empty - we have edges
          3.1 Pop an edge - This edge will contain a source(already visited - that's why this edge is in minHeap), a destination and weight of edge
          3.2 Have we already visited this destination ? Skip this edge if we did
          3.3 Add destination to visited set
          3.4 Add all edges from this destination to minHeap
          3.5 We are using this edge for our MST - add to result
      4. Return result

     */
    static int spanningTree(int V, ArrayList<ArrayList<ArrayList<Integer>>> adjList)
    {
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.weight));
        int mstWeight = 0;
        //Let's start with vertex 0 - add imaginary edge to 0
        minHeap.add(new Edge(-1,0,0));

        while (!minHeap.isEmpty()){
            Edge currentEdge = minHeap.poll();
            //We have already visited this vertex - skip
            if(visited.contains(currentEdge.destination)) continue;

            visited.add(currentEdge.destination);
            //Add all outgoing edges from edge destination
            for(ArrayList<Integer> edge : adjList.get(currentEdge.destination)){
                minHeap.add(new Edge(currentEdge.destination, edge.get(0),edge.get(1)));
            }
            //This condition to skip the imaginary edge we added - We could have added all edges from 0 to minHeap and start too - but this is more neat
            if(currentEdge.source != -1) mstWeight += currentEdge.weight;
        }
        //In case of a disconnected graph we won't be able to visit all the vertices - return -1 in that case.
        return visited.size() == V ? mstWeight : -1;
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

    /* Connecting Cities With Minimum Cost - There are ‘N’ cities numbered from 1 to ‘N’ and ‘M’ roads. Each road connects two different cities and described as a two-way road using three integers (‘U’, ‘V’, ‘W’) which represents the cost ‘W’ to connect city ‘U’ and city ‘V’ together.
       Now, you are supposed to find the minimum cost so that for every pair of cities, there exists a path of connections (possibly of length 1) that connects those two cities together. The cost is the sum of the connection costs used. If the task is impossible, return -1.
       https://www.codingninjas.com/codestudio/problems/connecting-cities-with-minimum-cost_1386586

       This is a use case of MST - will convert the input to a format of problem - spanningTree.
       Note: For problem where we are provided all edges - Kruskal's too can be used - specially if edges are already sorted by weight.
     */
    public static int getMinimumCost(int n, int m, int[][] connections) {

        ArrayList<ArrayList<ArrayList<Integer>>> graph = new ArrayList<>();
        for (int v = 0 ; v < n; v++) graph.add(new ArrayList<>());
        for(int[] edge : connections){
            //Vertices are named 1-N, we're maintaining indices 0 to N-1, so deduct 1 from input
            graph.get(edge[0] - 1).add(new ArrayList<>(Arrays.asList(edge[1] - 1, edge[2])));
            graph.get(edge[1] - 1).add(new ArrayList<>(Arrays.asList(edge[0] - 1, edge[2])));
        }

        return spanningTree(n,graph);
    }

    /* Min Cost to Connect All Points - You are given an array points representing integer coordinates of some points on a 2D-plane, where points[i] = [xi, yi].
       The cost of connecting two points [xi, yi] and [xj, yj] is the manhattan distance between them: |xi - xj| + |yi - yj|, where |val| denotes the absolute value of val.
       Return the minimum cost to make all points connected.
       https://leetcode.com/problems/min-cost-to-connect-all-points/

       Note : Here all points are vertices - and there is an edge between all of them - this is a fully connected graph. It makes more sense to perform Prim's for adjacency matrix here
       as V^2 will be less than number of 2*Edges.
       TODO: Implement Prim's algorithm for adjacency matrix.

     */
    public int minCostConnectPoints(int[][] points) {
        ArrayList<ArrayList<ArrayList<Integer>>> graph = new ArrayList<>();
        int V = points.length;
        for (int v = 0 ; v < V; v++) graph.add(new ArrayList<>());

        //Convert input to a format accepted by problem - spanningTree : We are assuming each point to be vertex num = it's index in input and weight between them as manhattanDistance
        for(int i = 0 ; i< V; i++){
            for(int j = 0; j<V;j++){
                if(i == j ) continue;
                graph.get(i).add(new ArrayList<>(Arrays.asList(j, manhattanDistance(points[i], points[j]))));
            }
        }
        return spanningTree(V,graph);
    }

    private int manhattanDistance(int[] point1, int[] point2) {
        return Math.abs(point1[0] - point2[0]) + Math.abs(point1[1] - point2[1]);
    }
}

