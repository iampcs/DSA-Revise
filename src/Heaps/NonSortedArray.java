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
    Input: nums = [3,4,5,2]
    Output: 12
    Explanation: If you choose the indices i=1 and j=2 (indexed from 0), you will get the maximum value, that is, (nums[1]-1)*(nums[2]-1) = (4-1)*(5-1) = 3*4 = 12.
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
    Top K Frequent Elements - Given an integer array nums and an integer k, return the k most frequent elements. You may return the answer in any order.
    https://leetcode.com/problems/top-k-frequent-elements/
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
    /*
        Least Number of Unique Integers after K Removals
        Given an array of integers arr and an integer k. Find the least number of unique integers after removing exactly k elements.
        Input: arr = [5,5,4], k = 1
        Output: 1
        Explanation: Remove the single 4, only 5 is left.
        https://leetcode.com/problems/least-number-of-unique-integers-after-k-removals/
    */
    public static int findLeastNumOfUniqueInts(int[] arr, int k) {

        // Frequency Map
        Map<Integer,Integer> map = new HashMap<>();
        for(int num : arr) map.put(num, map.getOrDefault(num, 0) + 1);

        //We want the one with the least frequency to be on top, so we can remove those elements first
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a,b) -> a[1] - b[1]);

        // Add frequency map tp minHeap
        for(Map.Entry<Integer,Integer> entry : map.entrySet()) minHeap.add(new int[]{entry.getKey(), entry.getValue()});

        //Remove the elements with the least frequencies first until k > 0. Add it back to heap if K is exhausted.
        while (k > 0){
            int[] popped = minHeap.poll();
            if (k < popped[1]) {
                minHeap.add(new int[]{popped[0], popped[1] - k});
            }
            k = k - popped[1];
        }

        return minHeap.size();

    }

    /*
    Reorganize String - Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
    https://leetcode.com/problems/reorganize-string/
    */
    public static String reorganizeString(String s) {

        StringBuilder solution = new StringBuilder();
        // Frequency Map
        Map<Character,Integer> map = new HashMap<>();
        for (Character ch : s.toCharArray()) map.put(ch, map.getOrDefault(ch,0) + 1);
        //We will use the character with most frequency alternatively to create a solution string - character with max frequency will be on top
        PriorityQueue<Map.Entry<Character,Integer>> maxHeap = new PriorityQueue<>((a,b) -> b.getValue() - a.getValue());
        for(Map.Entry<Character,Integer> entry : map.entrySet()) maxHeap.add(entry);

        //In each step we append one occurrence of the highest frequency character to the output string
        //We will not put this character back in the heap to ensure that no two same characters are adjacent to each other.
        Map.Entry<Character,Integer> prevEntry = null;
        while (maxHeap.size() > 0){
            // Get current max frequency character
            Map.Entry<Character,Integer> currEntry = maxHeap.poll();
            //Add previous max frequency character if applicable
            if(prevEntry != null && prevEntry.getValue() > 0) maxHeap.add(prevEntry);

            solution.append(currEntry.getKey());
            //Reduce frequency by 1, as we have used it in our solution
            currEntry.setValue(currEntry.getValue() - 1);
            //Store this character to be added back to maxHeap on next iteration
            prevEntry = currEntry;
        }
        return solution.length() == s.length() ? solution.toString() : "";
    }

    /*
    Rearrange String K Distance Apart - Given a string and a number ‘K’, find if the string can be rearranged such that the same characters are at least ‘K’ distance apart from each other.
    https://www.lintcode.com/problem/907/
    This problem is similar to reorganizeString, only difference is that in the reorganizeString the same characters need not be adjacent i.e.,
    they should be at least ‘2’ distance apart while in the current problem, the same characters should be ‘K’ distance apart.
    We will follow similar approach, we will re-insert the character after ‘K’ iterations.
    */
    public static String rearrangeString(String s, int k) {
        StringBuilder solution = new StringBuilder();
        // Frequency Map
        Map<Character,Integer> map = new HashMap<>();
        for (Character ch : s.toCharArray()) map.put(ch, map.getOrDefault(ch,0) + 1);
        //We will use the character with most frequency every K iteration to create a solution string - character with max frequency will be on top
        PriorityQueue<Map.Entry<Character,Integer>> maxHeap = new PriorityQueue<>((a,b) -> b.getValue() - a.getValue());
        for(Map.Entry<Character,Integer> entry : map.entrySet()) maxHeap.add(entry);

        //In each step we append one occurrence of the highest frequency character to the output string
        //We will not put this character back in the heap to ensure that no two same characters are adjacent to each other.
        Queue<Map.Entry<Character,Integer>> prevEntires = new LinkedList<>();
        while (maxHeap.size() > 0){
            // Get current max frequency character
            Map.Entry<Character,Integer> currEntry = maxHeap.poll();

            solution.append(currEntry.getKey());
            //Reduce frequency by 1, as we have used it in our solution
            currEntry.setValue(currEntry.getValue() - 1);

            //Add previous max frequency characters to queue
            prevEntires.add(currEntry);
            //Now that we have K entries in queue, we can use the same character again, adding it back to heap
            //If frequency is greater than 0
            if(prevEntires.size() == k){
                Map.Entry<Character,Integer> prevEntry = prevEntires.poll();
                if(prevEntry.getValue() > 0) maxHeap.add(prevEntry);
            }
        }
        return solution.length() == s.length() ? solution.toString() : "";
    }
    /*
    Task Scheduler
    Given a characters array tasks, representing the tasks a CPU needs to do, where each letter represents a different task. Tasks could be done in any order.
    Each task is done in one unit of time. For each unit of time, the CPU could complete either one task or just be idle.
    However, there is a non-negative integer n that represents the cooldown period between two same tasks (the same letter in the array), that is that there must be at
    least n units of time between any two same tasks.
    Return the least number of units of times that the CPU will take to finish all the given tasks.
    Input: tasks = ["A","A","A","B","B","B"], k = 2
    Output: 8
    Explanation: A -> B -> idle -> A -> B -> idle -> A -> B
    There is at least 2 units of time between any two same tasks.
    https://leetcode.com/problems/task-scheduler/
    This problem is similar to rearrangeString. We need to rearrange tasks such that same tasks are ‘k’ distance apart.
    We will use a maxHeap to execute the highest frequency task first. After executing a task we decrease its frequency and put it in a
    waiting list. In each iteration, we will try to execute as many as k+1 tasks.
    For the next iteration, we will put all the waiting tasks back in the maxHeap.
    If, for any iteration, we are not able to execute k+1 tasks, the CPU has to remain idle for the remaining time in the next iteration.
    */
    public int leastInterval(char[] tasks, int K) {
        int solution = 0;
        // Frequency Map
        Map<Character,Integer> map = new HashMap<>();
        for (Character ch : tasks) map.put(ch, map.getOrDefault(ch,0) + 1);
        //We will use the character with most frequency every K iteration to create a solution - character with max frequency will be on top
        PriorityQueue<Map.Entry<Character,Integer>> maxHeap = new PriorityQueue<>((a,b) -> b.getValue() - a.getValue());
        for(Map.Entry<Character,Integer> entry : map.entrySet()) maxHeap.add(entry);


        while (maxHeap.size() > 0){
            List<Map.Entry<Character,Integer>> coolDownList = new ArrayList<>();
            //Including the character itself, next time it will come after K+1 iterations.
            //[A,B,A,C], K=2 -> [A,B,C,A]
            int n = K + 1;

            for(;n>0 && !maxHeap.isEmpty(); n--){
                // Get current max frequency character
                Map.Entry<Character,Integer> currEntry = maxHeap.poll();
                //Add it to solution
                solution++;
                //Only if frequency is left post adding we are interested in it
                if(currEntry.getValue() > 1){
                    //Reduce frequency by 1, as we have used it in our solution
                    currEntry.setValue(currEntry.getValue() - 1);
                    //Add it to coolDown list
                    coolDownList.add(currEntry);
                }
            }
            // Add all coolDown characters back to maxHeap as they are eligible to add back to schedule
            maxHeap.addAll(coolDownList);
            // We could not add fill K+1 places from maxHeap, we will be having leftover 'n' idle intervals for next iterations
            // We don't have anything left in coolDownList means we have successfully scheduled all tasks - maxHeap will be empty in that case
            // No need to add 'n' - idle states after that
            if(!maxHeap.isEmpty()) solution += n;
        }
        return solution;
    }
    /*
    Maximum Frequency Stack - Design a stack-like data structure to push elements to the stack and pop the most frequent element from the stack.
    https://leetcode.com/problems/maximum-frequency-stack/
    We need the element with max frequency, so we will use a maxHeap to store these values. maxHeap will be sorting based on frequency,
    and we want the element pushed last to stack, so we will need another variable sequence to store insertion sequence.
    We will need a map to track current character and its frequency in stack.
    */
    static class FreqStack {


        private Map<Integer,Integer> map;
        private PriorityQueue<int[]> maxHeap;
        private int sequence;

        public FreqStack() {
            sequence = 0;
            map = new HashMap<>();
            //maxHeap will first sort based on frequency, if they are equal, sort on sequence
            maxHeap = new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                // int[0] = val, int[1] = frequency, int[2] = sequence
                public int compare(int[] o1, int[] o2) {
                    if(o1[1] == o2[1]) return o2[2] - o1[2];
                    return o2[1] - o1[1];
                }
            });
        }

        public void push(int val) {
            //Frequency Map
            map.put(val, map.getOrDefault(val, 0) + 1);
            //Adding to Heap - Value, current frequency, sequence
            maxHeap.add(new int[]{val,map.get(val),sequence++});
        }

        public int pop() {
            int[] toReturnElement = maxHeap.poll();
            //Decrease frequency of character
            map.put(toReturnElement[0], map.get(toReturnElement[0]) - 1);
            return toReturnElement[0];
        }
    }
}
