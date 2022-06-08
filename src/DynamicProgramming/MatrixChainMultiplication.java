package DynamicProgramming;

/* Problem : If a chain of matrices is given, we have to find the minimum number of the correct sequence of matrices to multiply.
   The problem is not actually to perform the multiplications, but merely to decide in which order to perform the multiplications.
   Note:no matter how we parenthesize the product, the result will be the same. For example, if we had four matrices A, B, C, and D, we would have:

(ABC)D = (AB)(CD) = A(BCD) = ....
However, the order in which we parenthesize the product affects the number of simple arithmetic operations needed to compute the product, or the efficiency.
For example, suppose A is a 10 × 30 matrix, B is a 30 × 5 matrix, and C is a 5 × 60 matrix. Then,
Cost to multiple matrix M,N with dimension - (a,b),(b,c) is (axbxc)

(AB)C = (10×5x30) + (10×5×60) = 1500 + 3000 = 4500 operations
A(BC) = (30×5×60) + (10×30×60) = 9000 + 18000 = 27000 operations.
Clearly the first parenthesize requires less numb of operations.

We'll be given an array arr[ ] which represents the chain of matrices such that the ith matrix arr[i] is of dimension arr[i-1] x arr[i].

MCM problems are about partitioning - Based on problem, we will partition the problem in sub-problems and then combine solutions from these sub-problems to calculate solution for
current partition. Based on problem - we might need to go through all possible partitions to get our final solution.

We can see here that solving this problem via recursion template is easy - adding memo to it makes the solution solve in O(N^3) - but solving it using tabulation method is not
straight forward - We have to use gap method here to fill the table diagonally. This filling table diagonally makes sense if we have only one string/array input + we can't skip any
input + some combinations don't make sense, so they will be zero or null - dpTable[M][N] saves solution for M&N, but sometimes M&N don't make valid problem set.

 */
public class MatrixChainMultiplication {
    public static void main(String[] args) {

    }

    /* Matrix chain multiplication - You're given an array `dims` of `n` positive integers, where matrix `M[i]` has dimension `dims[i-1] × dims[i]` for `i=1…n`. Determine the optimal parenthesization of the product of matrices `M[1…n]`.
       https://techiedelight.com/practice/?problem=MatrixChainMultiplication
     */
    public static int findOptimalProduct(int[] dims)
    {
        Integer[][] memo = new Integer[dims.length][dims.length];
        //We don't have a zero choice , also even though array size is len, we have len - 1 matrices only
        Integer[][] dpTable = new Integer[dims.length - 1][dims.length - 1];
        int solution = findOptimalProductBruteForce(dims, 1, dims.length-1);
        solution = findOptimalProductMemo(dims,1, dims.length -1, memo);
        solution = findOptimalProductTabulation(dims, dpTable);

        return solution;
    }

    //Time complexity - Exponential
    private static int findOptimalProductBruteForce(int[] dims, int start, int end) {

        //We have one or less matrix in this condition - need at-least two matrices to multiply
        if(start >= end) return  0;
        int minimumCost = Integer.MAX_VALUE;

        //Partitioning from start till end - 1, we need a case where both partition have at-least one matrix
        for(int partition = start; partition < end ; partition++){
            //Cost of left partition - start to partition
            int leftPartitionCost = findOptimalProductBruteForce(dims, start, partition);
            //Cost of right partition - partition + 1 to end
            int rightPartitionCost = findOptimalProductBruteForce(dims, partition + 1, end);
            //Cost to multiply these two partitions
            int cost = dims[start - 1] * dims[partition] * dims[end];
            //Total cost of multiplying with this arrangement of partition
            int totalCost = leftPartitionCost + rightPartitionCost + cost;

            //Storing minimum of all such partitions
            minimumCost = Math.min(minimumCost, totalCost);

        }

        return minimumCost;
    }

    //memo[start][end] stores minimum product cost for start and end
    //Time complexity is O(N^3) - Internally we can have up-to three for loops
    private static int findOptimalProductMemo(int[] dims, int start, int end, Integer[][] memo) {

        //We have one or less matrix in this condition - need at-least two matrices to multiply
        if(start >= end) return  0;
        int minimumCost = Integer.MAX_VALUE;

        if(memo[start][end] != null) return memo[start][end];

        //Partitioning from start till end - 1, we need a case where both partition have at-least one matrix
        for(int partition = start; partition < end ; partition++){
            //Cost of left partition - start to partition
            int leftPartitionCost = findOptimalProductMemo(dims, start, partition,memo);
            //Cost of right partition - partition + 1 to end
            int rightPartitionCost = findOptimalProductMemo(dims, partition + 1, end,memo);
            //Cost to multiply these two partitions
            int cost = dims[start - 1] * dims[partition] * dims[end];
            //Total cost of multiplying with this arrangement of partition
            int totalCost = leftPartitionCost + rightPartitionCost + cost;

            //Storing minimum of all such partitions
            minimumCost = Math.min(minimumCost, totalCost);

        }
        memo[start][end] = minimumCost;

        return memo[start][end];
    }

    //This won't be a simple conversion - time complexity is N^3, we are using a 2D table to cache - whenever this kind of mis-match happens
    //We have a use some additional logic - this additional logic will add N to our solution
    //Here we have changed the way we look into the array - we will be starting from index 0, so first matrix dim[start] - dim[0],dim[1]. Here our end will be second last element
    // dim[end] = dim[dim.length - 2], dim[dim.length - 1]
    private static int findOptimalProductTabulation(int[] dims, Integer[][] dpTable) {

        //For an array      A B C
        //Gap from A to -   0 1 2
        //We are using this way to fill the table diagnostically
        for(int gap = 0; gap < dims.length; gap++){
            // For each possible gap/length
            for(int start = 0 ,end = gap; end < dims.length - 1; start++, end++){
                //Gap is 0 , means one matrix - can't be multiplied
                if(gap == 0) dpTable[start][end] = 0;
                //Gap is 1 - means we have two matrices - M N with dimensions (start,end) (end,end+1)
                else if(gap == 1) {
                    dpTable[start][end] = dims[start] * dims[end] * dims[end + 1];
                }
                else {
                    int minimumCost = Integer.MAX_VALUE;
                    for(int partition = start; partition < end; partition++){
                        //Cost of left partition - start to partition
                        int leftPartitionCost = dpTable[start][partition];
                        //Cost of right partition - partition + 1 to end
                        int rightPartitionCost = dpTable[partition + 1][end];
                        //Cost to multiply these two partitions
                        //Observe the change here from memo - as we have changed the logic to pic dims from dims[i],dims[i - 1] to dims[i], dims[i+1] - by changing the starting point
                        int cost = dims[start] * dims[partition + 1] * dims[end + 1];
                        //Total cost of multiplying with this arrangement of partition
                        int totalCost = leftPartitionCost + rightPartitionCost + cost;

                        //Storing minimum of all such partitions
                        minimumCost = Math.min(minimumCost, totalCost);
                    }
                    //Store the minimum cost for all the partitions, for a given start & end
                    dpTable[start][end] = minimumCost;

                }
            }
        }
        //Hard to draw this here
        //Refer - https://youtu.be/pEYwLmGJcvs?list=PL-Jc9J83PIiE-181crLG1xSIWhTGKFiMY&t=714 to see how we are storing values in dpTable
        return dpTable[0][dims.length - 1];
    }

    /* Palindrome Partitioning II - Given a string s, partition s such that every substring of the partition is a palindrome.
       Return the minimum cuts needed for a palindrome partitioning of s.
       https://leetcode.com/problems/palindrome-partitioning-ii/

     */
    public static int minCut(String s) {

        Integer[][] memo = new Integer[s.length()][s.length()];

        Boolean[][] palindromicTable = createPalindromicTable(s);
        int solution = minCutOptimized(s, palindromicTable);

        return solution;
    }

    //This solution is exactly like MCM - Complexity is O(N^3)
    private static int minCutMemo(String s, int start, int end, Integer[][] memo, Boolean[][] palindromicTable) {
        //If there is only one character or string is already palindrome - no need to cut it further
        if(start >= end || palindromicTable[start][end]) return 0;

        //Have we solved this before?
        if(memo[start][end] != null) return memo[start][end];

        int minCut = Integer.MAX_VALUE;
        //For all possible partitions
        for(int partition = start ; partition < end; partition ++){
            //Have we solved this before? If not solve for left partition
            int leftPartition = memo[start][partition] != null ? memo[start][partition] : minCutMemo(s, start, partition, memo, palindromicTable);
            //Have we solved this before? If not solve for right partition
            int rightPartition = memo[partition + 1][end] != null ? memo[partition + 1][end] : minCutMemo(s,partition + 1, end, memo,palindromicTable);
            //Cutting into left and right is also 1 cur, so adding to total cost
            int totalCuts = leftPartition + rightPartition + 1;

            // Store minimum cuts required from all cuts available for current start & end
            minCut = Math.min(minCut, totalCuts);
        }

        memo[start][end] = minCut;

        return memo[start][end];
    }

    /*
    We will use suffix matching here - We will maintain an array dpTable[index] represent - minimum cut required to make all strings palindrome from (0 - index)
    Whenever we add a new character to our existing solution - suppose at index - J
    1. We will run a loop from J till I - where I goes from J till 1 - Why 1? Because we know dpTable[0] will be 0 - only single element
        In this we will check (J till I) is palindrome? - If yes - We already have solution for dpTable[I] - our solution will be dpTable[I] + 1 - cut at I
        We will find all such cuts and store the minimum of it
    This approach is explained - https://youtu.be/qmTtAbOTqcg?list=PL-Jc9J83PIiE-181crLG1xSIWhTGKFiMY&t=1843
    Time Complexity - O(N^2)
     */
    private static int minCutOptimized(String s, Boolean[][] palindromicTable){
        int[] dpTable = new int[s.length()];
        for(int start = 1; start < s.length() ; start ++){
            //If string (0-start) is already palindrome - 0 cuts required
            if(palindromicTable[0][start]) dpTable[start] = 0;
            else {
                int minCost = Integer.MAX_VALUE;
                //We will start from end and keep increasing suffix size
                for(int suffix = start; suffix >= 1; suffix-- ){
                    //If ((start,end) is palindrome - (end,start) too will be palindrome)
                    if(palindromicTable[suffix][start]){
                        //(start-suffix) is palindrome means - we need to cut this just before suffix and add solution for dpTable[suffix - 1] to get total cuts
                        int cut = dpTable[suffix - 1] + 1;
                        minCost = Math.min(minCost, cut);
                    }
                }

                dpTable[start] = minCost;
            }

        }

        return dpTable[s.length() - 1];
    }

    //Create a palindromic table where palindromicTable[start][end] stores if string between A & B is palindrome
    private static Boolean[][] createPalindromicTable(String s) {

        Boolean[][] palindromicTable = new Boolean[s.length()][s.length()];
        //For an array      A B C
        //Gap from A to -   0 1 2
        //We are taking this approach to fill the table diagnostically
        //How to fill it on paper - https://youtu.be/qmTtAbOTqcg?list=PL-Jc9J83PIiE-181crLG1xSIWhTGKFiMY&t=327
        for(int gap = 0; gap < s.length(); gap++){
            for(int start = 0, end = gap; end < palindromicTable.length; start++, end++){
                if(gap == 0) palindromicTable[start][end] = true;
                else if(gap == 1) palindromicTable[start][end] = s.charAt(start) ==s.charAt(end);
                else {
                    if(s.charAt(start)==s.charAt(end) && palindromicTable[start + 1][end - 1]) palindromicTable[start][end] = true;
                    else palindromicTable[start][end] = false;
                }
            }
        }
        return palindromicTable;
    }

    //TODO : Boolean Parenthesization , Scrambled String, Egg dropping, Bursting Baloon


}


