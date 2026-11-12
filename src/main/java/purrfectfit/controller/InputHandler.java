package purrfectfit.controller;

import java.util.Arrays;
import java.util.function.BooleanSupplier;
import javax.swing.Timer;
import purrfectfit.model.ClothingItem;
import purrfectfit.model.GameModel;
import purrfectfit.model.GameStatus;
import purrfectfit.model.Outfit;
import purrfectfit.model.SubmissionResult;
import purrfectfit.view.MainWindow;

// -------------------------------------------------------------------------
/**
 * Handles the user's inputs (button clicks) and owns the game timer.
 *
 * @author Ari Bajaj
 * @version Nov 11, 2026
 */
public class InputHandler
{
    private final GameModel model;
    private final OutputHandler output;
    private final MainWindow window;
    private final Timer gameTimer;

    /**
     * The items the player has picked so far, one slot per category in enum
     * order (hat, top, shoes). null means nothing picked yet.
     */
    private final ClothingItem[] selection = new ClothingItem[3];

    // ----------------------------------------------------------
    /**
     * Create a new InputHandler object.
     *
     * @param model
     *            - GameModel
     * @param output
     *            - OutputHandler
     * @param window
     *            - MainWindow
     */
    public InputHandler(GameModel model, OutputHandler output, MainWindow window)
    {
        this.model = model;
        this.output = output;
        this.window = window;
        this.gameTimer = new Timer(1000, e -> onTick());
        attachListeners();
    }


    private void attachListeners()
    {
        window.startScreen.startButton.addActionListener(e -> onStart());
        window.gameScreen.submitButton.addActionListener(e -> onSubmit());
        window.resultsScreen.nextButton.addActionListener(e -> onNext());
        window.resultsScreen.retryButton.addActionListener(e -> onRetry());
        window.gameScreen.setOptionListener(item -> onOptionChosen(item));
    }


    private void onStart()
    {
        loadLevel(() -> model.startLevel(1));
    }


    private void onNext()
    {
        loadLevel(model::nextLevel);
    }


    private void onRetry()
    {
        loadLevel(model::retryLevel);
    }


    /**
     * Runs a model call that starts a level (startLevel, nextLevel or
     * retryLevel) in the background, then shows the game.
     */
    private void loadLevel(BooleanSupplier startAction)
    {
        clearSelection();
        output.showLoading(true);
        new LevelLoader(startAction, () -> onLevelReady(), () -> {
            output.showLoading(false); // model refused; stay on this screen
        }).execute();
    }


    private void onLevelReady()
    {
        output.showLoading(false);
        if (model.getStatus() == GameStatus.RUNNING)
        {
            output.showGame();
            gameTimer.start();
        }
        else
        {
            // nextLevel() after the last level ends the game
            output.showGameComplete(model.getTotalScore());
        }
    }


    private void onOptionChosen(ClothingItem item)
    {
        selection[item.getCategory().ordinal()] = item;
        output.select(item);
        output.refreshGame();
    }


    private void onSubmit()
    {
        // Outfit refuses null parts, so only build one when all 3 are picked.
        // submitOutfit(null) makes the model answer INVALID.
        Outfit outfit = null;
        if (selection[0] != null && selection[1] != null && selection[2] != null)
        {
            outfit = new Outfit(selection[0], selection[1], selection[2]);
        }

        SubmissionResult submission = model.submitOutfit(outfit);
        switch (submission)
        {
            case CORRECT:
                output.feedback("Your outfit is correct!");
                clearSelection(); // next customer starts with nothing picked
                break;
            case INCORRECT:
                output.feedback("Your outfit is incorrect!");
                break;
            case INVALID:
                output.feedback(
                    "A hat, top, and shoes are all required for a valid outfit.");
                break;
            case EXPIRED:
                output.feedback("You ran out of time!");
                break;
            default:
                break;
        }

        if (model.getStatus() != GameStatus.RUNNING)
        {
            endLevel(); // the model finished the level (time ran out)
        }
        else
        {
            output.refreshGame();
        }
    }


    private void onTick()
    {
        model.updateTime(); // finishes the level when time is up
        if (model.getStatus() != GameStatus.RUNNING)
        {
            endLevel();
        }
        else
        {
            output.refreshGame();
        }
    }


    private void endLevel()
    {
        gameTimer.stop();
        output.showResults(
            model.getStatus() == GameStatus.PASSED,
            model.getLevelScore(),
            model.getCurrentLevel());
    }


    private void clearSelection()
    {
        Arrays.fill(selection, null);
    }
}