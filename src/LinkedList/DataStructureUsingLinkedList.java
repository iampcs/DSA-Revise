package LinkedList;

import java.util.HashMap;
import java.util.Map;

public class DataStructureUsingLinkedList {
    public static void main(String[] args) {

    }
    static class DoubleLinkedList{
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
    /* LRU Cache - Design a data structure that follows the constraints of the Least Recently Used (LRU) cache. Implement the LRUCache class:
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
        private final int capacity;
        //Dummy nodes to keep track of least recently used and most recently used
        private final DoubleLinkedList leastRecentlyUsed;
        private final DoubleLinkedList mostRecentlyUsed;
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
                DoubleLinkedList newNode = new DoubleLinkedList(key, value);
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
   We will be implementing this approach - https://www.youtube.com/watch?v=0PSB9y8ehbk
   Will have two HashMap - One containing - (key,value) pair and another containing (frequency, [nodes]) - frequency and list of nodes with that frequency.
   In summary :
   1. Increase frequency of a node(key,value) when we are getting, putting or updating it. Move it in frequencyMap to next frequency
   2. Maintain current minimum frequency - In case of size overflow - remove from frequency-map and map
   3. In case of a tie - where two nodes are having same frequency - we will remove the LRU one , can check the  LRUCache class to see how we are maintaining nodes for getting LRU

     */
    public class LFUCache {
        class Node {
            int key, val, frequency;
            Node prev, next;
            Node(int key, int val) {
                this.key = key;
                this.val = val;
                frequency = 1;
            }
        }

        class DoubleLinkedList {
            //Dummy nodes to keep track of MRU and LRU
            Node mostRecentlyUsed, leastRecentlyUsed;
            int size;
            DoubleLinkedList() {
                mostRecentlyUsed = new Node(0, 0);
                leastRecentlyUsed = new Node(0, 0);
                mostRecentlyUsed.next = leastRecentlyUsed;
                leastRecentlyUsed.prev = mostRecentlyUsed;
            }
            //Add to next of MRU
            void add(Node node) {
                mostRecentlyUsed.next.prev = node;
                node.next = mostRecentlyUsed.next;
                node.prev = mostRecentlyUsed;
                mostRecentlyUsed.next = node;
                size += 1;
            }
            //Removes node
            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
                size -= 1;
            }
            //Removes LRU in case of a cache overflow
            Node removeLRU() {
                if (size > 0) {
                    Node node = leastRecentlyUsed.prev;
                    remove(node);
                    return node;
                }
                else return null;
            }
        }

        int capacity, size, leastFrequency;
        //To check if node exists in cache
        Map<Integer, Node> nodeMap;
        //Stores frequency of nodes as a linkedList - as LRU cache format
        Map<Integer, DoubleLinkedList> frequencyMap;
        public LFUCache(int capacity) {
            this.capacity = capacity;
            nodeMap = new HashMap<>();
            frequencyMap = new HashMap<>();
        }

        public int get(int key) {
            //Get node from nodeMap - if absent return -1
            Node node = nodeMap.get(key);
            if (node == null) return -1;

            //Node is present - update frequencyMap
            updateFrequencyMap(node);
            //return value
            return node.val;
        }

        public void put(int key, int value) {
            //Edge case when capacity is zero
            if (capacity == 0) return;
            Node node;
            //If node exists in cache - update value - update frequencyMap
            if (nodeMap.containsKey(key)) {
                node = nodeMap.get(key);
                node.val = value;
                updateFrequencyMap(node);
            }
            //Create node - add it to cache - add it to frequencyCache
            //If size exceeds capacity - remove LFU + LRU element - update leastFrequency if changed - update size
            else {
                //Create new node - add to cache - increment size
                node = new Node(key, value);
                nodeMap.put(key, node);
                size += 1;

                //Size overflow - remove LFU + LRU element - reduce size
                if (size > capacity) {
                    DoubleLinkedList currentList = frequencyMap.get(leastFrequency);
                    //Remove from frequencyMap as well as cache
                    nodeMap.remove(currentList.removeLRU().key);
                    size -= 1;
                }

                //New element inserted - its frequency will be 1
                leastFrequency = 1;
                //Add to frequencyMap - we are creating an LRU list if it's not already present - adding to LRU list and then adding this LRU list to frequencyMap
                DoubleLinkedList newList = frequencyMap.getOrDefault(node.frequency, new DoubleLinkedList());
                newList.add(node);
                frequencyMap.put(node.frequency, newList);
            }
        }

        private void updateFrequencyMap(Node node) {
            //Frequency will be updated - remove from old frequencyMap key
            DoubleLinkedList oldList = frequencyMap.get(node.frequency);
            oldList.remove(node);

            //Is this node last node from leastFrequency key in frequencyMap ?
            //Update leastFrequency - Why leastFrequency += 1 ? Because current node frequency has increased by 1, and its guaranteed that we will have a node at (leastFrequency + 1) position
            if (node.frequency == leastFrequency && oldList.size == 0) leastFrequency += 1;

            //Add to frequencyMap - we are creating an LRU list if it's not already present - adding to LRU list and then adding this LRU list to frequencyMap
            node.frequency += 1;
            DoubleLinkedList newList = frequencyMap.getOrDefault(node.frequency, new DoubleLinkedList());
            newList.add(node);
            frequencyMap.put(node.frequency, newList);
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
