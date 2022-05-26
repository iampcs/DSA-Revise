package RecursionsAndBackTracking;

import java.util.*;
/*
How to identify recursive problem ?
    1. If we have a condition in problem - in which we are provided with choice - and based on those choice we make some decision.
       Those decision now change our choices - makes them smaller.
    2. Intuition - We don't know solution for our problem set, but we know we can get a solution by dividing this problem set into mutually exclusive
       parts and then combine solution from each part to form our solution.

How to solve recursive problems :
    1. Base - Hypothesis - Induction method : This works if we can't see any choice - decision scenarios - When we are forced to use recursion
        Base - This is generally the smallest valid input or the largest invalid input or a destination or a condition that has to be true.
        We will assume that our solution already works for a smaller input - this is Hypothesis
        Induction - Given we already have a solution that works for a smaller input, what will be required to make it work for current input.
            This is similar to how in maths we assume something is true for N-1, then prove it will be true for N too.
        These are generally not tail ending recursions and works similar to BackTracking algorithms.
     2. Input-Output/ Processed-Unprocessed method - We solve this by creating a decision tree around input-output. Each branch in that tree will
        a decision we took based on choices(input + constraints). Each node in that tree will be current input-output. All leaves in this tree
        will be our potential solution. We will reach leaf when we are out of choices, here we will get our Base condition result.
        Recursion  based on this tree is DFS, traversed from left to right.
        This pattern we will be discussing in Subset Patterns.
 */

public class RecursionBaseHypothesisInduction {
    public static void main(String[] args) {

    }

    public static void reverseStack(Stack<Integer> stack){
        // Base Cond - We can't reverse an empty Stack
        if(stack.isEmpty()) return;
        //Nth Element
        Integer top = stack.pop();
        //Hypothesis - Except the top element my whole stack will be reversed by calling it on a smaller input - (N-1) Elements
        reverseStack(stack);
        //Induction - Now that my stack is reversed, Need to put the Nth Element at correct position - at bottom of stack
        insertAtBottom(stack,top);
    }

    private static void insertAtBottom(Stack<Integer> stack, Integer top) {
        //Base condition - We want to insert at bottom, means stack should be empty
        if(stack.isEmpty()) stack.add(top);
        else {
            //Nth Element
            Integer currTop = stack.pop();
            //Hypothesis - Insert at bottom works for N-1 elements, given an element to insert at bottom, we called this method on N-1 elements
            insertAtBottom(stack,top);
            //Induction - Now that our element is inserted at bottom of stack, we can place back the Nth element on top
            stack.add(currTop);
        }
    }

    public static void deleteMiddleElementStack(Stack<Integer> stack, int size){
        //Base - Have we reached the middle of stack? If yes - delete element
        if(stack.size() == size/2 + 1) stack.pop();
        else {
            //Nth Element
            Integer top = stack.pop();
            //Hypothesis - delete Middle elements works for N-1 elements, given a stack and size, we called this method on N-1 elements
            deleteMiddleElementStack(stack,size);
            //Induction - Now that our middle element is deleted, we can place back the Nth element on top
            stack.add(top);
        }
    }


    public static void sortStack(Stack<Integer> stack){
        // Base Cond - We can't sort an empty Stack
        if(stack.isEmpty()) return;
        //Nth Element
        Integer lastElementToSort = stack.pop();
        //Hypothesis - Except the top element my whole stack will be sorted by calling it on a smaller input - (N-1) Elements
        sortStack(stack);
        //Induction - Now that my stack is sorted, Need to put the Nth Element at correct position
        insertAtCorrectPlace(stack, lastElementToSort);

    }

    private static void insertAtCorrectPlace(Stack<Integer> stack, Integer lastElementToSort) {
        // Base Cond - We can't sort an empty Stack, one element stack is always sorted
        if(stack.isEmpty()) {
            stack.add(lastElementToSort);
        }
        //
        else if(stack.peek() > lastElementToSort){
            //Nth Element
            Integer top = stack.pop();
            //Hypothesis - insertAtCorrectPlace will work on a smaller input - (N-1) Elements
            insertAtCorrectPlace(stack,lastElementToSort);
            //Induction - lastElementToSort is added to its correct place, add the Nth element back to stack
            stack.add(top);
        }
        // We are already at correct place
        else
            stack.add(lastElementToSort);
    }
}
