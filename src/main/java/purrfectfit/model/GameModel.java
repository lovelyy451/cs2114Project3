
package purrfectfit.model;

import java.util.function.LongSupplier;

/**
 * Manages the gameplay state of Purrfect Fit.
 *
 * Controls levels, the customer queue, outfit submissions,
 * scoring, timing, and progression independently of the view.
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
    private int finalRemainingTime;

    private long levelStartTime;
    private long totalPausedNanos;

    private final LinkedQueue<Customer> customers;
    private final CustomerGenerator customerGenerator;
    private final LongSupplier nanoTimeSource;

    private GameStatus status;


    /**
     * Creates a game model using standard customer generation
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
     *            the monotonic clock in nanoseconds
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
        finalRemainingTime = 0;
        levelStartTime = 0L;
        totalPausedNanos = 0L;

        status = GameStatus.NOT_STARTED;
    }


    /**
     * Starts the first level of a new game.
     *
     * Subsequent levels must be started through nextLevel().
     * Failed levels must be restarted through retryLevel().
     *
     * @param level
     *            the level number, which must be 1
     * @return true if the first level starts successfully
     */
    public boolean startLevel(int level)
    {
        if (status != GameStatus.NOT_STARTED || level != 1)
        {
            return false;
        }

        initializeLevel(level);
        return true;
    }


    /**
     * Submits an outfit for the active customer.
     *
     * Invalid selections do not affect scoring or the queue.
     * A submission at the exact deadline is accepted while
     * the level remains running.
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

        long submissionTime = nanoTimeSource.getAsLong();

        if (getElapsedNanos(submissionTime)
            > (long)timeLimitSeconds * NANOS_PER_SECOND)
        {
            finishLevel();
            return SubmissionResult.EXPIRED;
        }

        if (selectedOutfit == null)
        {
            return SubmissionResult.INVALID;
        }

        Customer currentCustomer = customers.peek();

        if (!isValidSelection(selectedOutfit, currentCustomer))
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
     * Determines whether an outfit uses clothing choices
     * displayed for the current customer.
     *
     * @param outfit
     *            the submitted outfit
     * @param customer
     *            the current customer
     * @return true if all selected items are available
     */
    private boolean isValidSelection(
        Outfit outfit,
        Customer customer)
    {
        if (outfit == null || customer == null)
        {
            return false;
        }

        ClothingItem[][] options = customer.getAvailableOptions();

        ClothingItem[] selected = {
            outfit.getHat(),
            outfit.getTop(),
            outfit.getShoes()
        };

        for (int category = 0; category < selected.length; category++)
        {
            boolean found = false;

            for (int i = 0; i < options[category].length; i++)
            {
                if (selected[category] == options[category][i])
                {
                    found = true;
                    break;
                }
            }

            if (!found)
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Returns the customer at the front of the queue.
     *
     * @return the current customer, or null if inactive
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
     * The last recorded value is preserved when the
     * level is no longer running.
     *
     * @return the remaining time, never below zero
     */
    public int getRemainingTime()
    {
        if (status != GameStatus.RUNNING)
        {
            return finalRemainingTime;
        }

        return calculateRemainingTime(nanoTimeSource.getAsLong());
    }


    /**
     * Calculates remaining time using a clock reading.
     *
     * @param now
     *            the current monotonic time in nanoseconds
     * @return remaining seconds, rounded upward
     */
    private int calculateRemainingTime(long now)
    {
        long elapsedNanos = getElapsedNanos(now);

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
     * Calculates active gameplay time, excluding time spent
     * preparing customer dialogue.
     *
     * @param now
     *            the current monotonic time in nanoseconds
     * @return elapsed gameplay time in nanoseconds
     */
    private long getElapsedNanos(long now)
    {
        return Math.max(
            0L,
            now - levelStartTime - totalPausedNanos);
    }


    /**
     * Returns the minimum outfits required for the level.
     *
     * @return the minimum outfit requirement
     */
    public int getMinimumOutfits()
    {
        return minimumOutfits;
    }


    /**
     * Returns the number of correctly completed outfits.
     *
     * @return the completed outfit count
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
     * @return the game status
     */
    public GameStatus getStatus()
    {
        return status;
    }


    /**
     * Checks whether the current level has expired.
     *
     * The level finishes when its time limit is reached.
     * Submissions are evaluated separately so a submission
     * at the exact deadline can still be accepted.
     */
    public void updateTime()
    {
        if (status != GameStatus.RUNNING)
        {
            return;
        }

        long now = nanoTimeSource.getAsLong();

        if (getElapsedNanos(now)
            >= (long)timeLimitSeconds * NANOS_PER_SECOND)
        {
            finishLevel();
        }
    }


    /**
     * Finalizes the current level based on the number
     * of completed outfits.
     *
     * Passing commits temporary points to the total.
     * Failing discards temporary points.
     */
    private void finishLevel()
    {
        if (status != GameStatus.RUNNING)
        {
            return;
        }

        finalRemainingTime =
            calculateRemainingTime(nanoTimeSource.getAsLong());

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
     * Generates fresh customers and resets temporary
     * progress without changing the cumulative score.
     *
     * @return true if the level restarts successfully
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
     * Advances after a successfully completed level.
     *
     * After Level 15, progression ends the game.
     *
     * @return true if progression succeeds
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
     * Discards unfinished level points and preserves
     * the cumulative score from previously passed levels.
     */
    public void exitGame()
    {
        if (status == GameStatus.RUNNING)
        {
            finalRemainingTime = getRemainingTime();
            levelScore = 0;
        }

        status = GameStatus.FINISHED;
    }


    /**
     * Initializes the specified level with a fresh queue,
     * level settings, and temporary progress.
     *
     * Initial dialogue is prepared before the clock starts.
     *
     * @param level
     *            the level number to initialize
     */
    private void initializeLevel(int level)
    {
        currentLevel = level;
        levelScore = 0;
        completedOutfits = 0;

        minimumOutfits = calculateMinimumOutfits(level);
        timeLimitSeconds = calculateTimeLimit(level);
        finalRemainingTime = 0;

        while (!customers.isEmpty())
        {
            customers.dequeue();
        }

        for (int i = 0; i < QUEUE_CAPACITY; i++)
        {
            customers.enqueue(customerGenerator.generateCustomer());
        }

        customerGenerator.prepareDialogue(customers.peek());

        totalPausedNanos = 0L;
        levelStartTime = nanoTimeSource.getAsLong();

        status = GameStatus.RUNNING;
    }


    /**
     * Prepares dialogue when a new customer reaches
     * the front of the queue.
     *
     * Dialogue preparation time is excluded from gameplay.
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
     * Calculates the minimum outfits for a level.
     *
     * @param level
     *            the level number
     * @return the minimum required outfits
     */
    private int calculateMinimumOutfits(int level)
    {
        return level + 1;
    }


    /**
     * Calculates the approved time limit for a level.
     *
     * @param level
     *            the level number from 1 to 15
     * @return the time limit in seconds
     * @throws IllegalArgumentException
     *             if the level is outside the valid range
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