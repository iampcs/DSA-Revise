package Tree;

import java.util.ArrayList;
import java.util.List;

public class DepthFirstSearch {

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

    /* Diameter of Binary Tree - Given the root of a binary tree, return the length of the diameter of the tree.
      The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.
      The length of a path between two nodes is represented by the number of edges between them.
      https://leetcode.com/problems/diameter-of-binary-tree/

      There are three possibilities :
      1. Diameter passes through current node - diameter : left subtree height + right subtree height + 2(edges connecting left,right subtree to current node)
      2. Diameter is present on left side of tree  - diameter : diameter of left subtree
      3. Diameter is present on right side of tree - diameter : diameter of right subtree

      Max of these three will be diameter of current node.

      We don't want to calculate height multiple times at each node - So each call to subtree will return two info to current node -
      diameter of subtree, and height of subtree : current node can find out its diameter from this information if it calls on both subtrees.
      And will pass along, its height and diameter to its parent - until we reach root.

     */
    public int diameterOfBinaryTree(TreeNode root) {
        if(root == null) return 0;

        return diameterOfBinaryTreeDP(root)[0];
    }

    //int[0] : Diameter of that node , int[1] : Height of that node
    private int[] diameterOfBinaryTreeDP(TreeNode root){
        //We are below leaf node of root is null - Diameter will be zero - no edges, height will be -1, we are below leaf node
        if(root == null) return new int[]{0,-1};

        // Left subtree - leftInfo[0] - diameter , leftInfo[1] : height
        int[] leftInfo = diameterOfBinaryTreeDP(root.left);

        // Right subtree - rightInfo[0] - diameter , rightInfo[1] : height
        int[] rightInfo = diameterOfBinaryTreeDP(root.right);

        //If diameter pass from current root
        int diameterViaRoot = leftInfo[1] + rightInfo[1] + 2;

        // Return diameter of current root + height of current root
        return new int[]{Math.max(diameterViaRoot, Math.max(leftInfo[0], rightInfo[0])),
                         Math.max(leftInfo[1], rightInfo[1]) + 1 };
    }

    /* Balanced Binary Tree - Given a binary tree, determine if it is height-balanced.
       For this problem, a height-balanced binary tree is defined as: a binary tree in which the left and right subtrees of every node differ in height by no more than 1.
       https://leetcode.com/problems/balanced-binary-tree/

       Exactly similar approach as diameter question - Here when we do a function call for subtree - it returns us its height and if its balanced.
       From this info we can find if current node is balanced.
     */
    public boolean isBalanced(TreeNode root) {
        if(root == null) return true;

        return isBalancedDP(root)[0] == 1;

    }

    //int[0] : Is current root balanced , int[1] : Height of current root
    public int[] isBalancedDP(TreeNode root){
        if(root == null) return new int[]{1,-1};

        int[] leftInfo = isBalancedDP(root.left);
        int[] rightInfo = isBalancedDP(root.right);

        int isBalanced = 0;
        // If left subtree is balanced and right subtree is balanced and height difference between left subtree and right subtree is less than 1
        //Our current root is balanced
        if(leftInfo[0] == 1 && rightInfo[0] == 1 && Math.abs(leftInfo[1] - rightInfo[1]) <= 1) isBalanced = 1;

        int currNodeHeight = Math.max(leftInfo[1] , rightInfo[1]) + 1;

        return new int[]{isBalanced, currNodeHeight};
    }


    /* Binary Tree Paths - Given the root of a binary tree, return all root-to-leaf paths in any order.
       https://leetcode.com/problems/binary-tree-paths/

       We need it in form of A->B->C

     */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> answer = new ArrayList<String>();
        if (root != null) binaryTreePaths(root, "", answer);
        return answer;
    }
    private void binaryTreePaths(TreeNode root, String path, List<String> answer) {
        if (root.left == null && root.right == null) answer.add(path + root.val);
        if (root.left != null) binaryTreePaths(root.left, path + root.val + "->", answer);
        if (root.right != null) binaryTreePaths(root.right, path + root.val + "->", answer);
    }

    /* Lowest Common Ancestor of a Binary Tree - Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
       https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/

       Here its guaranteed that elements will be present. But we can solve it for case where it's not present too.
       There are three scenarios for LCA -
       1. One element could be on my left and one can be on my right - I am the LCA then
       2. I am equal to either of elements - because I am sure both elements are present, and I am the one discovered first - I am LCA
       3. I am not LCA, so it's either present in my left or right.
     */

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        //Base condition
        if(root == null) return null;
        //Condition 2
        if(root.val == p.val || root.val == q.val) return root;

        //Calling left and right subtree to return find and return LCA
        TreeNode left =  lowestCommonAncestor(root.left, p, q);
        TreeNode right =  lowestCommonAncestor(root.right, p, q);

        //They both are not null? Condition 1
        if(left != null && right != null) return root;

        //It's either on left or right - return the one not null - Condition 3
        return left != null ? left : right;
    }




}
