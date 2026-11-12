package purrfectfit.controller;

import purrfectfit.model.ClothingItem;
import purrfectfit.model.Customer;
import purrfectfit.model.GameModel;
import purrfectfit.view.MainWindow;

// -------------------------------------------------------------------------
/**
 * Handles sending data back out to the view
 *
 * @author Ari Bajaj
 * @version Nov 11, 2026
 */
public class OutputHandler
{
    private final GameModel model;
    private final MainWindow window;

    // ----------------------------------------------------------
    /**
     * Create a new OutputHandler object.
     *
     * @param model
     *            - GameModel
     * @param window
     *            - MainWindow
     */
    public OutputHandler(GameModel model, MainWindow window)
    {
        this.model = model;
        this.window = window;
    }


    // ----------------------------------------------------------
    /**
     * Shows the start screen
     */
    public void showStart()
    {
        window.showScreen("start");
    }


    // ----------------------------------------------------------
    /**
     * Shows the game screen
     */
    public void showGame()
    {
        window.showScreen("game");
        refreshGame();
    }


    // ----------------------------------------------------------
    /**
     * Refreshes the game screen with updated data. The view keeps showing
     * the player's chosen items until the customer changes, so the
     * selection is not sent here; see select().
     */
    public void refreshGame()
    {
        Customer customer = model.getCurrentCustomer(); // null when queue is empty
        window.setCustomer(customer);
        window.setDialogue(customer == null ? "" : customer.getDialogue());
        window.setTimeRemaining(model.getRemainingTime());
        window.setLevel(model.getCurrentLevel());
        window.setScore(model.getLevelScore());
        window.setProgress(model.getCompletedOutfits(), model.getMinimumOutfits());
        window.repaint();
    }


    // ----------------------------------------------------------
    /**
     * Shows the results screen
     *
     * @param passed
     *            - whether the level was passed
     * @param score
     *            - score to display
     * @param level
     *            - the level that just ended
     */
    public void showResults(boolean passed, int score, int level)
    {
        window.showResults(passed, score, level);
    }


    // ----------------------------------------------------------
    /**
     * Shows the end-of-game screen after the last level
     *
     * @param totalScore
     *            - the final cumulative score
     */
    public void showGameComplete(int totalScore)
    {
        window.showGameComplete(totalScore);
    }


    // ----------------------------------------------------------
    /**
     * Shows or hides the loading state
     *
     * @param loading
     *            - true while a level is being generated
     */
    public void showLoading(boolean loading)
    {
        window.setLoading(loading);
    }


    // ----------------------------------------------------------
    /**
     * Changes the outfit selection
     *
     * @param item
     *            - item that the user has added/changed
     */
    public void select(ClothingItem item)
    {
        window.setSelectedItem(item.getCategory(), item);
    }


    // ----------------------------------------------------------
    /**
     * Shows a feedback message
     *
     * @param text
     *            - the message
     */
    public void feedback(String text)
    {
        window.showFeedback(text);
    }
}