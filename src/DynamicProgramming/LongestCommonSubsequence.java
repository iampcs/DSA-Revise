package DynamicProgramming;
/*
 These are mostly string based problems - where we will be asked to compare two strings or compare with itself and find solution based
 on some conditions. Just like any DP problems, we will have a choice here on current character and based on those choices we will take decisions
 and make recursive calls. Because we move one charter at a time here, we can have repeated sub-problems, this is where DP comes into picture.

 Problem Statement - Given two strings ‘s1’ and ‘s2’, find the length of the longest subsequence which is common in both the strings.

 Subsequence is non-contiguous, Substring is contiguous.
 */
public class LongestCommonSubsequence {
    public static void main(String[] args) {

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








}
