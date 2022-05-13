package LinkedList;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

/*
There are mostly two types of questions regarding LinkedList
1. Can be solved using fast-slow pointer
2. Reverse LinkedList - tracking previous and next

We will be using either one or both concepts to solve problems
Also if there are cases where head of a list can change - it's better to create a dummyNode and attach it to start of list - perform operations normally without thinking of the
edge case - it will take care of itself - return dummyHead.next.
 */
public class LinkedList {
    public static void main(String[] args) {

    }
    /* Middle of the Linked List - Given the head of a singly linked list, return the middle node of the linked list.If there are two middle nodes, return the second middle node.
       https://leetcode.com/problems/middle-of-the-linked-list/
       Basic use of slow-fast pointer - complexity remains O(N) only
 */
    public ListNode middleNode(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;

        //While we can move fast pointer - move slow pointer too
        while(fast!= null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    /* Delete the Middle Node of a Linked List - You are given the head of a linked list. Delete the middle node, and return the head of the modified linked list.
       https://leetcode.com/problems/delete-the-middle-node-of-a-linked-list/
       We know the how to find the middle element - keep a track of its previous index - point it to next of middle to delete middle
     */
    public ListNode deleteMiddle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        ListNode prev = null;
        //Edge case - in-case we have single node
        if(head.next == null) return null;

        while(fast!= null && fast.next != null){
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        //Deleting middle
        prev.next = prev.next.next;

        return head;
    }
    /* Remove Nth Node From End of List - Given the head of a linked list, remove the nth node from the end of the list and return its head.
       https://leetcode.com/problems/remove-nth-node-from-end-of-list/

       Keep two pointers - aheadByN and preDelete - move aheadByN by N position from head - now move both preHead and aheadByN together until
       aheadByN is null. preDelete will be at (delete - 1), make it point to (delete + 1) to delete element.
       These problems are prone to off by one error - solve this on a notebook
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {

        ListNode aheadByN = head;
        ListNode preDelete = head;

        // move aheadByN - we are guaranteed in question that N will be smaller than or equal to size of linkedList
        while(n > 0){
            aheadByN = aheadByN.next;
            n--;
        }
        //Edge Case - There are N elements only - delete head
        if(aheadByN == null) return head.next;

        //Move both pointers
        while(aheadByN.next != null){
            preDelete = preDelete.next;
            aheadByN = aheadByN.next;
        }

        //Delete preDelete.next
        preDelete.next = preDelete.next.next;
        return head;
    }

    /* Linked List Cycle - Given head, the head of a linked list, determine if the linked list has a cycle in it.
       https://leetcode.com/problems/linked-list-cycle/
       Basic case of slow-fast pointer - check if they are ever equal.
     */
    public boolean hasCycle(ListNode head) {
        if(head == null) return false;
        ListNode slow = head;
        ListNode fast = head;

        while(fast!= null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast) return true;
        }

        return false;
    }
    /* Linked List Cycle II - Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.
       https://leetcode.com/problems/linked-list-cycle-ii/

       We are using slow-fast pointer method to find loop - when we have found that point - we are starting another pointer from start
       and moving start and slow/fast pointer one node at a time - where they intersect - that point will be the starting of loop
       This is also called Floyd's Cycle detection algorithm - On why it works ? - Can watch https://www.youtube.com/watch?v=LUm2ABqAs1w
     */
    public ListNode detectCycle(ListNode head) {
        if(head == null) return null;
        ListNode slow = head;
        ListNode fast = head;
        ListNode cycleNode = null;

        while(fast!= null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;

            if(slow == fast) {
                cycleNode = head;
                break;
            }
        }
        //We have a cycle - detect node from where cycle starts using Floyd's algorithm
        while (cycleNode != null && cycleNode != slow){
            cycleNode = cycleNode.next;
            slow = slow.next;
        }

        return cycleNode;
    }

    /* Reverse Linked List - Given the head of a singly linked list, reverse the list, and return the reversed list.
       https://leetcode.com/problems/reverse-linked-list/

       We want to do in-place reversal - with O(1) : We are maintaining two pointers - curr and prev - at every iteration we are pointing curr to prev
       and moving curr to its next position. When curr is null - prev will point to end of list.
     */
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next = null;

        while(curr != null){
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        return prev;
    }

    /* Palindrome Linked List - Given the head of a singly linked list, return true if it is a palindrome.
       https://leetcode.com/problems/palindrome-linked-list/
       Again we want to do this in-place for a O(1) space complexity.
       We can get to middle of linkedList - reverse the second part of LinkList and then move and compare start & middle until middle is reaches end.
     */

    public boolean isPalindrome(ListNode head) {
        //Get middle of LinkedList
        ListNode middle = middleNode(head);
        //Reverses the second half - First half will still be attached to end of second half
        //Odd length : 3 will be middle - 1->2->3->4->5 : 1->2->3<-4<-5
        //Even length : 4 will be middle - 1->2->3->4->5->6 : 1->2->3->4<-5<-6
        // If we want the original list to be as it is - store the starting point of second list - reverse the second half again and attach it back to first half.
        ListNode reversedSecondHalf = reverseList(middle);

        while (head != null && reversedSecondHalf != null){
            if(head.val != reversedSecondHalf.val) return false;

            head = head.next;
            reversedSecondHalf = reversedSecondHalf.next;
        }

        return true;
    }

    /* Reorder List - You are given the head of a singly linked-list. The list can be represented as: L0 → L1 → … → Ln - 1 → Ln
       Reorder the list to be on the following form: L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
       You may not modify the values in the list's nodes. Only nodes themselves may be changed.
       https://leetcode.com/problems/reorder-list/

       We can do this easily by storing nodes position to an external sata structure like an array and rearrange. But we want to do this in-place.
       This problem is a lot similar to palindrome problem - there we were checking of first half is same as second half - here we will be
       merging first-half and second-half.
     */
    public void reorderList(ListNode head) {
        ListNode middle = middleNode(head);
        ListNode reversedSecondHalf = reverseList(middle);

        //Traversing both start and middle
        //This could be better understood with a diagram - sit with a pen and paper
        //This gets tricky for even length LinkedList when head is pointing to end of first half
        while(head != null && reversedSecondHalf != null){
            //making start point to middle and moving start to next position
            ListNode temp = head.next;
            head.next = reversedSecondHalf;
            head = temp;

            //making middle point to start and moving middle to next position
            temp = reversedSecondHalf.next;
            reversedSecondHalf.next = head;
            reversedSecondHalf = temp;
        }

        //In case of even length LinkedList - end of head is still pointing to end of reversed list - make it null
        //For odd length case - its handled above
        if(head != null) head.next = null;

    }
    /* Add Two Numbers II - You are given two non-empty linked lists representing two non-negative integers. The most significant digit comes first and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
       You may assume the two numbers do not contain any leading zero, except the number 0 itself.
       https://leetcode.com/problems/add-two-numbers-ii/

       Simple case of reversing LinkedList before adding and then reversing the sum again. These questions are generally easy to come up with and code
       What takes time is coding to cover all edge cases - So be clear on all the constraints.
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode l1Reversed = reverseList(l1);
        ListNode l2Reversed = reverseList(l2);
        ListNode sum = null;
        ListNode sumHead = null;
        int carryOn = 0;

        while (l1Reversed != null && l2Reversed != null){
            if(sum == null){
                sum = new ListNode((l1Reversed.val + l2Reversed.val + carryOn) % 10);
                carryOn = (l1Reversed.val + l2Reversed.val + carryOn) / 10;
                sumHead = sum;
            }else{
                sum.next = new ListNode((l1Reversed.val + l2Reversed.val + carryOn) % 10);
                carryOn = (l1Reversed.val + l2Reversed.val + carryOn) / 10;
            }

            l1Reversed = l1Reversed.next;
            l2Reversed = l2Reversed.next;
        }

        //Case where length of l1 is greater than l2
        while (l1Reversed != null){
            sum.next = new ListNode((l1Reversed.val + carryOn) % 10);
            carryOn = (l1Reversed.val + carryOn) / 10;
        }
        //Case where length of l2 is greater than l1
        while (l2Reversed != null){
            sum.next = new ListNode((l2Reversed.val + carryOn) % 10);
            carryOn = (l2Reversed.val + carryOn) / 10;
        }
        //Case where our sum length will be more than l1 & l2 - Eg. 9 , 99 - will have sum : (1)08  - This one in bracket case is what we are
        //handling here
        if(carryOn == 1){
            sum.next = new ListNode(carryOn);
        }
        //Sum will be in reversed order
        return reverseList(sumHead);
    }

    /* Copy List with Random Pointer - A linked list of length n is given such that each node contains an additional random pointer, which could point to any node in the list, or null.
       Construct a deep copy of the list.
       https://leetcode.com/problems/copy-list-with-random-pointer/

       Although this question doesn't explore any popular concept - it's a popular question so covering it. We can create a deep copy in two pass of original list and HashMap
       Time : O(N), Space: O(N)
       There is another approach that doesn't take O(N) space - It's not very intuitive - will discuss that too.

     */
    public Node copyRandomList(Node head) {
        //Map of - original list node, copied list node
        HashMap<Node,Node> map = new HashMap<>();
        Node start = head;
        //First pass we are creating a copy of each node in original node - not populating next or random pointer
        //We ae not populating now as random pointer can point to a node , not created yet
        while (start != null){
            map.put(start,new Node(start.val));
            start = start.next;
        }

        start = head;
        //Now we are populating next and random pointer from map
        while (start != null){
            map.get(start).next = map.get(start.next);
            map.get(start).random = map.get(start.random);

            start = start.next;
        }
        return map.get(head);
    }

    /* Reverse Linked List II - Given the head of a singly linked list and two integers left and right where left <= right,
       reverse the nodes of the list from position left to position right, and return the reversed list.
       https://leetcode.com/problems/reverse-linked-list-ii/

       This algorithm will have three parts :
       1. Iterate till left , save prevLeft
       2. Reverse from left to right , save nextRight
       3. Connect the reversed part to rest of list using prevLeft and nextRight

       So basically we are detaching the sublist - reversing it - and attaching it back to main list - where to attach we are saving those parts
       Note: We will add a dummy node in at start - we do this to take care of edge case if head changes - adding dummy will make our code edge case code proof - we can always
       return dummy.next
       Following approach - https://www.youtube.com/watch?v=RF_M9tX4Eag

       Similar problems - Swap Nodes in Pairs - https://leetcode.com/problems/swap-nodes-in-pairs/
       This is same problem as above one with value of K = 2

     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        //Creating and adding dummy node and attaching it to start
        // 0->1->2->3->4->5
        ListNode dummyHead = new ListNode(0,head);
        ListNode prevLeft = null;
        ListNode currNode = dummyHead;

        //This loop will place currNode at left and prevLeft at left - 1
        //Lets assume left = 2 , right = 5 - this is position for 1-based-index
        // 0->   1->       2->      3->4->5
        //    prevLeft   currNode
        for(int pos = 0; pos < left; pos++){
            prevLeft = currNode;
            currNode = currNode.next;
        }

        //Reverse list from left to right
        //At end of this loop - currNode will be placed at right + 1
        //prev will be placed at right
        ListNode prev = null;
        for(int pos = left; pos <= right ; pos++){
            ListNode tmpNext = currNode.next;
            currNode.next = prev;
            prev = currNode;
            currNode = tmpNext;
        }

        //This is how list will look like after above loop - 2(left) will point to null
        // 0 ->   1 ->      2 <-3   <-4             5
        //     prevLeft             prev        currNode

        //We want 2(left) to point to 5(right + 1, currNode)
        prevLeft.next.next = currNode;
        //We want 1(prevLeft) to point at 4(right)
        prevLeft.next = prev;

        return dummyHead.next;
    }
    /* Reverse Nodes in k-Group - Given the head of a linked list, reverse the nodes of the list k at a time, and return the modified list.
       k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes,
       in the end, should remain as it is.You may not alter the values in the list's nodes, only nodes themselves may be changed.
       https://leetcode.com/problems/reverse-nodes-in-k-group/

       This problem is similar to reverseBetween - just that our left and right are always changing. Like previous problem - we will use a dummyNode to skip edge cases.
       Approach is similar to previous question - we will move Kth list at a time - reverse that part and attach it back to main list.
       Approach - https://www.youtube.com/watch?v=1UOPsfP85V4

       Here instead of prevLeft we have prevGroup - group of K nodes we want to reverse
       instead of nextRight we have nextGroup - We will detach the K-group nodes - reverse it and attach it back to prevGroup & nextGroup
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummyHead = new ListNode(0, head);

        ListNode prevGroup = dummyHead;

        //We want to do this in a single pass - we don't know how many K-pairs are there - we only know list is finite and will end
        while (true){
            //------------Get the Kth group---------------//
            //Function call to get Kth Node from a given node
            ListNode KthNode = getKthNode(prevGroup, k);
            //Current group does't have K nodes - leave it as it is, break from while
            if(KthNode == null) break;
            //If Kth node is present - we know next group will start from KthNode.next
            ListNode nextGroup = KthNode.next;

            // --------------- Reverse the Kth group --------------------//
            //Start of Kth group will be prevGroup.next, we know that first node of each group will be attached to start of next group post reverse
            //So we have tweaked prev to contain next group start instead of null
            ListNode currNode = prevGroup.next;
            ListNode prev = KthNode.next;

            while (currNode != nextGroup){
                ListNode tmpNext = currNode.next;
                currNode.next = prev;
                prev = currNode;
                currNode = tmpNext;
            }

            //---------------Attach the Kth group back to main list ----------------//
            //We have to do two things here - attach prevGroup to reversed list start(KthNode now) and make a new prevGroup before we move to next group
            //prevGroup is still attached to old-start(end of group now) of group - this old-start will be prevGroup for next group
            ListNode tmp = prevGroup.next;
            prevGroup.next = KthNode;
            prevGroup = tmp;

            //There is another problem similar to this where are asked to reverse alternating Kth Group - We can skip K nodes here
            //If there are not enough nodes we will break out of loop
        }

        return dummyHead.next;
    }

    private ListNode getKthNode(ListNode currNode, int k) {
        while (currNode != null && k != 0){
            currNode = currNode.next;
            k = k-1;
        }
        return currNode;

    }
    /* Rotate List - Given the head of a linked list, rotate the list to the right by k places.
        https://leetcode.com/problems/rotate-list/
        It's actually easy to rotate in LinkedList than in array, we don't have to rotate - but just move the K nodes in end to start.
        Eg. Input : 1 -> 2 -> 3 -> 4 -> 5   K = 2
            Output: 4 -> 5 -> 1 -> 2 -> 3
        Approach : We will take two pass to do this, it's not max efficient in terms of time - O(2N) but we are not using any extra space
        1. Iterate till end - find length
        2. Iterate again till K+1 node from end - (length - K - 1)
        3. Detach last K nodes and attach it in front - return new head
     */
    public ListNode rotateRight(ListNode head, int k) {
        //Edge case with 0,1 length
        if(head == null || head.next == null) return head;

        ListNode endList = head;
        int length = 1;
        //Measure list length - endList will be on end of list
        while (endList.next != null){
            endList = endList.next;
            length += 1;
        }
        //k can be greater than length - find actual number of rotations
        k = k % length;
        //No rotations - return list as it is
        if(k == 0) return head;

        //Move till K+1th node from end
        ListNode currNode = head;
        for(int pos = 0; pos < length - k - 1; pos++)
            currNode = currNode.next;

        //Detach K nodes from end and attach to start
        ListNode newHead = currNode.next;
        currNode.next = null;
        endList.next = head;

        return newHead;
    }
    /* Sort List - Given the head of a linked list, return the list after sorting it in ascending order.
       https://leetcode.com/problems/sort-list/

       We will use recursive merge sort here :
       1. Break list into two equal half parts - until it breaks down to single individual element
       2. Merge sorted list and return

       Time complexity - O(NlogN)
     */
    public ListNode sortList(ListNode head) {
        //Base case
        if(head == null || head.next == null) return head;

        //Dividing list into two halves by finding middle point
        ListNode prev = null, slow = head, fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = null;

        //We need to sort left and right part recursively
        ListNode leftSide = head;
        ListNode rightSide = slow;

        leftSide = sortList(leftSide);
        rightSide = sortList(rightSide);

        //Merge sorted list and return
        return mergeTwoLists(leftSide,rightSide);
    }
    /* Merge Two Sorted Lists - You are given the heads of two sorted linked lists list1 and list2.
       Merge the two lists in a one sorted list. The list should be made by splicing together the nodes of the first two lists.
       Return the head of the merged linked list.
       https://leetcode.com/problems/merge-two-sorted-lists/
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if(l1 == null) return l2;
        else if(l2 == null) return l1;
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while(l1 != null && l2!= null){
            if(l1.val <= l2.val){
                curr.next = l1;
                l1 = l1.next;
            }else {
                curr.next = l2;
                l2 = l2.next;
            }
            curr = curr.next;
        }
        curr.next = l1 == null? l2:l1;
        return dummy.next;
    }



    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    public class Node {
        int val;
        Node next;
        Node random;
        Node() {}
        Node(int val) { this.val = val; }
        Node(int val, Node next) { this.val = val; this.next = next; }
    }
}

