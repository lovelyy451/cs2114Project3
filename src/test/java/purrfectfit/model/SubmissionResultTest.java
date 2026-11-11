
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Tests the SubmissionResult enum to verify that all four
 * outfit submission results are defined correctly.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class SubmissionResultTest
{
    /**
     * Tests that the SubmissionResult enum contains exactly
     * CORRECT, INCORRECT, INVALID, and EXPIRED
     * in the expected order.
     */
    @Test
    public void testSubmissionResults()
    {
        SubmissionResult[] results = SubmissionResult.values();

        assertEquals(4, results.length);
        assertEquals(SubmissionResult.CORRECT, results[0]);
        assertEquals(SubmissionResult.INCORRECT, results[1]);
        assertEquals(SubmissionResult.INVALID, results[2]);
        assertEquals(SubmissionResult.EXPIRED, results[3]);
    }
}
