package SlidingWindow;

/*
Identify a Sliding window problem :
1. Array or String based
2. Solution resolves around either sub-array or sub-string - Should be contiguous
3. There will be a fixed window mentioned which contains our solution

Template:

1. Data structure to contain solution + Supporting data structure
2. start and end of window pointers initialized to 0
3. While window has not reached end of input array - end < input.length
    3.1. Add input[end] to window - Do required calculations to supporting data structure
    3.2. Did we reached window size? (end - start + 1) < K - just increment window size(end++) and continue
    3.3. We reached window size (end - start + 1) == K is true -
        3.3.1. Calculate solution for current window
        3.3.2. Remove input[start] from window - This can be a single calculation or a series of calculations to supporting data structure
        3.3.3. Slide Window -  start++ , end++ - maintain window size
4. Return solution

 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FixedWindowSize {
    public static void main(String[] args) {

    }

    /*
    Max Sum Subarray of size K - Given an array of integers Arr of size N and a number K.
    Return the maximum sum of a subarray of size K.
    https://practice.geeksforgeeks.org/problems/max-sum-subarray-of-size-k5313/1
    Hint: Rolling Sum
    */
    static int maximumSumSubarray(int K, ArrayList<Integer> Arr, int N){
        int solution = 0;
        int currentSum = 0; /*Supporting data structure */
        int start = 0,end = 0;

        while(end < N){
            // Add input[end] to window
            currentSum = currentSum + Arr.get(end);

            //Did we reached window size?
            if(end - start + 1 < K){
                //just increment window size
                end++;
            }
            //We reached window size
            else if(end - start + 1 == K){
                //Calculate solution
                solution = Math.max(solution,currentSum);

                //Removed start index contribution as we are sliding the window to right
                currentSum = currentSum - Arr.get(start);

                //Slide window
                start++;
                end++;
            }
        }

        return solution;
    }

    /* First negative integer in every window of size k - Given an array A[] of size N and a positive integer K,
    find the first negative integer for each and every window(contiguous subarray) of size K.
    https://practice.geeksforgeeks.org/problems/first-negative-integer-in-every-window-of-size-k3345/1
    Hint: Maintain Queue of possible solutions
    */
    public long[] printFirstNegativeInteger(long A[], int N, int K)
    {
        long[] solution = new long[N-K+1];
        //We are maintaining a queue to store possible solutions
        // Will be storing indexed instead of numbers itself - makes it easy in case of duplicate numbers
        ArrayDeque<Integer> possibleSolutions = new ArrayDeque<>();
        int start = 0, end = 0;

        while(end < N){

            //Only adding negative numbers to possible solutions
            if(A[end] < 0){
                possibleSolutions.addLast(end);
            }
            //Did we reached window size?
            if(end - start + 1 < K){
                //just increment window size
                end++;
            }
            //We reached window size
            else if(end - start + 1 == K){
                //Calculate solution
                solution[start] = possibleSolutions.isEmpty() ? 0 : A[possibleSolutions.getFirst()];

                // Is start part of possible solution? If yes, remove start.
                if(!possibleSolutions.isEmpty() && start == possibleSolutions.getFirst()){
                    possibleSolutions.removeFirst();
                }

                //Slide window
                start++;
                end++;
            }
        }

        return solution;
    }

    /*
    Count Occurrences of Anagrams - Given a word pat and a text txt.
    Return the count of the occurrences of anagrams of the word in the text.
    https://practice.geeksforgeeks.org/problems/count-occurences-of-anagrams5839/1#
    Hint: Main HashMap to track charater occurrences
    */
    static int countAnagrams(String pat, String txt) {
        int solution = 0;
        int start=0,end = 0;
        //HashMap will store character-frequency in pattern.
        //Idea is to increment/decrement frequency in map as we parse txt and increase window size
        //If all the frequencies are zero means - we got all the characters required.
        Map<Character,Integer> map = new HashMap<>();
        //Faster than String.charAt()
        char[] inputString = txt.toCharArray();
        char[] inputAnagram = pat.toCharArray();

        for(Character ch:inputAnagram){
            if(map.containsKey(ch)) map.put(ch, map.get(ch) + 1);
            else map.put(ch,1);
        }
        //Maintaining this variable saves us time. We know we have an anagram if character-frequency matches with pattern.
        //In that case frequency will be 0 for all the characters we have a anagram. Instead of checking map for all the frequencies
        //we are maintaining this charCount to track number of zero frequencies character
        int charCount  = map.size();


        while(end < inputString.length){

            // We are only concerned when we encounter a character already part of map - part of pattern
            if(map.containsKey(inputString[end])) {
                //We now have one instance of character in our window, so required character is reduced by one
                map.put(inputString[end], map.get(inputString[end]) - 1);
                //If frequency becomes zero - it means now we have all the instances of character required in our window
                //Now we need one character less than before
                if(map.get(inputString[end]) == 0){
                    //We only decrease charCount if character frequency changes from Non-Zero<->0
                    charCount--;
                }

            }
            //Did we reached window size?
            if(end - start + 1 < inputAnagram.length){
                //just increment window size
                end++;
            }
            //We reached window size
            else if(end - start + 1 == inputAnagram.length){
                //Calculate solution
                if(charCount == 0) solution++;

                // Is start part of initial map? If yes, it means we need to replace that character going forward.
                // We need one more instance of that character as start is getting removed now.
                if(map.containsKey(inputString[start])) {
                    map.put(inputString[start], map.get(inputString[start]) + 1);
                    //We only increase charCount if character frequency changes from 0<->Non-Zero
                    if(map.get(inputString[start]) == 1) charCount++;
                }

                //Slide window
                start++;
                end++;
            }
        }
        return solution;
    }

    /*
        Sliding Window Maximum - You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right.
        You can only see the k numbers in the window. Each time the sliding window moves right by one position.
        Return the max sliding window.
        https://leetcode.com/problems/sliding-window-maximum/
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        int[] solution = new int[nums.length -k + 1];
        int start=0,end = 0;
        // Just like printFirstNegativeInteger question, we will be maintaining a special queue which always fetch us
        // maximum number in current window - We need to maintain this queue
        // contains indexes only - not actual numbers
        ArrayDeque<Integer> queue = new ArrayDeque<>();

        while(end < nums.length){
            //Add input[end] to window
            //When we are adding a number and its greater than its elements on left, we don't need those elements anymore
            //Those elements will never be part of solution going forward as we have a greater element present right of them.
            //So remove those elements
            while(!queue.isEmpty() && nums[end] >= nums[queue.getLast()]) {
                queue.removeLast();
            }
            queue.addLast(end);

            //Did we reached window size?
            if(end-start+1 < k){
                //just increment window size
                end++;
            }
            //We reached window size
            else if(end-start+1 == k){
                //Calculate Solution
                solution[start] = nums[queue.getFirst()];

                //If start contains the max number for current window, remove start from queue - now second-largest number
                // will be the max for current queue
                if(start == queue.getFirst()) queue.removeFirst();

                //Slide Window
                start++;
                end++;
            }
        }
        return solution;
    }
}
