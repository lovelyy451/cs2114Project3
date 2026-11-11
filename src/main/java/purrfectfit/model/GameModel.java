
package purrfectfit.model;

import java.util.function.LongSupplier;

/**
 * Manages the gameplay state of Purrfect Fit.
 *
 * Controls levels, the customer queue, outfit submissions,
 * scoring, timing, and progression.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class GameModel
{
    private static final int MAX_LEVEL = 15;
    private static final int QUEUE_CAPACITY = 3;
    private static final int POINTS_PER_OUTFIT = 10;
    private static final long NANOS_PER_SECOND = 1_000_000_000L;

    private int currentLevel;
    private int totalScore;
    private int levelScore;
    private int completedOutfits;
    private int minimumOutfits;
    private int timeLimitSeconds;
    private long levelStartTime;

    private final LinkedQueue<Customer> customers;
    private final CustomerGenerator customerGenerator;
    private final LongSupplier nanoTimeSource;

    private GameStatus status;
    private long totalPausedNanos;


    /**
     * Creates a game model with standard customer generation
     * and the system's monotonic clock.
     */
    public GameModel()
    {
        this(new CustomerGenerator(), System::nanoTime);
    }


    /**
     * Creates a game model with injectable dependencies.
     *
     * @param generator
     *            the customer generator
     * @param timeSource
     *            the source of monotonic time in nanoseconds
     * @throws IllegalArgumentException
     *             if either dependency is null
     */
    public GameModel(
        CustomerGenerator generator,
        LongSupplier timeSource)
    {
        if (generator == null || timeSource == null)
        {
            throw new IllegalArgumentException(
                "Game model dependencies cannot be null.");
        }

        customerGenerator = generator;
        nanoTimeSource = timeSource;
        customers = new LinkedQueue<Customer>();

        currentLevel = 0;
        totalScore = 0;
        levelScore = 0;
        completedOutfits = 0;
        minimumOutfits = 0;
        timeLimitSeconds = 0;
        levelStartTime = 0L;
        totalPausedNanos = 0L;

        status = GameStatus.NOT_STARTED;
    }


    /**
     * Starts a valid level and prepares its initial customers.
     *
     * The first customer's dialogue is prepared before
     * the countdown begins.
     *
     * @param level
     *            the level number to start
     * @return true if the level starts successfully
     */
    public boolean startLevel(int level)
    {
        if (level < 1 || level > MAX_LEVEL)
        {
            return false;
        }

        if (status == GameStatus.RUNNING
            || status == GameStatus.FINISHED)
        {
            return false;
        }

        if (status == GameStatus.NOT_STARTED && level != 1)
        {
            return false;
        }

        if (status == GameStatus.PASSED
            && level != currentLevel + 1)
        {
            return false;
        }

        if (status == GameStatus.FAILED
            && level != currentLevel)
        {
            return false;
        }

        initializeLevel(level);
        return true;
    }


    /**
     * Submits an outfit for the current customer.
     *
     * Correct submissions award temporary points and
     * advance the FIFO queue.
     *
     * @param selectedOutfit
     *            the outfit selected by the player
     * @return the result of the submission
     */
    public SubmissionResult submitOutfit(Outfit selectedOutfit)
    {
        if (status != GameStatus.RUNNING)
        {
            return SubmissionResult.EXPIRED;
        }

        updateTime();

        if (status != GameStatus.RUNNING)
        {
            return SubmissionResult.EXPIRED;
        }

        if (selectedOutfit == null)
        {
            return SubmissionResult.INVALID;
        }

        Customer currentCustomer = getCurrentCustomer();

        if (currentCustomer == null)
        {
            return SubmissionResult.INVALID;
        }

        if (!currentCustomer.getCorrectOutfit().matches(
            selectedOutfit))
        {
            return SubmissionResult.INCORRECT;
        }

        levelScore += POINTS_PER_OUTFIT;
        completedOutfits++;

        customers.dequeue();
        customers.enqueue(customerGenerator.generateCustomer());

        prepareCurrentCustomerDialogue();

        return SubmissionResult.CORRECT;
    }


    /**
     * Returns the customer at the front of the queue.
     *
     * @return the current customer, or null if unavailable
     */
    public Customer getCurrentCustomer()
    {
        if (status != GameStatus.RUNNING
            || customers.isEmpty())
        {
            return null;
        }

        return customers.peek();
    }


    /**
     * Returns the remaining level time in seconds.
     *
     * @return the remaining time, never below zero
     */
    public int getRemainingTime()
    {
        if (status != GameStatus.RUNNING)
        {
            return 0;
        }

        long elapsedNanos =
            nanoTimeSource.getAsLong()
                - levelStartTime
                - totalPausedNanos;

        if (elapsedNanos < 0)
        {
            elapsedNanos = 0;
        }

        long remainingNanos =
            (long)timeLimitSeconds * NANOS_PER_SECOND
                - elapsedNanos;

        if (remainingNanos <= 0)
        {
            return 0;
        }

        return (int)(
            (remainingNanos + NANOS_PER_SECOND - 1)
                / NANOS_PER_SECOND);
    }


    /**
     * Returns the minimum outfits needed to pass.
     *
     * @return the minimum outfit requirement
     */
    public int getMinimumOutfits()
    {
        return minimumOutfits;
    }


    /**
     * Returns the number of completed outfits.
     *
     * @return the number of correctly completed outfits
     */
    public int getCompletedOutfits()
    {
        return completedOutfits;
    }


    /**
     * Returns the cumulative score from passed levels.
     *
     * @return the cumulative score
     */
    public int getTotalScore()
    {
        return totalScore;
    }


    /**
     * Returns the temporary score for the current attempt.
     *
     * @return the current level score
     */
    public int getLevelScore()
    {
        return levelScore;
    }


    /**
     * Returns the current level number.
     *
     * @return the current level, or zero before starting
     */
    public int getCurrentLevel()
    {
        return currentLevel;
    }


    /**
     * Returns the current game status.
     *
     * @return the current game status
     */
    public GameStatus getStatus()
    {
        return status;
    }


    /**
     * Checks whether the level timer has expired.
     *
     * When time expires, the level is passed if enough
     * outfits were completed. Otherwise, it is failed.
     */
    public void updateTime()
    {
        if (status != GameStatus.RUNNING)
        {
            return;
        }

        if (getRemainingTime() > 0)
        {
            return;
        }

        if (completedOutfits >= minimumOutfits)
        {
            totalScore += levelScore;
            status = GameStatus.PASSED;
        }
        else
        {
            levelScore = 0;
            status = GameStatus.FAILED;
        }
    }


    /**
     * Restarts the current level after failure.
     *
     * @return true if the level was restarted
     */
    public boolean retryLevel()
    {
        if (status != GameStatus.FAILED)
        {
            return false;
        }

        initializeLevel(currentLevel);
        return true;
    }


    /**
     * Advances to the next level after passing.
     *
     * Passing Level 15 allows the player to finish
     * the game by selecting Next.
     *
     * @return true if progression was successful
     */
    public boolean nextLevel()
    {
        if (status != GameStatus.PASSED)
        {
            return false;
        }

        if (currentLevel == MAX_LEVEL)
        {
            status = GameStatus.FINISHED;
            return true;
        }

        initializeLevel(currentLevel + 1);
        return true;
    }


    /**
     * Ends the game session.
     *
     * Discards unfinished level points while preserving
     * the cumulative score from previously passed levels.
     */
    public void exitGame()
    {
        if (status == GameStatus.RUNNING
            || status == GameStatus.FAILED)
        {
            levelScore = 0;
        }

        status = GameStatus.FINISHED;
    }


    /**
     * Initializes the settings, progress, and queue
     * for the specified level.
     *
     * Prepares initial dialogue before starting the clock.
     *
     * @param level
     *            the level to initialize
     */
    private void initializeLevel(int level)
    {
        currentLevel = level;
        levelScore = 0;
        completedOutfits = 0;

        minimumOutfits = calculateMinimumOutfits(level);
        timeLimitSeconds = calculateTimeLimit(level);

        while (!customers.isEmpty())
        {
            customers.dequeue();
        }

        for (int i = 0; i < QUEUE_CAPACITY; i++)
        {
            customers.enqueue(customerGenerator.generateCustomer());
        }

        // Prepare initial dialogue before the countdown starts.
        customerGenerator.prepareDialogue(customers.peek());

        // Reset timing for this level only.
        totalPausedNanos = 0L;
        levelStartTime = nanoTimeSource.getAsLong();

        status = GameStatus.RUNNING;
    }


    /**
     * Prepares dialogue for the front customer.
     *
     * The time spent generating dialogue is excluded
     * from the active level countdown.
     */
    private void prepareCurrentCustomerDialogue()
    {
        if (customers.isEmpty())
        {
            return;
        }

        Customer customer = customers.peek();

        if (customer.getDialogue() != null)
        {
            return;
        }

        long pauseStart = nanoTimeSource.getAsLong();

        try
        {
            customerGenerator.prepareDialogue(customer);
        }
        finally
        {
            totalPausedNanos +=
                nanoTimeSource.getAsLong() - pauseStart;
        }
    }


    /**
     * Calculates the minimum outfits required for a level.
     *
     * @param level
     *            the level number
     * @return the minimum number of outfits
     */
    private int calculateMinimumOutfits(int level)
    {
        return level + 1;
    }


    /**
     * Calculates the time limit using the approved
     * 15-level progression formula.
     *
     * @param level
     *            the level number from 1 to 15
     * @return the time limit in seconds
     * @throws IllegalArgumentException
     *             if the level is outside 1 to 15
     */
    private int calculateTimeLimit(int level)
    {
        if (level < 1 || level > MAX_LEVEL)
        {
            throw new IllegalArgumentException(
                "Level must be between 1 and 15.");
        }

        int requiredOutfits = calculateMinimumOutfits(level);
        int secondsPerOutfit;

        if (level <= 5)
        {
            secondsPerOutfit = 30;
        }
        else if (level <= 10)
        {
            secondsPerOutfit = 20;
        }
        else
        {
            secondsPerOutfit = 10;
        }

        int spareTime = Math.max(0, 35 - 5 * level);

        return requiredOutfits * secondsPerOutfit + spareTime;
    }
}