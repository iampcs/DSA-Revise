package Graph;

import java.util.*;

/* When we are scheduling jobs or tasks, they may have dependencies. For example, before we finish task a, we have to finish b first.
   In this case, given a set of tasks and their dependencies, how shall we arrange our schedules?
   Topological Sort helps here. It's a linear ordering of all vertices such that for any edge (u, v), u comes before v.
   Another way to describe it is that when you put all vertices horizontally on a line, all the edges are pointing from left to right.
   E.g. For a graph  0 -> 1 -> 2 :  Represents 1 is dependent on 0, 2 & 3 are dependent on 1. Topological Order - 0,1,2,3 or 0,1,3,2
                          |->  3
   Fundamental concepts related to topological sort :
   1. Source: Any node that has no incoming edge(in-degree) and has only outgoing edges is called a source.
   2. Sink: Any node that has only incoming edges(in-degree) and no outgoing edge is called a sink.
      So, we can say that a topological ordering starts with one of the sources and ends at one of the sinks.
   3. A topological ordering is possible only when the graph has no directed cycles, i.e. if the graph is a Directed Acyclic Graph (DAG).
      If the graph has a cycle, some vertices will have cyclic dependencies which makes it impossible to find a linear ordering among vertices.
 */
public class TopologicalSort {
    public static void main(String[] args) {

    }
    /* This is BFS approach - Also known as Kahn's Algorithm - O(V + E)
       1. Create an in-degree array which stores in-degree of all vertices in an array - For this we have to traverse all edges - O(E)
       2. Find all sources(in-degree = 0) and add to BFS queue - O(V)
       3. Poll from queue and perform until queue is empty
          3.1 Add to topological sorted list
          3.2 Get all its neighbour - reduce their in-degree by 1 (because we have already processed one of its dependencies)
          3.3 If for any neighbour - in-degree is 0(it's a source now), add to queue.
       4. If length of topological sort array is not equal to number of vertices - graph has a cycle - topological order not possible

       Note: Assumption : This is a DAG - so no need to maintain a visited array.
       Also, if graph contains a cycle - size of topologicalSort will be less than V - because there will be case when vertices are there but none of them have in-degree of 0
     */
    static List<Integer> topologicalSortBFS(int V, ArrayList<ArrayList<Integer>> adj)
    {
        List<Integer> topologicalSort = new ArrayList<>();

        // Creating and populating in-degree array
        int[] inDegrees = new int[V];
        for(ArrayList<Integer> edges : adj){
            for(int vertex : edges) inDegrees[vertex] += 1;
        }
        //Adding all source to queue
        Queue<Integer> queue = new LinkedList<>();
        for(int vertex = 0; vertex < V; vertex++){
            if(inDegrees[vertex] == 0) queue.add(vertex);
        }

        while (!queue.isEmpty()){
            //Get current source and add it to topologicalSort
            Integer currSource = queue.poll();
            topologicalSort.add(currSource);
            //For all neighbour of current source
            for (int neighbour = 0; neighbour < adj.get(currSource).size(); neighbour++){
                int currentNeighbour = adj.get(currSource).get(neighbour);
                //Reduce their in-degree
                inDegrees[currentNeighbour] -= 1;
                //Add as a source if in-degree is zero
                if(inDegrees[currentNeighbour] == 0) queue.add(currentNeighbour);
            }
        }

        return topologicalSort;
    }
    /* Topological Sort with DFS - Approach is similar to DFS of graph
      1. We will maintain a visited[] array to check is a node is already visited - Why, if there are no cycle ? Even if there are no cycle - a node can be reached by two ways
         In case of BFS we were starting for a node with no dependencies - we are guaranteed not to add source twice - in DFS its random.
      2. Maintain a List to store topological sort
      3. Idea behind DFS is simple - For any node 'a', all its dependents (b,c) should be added to sorting list first and then a - [a,b,c]
         So, perform dfs on all dependents of 'a' and then add 'a' to front.

     */
    static List<Integer> topologicalSortDFS(int V, ArrayList<ArrayList<Integer>> adj){
        boolean[] visited = new boolean[V];
        List<Integer> topologicalSort = new LinkedList<>();
        //We will run dfs for all nodes
        for(int vertex = 0; vertex < V; vertex++)
            topologicalSortDFS(vertex,adj,visited, topologicalSort);

       return topologicalSort;
    }

    private static void topologicalSortDFS(int source, ArrayList<ArrayList<Integer>> adj, boolean[] visited, List<Integer> topologicalSort) {
        if(!visited[source]){
            visited[source] = true;
            //For all neighbours(dependents) of source
            for(int nIndex = 0 ; nIndex < adj.get(source).size(); nIndex++){
                int neighbour = adj.get(source).get(nIndex);
                //perform dfs
                topologicalSortDFS(neighbour,adj,visited,topologicalSort);
            }
            //In the end add source to front of list
            topologicalSort.add(0,source);
        }
    }

    /* Course Schedule - There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
       You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
       For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
       Return true if you can finish all courses. Otherwise, return false.
       https://leetcode.com/problems/course-schedule/
       Eg. Input: numCourses = 2, prerequisites = [[1,0]]
           Output: true

       We can see we need topological sort to do this - we are not sure if this graph contains a cycle - So we will perform a topologicalSortBFS on this graph -
       if the size of sort array is same as numberCourses - we will return true else false

       Similar problem -  Course Schedule II
       https://leetcode.com/problems/course-schedule-ii/

       Here they are asking for a topological order if exists - we are either way calculating one in BFS.
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        //We need to convert this input to adjList
        ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
        for(int course = 0; course < numCourses; course++) adjList.add(new ArrayList<Integer>());

        for(int[] prerequisite : prerequisites){
            adjList.get(prerequisite[1]).add(prerequisite[0]);
        }

        List<Integer> topologicalSort = topologicalSortBFS(numCourses,adjList);

        return topologicalSort.size() == numCourses;
    }

    /* Find all Possible Topological Orderings of a DAG - Given a list of edges of a directed acyclic graph (DAG), return a set of all topological orderings of the graph.
       Eg. Input: edges = [(0, 1), (0, 2)]  Output: {[0, 1, 2], [0, 2, 1]}
           Input: edges = [(0, 2), (1, 2), (2, 3), (2, 4), (3, 4), (3, 5)]  Output: {[0, 1, 2, 3, 4, 5], [0, 1, 2, 3, 5, 4], [1, 0, 2, 3, 4, 5], [1, 0, 2, 3, 5, 4]}
       Following is a practice example - it uses diff data structure to store edge and graph data. In interest of continuation of previous problem - will use the same
       input/output format as above.
       https://techiedelight.com/practice/?problem=TopologicalSortII
       Following problem gets an adjList and number of vertices - print all topologicalSorts
       We will use the same BFS approach with backtracking. In first step we add all vertices with 0 in-degree to queue and perform BFS - now in which order we poll these vertices
       determines our topological sort - In this case - we will try all possible combinations
     */

    private static List<List<Integer>> allTopologicalSorts(int V, List<List<Integer>> adjList){
        List<List<Integer>> allTopologicalSorts = new ArrayList<>();
        // Creating and populating in-degree array
        int[] inDegrees = new int[V];
        for(List<Integer> edges : adjList){
            for(int vertex : edges) inDegrees[vertex] += 1;
        }
        //Adding all source to queue
        Queue<Integer> queue = new LinkedList<>();
        for(int vertex = 0; vertex < V; vertex++){
            if(inDegrees[vertex] == 0) queue.add(vertex);
        }

        allTopologicalSorts(V,adjList, queue, new ArrayList<Integer>(), allTopologicalSorts, inDegrees);
        return allTopologicalSorts;
    }
    //Logic is simple backtracking - try every source in queue as first source to expand and include its neighbour - do this recursively for all sources in queue
    private static void allTopologicalSorts(int V, List<List<Integer>> adjList, Queue<Integer> queue, ArrayList<Integer> currentSortedList,
                                            List<List<Integer>> allTopologicalSorts, int[] inDegrees) {
        if(!queue.isEmpty()){
            //Try this for all sources in current queue
            for(int source : queue){
                //Add this source to sorted list
                currentSortedList.add(source);
                //Create a new queue everytime - this new queue will have 'source' as first source to expand
                //So we will do the whole routine of removing the source - decrementing its neighbours in-degree - adding them to queue if they are 'source'
                Queue<Integer> queueForNextPossibility = new LinkedList<>(queue);
                queueForNextPossibility.remove(source);

                //For all neighbour of current source
                for (int neighbourIndex = 0; neighbourIndex < adjList.get(source).size(); neighbourIndex++){
                    int currentNeighbour = adjList.get(source).get(neighbourIndex);
                    //Reduce their in-degree
                    inDegrees[currentNeighbour] -= 1;
                    //Add as a source if in-degree is zero
                    if(inDegrees[currentNeighbour] == 0) queueForNextPossibility.add(currentNeighbour);
                }

                //Recurse for other vertices - taking 'source' as first node to expand
                allTopologicalSorts(V,adjList,queueForNextPossibility,currentSortedList, allTopologicalSorts, inDegrees);

                //----------------- Backtracking -------------------
                //Get the graph ready for next loop when a new source will be treated as first source to expand
                //Remove current source from sorted list - will be adding new source to it in next loop
                currentSortedList.remove(currentSortedList.size() - 1);
                for (int neighbourIndex = 0; neighbourIndex < adjList.get(source).size(); neighbourIndex++){
                    int currentNeighbour = adjList.get(source).get(neighbourIndex);
                    inDegrees[currentNeighbour] += 1;
                }
            }
        }
        // For happy case - all vertices are consumed by now, queue is empty and currentSortedList is as long as number of vertices - add this to list of all solution
        //If currentSorted size is less than V - there is cycle - no solution
        if(currentSortedList.size() == V) {
            allTopologicalSorts.add(new ArrayList<>(currentSortedList));
        }
    }


}
