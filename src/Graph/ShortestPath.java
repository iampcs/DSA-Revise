package Graph;

import java.util.*;

/*
SSSP - Single Source Shortest Path - Shortest path from give source to all vertices
APSP - All Pairs Shortest Path - Shortest paths between all pairs of vertices.

All these algorithms apply for directed as well as undirected graphs.

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
        SSSP in undirected unweighted graph using BFS - We will be using distance array to store distance from source as well as if node is visited
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
    /*
        SSSP for directed weighted graph – Dijkstra’s Algorithm
        Dijkstra is similar to BFS, instead of Queue we use a priority queue here, it takes a greedy way to reach any vertex
        Given a weighted directed graph with non-negative edge weights and a source vertex, return the shortest path cost from the source vertex to every other reachable vertex in the graph.
        https://techiedelight.com/practice/?problem=SingleSourceShortestPaths
        Input: Graph [edges = [(0, 1, 10), (0, 4, 3), (1, 2, 2), (1, 4, 4), (2, 3, 9), (3, 2, 7), (4, 1, 1), (4, 2, 8), (4, 3, 2)], vertices = 5],
        source = 1
        Output: {[1, 2, 2], [1, 3, 6], [1, 4, 4]}

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
