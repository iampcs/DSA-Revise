package RecursionsAndBackTracking;

import java.lang.invoke.SwitchPoint;
import java.util.*;
import java.util.stream.Collectors;

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
        /*Adding current unprocessed element to solution */
        ArrayList<Integer> listWithAdding = new ArrayList<>(singleSubset);
        listWithAdding.add(numbers[position]);

        /* Skipping current unprocessed element*/
        ArrayList<Integer> listWithoutAdding = new ArrayList<>(singleSubset);

        /* Recurse on both the branches created - with adding and without adding current element*/
        subsets(numbers, position + 1,listWithAdding, solution);
        subsets(numbers,position+1,listWithoutAdding,solution);
    }
    /* Given a set of distinct integers, S, return all possible subsets. - The subsets should be sorted in ascending ( lexicographic ) order.
       https://www.interviewbit.com/problems/subset/

       The only difference here is to print it lexicographically - How can we do it? The idea is to sort it first.
       Then start fixing one element from this sorted order - and run the recursive function on rest
       When we are done with printing all possible subsets with the fixed element - remove it - so we can fix the
       next element and recurse for elements after that.
     */
    public ArrayList<ArrayList<Integer>> subsets(ArrayList<Integer> A) {
        ArrayList<ArrayList<Integer>> solution = new ArrayList<>();
        Collections.sort(A);
        //We are starting with -1, to add empty subset too
        subsets(A,-1, new ArrayList<>(),solution);

        return solution;

    }
    void subsets(ArrayList<Integer> A, int pos, ArrayList<Integer> currSol, ArrayList<ArrayList<Integer>> solution){
        //We have processed all elements - return
        if(pos == A.size())  return;
        //This is different here - as we want to add subsets as soon as they are available - to main order
        solution.add(new ArrayList<>(currSol));
        //For all numbers in A
        for(int cPos = pos + 1; cPos < A.size(); cPos++){
            //Fix current element
            currSol.add(A.get(cPos));
            //Recurse for rest of elements
            subsets(A,cPos,currSol,solution);
            //Remove current element - so in next iteration we can fix another element
            currSol.remove(currSol.size() - 1);
        }
    }

    /* Combination Sum - Given an array of candidate numbers A and a target number B, find all unique combinations in A where the candidate numbers sums to B.
       The same repeated number may be chosen from A unlimited number of times.
       Constraints :
            Elements in a combination (a1, a2, … , ak) must be in non-descending order. (ie, a1 ≤ a2 ≤ … ≤ ak).
            The combinations themselves must be sorted in ascending order.
            The solution set must not contain duplicate combinations.
      https://www.interviewbit.com/problems/combination-sum/

      This problem is similar to previous problem - we will sort the input array and remove duplicates. Only change from previous problem is we can use the same
      element again and again - so we won't move to next element until our function call is valid (sum is less than 0 or no more elements left).
      Once we have all the possible solution with first position number - we will remove it and start with second position.
     */
    public ArrayList<ArrayList<Integer>> combinationSum(ArrayList<Integer> A, int B) {
        ArrayList<ArrayList<Integer>> solution = new ArrayList<>();
        //Sort and remove duplicates
        List<Integer> newA = A.stream().distinct().sorted().collect(Collectors.toList());
        //Start recursive call from position 0
        combinationSum(newA,0,B, new ArrayList<>(), solution);

        return solution;
    }

    private void combinationSum(List<Integer> A, int pos, int currSum, ArrayList<Integer> seq, ArrayList<ArrayList<Integer>> solution) {
        //Can't get a solution from here - return
        if(pos == A.size() || currSum < 0) return;
        //We got our solution - as all numbers are positive - we can't get a solution going forward - return
        //How can we solve this for negative numbers too ?
        if(currSum == 0){
            solution.add(new ArrayList<>(seq));
            return;
        }
        //For all number in A
        for(int cPos = pos; cPos < A.size(); cPos++){
            //Add cPos to current solution - Fix A[cPos]
            seq.add(A.get(cPos));
            //Recurse for same condition with updated target sum
            combinationSum(A,cPos, currSum - A.get(cPos),seq, solution);
            //We must have got all possible solutions starting from A[cPos] from prev recursive call
            //Remove A[cPos] from combination, so we can fix cPos + 1 as first element at current depth
            seq.remove(seq.size() - 1);
        }
    }

    /*  Letter Case Permutation - Given a string s, you can transform every letter individually to be lowercase or uppercase to create another string.
    Ex. Input: s = "a1b2"  Output: ["a1b2","a1B2","A1b2","A1B2"]
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
            solution.add(output);
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
        Ex. Input: nums = [1,2,3]
            Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
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
            /*Removing from output to again place it to a new position in next iteration*/
            output.remove(rCall);
        }
    }

    /*  Letter Combinations of a Phone Number - Given a string containing digits from 2-9 inclusive,
        return all possible letter combinations that the number could represent. A mapping of digit to letters - imagine old Nokia Mobile
        Ex. Input: digits = "23"
            Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
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
            Get all the character for a digit from map, for each character - add to current solution and recurse for next position,
            as we are adding this character in function call only, we don't need to backtrack.
         */
        for(Character charInDigit : map.get(digit[position])){
            letterCombinations(digit,position + 1,map,currSolution + charInDigit,solution);
        }
    }

    /*
    Generate Parentheses - Given n pairs of parentheses, write a function to generate all combinations of well-formed valid parentheses.
     https://leetcode.com/problems/generate-parentheses/
     Ex. Input: n = 3
         Output: ["((()))","(()())","(())()","()(())","()()()"]
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
