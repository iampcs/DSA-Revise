package Intervals;

import java.util.*;

/*
This pattern describes an efficient technique to deal with overlapping intervals. In a lot of problems involving intervals, we either need to find overlapping intervals or merge intervals if they overlap.
Given two intervals (‘a’ and ‘b’), there will be four different ways the two intervals can relate to each other:
1. a and b do not overlap - a starts and ends before b starts
2. a and b overlaps, b ends after a - a starts before b and ends after b starts but before b ends
3. a completely overlaps b - b starts after a start and ends before a end
4. a completely overlaps b but both have same start time - a and b starts together but b ends before a

Note: Further exploration - An alternative approach to these problems will be to create an Interval Tree - It's a more advance problem, but we can perform repetitive tasks like adding an interval,
removing an interval or find overlaps in O(logN) time - https://www.geeksforgeeks.org/interval-tree/
 */
public class Intervals {
    public static void main(String[] args) {

    }
    /* Merge Intervals - Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
       and return an array of the non-overlapping intervals that cover all the intervals in the input.
       Eg.  Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
            Output: [[1,6],[8,10],[15,18]]
            Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
            https://leetcode.com/problems/merge-intervals/

       These are visual problems - means drawing these intervals on number line - it makes it very easier understand what is asked and how to solve the problem
       Check - https://www.youtube.com/watch?v=44H3cEC2fFM
       Merging approach :
       1. Sort the intervals on start time to ensure - a.start <= b.start
       2. Check if second interval overlaps with first - If it does - create a new merge interval c with (a.start , max(a.end, b.end)) - replace a and b with c
       3. Move to next interval

       Code is ugly due to input and output format - could look better if Lists were used
       Time Complexity - O(NlogN) for sorting - O(N) for merging -> O(NLogN)
     */
    public int[][] merge(int[][] intervals) {
        //Sort intervals array based on start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        //Create a new array to store solution - its maximum size will be of intervals
        int[][] mergedIntervals = new int[intervals.length][intervals[0].length];
        //Add first interval to solution
        mergedIntervals[0] = intervals[0];
        //As solution is required in array - we need to keep an index to know current size of our solution
        int mergeIndex  = 0;
        //As we have already added 0th index to our solution, traverse from index 1
        for(int interval = 1; interval < intervals.length; interval++){
            //Store last interval in our solution - we will compare next interval with this - We know for sure that start time won't change, Why?
            //We will be modifying end time, if this new interval overlaps
            int[] previousInterval = mergedIntervals[mergeIndex];
            //Check if interval overlaps - current Interval start <= merged Interval end
            if(intervals[interval][0] <= previousInterval[1])
                //Modify merged interval end to store max of both end - We cover all cases here
                mergedIntervals[mergeIndex][1] = Math.max(mergedIntervals[mergeIndex][1], intervals[interval][1]);
            //Interval don't overlap - add it to our solution as it is
            else {
                mergeIndex += 1;
                mergedIntervals[mergeIndex] = intervals[interval];
            }
        }
        //If our solution is smaller than intervals we will have extra (0,0) - So only return till our solution
        return Arrays.copyOf(mergedIntervals, mergeIndex + 1);
    }

    /* Insert Interval - You are given an array of non-overlapping intervals where intervals[i] = [starti, endi] represent the start and the end of the ith interval and intervals is sorted in ascending order by starti.
    You are also given an interval newInterval = [start, end] that represents the start and end of another interval.
    Insert newInterval into intervals such that intervals is still sorted in ascending order by starti and intervals still does not have any overlapping intervals (merge overlapping intervals if necessary).
    Return intervals after the insertion.
    https://leetcode.com/problems/insert-interval/

    Eg. Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
        Output: [[1,5],[6,9]]

       I hate this solution due to limitation of Java with arrays - but this works in O(N) complexity
       Following approach is described in - https://www.youtube.com/watch?v=A8NUOmlwOlM

       We have three cases here -
       1. newInterval.end is less than interval.start - Non-overlapping - We can just add this newInterval to mergedIntervals and add rest of intervals as it is and return
       2. newInterval.start is greater than interval.end - Non-overlapping - We can add current interval to mergedIntervals and proceed further
       3. newInterval is overlapping with interval - merge them - Don't add it to mergedInterval yet - because we might need to merge it with further intervals

     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
        //Edge case - If intervals is empty
        if(intervals.length == 0) {
            return new int[][]{newInterval};
        }
        int[][] mergedIntervals = new int[intervals.length + 1][intervals[0].length];
        int mergeIndex = 0;

        for(int interval = 0; interval < intervals.length ; interval++){
            //Case 1 - newInterval.end is less than interval.start - Non-overlapping - We can just add this newInterval to mergedIntervals and add rest of intervals as it is and return
            if(newInterval[1] < intervals[interval][0]){
                mergedIntervals[mergeIndex] = newInterval;
                for(int i = interval ,j = mergeIndex + 1; i < intervals.length && j < mergedIntervals.length; i++,j++){
                    mergedIntervals[j] = intervals[i];
                    mergeIndex += 1;
                }
                return Arrays.copyOf(mergedIntervals, mergeIndex + 1);
            }
            //Case 2 - newInterval.start is greater than interval.end - Non-overlapping - We can add current interval to mergedIntervals and proceed further
            else if(newInterval[0] > intervals[interval][1]){
                mergedIntervals[mergeIndex] = intervals[interval];
                mergeIndex += 1;
            }
            //Case 3 - newInterval is overlapping with interval - merge them - Don't add it to mergedInterval yet - because we might need to merge it with further intervals
            else {
                newInterval = new int[]{Math.min(newInterval[0],intervals[interval][0]),
                        Math.max(newInterval[1],intervals[interval][1])};
            }
        }

        //There can be a case where we end up merging all intervals and exit above for loop
        //In that case we are adding newInterval here to mergedIntervals
        mergedIntervals[mergeIndex] = newInterval;
        return Arrays.copyOf(mergedIntervals, mergeIndex + 1);
    }

    /* Non-overlapping Intervals - Given an array of intervals where intervals[i] = [starti, endi], return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.
       Input: intervals = [[1,2],[2,3],[3,4],[1,3]]  Output: 1
       Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
       https://leetcode.com/problems/non-overlapping-intervals/

       We can use a greedy approach to solve this issue. For any two intervals we can have following cases:
       1. a.start <= b.start < a.end : Overlapping - which interval to delete? We reduce chance of overlapping if we delete the one with max end
       2. a.end <= b.start : Non-overlapping case for this question - no need to delete

       Approach :
       1. Sort intervals as per start time
       2. Pick one interval at a time and check if they are overlapping
            3. If overlapping delete the one with maximum end - keep track of ends to check if next one is overlapping
            4. If no overlapping - no need to delete - just update end of this new interval
     */
    public int eraseOverlapIntervals(int[][] intervals) {
        int deleteCount = 0;
        // Sort based on starting time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        //Initialize endInterval with first interval
        int endInterval = intervals[0][1];

        for(int interval = 1; interval < intervals.length; interval++){
            //Overlapping condition - b.start < a.end
            if(intervals[interval][0] < endInterval ){
                //We are not actually deleting an interval - In future we will ignore its end
                deleteCount += 1;
                //We will remove the one with max end or keep the one with minimum end - update endInterval - ignore the one with max end
                endInterval = Math.min(endInterval, intervals[interval][1]);
            }else {
                //Non overlapping - update end
                endInterval = intervals[interval][1];
            }
        }
        return deleteCount;
    }

    /* Interval List Intersections - You are given two lists of closed intervals, firstList and secondList, where firstList[i] = [starti, endi] and secondList[j] = [startj, endj].
       Each list of intervals is pairwise disjoint and in sorted order.Return the intersection of these two interval lists.
       A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b. The intersection of two closed intervals is a set of real numbers that are either empty or represented as a closed interval.
       Eg. Input: firstList = [[0,2],[5,10],[13,23],[24,25]],
                 secondList = [[1,5],[8,12],[15,24],[25,26]]
           Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
        https://leetcode.com/problems/interval-list-intersections/

        Following code could have been smaller if we were to merge these two lists and calculating intersections at the same time - but that would have made the code complex to understand
        We still are solving this in O(N+M) complexity

        Approach :
        1. Create a new mergedList which contains intervals from both in sorted order - as both the lists are already sorted - this can be done in O(N+M)
        2. For consecutive intervals (a,b) we have the following cases:
            1)  a.end < b.start : non-overlapping - do nothing
            2) a.end >= b.start and a.end < b.end : Overlapping - add intersection to intersections - min(a.end, b.start), min(a.end,b.end) :
                Eg. [5,10][5,6] : Intersection :  min(10,5), min(10,6) -> (5,6)
                2.1) a.end > b.end : Overlapping - here b is inside a - its possible that a can have intersection with c - in this case we can do (b.end = a.end)
                     In our code we are traversing the intervals one by one - if a previous interval end is longer than current interval end - when we move to next interval,
                     we will miss this info - so as a hack we are modifying current interval end to previous interval's end.

       Note: This problem can also be solved using two-pointer approach. Although the complexity will be same - but it will save on space.
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        //Length of intersections will be at max - M+N-1. Why ?
        int[][] intersections = new int[firstList.length + secondList.length - 1][2];
        int interSectionIndex = 0;
        int[][] mergedList = new int[firstList.length + secondList.length ][2];
        int mergedListIndex = 0;
        int firstIndex = 0;
        int secondIndex = 0;

        //Creating mergedList which will be sorted
        while (firstIndex < firstList.length && secondIndex < secondList.length){
            if(firstList[firstIndex][0] <= secondList[secondIndex][0]){
                mergedList[mergedListIndex] = firstList[firstIndex];
                mergedListIndex++;
                firstIndex++;
            }else {
                mergedList[mergedListIndex] = secondList[secondIndex];
                mergedListIndex++;
                secondIndex++;
            }
        }
        while (firstIndex < firstList.length){
            mergedList[mergedListIndex] = firstList[firstIndex];
            mergedListIndex++;
            firstIndex++;
        }
        while (secondIndex < secondList.length){
            mergedList[mergedListIndex] = secondList[secondIndex];
            mergedListIndex++;
            secondIndex++;
        }

        //For mergeList comparing (a,b)
        for(int i = 0; i < mergedList.length - 1; i++){
            //a.end >= b.start
            if(mergedList[i][1] >= mergedList[i+1][0]){
                intersections[interSectionIndex] = new int[]{Math.min(mergedList[i][1],mergedList[i+1][0]),
                                                             Math.min(mergedList[i][1],mergedList[i+1][1])};
                interSectionIndex += 1;
                //a.end > b.end
                if(mergedList[i][1] > mergedList[i+1][1]) mergedList[i+1][1] = mergedList[i][1];
            }
        }
        return Arrays.copyOf(intersections, interSectionIndex );
    }
    /* Meeting Rooms - Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
       (0,8),(8,10) is not conflict at 8
       https://www.lintcode.com/problem/920/

       This is an eazy-peazy after we have done previous questions - we just need to return if there are overlapping intervals
     */
    public boolean canAttendMeetings(List<Interval> intervals) {
        //Sort based on starting time
        Collections.sort(intervals, Comparator.comparingInt(interval -> interval.start));
        int endInit = -1;

        for (Interval interval : intervals){
            //Overlapping intervals
            if(interval.start < endInit) return false;
            //Not overlapping update end
            else endInit = interval.end;
        }
        return true;
    }
    /* Meeting Rooms II - Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), find the minimum number of conference rooms required.)
       (0,8),(8,10) is not conflict at 8
       https://www.lintcode.com/problem/919/

       We need to find maximum overlapping intervals here. This problem is different from rest of problems we solved - If we try to sort the array based on start time or end time
       we will not get maximum overlapping intervals here - I tried - there will be missed case - because we can have one interval at a time while iterating it's not possible to cover
       all use case. We can solve it that way by keep track of the ending time of all the meetings currently happening so that when we try to schedule a new meeting,
       we can see what meetings have already ended. We need to put this information in a data structure that can easily give us the smallest ending time.
       A Min Heap would fit our requirements best.
       We have solved it using both the approaches.

       Approach:
       1. Sort all the meetings on their start time.
       2. Create a min-heap to store all the active meetings - How? Let this min-Heap be sorted based on end timings. A meeting m1 is in heap top - we want to add m2 - Now think m2 will be added at m2.start time - if m1.end is less than
          or equal to m2.start - means when we were adding m2, m1 has already ended. So we will remove m1, this is how we can store all active meetings.
       3. Iterate through all the meetings one by one to add them in the min-heap. Let’s say we are trying to schedule the meeting m1.
       4. Since the min-heap contains all the active meetings, so before scheduling m1 we can remove all meetings from the heap that have ended before m1, i.e.,
          remove all meetings from the heap that have an end time smaller than or equal to the start time of m1.
       5. The heap will always have all the overlapping meetings, so we will need rooms for all of them.

       A different approach for this problem : https://www.youtube.com/watch?v=FdzJmTCVyJU
       1. Create two sorted arrays - start[] and end[] - this is where this problem differs
       2. Maintain two pointers - start pointer & end pointer -
          2.1.If element at start is less than end - a meeting has started - increment meeting room - increment start pointer
          2.2 If element at end is less than start - a meeting has ended - decrement meeting room - increment end pointer
          2.3 If they are same - as per question - there is no overlapping - increment end pointer - decrement meeting room
       3. Maximum value of meeting rooms will be our solution

       Note : Similar Problems -
       1. Find minimum number of platforms required - given trains (start,end) times.
       2. Given a list of intervals, find the point where the maximum number of intervals overlap.
       3. Maximum CPU Load - We are given a list of Jobs. Each job has a Start time, an End time, and a CPU load when it is running. Our goal is to find the maximum CPU load at any time if all the jobs are
          running on the same machine : This problem can be easily solved using below approach by - maintaining a currentLoad in minHeap variable - adding load to it when adding to minHeap
          and removing load when removing from minHeap - maximum value of currentLoad will be our solution.


     */
    public int minMeetingRooms(List<Interval> intervals) {
        List<Integer> start = new ArrayList<>(intervals.size());
        List<Integer> end = new ArrayList<>(intervals.size());
        int minMeetingRoom = 0;
        int currentMeetingRoom = 0;
        for(Interval interval : intervals){
            start.add(interval.start);
            end.add(interval.end);
        }
        //Sorted start[] and end[]
        Collections.sort(start);
        Collections.sort(end);

        int startIndex = 0;
        int endIndex = 0;
        //Start will always finish before end - Why? - Once that happens we will get our maximum overlapping or minimum meeting rooms
        while (startIndex < start.size()){
            if(start.get(startIndex) < end.get(endIndex)) {
                currentMeetingRoom += 1;
                minMeetingRoom = Math.max(currentMeetingRoom, minMeetingRoom);
                startIndex += 1;
            }else {
                currentMeetingRoom -= 1;
                endIndex += 1;
            }
        }

        return minMeetingRoom;
    }
    //Same complexity as previous approach - but much elegant solution
    public int minMeetingRoomsHeap(List<Interval> intervals) {
        //Sort by meeting time
        Collections.sort(intervals, Comparator.comparingInt(interval -> interval.start));
        int minRooms = 0;
        //minHeap sorted by interval.end time
        PriorityQueue<Interval> minHeap = new PriorityQueue<>(intervals.size(), Comparator.comparingInt(a -> a.end));
        for(Interval interval : intervals){
            /// Remove all meetings that have ended
            while (!minHeap.isEmpty() && interval.start >= minHeap.peek().end) minHeap.poll();
            //Add current meeting
            minHeap.add(interval);
            //minHeap size will indicate all current active meetings, so we need rooms for all ot them
            minRooms = Math.max(minRooms, minHeap.size());
        }

        return minRooms;
    }
    /* Employee Free Time - We are given a list schedule of employees, which represents the working time for each employee.
       Each employee has a list of non-overlapping Intervals, and these intervals are in sorted order.
       Return the list of finite intervals representing common, positive-length free time for all employees, also in sorted order.
       The Intervals is an 1d-array. Each two numbers shows an interval. For example, [1,2,8,10] represents that the employee works in [1,2] and [8,10].
       Also, we wouldn't include intervals like [5, 5] in our answer, as they have zero length.
       https://www.lintcode.com/problem/850/

       One plain solution will be to merge all intervals into a sorted-by-start time list - then eliminate - check if two intervals are overlapping - if not, store free time between them
       This algorithm will work in O(NlogN) time - due to sorting.
       Can we do better? We know that for an employee intervals will always be sorted - can we use this to our advantage?
       1. We can use a minHeap here - which will be sorted based on start time
       2. Now we will add first interval of each employee - this will guarantee us that minHeap top will be the element with the smallest start
       3. Now we want the second-smallest start to compare it with - its possible that second-smallest start is inside minHeap already - its also possible its next interval of the poped employee interval
       4. So we will add next interval of every poped employee interval and insert it to minHeap - now we are guaranteed to have the smallest start back on top of minHeap
       5. From here onwards It's same as sorted Array - because our heap size will be atMax K and we will be inserting/poping N intervals - voila - O(NlogK)

     */
    public List<Interval> employeeFreeTime(int[][] schedule) {
       List<Interval> freeTime = new ArrayList<>();
       //We created a class - EmployeeInterval which will store current employee index interval index and interval
       //This is a minHeap sorted by start time
       PriorityQueue<EmployeeInterval> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a.interval.start));

       //Add first interval of every employee
       for (int employee = 0; employee < schedule.length; employee++){
           minHeap.add(new EmployeeInterval( new Interval(schedule[employee][0],schedule[employee][1]), employee, 0));
       }
       //Making top interval as first interval to compare others with
       Interval previousInterval = minHeap.peek().interval;
       while (!minHeap.isEmpty()){

           EmployeeInterval topEmployee = minHeap.poll();

           //Non-overlapping condition - add to free time
           if(previousInterval.end < topEmployee.interval.start)
               freeTime.add(new Interval(previousInterval.end, topEmployee.interval.start));

           // As we will only compare next start with previous end to compare overlapping condition, we will choose the previous which has the highest end value
           if(previousInterval.end < topEmployee.interval.end)
               previousInterval = topEmployee.interval;

           //Adding next interval of current interval employee if it exists
           //These indexes might be confusing - better to calculate this with a pen and paper
           if(topEmployee.intervalIndex * 2 + 2 < schedule[topEmployee.employeeIndex].length ){
                minHeap.add(new EmployeeInterval( new Interval(schedule[topEmployee.employeeIndex][(topEmployee.intervalIndex + 1) * 2],
                                                               schedule[topEmployee.employeeIndex][(topEmployee.intervalIndex + 1) * 2 + 1]),
                                                               topEmployee.employeeIndex, topEmployee.intervalIndex + 1));
           }
       }
       return freeTime;
    }

    class EmployeeInterval{
        Interval interval;
        int employeeIndex;
        int intervalIndex;

        public EmployeeInterval(Interval interval, int employeeIndex, int intervalIndex){
            this.interval = interval;
            this.employeeIndex = employeeIndex;
            this.intervalIndex = intervalIndex;
        }
    }



    public class Interval {
        int start, end;
        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }


}
