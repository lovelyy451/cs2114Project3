package purrfectfit.model;

/**
 * Represents the possible results of submitting an outfit
 * to a customer in Purrfect Fit.
 *
 * Each result indicates whether the submitted outfit is
 * correct, incorrect, invalid, or submitted after time expires.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public enum SubmissionResult
{
    /**
     * The submitted outfit matches the customer's correct outfit.
     */
    CORRECT,

    /**
     * The submitted outfit does not match the customer's correct outfit.
     */
    INCORRECT,

    /**
     * The submitted outfit is null, incomplete, or otherwise invalid.
     */
    INVALID,

    /**
     * The outfit was submitted after the level's time limit expired.
     */
    EXPIRED
}
