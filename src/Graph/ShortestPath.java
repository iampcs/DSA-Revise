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
 */
public class ShortestPath {
    public static void main(String[] args) {

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

       This question basically is finding max among SSSPs, if a node is not reachable from source, return -1.
     */
    public int networkDelayTime(int[][] times, int n, int k) {


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
