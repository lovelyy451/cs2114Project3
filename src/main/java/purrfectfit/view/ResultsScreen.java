package purrfectfit.view;

// Imports :)

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Displays the pass or fail message, the score,
 * and the next or retry button :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class ResultsScreen
    extends JPanel
{
    /** Go to the next level (shown only after a pass). */
    public final JButton nextButton = new ImageButton("Next Level", Layout.RESULTS_NEXT_BUTTON,
        Layout.RESULTS_NEXT_BUTTON_FONT_SIZE, Layout.RESULTS_NEXT_BUTTON_COLOR);
    /** Try the level again (shown only after a fail). */
    public final JButton retryButton = new ImageButton("Retry", Layout.RESULTS_RETRY_BUTTON,
        Layout.RESULTS_RETRY_BUTTON_FONT_SIZE, Layout.RESULTS_RETRY_BUTTON_COLOR);

    private final PixelLabel outcomeLabel = new PixelLabel("", Layout.RESULTS_OUTCOME,
        Fonts.bold(Layout.RESULTS_OUTCOME_FONT_SIZE), Layout.RESULTS_OUTCOME_COLOR,
        Layout.RESULTS_OUTCOME_ALIGN);
    private final PixelLabel levelLabel = new PixelLabel("", Layout.RESULTS_LEVEL,
        Layout.RESULTS_LEVEL_FONT_SIZE, Layout.RESULTS_LEVEL_COLOR, Layout.RESULTS_LEVEL_ALIGN);
    private final PixelLabel scoreLabel = new PixelLabel("", Layout.RESULTS_SCORE,
        Layout.RESULTS_SCORE_FONT_SIZE, Layout.RESULTS_SCORE_COLOR, Layout.RESULTS_SCORE_ALIGN);


    // ----------------------------------------------------------
    /**
     * Create a new ResultsScreen.
     *
     * @param music
     *            the shared music player (for the mute button)
     */
    public ResultsScreen(Music music)
    {
        setLayout(null);
        setPreferredSize(new Dimension(Layout.WIDTH, Layout.HEIGHT));
        add(outcomeLabel);
        add(levelLabel);
        add(scoreLabel);
        add(nextButton);
        add(retryButton);
        add(new MuteButton(music, Layout.RESULTS_MUTE_BUTTON));
    }


    // ----------------------------------------------------------
    /**
     * Fills in the results and shows the right button.
     *
     * @param passed
     *            true if the level was passed
     * @param score
     *            the score to display
     * @param level
     *            the level that just ended
     */
    public void setResults(boolean passed, int score, int level)
    {
        outcomeLabel.setText(passed ? "Level Passed!" : "Level Failed");
        levelLabel.setText("Level " + level);
        scoreLabel.setText("Score: " + score);
        nextButton.setVisible(passed);
        retryButton.setVisible(!passed);
    }


    // ----------------------------------------------------------
    /**
     * Shows the end-of-game message after the last level (no buttons).
     *
     * @param totalScore
     *            the final cumulative score
     */
    public void setGameComplete(int totalScore)
    {
        outcomeLabel.setText("Game Complete!");
        levelLabel.setText("Thanks for playing Purrfect Fit");
        scoreLabel.setText("Total score: " + totalScore);
        nextButton.setVisible(false);
        retryButton.setVisible(false);
    }


    // ----------------------------------------------------------
    /**
     * Disables the buttons while the next level is being generated.
     *
     * @param loading
     *            true while loading
     */
    public void setLoading(boolean loading)
    {
        nextButton.setEnabled(!loading);
        retryButton.setEnabled(!loading);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Image bg = Assets.background();
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
    }
}