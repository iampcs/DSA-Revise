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
    /* Binary Tree Maximum Path Sum - A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.
       The path sum of a path is the sum of the node's values in the path.
       Given the root of a binary tree, return the maximum path sum of any non-empty path.
       https://leetcode.com/problems/binary-tree-maximum-path-sum/

       Logic is similar to diameter of tree, each node will pass to its ancestor - the longest chain it is part of - either left or right
       that node will also update maximum Binary Tree path sum if it has a greater than existing one.
       We could have had an array of size two with first element tracking the longest chain and second tracking maximum Binary Tree path sum.
       But there were complications with negative numbers and solution was getting complex. So storing maxPathSum in a global variable which each node can
       update.

       Note - When I say max sum chain - means it's a straight chain, it doesn't go from left to right - Chain at current root - Max(leftChain, rightChain) + root.value
              When I say max sum path - means it's an upper semi eclipse, it goes from left to right and includes current root - leftChain + rightChain + root.value

     */
    public static int maxSumPath;
    public int maxPathSum(TreeNode root) {
        //Storing the root as maxPathSum - this will be the case if tree has only node.
        maxSumPath = root.val;
        maxPathSumDFS(root);
        return maxSumPath;
    }

    //int[0] : contains max sum path of left subtree , int[1] : contains sum of maximum path
    public int maxPathSumDFS(TreeNode root){
        //Base condition - null root doesn't contribute to sum
        if(root == null) return 0;

        //Get max sum chain from left and right
        int leftChain = maxPathSumDFS(root.left);
        int rightChain = maxPathSumDFS(root.right);

        //We don't need the chain if its returning negative sum - we already have root with us, this chain won't help
        leftChain = Math.max(leftChain, 0);
        rightChain = Math.max(rightChain, 0);

        //Do we have a new max sum path ?
        maxSumPath = Math.max(maxSumPath, leftChain + rightChain + root.val);

        // Return max chain from this current root
        return root.val + Math.max(leftChain,rightChain);

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

    //p & q may be present - will return null in case either or both of them missing
    //Static member to store solution
    static TreeNode LCA = null;
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        lowestCommonAncestorDFS(root,p,q);

        return LCA;
    }

    public boolean lowestCommonAncestorDFS(TreeNode root, TreeNode p, TreeNode q) {
        //Base condition
        if(root == null) return false;

        //Idea is similar to previous LCA - we are checking for condition 1,2 or 3 and returning to our parent if either is true.
        boolean self = root.val == p.val || root.val == q.val;
        boolean left = lowestCommonAncestorDFS(root.left, p, q);
        boolean right = lowestCommonAncestorDFS(root.right, p, q);

        //If any of three condition is true - We have our LCA
        if((self && left) || (self && right) || (left && right)) LCA = root;

        return self || left || right;
    }

    /* Path Sum III - Given the root of a binary tree and an integer targetSum, return the number of paths where the sum of the values along the path equals targetSum.
       The path does not need to start or end at the root or a leaf, but it must go downwards (i.e., traveling only from parent nodes to child nodes).
       https://leetcode.com/problems/path-sum-iii/
       This is a basic recursive problem with choices - We have two choices
       1. Include current node in our solution path - We need to pass targetSum - root.val to subtrees
       2. Exclude current node from solution path - In this case we need to pass original targetSum provided to subtrees
     */
    public int pathSumIII(TreeNode root, int sum) {
        if (root == null) return 0;
        //Total of : Include current node + Exclude current node
        //We need to preserve sum in case we are not including current root, hence a separate method where we are modifying sum
        return pathSumFrom(root, sum) + pathSumIII(root.left, sum) + pathSumIII(root.right, sum);
    }

    private int pathSumFrom(TreeNode node, int sum) {
        if (node == null) return 0;
        //Tree contains negative values - So even if we found a path - its possible going forward we could discover more such paths
        return (node.val == sum ? 1 : 0)
                + pathSumFrom(node.left, sum - node.val) + pathSumFrom(node.right, sum - node.val);
    }

    /* Path Sum II - Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the sum of the node values in the path equals targetSum.
       Each path should be returned as a list of the node values, not node references.
       https://leetcode.com/problems/path-sum-ii/

       We will be using an arrayList to track path, if target sum is zero, we will add a copy to our solution. When we have processed both left and right
       subtree, we will backtrack.
       We can remove this backtracking at cost of space if we pass a copy of path to subtrees.

     */

    public List<List<Integer>> pathSumII(TreeNode root, int targetSum) {
        List<List<Integer>> solution = new ArrayList<>();
        if(root == null) return solution;

        pathSumII(root,targetSum,solution, new ArrayList<Integer>());
        return solution;

    }

    private void pathSumII(TreeNode root, int targetSum, List<List<Integer>> solution, ArrayList<Integer> currPath) {
        if(root == null) return;

        //We are at leaf and targetSum is zero - Add leaf to path, add this path to solution, remove leaf from path
        if(root.left == null && root.right == null & root.val == targetSum){
            currPath.add(root.val);
            solution.add(new ArrayList(currPath));
            //Backtrack
            currPath.remove(currPath.size() - 1);
            return;
        }

        //Add current root to path
        currPath.add(root.val);

        pathSumII(root.left, targetSum - root.val, solution, currPath);
        pathSumII(root.right,targetSum - root.val, solution, currPath);

        //Backtrack
        currPath.remove(currPath.size() - 1);
    }

    /* Sum Root to Leaf Numbers - You are given the root of a binary tree containing digits from 0 to 9 only.Each root-to-leaf path in the tree represents a number.
       For example, the root-to-leaf path 1 -> 2 -> 3 represents the number 123.
       Return the total sum of all root-to-leaf numbers. Test cases are generated so that the answer will fit in a 32-bit integer.
       https://leetcode.com/problems/sum-root-to-leaf-numbers/
       Logic is simple - We pass current node value to left child and right child : If path 1->2 - We will pass 12 to both the child
       When we encounter a leaf - will add this path to sum.

     */
    int sumNumbers = 0;
    public int sumNumbers(TreeNode root) {
        sumNumbersDFS(root, 0);
        return sumNumbers;
    }

    private void sumNumbersDFS(TreeNode root, int val) {
        if(root == null) return;
        //Leaf node - add current path to sum
        if(root.left == null && root.right == null){
            //If we get 12 from parent, and current node is 3, number will be 123 = 12 * 10 + 3
            sumNumbers += val * 10 + root.val;
        }

        //Pass current path number to subtrees
        sumNumbersDFS(root.left, val * 10 + root.val);
        sumNumbersDFS(root.right, val * 10 + root.val);
    }

    /* Validate Binary Search Tree - Given the root of a binary tree, determine if it is a valid binary search tree (BST).
       A valid BST is defined as follows:
       * The left subtree of a node contains only nodes with keys less than the node's key.
       * The right subtree of a node contains only nodes with keys greater than the node's key.
       * Both the left and right subtrees must also be binary search trees.
       https://leetcode.com/problems/validate-binary-search-tree/

       Every node will define boundaries for its subtrees - If any of those boundaries are broken - It's not a BST .
       We give minValue and maxValue - root.val is implicit
       minValue<---LeftSub tree--->root.val<---LeftSub tree--->maxValue
     */
    public boolean isValidBST(TreeNode root) {
        //Single node is a BST
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean isValidBST(TreeNode root, long minVal, long maxVal) {
        if (root == null) return true;

        //Going outside/on boundary
        if (root.val >= maxVal || root.val <= minVal) return false;
        //Call subtrees to check if they are BST based on new minValue & maxValue
        return isValidBST(root.left, minVal, root.val) && isValidBST(root.right, root.val, maxVal);
    }

    /* Construct Binary Tree from Preorder and Inorder Traversal - Given two integer arrays preorder and inorder where preorder is the preorder traversal of a binary tree and
       inorder is the inorder traversal of the same tree, construct and return the binary tree.
       https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/

       Conditions for constructing a binary tree from traversals
       1. One of the traversals should be in-order, else it's not possible to divide array into subtrees size
       2. Traversals must consist of unique values.

       How to build tree using inorder & preorder(postorder,levelOrder)
       1. First element of preorder is root - create root node
       2. Find that element in inorder - inorderRootIndex : this index divides our inorder into left subtree and right subtree <--left-->inorderRootIndex<--right-->
       3. We now know the inorder traversal for left & right subtree, also their sizes - L & R
       4. For preorder - roots are stored first then left subtree and then right subtree - So if there is a left subtree, its index will be on next of root index - 1st index - also it will be
          L length long. Left Subtree - (1, 1 + L - 1), Right subtree will start from (1 + L, end)
       5. Recursively do the same for root.left and root.right

     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {

        return buildTree(preorder, 0,preorder.length -1, inorder, 0, inorder.length-1);

    }

    private TreeNode buildTree(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd) {
        //Base condition
        if(preEnd < preStart || inEnd < inStart) return null;
        TreeNode root = new TreeNode(preorder[preStart]);

        //We know root will fall between  inStart and inEnd only
        int inorderRootIndex = inStart;
        for(;inorderRootIndex < inEnd; inorderRootIndex++){
            if(inorder[inorderRootIndex] == root.val) break;
        }

        int leftSubTreeSize = inorderRootIndex - inStart;

        //Call for left subtree and right subtree with new preorder and postorder traversals
        root.left = buildTree(preorder, preStart + 1, preStart + leftSubTreeSize, inorder, inStart, inorderRootIndex - 1);
        root.right = buildTree(preorder, preStart + leftSubTreeSize + 1, preEnd, inorder, inorderRootIndex + 1, inEnd);

        return root;
    }


}
