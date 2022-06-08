package DynamicProgramming;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* These are some problems which are not part of any pattern. But we will use Dynamic programming here to improve on
   complexity.
   Also contains some problems with neat tricks.
 */
public class Misc {
    public static void main(String[] args) {

    }
    /* Word Break - Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.
   Note that the same word in the dictionary may be reused multiple times in the segmentation.
   Ex. Input: s = "leetcode", wordDict = ["leet","code"]
       Output: true
       Explanation: Return true because "leetcode" can be segmented as "leet code".
   https://leetcode.com/problems/word-break/
 */
    public boolean wordBreak(String s, List<String> wordDict) {
        //Will move dictionary to a set for faster search
        //Because the whole string should part of solution - starting from start index
        //meme[index] will contain info if string from (index, string.length() - 1) can be converted to a series of words
        return wordBreak(s, 0, new HashSet<>(wordDict), new Boolean[s.length()]);
    }

    private boolean wordBreak(String s, int index, Set<String> wordDict, Boolean[] memo) {
        //Reached end of string - means all characters in string were part of dictionary - return true
        if(index == s.length()) return true;
        //Have we solved for this index already ?
        if(memo[index] != null) return memo[index];
        //Neat trick - instead of going letter by letter from string and checking if it's a part of dictionary - check if from present index, do we have any word from
        //dictionary - If yes - extract word - recurse for rest of string
        for(String word : wordDict) {
            if(index + word.length() > s.length()) continue;
            String matchingWord = s.substring(index, index + word.length());
            //Word is matching - recurse for rest of string - if we get a true - return immediately - no need to search further - we only need single occurrence
            if(word.equals(matchingWord) && wordBreak(s, index + word.length(), wordDict, memo)) {
                //Above conditions are true - it's possible to form words from this index of string
                //As you know this will be evaluated in backward direction - so memo[0] will be the last one to be evaluated and return
                memo[index] = true;
                return true;
            }
        }
        //Not possible from this index - go back and try some other word
        memo[index] = false;
        return false;
    }

    /* Knight Dialer - The chess knight has a unique movement, it may move two squares vertically and one square horizontally, or two squares horizontally and one square vertically (with both forming the shape of an L).
       We have a chess knight and a phone pad as shown below, the knight can only stand on a numeric cell (i.e. blue cell).
       Given an integer n, return how many distinct phone numbers of length n we can dial.
       You are allowed to place the knight on any numeric cell initially, and then you should perform n - 1 jumps to dial a number of length n.
       All jumps should be valid knight jumps. As the answer may be very large, return the answer modulo 109 + 7.
       https://leetcode.com/problems/knight-dialer/


     */
    public static final int MOD = 1000000007;
    public int knightDialer(int n) {
        //Creating a dialer and all possible directions knight can move to
        int[][] dialer = new int[][]{{1,2,3},{4,5,6},{7,8,9},{-1,0,-1}};
        int[] xDir = new int[]{1,1,-1, -1, 2, 2, -2 ,-2};
        int[] yDir = new int[]{2,-2, 2, -2 , -1, 1, -1, 1};

        int totalSum = 0;
        //Our memo table will contain info on [jumps,num] - means if we have n jumps left, and we are at number num - how many possible numbers you can form
        //We know that we can reach to same number and same number of jumps left for many cases - so caching it
        Integer[][] memo = new Integer[n+1][10];
        //Try to start from all the numbers on dialer
        for(int row = 0; row < dialer.length; row++){
            for(int col = 0; col < dialer[0].length; col++){
                if(dialer[row][col] != -1){
                    //Recurse for n-1 jumps
                    totalSum = (totalSum +  knightDialer(dialer,row,col,n-1, memo,xDir,yDir)) % MOD;
                }
            }
        }

        return totalSum;
    }

    private int knightDialer(int[][] dialer, int row, int col, int jumps, Integer[][] memo, int[] xDir, int[] yDir) {
        //Base conditions
        if(row < 0 || col < 0 || row >= dialer.length || col >= dialer[0].length || dialer[row][col] == -1) return 0;
        //No more jumps left - we have found a possible number - return 1
        if(jumps == 0) return 1;
        //Have we already solved for this?
        if(memo[jumps][dialer[row][col]] != null) return memo[jumps][dialer[row][col]];
        //For starting as [row][col] - how many numbers we can form with 'jumps' jump
        int currSum = 0;
        //Try all directions
        for(int dir = 0; dir < xDir.length; dir++){
            //Move to new direction
            row = row + xDir[dir];
            col = col + yDir[dir];
            //Used one jump for moving - recurse from jump - 1 and new start position
            currSum = (currSum +  knightDialer(dialer,row,col, jumps - 1, memo,xDir,yDir)) % MOD;
            //Backtrack
            row = row - xDir[dir];
            col = col - yDir[dir];

        }

        memo[jumps][dialer[row][col]] = currSum;
        return memo[jumps][dialer[row][col]];
    }


}

