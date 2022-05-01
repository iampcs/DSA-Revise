package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Traversal {
    public static void main(String[] args) {

    }
    /* Depth First Search - Similar to DFS for trees, but we have to maintain a visited array to track nodes already visited - this is because
   we can have cycles in graph.
   https://practice.geeksforgeeks.org/problems/depth-first-traversal-for-a-graph/1/
   Given a connected undirected graph. Perform a Depth First Traversal of the graph.
   When we get an adjList - we assume its indexes to be our vertices name and start with 0.
   adjList.get(0).get(0) - will represent - get me zeroth vertex's first neighbour - now in case of weighted edges, there will be pair here <n,w>
   where n can represent the neighbour vertex and w, weight of edge between them.

   Also, it is assumed here that the graph is connected. Else we can change the code to run dfsOfGraph for all vertices.
 */
    public ArrayList<Integer> dfsOfGraph(int totalVertex, ArrayList<ArrayList<Integer>> adjList) {
        //Visited array to keep track of vertices already visited
        boolean[] visited = new boolean[totalVertex];
        ArrayList<Integer> dfsPath = new ArrayList<>();
        //We want to start from vertex 0
        dfsOfGraph(0,adjList, visited, dfsPath);

        return dfsPath;
    }

    private void dfsOfGraph(int currentVertex, ArrayList<ArrayList<Integer>> adjList, boolean[] visited, ArrayList<Integer> dfsPath) {
        //Add current vertex to path and mark it as visited
        dfsPath.add(currentVertex);
        visited[currentVertex] = true;
        //Fetch all the neighbours of current vertex - this can also be achieved with an iterator
        for (int neighbourVertexIndex = 0; neighbourVertexIndex < adjList.get(currentVertex).size(); neighbourVertexIndex++) {
            //For each neighbour vertex
            int neighbourVertex = adjList.get(currentVertex).get(neighbourVertexIndex);
            //Check if its already visited - if not recurse for that vertex
            if (visited[neighbourVertex] == false) {
                dfsOfGraph(neighbourVertex, adjList, visited, dfsPath);
            }
        }
    }

    /* Breadth First Search - Similar to DFS, we will maintain a visited array.
    https://practice.geeksforgeeks.org/problems/bfs-traversal-of-graph/1/
    Given a directed graph. The task is to do Breadth First Traversal of this graph starting from 0. Assume that graph is connected.
     */

    public ArrayList<Integer> bfsOfGraph(int totalVertex, ArrayList<ArrayList<Integer>> adjList) {
        boolean[] visited = new boolean[totalVertex];
        ArrayList<Integer> bfsPath = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        //Adding 0th node and marking visited as true - this can also be modified to start from any vertex
        queue.add(0);
        visited[0] = true;
        while (!queue.isEmpty()){
            int currentVertex = queue.poll();
            bfsPath.add(currentVertex);
            //For each neighbour of current vertex - check if it's not visited - if not - add to queue to perform bfs on it later.
            for (int neighbourVertexIndex = 0; neighbourVertexIndex < adjList.get(currentVertex).size(); neighbourVertexIndex++) {
                int neighbourVertex = adjList.get(currentVertex).get(neighbourVertexIndex);
                if (!visited[neighbourVertex]) {
                    visited[neighbourVertex] = true;
                    queue.add(adjList.get(currentVertex).get(neighbourVertexIndex));
                }
            }
        }
        return bfsPath;
    }
}

