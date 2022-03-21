package SlidingWindow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
Identify a Sliding window problem :
1. Array or String based
2. Solution resolves around either sub-array or sub-string - Should be contiguous
3. There will be a condition mentioned - K , based on which we vary our window that contains satisfies the condition

Template:

1. Data structure to contain solution + Supporting data structure to quantify condition
2. start and end of window pointers initialized to 0
3. While window has not reached end of input array - end < input.length
    3.1. Add input[end] to window - Do required calculations to supporting data structure
    3.2. Did we reached condition? present-condition < K (conditioned mentioned) - just increment window size(end++) and continue
    3.3. We reached condition - present-condition == K is true - increment window size(end++)
        3.3.1. Calculate solution for current window
        3.3.2. Increment window size(end++) and continue;
    3.4. We have gone beyond condition now -  present-condition > K - Slide the window
        3.4.1. While condition > K
            3.4.1.1. Remove input[start] from window - This can be a single operation or a series of operations
            3.4.1.2. Slide window front - start++;
        3.4.2. Slide Window rear - end++
4. Return solution

 */
public class VariableWindowSize {
    public static void main(String[] args) {

    }

    /*
       Longest K unique characters substring - Given a string you need to print the size of the longest possible substring that has exactly K unique characters.
       If there is no possible substring then print -1.
       https://practice.geeksforgeeks.org/problems/longest-k-unique-characters-substring0853/1#
    */
    public static int longestKSubstr(String s, int k) {

        int solution = -1;
        int start=0,end=0;
        //We will use this map quantify our condition - our condition here is number of unique characters
        Map<Character,Integer> uniqueCharacterMap = new HashMap<>();
        //To store number of unique character in current window - this saves us time to iterate map
        int uniqueCharacterCount = 0;


        while(end < s.length()){
            //Add input[end] to window
            if(uniqueCharacterMap.containsKey(s.charAt(end))) {
                //We are not removing any key from map, its possible this key was part of old window size
                // If we adding a character not already present in map - its unique - increment uniqueCharacterCount
                if(uniqueCharacterMap.get(s.charAt(end)) == 0) uniqueCharacterCount++;
                uniqueCharacterMap.put(s.charAt(end), uniqueCharacterMap.get(s.charAt(end)) + 1);
            }
            else {
                //If we adding a character not already present in map - its unique - increment uniqueCharacterCount
                uniqueCharacterMap.put(s.charAt(end),1);
                uniqueCharacterCount++;
            }

            //Did we reached condition? present-condition < K (conditioned mentioned) - just increment window size(end++)
            if(uniqueCharacterCount < k){
                end++;
            }
            //We reached condition, present-condition == K is true - increment window size
            else if(uniqueCharacterCount == k){
                //Calculate solution for current window
                solution = Math.max(solution,end-start+1);
                //Increment window size
                end++;
            }
            //We have gone beyond condition now -  present-condition > K
            else{
                //while condition is not equal or less than it
                while(uniqueCharacterCount > k){
                    //Remove input[start] from window
                    uniqueCharacterMap.put(s.charAt(start), uniqueCharacterMap.get(s.charAt(start)) - 1);
                    if(uniqueCharacterMap.get(s.charAt(start)) == 0) uniqueCharacterCount--;
                    //Slide window front
                    start++;
                }
                //Slide Window rear
                end++;
            }
        }

        return solution;
    }

    /*
        Longest Substring Without Repeating Characters - Given a string s, find the length of the longest substring without repeating characters.
        https://leetcode.com/problems/longest-substring-without-repeating-characters/
        Here we have only two conditions - while adding a new element to window - either our condition of all unique characters
        still holds true or we have gone beyond condition and need to adjust window
    */
    public static int lengthOfLongestSubstring(String s) {
        int solution = 0;
        //We are using a set here to quantify condition
        Set<Character> set = new HashSet<>();
        int start = 0;
        int end = 0;
        char[] stringArray = s.toCharArray();

        while(end < stringArray.length ){
            //Add input[end] to window - check Set to see if condition still holds true
            if(!set.contains(stringArray[end])){
                set.add(stringArray[end]);
                //Calculate solution for current window
                solution = Math.max(solution, end - start + 1);
                //Increment window size
                end++;
            }
            //We have gone beyond condition now -  our window contains a duplicate character
            else{
                //Start removing character until our condition is true again
                while(stringArray[start] != stringArray[end]) {
                    //Remove input[start] from window
                    set.remove(stringArray[start]);
                    start++;
                }
                //start contains the first duplicate character now, our window will start from next index now
                start ++;
                //Slide Window rear
                end++;
            }

        }
        return solution;
    }
}
