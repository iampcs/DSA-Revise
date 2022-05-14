package Graph;

import java.util.*;
/* Why is complexity of BFS and DFS - O(V + E) and not O(VE) ?
   Let's take an example of DFS - In DFS, you traverse each node exactly once. Therefore, the time complexity of DFS is at least O(V).
   Now, any additional complexity comes from how you discover all the outgoing paths or edges for each node which, in turn, is dependent on the way your graph is implemented.
   If an edge leads you to a node that has already been traversed, you skip it and check the next.
   Typical DFS implementations use a hash table to maintain the list of traversed nodes so that you could find out if a node has been encountered before in O(1) time (constant time).

   * If your graph is implemented as an adjacency matrix (a V x V array), then, for each node, you have to traverse an entire row of length V in the matrix to discover all
     its outgoing edges. Please note that each row in an adjacency matrix corresponds to a node in the graph, and the said row stores information about edges stemming
     from the node. So, the complexity of DFS is O(V * V) = O(V^2).
   * If your graph is implemented using adjacency lists, wherein each node maintains a list of all its adjacent edges, then, for each node, you could discover all its
     neighbors by traversing its adjacency list just once in linear time. For a directed graph, the sum of the sizes of the adjacency lists of all the nodes is E (total number of edges).
     So, the complexity of DFS is O(V) + O(E) = O(V + E).
     For an undirected graph, each edge will appear twice. Once in the adjacency list of either end of the edge. So, the overall complexity will be O(V) + O (2E) ~ O(V + E).
   * There are different other ways to implement a graph. You can reason the complexity accordingly.

 */
public class Traversal {
    public static void main(String[] args) {

    }
    /* Depth First Search - O(V + E)
   Similar to DFS for trees, but we have to maintain a visited array to track nodes already visited - this is because we can have cycles in graph.
   https://practice.geeksforgeeks.org/problems/depth-first-traversal-for-a-graph/1/
   Given a connected undirected graph. Perform a Depth First Traversal of the graph.
   When we get an adjList - we assume its indexes to be our vertices name and start with 0.
   adjList.get(0).get(0) - will represent - get me zeroth vertex's first neighbour - now in case of weighted edges, there will be pair here <n,w>
   where n can represent the neighbour vertex and w, weight of edge between them.
   Eg. Input : [[1,2,4],[0],[0],[4],[0,3]]  Output : [0,1,2,4,3]

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
    Similar to BFS of tree - We will maintain a queue which will contain all element at current level(distance from start vertex)
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

    /* Find if Path Exists in Graph - Given edges and the integers n, source, and destination, return true if there is a valid path from source to destination, or false otherwise.
       https://leetcode.com/problems/find-if-path-exists-in-graph/
       Will perform DFS from source to check if we will ever reach destination.
     */
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        boolean[] visited = new boolean[n];
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
        //Converting edges to adjList
        for(int i = 0; i< n; i++){
            adjList.add(new ArrayList<Integer>());
        }
        for(int[] edge : edges){
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }
        return validPath(n, adjList, source, destination, visited);

    }

    public boolean validPath(int n, ArrayList<ArrayList<Integer>> adjList, int source, int destination, boolean[] visited){
        //We have reached destination
        if(source == destination) return true;
        //Mark this node as visited, so we don't visit this again
        visited[source] = true;
        //For all neighbours of current node
        for(int neighbourIndex = 0; neighbourIndex < adjList.get(source).size() ; neighbourIndex++){
            Integer currentNeighbour = adjList.get(source).get(neighbourIndex);
            //If we haven't visited this neighbour already
            if(visited[currentNeighbour] == false){
                //Check if there is a path from this neighbour to destination
                boolean hasPath = validPath(n,adjList,currentNeighbour,destination,visited);
                //We want to return from this loop of all neighbours as soon as we find a path, no need to evaluate further
                if(hasPath) return true;
            }
        }
        //Current node can't reach to destination
        return false;
    }

    /* All Paths From Source to Target - Given a directed acyclic graph (DAG) of n nodes labeled from 0 to n - 1, find all possible paths from node 0 to node n - 1 and return them in any order.
       The graph is given as follows: graph[i] is a list of all nodes you can visit from node i
       https://leetcode.com/problems/all-paths-from-source-to-target/
       Eg. Input: graph = [[1,2],[3],[3],[]]
           Output: [[0,1,3],[0,2,3]]
           Explanation: There are two paths: 0 -> 1 -> 3 and 0 -> 2 -> 3.

       Similar to previous problem - just that instead of returning true and stopping when we reach destination, we will store current path instead
       Even though its mentioned here that graph is acyclic - we will solve it as a cyclic graph - to have a generic code
     */
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        int vertices = graph.length;
        List<List<Integer>> allPaths = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        allPathsSourceTarget(graph, 0, vertices - 1, visited, new ArrayList<Integer>(), allPaths);
        return allPaths;
    }

    private void allPathsSourceTarget(int[][] graph, int start, int destination, boolean[] visited,
                                      ArrayList<Integer> currPath, List<List<Integer>> allPaths) {
        //We have reached destination - add destination itself to current path
        //Add current path to list of all paths
        //Backtrack after adding - as this same current path will be used again by some other flow
        if(start == destination){
            currPath.add(destination);
            allPaths.add(new ArrayList<>(currPath));
            currPath.remove(currPath.size() - 1);
            return;
        }

        //mark current node as visited, add it to current path
        visited[start] = true;
        currPath.add(start);

        //For all the neighbours of current node
        for(int neighbourIndex = 0; neighbourIndex < graph[start].length ; neighbourIndex ++){
            int currentNeighbour = graph[start][neighbourIndex];
            //Perform DFS on neighbour if not visited
            if(visited[currentNeighbour] == false){
                allPathsSourceTarget(graph,currentNeighbour,destination,visited,currPath,allPaths);
            }
        }
        //Backtrack - because we can visit this same node from some other path, remove current node from path while backtracking
        visited[start] = false;
        currPath.remove(currPath.size() - 1);

    }

}

