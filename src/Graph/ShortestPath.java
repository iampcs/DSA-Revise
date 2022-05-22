package Graph;

import java.util.*;

/*
SSSP - Single Source Shortest Path - The Shortest path from give source to all vertices
APSP - All Pairs Shortest Path - The Shortest paths between all pairs of vertices.

All these algorithms apply for directed as well as undirected graphs.

SSSP for unweighted graph - BFS - O(V + E)
SSSP for weighted graph without negative cycle - Dijkstra - O(E + VLogV)
SSSP for weighted graph with negative cycle - Bellman-Ford - O(VE)
APSP for weighted graph with negative cycle - Floyd-Warshall - O(V^3)

Note: We can also tweak the algorithm's to find max/min of anything from source to destination - For Dijkstra we keep adding edge weight to pick the next edge - what if we change
this to match with our question ? We will be solving some problems like this.
 */
public class ShortestPath {
    public static void main(String[] args) {
        swimInWater( new int[][]{{0,2},{1,3}});
    }

    /* SSSP in undirected unweighted(Can also be interpreted as weight of 1 per edge) graph using BFS
    We will be using distance array to store distance from source as well as if node is visited */
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
    /*
        SSSP for directed weighted graph – Dijkstra’s Algorithm
        Dijkstra is similar to BFS, instead of Queue we use a priority queue here, it takes a greedy way to reach any vertex
        Given a weighted directed graph with non-negative edge weights and a source vertex, return the shortest path cost from the source vertex to every other reachable vertex in the graph.
        https://techiedelight.com/practice/?problem=SingleSourceShortestPaths
        Input: Graph [edges = [(0, 1, 10), (0, 4, 3), (1, 2, 2), (1, 4, 4), (2, 3, 9), (3, 2, 7), (4, 1, 1), (4, 2, 8), (4, 3, 2)], vertices = 5],
        source = 1        Output: {[1, 2, 2], [1, 3, 6], [1, 4, 4]}
        source = 0        Output: {[0, 1, 4], [0, 2, 6], [0, 3, 5], [0, 4, 3]}

        Assumption - Graph is connected.

        Idea behind Dijkstra is simple - we create Pair <vertex, weight to reach that vertex> - push this pair to priority queue and poll the vertex with the least <weight to reach vertex>.
        Perform BFS for this polled vertex. This always picking the vertex with the least weight to reach is greedy approach.
        Can check this video for graph traversal.
        https://www.youtube.com/watch?v=sD0lLYlGCJE&list=PL-Jc9J83PIiHfqDcLZMcO9SsUDY4S3a-v&index=16
     */
    public Set<List<Integer>> findShortestPaths(Graph graph, int source)
    {
        Set<List<Integer>> paths = new HashSet<>();
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(node -> node.weightToReachNode));
        boolean[] visited = new boolean[graph.vertices];

        priorityQueue.add(new Node(source,0, source + " "));

        while (!priorityQueue.isEmpty()){
            Node currentNode = priorityQueue.poll();

            if(visited[currentNode.currentNode] == false){
                //We have reached an unvisited node - add this to paths - we are skipping source to source case
                if(currentNode.currentNode != source)
                    //Add [source, destination, weight of path]
                    paths.add(List.of(source,currentNode.currentNode,currentNode.weightToReachNode));
                //Mark this node as visited
                visited[currentNode.currentNode] = true;
                //For every edge from this node
                for(Edge edge : graph.adjList.get(currentNode.currentNode)){
                    //Add neighbour to queue if not visited
                    //We are also adding path to current node
                    if(visited[edge.dest] == false){
                        priorityQueue.add(new Node(edge.dest, currentNode.weightToReachNode + edge.weight, currentNode.pathToReachNode + " " + edge.dest ));
                    }
                }
            }
        }
        return paths;
    }

    /* Network Delay Time - You are given a network of n nodes, labeled from 1 to n. You are also given times, a list of travel times as directed edges times[i] = (ui, vi, wi), where ui is the source node, vi is the target node, and wi is the time it takes for a signal to travel from source to target.
       We will send a signal from a given node k. Return the time it takes for all the n nodes to receive the signal. If it is impossible for all the n nodes to receive the signal, return -1.
       https://leetcode.com/problems/network-delay-time/

       This is a directed graph with weighted edges. We can use Dijkstra’s Algorithm here. As the transmission is parallel, minimum time to reach all nodes
       will be the time it takes to add the last edge(max one, in-case of multiple) to the shortest path in Dijkstra’s Algorithm.
     */
    public int networkDelayTime(int[][] times, int n, int k) {
        Set<Integer> visited = new HashSet<>();
        //int[0] : target node, int[1]: edge weight to reach target node
        PriorityQueue<int[]> minheap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        int minTime = 0;
        Map<Integer, List<int[]>> adjList = new HashMap<>();

        //Create adjList
        for (int[] edge : times){
            if(!adjList.containsKey(edge[0])) adjList.put(edge[0], new ArrayList<>());
            adjList.get(edge[0]).add(edge);
        }
        //Add start node to minHeap with weight to reach as 0
        minheap.add(new int[]{k,0});
        while (!minheap.isEmpty() && visited.size() != n){
            int[] currEdge = minheap.poll();
            if(!visited.contains(currEdge[0])){
                visited.add(currEdge[0]);
                //As we add new int[] to minheap as <node, weight to reach that node> , check if time to reach this node is max
                minTime = Math.max(minTime, currEdge[1]) ;
                //If there are outgoing edges from current node
                if(adjList.containsKey(currEdge[0])){
                    //For each neighbour
                    for(int[] neighbours : adjList.get(currEdge[0])){
                        //If it's not already visited - add to minHeap as <node , time to reach (node - 1) + time to reach node from (node - 1)>
                        if(!visited.contains(neighbours[1])) minheap.add(new int[]{neighbours[1], currEdge[1] + neighbours[2]});
                    }
                }
            }
        }
        //Return minTime in case it's a connected graph only
        return visited.size() == n ? minTime : -1;
    }
    /* Cheapest Flights Within K Stops - There are n cities connected by some number of flights. You are given an array flights where flights[i] = [fromi, toi, pricei] indicates that there is a flight from city fromi to city toi with cost pricei.
       You are also given three integers src, dst, and k, return the cheapest price from src to dst with at most k stops. If there is no such route, return -1.
       https://leetcode.com/problems/cheapest-flights-within-k-stops/

       There are many approaches to solve this :
       1. DFS - we will check all paths from source to destination - maintain a minHeap - add <distance, k> to minHeap whenever we reach dst within k stops, return top of minHeap
       2. Dijkstra algorithm with a twist - We will start src with (K+1) max possible stops - don't add an edge if we don't have any K left - O(VE) - https://www.youtube.com/watch?v=IQOG3w4abAg
       2. Bellman-Ford - We know with bellman ford, we get the shortest path with V-1 loops, Here we have max k loops available - we will see with k loops, if we can reach
          destination, if yes, what is the minimum value - return if present. O(E*k) - https://www.youtube.com/watch?v=5eIK3zUdYmE

     */
    public static int findCheapestPriceDijkstra(int n, int[][] flights, int src, int dst, int k) {
        //int[0] : target node, int[1]: edge weight to reach target node, int[2] = k - or vertices between source-destination
        PriorityQueue<int[]> minheap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        List<List<int[]>> adjList = new ArrayList<>();
        for(int i = 0; i < n ; i++) adjList.add(i, new ArrayList<>());

        //Create adjList
        for (int[] edge : flights){
            adjList.get(edge[0]).add(new int[]{edge[1], edge[2]});
        }
        minheap.add(new int[]{src, 0, k + 1});

        while (!minheap.isEmpty()){
            //int[0] : target node, int[1]: edge weight to reach target node, int[2] = k - or vertices between source-destination
            int[] currNode = minheap.poll();

            if(currNode[0] == dst) return currNode[1];
            if(currNode[2] > 0){
                for (int[] edge : adjList.get(currNode[0])){
                    minheap.add(new int[]{edge[0], currNode[1] + edge[1] , currNode[2] - 1});
                }
            }
        }

        return -1;
    }

    public int findCheapestPriceBellman(int n, int[][] flights, int src, int dst, int k) {
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[src] = 0; // 0 needed to reach 0;

        //k is the number os stops excluding source and destination - so we can run the loop k+1 times
        for(int i = 0; i < k+1; i++){
            //Note: This is a changes approach from Bellman Ford - Because we strictly want to calculate for single length - we won't be using original distance array
            //Suppose in original array we relax edge(u,v) and then pick edge(v,w) - here we will be relaxing w too, which is not possible with length 1 per iteration
            //So we will maintain a iterationDistance temporary distance array - to record changes in this iteration
            int[] iterationDistance = distance.clone();
            // For each edge, calculate the new distance
            for(int[] flight : flights){
                int s = flight[0];
                int d = flight[1];
                int cost = flight[2];

                //Source this iteration is not infinity and target cost is more than source cost plus edge(source,target) - relax target
                //Note how we are taking source from iterationDistance only and updating target for distance.
                //We don't want the source(current target) for next node to be polluted
                if(iterationDistance[s] != Integer.MAX_VALUE && distance[d] > iterationDistance[s] + cost){
                    distance[d] = iterationDistance[s] + cost; // Update it in the current row
                }
            }
        }
        return distance[dst] == Integer.MAX_VALUE ? -1 : distance[dst];
    }
    /* Swim in Rising Water - You are given an n x n integer matrix grid where each value grid[i][j] represents the elevation at that point (i, j).
       The rain starts to fall. At time t, the depth of the water everywhere is t.
       You can swim from a square to another 4-directionally adjacent square if and only if the elevation of both squares individually are at most t.
       You can swim infinite distances in zero time. Of course, you must stay within the boundaries of the grid during your swim.
       Return the least time until you can reach the bottom right square (n - 1, n - 1) if you start at the top left square (0, 0).
       https://leetcode.com/problems/swim-in-rising-water/

       Approach - https://www.youtube.com/watch?v=amvrKlMLuGY
      Although this question seems difficult to grasp at first - it basically is asking for a path to end where max height of a cell in path is minimum. Because we can
      move any number of cells in constant time - we have to wait t time for cells to be equal. So for any given path - max time taken will be maximum height cell of that path.

      Now that we know this - how can we use Dijkstra algorithm here ? What if we choose a cell based on max-height of path taken from minHeap?
      How do we calculate max-height of a path - it will be maximum of (current max height of path, height of to be newly added cell to path).
      Complexity - O(n^2 * log n ) - grid size * heapify

     */
    public static int swimInWater(int[][] grid) {
        int row = grid.length;
        int col = grid[0].length;
        Set<List<Integer>> visited = new HashSet<>();
        //int[0] : height , int[1] : row, int[2] : col
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        minHeap.add(new int[]{grid[0][0], 0,0});

        while (!minHeap.isEmpty()){
            int[] cell = minHeap.poll();
            int rCell = cell[1];
            int cCell = cell[2];
            int hCell = cell[0];
            //Have we reached end ? This is a greedy solution - the first time we reached will be actual solution - return height
            if(rCell == row - 1 && cCell == col - 1) return hCell;

            visited.add(List.of(rCell, cCell));

            //Bfs in all four directions if cell not visited yet - This is a greedy solution - if a cell is visited previously - that must be its best possible path
            //Note how we are adding new cell height - it will be maximum of (current max height of path, height of to be newly added cell to path)
            if( rCell < row - 1 && !visited.contains(List.of(rCell + 1,cCell))) minHeap.add(new int[]{Math.max(hCell,grid[rCell + 1][cCell]), rCell + 1, cCell});
            if( rCell > 0 && !visited.contains(List.of(rCell - 1,cCell))) minHeap.add(new int[]{Math.max(hCell,grid[rCell - 1][cCell]), rCell - 1, cCell});
            if( cCell < col - 1 && !visited.contains(List.of(rCell,cCell + 1))) minHeap.add(new int[]{Math.max(hCell,grid[rCell][cCell + 1]), rCell ,cCell + 1});
            if( cCell > 0 && !visited.contains(List.of(rCell,cCell - 1))) minHeap.add(new int[]{Math.max(hCell,grid[rCell][cCell - 1]), rCell, cCell - 1});

        }

        return 0;
    }


    /* Word Ladder - A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that:
        Every adjacent pair of words differs by a single letter.
        Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
        sk == endWord
        Given two words, beginWord and endWord, and a dictionary wordList, return the number of words in the shortest transformation sequence from beginWord to endWord, or 0 if no such sequence exists.
        https://leetcode.com/problems/word-ladder/

       This is basically finding the shortest length between start and end word - number of characters that differ between two words are edge length
       as we can only move to an edge length of 1, this is similar to an unweighted graph - we can use bfs to find the length of the shortest path.
       The trick here is to make the adjacency list from inputs. Once we have that we will perform multi-source BFS to check if we can reach from
       source to end. We are using multi-source BFS here because we want to process all words at edge length 1 from current word. Refer traversal problems such as wall & gates, rotten oranges to understand multi-source BFS.
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        //Set of all words given - this includes endWord
        Set<String> set = new HashSet<>(wordList);
        if(!set.contains(endWord)) return 0;
        // BFS queue - add startWord to start
        Queue<String> queue = new LinkedList<>();
        queue.add(beginWord);
        //Set of visited words
        Set<String> visited = new HashSet<>();
        queue.add(beginWord);
        //Length of our shortest path - as end will be a part of path - we can initiate it with 1
        int length = 1;

        while(!queue.isEmpty()){
            int size = queue.size();
            //We are using multi-source BFS here because we want to process all words at edge length 1 from current word
            for(int i = 0; i < size; i++){
                String word = queue.poll();
                //Have we reached end ?
                if(word.equals(endWord)) return length;

                //Find all possible length 1 words for current word
                for(int j = 0; j < word.length(); j++){
                    for(char k = 'a'; k <= 'z'; k++){
                        char arr[] = word.toCharArray();
                        arr[j] =  k;

                        String str = new String(arr);
                        //Check if it's a valid word from wordList and is not visited already
                        if(set.contains(str) && !visited.contains(str)){
                            //Add to bfs queue for next batch and mark it as visited
                            queue.add(str);
                            visited.add(str);
                        }
                    }
                }
            }
            // length will increase after each bfs iteration
            length += 1;
        }
        return 0;
    }

    /* Bellman-Ford Algorithm for Shortest Path : O(VE)- Dijkstra doesn't work for Graphs with negative weight edges, it gives an incorrect solution.
       Bellman-Ford works for such graphs - even though it can't give you a correct solution - because it doesn't exist for a negative cycle,
       but it can detect a negative cycle. Bellman-Ford is also simpler than Dijkstra and suites well for distributed systems. But time complexity of Bellman-Ford is O(VE)
       https://practice.geeksforgeeks.org/problems/negative-weight-cycle3504/1

       It works on a simple idea - the shortest length(not weight) of the shortest path will be (V - 1). It runs a loop V-1 times and relaxes edges
       one by one - so with each iteration we will get closer to our shortest path - because it starts with length 1, and then keep adding to it each iteration
       it can be seen as bottoms up approach of DP.
       Algorithm:
       1. Initiate distance array distance[V] with all values as infinite, set distance of source to 0, distance[source] = 0
       2. loop - (V-1) times - By end of this we will be guaranteed to have a shortest path of length (V-1), if exists
           For each edge (u,v)
           if : distance[v] > distance[u] + weight(u,v)
                distance[v] = distance[u] + weight(u,v)
       3. Run this for loop one more time, if shortest path is changing - there is a negative cycle

       Can watch this video for ref -  https://www.youtube.com/watch?v=FrLWd1tJ_Wc
     */
    public int isNegativeWeightCycle(int n, int[][] edges)
    {
        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        //Making 0th vertex as source index - it could be any index
        distance[0] = 0;

        for(int count = 0; count < n - 1; count++){
            for(int[] edge : edges){
                //Updating distance if we found a path with less weight
                //if : distance[v] > distance[u] + weight(u,v)
                //     distance[v] = distance[u] + weight(u,v)
                if(distance[edge[0]] != Integer.MAX_VALUE && distance[edge[1]] > distance[edge[0]] + edge[2])
                    distance[edge[1]] = distance[edge[0]] + edge[2];
            }
        }
        //Running this loop one more time to detect negative cycle
        for(int[] edge : edges){
            //We can still find a path with less weight - means there is a negative cycle
            if(distance[edge[1]] > distance[edge[0]] + edge[2])
                return 1;
        }

        return 0;
    }





    class Edge    {
        int source, dest, weight;
        public Edge(int source, int dest, int weight)
        {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }
    }

    class Node {
        int currentNode;
        int weightToReachNode;
        String pathToReachNode;

        Node(int currentNode, int weightToReachNode, String pathToReachNode){
            this.currentNode = currentNode;
            this.weightToReachNode = weightToReachNode;
            this.pathToReachNode = pathToReachNode;
        }
    }

    class Graph    {
        List<List<Edge>> adjList;
        int vertices;
        Graph(List<Edge> edges, int vertices)
        {
            adjList = new ArrayList<>();
            this.vertices = vertices;

            for (int i = 0; i < vertices; i++) {
                adjList.add(new ArrayList<>());
            }

            for (Edge edge: edges) {
                adjList.get(edge.source).add(edge);
            }
        }
    }


}
