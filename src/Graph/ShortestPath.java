package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/*
SSSP - Single Source Shortest Path - Shortest path from give source to all vertices
APSP - All Pairs Shortest Path - Shortest paths between all pairs of vertices.

SSSP for unweighted graph - BFS - O(V + E)
SSSP for weighted graph without negative cycle - Dijkstra - O(E + VLogV)
SSSP for weighted graph with negative cycle if there exist as non-negative solution - A* (Dijkstra with heuristics)
SSSP for weighted graph with negative cycle - Bellman-Ford - O(VE)
APSP for weighted graph with negative cycle - Floyd-Warshall - O(V^3)
 */
public class ShortestPath {
    public static void main(String[] args) {

    }

    /*
        SSSP using BFS - We will be using distance array to store distance from source as well as if node is visited
     */
    private int[] shortestPath(ArrayList<ArrayList<Integer>> adjList, int N, int src)
    {

        int[] distanceArray = new int[N];
        for(int i = 0; i < N; i++)
            distanceArray[i] = Integer.MAX_VALUE;

        Queue<Integer> queue = new LinkedList<>();

        //Distance from source to source will be 0
        distanceArray[src] = 0;
        queue.add(src);

        while(!queue.isEmpty())
        {
            int currentNode = queue.poll();

            for(int currentNeighbour:adjList.get(currentNode)){
                //We are checking here if node is visited, plus storing the distance too
                //This is BFS, so first visit will be the shortest path
                if(distanceArray[currentNode] + 1 < distanceArray[currentNeighbour]){
                    distanceArray[currentNeighbour] = distanceArray[currentNode] + 1;
                    queue.add(currentNeighbour);
                }
            }
        }

        return distanceArray;
    }
}
