package purrfectfit.view;

// Imports :)

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Displays the pass or fail message, the score,
 * and the next or retry button :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class ResultsScreen extends JPanel
{
    /** Go to the next level (shown only after a pass). */
    public final JButton nextButton = new JButton("Next Level");
    /** Try the level again (shown only after a fail). */
    public final JButton retryButton = new JButton("Retry");
 
    private final JLabel outcomeLabel = new JLabel("", JLabel.CENTER);
    private final JLabel levelLabel = new JLabel("", JLabel.CENTER);
    private final JLabel scoreLabel = new JLabel("", JLabel.CENTER);
 
 
    // ----------------------------------------------------------
    /**
     * Create a new ResultsScreen.
     */
    public ResultsScreen()
    {
        setLayout(new GridBagLayout());
        setBackground(Assets.PINK_LIGHT);
 
        outcomeLabel.setFont(Fonts.TITLE);
        outcomeLabel.setForeground(Assets.TEXT);
        levelLabel.setFont(Fonts.HUD);
        levelLabel.setForeground(Assets.TEXT);
        scoreLabel.setFont(Fonts.HUD);
        scoreLabel.setForeground(Assets.TEXT);
 
        for (JButton b : new JButton[] { nextButton, retryButton })
        {
            b.setFont(Fonts.HUD);
            b.setBackground(Assets.PINK_DARK);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
        }
 
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(8, 8, 8, 8);
        c.gridy = 0;
        add(outcomeLabel, c);
        c.gridy = 1;
        add(levelLabel, c);
        c.gridy = 2;
        add(scoreLabel, c);
        c.gridy = 3;
        c.ipadx = 40;
        c.ipady = 10;
        c.insets = new Insets(24, 8, 8, 8);
        add(nextButton, c);
        add(retryButton, c); // same cell: only one is visible at a time
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
}