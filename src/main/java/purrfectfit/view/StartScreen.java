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
 * Displays the title and start button(s) :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class StartScreen
    extends JPanel
{
    /** Music credit line. Add the artist here if the license asks for it. */
    public static final String CREDIT_TEXT = "Music: Cream Soda";

    /** Pressed to begin the game. */
    public final JButton startButton = new ImageButton("Start", Layout.START_START_BUTTON,
        Layout.START_START_BUTTON_FONT_SIZE, Layout.START_START_BUTTON_COLOR);


    // ----------------------------------------------------------
    /**
     * Create a new StartScreen.
     *
     * @param music
     *            the shared music player (for the mute button)
     */
    public StartScreen(Music music)
    {
        setLayout(null);
        setPreferredSize(new Dimension(Layout.WIDTH, Layout.HEIGHT));
        add(new PixelLabel("Purrfect Fit", Layout.START_TITLE, Fonts.bold(Layout.START_TITLE_FONT_SIZE),
            Layout.START_TITLE_COLOR, Layout.START_TITLE_ALIGN));
        add(new PixelLabel("Dress each customer in the perfect outfit!", Layout.START_SUBTITLE,
            Layout.START_SUBTITLE_FONT_SIZE, Layout.START_SUBTITLE_COLOR,
            Layout.START_SUBTITLE_ALIGN));
        add(new PixelLabel(CREDIT_TEXT, Layout.START_CREDIT, Layout.START_CREDIT_FONT_SIZE,
            Layout.START_CREDIT_COLOR, Layout.START_CREDIT_ALIGN));
        add(startButton);
        add(new MuteButton(music, Layout.START_MUTE_BUTTON));
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