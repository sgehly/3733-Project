package edu.wpi.cs3733.d19.teamM.utilities.AStar;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to represent a stack
 * @param <T> Generic type of the class
 */
public class Stack<T> {

    List<T> stack;

    /**
     * Constructor for the stack class
     */
    public Stack(){
        stack = new ArrayList<>();
    }

    /**
     * Remove top of the stack
     * @return The top of the stack
     */
    public T pop(){
        int size = stack.size() - 1;
        T obj = stack.get(size);
        stack.remove(size);
        return obj;
    }

    /**
     * The top of the stack
     * @return
     */
    public T getTop(){
        if (stack.size() == 0) return null;
        return stack.get(stack.size() - 1);
    }

    /**
     * Add an object of type T to the stack
     * @param obj - The object to add
     */
    public void addToStack(T obj) {
        stack.add(obj);
    }

}
