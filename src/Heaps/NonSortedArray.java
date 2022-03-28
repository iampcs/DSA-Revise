package Heaps;

/*
Identify a Heap problem :
1. These will mostly be problems which can be solved by sorting - So we will be reducing time-complexity from NlogN to NlogK using heaps.
2. Will contains words such as top,largest,smallest,closest,frequent K elements.

Template:
1. If ask is for smallest elements - choose a maxHeap.
2. If as is for largest elements - choose a minHeap.

Scenario : Suppose we need Kth largest element. We have two choice
    1. Sort the array, return Kth element from last - NlogN
    2. Create a minHeap of size K. Add all elements to this heap, keep poping elements from this heap as soon as size crosses K.
        as it's a minHeap, we know elements getting removed can never be our solution. Finally, when whole array is parsed - top element is our solution.
        Complexity - N * logK(Heapify - adding/removing/updating an element from heap of size k takes logK time)
*/


import java.util.*;

public class NonSortedArray {
    public static void main(String[] args) {

    }

    /*
    Given the array of integers nums, you will choose two different indices i and j of that array.
    Return the maximum value of (nums[i]-1)*(nums[j]-1)
    https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/
     */
    public static int maxProduct(int[] nums) {
        //Using minHeap as we need largest and 2nd largest elements from array
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for(int num: nums){
            pq.add(num);
            if(pq.size() > 2) pq.remove();
        }

        return (pq.remove() -1) * (pq.remove() -1);

    }

    class  pair implements Comparable{
        public Integer soldiers;
        public Integer index;

        pair(Integer soldiers, Integer index){
            this.soldiers = soldiers;
            this.index = index;
        }

        @Override
        public int compareTo(Object o) {
            pair compareTo = (pair) o;
            if(this.soldiers != ((pair) o).soldiers)
                return this.soldiers.compareTo(((pair) o).soldiers);

            return this.index.compareTo(((pair) o).index);
        }
    }

    /*
        Find K weakest rows in a matrix - based on some conditions.
        https://leetcode.com/problems/the-k-weakest-rows-in-a-matrix/
    */
    public int[] kWeakestRows(int[][] mat, int k) {
        int[] solution = new int[k];
        /*We need K weakest rows - maxHeap.
         Created a new class to store key-value pair. In this kind of questions we need to store indexes along with
         some calculated values which will used to sort these elements.  */
        PriorityQueue<pair> pq = new PriorityQueue<>(Collections.reverseOrder());

        for(int row = 0; row < mat.length;row++){
            int soldiers = 0;
            for(int col = 0; col < mat[0].length; col++){
                if(mat[row][col] == 1) soldiers++;
            }
            pq.add(new pair(soldiers,row));
            if(pq.size() > k) pq.remove();
        }

        for (int i=k-1;i>=0;i--){
            solution[i] = pq.remove().index;
        }

        return solution;
    }

    /* Keep smashing two(K) most heaviest stones until none/one is left.
    https://leetcode.com/problems/last-stone-weight/ */
    public int lastStoneWeight(int[] stones) {

        /* We need the heaviest stones among all here, here we need all stones to be present in Heap, and we want the most heaviest one's
        * to be on top - maxHeap - Idea is to keep smashing and inserting back to heap until heap is empty or of size one*/
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for(int num : stones) pq.add(num);

        while (pq.size() >= 2){
            int Y = pq.remove();
            int X = pq.remove();
            if(X != Y) pq.add(Y-X);

        }

        return pq.isEmpty() ? 0 : pq.remove();
    }

    /*
    Given an array of points in a 2D plane, find ‘K’ closest points to the origin.
    https://leetcode.com/problems/k-closest-points-to-origin/
    Solutions -
    1. Sort the array with a custom comparator and return first K points - NlogN
    2. maxHeap of size K - NlogK
    3. BinarySearch - Worst N^2 , Average N

    Create a comparator to sort based on columns - Here two rows are compared based on column.
    sortByColumn(int[][] mat, int col){
        Arrays.sort(mat, (a,b) -> a[col] - b[col]);
    }
     */
    public int[][] kClosest(int[][] points, int k) {
        /* Using a maxHeap to get K closest points to origin.
        Use a lambda comparator to sort the PQ by farthest distance */
        Queue<int[]> maxHeap = new PriorityQueue<>((a, b) -> b[0] - a[0]);
        for (int i = 0; i < points.length; i++) {
            //Using an array to store two values, squared distance and index
            int[] entry = {squaredDistance(points[i]), i};
            if (maxHeap.size() < k) {
                // Fill the maxHeap up to k points
                maxHeap.add(entry);
            } else if (entry[0] < maxHeap.peek()[0]) {
                // If the maxHeap is full and a closer point is found,
                // discard the farthest point and add this one
                maxHeap.poll();
                maxHeap.add(entry);
            }
        }

        // Return all points stored in the maxHeap
        int[][] solution = new int[k][2];
        for (int i = 0; i < k; i++) {
            int entryIndex = maxHeap.poll()[1];
            solution[i] = points[entryIndex];
        }
        return solution;
    }

    private int squaredDistance(int[] point) {
        //X = point[0] , Y = point[1]
        //sqrt(X^2 + Y^2) - We are ignoring sqrt here.
        return point[0] * point[0] + point[1] * point[1];
    }

    /*
    Top K Frequent Elements - https://leetcode.com/problems/top-k-frequent-elements/
    Will use a HashMap to create a frequencyMap - then use a minHeap to return top K frequent elements - N + NlogK
     */
    public static int[] topKFrequent(int[] nums, int k) {
        int[] solution = new int[k];
        //Need top frequent elements - create minHeap that takes array as input
        //Create a comparator - we are storing frequency in index - 1 of array. Need a minHeap so a[1] - b[1] : maxHeap is b[1] - a[1]
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[1] - b[1]);
        //Map for frequency map
        Map<Integer,Integer> map = new HashMap<>();
        for(int num : nums){
            map.put(num, map.getOrDefault(num,0) + 1);
        }
        //Add to minHeap until size crosses K
        for(Map.Entry<Integer,Integer> instance : map.entrySet()){
            minHeap.add(new int[] {instance.getKey(),instance.getValue()});
            if(minHeap.size() > k) minHeap.remove();
        }

        for(int i=0; i<k ;i++){
            solution[i] = minHeap.remove()[0];
        }
        return solution;
    }

    /*
    Frequency Sort - Sort Array by Increasing Frequency - If multiple values have the same frequency, sort them in decreasing order.
    https://leetcode.com/problems/sort-array-by-increasing-frequency/
     */
    class customComparator implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            // We need decreasing order if frequency is same
            if(o1[1] == o2[1]) return o2[0] - o1[0];

            return o1[1] - o2[1];
        }
    }
    public int[] frequencySort(int[] nums) {
        int[] solution = new int[nums.length];
        //Here we don't have K, we want minimum frequency elements to come first - for that they have to be on top, hence minHeap
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(new customComparator());
        // Frequency Map
        Map<Integer,Integer> map = new HashMap<>();
        for(int num : nums) map.put(num, map.getOrDefault(num, 0) + 1);
        //Add frequencyMap to minHeap
        for(Map.Entry<Integer,Integer> instance : map.entrySet()){
            minHeap.add(new int[]{instance.getKey(), instance.getValue()});
        }

        //Create solution array from minHeap
        int solutionIndex = 0;
        while (minHeap.size()> 0){
            int[] res = minHeap.remove();
            int freq = res[1];
            while(freq-- > 0 ){
                solution[solutionIndex++] = res[0];
            }
        }

        return solution;
    }
}
