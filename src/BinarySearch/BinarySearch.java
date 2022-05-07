package BinarySearch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalInt;

/* Whenever we are given a sorted Array or LinkedList or Matrix, and we are asked to find a certain element, the best algorithm we can use is the Binary Search
   It's not always the case that DS will be linearly sorted, there could be variations - but even then we can use BinarySearch to optimize the solution from O(N) to O(logN).
   Although the algorithm is simple, we are prone to make Off By One error a lot - so take special care of indexes.

   Sometimes binary search can be used to optimize a brute force solution - Suppose for a problem - we have no other way to solve it other than trying all possible solution from
   (min to max). In this case our complexity will be O( max * something), we can optimize this a bit by not trying from min to max but apply binary search on this range to find
   optimal solution.
   So instead of going - min , min + 1 . . . max -1, max
   We go - (min + max)/2 . . (min + max)/2 +/- 1
   This will reduce our complexity to O(log(max) * something)

 */
public class BinarySearch {
    public static void main(String[] args) {
    }

    /* Binary Search - Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums.
       If target exists, then return its index. Otherwise, return -1.
       https://leetcode.com/problems/binary-search/
       Idea is simple - match with middle element - reduce array size based on comparison result.
     */

    public int search(int[] nums, int target) {
        int start = 0;
        int end = nums.length - 1;
        // When start or end is large, (start + end)/2 will give us the wrong result due to integer overflow
        int middle  = start + (end-start)/2;

        while(start <= end){
            if(nums[middle] == target) return middle;
            if(nums[middle] < target) start = middle + 1;
            else end = middle - 1;
            middle = (start + end)/2;
        }
        return -1;
    }
    // Recursive Binary Search
    public int binarySearch(int[] nums, int start , int end , int target){
        if(start > end) return -1;
        int mid = start + (end - start)/2;

        if(nums[mid] == target) return  mid;
        else if(nums[mid] > target) return binarySearch(nums, start, mid - 1, target);
        else return binarySearch(nums,mid + 1, end, target);
    }

    public int binarySearchDescending(int[] nums, int start , int end , int target){
        if(start > end) return -1;
        int mid = start + (end - start)/2;

        if(nums[mid] == target) return  mid;
        else if(nums[mid] < target) return binarySearchDescending(nums, start, mid - 1, target);
        else return binarySearchDescending(nums,mid + 1, end, target);
    }

    /* Find First and Last Position of Element in Sorted Array - Given an array of integers nums sorted in non-decreasing order, find the starting and ending position of a given target value.
       If target is not found in the array, return [-1, -1].
       https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
       We have created two functions to find first and last element to make the code less complex. findFirst will keep performing binary search on left side of array after it found
       the target. findLast will keep performing on right side of array after it found the target. Each time we find target - we will update first and last index.
     */
    public int[] searchRange(int[] nums, int target) {
        int[] result = new int[2];
        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        return result;
    }

    private int findFirst(int[] nums, int target){
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while(start <= end){
            int mid = (start + end) / 2;
            //Only changed part - We are storing updated index here instead of returning
            if(nums[mid] == target) idx = mid;

            //If found go left
            if(nums[mid] >= target){
                end = mid - 1;
            }
            //Else go right
            else{
                start = mid + 1;
            }

        }
        return idx;
    }

    private int findLast(int[] nums, int target){
        int idx = -1;
        int start = 0;
        int end = nums.length - 1;
        while(start <= end){
            int mid = (start + end) / 2;
            //Store index
            if(nums[mid] == target) idx = mid;
            //If found go right
            if(nums[mid] <= target){
                start = mid + 1;
            }
            //Else go left
            else{
                end = mid - 1;
            }

        }
        return idx;
    }

    /* Number of occurrence - Given a sorted array Arr of size N and a number X, you need to find the number of occurrences of X in Arr.
       https://practice.geeksforgeeks.org/problems/number-of-occurrence2259/1/#

       This question is just a variation of previous question - if we have first and last index of am element - we can find out number of occurrences.
     */
    int count(int[] nums, int n, int target) {
        int[] result = new int[2];
        result[0] = findFirst(nums, target);
        result[1] = findLast(nums, target);
        //If element is not present return 0, else return diff of indexes + 1 for number of occurrences.
        return result[0] != -1 ? result[1] - result[0] + 1 : 0;
    }

    /* Find Minimum in Rotated Sorted Array - Suppose an array of length n sorted in ascending order is rotated between 1 and n times.
    For example, the array nums = [0,1,2,4,5,6,7] might become:
    [4,5,6,7,0,1,2] if it was rotated 4 times.
    [0,1,2,4,5,6,7] if it was rotated 7 times.
    Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
    Given the sorted rotated array nums of unique elements, return the minimum element of this array.
    https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/

    This question can also be read as - find rotation count of a rotated array.
    If we observe the rotated array - we can observe that our minimum element is the only element - which is smaller than its left and right both sides. We have use binary search to
    look for this property.
    Below algorithm will only work if there are no duplicates.
     */
    public static int findMin(int[] nums) {
        int start = 0;
        int end = nums.length-1;

        while (start < end){
            int mid = start + (end -start)/ 2;

            //We are taking care of array rotation here by checking mid is not at start or end of array
            //If mid has not reached end & mid is greater than next element - this can only happen if next element is the smallest
            if(mid < end && nums[mid] > nums[mid + 1]) return nums[mid + 1];
            //If mid has not reached start & mid is smaller than previous element - this can only happen if mid itself is smallest
            if(mid > start && nums[mid] < nums[mid - 1]) return nums[mid];

            //If this condition is true - means left side array is sorted - our pivot - minimum element will be on other side
            if(nums[start] < nums[mid]) start = mid + 1;
            //Left side is not sorted - pivot must b eon this side only
            else end = mid - 1;
        }

        //Array is not rotated
        return nums[0];
    }

    /* Search in Rotated Sorted Array - Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.
       https://leetcode.com/problems/search-in-rotated-sorted-array/

       This can easily be solved if we have solved the "findMin" problem. We know that find min divides array into two sorted arrays. Once we have the pivot, we can perform
       binary search on both sides and return if element is found on either side.
     */
    public int searchRotated(int[] nums, int target) {
        //Our findMin returns the element - it can be easily modified to return index of that element - we are using the index variant here
        int rotated =  findMin(nums);
        int left =  binarySearch(nums, 0 , rotated - 1, target);

        if(left != -1 ) return left;
        else return binarySearch(nums, rotated , nums.length - 1, target);
    }

    /* Search in a nearly sorted array - Given a nearly sorted array such that each of its elements may be misplaced by no more than one position from the correct sorted order, efficiently search a given element in it and return its index.
       https://techiedelight.com/practice/?problem=SearchNearlySortedArray

       This is just an extension of binarySearch - only difference here is that instead of just comparing mid with target, we will have to compare mid + 1 and mid - 1 too with target
       as we don't know which one is actual mid. if target is not equal to any of the mid we will search on right/left side based on mid.
     */
    public static int findIndex(int[] nums, int target)
    {
        return binarySearchNearlySorted(nums, 0, nums.length - 1, target);
    }

    private static int binarySearchNearlySorted(int[] nums, int start, int end, int target){
        if(start > end) return -1;
        int mid = start + (end - start)/2;

        // Looking for all possible values of mid
        if(nums[mid] == target) return mid;
        if(mid > start && nums[mid - 1] == target) return mid -1 ;
        if(mid < end && nums[mid + 1] == target) return mid + 1;

        //Here we know that target is not any of mid possible values - if target is bigger than mid - it's bigger than mid+1 and mod - 1 too and vice versa
        return nums[mid] > target ? binarySearchNearlySorted(nums, start, mid - 2, target) : binarySearchNearlySorted(nums, mid + 2, end, target) ;
    }

    /* Find floor and ceil of a number in a sorted integer array
       For a number x, floor(x) is the largest integer in the array less than or equal to x, and ceil(x) is the smallest integer in the array greater than or equal to x.
       If the floor or ceil doesn’t exist, consider it to be -1.
       https://techiedelight.com/practice/?problem=FloorAndCeil

     */
    public static int[] findFloorAndCeil(int[] nums, int x)
    {
        return new int[]{findFloor(nums,x), findCeil(nums,x)};
    }

    private static int findFloor(int[] nums, int x){
        int floor = -1;
        int start = 0;
        int end = nums.length - 1;

        while (start <= end){
            int mid = start + (end - start)/2;
            // if `x` is equal to the middle element, it is the floor
            if(nums[mid] == x){
                floor = mid;
                break;
            }
            // if `x` is more than the middle element, the floor exists in the subarray nums[mid+1…end]; update floor to the middle element - this could be our potential solution
            // and reduce our search space to the right subarray nums[mid+1…end]
            if(nums[mid] < x){
                floor = mid;
                start = mid + 1;
            }
            //`x` is less than the middle element, the floor exists in the left subarray nums[left…mid-1]
            else {
                end = mid - 1;
            }
        }

        return floor != -1 ? nums[floor] : floor;
    }

    private static int findCeil(int[] nums, int x){
        int ceil = -1;
        int start = 0;
        int end = nums.length - 1;
        while (start <= end){
            int mid = start + (end - start)/2;
            // if `x` is equal to the middle element, it is the ceil
            if(nums[mid] == x){
                ceil = mid;
                break;
            }
            // if `x` is less than the middle element, the ceil exists in the
            // subarray nums[left…mid]; update ceil to the middle element - this could be our potential solution
            // and reduce our search space to the left subarray nums[left…mid-1]
            if(nums[mid] > x){
                ceil = mid;
                end = mid - 1;
            }
            // if `x` is more than the middle element, the ceil exists in the
            // right subarray nums[mid+1…right]
            else {
                start = mid + 1;
            }
        }

        return ceil != -1 ? nums[ceil] : ceil;
    }

    /* Find Smallest Letter Greater Than Target - Given a characters array letters that is sorted in non-decreasing order and a character target,
       return the smallest character in the array that is larger than target.
       Note that the letters wrap around. For example, if target == 'z' and letters == ['a', 'b'], the answer is 'a'.
       https://leetcode.com/problems/find-smallest-letter-greater-than-target/

       This problem is similar to ceil - only difference - we want the next element - so if we find a potential solution - letters[mid] > target we store the potential
       solution and traverse left
       Else we traverse right - also in case of letters[mid] == target
       Also we will always have a solution here because of character wrapping here - So we can take modulo if ceil instead of ceil.
     */
    public char nextGreatestLetter(char[] letters, char target) {
        int ceil = 0;
        int start = 0;
        int end = letters.length - 1;
        while (start <= end){
            int mid = start + (end - start)/2;

            if(letters[mid] > target){
                ceil = mid;
                end = mid - 1;
            }
            else {
                start = mid + 1;
            }
        }
        return letters[ceil % letters.length];
    }

    /* Single Element in a Sorted Array - You are given a sorted array consisting of only integers where every element appears exactly twice, except for one element which appears exactly once.
       Return the single element that appears only once.
       https://leetcode.com/problems/single-element-in-a-sorted-array/
       Array is sorted so that's a hint to use binary search. Now we have to find this element based on a property - that it is the only non-duplicated element means element on its left
       and right are different - that our equivalent to comparing middle element in plain binary search.
       Now how should we partition based on mid-comparison? If we have a sorted array with each number occurring twice - the number at even index will be same as index - 1 (except 0)
       also if index is odd, we will get same number at index + 1(except end). We will use these conditions only to decide if we should go left or right
     */
    public static int singleNonDuplicate(int[] nums) {
        int start = 0;
        int end = nums.length - 1;

        while (start <= end){
            //This is for case having single element
            if(start == end ) return nums[start];
            int mid = start + (end - start)/2;

            //Taking care of IndexOutOfBound case and comparing element with its left & right - if they are diff, we found our element
            if(mid > start && mid < end && nums[mid - 1] != nums[mid] && nums[mid+1] != nums[mid]) return nums[mid];
            //Checking if mid is even or odd and then comparing respective neighbours to decide where to go
            //Eg. mid = 2 , nums = [0,0,1,1,2] , we are checking next element - its equal - our start will be indexed 4
            if(mid % 2 == 0 && nums[mid] == nums[mid + 1]) start = mid + 2;
            //Eg. mid = 3, [0,0,1,1,2], we are checking previous element - its equal - our start will be indexed 4
            else if(mid % 2 != 0 && nums[mid] == nums[mid - 1]) start = mid + 1;
            //If it's not equal - that single number be on left
            // //Eg. mid = 5, [0,0,1,1,2,5,5], we are checking previous element - it's not equal  - our end will be indexed 5
            else end = mid;
        }

        return nums[0];
    }
    /* Find position of an element in a sorted array of infinite numbers
       In practice there is no, infinite array - but these questions are interesting and can be mostly asked in 1:1 interviews - this question basically boils down to
       find an element in a sorted array where we don't know end.
       How will our binarySearch work without end? One approach could be set a random end - lets suppose index 1, now compare :
       1. If target is less than this end - perform binarySearch for (start,end)
       2. If target is greater than array[end], shift end to a farther position - lets say end * 2 , and make start as end + 1.
       3. Repeat 1 & 2
     */
     int findPos(int arr[],int key){
        int start = 0;
        int end = 1;

        while(key > arr[end]){
            start = end;
            end = end * 2;
        }
        return binarySearch(arr, start, end, key);
    }

    /* Find Peak Element - A peak element is an element that is strictly greater than its neighbors.
       Given an integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.
       You may imagine that nums[-1] = nums[n] = -∞.
       https://leetcode.com/problems/find-peak-element/

       We can use binary search here as we want to return only one peak element index. Just like in binary search, we will find middle index - compare it with target - target is a property here -
       target - element should be strictly greater than its neighbours.
       Based on result of comparison we have three choices :
       1. mid-element satisfies target property - return mid
       2. mid-element is smaller than its right neighbour - peak could be there on right
       3. mid-element is smaller than its left neighbour - peak could be there on left

       This solution also solve problem like - Bitonic Point :  Given an array arr of n elements which is first increasing and then may be decreasing, find the maximum element in the array.
       https://practice.geeksforgeeks.org/problems/maximum-value-in-a-bitonic-array3001/1#

       In a Bitonic array - there will be only one peak - which is also the maximum
     */
    public int findPeakElement(int[] nums) {
        int start = 0;
        int end = nums.length - 1;
        int peakElement = 0;

        while(start <= end){
            //Base condition when array size is 1
            if(start == end ) return start;
            int mid = start + (end - start)/2;
            //Edge case - if mid is at 0 - we only need to compare it with right neighbour
            if(mid == 0){
                if(nums[mid] > nums[mid + 1]) return mid;
                else start = mid + 1;
            }
            //Edge case - if mid is at end - we only need to compare it with left neighbour
            else if(mid == nums.length - 1){
                if(nums[mid] > nums[mid - 2]) return mid;
                else end = mid - 1;
            }
            else{
                //Case 1
                if(nums[mid] > nums[mid - 1] && nums[mid] > nums[mid + 1]) return mid;
                //Case 2
                else if(nums[mid + 1] >= nums[mid]) start = mid + 1;
                //Case 3
                else end = mid - 1;
            }
        }

        //Code won't come here - we can make use of this return if we break from while as soon as we have a peak index.
        return peakElement;
    }

    /* Search in Bitonic Array - Given a bitonic sequence A of N distinct elements, write a program to find a given element B in the bitonic sequence in O(logN) time.
       https://www.interviewbit.com/problems/search-in-bitonic-array/
       A peak element divides a bitonic array into two arrays of increasing and decreasing sub-arrays. We can find peak element index and run binary search on both the sub-arrays.

     */
    public int solve(ArrayList<Integer> A, int B) {
        int[] nums = new int[A.size()];
        for(int i = 0; i < A.size(); i++) nums[i] = A.get(i);

        int peak = findPeakElement(nums);
        int leftIndex = binarySearch(nums, 0, peak, B);
        int rightIndex = binarySearchDescending(nums, peak + 1, A.size() - 1, B);

        return  leftIndex != -1 ? leftIndex : rightIndex;
    }

    /* Search a 2D Sorted Matrix - Write an efficient algorithm that searches for a value target in an m x n integer matrix. This matrix has the following properties:
        1. Integers in each row are sorted from left to right.
        2. The first integer of each row is greater than the last integer of the previous row.
        https://leetcode.com/problems/search-a-2d-matrix/
        There are two ways to solve this problem
        1. Use the soul behind binarySearch - eliminate search space based on a comparison
        2. Use 2D array as 1D array and perform simple binary search

     */

    public boolean searchMatrix(int[][] matrix, int target) {
        //We are taking mid as top right element & eliminate a row or column based on comparison result
        //We will be traversing in left-down direction - mid is matrix[rowIndex][colIndex]
        int rowIndex = 0;
        int colIndex  = matrix[0].length - 1;
        boolean isPresent = false;

        while (rowIndex < matrix.length &&  colIndex >= 0){
            //Found element return
            if(matrix[rowIndex][colIndex] == target) return true;
            //mid is greater than target - we know any element below mid can't be target - mid column eliminated
            if(matrix[rowIndex][colIndex] > target) colIndex -= 1;
            //mid is smaller than target - as we are traversing from right to left - we know any element left of current row can't be target - current row eliminated
            else rowIndex += 1;
        }
        return isPresent;
    }

    /* This on is difficult to think on spot - but can be deduced if we have a matrix in front of us.
       [1, 3, 5, 7],
       [10,11,16,20],
       [23,30,34,60]
       If we consider the above matrix of size RxC as 1D array and traverse row by row- starting element is 0(position 0) and end element is 60(position - 11)
       How can we represent an element at position X in 2D matrix? Index of X will [X/C][X%C] - this can also be deduced while looking into matrix.
       So an element at position 4 (10), in 1D array will be at index [4/4][4%4] or [1][0].
     */
    public boolean searchMatrix2Dto1D(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int start = 0, rows = matrix.length, cols = matrix[0].length;
        int end = rows * cols - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            if (matrix[mid / cols][mid % cols] == target) {
                return true;
            }
            if (matrix[mid / cols][mid % cols] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return false;
    }

    /* Koko Eating Bananas - Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.
    Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas,
    she eats all of them instead and will not eat any more bananas during this hour. Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.
    Return the minimum integer k such that she can eat all the bananas within h hours.
    https://leetcode.com/problems/koko-eating-bananas/

    This is one of those questions where it's not very intuitive to use binary search - our algorithm will be bruteforce - but we can use binary search to limit possible input
    space for brute force using binarySearch.
    Solution is beautifully explained - https://www.youtube.com/watch?v=U2SozAs9RzA&list=PLot-Xpze53leNZQd0iINpD-MAhMOMzWvO&index=11

    There are similar problems following this approach of limiting brute force range and apply binary search on it
    Allocate minimum number of pages - https://practice.geeksforgeeks.org/problems/allocate-minimum-number-of-pages0937/1/
        In above questions we can range brute force by number of maximum number of pages - and perform binarySearch on this range to check if mid - satisfies condition
     */
    public int minEatingSpeed(int[] piles, int H) {
        //Monkey has to eat a minimum of 1 banana/hr to eat all, maximum if it can eat the max lot size, its guaranteed to finish all in time
        int min = 1, max = getMaxPile(piles);

        // Binary search to find the smallest valid min.
        while (min <= max) {
            int mid = min + (max - min) / 2;

            if (canEatAll(piles, mid, H)) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }

        return min;
    }
    // Check if the monkey can eat all piles with given rate and hour
    private boolean canEatAll(int[] piles, int K, int H) {
        int countHour = 0; // Hours it will take to eat all bananas at speed K.

        for (int pile : piles) {
            countHour += pile / K;
            if (pile % K != 0)
                countHour++;
        }
        return countHour <= H;
    }
    // Get max value in pile
    private int getMaxPile(int[] piles) {
        int maxPile = Integer.MIN_VALUE;
        for (int pile : piles) {
            maxPile = Math.max(pile, maxPile);
        }
        return maxPile;
    }

}
