package RecursionsAndBackTracking;

/*
All the subsets problems could have been solved via backtracking template, if instead of changing out output inside of function call
we changed it before recursive function call and revert it back post function call. There are some scenarios where it's difficult to
change either our output or a utility data structure we are maintaining inside a function call, for those scenarios backtracking template makes sense.
    Backtracking approach - We will be using this if we are using a data structure that can't be changed inside argument list. This generally is
    called inside a loop - where we are iterating over all possible choices.
            2.1. Change argument to recursive call based on choices/conditions : A -> A`
            2.2. Make recursive call with A` argument
            2.3. Revert argument to its pre-call stage : A` -> A  --> Backtrack step
       func(A,B){
            Base Cond :
            <Optional>loop:
            A = A + a
            func(A,B)
            A = A - a
 */

import java.util.*;
import java.util.stream.Collectors;

public class BackTracking {
    public static void main(String[] args) {

    }
    /*Combination Sum - Given an array of distinct integers candidates and a target integer target,
    return a list of all unique combinations of candidates where the chosen numbers sum to target.
    The same number may be chosen from candidates an unlimited number of times.
    Two combinations are unique if the frequency of at least one of the chosen numbers is different.
    https://leetcode.com/problems/combination-sum/
     */

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> solution = new ArrayList<>();
        // Recursive solution - We are passing position in candidates - processed/unprocessed, current target after picking elements ,
        // an array that contains current picked elements, final solution list
        combinationSum(candidates, 0, target,new ArrayList<Integer>(), solution);

        return solution;
    }

    private void combinationSum(int[] candidates, int pos, int target, ArrayList<Integer> currSolution ,List<List<Integer>> solution) {
        //Base Cond : We have reached a solution, because we are using same array ref for each recursive call, create a new array out of current
        //array and add it to solution
        if(target == 0){
            solution.add(new ArrayList<>(currSolution));
            return;
        }

        //Pick other numbers only if target is greater than 0, and we have elements left in candidates
        if(target > 0 && pos < candidates.length){
            //We can see that we are using an array to maintain our current solution, unlike string which can be modified inside function
            // call and always create a new instance, we can't modify this array inside a function call, hence we will modify it before our
            // recursive call, and then revert it back post call

            //Adding current position element to solution
            currSolution.add(candidates[pos]);
            //As we can choose the same number unlimited number of times, not going to next position
            //Subtracting current element from target and recurse
            combinationSum(candidates,pos, target - candidates[pos], currSolution, solution);
            //Backtrack - removing the element we added before recursive call
            currSolution.remove(currSolution.size() - 1);

            //Not choosing the number and moving to next position
            combinationSum(candidates,pos + 1, target, currSolution, solution);
        }
    }

    /*
        Combination Sum II : Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.
        Each number in candidates may only be used once in the combination.
        Note: The solution set must not contain duplicate combinations.
        This problem can be solved exactly like previous problem by taking a Set as a solution, sorting the solution before adding to Set. But this
        adds a lot of overhead.
        One more way is to ensure when we branch, we ensure one of branch can never contain the number at current position. This can be achieved
        either by maintaining a set of elements in our current solution, adding and removing from that. Or by sorting the input array and checking for
        adjacent indexes. We will be using the sorting approach here.
     */
    public static List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> solution = new ArrayList<>();

        Arrays.sort(candidates);
        System.out.println(candidates);
        combinationSum2(candidates,0,target,new ArrayList<Integer>(), solution);
        return solution;

    }

    private static void combinationSum2(int[] candidates, int position, int target, ArrayList<Integer> currSolution, List<List<Integer>> solution) {
        //Base Condition
        if(target == 0){
            solution.add(new ArrayList<Integer>(currSolution));
            return;
        }
        if(target < 0) return;


        if(target > 0 && position < candidates.length){
            //As all elements are positive, initializing numberAtPrevIndex with negative number
            int numberAtPrevIndex = Integer.MIN_VALUE;

            /* This loop is complex to explain - bear with me.
               For each loop, we will be taking element at current position only if it's not taken before. This can only happen
               when we are at second iteration of each loop.
               So our branches :
               1. Take an element even if it's repeated case - first iteration of every for loop can't check for previous index
                  duplicates as current position starts from position
               2. Never take an element if its repeated case - second iteration onwards - So in each for loop we will be adding unique elements

             */
            for(int currPosition = position; currPosition < candidates.length; currPosition ++){
                if(numberAtPrevIndex == candidates[currPosition])  continue;


                //Adding current position array to solution
                currSolution.add(candidates[currPosition]);
                combinationSum2(candidates,currPosition + 1,target - candidates[currPosition], currSolution, solution);
                //Backtrack
                currSolution.remove(currSolution.size() - 1);

                //Next iteration currPosition number will be previous number
                numberAtPrevIndex = candidates[currPosition];
            }
        }
    }
    /*
        Path with Maximum Gold - In a gold mine grid of size m x n, each cell in this mine has an integer representing the amount of gold in that cell, 0 if it is empty.
        Return the maximum amount of gold you can collect under the conditions:
        1. Every time you are located in a cell you will collect all the gold in that cell.
        2. From your position, you can walk one step to the left, right, up, or down.
        3. You can't visit the same cell more than once.
        4. Never visit a cell with 0 gold.
        5. You can start and stop collecting gold from any position in the grid that has some gold.
        https://leetcode.com/problems/path-with-maximum-gold/
     */
    public int getMaximumGold(int[][] grid) {
        boolean[][] visited= new boolean[grid.length][grid[0].length];
        int solution=0;
        //We don't have a starting point and end point - so we will be starting with all the points in the grid - conditions will be our ending for each point
        for(int row=0;row<grid.length;row++){
            for(int col=0;col<grid[0].length;col++){
                solution=Math.max(solution, getMaximumGold(grid,visited,row,col,0));
            }
        }
        return solution;
    }
    int getMaximumGold(int[][] grid, boolean[][] visited, int row, int col, int currMax){
        //Base condition - We can't go outside grid, or we can't visit an already visited place, or we can't go to a place with 0 gold.
        if(row < 0 || col < 0 || col > grid[0].length - 1 || row > grid.length - 1 || visited[row][col] == true || grid[row][col] == 0) return currMax;

        //Mark this position as visited
        visited[row][col]=true;

        //Once we have collected gold from a position we can't come back to that position, hence if we can only choose one direction from that point.
        //Add current position gold to currMax and recurse in all directions
        int rightMax = getMaximumGold(grid,visited,row,col+1,currMax+grid[row][col]);
        int downMax = getMaximumGold(grid,visited,row+1,col,currMax+grid[row][col]);
        int upMax = getMaximumGold(grid,visited,row-1,col,currMax+grid[row][col]);
        int leftMax = getMaximumGold(grid,visited,row,col-1,currMax+grid[row][col]);

        //Backtrack - We can visit same position from diff direction
        visited[row][col]=false;
        //We want to return the maximum of all directions
        return Math.max(rightMax, Math.max(downMax, Math.max(upMax,leftMax)));
    }

    /*
        N-Queens - The n-queens puzzle is the problem of placing n queens on an n x n chessboard such that no two queens attack each other.
        https://leetcode.com/problems/n-queens/
        Classic backtracking problem - what makes it special is how we are bounding branch getting created. For each column in a given row - we will try
        to place a queen - now we can only place this queen if it's safe to place the queen in that column. If we can't find any such column - we backtrack to previous row.
        If we are successful in placing a queen in column c or given row r, we will try doing the same for next row r+1. Once we have successfully placed all queens in each row
        we have a solution - record it based on how it's asked in question.
        Designing isSafe() method - For placing each queen we can check if there is a queen in current column or either of diagonals - each of this operation is O(N). Can we
        optimize this? So that this is constant ? We can by maintaining three more arrays - columns array, diagonal array , reverse diagonal array.
        1. columns array - size will be N - This will store if there is a queen in a given column index
        2. diagonal array - size will be 2N - 1. If we add (row + col) index - we will see it's unique for each diagonal. We can store this in our 2N - 1 array at respective index
        3. reverse diagonal array - size will be 2N - 1. If we do (row - col) - we will see it's unique for each reverse diagonal. We will get negative indexes here, so to offset
           lets add (N - 1) to each of index - our formula will be (row - col + N - 1).
        We will modify these array too when we backtrack.

        Eg. 4 x 4 array - We have already placed two queens and trying to place our third queen, how will our arrays look like ?
        * Q * *
        * * * Q
        * * * *
        * * * *

        columns : [false, true, false, true]
        diagonal : [false, true, false, false, true, false, false] We have queens at [0,1] & [1,3] so formula (row + column)  - index 1 & 4 will true
        rev diagonal : [false, true, true, false, false, false, false] We have queens at [0,1] & [1,3] so formula (row - column + N(4) - 1) - index 2 & 1 will be true.

     */

    public List<List<String>> solveNQueens(int n) {
        List<List<String>> solution = new ArrayList<>();
        boolean[] columns = new boolean[n];
        boolean[] diagonal = new boolean[2*n - 1];
        boolean[] revDiagonal = new boolean[2*n - 1];

        //Make recursive call
        solveNQueens(n,0,columns,diagonal,revDiagonal, new ArrayList<String>(), solution);


        return solution;
    }

    private void solveNQueens(int n, int row,  boolean[] columns, boolean[] diagonal, boolean[] revDiagonal, ArrayList<String> currSolution, List<List<String>> solution) {
        //We have placed a queen at each row
        if(row == n){
            solution.add(new ArrayList<>(currSolution));
            return;
        }

        //For each column in current row
        for (int col = 0 ; col < n; col ++){
            //If it's safe to place a queen here
            if(isQueenSafe(n,row,col,columns,diagonal,revDiagonal)){
                //Setting columns,diagonal,revDiagonal arrays to true
                columns[col] = true;
                diagonal[row + col] = true;
                revDiagonal[row - col + n - 1] = true;

                //Adding current row configuration to current solution
                char[] currRow = new char[n];
                Arrays.fill(currRow,'.');
                currRow[col] = 'Q';
                currSolution.add(new String(currRow));

                //Queen placed in current col, place next queen in next column - recurse on next row
                solveNQueens(n,row+1,columns,diagonal,revDiagonal,currSolution, solution);

                //Backtrack
                columns[col] = false;
                diagonal[row + col] = false;
                revDiagonal[row - col + n - 1] = false;

                currRow[col] = '.';
                currSolution.remove(currSolution.size() - 1);
            }
        }
    }

    boolean isQueenSafe(int n, int row, int col, boolean[] columns, boolean[] diagonal, boolean[] revDiagonal){
        //Queen is safe only if all three arrays value - columns,diagonal and revDiagonal are false
        return !columns[col] && !diagonal[row + col] && !revDiagonal[row - col + n - 1];
    }

    /*
    Sudoku Solver - https://leetcode.com/problems/sudoku-solver/
     */


}

