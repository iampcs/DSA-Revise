package LinkedList;
/*
There are mostly two types of questions regarding LinkedList
1. Can be solved using fast-slow pointer
2. Reverse LinkedList - tracking previous and next

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


    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}

