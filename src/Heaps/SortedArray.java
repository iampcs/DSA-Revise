package Heaps;

import java.util.*;

/*
Given array here will be sorted, nearly sorted or K-Sorted. We will be using heap here to minimize time complexity.

*/
public class SortedArray {
    public static void main(String[] args) {

    }

    /* Sort a K-Sorted/Nearly sorted Array
    https://www.geeksforgeeks.org/nearly-sorted-algorithm/
     */
    public static void kSort(int[] arr, int n, int k)
    {
        // As a number can be atmost K away from its original position, we will create a minHeap of sizeK
        // Will keep adding elements to it minHeap and poll when size crosses K.
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int index = 0;


        for (int i = 0; i < arr.length; i++) {
            minHeap.add(arr[i]);
            if(minHeap.size() > k)
                arr[index++] = minHeap.poll();
        }
        while (minHeap.size() > 0) {
            arr[index++] = minHeap.poll();
        }
    }

    /* K Closest Elements : Given a sorted integer array arr, two integers k and x,
    return the k closest integers to x in the array. The result should also be sorted in ascending order.
    https://leetcode.com/problems/find-k-closest-elements/
    As the final result needs to be sorted and this array is already sorted, a better approach will be to use Binary-Search to find
    X or number closed to X, and from here use two pointers to fetch the K size window - This can be done in logN + K
     */
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        // We are sorting based on distance from X. We need the closest one's so maxHeap.
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            //Compare based on absolute distance from X, if they are same smallest will get preference.
            public int compare(int[] o1, int[] o2) {
                if(o1[0] == o2[0]) return o2[1] - o1[1];
                return o2[0] - o1[0];
            }
        });

        ArrayList<Integer> solution = new ArrayList<>();
        for(int i =0; i< arr.length;i++){
            maxHeap.add(new int[]{Math.abs(arr[i]-x), arr[i]});
            if(maxHeap.size() > k) maxHeap.poll();
        }
        while (maxHeap.size() > 0){
            solution.add(maxHeap.poll()[1]);
        }

        Collections.sort(solution);
        return solution;
    }

    /*
      Merge k Sorted Lists - Given an array of k linked-lists lists, each linked-list is sorted in ascending order.
      Merge all the linked-lists into one sorted linked-list and return it.
      https://leetcode.com/problems/merge-k-sorted-lists/
      We can use minHeap to solve this -
      1. Insert the first element of each array in a Min Heap.
      2. Remove the smallest (top) element from the heap and add it to the merged list.
      3. Insert the next element of the same list into the heap - Removed node will already contain its next node address.
     */

    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }
    public static ListNode mergeKLists(ListNode[] lists) {
        ListNode solutionHead =null, solutionTail = null;
        //minHeap to store ListNode - It's effective to store node data itself as it contains next node address
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a,b) -> a.val - b.val);

        //Add root of each list , if not null
        for(ListNode root : lists){
            if(root != null) {
                minHeap.add(root);
            }
        }

        while (!minHeap.isEmpty()){
            //Remove minHeap Top
            ListNode currNode = minHeap.poll();

            // First element in merged list
            if(solutionHead == null){
                solutionHead = solutionTail = currNode;
            }
            else {
                solutionTail.next = currNode;
                solutionTail = currNode;
            }
            //Insert the next element of the same list into the minHeap.
            if(currNode.next != null) minHeap.add(currNode.next);
        }

        return solutionHead;
    }

    /*
    Kth Smallest Number in M Sorted Lists - Given ‘M’ sorted arrays, find the K’th smallest number among all the arrays.
    https://www.geeksforgeeks.org/find-m-th-smallest-value-in-k-sorted-arrays/
    We can solve this using a minHeap : Just like mergeKLists, we will merge all sorted arrays until we have removed
    K elements from heap. Idea of using a minHeap instead of maxHeap here is that even before adding all elements we can get our solution,
    if it were list of non-sorted arrays, maxHeap would have made sense.
    But here the input is a list of arrays compared to LinkedLists.
    This means that when we want to push the next number in the heap we need to know what the index of the current number in the current array was.
    To handle this, we will need to keep track of the array and the element indices.
     */
    public static int findKthSmallest(List<Integer[]> lists, int K) {
        //minHeap with element as :
        //int[0] = number , int[1] = elementIndex, int[2] = arrayIndex
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        //Adding first element from each list
        for (int list = 0; list < lists.size(); list++) {
            if (lists.get(list) != null) {
                minHeap.add(new int[]{lists.get(list)[0], 0, list});
            }
        }

        int numCount = 0, solution = 0;
        while (!minHeap.isEmpty()){
            //Poll top element & assigned it to solution
            int[] currNum = minHeap.poll();
            solution = currNum[0];
            //Have we already popped K elements ? If yes, this solution is our Kth smallest element, break from this loop
            if(++numCount == K) break;

            //Increment index of current array from lists
            currNum[1] += 1;
            //If current index is less than length of that array, add next element in that array
            if(lists.get(currNum[2]).length > currNum[1]) {
                minHeap.add(new int[]{lists.get(currNum[2])[currNum[1]], currNum[1], currNum[2]});
            }
        }

        return solution;
    }




    /*
    Kth Smallest Element in a Sorted Matrix - Given an n x n matrix where each of the rows and columns is sorted in ascending order,
    return the kth smallest element in the matrix.
    https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
    Heap solution will be exactly same as findKthSmallest problem, just instead of list of arrays, we got a matrix.
    Here as the rows and columns are sorted, we can use a Binary-Search solution too which is more efficient,
    will be discussing that in Binary Search patterns
     */
    public static int kthSmallestMatrix(int[][] matrix, int K) {
        //minHeap with element as :
        //int[0] = number , int[1] = row, int[2] = column
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        //Adding first element from each list
        //We are adding row < K, as rows are sorted and if K is less than number of rows, our solution will never lie on rows greater than K
        for (int row = 0; row < matrix.length && row < K; row++) {
            minHeap.add(new int[]{matrix[row][0], row, 0});
        }

        int numCount = 0, solution = 0;
        while (!minHeap.isEmpty()){
            //Poll top element & assigned it to solution
            int[] currNum = minHeap.poll();
            solution = currNum[0];
            //Have we already popped K elements ? If yes, this solution is our Kth smallest element, break from this loop
            if(++numCount == K) break;

            //Increment index of current array from lists
            currNum[2] += 1;
            //If current index is less than length of that array, add next element in that array
            if(matrix[currNum[1]].length > currNum[2]) {
                minHeap.add(new int[]{matrix[currNum[1]][currNum[2]], currNum[1], currNum[2]});
            }
        }

        return solution;
    }

    /*
    Smallest Range Covering Elements from K Lists - You have k lists of sorted integers in non-decreasing order.
    Find the smallest range that includes at least one number from each of the k lists.
    https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
     */

    /*
    Find K Pairs with Smallest Sums - Given two sorted arrays in ascending order ,
    find ‘K’ pairs with the smallest sum where each pair consists of numbers from both the arrays.
    https://leetcode.com/problems/find-k-pairs-with-smallest-sums/
    */
}
