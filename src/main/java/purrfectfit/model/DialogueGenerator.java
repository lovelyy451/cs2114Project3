
package purrfectfit.model;

/**
 * Defines the contract for generating customer dialogue
 * in Purrfect Fit.
 *
 * Implementing classes provide dialogue using either
 * an AI service or predefined fallback responses.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public interface DialogueGenerator
{
    /**
     * Generates dialogue for the specified customer.
     *
     * @param customer the customer whose dialogue is generated
     * @return the generated customer dialogue
     */
    String generateDialogue(Customer customer);
}
