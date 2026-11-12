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
 * Displays the title and start button(s) :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class StartScreen extends JPanel
{
    /** Pressed to begin the game. */
    public final JButton startButton = new JButton("Start");
 
    private final JLabel titleLabel = new JLabel("Purrfect Fit");
 
 
    // ----------------------------------------------------------
    /**
     * Create a new StartScreen.
     */
    public StartScreen()
    {
        setLayout(new GridBagLayout());
        setBackground(Assets.PINK_LIGHT);
 
        titleLabel.setFont(Fonts.TITLE);
        titleLabel.setForeground(Assets.TEXT);
 
        JLabel subtitle = new JLabel("Dress each customer in the perfect outfit!");
        subtitle.setFont(Fonts.BODY);
        subtitle.setForeground(Assets.TEXT);
 
        startButton.setFont(Fonts.HUD);
        startButton.setBackground(Assets.PINK_DARK);
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
 
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.insets = new Insets(8, 8, 8, 8);
        c.gridy = 0;
        add(titleLabel, c);
        c.gridy = 1;
        add(subtitle, c);
        c.gridy = 2;
        c.ipadx = 60;
        c.ipady = 12;
        c.insets = new Insets(30, 8, 8, 8);
        add(startButton, c);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows or hides a "loading" state while the level is being generated.
     *
     * @param loading
     *            true while loading
     */
    public void setLoading(boolean loading)
    {
        startButton.setEnabled(!loading);
        startButton.setText(loading ? "Loading..." : "Start");
    }
}