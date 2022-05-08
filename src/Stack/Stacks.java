package Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/* Stack questions will always be optimization problems - These problems could also be solved using brute-force, but with stacks we can do this
   in linear time. First few problems will cover basic on how to think in terms of stacks - how we can save on multiple traversal costs.
   Using this basics we will try to solve actual problems.
   Note: If in a problem - we observe that we only are concerned about top/most recent elements only to make a decision - this could be a hint to use a Stack.
 */

public class Stacks {
    public static void main(String[] args) {
        carFleet(10,new int[]{6,8}, new int[]{3,2});

    }

    /* Next Greater Element to Right - Given an array arr[ ] of size N having distinct elements,
       the task is to find the next greater element for each element of the array in order of their appearance in the array.
       Next greater element of an element in the array is the nearest element on the right which is greater than the current element.
       If there does not exist next greater of current element, then next greater element for current element is -1. For example, next greater of the last element is always -1.
       https://practice.geeksforgeeks.org/problems/next-larger-element-1587115620/1/

       We will be storing index here instead - we require distance between days
       https://leetcode.com/problems/daily-temperatures/submissions/

       This question can be solved in O(N^2) using two for loops. But we can use a stack to keep track of next greatest element. Because we want the next greatest element on right
       we can traverse element from right and push to Stack, for any given element we will keep poping until Stack is empty - means no greater element
       or we find greater element to right. After this we will push the current element to Stack.
       This will reduce time complexity to O(N)
       Similar to this we can also find next smaller element to right.
     */
    public static long[] nextLargerElement(long[] arr, int n)
    {
        long[] solution = new long[arr.length];
        Stack<Long> stack = new Stack<>();
        //Traversing from right to left
        for(int index = arr.length - 1; index >= 0; index--){
            //Pop all elements which are smaller than current element - we want only next greater - these elements won't be used
            while (!stack.isEmpty() && stack.peek() < arr[index]) stack.pop();
            //There is no greater element left - put -1
            if(stack.isEmpty()) solution[index] = -1L;
            //top of stack contains next greater element
            else solution[index] = stack.peek();

            //Add current element to top of stack - this could be solution for elements to left
            stack.push(arr[index]);
        }
        return solution;
    }
    /* Smallest number on left - Given an array a of integers of length n, find the nearest smaller number for every element such that the smaller element is on left side.
       If no small element present on the left print -1.
       https://practice.geeksforgeeks.org/problems/smallest-number-on-left3403/1/

       Similar to previous question - just that we are looking left - So we will be traversing from left to right and compare, Here we can have duplicate elements too -
       so comparing for that too.
       Similar to this we can also find left greater element.
     */
    static List<Integer> leftSmaller(int n, int a[])
    {
        List<Integer> solution = new ArrayList<>();
        Stack<Integer> stack = new Stack<>();
        //Left to Right
        for(int index = 0; index < a.length; index++){
            //Checking for equal or smaller - duplicate elements
            while (!stack.isEmpty() && a[index] <= stack.peek()) stack.pop();
            if(stack.isEmpty()) solution.add(-1);
            else solution.add(stack.peek());

            stack.push(a[index]);
        }
        return solution;
    }
    /* Online Stock Span - Design an algorithm that collects daily price quotes for some stock and returns the span of that stock's price for the current day.
       The span of the stock's price today is defined as the maximum number of consecutive days (starting from today and going backward) for which the stock
       price was less than or equal to today's price.
       For example, if the price of a stock over the next 7 days were [100,80,60,70,60,75,85], then the stock spans would be [1,1,1,2,1,4,6].
       Implement the StockSpanner class:
       StockSpanner() Initializes the object of the class.
       int next(int price) Returns the span of the stock's price given that today's price is price.

       This looks like a big question but its boils down to finding left greater element index. Our stock span will be from current index till (left greater element index - 1)
       As we don't have an array upfront here, we are storing those price and result in stack as pair.
     */
    class StockSpanner {
        // int[0] - price, int[1] - span
        Stack<int[]> stack;
        public StockSpanner() {
            stack = new Stack<>();
        }

        public int next(int price) {
            //Default span
            int span = 1;
            /*While we don't find a greater element on left - keep poping and add this element span to current element - remember we are only storing an element on stack
             if it has no smaller element on left
             Eg. Suppose our Stack is like - [50,1],[40,2] :  Elements - 50,30,40
             now we want to add 60, So this 60 will take all the spans of 40(which took span of 30) and 50, and our stack will be - [60,4]
             Anything which is less than or equal to 60 will have a span of 1. if its greater than 60, means it's greater than all elements before 60, So it will take span of 60
             */
            while (!stack.isEmpty() && stack.peek()[0] <= price)
                span += stack.pop()[1];
            stack.push(new int[]{price, span});
            return span;
        }
    }

    /* Largest Rectangle in Histogram - Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.
       https://leetcode.com/problems/largest-rectangle-in-histogram/
       Eg. heights = [2,1,5,6,2,3] Output: 10
       Lets suppose we want to find what is the maximum area that can be drawn from each index ? Suppose from index 2(5), For that we need to expand it in both direction -
       we can only expand if neighbours are greater/same height as current index. For we can expand to index 3(6), but not to index 1(1). So our max height at current index(2)
       will be arr[currentIndex] * Width(expanded)
       In simple terms, we need to find the first smallest element in both direction, we can expand till smallerIndex - 1.
       So, calculate - leftSmaller & rightSmaller to get width and multiply by currentIndex height to get max rectangle area at current index. Max of all indexes will be our solution

       Note: Only difference here is - a case when we don't find left/right smaller - in previous questions we were asked to assign 0/-1 to those, here we will assign indexes (-1 & length)
       for left & right misses - assuming index -1 & length will have 0 building heights.
       Note: We could eliminate Stack here and use rightSmaller and leftSmaller arrays itself to calculate next but that will defeat the purpose of Stacks which we are using here
       It could be done for space optimization and is out of scope for now.
     */
    public static int largestRectangleArea(int[] heights) {
        int maxArea = 0;
        int leftSmaller[] = new int[heights.length];
        int rightSmaller[] = new int[heights.length];
        Stack<Integer> stack = new Stack<>();

        //Calculating rightSmaller
        for(int index = heights.length - 1; index >=0 ; index--){
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[index]) stack.pop();
            //Assigning length index for elements having no smaller to right
            if(stack.isEmpty()) rightSmaller[index] = heights.length;
            else rightSmaller[index] = stack.peek();

            stack.push(index);
        }

        stack.clear();
        //Calculating leftSmaller and area for current index
        for(int index = 0; index < heights.length; index++){
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[index]) stack.pop();
            //Assigning -1 to elements having no smaller to left
            if(stack.isEmpty()) leftSmaller[index] = -1;
            else leftSmaller[index] = stack.peek();

            stack.push(index);

            //Area = currentHeight * Width
            int currentIndexArea = heights[index] * (rightSmaller[index] - leftSmaller[index] - 1);
            maxArea = Math.max(maxArea, currentIndexArea);
        }
        return maxArea;

    }
    /* Maximal Rectangle - Given a rows x cols binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.
       https://leetcode.com/problems/maximal-rectangle/

       Although this problem looks different, but it's an extension of largestRectangleArea only. If we consider each row as base, we can get building heights for each row
       and convert this to largestRectangleArea problem. Our solution will be max of all such rows - Lets see an example of 2D binary matrix to largestRectangleArea
       Eg.   2D  Binary Matrix          1D building heights
           ["1","0","1","0","0"],          [1,0,1,0,0]
           ["1","0","1","1","1"],          [2,0,2,1,1]
           ["1","1","1","1","1"],          [3,1,3,2,2]
           ["1","0","0","1","0"]           [4,0,0,3,0]  //Building can't be in air - We add to height of previous base height - only if we have a 1 in current place

     */
    public static int maximalRectangle(char[][] matrix) {
        int maxArea = 0;
        int[][] floorMatrix = new int[matrix.length][matrix[0].length];
        //Converting 2D matrix to Rows * 1D arrays
        for(int row = 0 ;row < matrix.length; row++){
            for(int col = 0; col < matrix[0].length; col++){
                if(row == 0) floorMatrix[row][col] = Character.getNumericValue(matrix[row][col]);
                else if(matrix[row][col] == '1') floorMatrix[row][col] = 1 + floorMatrix[row - 1][col];
                else floorMatrix[row][col] = 0;
            }
            //Calculate max area of each row
            maxArea = Math.max(maxArea,largestRectangleArea(floorMatrix[row]));
        }

        return maxArea;

    }
    /* Trapping Rain Water - Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
       Check diagram in problem to understand the problem clearly - https://leetcode.com/problems/trapping-rain-water/
       We can solve this by adding up trapped water over every index - water will be trapped over an index(building) only if there exist a building taller in left & right of current
       building. Also water will only go till height of min(leftTaller,rightTaller) and water will be stored above building - So water stored : min(leftTaller,rightTaller) - height(currentBuilding)
       If there are no taller building on left or right side - we will have -1 - we will only consider a building if its has non-negative value on both side.

       Note: Here we want the tallest building on right and left, not the nearest tallest building - So we will add a building to stack only if there is no taller building
       available - because of this our stack will either be empty or contain almost one element - This gives us a chance to replace Stack with a variable instead, which will
       optimize for space complexity but not time. Here we will be using stacks only.
     */
    public int trap(int[] heights) {
        int trappedWater = 0;
        int[] rightTaller = new int[heights.length];
        int[] leftTaller = new int[heights.length];
        Stack<Integer> stack = new Stack<>();

        for(int index = heights.length - 1; index >= 0; index--){
            //Remove everything which is smaller or equal to current building
            while (!stack.isEmpty() && stack.peek() <= heights[index]) stack.pop();
            //Stack is empty - means this is the tallest building till now, add it to stack
            if(stack.isEmpty()) {
                stack.push(heights[index]);
                rightTaller[index] = -1;
            }
            //A taller building exist - populate rightTaller with taller building - We don't need to add currentBuilding to stack as there already is a taller building to right
            else rightTaller[index] = stack.peek();


        }

        stack.clear();

        for(int index = 0; index < heights.length; index++){
            while (!stack.isEmpty() && stack.peek() <= heights[index]) stack.pop();
            //There is no taller building to left - add current building as tallest building
            if(stack.isEmpty()) {
                stack.push(heights[index]);
                leftTaller[index] = -1;
            }
            //Taller building exist on left - populate leftTaller array - no need to add current building to stack as it can never be a solution
            else leftTaller[index] = stack.peek();


            //If there exist a taller building to left and right - add trapped water
            if(rightTaller[index] != -1 && leftTaller[index] != -1){
                trappedWater += Math.min(rightTaller[index], leftTaller[index]) - heights[index];
            }
        }

        return trappedWater;
    }
    /* Car Fleet - https://leetcode.com/problems/car-fleet/
       This question can be reduced to a Stack greater to right question - but this idea won't come to mind right away
       Can watch this -  https://www.youtube.com/watch?v=Pr6T-3yB9RM
       To understand the approach we took
     */
    public static int carFleet(int target, int[] position, int[] speed) {

        Stack<int[]> stack = new Stack<>();
        //Want cars in order of positions from right to left - int[0] : position , int[1] : speed
        PriorityQueue<int[]> priorityQueue = new PriorityQueue<int[]>((a,b) -> b[0] - a[0]);
        for(int index = 0; index < position.length; index++)  priorityQueue.add(new int[]{position[index],speed[index]});

        while (!priorityQueue.isEmpty()){
            int[] currCar = priorityQueue.poll();
            //We add a carFleet to stack only if previous fleet has time lower than current fleet - means they will never intersect to become a single fleet.
            if(stack.isEmpty()) stack.add(currCar);
            else {
                //A Java thing - we need to ass this type case here - else division result will be int and then converted to float.
                float currCarTime = (float) (target - currCar[0])/currCar[1];
                float topCarTime = (float) (target - stack.peek()[0])/ stack.peek()[1];

                if(currCarTime > topCarTime) stack.add(currCar);
            }
        }

        return stack.size();
    }


    /* Valid Parentheses - Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
       An input string is valid if:
        Open brackets must be closed by the same type of brackets.
        Open brackets must be closed in the correct order.
        https://leetcode.com/problems/valid-parentheses/

        This kind of questions are easy to understand, and we will know that we want Stack here.
        Similar question - https://leetcode.com/problems/evaluate-reverse-polish-notation/
     */
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (Character ch : s.toCharArray()){
            if(ch == '(' || ch == '{' || ch == '[' ) stack.add(ch);
            else if(ch == ')'){
                 if(stack.isEmpty() || stack.pop() != '(') return false;
            }
            else if(ch == '}'){
                if(stack.isEmpty() || stack.pop() != '{') return false;
            }
            else if(ch == ']'){
                if(stack.isEmpty() || stack.pop() != '[') return false;
            }
        }

        return stack.isEmpty();
    }

}
