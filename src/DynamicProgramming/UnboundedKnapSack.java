package DynamicProgramming;

/* Go through ZeroOneKnapSack before coming here.
   The only difference between the 0/1 Knapsack problem and this problem is that we are allowed to use an unlimited quantity of an item.

   This changes only one thing - When we had choices & we were picking an item - We were recurse over N-1 remaining element.
   But here as we can pick the same element again and again, we will recurse on N only.

 */
public class UnboundedKnapSack {
    public static void main(String[] args) {

    }

    // Exactly same code as 0/1 knapsack except the picking element part
    static int UnboundedKnapSackMemoization(int W, int wt[], int val[], int n, Integer[][] memo){

        //We don't have any elements left to put into bag or knapsack is full.
        //We are traversing backward from item list
        if(n == 0 || W == 0) return 0;

        //have we already solved this problem for current n & W? if yes return solution
        if(memo[n][W] != null) return memo[n][W];

        //Current weight under consideration is less or equal to knapsack current capacity - We have two choices now
        if(wt[n-1] <= W){
            //We can either pick this item, if picked - add its value. Reduce knapsack capacity. Recurse for all elements - repeating is allowed .
            int pickItem = val[n- 1] + UnboundedKnapSackMemoization(W - wt[n-1], wt,val, n, memo);

            //We can skip this item. Recurse for remaining elements
            int skipItem = UnboundedKnapSackMemoization(W , wt,val, n-1, memo);

            // Store in memo table & return maximum of both choices
            memo[n][W] = Math.max(pickItem,skipItem);
            return memo[n][W];
        }
        // Our current item can't fit in knapsack - no option other than to skip it.
        else {
            //Skip item. Recurse for remaining elements
            int skipItem = UnboundedKnapSackMemoization(W , wt,val, n-1, memo);
            memo[n][W] = skipItem;
            return memo[n][W];
        }
    }

    static int UnboundedKnapSackTabulation(int W, int wt[], int val[], int n, int[][] dpTable) {
        //We have our table already filled for base condition - for n=0 & W=0 - We need to populate for rest of cells - sub-problems

        for(int item = 1; item <= n; item++){
            for(int capacity = 1; capacity <= W; capacity++){
                //Current weight under consideration is less or equal to knapsack current capacity - We have two choices now
                //dpTable is of size n+1 * W+1 and start from n=0 & W=0, while wt[] and val[] are of size n.
                //So the element at dpTable[n`][W] has wt[n`-1] and val[n`-1]
                if(wt[item - 1] <= capacity){
                    //We can either pick this item, if picked - add its value. Reduce knapsack capacity.
                    //We have picked this item - now our max value will be -
                    // value of current item + same problem with capacity reduced by current item weight - Repetition allowed
                    int pickItem = val[item - 1] + dpTable[item][capacity - wt[item - 1]];

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

    /* Rod Cutting - Given a rod of length N and an array of prices, price[] that contains prices of all pieces of size smaller than N.
       Determine the maximum value obtainable by cutting up the rod and selling the pieces.
       https://practice.geeksforgeeks.org/problems/rod-cutting0840/1/

       How is this unboundedKnapSack ? We have lengths - [1 - N] as weights and price as val. As we can have same cut again and again its unbounded
     */
    public int cutRod(int price[], int n) {
        int arrayLength = price.length;
        int[][] dpTable = new int[arrayLength + 1][n + 1];
        int[] lengths = new int[arrayLength];
        for(int len = 0; len < arrayLength; len++) lengths[len] = len + 1;
        return UnboundedKnapSackTabulation(n, lengths, price, arrayLength, dpTable);
    }

    /* Coin Change - You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
       Return the fewest number of coins that you need to make up that amount.
       If that amount of money cannot be made up by any combination of the coins, return -1.
        https://leetcode.com/problems/coin-change/
     */
    public int coinChange(int[] coins, int amount) {
        Integer[][] memo = new Integer[coins.length + 1][amount + 1];

        int solution = coinChangeMemo(coins,coins.length,amount,memo);

        // If we don't have a solution Math.min(pickCurrentCoin,skipCurrentCoin) will contain - Integer.MAX_VALUE - 1 - for the scenario where we are skipping the coin
        return solution == Integer.MAX_VALUE - 1 ? -1 : solution;
    }

    public int coinChangeMemo(int[] coins, int N, int amount, Integer[][] memo){
        //Base condition - This equation is calculated mathematically
        //We have zero coins or zero denomination coin - how many of these will be required to get amount ? Infinity
        //We don't have infinity so we are taking Integer.MAX_VALUE -1 - Why ?
        //Because we are adding 1 to recursive solution - this will cause overflow and will be a -ve number
        if(N == 0) return Integer.MAX_VALUE - 1;

        //We have coins but amount required is zero - How many coins will be required ? Zero
        //Be clear here, we don't required number of solutions here but number of coins - empty subset is not a solution here
        if(amount == 0 ) return 0;

        //We already have calculated this? return calculated value.
        if(memo[N][amount] != null) return memo[N][amount];

        //Do we have a choice to pick this coin?
        if(coins[N-1] <= amount){
            //Picked this coin - number of coins will be increased by 1, recurse same problem with reduced amount
            int pickCurrentCoin = 1 + coinChangeMemo(coins, N, amount - coins[N-1], memo);

            //Skip coin - recurse for remaining coin with same amount
            int skipCurrentCoin = coinChangeMemo(coins,N - 1,amount, memo);

            // We want the minimum number of coins from both possible choices
            memo[N][amount] = Math.min(pickCurrentCoin,skipCurrentCoin);
        }
        else {
            //Coin denomination is greater than amount - no choice - skip coin - recurse for remaining coins for same amount
            int skipCurrentCoin = coinChangeMemo(coins,N - 1,amount, memo);
            memo[N][amount] = skipCurrentCoin;
        }

        return memo[N][amount];
    }

    public int coinChangeTabulation(int[] coins, int N, int amount, int[][] dpTable){


        for(int coin = 0; coin <= N; coin++){
            for(int currAmount = 0; currAmount <= amount; currAmount++){
                //Base condition - This equation is calculated mathematically
                //We have zero coins or zero denomination coin - how many of these will be required to get amount ? Infinity
                //We don't have infinity so we are taking Integer.MAX_VALUE -1 - Why ?
                //Because we are adding 1 to recursive solution - this will cause overflow and will be a -ve number
                if(coin == 0) {
                    dpTable[coin][currAmount] = Integer.MAX_VALUE - 1;
                }
                //We have coins but amount required is zero - How many coins will be required ? Zero
                //Be clear here, we don't required number of solutions here but number of coins - empty subset is not a solution here
                else if(currAmount == 0) {
                    dpTable[coin][currAmount] = 0;
                }
                //Do we have a choice to pick this coin?
                else if(coins[coin - 1] <= currAmount){
                    //Picked this coin - number of coins will be increased by 1, check for a solution with reduced amount and same coins available
                    int pickedCurrentCoin = 1 + dpTable[coin][currAmount - coins[coin - 1]];
                    //Skip coin - check for a solution without this coin and same amount
                    int skipCurrentCoin = dpTable[coin - 1][currAmount];
                    // We want the minimum number of coins from both possible choices
                    dpTable[coin][currAmount] = Math.min(pickedCurrentCoin , skipCurrentCoin);
                }
                else {
                    //Coin denomination is greater than amount - no choice - skip coin - check for a solution without this coin and same amount
                    dpTable[coin][currAmount] = dpTable[coin - 1][currAmount];
                }
            }

        }
        return dpTable[N][amount];
    }

    /* Coin Change 2 - You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
       Return the number of combinations that make up that amount.
       If that amount of money cannot be made up by any combination of the coins, return 0.
       https://leetcode.com/problems/coin-change-2/
        Here we want the number of combinations, not coins - be very clear about this. Ask interviewer if base conditions are not clear.
     */
    public int change(int amount, int[] coins) {
        Integer[][] memo = new Integer[coins.length + 1][amount + 1];
        Integer[][] dpTable = new Integer[coins.length + 1][amount + 1];
        int solution = changeMemo(amount, coins, coins.length,memo);
        solution = changeTabulation(amount,coins,coins.length,dpTable);

        return solution;
    }

    public int changeMemo(int amount, int[] coins, int N, Integer[][] memo){
        //Amount is zero , we have found a combination
        if(amount == 0) return 1;
        //Amount is not zero, and we are out of coins - No combination possible - return 0
        if(N == 0) return 0;
        // Have we already solved this? Return is solved
        if(memo[N][amount] != null) return memo[N][amount];

        //We have a choice for this coin
        if(coins[N-1] <= amount){
            //Pick coin - recurse same problem for reduced amount
            int pickCoin = changeMemo(amount - coins[N-1], coins, N, memo);

            //Skipping coin - recurse for same problem without this coin or remaining coins
            int skipCoin = changeMemo(amount, coins, N-1, memo);

            //We want all combination with or without current coin
            memo[N][amount] = pickCoin + skipCoin;

        }else {
            int skipCoin = changeMemo(amount, coins, N-1, memo);
            memo[N][amount] = skipCoin;
        }

        return memo[N][amount];
    }
    public int changeTabulation(int amount, int[] coins, int N, Integer[][] dpTable){

        for(int currentCoinIndex = 0; currentCoinIndex <=N; currentCoinIndex ++){
            for(int currentAmount = 0; currentAmount <= amount; currentAmount ++){
                //Amount is zero , we have found a combination
                if(currentAmount == 0) dpTable[currentCoinIndex][currentAmount] = 1;

                //Amount is not zero, and we are out of coins - No combination possible - return 0
                else if(currentCoinIndex == 0) dpTable[currentCoinIndex][currentAmount] = 0;

                //We have a choice for this coin
                else if(coins[currentCoinIndex - 1] <= currentAmount){
                    //Pick coin - check for a already solved small problem for reduced amount
                    int pickCoin = dpTable[currentCoinIndex][currentAmount - coins[currentCoinIndex - 1]];
                    //Skipping coin - check for an already solved small problem without this coin and same amount
                    int skipCoin = dpTable[currentCoinIndex - 1][currentAmount];

                    //We want all combination with or without current coin
                    dpTable[currentCoinIndex][currentAmount] = pickCoin + skipCoin;
                }
                else{
                    //Coin denomination is more than amount - No choice
                    //Skipping coin - check for an already solved small problem without this coin and same amount
                    int skipCoin = dpTable[currentCoinIndex - 1][currentAmount];
                    dpTable[currentCoinIndex][currentAmount] = skipCoin;
                }
            }
        }
        return dpTable[N][amount];
    }


}
