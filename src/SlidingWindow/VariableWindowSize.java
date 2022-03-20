package SlidingWindow;

/*
Identify a Sliding window problem :
1. Array or String based
2. Solution resolves around either sub-array or sub-string - Should be contiguous
3. There will be a condition mentioned - K , based on which we vary our window that contains satisfies the condition

Template:

1. Data structure to contain solution + Supporting data structure to quantify condition
2. start and end of window pointers initialized to 0
3. While window has not reached end of input array - end < input.length
    3.1. Add input[end] to window - Do required calculations to supporting data structure
    3.2. Did we reached window size? present-condition < K (conditioned mentioned) - just increment window size(end++) and continue
    3.3. We reached window size present-condition == K is true - increment window size(end++)
        3.3.1. Calculate solution for current window
        3.3.2. Increment window size(end++) and continue;
    3.4. We have gone beyond window size now -  present-condition > K - Slide the window
        3.4.1. While condition > K
            3.4.1.1. Remove input[start] from window - This can be a single operation or a series of operations
            3.4.1.2. Slide window front - start++;
        3.4.2. Slide Window rear - end++
4. Return solution

 */
public class VariableWindowSize {
    public static void main(String[] args) {

    }
}
