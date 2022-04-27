package Tree;

import java.util.*;

/* Questions will be straight forward - We will have to do operations horizontally or in levels.
Template for level traversal is simple - tracking levels based on a limiter
1. Offer root and a delimiter to queue
2. While !queue.isEmpty()
    * poll element from queue - poll()
    * if element
        add left & right child if not empty
    * else delimiter
        perform operations you want to do after every level change
        add delimiter to queue if queue is not empty - to track new level

There is one more template we can use to traverse in levels - this approach is useful if we want to do some operations between nodes of a level - like connecting them
Tracking levels based on queue size
1. Offer root to queue
2. While !queue.isEmpty()
   * get current level size - queue.size()
   * for(i -> (0,level size))
        poll element from queue - poll()
        add left & right child if not empty
   * perform operations you want to do after every level change

 */

public class BreadthFirstSearch {
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }
    public static void main(String[] args) {

    }
    /* Maximum Level Sum of a Binary Tree - Given the root of a binary tree, the level of its root is 1, the level of its children is 2, and so on.
       Return the smallest level x such that the sum of all the values of nodes at level x is maximal.
       https://leetcode.com/problems/maximum-level-sum-of-a-binary-tree/

       //Just do a level order traversal and keep track of current level sum and maxSum - return the level with maxSum

     */
    public int maxLevelSum(TreeNode root) {
        if(root.left == null && root.right == null) return root.val;

        int solution = 1;
        int currentLevel = 1;
        //There could be negative values too
        Long maxSum = Long.MIN_VALUE;
        Long currLevelSum = 0L;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        //We are using nulls to mark level boundaries
        queue.add(null);

        while (!queue.isEmpty()){
            TreeNode currNode = queue.poll();
            if(currNode != null){
                currLevelSum += currNode.val;
                if(currNode.left != null) queue.add(currNode.left);
                if(currNode.right != null) queue.add(currNode.right);
            }
            else{
                //We have found a new maxSum level
                if(currLevelSum > maxSum) {
                    solution = currentLevel;
                    maxSum = currLevelSum;
                }
                currLevelSum = 0L;
                currentLevel += 1;

                //Previous level over - if there are more elements remaining - add null to define current level boundary
                if(!queue.isEmpty()) queue.add(null);
            }
        }

        return solution;
    }

    /* Binary Tree Right Side View - Given the root of a binary tree, imagine yourself standing on the right side of it, return the values of the nodes you can see ordered from top to bottom.
       https://leetcode.com/problems/binary-tree-right-side-view/
       We put a null after each level, when polling from queue, just check if next element is null(even if the queue is empty - we will get null from peek)- it means this is the last element in this
       level - store this in solution.

     */
    public List<Integer> rightSideView(TreeNode root) {
        List<Integer> solution = new ArrayList<>();
        if(root == null) return solution;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()){
            TreeNode currNode = queue.poll();
            if(currNode != null){
                if(queue.peek() == null) solution.add(currNode.val);
                if(currNode.left != null) queue.add(currNode.left);
                if(currNode.right != null) queue.add(currNode.right);
            }else {
                if(!queue.isEmpty()) queue.add(null);
            }
        }
        return solution;
    }

    /* Binary Tree Zigzag Level Order Traversal - Given the root of a binary tree, return the zigzag level order traversal of its nodes' values.
       https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/

       There are many ways to solve this. We are doing a normal tree traversal - only when it's required to store elements backward - we keep adding those elements
       to 0th index, hence adding in reverse position.

     */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> solution = new ArrayList<>();
        if(root == null) return  solution;

        boolean isReverse = true;
        List<Integer> currLevel = new LinkedList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);
        solution.add(List.of(root.val));

        while (!queue.isEmpty()){
            TreeNode currNode = queue.poll();
            if(currNode != null){
                if(isReverse){
                    if(currNode.left != null){
                        queue.add(currNode.left);
                        //This is the clever part - adding new values to 0th index
                        currLevel.add(0,currNode.left.val);
                    }
                    if(currNode.right != null){
                        queue.add(currNode.right);
                        //This is the clever part - adding new values to 0th index
                        currLevel.add(0, currNode.right.val);
                    }
                }
                else{
                    if(currNode.left != null){
                        queue.add(currNode.left);
                        currLevel.add(currNode.left.val);
                    }
                    if(currNode.right != null){
                        queue.add(currNode.right);
                        currLevel.add(currNode.right.val);
                    }

                }
            }else{
                isReverse = !isReverse;
                //We add a delimiter to end of last level too, polling that will make us add an empty level to solution
                if(currLevel.size() > 0){
                    solution.add(new ArrayList<>(currLevel));
                    currLevel.clear();
                }
                currLevel.clear();

                if(!queue.isEmpty()) queue.add(null);
            }
        }
        return solution;
    }

    /* Populating Next Right Pointers in Each Node /  Populating Next Right Pointers in Each Node ||
       Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
       We have solved this using BFS, this can be solved using DFS too
       In approach 1, we are keeping a track of all nodes in current level, once level is over - we are populating next nodes for each of them
       In approach 2
     */

    public Node connect(Node root) {
        if(root == null) return null;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);
        root.next = null;

        ArrayList<Node> currLevel = new ArrayList<>();

        while (!queue.isEmpty()){
            Node currNode = queue.poll();
            if(currNode != null){
                if(currNode.left != null) {
                    queue.add(currNode.left);
                    currLevel.add(currNode.left);
                }
                if(currNode.right != null) {
                    queue.add(currNode.right);
                    currLevel.add(currNode.right);
                }
            }else {
                if(!currLevel.isEmpty()){
                    for(int i = 0; i < currLevel.size() - 1; i++){
                        currLevel.get(i).next = currLevel.get(i+1);
                    }
                    currLevel.get(currLevel.size() - 1).next = null;
                    currLevel.clear();
                }

                if(!queue.isEmpty()) queue.add(null);
            }
        }

        return root;
    }

    public Node connectTemplate2(Node root) {
        if(root == null) return null;
        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        while(!q.isEmpty()) {
            Node rightNode = null;
            for(int i = q.size(); i > 0; i--) {
                Node cur = q.poll();
                cur.next = rightNode;
                rightNode = cur;
                //Observe this - We are traversing from right to left - so that for every left there is a right
                if(cur.right != null)  q.offer(cur.right);
                if(cur.left != null)   q.offer(cur.left);

            }
        }
        return root;
    }
    //DFS approach
    public Node connectDFS(Node root) {
        if(root == null) return null;
        Node leftChild = root.left, rightChild = root.right, nextPointer = root.next;
        if(leftChild != null) {
            //Draw this on a piece of paper - it's easier to understand if we have a visual diagram here
            //Null case is covered here
            leftChild.next = rightChild;
            //We want to connect current root right child to current root sibling's left child
            //We are using next pointer here cleverly
            if(nextPointer != null) rightChild.next = nextPointer.left;

            connect(leftChild);
            connect(rightChild);
        }

        return root;
    }



}



