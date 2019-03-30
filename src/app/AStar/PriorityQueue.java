package app.AStar;

import java.util.ArrayList;
import java.util.List;

/**
 * Optimized Priority Queue
 *
 * Updated addToQueue() to operate in O(long(n) rather than O(n log(n))
 *
 * @param <T>
 */
public class PriorityQueue <T extends Comparable> {

    private List<T> heap;

    public PriorityQueue(){
        heap = new ArrayList<>();
    }

    /**
     * Get parent's index
     * @param i - Child index
     * @return Parent's index
     */
    private int getParent(int i){
        return (int) (i - 1) / 2;
    }

    /**
     * Add a node to the queue
     * @param n - The node to  add
     */
    public void addToQueue(T n){
        heap.add(n);
        placeNode(n, heap.size() - 1);
    }

    /**
     * Place node into queue
     * @param n - Node to insert
     * @param i - The index of the node
     */
    private void placeNode(T n, int i){
        int parentIndex = getParent(i);
        if (n.compareTo(heap.get(parentIndex)) < 0){ // min heap, if n < parent swap
            swap(i, parentIndex);
            placeNode(n, parentIndex);
        }
    }

    /**
     * Swaps two nodes
     * @param i1 - First index
     * @param i2 - Second index
     */
    private void swap(int i1, int i2){
        T temp = heap.get(i1);
        heap.set(i1, heap.get(i2));
        heap.set(i2, temp);
    }

    /**
     * Pops a node off the stack
     * @return - The minimum node in the queue
     */
    public T pop(){
        T root = heap.get(0);

        heap.remove(0);
        if(heap.size() > 1) placeNode(heap.get(1), 1); // check other element to see if bigger
        if(heap.size() > 2) placeNode(heap.get(2), 2); // check other element to see if bigger

        return root;
    }

    /**
     * Get the queue
     * @return - The queue
     */
    public List<T> getQueue(){
        return heap;
    }

    /**
     * Get the length of the queue
     * @return The queue's length
     */
    public int getLength(){
        return heap.size();
    }
}
