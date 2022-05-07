package Stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/* Stack questions will always be optimization problems - These problems could also be solved using brute-force, but with stacks we can do this
   in linear time. First few problems will cover basic on how to think in terms of stacks - how we can save on multiple traversal costs.
   Using this basics we will try to solve actual problems
 */

public class Stacks {
    public static void main(String[] args) {

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


    /* Valid Parentheses - Given a string s containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.
       An input string is valid if:
        Open brackets must be closed by the same type of brackets.
        Open brackets must be closed in the correct order.

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
