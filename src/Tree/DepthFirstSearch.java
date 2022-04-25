package Tree;

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

}
