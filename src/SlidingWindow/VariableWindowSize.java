package SlidingWindow;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
    public static void main(String[] args) throws IOException {

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

    /*
        Minimum Window Substring - Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window.
        If there is no such substring, return the empty string "". Answer will be unique.
        https://leetcode.com/problems/minimum-window-substring/
    */

    public static String minWindow(String s, String t) {
        int start=0,end=0;
        int solutionStart=-1,endSolution=-1,solutionLength = Integer.MAX_VALUE;
        // We are using two hashMaps here , one to store Character frequency in t and one to store character frequency in current window
        Map<Character,Integer> charFrequency = new HashMap<>();
        for(Character ch : t.toCharArray()) {
            if(charFrequency.containsKey(ch)) charFrequency.put(ch, charFrequency.get(ch) + 1);
            else charFrequency.put(ch,1);
        }
        //How many unique characters are there in t
        int charToMatch = charFrequency.size();

        Map<Character,Integer> windowMap = new HashMap<>();
        //How many characters matched till now in current window
        int charMatch = windowMap.size();

        while (end < s.length()){
            //Add to window hashmap only if its only if its required - ignore those characters not there in t
            if(charFrequency.containsKey(s.charAt(end))){
                if(windowMap.containsKey(s.charAt(end)))
                    windowMap.put(s.charAt(end), windowMap.get(s.charAt(end)) + 1);
                else windowMap.put(s.charAt(end), 1);

                //If we have matched all instances of a character
                //Use equals when matching Integer,Character,Double etc, == matches reference and can be wrong
                //https://leetcode.com/problems/minimum-window-substring/discuss/266059/Why-you-failed-the-last-test-case%3A-An-interesting-bug-when-I-used-two-HashMaps-in-Java
                if(windowMap.get(s.charAt(end)).equals(charFrequency.get(s.charAt(end)))){
                    charMatch++;
                }
            }
            // We have the solution now, can we reduce its solution size without breaking condition ?
            // Here even after we have reached our condition, there is still scope for a new solution, if there is a new solution available
            //means we have actually crossed our condition - Hence slide the window
            while(charMatch == charToMatch){
                if(end - start + 1 < solutionLength){
                    solutionLength = end - start + 1;
                    endSolution = end;
                    solutionStart = start;
                }

                if(windowMap.containsKey(s.charAt(start))){
                    //Post this if we reduce, we will be short of an instance of a character, hence reducing character matched
                    if(windowMap.get(s.charAt(start)).equals(charFrequency.get(s.charAt(start)))){
                        charMatch--;
                    }
                    windowMap.put(s.charAt(start), windowMap.get(s.charAt(start)) - 1);

                }
                //Slide window front
                start++;
            }
            //Slide Window rear
            end++;

        }

        return solutionStart == -1 ? "" : s.substring(solutionStart,endSolution+1);

    }
}
