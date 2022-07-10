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
        //Add current vertex to path and mark it as visited - This method won't be called for a vertex already visited - check below
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
       This problem can be better solved using Union-Find data structure - When ever we are given edges instead of adjList or adjMatrix
       and question is of connectivity - UnionFind will be a better solution.
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
    public List<List<Integer>> allPathsSourceTarget(int[][] adjList) {
        int vertices = adjList.length;
        List<List<Integer>> allPaths = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        allPathsSourceTarget(adjList, 0, vertices - 1, visited, new ArrayList<Integer>(), allPaths);
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
    /* Max Area of Island - You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.)
       You may assume all four edges of the grid are surrounded by water. The area of an island is the number of cells with a value 1 on the island.
       Return the maximum area of an island in grid. If there is no island, return 0.
       https://leetcode.com/problems/max-area-of-island/

       Simple DFS solution with visited array. Our dfsArea will return sum of area of all its connecting tiles. We will find the max among them
       and return.

     */
    public int maxAreaOfIsland(int[][] grid) {
        int gridRow = grid.length;
        int gridCol = grid[0].length;
        boolean[][] visited = new boolean[gridRow][gridCol];
        int maxArea = 0;

        for(int row = 0; row < gridRow; row++){
            for(int col = 0; col < gridCol; col++){
                if(!visited[row][col]){
                    int currArea = dfsArea(row,col, gridRow,gridCol, visited, grid);
                    maxArea = Math.max(currArea,maxArea);
                }
            }
        }

        return maxArea;
    }

    private int dfsArea(int row, int col, int gridRow, int gridCol, boolean[][] visited, int[][] grid) {
        //If we are out of board or tile is already visited or tile is water - return 0
        //Note: In case of these checks - always check for board size before checking other conditions
        if( row == gridRow || row < 0 || col == gridCol || col < 0 || visited[row][col] || grid[row][col] == 0) return 0;
        visited[row][col] = true;

        //Return current area + area of all surrounding tiles
        return 1 + dfsArea(row + 1, col, gridRow, gridCol, visited, grid) +
                          dfsArea(row - 1, col, gridRow, gridCol, visited, grid) +
                          dfsArea(row , col + 1, gridRow, gridCol, visited, grid) +
                          dfsArea(row, col - 1, gridRow, gridCol, visited, grid) ;

    }

    /* Pacific Atlantic Water Flow - https://leetcode.com/problems/pacific-atlantic-water-flow/
       Algorithm approach - https://www.youtube.com/watch?v=s-VkcjHqkGI

       We have to find coordinates from where water can flow to both ocean - instead of going to each cell and checking if water will flow O((N*M) * (N*M))
       to both - we perform a DFS from cells we know water will flow to at-least one ocean, two sets will be created - a set of coordinates
       that will flow to pacific and set of coordinated which flows to atlantic - 2 * O(NxM)
       Our solution set will contain common sets from both the sets

     */
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> solution = new ArrayList<>();
        int row = heights.length;
        int col = heights[0].length;
        //Will contain coordinates from where water can flow to pacific
        Set<List<Integer>> pSet = new HashSet<>();
        //Will contain coordinates from where water can flow to atlantic
        Set<List<Integer>> aSet = new HashSet<>();

        // We know all elements from first col will flow to pacific
        // Also all elements from last column will flow to atlantic
        //Performing DFS from these points to check till where water can flow
        for(int rowC = 0 ; rowC < row; rowC++){
            dfsOnGrid(rowC, 0,pSet,heights[rowC][0], row, col, heights);
            dfsOnGrid(rowC, col - 1,aSet,heights[rowC][col - 1], row, col, heights);
        }
        // We know all elements from first row will flow to pacific
        // Also all elements from last row will flow to atlantic
        //Performing DFS from these points to check till where water can flow
        for(int colC = 0 ; colC < col; colC++){
            dfsOnGrid(0, colC,pSet,heights[0][colC], row, col, heights);
            dfsOnGrid(row - 1, colC,aSet,heights[row - 1][colC], row, col, heights);
        }
        //Common coordinates from both the set will be our solution
        for(List<Integer> rc : pSet){
            if(aSet.contains(rc)) solution.add(rc);
        }
        return solution;
    }
    //Water can flow from high position to low - but we are backtracking from sea and checking where can water reach - so we will check for coordinates higher than current one
    private void dfsOnGrid(int rowC, int colC, Set<List<Integer>> visitedSet, int prevHeight, int row, int col, int[][] heights) {
        //We want to be inside board, coordinate should not be already visited - current coordinate should be higher than prev - else water won't flow
        if(visitedSet.contains(List.of(rowC,colC)) || rowC < 0 || colC < 0 || rowC == row || colC == col || heights[rowC][colC] < prevHeight)
            return;

        //Mark as visited and perform dfs on all four direction
        visitedSet.add(List.of(rowC, colC));
        dfsOnGrid(rowC + 1,colC,visitedSet,heights[rowC][colC],row,col,heights);
        dfsOnGrid(rowC - 1,colC,visitedSet,heights[rowC][colC],row,col,heights);
        dfsOnGrid(rowC,colC + 1,visitedSet,heights[rowC][colC],row,col,heights);
        dfsOnGrid(rowC,colC - 1,visitedSet,heights[rowC][colC],row,col,heights);
    }

    /* Surrounded Regions - Given an m x n matrix board containing 'X' and 'O', capture all regions that are 4-directionally surrounded by 'X'.
       A region is captured by flipping all 'O's into 'X's in that surrounded region.
       https://leetcode.com/problems/surrounded-regions/

       Algorithm approach - https://www.youtube.com/watch?v=9z2BunfoZ5Y
       The approach is similar to previous problem - instead of finding regions surrounded by C and flipping them - why not find regions that can't be flipped and flip the rest.
       We know that 'O' that are near borders can't be flipped - So we perform dfs on all these 'O' and marked those regions. Anything else other than these regions could be flipped.

     */
    public void solve(char[][] board) {
        //'Can't be flipped' set
        Set<List<Integer>> visited = new HashSet<>();
        int row = board.length;
        int col = board[0].length;

        //Performing dfs on borders and marking regions that can't be flipped
        for(int rC = 0; rC < row; rC++){
            dfsBoard(rC,0,visited,board,row,col);
            dfsBoard(rC,col - 1,visited,board,row,col);
        }
        for(int cC = 0; cC < col; cC++){
            dfsBoard(0,cC,visited,board,row,col);
            dfsBoard(row - 1,cC,visited,board,row,col);
        }

        //Marking all other regions as 'X' that are not part of regions marked as 'can't be flipped'
        for(int rC = 0; rC < row; rC++){
            for(int cC = 0; cC < col; cC++){
                if(board[rC][cC] == 'O' && !visited.contains(List.of(rC,cC)))
                    board[rC][cC] = 'X';
            }
        }

    }

    private void dfsBoard(int rC, int cC, Set<List<Integer>> visited, char[][] board, int row, int col) {
        //Not already a part of set or outside of board or is a 'X'
        if(visited.contains(List.of(rC,cC)) || rC < 0 || rC == row || cC < 0 || cC == col || board[rC][cC] == 'X') return;

        //We have found a cell that can't be flipped - add to set and perform dfs on all directions
        visited.add(List.of(rC,cC));
        dfsBoard(rC+ 1,cC,visited,board,row,col);
        dfsBoard(rC - 1,cC,visited,board,row,col);
        dfsBoard(rC,cC + 1,visited,board,row,col);
        dfsBoard(rC,cC - 1,visited,board,row,col);
    }

    /* Rotting Oranges - You are given an m x n grid where each cell can have one of three values:
    0 representing an empty cell,
    1 representing a fresh orange, or
    2 representing a rotten orange.
    Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.
    Return the minimum number of minutes that must elapse until no cell has a fresh orange. If this is impossible, return -1.

    https://leetcode.com/problems/rotting-oranges/
    Approach : https://www.youtube.com/watch?v=y704fEOx0s0

    Every second a rotten orange will make a fresh orange rotten. We will use multi-source bfs here. This is a technique where we will perform bfs on multiple nodes at once.
    We will find out the count of fresh oranges - and add all rotten oranges to our bfs queue.
    Then will perform bfs on all those rotten oranges.
        Reduce count of fresh orange if any nearby
        Add all oranges nearby to queue now
        Increment time
    Because we will be visiting each grid only once - time complexity - O(mn)
     */
    public int orangesRotting(int[][] grid) {
        int time = 0;
        int fresh = 0;
        int row = grid.length;
        int col = grid[0].length;
        Queue<List<Integer>> queue = new LinkedList<>();

        //Count fresh orange and add rotten oranges at t = 0, to queue
        for(int r = 0; r < row ; r++){
            for(int c = 0 ; c< col ; c++){
                if(grid[r][c] == 1) fresh += 1;
                else if(grid[r][c] == 2) queue.add(List.of(r,c));
            }
        }
        //If there are no fresh oranges - we are done there - no point increasing our time
        while (!queue.isEmpty() && fresh != 0){
            //Run BFS on all oranges at time = t
            int qSize = queue.size();
            for(int rotten = 0; rotten < qSize; rotten++){
                List<Integer> currOrange = queue.poll();
                int currRow = currOrange.get(0);
                int currCol = currOrange.get(1);

                //BFS on all four direction - If fresh orange detected - rot it - add it to queue for next batch processing
                if(currRow + 1 < row && grid[currRow + 1][currCol] == 1){
                    grid[currRow + 1][currCol] = 2;
                    queue.add(List.of(currRow + 1, currCol));
                    fresh -= 1;
                }
                if(currRow - 1 >= 0 && grid[currRow - 1][currCol] == 1){
                    grid[currRow - 1][currCol] = 2;
                    queue.add(List.of(currRow - 1, currCol));
                    fresh -= 1;
                }
                if(currCol + 1 < col && grid[currRow][currCol + 1] == 1){
                    grid[currRow][currCol + 1] = 2;
                    queue.add(List.of(currRow, currCol + 1));
                    fresh -= 1;
                }
                if(currCol - 1 >= 0 && grid[currRow][currCol - 1] == 1){
                    grid[currRow][currCol - 1] = 2;
                    queue.add(List.of(currRow, currCol - 1));
                    fresh -= 1;
                }
            }
            //All oranges at time = t are polled - increment time for next batch
            time += 1;
        }
        //If all fresh will rot we return time else -1
        return fresh == 0 ? time : -1;
    }

    /* Walls and Gates - You are given a m x n 2D grid initialized with these three possible values.
    -1 - A wall or an obstacle.
    0 - A gate.
    INF - Infinity means an empty room. We use the value 2^31 - 1 = 2147483647 to represent INF as you may assume that the distance to a gate is less than 2147483647.
    Fill each empty room with the distance to its nearest gate. If it is impossible to reach a Gate, that room should remain filled with INF
    https://www.lintcode.com/problem/663/

    Approach 1 : Perform DFS on each room to find the closest gate - max rooms - mn, dfs - mn - O((mn) * (mn))
    Approach 2: Just like precious problem, we can do a multi-source bfs. We will add all the gates to a queue - perform bfs on all four directions - Will update a room
    only if its reachable faster from current gate - this way we will visit a cell max once - O(mn)
     */
    public void wallsAndGates(int[][] rooms) {
        int row = rooms.length;
        int col = rooms[0].length;
        Queue<List<Integer>> queue = new LinkedList<>();
        int length = 1;
        //All directions for a shorter code
        int[][] directions = new int[][]{{1,0},{-1,0},{0,1},{0,-1}};

        //Adding all gates to queue
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col ; c++){
                if(rooms[r][c] == 0) queue.add(List.of(r,c));
            }
        }

        while (!queue.isEmpty()){
            int qSize = queue.size();
            //For all gates
            for(int q = 0; q < qSize; q++){
                List<Integer> currGate = queue.poll();
                //Perform bfs on all four directions
                for(int[] direction : directions){
                    int currGateRow = currGate.get(0) + direction[0];
                    int currGateCol = currGate.get(1) + direction[1];
                    //Skip this cell if - its outside board, it's an obstacle or somebody(other gate) has already populated a value - means it's closer to that gate
                    if(currGateRow < 0 || currGateRow == row ||
                            currGateCol < 0 || currGateCol  == col ||
                            rooms[currGateRow][currGateCol] == -1 || rooms[currGateRow][currGateCol] < length) continue;
                    //Room is closer to current gate - update length & add to queue for performing bfs for next batch
                    rooms[currGateRow][currGateCol] = length;
                    //Why are we adding rooms in a queue made for gates? Well, these rooms will act as passage to gate for neighbours rooms.
                    queue.add(List.of(currGateRow,currGateCol));
                }
            }
            //For next bfs iteration length will be increased by 1
            length += 1;
        }
    }



}

