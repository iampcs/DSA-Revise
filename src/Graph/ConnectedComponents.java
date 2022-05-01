package Graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ConnectedComponents {
    public static void main(String[] args) {

    }

    /* Connected Components in an undirected graph - Given a graph with V vertices. Find the number of Provinces.
       Note: A province is a group of directly or indirectly connected cities and no other cities outside the group.
       https://practice.geeksforgeeks.org/problems/number-of-provinces/1/
       Although here number of provinces are asked, we are also storing all connected components too - solution to question asking connected component
       Idea is to perform BFS on all vertices, if not visited. Now suppose 0-1 are connected and 2-3 are connected. We perform BFS on 0, we get edge (0,1). We won't perform BFS on 1, as its already visited.
       There is no way we can reach 2,3 from 0. Our BFS queue will be empty now, we know one connected component is over. Again BFS will start from vertex 2, add (2-3) to connected component

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
                        if(neighbour != currentNode && adjMatrix.get(currentNode).get(neighbour)!= 0 && visited[neighbour] == false){
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
}
