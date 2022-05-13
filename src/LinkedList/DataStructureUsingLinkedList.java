package LinkedList;

public class DataStructureUsingLinkedList {
    public static void main(String[] args) {

    }
    /* LRU Cache - Design a data structure that follows the constraints of a Least Recently Used (LRU) cache. Implement the LRUCache class:
       LRUCache(int capacity) Initialize the LRU cache with positive size capacity.
       int get(int key) Return the value of the key if the key exists, otherwise return -1.
       void put(int key, int value) Update the value of the key if the key exists. Otherwise, add the key-value pair to the cache. If the number of keys exceeds the capacity from this operation, evict the least recently used key.
       The functions get and put must each run in O(1) average time complexity.
       https://leetcode.com/problems/lru-cache/
     */
    class LRUCache {

        public LRUCache(int capacity) {

        }

        public int get(int key) {

            return 0;
        }

        public void put(int key, int value) {

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

     */
    class MyCircularQueue {

        public MyCircularQueue(int k) {

        }

        public boolean enQueue(int value) {

        }

        public boolean deQueue() {

        }

        public int Front() {

        }

        public int Rear() {

        }

        public boolean isEmpty() {

        }

        public boolean isFull() {

        }
    }
}
