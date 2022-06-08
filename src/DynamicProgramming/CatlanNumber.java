package DynamicProgramming;
/*
Similar to Fibonacci numbers , can be used to solve various problems.
C0 - 1
C1 - 1
C2 - C0C1 + C1C0 - 2
.
.
CN - C0CN-1 + C1CN-2 + C2CN-3 + . . . . + CN-1C0

From the look of any question based on catalan numbers, it's impossible to know we can use catalan numbers here -
One hint will be asking for total ways/count
Then solve the problem for till N = 4 and match if it's forming a
catalan number pattern - When you are solving you will observe that based on what decision we are taking we will partition the inputs - and they will be sub-problems we have
already solved
C0 - 1  - This doesn't make sense for some problems - Just assume it as 1(Base for multiplication)
C1 - 1
C2 - 2
C3 - 5
C4 - 14
C5 - 42
 */

public class CatlanNumber {
    public static void main(String[] args) {
        System.out.println(catlanNumber(5));
    }

    //Find Nth Catalan Number
    public static int catlanNumber(int N){
        int[] dpTable = new int[N+1];

        //Base Conditions
        dpTable[0] = 1;
        dpTable[1] = 1;


        for(int n = 2; n< dpTable.length; n++){
            //Inner loop starts from 0 and goes till n-1
            for(int j = 0; j < n; j++){
                //CN - C0CN-1 + C1CN-2 + C2CN-3 + . . . . + CN-1C0
                dpTable[n] += dpTable[j] * dpTable[n - j - 1];
            }
        }

        return dpTable[N];
    }

    /* Unique Binary Search Trees - Given an integer n, return the number of structurally unique BST's (binary search trees) which has exactly n nodes of unique values
       from 1 to n.
       https://leetcode.com/problems/unique-binary-search-trees/
       Formation of BST for a given N follows Catalan number pattern
       How BST are formed from 0 to N can bee seen here - https://youtu.be/H1qjjkm3P3c?t=71
     */
    public int numTrees(int N){
        return catlanNumber(N);
    }

    /* Counting Valleys And Mountains - You are given a number n, representing the number of upstrokes / and number of downstrokes . You are required to find the number of valleys and mountains you can create using strokes.
        At no point should we go below the sea-level. (number of downstrokes should never be more than number of upstrokes).
        0 - 1 {There is just 1 way to do nothing}
        1 - /\ - 1
                   /\
        2 - /\/\  /  \  - 2
                                           /\
                      /\   /\     /\/\    /  \
        3 - /\/\/\ /\/  \ /  \/\ /    \  /    \ - 5
        https://www.pepcoding.com/resources/data-structures-and-algorithms-in-java-levelup/dynamic-programming/count-valleys-mountains-official/ojquestion

        This problem can also be seen as taking a pair of strokes and then deciding if we want to place next pair inside of outside the first pair - this first pair
        partitions the problem.
        We can see a catalan numbers forming - to see how to form for a new N using existing N-1 ways - or evaluate forming for a new N as a function of 0-N-1 - can check
        https://youtu.be/hM_FJnrP1kk?t=169
     */
    public int countMountainValley(int N){
        //Although this can be solved by simply returning catalanNumber function - Will try to solve this using inside/outside terminology

        int[] dpTable = new int[N + 1];
        //Base cond - Only one way to create valley & mountain given 0,1 strokes
        dpTable[0] = 1;
        dpTable[1] = 1;

        for(int strokes = 2; strokes < dpTable.length; strokes ++){
            //                                                                                         /\
            //We have n strokes now, taking the nth pair - / \ - we can either next n-1 pair inside - /  \or outside - /\/\ - we will do this for all possible combination of inside
            // & outside - These inside & outside solutions are less than current stroke numbers - means they are already solved
            int inside = strokes - 1;
            int outside = 0;

            while (inside >= 0){
                dpTable[strokes] += dpTable[inside] * dpTable[outside];
                inside --;
                outside ++;
            }
        }
        return dpTable[N];
    }

    /* Count valid Balanced Parentheses - You are given a number n, representing the number of opening brackets and closing brackets.
       You are required to find the number of ways in which you can arrange the brackets if the closing brackets should never exceed opening brackets - or all valid parentheses
       for 1, answer is 1 -> ()
       for 2, answer is 2 -> ()(), (())
       for 3, answer is 5 -> ()()(), () (()), (())(), (()()), ((()))

       We have already solved this - RecursionsAndBackTracking.RecursionSubsetsPattern.generateParenthesis(int)
       There we were generating all valid combinations - here we need to give count of valid combinations
       This problem is exactly similar to mountain & Valley problem solved above - We can place a new parenthesis inside a given pair
       or outside.
     */

    public int countValidParentheses(int N){
        return countMountainValley(N);
    }

    /* Non-Intersecting Chords in a Circle - You are given 2*N points on a circle. You have to draw N non-intersecting chords on a circle.
       You have to find the number of ways in which these chords can be drawn
       There is no way anyone can associate catalan number here unless they try to find answer for N = 1,2,3 and see its matching catalan numbers
       Whenever we introduce Nth chord, we divide the circle into two parts - sub-problems
       We already have solution from those two parts. Suppose P1 - part 1, P2 - part 2 , we introduced Nth chord - to find F(N)
            P1           P2          Solution
points :  N-1 * 2        0           F(N-1) + F(0)
          N-2 * 2        2           F(N-2) + F(1)
          .
          .
          0             N-1 * 2      F(0)   + F(N-1)

     */

    public int countChord(int N){
        return catlanNumber(N/2);
    }

    /* Number Of Ways Of Triangulation - You are given a number N, which represents the number of sides in a polygon.
       You have to find the total number of ways in which the given polygon can be triangulated - Number of ways of converting polygon to
       set of triangles.
       https://www.pepcoding.com/resources/data-structures-and-algorithms-in-java-levelup/dynamic-programming/number-of-ways-of-triangulation-official/ojquestion

       Minimum number of sides to triangulate is 3
       Polygon Sides(N)   Triangles       Catalan Number
              2              1                 C0          -> This case here doesn't make sense - We are just assigning multiplayer base - 1
              3              1                 C1
              4              2                 C2
              5              5                 C3
              6              14                C4
              7              42                C5   -> https://en.wikipedia.org/wiki/Polygon_triangulation#/media/File:Polygon_Triangulations_(heptagon).svg

      We can observe that N sided polygon can be triangulated N-2 catalan number
      Just like the chord example - We can observe that a straight line - which will be used to create triangle - divides the polygon into
      smaller polygons - which we have already solved
     */
    public int countPolygonTriangulation(int N){ return catlanNumber(N-2); }
}
