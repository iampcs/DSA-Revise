package Graph;

import java.util.*;

/* Although the concept is easy to grasp - but a visual representation helps grasp it quicker
   Disjoint Set - Union & Find - Detect Cycle - https://www.youtube.com/watch?v=eTaWFhPXPz4
                  Union By Rank - Path Compression - https://www.youtube.com/watch?v=kaBX2s3pYO4

   A disjoint-set data structure is a data structure that keeps track of a set of elements partitioned into a number of disjoint (non-overlapping) subsets.
   Eg. Consider that there are 5 students in a classroom namely, A, B, C, D, E. They will be denoted as 5 different subsets: {A}, {B}, {C}, {D}, {E}.
   After some point of time, A became friends with B and C became friend with D. So, A and B will now belong to same set and C and D will now belong to same set.
   The disjoint data structure will now be: {A, B}, {C, D}, {E}. If at any point in time, we want to check that if any two students are friends or not then we can simply check if they belong to the same set or not.

   There are generally two types of operations performed on a Disjoint-Set data structure:
   1. Union(A, B): This operation tells to merge the sets containing elements A and B respectively by performing a Union operation on the sets.
   2. Find(A): This operation tells to find the subset to which the element A belongs - Each set will be representative(parent) - Find will return this parent.

   Implementation: The disjoint set data structure can be implemented using a Parent array representation. Although it's an array representation - think it as a tree - pointing upwards.
   Eg. Suppose we have 3 elements - {{0},{1},{2}} - parents array - [0,1,2] : Initially each is its own parent
   Union(1,0)  - {{0,1},{2}} - [0,0,2]           {0}  {2}
   Find(1) - 0                                    |
   Find(0) - 0                                   {1}
   Find(2) - 2

   Union(1,2) - {{0,1,2}} - [0,0,0]             {0}___
   Find(2) - 0                                   |   |
                                                {1} {2}
   Note that the implementation of union() and find() is naive and takes O(N) time in worst case. The trees created to represent subsets can be skewed and can become like a linked list.
   These methods can be improved to O(logN) using Union by Rank or Height. This could be further reduced to amortized constant time complexity with Path Compression
   1. Union by Rank - The idea is to always attach smaller depth tree under the root of the deeper tree. Why it's rank and not height? Because of path compression - it modifies this
      representative tree - where rank remains same but height will change.
   2. Path Compression - The idea is to flatten the tree when find is called. When find() is called for an element x, root of the tree is returned.
      The find() operation traverses up from x to find root.  The idea of path compression is to make the found root as parent of x so that we don't have to traverse all
      intermediate nodes again. If x is root of a subtree, then path (to root) from all nodes under x also compresses.
  Note : Disjoint Sets are used to find cycle in an undirected graph - When can we say there is a cycle in a graph? Adding an edge (src,dest) to a graph can form a cycle if -
  there is already a way to reach from "src to dest"(means they are part of same set).
    isCycle(int src, int dest){
        return find(src) == find(dest);
    }
 Note: We can also find our number of sets at present in a disjoint set - We can maintain a numOfSets - and initialize it with V. When ever we are performing an union and
 we are merging two sets with diff parent - we will reduce numOfSets.
 */
public class DisjointSets {
    public static void main(String[] args) {

    }

    static class UnionFind {
        // The disjoint set data structure can be implemented using a Parent array representation.
        // The i’th element of the array is the parent of the i’th item.
        int vertices;
        int[] parents;
        int[] ranks;
        int numOfSets;

        UnionFind(int vertices) {
            this.vertices = vertices;
            parents = new int[vertices];
            //Initially each element is its own parent
            for (int pos = 0; pos < vertices; pos++)
                parents[pos] = pos;
            //By default, all ranks are 0 only
            ranks = new int[vertices];
            numOfSets = vertices;
        }

        //Worst case complexity - O(N) in case hierarchy is linear
        int find(int v) {
            //If v is its own parent - root - return v
            if (parents[v] == v) return v;
            //v's parent could also be not root - so call recursively to find root
            return find(parents[v]);
        }

        // It takes, as input, two elements. And finds the representatives of their sets using the find operation,
        // and finally puts either one of the trees (representing the set) under the root node of the other tree, effectively merging the trees and the sets.
        //Worst case complexity - O(N) in case hierarchy is linear
        void union(int v, int x) {
            //Find parent of v
            int vParent = find(v);
            //Find parent of x
            int xParent = find(x);

            if(xParent != vParent) numOfSets -= 1;

            //Make parent of v's parent to that of x's parent - hence merging v subgroup to x.
            parents[vParent] = xParent;
        }

        int findWithPathCompression(int v) {
            //If v is not root - we find its root and replace its parent with root
            //So that next time we don't have to traverse all the way to root
            if (parents[v] != v)
                parents[v] = find(parents[v]);

            return parents[v];
        }

        //Similar to union - just that instead of making anyone(xParent) as root and then attaching the other to this subtree - we will be attaching the smaller one to bigger
        //subtree - this is determined by rank of a root. Only way height of tree will increase if we are merging two equal rank subsets.
        // In this way its guaranteed that height of tree won't be greater than log n
        void unionByRank(int v, int x) {
            int vParent = findWithPathCompression(v);
            int xParent = findWithPathCompression(x);
            if(xParent != vParent) numOfSets -= 1;
            // Rank of x's parent is smaller than that of v's parent - merge x's subset to v's
            if (ranks[xParent] < ranks[vParent]) parents[xParent] = vParent;
                // Rank of v's parent is smaller than that of v's parent - merge v's subset to x's
            else if (ranks[vParent] < ranks[xParent]) parents[vParent] = xParent;
                //x's and v's parent ranks are similar - this is similar to naive case - we can merge anyone into another - we just need to increase the rank of parent we are merging into
            else {
                //Mering x's subset to v's - this could have been other way around
                parents[xParent] = vParent;
                //Rank is increased for v's parent now
                ranks[vParent] += 1;
            }
        }

        boolean isCycle(int src, int dest) {
            return findWithPathCompression(src) == findWithPathCompression(dest);
        }
    }


    static class Edge {
        int source, dest, weight;

        public Edge(int source, int dest, int weight) {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

    }

    /* Redundant Connection - In this problem, a tree is an undirected graph that is connected and has no cycles.
       You are given a graph that started as a tree with n nodes labeled from 1 to n, with one additional edge added. The added edge has two different vertices chosen from 1 to n, and was not an edge that already existed.
       The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the graph.
       Return an edge that can be removed so that the resulting graph is a tree of n nodes. If there are multiple answers, return the answer that occurs last in the input.
       https://leetcode.com/problems/redundant-connection/

       In short return the first edge that introduce a cycle. We can do this easily with disjoint sets.

     */
    public int[] findRedundantConnection(int[][] edges) {
        //We are not given number of vertices - but told that it has one edge extra - means it has V edges - as V starts from 1, we are creating a class for (V+1)
        UnionFind unionFind = new UnionFind(edges.length + 1) ;
        int[] res = new int[2];
        for(int[] edge : edges){
            if(unionFind.isCycle(edge[0],edge[1])){
                res[0] = edge[0];
                res[1] = edge[1];
                break;
            }

            unionFind.unionByRank(edge[0],edge[1]);
        }

        return res;
    }

    /* Graph Valid Tree - Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), write a function to check whether these edges make up a valid tree.
       https://www.lintcode.com/problem/178/

       Simple application of union tree and spanning tree concept - for a spanning tree number of edges - V - 1
       Even if a graph has V-1 edges - it can have a cycle with disconnected nodes - we check this using union-find
     */
    public boolean validTree(int n, int[][] edges) {
        if(edges.length != n - 1) return false;
        UnionFind uf = new UnionFind(n);

        for(int[] edge : edges){
            if(uf.isCycle(edge[0],edge[1])) return false;
            uf.unionByRank(edge[0],edge[1]);
        }

        return true;
    }

    /* Number of Operations to Make Network Connected - There are n computers numbered from 0 to n - 1 connected by ethernet cables connections forming a network where connections[i] = [ai, bi] represents a connection between computers ai and bi.
      Any computer can reach any other computer directly or indirectly through the network. You are given an initial computer network connections.
      You can extract certain cables between two directly connected computers, and place them between any pair of disconnected computers to make them directly connected.
      Return the minimum number of times you need to do this in order to make all the computers connected. If it is not possible, return -1.
      https://leetcode.com/problems/number-of-operations-to-make-network-connected/

       Similar to above problem - To connect n nodes - we need n-1 edges, We just have to check if we have minimum number of edges to join all nodes(spanning tree) - if we do
       edge we need to move - number of subsets - 1
     */
    public int makeConnected(int n, int[][] connections) {
        if(connections.length < n-1) return -1;
        UnionFind uf = new UnionFind(n);

        for(int[] edge : connections){
            uf.unionByRank(edge[0],edge[1]);
        }

        return uf.numOfSets - 1;
    }

}
