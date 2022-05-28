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


}

