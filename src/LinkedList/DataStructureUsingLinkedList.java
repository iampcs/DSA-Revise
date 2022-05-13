package LinkedList;

import java.util.HashMap;
import java.util.Map;

public class DataStructureUsingLinkedList {
    public static void main(String[] args) {

    }
    class DoubleLinkedList{
        int key;
        int value;
        DoubleLinkedList prev;
        DoubleLinkedList next;
        DoubleLinkedList(){}
        DoubleLinkedList(int key, int value){
            this.key = key;
            this.value = value;
        }
        DoubleLinkedList(int value){
            this.value = value;
        }
    }
    /* LRU Cache - Design a data structure that follows the constraints of a Least Recently Used (LRU) cache. Implement the LRUCache class:
       LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
       int get(int key) Return the value of the key if the key exists, otherwise return -1.
       void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
       The functions get and put must each run in O(1) average time complexity.
       https://leetcode.com/problems/lru-cache/

       This can be achieved by maintaining a doubly linkedlist for order and a hashMap for O(1) get and put.
     */
    class LRUCache {
        Map<Integer,DoubleLinkedList> cache;
        private int currentSize;
        private int capacity;
        //Dummy nodes to keep track of least recently used and most recently used
        private DoubleLinkedList leastRecentlyUsed, mostRecentlyUsed;
        public LRUCache(int capacity) {
            this.capacity = capacity;
            currentSize = 0;
            cache = new HashMap<>();

            leastRecentlyUsed = new DoubleLinkedList();
            mostRecentlyUsed = new DoubleLinkedList();

            leastRecentlyUsed.next = mostRecentlyUsed;
            leastRecentlyUsed.prev = null;

            mostRecentlyUsed.prev = leastRecentlyUsed;
            mostRecentlyUsed.next = null;
        }

        public int get(int key) {

            DoubleLinkedList currNode = cache.get(key);
            //Element not found
            if(currNode == null) return -1;

            //We have make this node as most recently used node now - remove from list and add as most recently used
            this.removeFromList(currNode);
            this.addToList(currNode);

            return currNode.value;

        }

        //Adds to end of list - mostRecentlyUsed
        private void addToList(DoubleLinkedList currNode) {

            currNode.prev = mostRecentlyUsed.prev;
            mostRecentlyUsed.prev.next = currNode;

            currNode.next = mostRecentlyUsed;
            mostRecentlyUsed.prev = currNode;
        }
        //Removed a node from List
        private void removeFromList(DoubleLinkedList currNode) {
            currNode.prev.next = currNode.next;
            currNode.next.prev = currNode.prev;
        }

        public void put(int key, int value) {
            //Key is already present - we update the value and make the element as most recently used
            if(cache.containsKey(key)){
                DoubleLinkedList currNode = cache.get(key);
                currNode.value = value;
                //We have make this node as most recently used node now - remove from list and add as most recently used
                this.removeFromList(currNode);
                this.addToList(currNode);
            }
            //Not present
            else {
                //Add to cache and list - make it most recently used
                DoubleLinkedList newNode = new DoubleLinkedList(key,value);
                this.addToList(newNode);
                this.cache.put(key,newNode);
                currentSize += 1;

                //Have we crossed limit?
                if(currentSize > capacity){
                    //Remove from cache and list , reduce current size
                    //See how easily we can get address of least recently used and most recently used by maintaining dummy nodes
                    this.cache.remove(leastRecentlyUsed.next.key);
                    this.removeFromList(leastRecentlyUsed.next);
                    currentSize -=1;
                }
            }
        }
    }
    /* LFU Cache - Design and implement a data structure for a Least Frequently Used (LFU) cache.
    Implement the LFUCache class:
        * LFUCache(int capacity) Initializes the object with the capacity of the data structure.
        * int get(int key) Gets the value of the key if the key exists in the cache. Otherwise, returns -1.
        * void put(int key, int value) Update the value of the key if present, or inserts the key if not already present. When the cache reaches its capacity, it should invalidate and remove the least frequently used key before inserting a new item.
          For this problem, when there is a tie (i.e., two or more keys with the same frequency), the least recently used key would be invalidated.

   To determine the least frequently used key, a use counter is maintained for each key in the cache. The key with the smallest use counter is the least frequently used key.
   When a key is first inserted into the cache, its use counter is set to 1 (due to the put operation). The use counter for a key in the cache is incremented either a get or put operation is called on it.
   The functions get and put must each run in O(1) average time complexity.

   https://leetcode.com/problems/lfu-cache/

     */
    class LFUCache {

        public LFUCache(int capacity) {

        }

        public int get(int key) {

        }

        public void put(int key, int value) {

        }
    }

    /* Design Circular Queue - Design your implementation of the circular queue. The circular queue is a linear data structure in which the operations are performed based on FIFO (First In First Out) principle and the last position is connected back to the first position to make a circle. It is also called "Ring Buffer".
       One of the benefits of the circular queue is that we can make use of the spaces in front of the queue. In a normal queue, once the queue becomes full, we cannot insert the next element even if there is a space in front of the queue. But using the circular queue, we can use the space to store new values.
       Implementation the MyCircularQueue class:
        MyCircularQueue(k) Initializes the object with the size of the queue to be k.
        int Front() Gets the front item from the queue. If the queue is empty, return -1.
        int Rear() Gets the last item from the queue. If the queue is empty, return -1.
        boolean enQueue(int value) Inserts an element into the circular queue. Return true if the operation is successful.
        boolean deQueue() Deletes an element from the circular queue. Return true if the operation is successful.
        boolean isEmpty() Checks whether the circular queue is empty or not.
        boolean isFull() Checks whether the circular queue is full or not.
        You must solve the problem without using the built-in queue data structure in your programming language.
        https://leetcode.com/problems/design-circular-queue/

        We will maintain two dummy nodes to mark front and rear of list - rest of implementation is pretty straight forward.
     */
    class MyCircularQueue {
        DoubleLinkedList front,rear;
        int size;
        int capacity;

        public MyCircularQueue(int k) {
            front = new DoubleLinkedList();
            rear = new DoubleLinkedList();

            front.next = rear;
            front.prev = null;

            rear.prev = front;
            rear.next = null;

            this.capacity = k;
            size = 0;
        }

        public boolean enQueue(int value) {
            if(isFull()) return false;

            DoubleLinkedList newNode = new DoubleLinkedList(value);

            newNode.prev = rear.prev;
            rear.prev.next = newNode;

            newNode.next = rear;
            rear.prev = newNode;

            size += 1;

            return true;
        }

        public boolean deQueue() {
            if(isEmpty()) return false;

            front.next = front.next.next;
            //Note here that front.next is already pointing to next of element to be deleted.
            front.next.prev = front;

            size -= 1;
            return true;
        }

        public int Front() {
            if(isEmpty()) return -1;
            return front.next.value;
        }

        public int Rear() {
            if(isEmpty()) return -1;
            return rear.prev.value;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }
    }
}
