package Tree;

import java.util.ArrayList;
import java.util.Arrays;

/* Properties of BST
    1. The left subtree of a node contains only nodes with keys lesser than the node’s key.
    2. The right subtree of a node contains only nodes with keys greater than the node’s key.
    3. The left and right subtree each must also be a binary search tree.

Basic operations differ here from binary trees like
    1. Find min/max takes on average logN time instead of O(N)
    2. Insertion takes logN on average
    3. Deletion takes logN
    4. Inorder traversal gives a sorted list.
    5. We can create a BST with only preOrder, postOrder, levelOrder traversal. We can retrieve inOrder from sorting any given order.

Inorder predecessor will the element in tree just smaller than current node - this could be right most node
of left subtree if exist or the first left ancestor. Similarly, inorder successor will be element in tree just greater than current node - this will be
either left most element in right subtree or first right ancestor in case subtree don't exist.


 */
public class BinarySearchTree {


    public static void main(String[] args) {

    }
    // We can search based on value of target, as with each comparison we are removing half of input space - Time Complexity - logN
    boolean search(Node root, int x) {
        if(root == null ) return false;
        if(root.data == x) return true;

        return root.data > x ? search(root.left,x) : search(root.right,x);
    }

    //Finding minimum/maximum - minimum element will be th left most leaf, maximum will be the right most
    int minValue(Node node) {
        if(node == null) return -1;
        if(node.left == null) return node.data;
        return minValue(node.left);
    }

    /* Inserting an element in BST - Find for location of that element if it were present in tree - If it does - ignore insertion(this can change based on requirements)
       else create a new node and insert it to the place it should have been - new nodes are always inserted as a leaf node.
     */
    Node insert(Node root, int key){
        //First element
        if(root == null){
            root = new Node(key);
            return root;
        }

        //Insert to left or right based on key value
        if(key < root.data) root.left = insert(root.left, key);
        else if(key > root.data) root.right = insert(root.right, key);

        return root;
    }
    /* Predecessor And Successor In BST - Given a root and key, return its predecessor and successor in BST, return null if not present.
       Ways we can achieve this -
       1. Find inorder traversal of tree - keep track of the node that come just before and after key - O(N)
       2. Assuming BST is balanced - we can reduce this to O(logN) - How?
          For predecessor - Lets compare key with root - if its smaller or equal - means our predecessor might lie on left subtree - same logic as finding an element in BST
                            The moment we find a number smaller than key - that could be our possible predecessor, we then keep going right to find a number smaller than key but larger
                            that current predecessor.
          For successor - Lets compare key with root - if it's greater than or equal - means our successor might lie on right subtree - The moment we find a root greater than key - that's our current
                          successor -  we then keep going left to find a number greater than key but smaller that current predecessor.
     */
    public static ArrayList<Integer> predecessorSuccessor(TreeNode root, int key) {
        TreeNode predecessor = null;
        TreeNode successor = null;
        TreeNode rootP = root;
        TreeNode rootS = root;
        ArrayList<Integer> solution = new ArrayList<>();

        while(rootP != null){
            if(key <= rootP.val) rootP = rootP.left;
            else {
                predecessor = rootP;
                rootP = rootP.right;
            }
        }

        while(rootS != null){
            if(key >= rootS.val) rootS = rootS.right;
            else{
                successor = rootS;
                rootS = rootS.left;
            }
        }

        solution.add(predecessor == null ? -1 : predecessor.val);
        solution.add(successor == null ? -1 : successor.val);

        return solution;
    }  

    /* Deleting an Element from BST - This is a bit tricky
    https://practice.geeksforgeeks.org/problems/delete-a-node-from-bst/1/
       How to delete X is handled as following :
        1. If X is leaf - Simple Remove - Can make its parent refer to null instead of X - Garbage collector will take care of rest
        2. If X has one child - Replace/Copy X with child - delete child
        3. If X has two children - Find inorder successor/predecessor of the X. Copy/Replace contents of the inorder successor/predecessor to the node and
           delete the inorder successor/predecessor.
     */
    public static TreeNode deleteNode(TreeNode root, int X) {
        //Base Case
        if(root == null) return null;

        //Find X in left subtree and right subtree
       if(X > root.val) root.right = deleteNode(root.right,X);
       else if (X < root.val) root.left = deleteNode(root.left, X);
       //Found X
       else {
           // X has two children
           if(root.left != null && root.right != null){
               //To get predecessor and successor, we can use either of value - using predecessor here.
               ArrayList<Integer> predecessorSuccessor = predecessorSuccessor(root,X);
               //Swapping root with predecessor
               root.val = predecessorSuccessor.get(0);
               //Deleting predecessor - predecessor will be present on left subtree
               root.left = deleteNode(root.left, predecessorSuccessor.get(0));
           }
           // X has one or zero child
           else {
               //If left is null, either right will have value or be null - in either case replace root with its right child and return
               if(root.left == null) root = root.right;
               //If left is not null - right will be null
               else root = root.left;
           }
       }

       return root;
    }

    /* Convert Sorted Array to Binary Search Tree - Given an integer array nums where the elements are sorted in ascending order, convert it to a height-balanced binary search tree.
       A height-balanced binary tree is a binary tree in which the depth of the two subtrees of every node never differs by more than one.
       https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/

       We know that once we choose an element to be root, all smaller than root will go on left, all greater will go on right. For a balanced BST
       we can choose mid-element as root, we will do this recursively for left and right subtree.
     */
    public TreeNode sortedArrayToBST(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length - 1);
    }

    public TreeNode sortedArrayToBST(int[] nums, int start, int end ){
        if(start > end) return null;

        // Setting mid element as root
        int mid = (start + end )/2;
        TreeNode root = new TreeNode(nums[mid]);

        //Recursion the same for left and right subtree
        root.left = sortedArrayToBST(nums,start, mid-1);
        root.right = sortedArrayToBST(nums, mid + 1, end);

        return root;

    }
    /* Validate Binary Search Tree - Given the root of a binary tree, determine if it is a valid binary search tree (BST).
       https://leetcode.com/problems/validate-binary-search-tree/
       Each node will set a minimum and maximum value elements in its subtree can be. If this condition is broken - we return false.
       A root can be of any value. It's left subtree can be of max value less than root & its right subtree can have min value as root.
     */
    public boolean isValidBST(TreeNode root) {
        //Root value is of Type Integer - To avoid overflow - using Long as boundaries
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean isValidBST(TreeNode root, long minVal, long maxVal) {
        // A null tree is a BST
        if (root == null) return true;
        if (root.val >= maxVal || root.val <= minVal) return false;
        return isValidBST(root.left, minVal, root.val) && isValidBST(root.right, root.val, maxVal);
    }

    /* Lowest Common Ancestor of a Binary Search Tree
       https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
       This will be similar to Binary tree LCA with just an added condition, given both the elements P,Q are present in BST, if we find an element
       which lies between P and Q. We have our LCA as they will lie on opposite site of this node.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null) return null;
        if(root.val == p.val || root.val == q.val) return root;
        //Additional Condition
        if(root.val > Math.min(p.val,q.val) && root.val < Math.max(p.val,q.val)) return root;

        //If our node is greater than P,Q means, PQ will be on left subtree so will be the LCA
        if(root.val > Math.max(p.val,q.val)) return lowestCommonAncestor(root.left, p, q);
        //LCA on right subtree
        else return lowestCommonAncestor(root.right, p, q);
    }






    class Node{
        int data;
        Node left;
        Node right;
        Node(int data){
            this.data = data;
            left = right = null;
        }
    }

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
}
