package Tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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




}



