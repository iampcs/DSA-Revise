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
}
