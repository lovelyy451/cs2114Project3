
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the GameStatus enum to verify that all five
 * game states are defined correctly.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class GameStatusTest
{
    /**
     * Tests that the GameStatus enum contains exactly
     * NOT_STARTED, RUNNING, PASSED, FAILED, and FINISHED
     * in the expected order.
     */
    @Test
    public void testGameStatuses()
    {
        GameStatus[] statuses = GameStatus.values();

        assertEquals(5, statuses.length);
        assertEquals(GameStatus.NOT_STARTED, statuses[0]);
        assertEquals(GameStatus.RUNNING, statuses[1]);
        assertEquals(GameStatus.PASSED, statuses[2]);
        assertEquals(GameStatus.FAILED, statuses[3]);
        assertEquals(GameStatus.FINISHED, statuses[4]);
    }
}
