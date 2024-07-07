import java.util.HashMap;

class LRUCache {

    class Node {
        int key, value; // Each node will store a key-value pair
        Node prev, next; // Pointers to the previous and next nodes in the doubly linked list
    }

    // Method to add a node right after the head (i.e., the front of the list)
    private void addNode(Node node) {
        Node neighbour = head.next; // The node that is currently right after the head

        node.next = neighbour; // Set the new node's next to the current first node
        node.prev = head; // Set the new node's previous to head

        neighbour.prev = node; // Update the current first node's previous to the new node
        head.next = node; // Update the head's next to the new node
    }

    // Method to remove a node from its current position in the list
    private void removeNode(Node node) {
        Node prev_neighbour = node.prev; // Node before the one to be removed
        Node next_neighbour = node.next; // Node after the one to be removed

        prev_neighbour.next = next_neighbour; // Update previous node's next to skip the removed node
        next_neighbour.prev = prev_neighbour; // Update next node's previous to skip the removed node

        node.next = node.prev = null; // Clean up the removed node's pointers
    }

    // Method to move a given node to the front (right after head), marking it as recently used
    private void moveToFront(Node node) {
        removeNode(node); // Remove the node from its current position
        addNode(node); // Add the node to the front (right after head)
    }

    private HashMap<Integer, Node> cache; // HashMap to store the cache items for O(1) access
    private int cap; // Maximum capacity of the cache
    private Node head, tail; // Dummy head and tail nodes to facilitate easy addition/removal

    public LRUCache(int capacity) {
        cache = new HashMap<>(); // Initialize the cache with a HashMap
        cap = capacity; // Set the capacity of the cache
        head = new Node(); // Create dummy head node
        tail = new Node(); // Create dummy tail node

        head.next = tail; // Point head's next to tail
        tail.prev = head; // Point tail's prev to head
    }

    public int get(int key) {
        Node node = cache.get(key); // Retrieve the node from the cache

        if (node == null) return -1; // If the node is not found, return -1

        moveToFront(node); // Move the accessed node to the front (most recently used)
        return node.value; // Return the value of the node
    }

    public void put(int key, int value) {
        Node node = cache.get(key); // Check if the key already exists in the cache

        if (node == null) { // If the key does not exist
            Node newNode = new Node(); // Create a new node
            newNode.key = key; // Set the key of the new node
            newNode.value = value; // Set the value of the new node

            if (cache.size() == cap) { // If the cache is at full capacity
                Node LRU_Node = tail.prev; // Identify the least recently used node (node before tail)
                cache.remove(LRU_Node.key); // Remove the LRU node from the cache
                removeNode(LRU_Node); // Remove the LRU node from the linked list
            }

            cache.put(key, newNode); // Add the new node to the cache
            addNode(newNode); // Add the new node to the front of the linked list

        }
         else { // If the key already exists
            node.value = value; // Update the value of the existing node
            moveToFront(node); // Move the updated node to the front
        }
    }

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2); // Create an LRUCache with capacity 2
        lruCache.put(1, 1); // cache is {1=1}
        lruCache.put(2, 2); // cache is {1=1, 2=2}
        System.out.println(lruCache.get(1));    // returns 1
        lruCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        System.out.println(lruCache.get(2));    // returns -1 (not found)
        lruCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
        System.out.println(lruCache.get(1));    // returns -1 (not found)
        System.out.println(lruCache.get(3));    // returns 3
        System.out.println(lruCache.get(4));    // returns 4
    }
}
