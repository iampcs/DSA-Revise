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
    3.1. Add input[end] to window
    3.2. Did we reached window size? (end - start + 1) < K - just increment window size(end++) and continue
    3.3. We reached window size (end - start + 1) == K is true -
        3.3.1. Calculate solution for current window
        3.3.2. Increment window size(end++) and continue;
    3.4. We have gone beyond window size now -  (end - start + 1) > K - Slide the window
        3.4.1. Remove input[start] from window - This can be a single operation or a series of operations
        3.4.2. <Optional> Make required modifications with Supporting Data Structure - if any
        3.4.3. <Optional> Check if we got a new solution
        3.4.4. Slide Window -  start++ , end++
4. Return solution

 */

import java.util.ArrayDeque;
import java.util.ArrayList;

public class FixedWindowSize {
    public static void main(String[] args) {


    }

    /*
    Max Sum Subarray of size K - Given an array of integers Arr of size N and a number K.
    Return the maximum sum of a subarray of size K.
    https://practice.geeksforgeeks.org/problems/max-sum-subarray-of-size-k5313
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
                //Increment window size
                end++;
            }
            //We have gone beyond window size now - slide window, make adjustments
            else {
                //Removed start index contribution as we are sliding the window to right
                currentSum = currentSum - Arr.get(start);
                //We might get a new solution as array size is again K
                solution = Math.max(solution,currentSum);

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
                //Increment window size
                end++;
            }
            //We have gone beyond window size now - slide window, make adjustments
            else {
                // Is start part of possible solution? If yes, remove start.
                if(!possibleSolutions.isEmpty() && start == possibleSolutions.getFirst()){
                    possibleSolutions.removeFirst();
                }
                //Slide window
                start++;
                //Now that the window is of size K again, we have a solution
                solution[start] = possibleSolutions.isEmpty() ? 0 : A[possibleSolutions.getFirst()];

                end++;
            }
        }

        return solution;
    }
}
