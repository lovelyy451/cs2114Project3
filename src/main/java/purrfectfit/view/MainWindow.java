package purrfectfit.view;

// Imports :)

import purrfectfit.model.ClothingCategory;
import purrfectfit.model.ClothingItem;
import purrfectfit.model.Customer;
 
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * The window, and it holds the screens and switches
 * between them :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class MainWindow extends JFrame
{
    /** Card name of the start screen. */
    public static final String START = "start";
    /** Card name of the game screen. */
    public static final String GAME = "game";
    /** Card name of the results screen. */
    public static final String RESULTS = "results";
    /** Fixed window content size. */
    public static final int WINDOW_WIDTH = 960;
    /** Fixed window content size. */
    public static final int WINDOW_HEIGHT = 540;
 
    static
    {
        // Metal looks the same on every OS, so colors/borders always show.
        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e)
        {
            // keep the default look
        }
    }
 
    /** Screens are public so the controller can reach their buttons. */
    public final StartScreen startScreen = new StartScreen();
    /** The gameplay screen. */
    public final GameScreen gameScreen = new GameScreen();
    /** The results screen. */
    public final ResultsScreen resultsScreen = new ResultsScreen();
 
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JPanel topBar = new JPanel(new BorderLayout());
    private final JButton muteButton = new JButton("Music: On");
    private final Music music = new Music();
 
 
    // ----------------------------------------------------------
    /**
     * Builds the window and shows it.
     */
    public MainWindow()
    {
        super("Purrfect Fit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
 
        JLabel title = new JLabel("  Purrfect Fit");
        title.setFont(Fonts.HUD);
        title.setForeground(Color.WHITE);
 
        muteButton.setFont(Fonts.BODY.deriveFont(14f));
        muteButton.setFocusable(false);
        muteButton.addActionListener(e -> {
            music.setMuted(!music.isMuted());
            muteButton.setText(music.isMuted() ? "Music: Off" : "Music: On");
        });
 
        topBar.setBackground(Assets.TEXT);
        topBar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 6));
        topBar.setPreferredSize(new Dimension(0, 32));
        topBar.add(title, BorderLayout.WEST);
        topBar.add(muteButton, BorderLayout.EAST);
 
        cards.add(startScreen, START); // first card added is shown first
        cards.add(gameScreen, GAME);
        cards.add(resultsScreen, RESULTS);
 
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topBar, BorderLayout.NORTH);
        getContentPane().add(cards, BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        pack();
        setLocationRelativeTo(null);
 
        music.play();
        setVisible(true);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Switches screens.
     *
     * @param name
     *            "start", "game" or "results"
     */
    public void showScreen(String name)
    {
        if (GAME.equals(name))
        {
            gameScreen.clearFeedback();
        }
        cardLayout.show(cards, name);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the results screen.
     *
     * @param passed
     *            whether the level was passed
     * @param score
     *            score to show
     * @param level
     *            level that just ended
     */
    public void showResults(boolean passed, int score, int level)
    {
        resultsScreen.setResults(passed, score, level);
        showScreen(RESULTS);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the end-of-game screen after the last level.
     *
     * @param totalScore
     *            the final cumulative score
     */
    public void showGameComplete(int totalScore)
    {
        resultsScreen.setGameComplete(totalScore);
        showScreen(RESULTS);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows or hides the loading state while a level is generated.
     *
     * @param loading
     *            true while loading
     */
    public void setLoading(boolean loading)
    {
        startScreen.setLoading(loading);
        resultsScreen.setLoading(loading);
    }
 
 
    // The rest simply pass through to the game screen.
 
    // ----------------------------------------------------------
    /**
     * Shows the front customer.
     *
     * @param customer
     *            customer or null
     */
    public void setCustomer(Customer customer)
    {
        gameScreen.setCustomer(customer);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the customer's dialogue.
     *
     * @param text
     *            dialogue
     */
    public void setDialogue(String text)
    {
        gameScreen.setDialogue(text);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows one chosen item (and clears old feedback).
     *
     * @param category
     *            item category
     * @param item
     *            the item
     */
    public void setSelectedItem(ClothingCategory category, ClothingItem item)
    {
        gameScreen.setSelectedItem(category, item);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the whole current selection.
     *
     * @param hat
     *            hat or null
     * @param top
     *            top or null
     * @param shoes
     *            shoes or null
     */
    public void setSelection(ClothingItem hat, ClothingItem top, ClothingItem shoes)
    {
        gameScreen.setSelection(hat, top, shoes);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the time left.
     *
     * @param seconds
     *            seconds remaining
     */
    public void setTimeRemaining(int seconds)
    {
        gameScreen.setTimeRemaining(seconds);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the level.
     *
     * @param level
     *            level number
     */
    public void setLevel(int level)
    {
        gameScreen.setLevel(level);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the score.
     *
     * @param score
     *            score
     */
    public void setScore(int score)
    {
        gameScreen.setScore(score);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows outfit progress.
     *
     * @param completed
     *            completed outfits
     * @param minimum
     *            needed to pass
     */
    public void setProgress(int completed, int minimum)
    {
        gameScreen.setProgress(completed, minimum);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows a feedback message.
     *
     * @param text
     *            message
     */
    public void showFeedback(String text)
    {
        gameScreen.showFeedback(text);
    }
}