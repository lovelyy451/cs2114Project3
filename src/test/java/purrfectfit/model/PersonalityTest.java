
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the Personality enum to verify that all four
 * customer personalities are defined correctly.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class PersonalityTest
{
    /**
     * Tests that the Personality enum contains exactly
     * AGGRESSIVE, KIND, IMPATIENT, and RESPECTFUL
     * in the expected order.
     */
    @Test
    public void testPersonalities()
    {
        Personality[] personalities = Personality.values();

        assertEquals(4, personalities.length);
        assertEquals(Personality.AGGRESSIVE, personalities[0]);
        assertEquals(Personality.KIND, personalities[1]);
        assertEquals(Personality.IMPATIENT, personalities[2]);
        assertEquals(Personality.RESPECTFUL, personalities[3]);
    }
}
