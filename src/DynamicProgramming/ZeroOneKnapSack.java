package DynamicProgramming;

import java.util.Collections;

/*
Problem Statement :  Given the weights and profits of ‘N’ items, -> Choice
                     we are asked to put these items in a knapsack which has a capacity ‘C’ -> Constraint <Base-Condition, Branch-Bound>
                     The goal is to get the -- maximum -- profit from the items in the knapsack  -> Optimization
                     Each item can only be -- selected once -- -> Decision
*/
public class ZeroOneKnapSack {
    public static void main(String[] args) {

    }

    /*
        0/1 Knapsack - You are given weights and values of N items, put these items in a knapsack of capacity W to get the maximum total value in the knapsack.
        Will be discussing all three approaches
        https://practice.geeksforgeeks.org/problems/0-1-knapsack-problem0945/1
    */
    static int knapSack(int W, int wt[], int val[], int n)
    {
        int maxProfit = 0;
        // Memo table is generally made up of variables that change in recursive call
        //We need a table of size n+1 & W+1, so we can store n & W elements - n=0 & W=0 will be one of condition
        //Each cell memo[n`][W`] is solution for a sub-problem if we have n` elements and W` weight.
        Integer[][] memo = new Integer[n+1][W+1];

        //Unlike memo, here we have to fill this table with base condition - or smallest possible sub-problem
        //For n = 0 - we don't have any elements in bag - It doesn't matter what is the capacity of bag - our max profit will 0
        //For W=0 - We don't have capacity left - It doesn't matter what items we have - our max profit will be 0
        //These are the base conditions - Fill for these base condition - As default value for int array is 0, we don't need to do any extra work here
        int[][] dpTable = new int[n+1][W+1];

        maxProfit = knapSackBruteForce(W,wt,val,n);
        maxProfit = knapSackMemoization(W,wt,val,n,memo);
        maxProfit = knapSackTabulation(W,wt,val,n,dpTable);

        return maxProfit;
    }



    /* Time complexity - O(2^N) - For each element we have two choices
       Space Complexity - O(N) - Recursive Stack
     */
    static int knapSackBruteForce(int W, int wt[],int val[], int n){

        //We don't have any elements left to put into bag or knapsack is full.
        //We are traversing backward from item list
        if(n == 0 || W == 0) return 0;

        //Current weight under consideration is less or equal to knapsack current capacity - We have two choices now
        if(wt[n-1] <= W){
            //We can either pick this item, if picked - add its value. Reduce knapsack capacity. Recurse for remaining elements.
            int pickItem = val[n- 1] + knapSackBruteForce(W - wt[n-1], wt,val, n-1);

            //We can skip this item. Recurse for remaining elements
            int skipItem = knapSackBruteForce(W , wt,val, n-1);

            // Return maximum of both choices
            return Math.max(pickItem,skipItem);
        }
        // Our current item can't fit in knapsack - no option other than to skip it.
        else {
            //Skip item. Recurse for remaining elements
            int skipItem = knapSackBruteForce(W , wt,val, n-1);
            return skipItem;
        }
    }

    /* Time complexity - O(nC) - memo table stores the results for all the sub-problems.
       Space Complexity - O(nC) - memo Table
     */
    static int knapSackMemoization(int W, int wt[],int val[], int n, Integer[][] memo){

        //We don't have any elements left to put into bag or knapsack is full.
        //We are traversing backward from item list
        if(n == 0 || W == 0) return 0;

        //have we already solved this problem for current n & W? if yes return solution
        if(memo[n][W] != null) return memo[n][W];

        //Current weight under consideration is less or equal to knapsack current capacity - We have two choices now
        if(wt[n-1] <= W){
            //We can either pick this item, if picked - add its value. Reduce knapsack capacity. Recurse for remaining elements.
            int pickItem = val[n- 1] + knapSackMemoization(W - wt[n-1], wt,val, n-1, memo);

            //We can skip this item. Recurse for remaining elements
            int skipItem = knapSackMemoization(W , wt,val, n-1, memo);

            // Store in memo table & return maximum of both choices
            memo[n][W] = Math.max(pickItem,skipItem);
            return memo[n][W];
        }
        // Our current item can't fit in knapsack - no option other than to skip it.
        else {
            //Skip item. Recurse for remaining elements
            int skipItem = knapSackMemoization(W , wt,val, n-1, memo);
            memo[n][W] = skipItem;
            return memo[n][W];
        }
    }

    static int knapSackTabulation(int W, int wt[],int val[], int n, int[][] dpTable) {
        //We have our table already filled for base condition - for n=0 & W=0 - We need to populate for rest of cells - sub-problems

        for(int item = 1; item <= n; item++){
            for(int capacity = 1; capacity <= W; capacity++){
                //Current weight under consideration is less or equal to knapsack current capacity - We have two choices now
                //dpTable is of size n+1 * W+1 and start from n=0 & W=0, while wt[] and val[] are of size n.
                //So the element at dpTable[n`][W] has wt[n`-1] and val[n`-1]
                if(wt[item - 1] <= capacity){
                    //We can either pick this item, if picked - add its value. Reduce knapsack capacity.
                    //We have picked this item - now our max value will be - value of current item +
                    //                                                       a cell where we don't have this element & capacity reduced by current item weight
                    int pickItem = val[item - 1] + dpTable[item - 1][capacity - wt[item - 1]];

                    //We can skip this item - now our max value will be - same as a cell without this item and current weight
                    int skipItem = dpTable[item - 1][capacity];

                    // Store in dpTable table - We have found out solution for current item & capacity
                    dpTable[item][capacity] = Math.max(pickItem,skipItem);
                }
                // Our current item can't fit in knapsack - no option other than to skip it.
                else {
                    //Skip item - now our max value will be - same as a cell without this item and current weight
                    int skipItem = dpTable[item - 1][capacity];
                    dpTable[item][capacity] = skipItem;
                }
            }
        }

        //Solution will be in last cell - Why ? Because that is what is asked in question - Find for array size n and capacity W.
        return dpTable[n][W];

    }


}
