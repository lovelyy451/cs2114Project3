package purrfectfit.model;

/**
 * Represents the possible states of the game in Purrfect Fit.
 *
 * Each status indicates the current stage of gameplay
 * and determines which game actions are permitted.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public enum GameStatus
{
    /**
     * The game has not started yet.
     */
    NOT_STARTED,

    /**
     * A level is currently active.
     */
    RUNNING,

    /**
     * The player has successfully completed a level.
     */
    PASSED,

    /**
     * The player has failed to complete a level.
     */
    FAILED,

    /**
     * The game session has ended.
     */
    FINISHED
}
