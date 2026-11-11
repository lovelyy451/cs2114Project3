package purrfectfit.model;

import java.util.NoSuchElementException;

/**
 * Represents a generic singly-linked FIFO queue.
 *
 * Elements are added at the rear and removed from the front.
 * The queue is used to manage customers in Purrfect Fit.
 *
 * @param <T>
 *            the type of elements stored in the queue
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class LinkedQueue<T>
{
    private Node<T> front;
    private Node<T> rear;
    private int size;

    /**
     * Represents a single node in the linked queue.
     *
     * @param <T>
     *            the type of data stored in the node
     */
    private static class Node<T>
    {
        private T data;
        private Node<T> next;

        /**
         * Creates a node containing the specified data.
         *
         * @param data
         *            the data stored in the node
         */
        public Node(T data)
        {
            this.data = data;
            this.next = null;
        }
    }


    /**
     * Creates an empty linked queue.
     */
    public LinkedQueue()
    {
        front = null;
        rear = null;
        size = 0;
    }


    /**
     * Adds an element to the rear of the queue.
     *
     * @param item
     *            the element to add
     * @throws IllegalArgumentException
     *             if the item is null
     */
    public void enqueue(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException(
                "Queue cannot contain null elements.");
        }

        Node<T> newNode = new Node<T>(item);

        if (isEmpty())
        {
            front = newNode;
        }
        else
        {
            rear.next = newNode;
        }

        rear = newNode;
        size++;
    }


    /**
     * Removes and returns the element at the front
     * of the queue.
     *
     * @return the removed front element
     * @throws NoSuchElementException
     *             if the queue is empty
     */
    public T dequeue()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException(
                "Cannot dequeue from an empty queue.");
        }

        T removed = front.data;
        front = front.next;
        size--;

        if (isEmpty())
        {
            rear = null;
        }

        return removed;
    }


    /**
     * Returns the front element without removing it.
     *
     * @return the front element
     * @throws NoSuchElementException
     *             if the queue is empty
     */
    public T peek()
    {
        if (isEmpty())
        {
            throw new NoSuchElementException(
                "Cannot peek at an empty queue.");
        }

        return front.data;
    }


    /**
     * Determines whether the queue contains no elements.
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return size == 0;
    }
}