package PopularProblemsGroup;

import java.util.*;

public class BestTimeToBuyAndSellStocks {
    public static void main(String[] args) {

    }
    /* Best Time to Buy and Sell Stock - You are given an array prices where prices[i] is the price of a given stock on the ith day.
    You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
    Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
    https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

    As we have only one transaction allowed - we can consider each day as selling day - but to sell we need to buy it first. So the day which
    had the least stock price will be our buy day. We need to keep a track of this while we traverse the price array.
     */
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int minBuyPrice = Integer.MAX_VALUE;

        for(int price : prices){
            //Is this price the current minimum - we can't sell on this day - save it as new current minimum and continue
            if(minBuyPrice > price) {
                minBuyPrice = price;
                continue;
            }
            //We can sell on this day
            maxProfit = Math.max(maxProfit, price - minBuyPrice);
        }
        return maxProfit;
    }

    /* Best Time to Buy and Sell Stock II - You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
       On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at any time.
       However, you can buy it then immediately sell it on the same day.Find and return the maximum profit you can achieve.
       https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/

       As we can't hold more than one share, we have to buy before selling. Algorithm :
       1. Initiate buyDate and sellDate as 0
       2. If price is going up - increment sell date
       3. If price dips - collect profit - initialize buyDate and sellDate again to new price

       So basically we are collecting  profit at highest point of an uptrend.
     */
    public int maxProfitII(int[] prices) {
        int maxProfit = 0;
        int buyDay = 0;
        int sellDay = 0;

        for(int day = 1; day < prices.length; day++){
            //If price has dip - collect profit - update buyDate to current day
            if(prices[day] < prices[day - 1]){
                maxProfit += prices[sellDay] - prices[buyDay];
                buyDay = day;
            }
            //Move sell day to current day - We want to treat each day as a sellDay
            sellDay = day;
        }

        //This is for the final uptrend - Its possible we finish with an uptrend and there is no downtrend to collect profit
        maxProfit += prices[sellDay] - prices[buyDay];

        return maxProfit;
    }
    /* Best Time to Buy and Sell Stock with Transaction Fee
       You are given an array prices where prices[i] is the price of a given stock on the ith day, and an integer fee representing a transaction fee.
       Find the maximum profit you can achieve. You may complete as many transactions as you like, but you need to pay the transaction fee for each transaction.
       Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
       https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/

       We can add transaction fee either while buying or selling - here we are adding to while buying.
       We are considering buying as loss here - as it's an expenditure
       In this problem we are storing state of a transaction on ith day.
       States are :
            1. On ith day - I am holding a share
            2. On ith day - I am not holding a share
       First thing that comes to mind during a recursive solution is on ith day, I buy a share or sell a share - but then we have to keep a track of buying and profit
       separately and pass it as function - designing solutions based on states helps writing DP solutions as we just add/subtract to previous state to reach current state.

       I am solving this using memo method - as that more intuitive to me - from a recursive brute to memo.
       Tabulation of this problem is given here - https://www.youtube.com/watch?v=oVKaUeQsQJE
     */
    public int maxProfitTransactionFee(int[] prices, int fee) {
        //memo[i][0] signifies - I am not holding a stock on ith day - what could be my max profit at this state
        //memo[i][1] signifies - I am holding a stock on ith day - what could be my max profit at this state
        Integer[][] memo = new Integer[prices.length][2];

        //I want max profit on my last day of trading when I am not holding stock
        return maxProfitTransactionFee(prices, prices.length - 1, fee, false, memo);
    }

    private int maxProfitTransactionFee(int[] prices, int index, int fee, boolean hasStock, Integer[][] memo) {
        //Baseclass : On 1st day - if I am holding a stock - means I have bought it on that day itself
        //So my profit is negative here : -ve (price of share + fee)
        if(index == 0 && hasStock) return -prices[index] - fee;
        //At day zero if I am not holding a share - my profits are zero - I can't sell it either
        if(index == 0) return 0;

        //Have we solved this problem?
        if(memo[index][hasStock ? 1:0] != null)
            return memo[index][hasStock ? 1:0];

        //If I am holding a stock on index day
        if(hasStock){
            //It's only possible if
            //I bought it today only - In that case my profit is : profit I made till yesterday - money I spend to buy this stock today
            int boughtToday = maxProfitTransactionFee(prices, index - 1, fee, false, memo) - prices[index] - fee;
            //I had this stock yesterday - profit same as yesterday
            int carryForward = maxProfitTransactionFee(prices, index - 1, fee, true, memo);
            //I want the max profit of above scenarios
            memo[index][hasStock ? 1:0] = Math.max(boughtToday, carryForward);
        }else {
            //I am not holding a stock on index(today) day
            //Its possible if
            //I sold it today only - In that case update profit to - profit when I had this stock till yesterday + stock's today price
            int soldToday = maxProfitTransactionFee(prices, index - 1,fee, true, memo) + prices[index];
            //I never had this stock to begin with - profit will be same as yesterday
            int carryForward = maxProfitTransactionFee(prices, index - 1, fee, false, memo);

            //I want the max profit of above scenario
            memo[index][hasStock ? 1:0] = Math.max(soldToday, carryForward);
        }

        return memo[index][hasStock ? 1:0];
    }
    /* Best Time to Buy and Sell Stock with Cooldown
       You are given an array prices where prices[i] is the price of a given stock on the ith day.
       Find the maximum profit you can achieve. You may complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:
            After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
       Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
       https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/

       Solve previous problem of Transaction Fee before coming here - We will use the same state method - Only difference here is the base case and cooldown restrictions.
    */
    public int maxProfitCoolDown(int[] prices) {
        Integer[][] memo = new Integer[prices.length][2];
        //Return me state - Last day of trading with no stock holding
        return maxProfitCoolDown(prices, prices.length - 1, false, memo);
    }

    private int maxProfitCoolDown(int[] prices, int index, boolean hasStock, Integer[][] memo) {
        //Base condition are important here - unlike last problem - we have to fill our memo table for 2 days here
        //If we are at first day - We can have two states like previous question
        if(index == 0){
            if(hasStock) return -prices[index];
            else return 0;
        }
        //If we are at day 1 - Again we have two states
        //1. We are holding : Here we have two cases - we take the max of both
        //      Case 1 : Bought today - update profit to : previous day profit without stock - today's price
        //      Case 2: Carry forwarded from previous day : No effect on profit - same as previous day profit
        //2. We are not holding : Two cases here
        //      Case 1: We sold it today - update profit : previous day profit when we were holding + today's price
        //      Case 2: Never had stock to begin with : previous day profit
        if(index == 1){
            if(hasStock) return Math.max(
                    //Carry forward
                    maxProfitCoolDown(prices,index - 1, true, memo),
                    //Bought today
                    maxProfitCoolDown(prices,index - 1, false, memo) - prices[index]
            );
            else return Math.max(
                    //No stock to begin with
                    maxProfitCoolDown(prices,index - 1, false, memo),
                    //Sold it today
                    maxProfitCoolDown(prices,index - 1, true, memo) + prices[index]
            );
        }

        if(memo[index][hasStock ? 1:0] != null)
            return memo[index][hasStock ? 1:0];

        if(hasStock){
            //If we are buying it today - We need to have sold it at - (day - 2) because of cooldown period
            //Because of this condition only we are having base case till day 2.
            int boughtToday = maxProfitCoolDown(prices, index - 2, false, memo) - prices[index];
            int carryForward = maxProfitCoolDown(prices, index - 1, true, memo);
            memo[index][hasStock ? 1:0] = Math.max(boughtToday, carryForward);
        }else {
            int soldToday = maxProfitCoolDown(prices, index - 1, true, memo) + prices[index];
            int carryForward = maxProfitCoolDown(prices, index - 1, false, memo);
            memo[index][hasStock ? 1:0] = Math.max(soldToday, carryForward);
        }

        return memo[index][hasStock ? 1:0];
    }

    /* Best Time to Buy and Sell Stock with at-most k transactions
       You are given an integer array prices where prices[i] is the price of a given stock on the ith day, and an integer k.
       Find the maximum profit you can achieve. You may complete at most k transactions.
       Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
       https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/

       I hate this problem - I could not find an optimized way for memo solution - because we are not filling our table serially - which is required for optimized solution.
       Time complexity for memo - O(N^2.k)
       Time complexity for Tabulation - O(N.k)

       Here our cache table stores - for a given day and number of transactions allowed what could be my max profit
       Tabulation approach with optimization - https://www.youtube.com/watch?v=3YILP-PdEJA

       A variant of this problem is with 2 transactions - although that can be solved with this approach with k = 2, but it has much faster solution available. I am not gonna code
       it here. Can check
       Greedy approach 1 - https://www.youtube.com/watch?v=wuzTpONbd-0
       Sliding window approach 2 - https://www.youtube.com/watch?v=gVavspgEHyM

     */
    public int maxProfitKTransactions(int k, int[] prices) {
        if(k == 0 || prices.length <= 1) return 0;
        Integer[][] memo = new Integer[prices.length][k + 1];
        Integer[][] dpTable = new Integer[k+1][prices.length];
        int maxProfit = 0;
        //Give me maxProfit on last day with k transactions
        maxProfit =  maxProfitKTransactionsMemo(prices, prices.length - 1, k, memo);
        maxProfit = maxProfitKTransactionsTabulation(k,prices,dpTable);


        return maxProfit;
    }

    private int maxProfitKTransactionsMemo(int[] prices, int day, int k, Integer[][] memo) {
        //Base case : Can't make profit if not allowed to transact or only one day prices are there
        if(k == 0 || day == 0) return 0;
        if(memo[day][k] != null) return memo[day][k];

        //I have two choices to make at any day - I will take the max of both - What is the profit I can make if I
        //Don't do any transaction today - will same as previous day with k transactions left
        int noTransactionToday = maxProfitKTransactionsMemo(prices, day - 1, k, memo);
        //I sell today - means using up 1 transaction
        //To sell today - I have to buy at some previous day - means loss of that day price -> minimize this loss -> pick a day with the least loss : pDay max profit - prices[pDay]
        int transactionToday = Integer.MIN_VALUE;
        //Checking the least loss for all possible previous days with k-1 transactions - This could be optimized in tabulation method
        for(int pDay = 0; pDay < day; pDay++){
            transactionToday = Math.max(
                    transactionToday,
                    prices[day] - prices[pDay] + maxProfitKTransactionsMemo(prices, pDay, k - 1, memo)
            );
        }
        memo[day][k] = Math.max(noTransactionToday, transactionToday);
        return memo[day][k];
    }

    private int maxProfitKTransactionsTabulation(int k, int[] prices, Integer[][] dpTable) {

        for(int cK = 0; cK <= k ; cK++){
            int maxPrevBuyProfit = Integer.MIN_VALUE;
            for (int day = 0; day < prices.length; day++){
                if(cK == 0 || day == 0) {
                    dpTable[cK][day] = 0;
                    continue;
                }
                //This logic is explained here - https://youtu.be/3YILP-PdEJA?t=2110
                maxPrevBuyProfit = Math.max(maxPrevBuyProfit, dpTable[cK - 1][day - 1] - prices[day - 1]);
                int noTransactionToday = dpTable[cK][day - 1];
                int transactionToday = prices[day] + maxPrevBuyProfit;

                dpTable[cK][day] = Math.max(noTransactionToday, transactionToday);
            }
        }
        return dpTable[k][prices.length - 1];
    }

}
