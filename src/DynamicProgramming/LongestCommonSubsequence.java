package DynamicProgramming;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/*
 These are mostly string based problems - where we will be asked to compare two strings or compare with itself and find solution based
 on some conditions. Just like any DP problems, we will have a choice here on current character and based on those choices we will take decisions
 and make recursive calls. Because we move one charter at a time here, we can have repeated sub-problems, this is where DP comes into picture.

 Problem Statement - Given two strings ‘s1’ and ‘s2’, find the length of the longest subsequence which is common in both the strings.

 Subsequence is non-contiguous, Substring is contiguous.
 */
public class LongestCommonSubsequence {
    public static void main(String[] args) {
        int[] nums = new int[]{2,5,3,7,8,1,9};
        findLBSLength(nums);

    }

    /* Longest Common Subsequence - Given two strings text1 and text2, return the length of their longest common subsequence.
       If there is no common subsequence, return 0.

     */
    public static int longestCommonSubsequence(String text1, String text2) {
        int solution = 0;
        //The two changing values to our recursive function are the two indexes - We could have also use a hash-table whose key would be a string (i1 + “|” + i2))
        Integer[][] memo = new Integer[text1.length() + 1][text2.length() + 1];
        Integer[][] dpTable = new Integer[text1.length() + 1][text2.length() + 1];
        //solution = longestCommonSubsequenceBruteForce(text1,text1.length(), text2,text2.length());
        //solution = longestCommonSubsequenceMemo(text1,text1.length(), text2,text2.length(),memo);
        solution = longestCommonSubsequenceTabulation(text1,text1.length(), text2,text2.length(),dpTable);

        return solution;
    }

    //Time Complexity - In worst case we are making two calls- complexity with be exponential - O(2^m+n)
    public static int longestCommonSubsequenceBruteForce(String S1, int N, String S2, int M){
        //Base Cond - If one of our string has zero length - we can't proceed to compare any further
        if(N ==0 || M==0) return 0;

        //We are traversing backwards here
        //If character matches for both the String at position N & M - This character is part of our solution, add this character count to
        // our solution and recurse for remaining string
        if(S1.charAt(N-1) == S2.charAt(M-1)){
            return 1 + longestCommonSubsequenceBruteForce(S1,N-1, S2, M-1);
        }else {
            //characters are not matching, now we have a choice to make here -
            // Either we skip S1 character and solve for the problem again,
            // or we skip S2 character and solve for the problem again
            // We can't skip both here as we could be skipping onr of our potential solution
            //We don't know skipping which one will get us our solution - so we call both those choices and return the one that gives us max count

            int skipFirtStringCharacter = longestCommonSubsequenceBruteForce(S1,N-1,S2,M);
            int skipSecondStringCharacter =  longestCommonSubsequenceBruteForce(S1,N,S2,M-1);
            return Math.max(skipFirtStringCharacter,skipSecondStringCharacter);
        }
    }

    //Time complexity - O(N*M), that's the size of our memo matrix - we are never going to solve more sub-problem than that
    public static int longestCommonSubsequenceMemo(String S1, int N, String S2, int M, Integer[][] memo){
        //Base Cond - If one of our string has zero length - we can't proceed to compare any further
        if(N ==0 || M==0) return 0;
        //Have we already solved this? Return if solved
        if(memo[N][M] != null) return memo[N][M];

        //Same calls and logic as of brute force
        if(S1.charAt(N-1) == S2.charAt(M-1)){
            memo[N][M] = 1 + longestCommonSubsequenceMemo(S1,N-1, S2, M-1, memo);
        }else{
            int skipFirtStringCharacter = longestCommonSubsequenceMemo(S1,N-1,S2,M, memo);
            int skipSecondStringCharacter =  longestCommonSubsequenceMemo(S1,N,S2,M-1,memo);
            memo[N][M] = Math.max(skipFirtStringCharacter,skipSecondStringCharacter);
        }

        return memo[N][M];
    }


    public static int longestCommonSubsequenceTabulation(String S1, int N, String S2, int M, Integer[][] dpTable){

        for(int s1Index = 0; s1Index <= N; s1Index++){
            for(int s2Index = 0; s2Index <=M; s2Index++){
                //Base Cond - If one of our string has zero length - we can't proceed to compare any further
                if(s1Index == 0 || s2Index == 0) dpTable[s1Index][s2Index] = 0;

                //Character matches for both the String- This character is part of our solution, add this character count to
                // our solution and check for a solution where we don't have these characters
                else if(S1.charAt(s1Index - 1) == S2.charAt(s2Index - 1)){
                    dpTable[s1Index][s2Index] = 1 + dpTable[s1Index-1][s2Index-1];
                }
                //Skip either charter - check for an already solve sub-problem - store maximum of both
                else {
                    int skipFirtStringCharacter = dpTable[s1Index-1][s2Index];
                    int skipSecondStringCharacter =  dpTable[s1Index][s2Index-1];
                    dpTable[s1Index][s2Index] = Math.max(skipFirtStringCharacter,skipSecondStringCharacter);
                }
            }
        }
        return dpTable[N][M];
    }

    /* We get dpTable as input from longestCommonSubsequenceTabulation - Backtrack from solution cell - dpTable[N][M]

     */
    public static String printSubsequence(String S1, int N, String S2, int M, Integer[][] dpTable){
        StringBuilder solution = new StringBuilder();

        //While we both strings
        while(N > 0 && M > 0){
            //If characters are equal at index N,M - We used to add it to solution and check for N-1 and M-1
            //We will be doing the same here too
            if(S1.charAt(N-1) == S2.charAt(M-1)){
                solution.append(S1.charAt(N-1));
                N = N-1;
                M = M- 1;
            }
            //When characters are not equal - we get dpTable[N][M] from max of dpTable[N-1][M], dpTable[N][M-1]
            //We will be backtracking here based on same condition
            else {
                if(dpTable[N-1][M] > dpTable[N][M-1])
                    N = N - 1;
                else
                    M = M - 1;
            }
        }

        // We are appending in reverse order
        return solution.reverse().toString();
    }
    /* Longest Repeating Subsequence - Given a string str, find the length of the longest repeating subsequence such that it can be found twice in the given string.
       The two identified subsequences A and B can use the same ith character from string str if and only if that ith character has different indices in A and B.
       https://practice.geeksforgeeks.org/problems/longest-repeating-subsequence2004/1

       Basically we want longest LCS which also has a duplicate present, and they should not share indices. Knowing how to find LCS, how can
       we modify the algorithm to accommodate this?  We are given only one string here,
       so the second string will also be the fist string.
       How about in step - S1.charAt(s1Index - 1) == S2.charAt(s2Index - 1), if characters match we add it to our solution. But we need two instances
       of same solution here. How can we achieve that? What if we only match a character which are not in same indices? So if we have something like
       0 1 2 3 4 5
       A A B D C C  - i
       A A B D C C  - ii
       LCS - AABACC as all characters are matching
       LRS - AC -   i(0) - ii(1),ii(3)  - Set 1 - A
                    i(1) - ii(0),ii(3)  - Set 2 - A
                    i(2) - Nothing
                    i(3) - Nothing
                    i(4) - ii(5)  - Set 1 - C
                    i(5) - ii(4)  - Set 2 - C

     */
    public int LongestRepeatingSubsequence(String str)
    {
        int N = str.length();
        int M = str.length();
        int[][] dpTable = new int[N+1][M+1];

        for(int s1Index = 0; s1Index <= N; s1Index++){
            for(int s2Index = 0; s2Index <=M; s2Index++){
                //Base Cond - If one of our string has zero length - we can't proceed to compare any further
                if(s1Index == 0 || s2Index == 0) dpTable[s1Index][s2Index] = 0;

                    //Character matches for both the String- This character could be part of our solution if they are not of same index
                    // check for a solution where we don't have these characters
                else if(str.charAt(s1Index - 1) == str.charAt(s2Index - 1) && s1Index != s2Index){
                    dpTable[s1Index][s2Index] = 1 + dpTable[s1Index-1][s2Index-1];
                }
                //Skip either charter - check for an already solve sub-problem - store maximum of both
                else {
                    int skipFirtStringCharacter = dpTable[s1Index-1][s2Index];
                    int skipSecondStringCharacter =  dpTable[s1Index][s2Index-1];
                    dpTable[s1Index][s2Index] = Math.max(skipFirtStringCharacter,skipSecondStringCharacter);
                }
            }
        }
        return dpTable[N][M];
    }

    /* Longest Common Substring - Given two strings. The task is to find the length of the longest common substring.
    https://practice.geeksforgeeks.org/problems/longest-common-substring1452/1/
    This problem is very similar to the longest common subsequence except when we don't have a matching character - we need to reset the count.
    We also need to track this max count as our solution won't be present in dpTable[N][M].
    What is different here is in earlier case even when one of our character was not matching - at that point in our dpTable[N`][M`] - our answer
    could be non-zero, because even though current indexes(N`,M`) are not part of solution they can still be part of whole solution.
    But in this case - if charters are not matching, we know this can never be part of solution, we wil be assigning it to zero.
    Our approach here is not to have an answer till now, but just store individual answers then find out the max
    Eg. This is just one of case to show how we are evaluating indexes - This is not how the flow will - We are skipping some use cases here -
    means we are only showing dpTable{ [0][0], [1][1], [2][2] ..}. There will be other cases set to  0 - dpTable{[0][1],[0,2] ..[5][0],[5][1]}
                    0 1 2 3 4 5
                    A B C D E F
                    A B F D E F
    dpTable[][] =   1 2 0 1 2 3

    Observe how we are increasing count until our characters are matching and resetting once they stop matching. The maximum value here (3), will be
    our solution
     */
    int longestCommonSubstrTabulation(String S1, String S2, int n, int m){
        Integer[][] dpTable = new Integer[n +1][m + 1];
        int maxLength = 0;
        for(int row = 0;row<=n;row++) {
            for(int col =0;col<=m;col++) {
                //Base Cond - If one of our string has zero length - we can't proceed to compare any further
                if(row == 0 || col == 0) {
                    dpTable[row][col] = 0;
                }
                //Character matches for both the String- This character could be part of our solution, increase count
                // and check for a solution where we don't have these characters
                // Check if we have found our solution
                else if(S1.charAt(row -1) == S2.charAt(col -1)) {
                    dpTable[row][col] = 1 + dpTable[row -1][col -1];
                    maxLength = Math.max(maxLength, dpTable[row][col]);
                }
                //No chance of this being our solution - reset counter
                else {
                    dpTable[row][col] = 0;
                }
            }
        }

        return maxLength;
    }

    /* Shortest Common Supersequence (SCS) - Given two strings str1 and str2, return the shortest string that has both str1 and str2 as subsequences.
       If there are multiple valid strings, return any of them.
       https://leetcode.com/problems/shortest-common-supersequence/
       This problem can be solved using two approaches
       1. Using the longest common subsequence -
            1.1. We will get the length of our solution as - M + N - lcs(str1,str2) - This logic is simple to understand - lcs is common among two string
                 use that common part only once. We will get the dpTable[N][M], buy running lcs()
            2.2. Will use this dpTable and fill up our solution in bottom-up manner -
                 * Base - If either is zero - return the other one
                 * If the current characters of N and M are equal, they are part of the SCS, and we move diagonally in the dptable - N-1, M-1
                 * If the current characters of X and Y are different, go up or left, depending on which cell has a higher number - this is same as
                   printSubsequence(), just that we are adding those characters too
                 * We can also find all SCS by tweaking above function a little
       2. We solve this like regular SCS problem - will create a dpTable[N][M], this dpTable will store length of SCS for current N`,M`
          * Once we have this table ready - we will backtrack to print solution

     This problem requires us to print solution - We can either build a recursive solution to create a memo and then another function to
     print this solution, or we can create a tabulation function to create dpTable and use that table to find solution too in same function -
     will be using the tabulation method
     */
    public String shortestCommonSupersequence(String str1, String str2) {

        String solution = shortestCommonSupersequenceLCS(str1, str1.length(), str2, str2.length() );
        solution = shortestCommonSupersequenceTabulation(str1, str1.length(), str2, str2.length());

        return solution;
    }

    private String shortestCommonSupersequenceTabulation(String str1, int N, String str2, int M) {
        Integer[][] dpTable = new Integer[N+1][M+1];
        StringBuilder solution = new StringBuilder();

        for (int row = 0; row <= N; row++) {
            for (int col = 0; col <= M; col++) {
                //Base conditions - If one string is empty - the shortest length of solution will be current length of other string
                if(row == 0)
                    dpTable[row][col] = col;
                else if (col == 0 )
                    dpTable[row][col] = row;
                //If characters match we only need to include that character once in our solution
                //If we already have solution length of N`,M` and we add same character to both the string - we only need to add it once in our
                //solution string - so length increased by 1
                //Eg.   A B C D  - S1 , A C D    - S2
                // SCS - ABCD of length 4 , if we add 'E' to both the string - SCS will become ABCDE hence increasing length by 1
                else if (str1.charAt(row - 1) == str2.charAt(col - 1)) {
                    dpTable[row][col] = 1 + dpTable[row - 1][col - 1];
                }
                // Now characters are not matching - We have a choice - I can add both these characters to my solution and move on.
                //But there could be a chance if adding one will do. In that case we check addling which one will give us the minimum length
                //Eg. ABCD - S1 , ABD - S2
                // SCS - Till AB it will add to solution as characters are same. But at index 2, we have C & D. We can either add both C & D
                // to pur solution and move to index 3 - but this will increase the SCS length - So we are not even considering this option
                //We have two options
                //We can add only C to SCS and check if D is taken care of in future - As we add a character we add 1
                //We can add only D to SCS and check if C is taken care of in future - As we add a character we add 1
                //We took the option which gives the minimum SCS length
                else {
                    dpTable[row][col] = Math.min(dpTable[row - 1][col] + 1, dpTable[row][col - 1] + 1);
                }
            }
        }

        //While we both strings
        while(N > 0 && M > 0){
            //If characters are equal at index N,M - We used to add it to solution and check for N-1 and M-1
            //We will be doing the same here too
            if(str1.charAt(N-1) == str2.charAt(M-1)){
                solution.append(str1.charAt(N-1));
                N = N-1;
                M = M- 1;
            }
            //When characters are not equal - we get dpTable[N][M] from min of dpTable[N-1][M] + 1, dpTable[N][M-1] + 1
            //We will be backtracking here based on same condition
            //Add current string to solution
            else {
                if(dpTable[N-1][M] < dpTable[N][M-1]){
                    solution.append(str1.charAt(N-1));
                    N = N - 1;
                }
                else{
                    solution.append(str2.charAt(M-1));
                    M = M - 1;
                }

            }
        }

        // One of string is empty - add other one as it is
        while (N > 0) {
            solution.append(str1.charAt(N-1));
            N = N-1;

        }

        while (M > 0) {
            solution.append(str2.charAt(M-1));
            M = M - 1;
        }

        // As we are traversing and adding solution backwards - reverse it before returning
        return solution.reverse().toString();


    }

    private String shortestCommonSupersequenceLCS(String str1, int N, String str2, int M) {
        Integer[][] dpTable = new Integer[N+1][M+1];
        //Will get us the dpTable
        longestCommonSubsequenceTabulation(str1,N,str2,M,dpTable);
        StringBuilder solution = new StringBuilder();

        //While we both strings
        while(N > 0 && M > 0){
            //If characters are equal at index N,M - We used to add it to solution and check for N-1 and M-1
            //We will be doing the same here too
            if(str1.charAt(N-1) == str2.charAt(M-1)){
                solution.append(str1.charAt(N-1));
                N = N-1;
                M = M- 1;
            }
            //When characters are not equal - we get dpTable[N][M] from max of dpTable[N-1][M], dpTable[N][M-1]
            //We will be backtracking here based on same condition
            //Add current string to solution
            else {
                if(dpTable[N-1][M] > dpTable[N][M-1]){
                    solution.append(str1.charAt(N-1));
                    N = N - 1;
                }
                else{
                    solution.append(str2.charAt(M-1));
                    M = M - 1;
                }
            }
        }

        // One of string is empty - add other one as it is
        while (M > 0) {
            solution.append(str2.charAt(M-1));
            M = M - 1;
        }
        while (N > 0) {
            solution.append(str1.charAt(N-1));
            N = N-1;

        }
        // As we are traversing and adding solution backwards - reverse it before returning
        return solution.reverse().toString();
    }

    /*
    Minimum number of deletions and insertions
    Given two strings str1 and str2. The task is to remove or insert the minimum number of characters from/in str1 to transform it into str2.
    https://practice.geeksforgeeks.org/problems/minimum-number-of-deletions-and-insertions0209/1#
    OR
    Delete Operation for Two Strings
    https://leetcode.com/problems/delete-operation-for-two-strings/

    Both the problems are same as they are asking for number of operations. This problem can be simplified to find LCS for both the strings only
    and then doing operation on LCS. As we know LCS is the longest common subsequence - it will require the least amount of operation.
    Eg. HEAP - str1, EAP - str2 , LCS - EA
    For first problem  - str1 -> Remove HP -> EA -> Add E -> EAP
    For second problem - str1 -> Remove HP -> EA <- Remove E <- EAP
     */
    public int minOperations(String str1, String str2){
        Integer[][] dpTable = new Integer[str1.length() + 1][str2.length() + 1];
        int lcs = longestCommonSubsequenceTabulation(str1, str1.length(), str2, str2.length(), dpTable);

        return str1.length() +  str2.length() - 2 * lcs;
    }

    /* Longest Palindromic Subsequence - Given a string s, find the longest palindromic subsequence's length in s.
       https://leetcode.com/problems/longest-palindromic-subsequence/

       This can be solved using two approaches -
       1. Using LCS code - If we try to find LCS for a string and its reverse - that LCS will be palindromic - Why ?
          Because if something matches in front - it is guaranteed to match towards end too as the string is reversed.
       2. Regular recursion approach - We will traverse the same string from left & right index - dpTable[left`][right`] - gives us the length
          of the longest palindromic subsequence till left` & right` are traversed - so (0-left`) & (strLen - right`)
          2.1. If characters match - check for (left + 1, right -1) + 2 - adding 2 as both these characters will be there in our solution
          2.2 If left == right - add 1 - a single character is always palindromic - middle of string
          2.3 If characters don't match - max of choices - ( skipping left, skipping right ) - max ((left+1, right), (left, right - 1))
     */
    public int longestPalindromeSubseq(String s){
        Integer[][] dpTable = new Integer[s.length() + 1][s.length() + 1];
        Integer[][] memo = new Integer[s.length() + 1][s.length() + 1];
        StringBuilder sb = new StringBuilder(s);
        String revS = sb.reverse().toString();
        int lcsApproachSolution = longestCommonSubsequenceTabulation(s,s.length(), revS, revS.length(), dpTable);
        int recursionApporoachSolution = longestPalindromeSubseqMemo(s,0,s.length() - 1, memo);

        return recursionApporoachSolution;
    }

    private int longestPalindromeSubseqMemo(String s, int leftIndex, int rightIndex, Integer[][] memo) {
        //Base Cond - We have crossed half mark path, no need to traverse further
        if(leftIndex > rightIndex) return 0;
        //Base Cond - Either single length string or we are at middle - single length is always palindrome - return 1
        if(leftIndex == rightIndex) return 1;

        //Have we already solved this ? Return if true
        if(memo[leftIndex][rightIndex] != null) return memo[leftIndex][rightIndex];

        //Characters matching - this will be part of my solution - Add 2, recurse for rest of string
        if(s.charAt(leftIndex) == s.charAt(rightIndex)){
            memo[leftIndex][rightIndex] = 2 + longestPalindromeSubseqMemo(s,leftIndex + 1, rightIndex - 1, memo);
        }
        //Last and First characters not matching - we have a choice now - either skip first and continue - or skip last and continue
        //We will perform both and pick the one giving us the longest solution
        else {
            int skipLeftCharacter = longestPalindromeSubseqMemo(s, leftIndex + 1, rightIndex, memo);
            int skipRightCharacter = longestPalindromeSubseqMemo(s, leftIndex, rightIndex - 1, memo);

            memo[leftIndex][rightIndex] = Math.max(skipLeftCharacter, skipRightCharacter);
        }
        return memo[leftIndex][rightIndex];
    }

    /* Minimum Insertion Steps to Make a String Palindrome - Given a string s.
       In one step you can insert or delete or insert/delete any character at any index of the string.
       Return the minimum number of steps to make s palindrome.
       https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
       https://www.geeksforgeeks.org/minimum-number-deletions-make-string-palindrome/

       Whether its deletion or insertion or both, approach is same - find the longest palindromic subsequence, one you have that -
       you can either remove all the remaining characters to make string palindrome or add same characters again in correct order to make the
       string palindrome.
     */
    public int minInsertions(String s) {
        Integer[][] memo = new Integer[s.length() + 1][s.length() + 1];
        int longestPalindromeSubseq = longestPalindromeSubseqMemo(s,0,s.length() - 1, memo);

        return s.length() - longestPalindromeSubseq;
    }

    /* Longest Palindromic Substring - Given a string s, return the longest palindromic substring in s.
       https://leetcode.com/problems/longest-palindromic-substring/

       This kind of questions are tricky - specially if you have already solved subsequence problems. Approach will be completely
       different here. We can solve this problem using two ways as both gives O(N^2 solution)
       1. Using DP- tabulation method
       2. Using iteration - expand method - given a character expand left-right to check if it's a part of a palindrome 

     */
    public String longestPalindrome(String s) {
        //dpTable[start][end] stores if the substring (start,end) is palindromic
        // There is just one string, doesn't make sense here to keep the case of zero length string
        //Using boolean as default value for boolean is false
        boolean[][] dpTable = new boolean[s.length()][s.length()];
        String solution = longestPalindromeTabulation(s,dpTable);
        solution = longestPalindromeExpandMethod(s, s.length());

        return solution;
    }

    private String longestPalindromeExpandMethod(String s, int length) {
        
        if(length < 2) return s;
        // palindromeVar[0] =palindromeStartIndex ,  palindromeVar[1] = palimdromeMaxLen
        int[] palindromeVar = new int[]{0,0};
        
        for (int index = 0; index < length-1 ; index++ ){
            expandPalindrome(s, index , index, palindromeVar);
            expandPalindrome(s, index, index + 1, palindromeVar);
        }
        
        return s.substring(palindromeVar[0], palindromeVar[0] + palindromeVar[1]);
    }

    private void expandPalindrome(String s, int low, int high, int[] palindromeVar) {

        //Expand in both direction until characters are matching
        while (low >= 0 && high < s.length() && s.charAt(low) == s.charAt(high)){
            low = low -1;
            high = high + 1;
        }

        //Revert last while to get actual value of start-end of palindromic string
        low = low + 1;
        high = high - 1;

        //If new length is greater than maxLen - update
        if(high - low + 1> palindromeVar[1] )
        {
            palindromeVar[0] = low ;
            palindromeVar[1] = high - low + 1;
        }
    }

    private String longestPalindromeTabulation(String s, boolean[][] dpTable) {

        int palindromeStartIndex = 0;
        int palimdromeMaxLen = 0;

        //start signifies start of our string in consideration - we are traversing from end - increasing length by one at a time
        for(int start = s.length() - 1; start >= 0; start --){
            //For a given start - we need to find the max palindrome within this window of (start-end)
            for (int end = start; end < s.length(); end++){
                //Character match - this string will be part of our solution
                if(s.charAt(start) == s.charAt(end)){
                    //will be set to true
                    dpTable[start][end] = (end - start < 3) //This is a small optimization - if length is less than or equal to 3, only end characters should match
                                            || dpTable[start + 1][end - 1]; // now that end characters are matching - check if this substring (start + 1, end - 1 ) was palindrome too
                }
                //Update start of palindrome substring and length if we found a longer palindrome - this can be modified to store palindromes too
                if(dpTable[start][end] && (end - start + 1 > palimdromeMaxLen)){
                    palindromeStartIndex = start;
                    palimdromeMaxLen = end - start + 1;
                }
            }
        }

        return s.substring(palindromeStartIndex,palindromeStartIndex + palimdromeMaxLen);
    }
    /* Subsequence Pattern Matching - Given a string and a pattern, write a method to count the number of times the pattern appears in the string as a subsequence.
       https://techiedelight.com/practice/?problem=PatternMatchIV
       This problem is very much similar to LCS - but here we want one word - pattern, to be completely a subsequence of another word.
       Brute force approach will be - match the pattern with the given string one character at a time
        1. If character matches - recursively match for the remaining lengths of the pattern and the word.
        2. At every step, we can always skip a character from the word to try to match the remaining string with the pattern. Start a recursive call by skipping one character from the word.
        3. Total counts will be sum of both of above calls - Why does this works?  Because we are making a call where we are skipping only word and not pattern - So there will
            always be a case where we are searching for a whole pattern on each starting/ending character of word.

        For Memo approach - memo[N`][M`] - stores total count of pattern till M` in word till N`
     */
    public static int numberOfPatternInString(String word, String pattern)
    {
       int solution = 0;
       Integer[][] memo = new Integer[word.length() + 1][pattern.length() + 1];
       solution = numberOfPatternInStringMemo(word, word.length(), pattern, pattern.length(), memo );

       return solution;
    }

    private static int numberOfPatternInStringMemo(String word, int N, String pattern, int M, Integer[][] memo) {
        // This ordering is important - What we have a situation where last character is remaining for word and patter and it's a match ?
        // Recursion call for N-1 will be called and returned zero - hence check for pattern length to be zero first
        if(M == 0) return 1;
        if(N == 0 ) return  0;

        //Have we already solved this problem ?
        if(memo[N][M] != null) return memo[N][M];

        //Character match - We have a choice - recurse for remaining length of word and pattern or skip this character from word
        //Total number of possible solutions with be sum of both
        if(word.charAt(N-1) == pattern.charAt(M-1)){
            int chooseChar  = numberOfPatternInStringMemo(word, N-1, pattern, M-1, memo);
            int skipChar = numberOfPatternInStringMemo(word, N-1, pattern, M , memo);

            memo[N][M] = chooseChar + skipChar;
        }
        // We don't have a choice - skip word character and recurse for remaining word
        //I can always take the common part of skipping from both matching & non-matching to make the code small - But this is more intuitive - shows me that I had a choice
        //I choose based on those choice and that is what overlaps the problem sub-set - Fits the template of recursion
        else{
            int skipChar = numberOfPatternInStringMemo(word, N-1, pattern, M , memo);
            memo[N][M] = skipChar;
        }

        return memo[N][M];
    }

    /* Longest Increasing Subsequence - Given an integer array nums, return the length of the longest strictly increasing subsequence.
       https://leetcode.com/problems/longest-increasing-subsequence/
       dpTable[n`] - means the longest increasing subsequence ending at n
       Each number individually is a LIS in itself - so we fill the dpTable with 1

     */
    public static int lengthOfLIS(int[] nums, int[] dpTable) {
        // dpTable.length will be nums.length
        //Each number individually is a LIS in itself - so we fill the dpTable with 1
        Arrays.fill(dpTable,1);
        int lis = 1;

        //First for loop is to include one number at time and calculate its LIS
        for(int i = 0; i < dpTable.length; i++){
            //For each number included from above loop - we will check if it can be a part of any existing LIS - if yes we will store the maximum
            for(int j=0; j<i; j++){
                //If my current number is greater than any previous number, I can be a part of this numbers LIS
                //Take max of exiting LIS , will be 1 initially, LIS of which [i] can be part of + 1(i itself)
                if(nums[i] > nums[j]){
                    dpTable[i] = Math.max(dpTable[i], dpTable[j]+1);
                    //If I found a longer LIS for current number  - store it
                    if(lis < dpTable[i])
                        lis = dpTable[i];
                }
            }
        }
        return lis;
    }

    /* Maximum sum increasing subsequence - Given an array of n positive integers. Find the sum of the maximum sum subsequence of the given array such that the
       integers in the subsequence are sorted in increasing order i.e. increasing subsequence.
        https://practice.geeksforgeeks.org/problems/maximum-sum-increasing-subsequence4749/1#
        Same logic as above - LIS. Instead of length we are maximizing sum now, so instead of length of each number  i.e 1, we will be using the number itself
     */
    public int maxSumIS(int arr[], int n)
    {
        int dpTable[] = new int[n];
        for(int i = 0; i< n ; i++) dpTable[i] = arr[i];
        int liss = arr[0];

        //First for loop is to include one number at time and calculate its LISS
        for(int includedNumbers = 0; includedNumbers < n; includedNumbers++){
            //For each number included from above loop - we will check if it can be a part of any existing LISS - if yes we will store the maximum
            for(int previousCalculatedLISS=0; previousCalculatedLISS<includedNumbers; previousCalculatedLISS++){
                //If my current number is greater than any previous number, I can be a part of this numbers LISS
                //Take max of exiting LISS , will be arr[i] initially, LISS of which [i] can be part of + arr[i](i itself)
                if(arr[includedNumbers] > arr[previousCalculatedLISS]){
                    dpTable[includedNumbers] = Math.max(dpTable[includedNumbers], dpTable[previousCalculatedLISS]+arr[includedNumbers]);
                    //If I found a greater LISS for current number  - store it
                    if(liss < dpTable[includedNumbers])
                        liss = dpTable[includedNumbers];
                }
            }
        }
        return liss;
    }

    /* Minimum number of deletions to make a sorted sequence - Given an array arr of size N, the task is to remove or delete the minimum number of elements from the
       array so that when the remaining elements are placed in the same sequence order form an increasing sorted sequence.
       https://practice.geeksforgeeks.org/problems/minimum-number-of-deletions-to-make-a-sorted-sequence3248/1/#
       This problem looks like a diff problem, but is an extension of LIS only. For a given array - LIS gives you the list of the longest increasing subsequence, if we remove
       all others elements which are not part of this LIS, we will have a sorted array.
     */
    public int minDeletions(int nums[], int n)
    {
        int[] dpTable = new int[n];
        int LIS = lengthOfLIS(nums, dpTable);
        //Delete length of elements not part of LIS
        return n - LIS;

    }
    /* Longest Bitonic Subsequence - A subsequence is considered bitonic if it is monotonically increasing and then monotonically decreasing.
       https://techiedelight.com/practice/?problem=LongestBitonicSubsequence
       How can we use LIS here? What we want is for each index i - We want the longest decreasing sequence towards its left, the longest decreasing sequence towards its right
       and add itself to both - 1 + LDS-left + LDS-right
       Now, if we calculate LIS from left to right , at any index i , i to 0 will be LDS-left
       Now if we calculate LIS from right to left , at any index i, i to array.length will be LDS-right
       Eg. If we take the index 4(8), 4 + 2 -1 = 5 will be the longest Bitonic subsequence
              2 5 3 7 8 1 9
        LIS-  1 2 2 3 4 1 5
        RLIS- 2 3 2 2 2 1 1
     */
    public static int findLBSLength(int[] nums)
    {
        int solution = 0;
        int[] reverseNums = new int[nums.length];
        for(int i = 0; i< reverseNums.length; i++){
            reverseNums [i] = nums[nums.length - i - 1];
        }

        int[] LIS_leftToRight = new int[nums.length];
        lengthOfLIS(nums, LIS_leftToRight);

        //We don't have a function to calculate LIS from right to left  - so we are reversing the array and calculating
        int[] LIS_rightToLeft = new int[nums.length];
        lengthOfLIS(reverseNums, LIS_rightToLeft);

        for (int i = 0; i < nums.length; i++){
            //We could either reverse the LIS_rightToLeft array or read from opposite index
            if(LIS_leftToRight[i] + LIS_rightToLeft[nums.length - 1 - i] - 1 > solution)
                solution = LIS_leftToRight[i] + LIS_rightToLeft[nums.length - 1 - i] - 1;
        }
        return solution;

    }




    }
