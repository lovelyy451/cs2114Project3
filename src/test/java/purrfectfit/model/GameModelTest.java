
package purrfectfit.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

/**
 * Tests the GameModel class, including game initialization,
 * outfit submission, queue progression, timing, scoring,
 * level transitions, and game termination.
 *
 * Each test method verifies a distinct gameplay scenario
 * or reachable branch of the model.
 *
 * @author Lovesh Kataria
 * @version 11.11.2026
 */
public class GameModelTest
{
    private static final long SECOND = 1_000_000_000L;

    /**
     * Creates a game model with deterministic customer
     * generation and an injectable clock.
     *
     * @param clock
     *            the clock supplying elapsed time
     * @return the initialized game model
     */
    private GameModel createModel(AtomicLong clock)
    {
        ClothingCatalog catalog =
            new ClothingCatalog(new Random(42));

        CustomerGenerator generator = new CustomerGenerator(
            catalog,
            new FallBackDialogueGenerator(),
            new Random(42));

        return new GameModel(generator, clock::get);
    }


    /**
     * Submits the correct outfit for the current customer.
     *
     * @param model
     *            the active game model
     * @return the submission result
     */
    private SubmissionResult submitCorrect(GameModel model)
    {
        return model.submitOutfit(
            model.getCurrentCustomer().getCorrectOutfit());
    }


    /**
     * Completes the required outfits for an active level.
     *
     * @param model
     *            the active game model
     */
    private void completeRequiredOutfits(GameModel model)
    {
        int required = model.getMinimumOutfits();

        for (int i = 0; i < required; i++)
        {
            assertEquals(
                SubmissionResult.CORRECT,
                submitCorrect(model));
        }
    }


    /**
     * Calculates the expected time limit for a level.
     *
     * @param level
     *            the level number
     * @return the expected time limit in seconds
     */
    private int expectedTimeLimit(int level)
    {
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

        return (level + 1) * secondsPerOutfit
            + Math.max(0, 35 - 5 * level);
    }


    /**
     * Completes and advances through levels until
     * the requested level becomes active.
     *
     * @param model
     *            the game model
     * @param clock
     *            the controllable clock
     * @param targetLevel
     *            the level to reach
     */
    private void advanceToLevel(
        GameModel model,
        AtomicLong clock,
        int targetLevel)
    {
        assertEquals(true, model.startLevel(1));

        for (int level = 1; level < targetLevel; level++)
        {
            completeRequiredOutfits(model);

            clock.addAndGet(expectedTimeLimit(level) * SECOND);
            model.updateTime();

            assertEquals(GameStatus.PASSED, model.getStatus());
            assertEquals(true, model.nextLevel());
        }

        assertEquals(targetLevel, model.getCurrentLevel());
        assertEquals(GameStatus.RUNNING, model.getStatus());
    }


    /**
     * Tests that the default constructor initializes
     * the game without starting a level.
     */
    @Test
    public void testDefaultConstructor()
    {
        GameModel model = new GameModel();

        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
        assertEquals(0, model.getCurrentLevel());
        assertEquals(0, model.getTotalScore());
        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getCompletedOutfits());
        assertEquals(0, model.getMinimumOutfits());
        assertEquals(0, model.getRemainingTime());
        assertEquals(null, model.getCurrentCustomer());
    }


    /**
     * Tests that the parameterized constructor accepts
     * valid dependencies and initializes game state.
     */
    @Test
    public void testValidDependencies()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
        assertEquals(0, model.getCurrentLevel());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that a null customer generator is rejected.
     */
    @Test
    public void testNullGenerator()
    {
        AtomicLong clock = new AtomicLong(0);

        try
        {
            new GameModel(null, clock::get);
            assertEquals("Expected exception", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Game model dependencies cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that a null time source is rejected.
     */
    @Test
    public void testNullTimeSource()
    {
        try
        {
            new GameModel(new CustomerGenerator(), null);
            assertEquals("Expected exception", "No exception");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(
                "Game model dependencies cannot be null.",
                e.getMessage());
        }
    }


    /**
     * Tests that Level 1 starts successfully.
     */
    @Test
    public void testStartFirstLevel()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(true, model.startLevel(1));
        assertEquals(GameStatus.RUNNING, model.getStatus());
        assertEquals(1, model.getCurrentLevel());
        assertEquals(2, model.getMinimumOutfits());
        assertEquals(90, model.getRemainingTime());
        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getCompletedOutfits());
    }


    /**
     * Tests that starting Level 0 is rejected.
     */
    @Test
    public void testStartLevelZero()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(false, model.startLevel(0));
        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
    }


    /**
     * Tests that starting a level other than Level 1
     * before gameplay begins is rejected.
     */
    @Test
    public void testStartHigherLevel()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(false, model.startLevel(2));
        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
    }


    /**
     * Tests that startLevel cannot restart an
     * already running level.
     */
    @Test
    public void testStartWhileRunning()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        assertEquals(false, model.startLevel(1));
        assertEquals(1, model.getCurrentLevel());
        assertEquals(GameStatus.RUNNING, model.getStatus());
    }


    /**
     * Tests that startLevel cannot restart a
     * previously failed level.
     */
    @Test
    public void testStartAfterFailure()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.FAILED, model.getStatus());
        assertEquals(false, model.startLevel(1));
    }


    /**
     * Tests that startLevel cannot restart a
     * successfully completed level.
     */
    @Test
    public void testStartAfterPassing()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.PASSED, model.getStatus());
        assertEquals(false, model.startLevel(1));
    }


    /**
     * Tests that submitting before gameplay starts
     * returns EXPIRED.
     */
    @Test
    public void testSubmitBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(
            SubmissionResult.EXPIRED,
            model.submitOutfit(null));

        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
    }


    /**
     * Tests that submitting a null outfit during
     * gameplay returns INVALID without changing state.
     */
    @Test
    public void testSubmitNullOutfit()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();

        assertEquals(
            SubmissionResult.INVALID,
            model.submitOutfit(null));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getCompletedOutfits());
    }


    /**
     * Tests that a hat not displayed among the
     * customer's choices is rejected.
     */
    @Test
    public void testSubmitInvalidHat()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        Outfit correct = customer.getCorrectOutfit();

        Outfit invalid = new Outfit(
            new ClothingItem("Unknown Hat", ClothingCategory.HAT),
            correct.getTop(),
            correct.getShoes());

        assertEquals(
            SubmissionResult.INVALID,
            model.submitOutfit(invalid));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getLevelScore());
    }


    /**
     * Tests that a top not displayed among the
     * customer's choices is rejected.
     */
    @Test
    public void testSubmitInvalidTop()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        Outfit correct = customer.getCorrectOutfit();

        Outfit invalid = new Outfit(
            correct.getHat(),
            new ClothingItem("Unknown Top", ClothingCategory.TOP),
            correct.getShoes());

        assertEquals(
            SubmissionResult.INVALID,
            model.submitOutfit(invalid));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getCompletedOutfits());
    }


    /**
     * Tests that shoes not displayed among the
     * customer's choices are rejected.
     */
    @Test
    public void testSubmitInvalidShoes()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        Outfit correct = customer.getCorrectOutfit();

        Outfit invalid = new Outfit(
            correct.getHat(),
            correct.getTop(),
            new ClothingItem(
                "Unknown Shoes",
                ClothingCategory.SHOES));

        assertEquals(
            SubmissionResult.INVALID,
            model.submitOutfit(invalid));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getLevelScore());
    }


    /**
     * Tests that an incorrect displayed hat produces
     * an INCORRECT result without scoring.
     */
    @Test
    public void testIncorrectHat()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        ClothingItem[][] options = customer.getAvailableOptions();
        Outfit correct = customer.getCorrectOutfit();

        ClothingItem wrongHat = options[0][0];

        if (wrongHat == correct.getHat())
        {
            wrongHat = options[0][1];
        }

        Outfit incorrect = new Outfit(
            wrongHat,
            correct.getTop(),
            correct.getShoes());

        assertEquals(
            SubmissionResult.INCORRECT,
            model.submitOutfit(incorrect));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getLevelScore());
    }


    /**
     * Tests that an incorrect displayed top produces
     * an INCORRECT result without advancing the queue.
     */
    @Test
    public void testIncorrectTop()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        ClothingItem[][] options = customer.getAvailableOptions();
        Outfit correct = customer.getCorrectOutfit();

        ClothingItem wrongTop = options[1][0];

        if (wrongTop == correct.getTop())
        {
            wrongTop = options[1][1];
        }

        Outfit incorrect = new Outfit(
            correct.getHat(),
            wrongTop,
            correct.getShoes());

        assertEquals(
            SubmissionResult.INCORRECT,
            model.submitOutfit(incorrect));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getCompletedOutfits());
    }


    /**
     * Tests that incorrect displayed shoes produce
     * an INCORRECT result without scoring.
     */
    @Test
    public void testIncorrectShoes()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();
        ClothingItem[][] options = customer.getAvailableOptions();
        Outfit correct = customer.getCorrectOutfit();

        ClothingItem wrongShoes = options[2][0];

        if (wrongShoes == correct.getShoes())
        {
            wrongShoes = options[2][1];
        }

        Outfit incorrect = new Outfit(
            correct.getHat(),
            correct.getTop(),
            wrongShoes);

        assertEquals(
            SubmissionResult.INCORRECT,
            model.submitOutfit(incorrect));

        assertEquals(customer, model.getCurrentCustomer());
        assertEquals(0, model.getLevelScore());
    }


    /**
     * Tests that submitting the correct outfit
     * increases temporary scoring and progress.
     */
    @Test
    public void testCorrectSubmission()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        assertEquals(
            SubmissionResult.CORRECT,
            submitCorrect(model));

        assertEquals(10, model.getLevelScore());
        assertEquals(1, model.getCompletedOutfits());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that serving a customer removes that
     * customer and advances the FIFO queue.
     */
    @Test
    public void testQueueAdvancement()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer first = model.getCurrentCustomer();

        submitCorrect(model);

        Customer second = model.getCurrentCustomer();

        assertEquals(false, first == second);
        assertEquals(GameStatus.RUNNING, model.getStatus());
    }


    /**
     * Tests that dialogue is prepared for the
     * newly active customer after a correct submission.
     */
    @Test
    public void testNextCustomerDialogue()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        submitCorrect(model);

        Customer customer = model.getCurrentCustomer();

        FallBackDialogueGenerator fallback =
            new FallBackDialogueGenerator();

        assertEquals(
            fallback.generateDialogue(customer),
            customer.getDialogue());
    }


    /**
     * Tests that a submission at the exact deadline
     * remains valid while gameplay is running.
     */
    @Test
    public void testSubmissionAtExactDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);

        assertEquals(
            SubmissionResult.CORRECT,
            submitCorrect(model));

        assertEquals(GameStatus.RUNNING, model.getStatus());
        assertEquals(1, model.getCompletedOutfits());
    }


    /**
     * Tests that submitting after the deadline
     * returns EXPIRED and ends the attempt.
     */
    @Test
    public void testSubmissionAfterDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(91 * SECOND);

        assertEquals(
            SubmissionResult.EXPIRED,
            submitCorrect(model));

        assertEquals(GameStatus.FAILED, model.getStatus());
        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that the current customer is unavailable
     * before gameplay begins.
     */
    @Test
    public void testCurrentCustomerBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(null, model.getCurrentCustomer());
    }


    /**
     * Tests that an active level exposes its
     * current customer.
     */
    @Test
    public void testCurrentCustomerWhileRunning()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        assertEquals(
            Personality.class,
            model.getCurrentCustomer()
                .getPersonality().getClass());
    }


    /**
     * Tests that the current customer becomes
     * unavailable after level failure.
     */
    @Test
    public void testCurrentCustomerAfterFailure()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(null, model.getCurrentCustomer());
    }


    /**
     * Tests that remaining time is rounded upward
     * when a fractional second has elapsed.
     */
    @Test
    public void testRemainingTimeFractionalSecond()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(500_000_000L);

        assertEquals(90, model.getRemainingTime());
    }


    /**
     * Tests that remaining time decreases after
     * an exact whole second has elapsed.
     */
    @Test
    public void testRemainingTimeWholeSecond()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(SECOND);

        assertEquals(89, model.getRemainingTime());
    }


    /**
     * Tests that remaining time reaches zero
     * at the exact deadline.
     */
    @Test
    public void testRemainingTimeAtDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);

        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that remaining time never becomes
     * negative after the deadline.
     */
    @Test
    public void testRemainingTimeAfterDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(100 * SECOND);

        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that updateTime has no effect before
     * gameplay starts.
     */
    @Test
    public void testUpdateTimeBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.updateTime();

        assertEquals(GameStatus.NOT_STARTED, model.getStatus());
    }


    /**
     * Tests that updateTime preserves RUNNING status
     * before the deadline.
     */
    @Test
    public void testUpdateTimeBeforeDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(89 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.RUNNING, model.getStatus());
    }


    /**
     * Tests that updateTime finishes an incomplete
     * level at the exact deadline.
     */
    @Test
    public void testUpdateTimeAtDeadline()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.FAILED, model.getStatus());
        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that updateTime does not alter an
     * already failed level.
     */
    @Test
    public void testUpdateTimeAfterFailure()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);
        model.updateTime();

        clock.set(120 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.FAILED, model.getStatus());
        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that meeting the minimum outfit count
     * passes the level.
     */
    @Test
    public void testLevelPassed()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.PASSED, model.getStatus());
        assertEquals(2, model.getCompletedOutfits());
    }


    /**
     * Tests that passing a level commits temporary
     * points to the cumulative score.
     */
    @Test
    public void testPassedLevelCommitsScore()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(20, model.getLevelScore());
        assertEquals(20, model.getTotalScore());
    }


    /**
     * Tests that failing a level with insufficient
     * completed outfits discards temporary points.
     */
    @Test
    public void testFailedLevelDiscardsScore()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        submitCorrect(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(GameStatus.FAILED, model.getStatus());
        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that retryLevel is rejected before
     * a game has started.
     */
    @Test
    public void testRetryBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(false, model.retryLevel());
    }


    /**
     * Tests that retryLevel is rejected while
     * the current level is still running.
     */
    @Test
    public void testRetryWhileRunning()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        assertEquals(false, model.retryLevel());
    }


    /**
     * Tests that retryLevel successfully restarts
     * a failed level.
     */
    @Test
    public void testRetryAfterFailure()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(true, model.retryLevel());
        assertEquals(GameStatus.RUNNING, model.getStatus());
        assertEquals(1, model.getCurrentLevel());
        assertEquals(90, model.getRemainingTime());
    }


    /**
     * Tests that retrying a failed level resets
     * temporary score and completed outfit count.
     */
    @Test
    public void testRetryResetsProgress()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        submitCorrect(model);

        clock.set(90 * SECOND);
        model.updateTime();

        model.retryLevel();

        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getCompletedOutfits());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that retryLevel is rejected after
     * successfully passing a level.
     */
    @Test
    public void testRetryAfterPassing()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(false, model.retryLevel());
    }


    /**
     * Tests that nextLevel is rejected before
     * gameplay begins.
     */
    @Test
    public void testNextLevelBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        assertEquals(false, model.nextLevel());
    }


    /**
     * Tests that nextLevel is rejected while
     * the current level is running.
     */
    @Test
    public void testNextLevelWhileRunning()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        assertEquals(false, model.nextLevel());
    }


    /**
     * Tests that nextLevel is rejected after
     * failing a level.
     */
    @Test
    public void testNextLevelAfterFailure()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(false, model.nextLevel());
    }


    /**
     * Tests that nextLevel advances to Level 2
     * after successfully passing Level 1.
     */
    @Test
    public void testNextLevelAfterPassing()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(true, model.nextLevel());
        assertEquals(2, model.getCurrentLevel());
        assertEquals(GameStatus.RUNNING, model.getStatus());
        assertEquals(3, model.getMinimumOutfits());
        assertEquals(115, model.getRemainingTime());
    }


    /**
     * Tests that advancing to a new level resets
     * temporary progress while preserving total score.
     */
    @Test
    public void testNextLevelResetsProgress()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        model.nextLevel();

        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getCompletedOutfits());
        assertEquals(20, model.getTotalScore());
    }


    /**
     * Tests the time-limit calculation for the
     * first difficulty range, Levels 1 through 5.
     */
    @Test
    public void testEarlyLevelTimeLimit()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        advanceToLevel(model, clock, 5);

        assertEquals(190, model.getRemainingTime());
        assertEquals(6, model.getMinimumOutfits());
    }


    /**
     * Tests the time-limit calculation for the
     * middle difficulty range, Levels 6 through 10.
     */
    @Test
    public void testMiddleLevelTimeLimit()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        advanceToLevel(model, clock, 6);

        assertEquals(145, model.getRemainingTime());
        assertEquals(7, model.getMinimumOutfits());
    }


    /**
     * Tests the time-limit calculation for the
     * final difficulty range, Levels 11 through 15.
     */
    @Test
    public void testLateLevelTimeLimit()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        advanceToLevel(model, clock, 11);

        assertEquals(120, model.getRemainingTime());
        assertEquals(12, model.getMinimumOutfits());
    }


    /**
     * Tests that completing Level 15 and advancing
     * ends the game instead of creating Level 16.
     */
    @Test
    public void testFinishAfterLevelFifteen()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        advanceToLevel(model, clock, 15);

        completeRequiredOutfits(model);

        clock.addAndGet(expectedTimeLimit(15) * SECOND);
        model.updateTime();

        assertEquals(GameStatus.PASSED, model.getStatus());
        assertEquals(true, model.nextLevel());

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(15, model.getCurrentLevel());
        assertEquals(1350, model.getTotalScore());
    }


    /**
     * Tests that nextLevel cannot advance a game
     * that has already finished.
     */
    @Test
    public void testNextLevelAfterFinished()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.exitGame();

        assertEquals(false, model.nextLevel());
    }


    /**
     * Tests that exiting an active level changes
     * the game status to FINISHED.
     */
    @Test
    public void testExitWhileRunning()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        model.exitGame();

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(null, model.getCurrentCustomer());
    }


    /**
     * Tests that exiting an active level discards
     * temporary points.
     */
    @Test
    public void testExitDiscardsTemporaryScore()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        submitCorrect(model);

        assertEquals(10, model.getLevelScore());

        model.exitGame();

        assertEquals(0, model.getLevelScore());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that exiting during gameplay preserves
     * the final remaining-time value.
     */
    @Test
    public void testExitPreservesRemainingTime()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        clock.set(30 * SECOND);
        model.exitGame();

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(60, model.getRemainingTime());

        clock.set(100 * SECOND);

        assertEquals(60, model.getRemainingTime());
    }


    /**
     * Tests that exiting before gameplay starts
     * finishes the session.
     */
    @Test
    public void testExitBeforeStarting()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.exitGame();

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(0, model.getTotalScore());
        assertEquals(0, model.getRemainingTime());
    }


    /**
     * Tests that exiting an already finished game
     * preserves its finished state.
     */
    @Test
    public void testExitAfterFinished()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.exitGame();
        model.exitGame();

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(0, model.getTotalScore());
    }


    /**
     * Tests that a finished game cannot restart
     * through startLevel.
     */
    @Test
    public void testStartAfterFinished()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.exitGame();

        assertEquals(false, model.startLevel(1));
        assertEquals(GameStatus.FINISHED, model.getStatus());
    }


    /**
     * Tests that a finished game rejects further
     * outfit submissions.
     */
    @Test
    public void testSubmitAfterFinished()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer customer = model.getCurrentCustomer();

        model.exitGame();

        assertEquals(
            SubmissionResult.EXPIRED,
            model.submitOutfit(customer.getCorrectOutfit()));

        assertEquals(GameStatus.FINISHED, model.getStatus());
    }


    /**
     * Tests that time spent preparing dialogue is
     * excluded from active gameplay time.
     */
    @Test
    public void testDialoguePreparationPausesClock()
    {
        AtomicLong clock = new AtomicLong(0);

        CustomerGenerator generator = new CustomerGenerator(
            new ClothingCatalog(new Random(42)),
            new FallBackDialogueGenerator(),
            new Random(42))
        {
            /**
             * Simulates five seconds of dialogue preparation
             * before assigning the customer's dialogue.
             *
             * @param customer
             *            the customer receiving dialogue
             */
            @Override
            public void prepareDialogue(Customer customer)
            {
                clock.addAndGet(5 * SECOND);
                super.prepareDialogue(customer);
            }
        };

        GameModel model = new GameModel(generator, clock::get);

        model.startLevel(1);

        assertEquals(90, model.getRemainingTime());

        assertEquals(
            SubmissionResult.CORRECT,
            submitCorrect(model));

        assertEquals(90, model.getRemainingTime());
        assertEquals(1, model.getCompletedOutfits());
    }


    /**
     * Tests that retrying a failed level creates
     * a fresh active customer.
     */
    @Test
    public void testRetryCreatesFreshCustomers()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);

        Customer original = model.getCurrentCustomer();

        clock.set(90 * SECOND);
        model.updateTime();

        assertEquals(true, model.retryLevel());

        Customer replacement = model.getCurrentCustomer();

        assertEquals(false, original == replacement);
        assertEquals(GameStatus.RUNNING, model.getStatus());
    }


    /**
     * Tests that exiting after passing a level
     * preserves the cumulative score.
     */
    @Test
    public void testExitAfterPassing()
    {
        AtomicLong clock = new AtomicLong(0);
        GameModel model = createModel(clock);

        model.startLevel(1);
        completeRequiredOutfits(model);

        clock.set(90 * SECOND);
        model.updateTime();

        model.exitGame();

        assertEquals(GameStatus.FINISHED, model.getStatus());
        assertEquals(20, model.getTotalScore());
    }
}