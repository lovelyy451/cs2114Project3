
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/**
 * Tests the LinkedQueue class to verify FIFO behavior,
 * queue operations, empty-state handling, and invalid inputs.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class LinkedQueueTest
{
    private LinkedQueue<String> queue;

    /**
     * Initializes an empty linked queue for testing.
     */
    public void setUp()
    {
        queue = new LinkedQueue<String>();
    }


    /**
     * Tests that a newly constructed queue is empty.
     */
    @Test
    public void testInitiallyEmpty()
    {
        setUp();

        assertEquals(true, queue.isEmpty());
    }


    /**
     * Tests that enqueue adds an element to an empty
     * queue and makes it nonempty.
     */
    @Test
    public void testEnqueueEmptyQueue()
    {
        setUp();

        queue.enqueue("First");

        assertEquals(false, queue.isEmpty());
        assertEquals("First", queue.peek());
    }


    /**
     * Tests that enqueue adds elements to the rear
     * and dequeue removes them in FIFO order.
     */
    @Test
    public void testEnqueueNonEmptyQueue()
    {
        setUp();

        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");

        assertEquals("First", queue.dequeue());
        assertEquals("Second", queue.dequeue());
        assertEquals("Third", queue.dequeue());
        assertEquals(true, queue.isEmpty());
    }


    /**
     * Tests that enqueue rejects null elements
     * with an IllegalArgumentException.
     */
    @Test
    public void testEnqueueNull()
    {
        setUp();

        try
        {
            queue.enqueue(null);
            assertEquals("Exception expected", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Queue cannot contain null elements.",
                e.getMessage());
        }
    }


    /**
     * Tests that dequeue removes the only element
     * and restores the queue to an empty state.
     */
    @Test
    public void testDequeueLastElement()
    {
        setUp();

        queue.enqueue("First");

        assertEquals("First", queue.dequeue());
        assertEquals(true, queue.isEmpty());

        queue.enqueue("Second");

        assertEquals("Second", queue.peek());
    }


    /**
     * Tests that dequeue throws a NoSuchElementException
     * when the queue is empty.
     */
    @Test
    public void testDequeueEmpty()
    {
        setUp();

        try
        {
            queue.dequeue();
            assertEquals("Exception expected", "No exception");
        }
        catch (NoSuchElementException e)
        {
            assertEquals(
                "Cannot dequeue from an empty queue.",
                e.getMessage());
        }
    }


    /**
     * Tests that peek returns the front element
     * without removing it from the queue.
     */
    @Test
    public void testPeek()
    {
        setUp();

        queue.enqueue("First");
        queue.enqueue("Second");

        assertEquals("First", queue.peek());
        assertEquals("First", queue.peek());
        assertEquals("First", queue.dequeue());
        assertEquals("Second", queue.peek());
    }


    /**
     * Tests that peek throws a NoSuchElementException
     * when the queue is empty.
     */
    @Test
    public void testPeekEmpty()
    {
        setUp();

        try
        {
            queue.peek();
            assertEquals("Exception expected", "No exception");
        }
        catch (NoSuchElementException e)
        {
            assertEquals(
                "Cannot peek at an empty queue.",
                e.getMessage());
        }
    }
}
