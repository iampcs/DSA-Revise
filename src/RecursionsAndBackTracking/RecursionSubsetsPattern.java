package RecursionsAndBackTracking;

import java.lang.invoke.SwitchPoint;
import java.util.*;

/*
How to identify this pattern ?
    1. Problems will be based on Permutations & Combinations.

How do we solve this ?
    1. We can use Input-Output/Processed-Unprocessed method for recursive solution - We solve this by creating a decision tree around input-output. Each branch in that tree will
        a decision we took based on choices(input + constraints). Each node in that tree will be current input-output at that depth. All leaves in this tree
        will be our potential solution. We will reach leaf when we are out of choices, here we will get our Base condition result.
        In most of the problems, we will be choosing to either pick a value or not pick a value - that two calls. This choosing can
        also be based on condition and constraints, this could be dynamic in some cases and hence we make conditional recursive calls.
        Here we have two approaches :
        1. Change the arguments inside recursive call, as arguments are getting changes inside call only, it remains same for that calling function.
           These will be mostly tail-ending recursions. Most subsets problems can be solved using this approach.
           func(A,B){
                Base Cond :
                func(A`,B)
                func(A,B`)
           }
        2. Backtracking approach - We will be using this if we are using a data structure that can't be changed inside argument list.
            2.1. Change argument to recursive call based on choices/conditions : A -> A`
            2.2. Make recursive call with A` argument
            2.3. Revert argument to its pre-call stage : A` -> A  --> Backtrack step
       func(A,B){
            Base Cond :
            A = A + a
            func(A,B)
            A = A - a
       }
    2. We can use BFS approach to solve this iteratively :
        2.1. Start with an empty set: [[]]
        2.2. Add first number to this empty set -> How should the solution change if we add a new number ? -> Make those changes iteratively
             For simple subset - Create a copy of existing subsets and add this new number to each of them - We are doing exactly what we
             were doing in recursive solution. One set of copy where we are not choosing this new introduced number, one set where we are
             adding this newly introduced number.
        2.3. Repeat this until we have added all input
        Eg. Subset of [1,5,3]  Solution: []
                                       []
                              Copy All/  \  Add 1
                                      [][1]
                             Copy All/     \ Add 5
                                 [][1]  +  [5][1,5]
                        Copy All/             \ Add 3
                        [][1][5][1,5]   +    [3][1,3][5,3][1,5,3]  -> These all subsets is our solution.

       Time complexity - Our recursive tree - T(N) = 2T(N-1) + N(This is for copying the leaf solution to main solution)
       As per Master Theorem for  T(N) = aT(N-b) + f(N) : a>0, b> 0, f(n) = O(n^k) where k >= 0  is O(N^K * a^(N/b))
       Complexity for above recursive call - a = 2, b = 1, k = 1 , Complexity - O( N^1 * 2^(N/1)) - O(N2^N)
       Another simple way to derive this is - We actually copy each element to 2^N subsets * N times for N elements
       Space complexity - O(N2^N) - Number of subsets(2^N) * max space for each subset(N)
*/

public class RecursionSubsetsPattern {
    public static void main(String[] args) {

    }




    /* Subsets -  Given an integer array nums of unique elements, return all possible subsets (the power set).
    https://leetcode.com/problems/subsets/
    */
    public List<List<Integer>> subsets(int[] nums) {
        /* Set to only contain unique elements */
        Set<List<Integer>> solution = new HashSet<>();
        /* Calling recursive function passing position in array, this position tells us what is processed and what is remaining*/
        subsets(nums,0, new ArrayList<>(),solution);
        return new ArrayList<>(solution);
    }
    public void subsets(int[] numbers, int position, List<Integer> singleSubset, Set<List<Integer>> solution ){
        /* All elements are processed, we are at leaf position in our decision tree, store result and return */
        if(position == numbers.length){
            solution.add(singleSubset);
            return;
        }
        /* Decision tree, we will be making two choices here - Add this unprocessed element to our solution or skip it */
        /* Skipping current unprocessed element*/
        ArrayList<Integer> listWithAdding = new ArrayList<>(singleSubset);

        /*Adding current unprocessed element to solution */
        listWithAdding.add(numbers[position]);
        ArrayList<Integer> listWithoutAdding = new ArrayList<>(singleSubset);

        /* Recurse on both the branches created - with adding and without adding current element*/
        subsets(numbers, position + 1,listWithAdding, solution);
        subsets(numbers,position+1,listWithoutAdding,solution);
    }

    /*  Letter Case Permutation - Given a string s, you can transform every letter individually to be lowercase or uppercase to create another string.
    https://leetcode.com/problems/letter-case-permutation/submissions
    This problem is similar to subset problem, only difference here is we branch here conditionally*/
    public List<String> letterCasePermutation(String s) {
        List<String> solution = new ArrayList<>();
        /* Calling recursive function passing position in array, this position tells us what is processed and what is remaining
           third parameter is our current output - this will be all processed elements*/
        letterCasePermutation(s,0,"",solution);
        return solution;
    }

    private void letterCasePermutation(String s, int position, String output, List<String> solution) {
        /* All elements are processed - We are at a leaf - Add current output to solution */
        if(position == s.length()){
            solution.add(output.toString());
            return;
        }

        Character ch = s.charAt(position);
        /* Add digit to current output, recurse for next position - Single branch*/
        if(Character.isDigit(ch)) {
            letterCasePermutation(s,position+1, output + ch, solution);

        /* Add an instance of lowercase to output , recurse for next position
           Add an instance of uppercase to output, recurse for next position
         */
        }else {
            letterCasePermutation(s,position + 1,output + (Character.toLowerCase(ch)), solution);
            letterCasePermutation(s,position + 1,output + (Character.toUpperCase(ch)) , solution);
        }
    }

    /*  Permutations - Given an array nums of distinct integers, return all the possible permutations.
        https://leetcode.com/problems/permutations/
        This problem is different in sense we are not excluding any elements from our solution, here we will be making decision on position
         of element in our solution rather than element itself*/
    public List<List<Integer>> permute(int[] nums) {
        /* All elements are unique, solutions will be unique, else we would have used a set here */
        List<List<Integer>> solution = new ArrayList<>();
        /* Calling recursive function passing position in array, this position tells us what is processed and what is remaining
           third parameter is our current output - this will be all processed elements*/
        permute(nums,0,new ArrayList<Integer>(),solution);

        return solution;
    }

    private void permute(int[] input, int position, ArrayList<Integer> output, List<List<Integer>> solution) {
        /* Solution length will be same as input, we have a solution now, store and return */
        if(output.size() == input.length){
            solution.add(output);
            return;
        }
        /* In subsets, we have two decisions to make, here our decision depends on our current output size.When adding a new element,
        place that element in possible places and recurse for next element. Eg. Current output is AB, we want to add C now.
        Positions of C could be --> ^AB, A^B, AB^
        This for loop adds to all those positions and recurse, because we don't want to repeat same element, that element is removed at end of
        each recursion.
         */
        for(int rCall = 0; rCall < output.size() + 1; rCall++){
            /* Current element added to one of all possible places in current output*/
            output.add(rCall, input[position]);

            /* Current position added to one of possible place in output, recurse for remaining elements */
            permute(input,position+1, new ArrayList<>(output),solution);

            /*Backtrack*/
            output.remove(rCall);
        }
    }

    /*  Letter Combinations of a Phone Number - Given a string containing digits from 2-9 inclusive,
        return all possible letter combinations that the number could represent. A mapping of digit to letters - imagine old Nokia Mobile
        https://leetcode.com/problems/letter-combinations-of-a-phone-number/ */
    public List<String> letterCombinations(String digits) {
        List<String> solution = new ArrayList<>();

        //Corner case - if no digits
        if(digits.equals(""))
            return solution;
        //Mapping number to digits
        Map<Character,Character[]> map = new HashMap<>();
        map.put('2', new Character[]{'a','b','c'});
        map.put('3', new Character[]{'d','e','f'});
        map.put('4', new Character[]{'g','h','i'});
        map.put('5', new Character[]{'j','k','l'});
        map.put('6', new Character[]{'m','n','o'});
        map.put('7', new Character[]{'p','q','r','s'});
        map.put('8', new Character[]{'t','u','v'});
        map.put('9', new Character[]{'w','x','y','z'});

        char[] digit = digits.toCharArray();
        /* Calling recursive function passing position in array, this position tells us what is processed and what is remaining
           fourth parameter is our current output - this will be all processed elements*/
        letterCombinations(digit,0,map,"",solution);
        return solution;

    }

    private void letterCombinations(char[] digit, int position, Map<Character, Character[]> map, String currSolution, List<String> solution) {
        /* Solution length will be same as input, we have a solution now, store and return */
        if(currSolution.length() == digit.length){
            solution.add(currSolution);
            return;
        }

        /* This is similar to permute - only difference here is digit place is fixed, we permute over characters for each digit
            Get all the character for a digit from map, for each character :
                Add to current solution and recurse for next position, as we are adding this character in function call only,
                we don't need to backtrack.
         */
        for(Character charInDigit : map.get(digit[position])){
            letterCombinations(digit,position + 1,map,currSolution + charInDigit,solution);
        }
    }

    /*
    Generate Parentheses - Given n pairs of parentheses, write a function to generate all combinations of well-formed valid parentheses.
     https://leetcode.com/problems/generate-parentheses/
     This problem is similar to subset problem - We branch here conditionally. For a valid parentheses, left parentheses should always be
     placed first before right parentheses. This means at any given point in our solution, there could never arise a case where left parentheses
     count could be smaller than right parentheses count.
     */
    public List<String> generateParenthesis(int n) {
        List<String> solution = new ArrayList<>();
        /* Recursive function that takes - n, current left parentheses count in our current solution,
           current right parentheses count in our current solution, current solution, final solution */
        generateParenthesis(n,0,0,"",solution);
        return solution;
    }

    private void generateParenthesis(int n, int lpCount, int rpCount, String currSolution, List<String> solution) {
        /* We have used all left & right parentheses. Add current solution to final solution and return */
        if(lpCount == n && rpCount == n){
            solution.add(currSolution);
            return;
        }
        /* Left count is more than right count, means we are free to add left or right parentheses in our current solution */
        if(lpCount > rpCount){
            /* We can't add more than n left parentheses - Add left and recurse*/
            if(lpCount < n)
                generateParenthesis(n, lpCount + 1, rpCount, currSolution + "(", solution);
            /* We can't add more than n right parentheses - Add right and recurse*/
            if(rpCount < n)
                generateParenthesis(n, lpCount, rpCount + 1, currSolution + ")", solution);
        }else
            /* Can only add left parentheses now else our condition - "there could never arise a case where left parentheses
                count could be smaller than right parentheses count" -  will fail */
            if(lpCount < n)
                generateParenthesis(n, lpCount + 1, rpCount, currSolution + "(", solution);
    }


}
