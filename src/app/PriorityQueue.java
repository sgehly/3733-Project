package app;
import java.util.*;

public class PriorityQueue{

    private ArrayList<Node>heap;
    private Node end;
    private Node start;

    //constructor for min heap
    public PriorityQueue(Node start, Node end){
        heap = new ArrayList<Node>();
        this.start = start;
        this.end = end;
    }

    //getter for the heap
    public ArrayList<Node> getHeap() {
        return heap;
    }

    //get size of heap
    public int getHeapSize(){
        return this.getHeap().size();
    }

    //function to make the whole array into a heap
    public void buildHeap(){
        //getting the size of the heap
        int size = this.getHeap().size();
        //dividing the size in half gets us the index of the last element that has children
        int i = size / 2;
        //for every node with children, heapify that sub-heap, and move up the tree
        while(i >= 0){
            heapify(i);
            i--;
        }
    }
    //adding something to the heap
    public void addtoHeap(Node n){
        ArrayList<Node> heap = this.getHeap();
        heap.add(n);
        this.buildHeap();
    }

    //function that makes a subtree into a heap, index of parent of subtree is i
    public void heapify(int i){
        ArrayList<Node>heap = this.getHeap();
        int size = heap.size(); //size of the heap
        int leftChildIndex = (2*i) + 1; //the index of a node's left child
        int rightChildIndex = (2*i) + 2; //the index of a node's right child
        Node n = heap.get(i); //easier way to access the node
        //Node leftChild = heap.get(leftChildIndex); //left child of n
        //Node rightChild = heap.get(rightChildIndex); //right child of n
        int smallest = 0; //smallest node (f score) in the the parent/leftchild/rightchild sub-heap

        //figuring out which node in subtree has the smallest f score
        if(leftChildIndex < size && heap.get(leftChildIndex).getF() < n.getF()){
            smallest = leftChildIndex;
        }
        else{
            smallest = i;
        }
        if(rightChildIndex < size && heap.get(rightChildIndex).getF() < heap.get(smallest).getF()){
            smallest = rightChildIndex;
        }
        if(smallest != i){ //a swap is necessary if the smallest one isn't the parent
            Node temp = n;
            heap.set(i, heap.get(smallest));
            heap.set(smallest, temp);
            heapify(smallest);
        }
    }

    public boolean findNode(Node node){
        for(Node n : this.getHeap()){
            if(n.getId().equals(node.getId())) {
                return true;
            }
        }
        return false;
    }
}