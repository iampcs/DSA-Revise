package DynamicProgramming;

/* Fibonacci numbers are a series of numbers in which each number is the sum of the two preceding numbers.
   Problems in this pattern will be similar.
 */
public class FibonacciSeries {
    public static void main(String[] args) {

        System.out.println(jump(new int[]{7,8,4,2,0,6,4,1,8,7,1,7,4,1,4,1,2,8,2,7,3,7,8,2,4,4,5,3,5,6,8,5,4,4,7,4,3,4,8,1,1,9,0,8,2}));

    }

    /* Climbing Stairs - You are climbing a staircase. It takes n steps to reach the top.
   Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
   https://leetcode.com/problems/climbing-stairs/
 */
    public int climbStairs(int n) {
        Integer[] memo = new Integer[n+1];
        return climbStairsMemo( n, 0, memo);
    }

    private int climbStairsMemo(int n, int curr, Integer[] memo) {
        //Base Conditions
        if(curr > n) return 0;
        if(curr == n) return 1;

        //Have we solved this problem? If yes return
        if(memo[curr] != null) return memo[curr];

        //We have a choice to take one jump or two jump
        int oneStep = climbStairsMemo(n, curr + 1, memo);
        int twoStep = climbStairsMemo(n,curr + 2, memo);

        //We need total of all choices here
        memo[curr] = oneStep + twoStep;
        return memo[curr];
    }

    /* Jump Game - You are given an integer array nums. You are initially positioned at the array's first index, and each element in the array represents
        your maximum jump length at that position. Return true if you can reach the last index, or false otherwise.
        https://leetcode.com/problems/jump-game/
        Similar to unbounded knapsack
     */

    public boolean canJump(int[] nums) {
        Boolean[] memo = new Boolean[nums.length + 1];
        //We are starting from first index and recurring on all available choices to check if we can reach end
        //Alternatively we could have start from end and check if there is any way to reach start too
        return canJumpMemo(nums, 0, memo);
    }

    private boolean canJumpMemo(int[] nums, int currPosition, Boolean[] memo) {
        //Base condition here
        if(currPosition >= nums.length) return false;

        //Position of this condition is important here - we have to cover a case where array size is 1
        if(currPosition == nums.length - 1) return true;
        //No way to jump from here
        if(nums[currPosition] == 0) return false;


        if(memo[currPosition] != null) return memo[currPosition];

        //Recurse for all possible jumps
        for(int jump = 1 ; jump <= nums[currPosition]; jump++){
            memo[currPosition] = canJumpMemo(nums, currPosition + jump, memo);
            //Optimization - We just need to return if we can reach end - once we get a true ; no need to jump further
            if(memo[currPosition]) return true;
        }

        return memo[currPosition];
    }

    /* Jump Game II - Given an array of non-negative integers nums, you are initially positioned at the first index of the array.
       Each element in the array represents your maximum jump length at that position.Your goal is to reach the last index in the minimum number of jumps.
       You can assume that you can always reach the last index.
       https://leetcode.com/problems/jump-game-ii/

       We are calculating from right to left here.

     */
    public static int jump(int[] nums) {

        Integer[] memo = new Integer[nums.length];

        return jumpMemo(nums, 0 , memo);
    }

    private static int jumpMemo(int[] nums, int currIndex, Integer[] memo) {

        //Base condition
        //We have reached/crossed end - no jumps required
        if( currIndex >= nums.length - 1) return 0;
        //Have we already solved this?
        if(memo[currIndex] != null) return memo[currIndex];

        //Given - there is always a way to reach end - maximum steps that will be required is length of array - if all are 1's
        //It's not possible to reach from [0] - but let's ignore this condition
        int minSteps = nums.length;
        //Will recurse for all jumps possible
        for (int jump = 1; jump <= nums[currIndex]; jump++){
            // Minimum step if we take this jump from current index
            //Observe this step carefully - At this moment we are not at end index - we have jumps from 1 to nums[currIndex] - even if we cross the last index - minimum number of
            //jumps will still be 1 - Why? If we are crossing means we have a solution present - which will be more than 1 and less than current number
            int minStepWithCurrentJump = 1 + jumpMemo(nums, currIndex + jump, memo);
            //Store minimum of all jumps
            //Silly question - Why are we not storing this directly to memo[currIndex] using Math.min(memo[currIndex], minStepWithCurrentJump)?
            //Because of a simple principle - we always store final calculated value in memo[] table. We are in a loop, we don't have a final solution yet.
            minSteps = Math.min(minSteps, minStepWithCurrentJump);
        }

        //Store minimum of all jumps for a current index
        //We are actually storing memo[currIndex] backwards - Only after recursion of all steps possible
        memo[currIndex] = minSteps;

        return memo[currIndex];
    }

    /* Min Cost Climbing Stairs - You are given an integer array cost where cost[i] is the cost of ith step on a staircase.
    Once you pay the cost, you can either climb one or two steps.You can either start from the step with index 0, or the step with index 1.
    Return the minimum cost to reach the top of the floor.
    https://leetcode.com/problems/min-cost-climbing-stairs/

    This problem is a simple version of jump.

     */
    public int minCostClimbingStairs(int[] cost) {

        int startWithZeroIndex = minCostClimbingStairs(cost, 0, new Integer[cost.length]);
        int statWithFirstIndex = minCostClimbingStairs(cost, 1, new Integer[cost.length]);

        return Math.min(startWithZeroIndex, statWithFirstIndex);
    }

    private int minCostClimbingStairs(int[] cost, int currIndex, Integer[] memo) {
        //We have reached top + 1
        if(currIndex > cost.length - 1) return 0;
        if(memo[currIndex]!= null) return memo[currIndex];

        int oneStep = cost[currIndex] + minCostClimbingStairs(cost, currIndex + 1, memo);
        int twoStep = cost[currIndex] + minCostClimbingStairs(cost, currIndex + 2, memo);

        memo[currIndex] = Math.min(oneStep, twoStep);
        return memo[currIndex];
    }

    /* House Robber - You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.
       Given an integer array nums representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.
       https://leetcode.com/problems/house-robber/
     */

    public int rob(int[] nums) {
        return  robMemo(nums, 0, new Integer[nums.length]);
    }

    private int robMemo(int[] nums, int currIndex, Integer[] memo) {
        //Base condition - No more house remaining to rob
        if(currIndex > nums.length - 1) return  0;
        if(memo[currIndex] != null) return memo[currIndex];

        //We have two choices here -
        //1. Rob this house - collect cash - recurse for current house + 2
        //2. Skip this house recurse for current house + 1
        int chooseToRob = nums[currIndex] + robMemo(nums, currIndex + 2, memo);
        int chooseToSkip = robMemo(nums, currIndex + 1, memo);

        //Store the maximum cash collected for current index
        memo[currIndex] = Math.max(chooseToRob, chooseToSkip);
        return memo[currIndex];

    }


}


