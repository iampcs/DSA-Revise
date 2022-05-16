package Graph;

import java.util.*;

/* Many problems can be formed where we will be asked number of connected components, their size, elements in connected components
   other operations regarding connected components like finding number of ways we can form a pair of elements each belonging to diff group(connected component).
   While connected component can be found using BFS/DFS for undirected graphs - If BFS or DFS visits all vertices, then the given undirected graph is connected.
   For directed graphs this approach won't work. Eg. A -> B -> C vertices, a BFS/DFS will show this as strongly connected, but there is no
   way to reach to A from any other node.So we can use Tarjan's or Kosaraju's algorithm for this.
*/
public class ConnectedComponents {
    public static void main(String[] args) {

    }

    /* Connected Components in an undirected graph - Given a graph with V vertices. Find the number of Provinces.
       Note: A province is a group of directly or indirectly connected cities and no other cities outside the group.
       Your task is to complete the function numProvinces() which takes an integer V and an adjacency matrix adj as input and returns the number of provinces.
       adj[i][j] = 1, if nodes i and j are connected and adj[i][j] = 0, if not connected.
       https://practice.geeksforgeeks.org/problems/number-of-provinces/1/

       Although here number of provinces are asked, we are also storing all connected components too - solution to question asking connected component
       Idea is to perform BFS on all vertices, if not visited. Now suppose 0-1 are connected and 2-3 are connected. We perform BFS on 0, we get edge (0,1). We won't perform BFS on 1, as its already visited.
       There is no way we can reach 2,3 from 0. Our BFS queue will be empty now, we know one connected component is over. Again BFS will start from vertex 2, add (2-3) to connected component

       Note: Same algorithm can solve for - Is undirected graph connected ? We have to run this on input graph and check if connected component is 1.
     */
    static int numProvinces(ArrayList<ArrayList<Integer>> adjMatrix, int vertices) {
        boolean[] visited = new boolean[vertices];
        ArrayList<ArrayList<Integer>> allConnectedComponents = new ArrayList<>();

        allConnectedComponents(adjMatrix, vertices, visited, allConnectedComponents);

        return allConnectedComponents.size();
    }

    private static void allConnectedComponents(ArrayList<ArrayList<Integer>> adjMatrix, int vertices, boolean[] visited,
                                               ArrayList<ArrayList<Integer>> allConnectedComponents) {
        //BFS on all vertices
        for(int vertex = 0; vertex < vertices ; vertex++){
            //Only if it's not visited already
            if(visited[vertex] == false){
                //Mark as visited
                visited[vertex] = true;
                //This is start of a new connected component
                ArrayList<Integer> currentComponent = new ArrayList<>();
                currentComponent.add(vertex);

                Queue<Integer> queue = new LinkedList<>();
                queue.add(vertex);
                while (!queue.isEmpty()){
                    int currentNode = queue.poll();
                    //Add all neighbours to queue
                    for(int neighbour = 0; neighbour < vertices; neighbour++){
                        //Except when neighbour is current node itself and there is an edge between current node-neighbour and neighbour is not visited already
                        if(neighbour != currentNode && adjMatrix.get(currentNode).get(neighbour) == 1 && visited[neighbour] == false){
                            //Add neighbour to queue, mark it as visited and add it to current connected component
                            queue.add(neighbour);
                            visited[neighbour] = true;
                            currentComponent.add(neighbour);
                        }
                    }
                }
                //Our queue is empty - means we are visited all possible neighbours/connected component for current node - add this to list of all components
                allConnectedComponents.add(currentComponent);
            }
        }
    }

    /* Number of Islands - Given an m x n 2D binary grid which represents a map of '1's (land) and '0's (water), return the number of islands.
       An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.
       https://leetcode.com/problems/number-of-islands/

       This problem can be seen as an application of connected component, if each element in matrix is considered a vertex and is connected by
       edges in all four direction. We can run BFS/DFS on each vertex to calculate number of islands.
     */
    public static int numIslands(char[][] grid) {
        Integer numberOfIslands = 0 ;
        int rowLen = grid.length;
        int colLen = grid[0].length;
        boolean[][] visited = new boolean[rowLen][colLen];
        //Run for all vertices
        for(int row =0 ; row <rowLen;row++){
            for(int col =0; col < colLen; col++){
                //Only if we are on land and not visited this land before
                if(grid[row][col] == 1 && visited[row][col] == false){
                    //Mark all connected land as visited
                    numIslands(grid,row,col,visited);
                    //We have found an island - so increment number of islands
                    numberOfIslands += 1;
                }
            }
        }
        return numberOfIslands;
    }
    //DFS to mark all land connected to given (row,col) land as visited
    private static void numIslands(char[][] grid, int row, int col, boolean[][] visited) {

        // It's easier to code base conditions - instead of stopping a recursive call - make all possible calls and return it based on base condition
        //Check we are inside grid
        //Check we are not on water, and we have not visited this node already
        if(row < 0 || row >= grid.length || col < 0 || col >= grid[0].length || grid[row][col] == 0 || visited[row][col] == true) {
            return;
        }
        //Mark this node as visited
        visited[row][col] = true;

        //Check for land node on all four directions to mark them as visited
        numIslands(grid,row + 1,col,visited);
        numIslands(grid,row - 1,col,visited);
        numIslands(grid,row,col + 1,visited);
        numIslands(grid,row,col - 1,visited);
    }

    /* Kosaraju's Algorithm for strongly connected directed graph - Time complexity - O(V + E)
       Watch to understand the logic behind Kosaraju's - https://www.youtube.com/watch?v=Rs6DXyWpWrI
       In summary
       1. Perform DFS - Sort nodes on decreasing end time - We use a stack to do this - O(V+E)
       2. Transpose the graph - reverse edges             - O(V+E)
       3. Perform DFS in order of Step 1, All the nodes we can reach from a node in list are part of its connected component - O(V+E)
       https://practice.geeksforgeeks.org/problems/strongly-connected-components-kosarajus-algo/1/
     */
    public int kosaraju(int V, ArrayList<ArrayList<Integer>> adj)
    {
       List<List<Integer>> stronglyConnectedComponents = new ArrayList<>();
        boolean[] visited = new boolean[V];
        Stack<Integer> visitOrder = new Stack<>();
        // DFS to get order of execution for 2nd DFS
        for(int v = 0; v < V; v++){
            if(!visited[v])
                dfs1(v,adj,visited,visitOrder);
        }

        //Transpose of graph and re-setting  visited array
        ArrayList<ArrayList<Integer>> revAdj = transposeGraph(V,adj);
        Arrays.fill(visited,false);

        //DF2 in order for DFS1 - Only strongly connected component will have the same DFS as first order
        while(!visitOrder.isEmpty()){
            int vertex = visitOrder.pop();
            if(!visited[vertex]){
                ArrayList<Integer> currentConnected = new ArrayList<>();
                dfs2(vertex, revAdj, visited,currentConnected);
                stronglyConnectedComponents.add(new ArrayList<>(currentConnected));
            }
        }

       return stronglyConnectedComponents.size();
    }

    private void dfs2(int vertex, ArrayList<ArrayList<Integer>> revAdj, boolean[] visited, ArrayList<Integer> currentConnected) {
        if(visited[vertex]) return;

        visited[vertex] = true;
        currentConnected.add(vertex);

        for(int neighbour : revAdj.get(vertex)){
            dfs2(neighbour,revAdj,visited,currentConnected);
        }
    }

    private ArrayList<ArrayList<Integer>> transposeGraph(int V, ArrayList<ArrayList<Integer>> adj) {
        ArrayList<ArrayList<Integer>> revAdj = new ArrayList<>();
        for(int v = 0; v < V; v++) revAdj.add(new ArrayList<Integer>());
        for(int v = 0; v < V; v++){
            for(int neighbour : adj.get(v)){
                revAdj.get(neighbour).add(v);
            }
        }

        return revAdj;
    }

    private void dfs1(int currNode, ArrayList<ArrayList<Integer>> adj, boolean[] visited, Stack<Integer> visitOrder) {
        if(visited[currNode]) return;

        visited[currNode] = true;
        for(int neighbour : adj.get(currNode)){
            dfs1(neighbour, adj, visited,visitOrder);
        }

        visitOrder.add(currNode);
    }

}
