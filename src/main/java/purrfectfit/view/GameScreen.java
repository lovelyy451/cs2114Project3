package purrfectfit.view;

// Imports :)

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import purrfectfit.model.ClothingCategory;
import purrfectfit.model.ClothingItem;
import purrfectfit.model.Customer;

/**
 * The main gameplay screen that has the
 * customer drawing, the three option rows,
 * the dialogue, the HUD and the swing timer :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class GameScreen extends JPanel
{
    /** Submit the outfit. The controller attaches a listener. */
    public final JButton submitButton = new JButton("Submit");
 
    private final ScenePanel scenePanel = new ScenePanel();
    private final Map<ClothingCategory, CategoryPanel> categoryPanels =
        new EnumMap<>(ClothingCategory.class);
    private final JTextArea dialogueArea = new JTextArea();
    private final JLabel feedbackLabel = new JLabel(" ");
    private final JLabel levelLabel = new JLabel("Level 1", SwingConstants.CENTER);
    private final JLabel timeLabel = new JLabel("Time 0:00", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Score 0", SwingConstants.CENTER);
    private final JLabel progressLabel = new JLabel("Outfits 0/0", SwingConstants.CENTER);
 
    /** Only used to skip redrawing when the same customer is set again. */
    private Customer shownCustomer;
 
 
    // ----------------------------------------------------------
    /**
     * Create a new GameScreen.
     */
    public GameScreen()
    {
        setLayout(new BorderLayout());
        setBackground(Assets.PINK_LIGHT);
 
        add(buildHud(), BorderLayout.NORTH);
 
        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(scenePanel, BorderLayout.CENTER);
        left.add(buildBottomBar(), BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);
        add(buildRightColumn(), BorderLayout.CENTER);
    }
 
 
    private JPanel buildHud()
    {
        JPanel hud = new JPanel(new GridLayout(1, 4));
        hud.setBackground(Assets.PINK_DARK);
        hud.setPreferredSize(new Dimension(0, 36));
        for (JLabel l : new JLabel[] { levelLabel, timeLabel, scoreLabel, progressLabel })
        {
            l.setFont(Fonts.HUD);
            l.setForeground(Color.WHITE);
            hud.add(l);
        }
        return hud;
    }
 
 
    private JPanel buildBottomBar()
    {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        bar.setPreferredSize(new Dimension(0, 112));
 
        feedbackLabel.setFont(Fonts.HUD);
        feedbackLabel.setForeground(Assets.TEXT);
        bar.add(feedbackLabel, BorderLayout.CENTER);
 
        submitButton.setFont(Fonts.HUD);
        submitButton.setBackground(Assets.PINK_DARK);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setPreferredSize(new Dimension(160, 60));
        bar.add(submitButton, BorderLayout.EAST);
        return bar;
    }
 
 
    private JPanel buildRightColumn()
    {
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
 
        dialogueArea.setEditable(false);
        dialogueArea.setFocusable(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setFont(Fonts.BODY);
        dialogueArea.setForeground(Assets.TEXT);
        dialogueArea.setBackground(Color.WHITE);
        dialogueArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Assets.PINK_DARK, 2),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        dialogueArea.setPreferredSize(new Dimension(300, 100));
        dialogueArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        right.add(dialogueArea);
        right.add(javax.swing.Box.createVerticalStrut(8));
 
        // One row per category, in enum order (hat, top, shoes).
        for (ClothingCategory category : ClothingCategory.values())
        {
            CategoryPanel panel = new CategoryPanel(category);
            categoryPanels.put(category, panel);
            right.add(panel);
        }
        return right;
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows a customer: the cat and their three choices per category. Does
     * nothing if this customer is already shown (the controller calls this
     * on every refresh).
     *
     * @param customer
     *            the front customer, or null if there is none
     */
    public void setCustomer(Customer customer)
    {
        if (customer == shownCustomer)
        {
            return;
        }
        shownCustomer = customer;
        scenePanel.clearOutfit();
        for (CategoryPanel panel : categoryPanels.values())
        {
            panel.setOptions(null);
        }
        if (customer == null)
        {
            scenePanel.setBase(null);
            return;
        }
        scenePanel.setBase(Assets.catBase(customer.getPersonality()));
        ClothingItem[][] options = customer.getAvailableOptions();
        for (ClothingItem[] row : options)
        {
            if (row != null && row.length > 0 && row[0] != null)
            {
                CategoryPanel panel = categoryPanels.get(row[0].getCategory());
                if (panel != null)
                {
                    panel.setOptions(row);
                }
            }
        }
    }
 
 
    // ----------------------------------------------------------
    /**
     * Sets the customer's dialogue text.
     *
     * @param text
     *            what the customer says (null shows nothing)
     */
    public void setDialogue(String text)
    {
        String shown = text == null ? "" : text;
        if (!shown.equals(dialogueArea.getText()))
        {
            dialogueArea.setText(shown);
        }
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows one chosen item on the cat and outlines its button. Also clears
     * the old feedback message because the player is working on a new try.
     *
     * @param category
     *            the item's category
     * @param item
     *            the item (null empties that slot)
     */
    public void setSelectedItem(ClothingCategory category, ClothingItem item)
    {
        applySelection(category, item);
        feedbackLabel.setText(" ");
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the whole current selection (called on every refresh, so it
     * does not touch the feedback message).
     *
     * @param hat
     *            chosen hat or null
     * @param top
     *            chosen top or null
     * @param shoes
     *            chosen shoes or null
     */
    public void setSelection(ClothingItem hat, ClothingItem top, ClothingItem shoes)
    {
        ClothingCategory[] cats = ClothingCategory.values(); // hat, top, shoes
        applySelection(cats[0], hat);
        applySelection(cats[1], top);
        applySelection(cats[2], shoes);
    }
 
 
    private void applySelection(ClothingCategory category, ClothingItem item)
    {
        CategoryPanel panel = categoryPanels.get(category);
        if (panel != null)
        {
            panel.setHighlightedItem(item);
        }
        scenePanel.setLayer(category, item == null ? null : Assets.itemLayer(item));
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the remaining time.
     *
     * @param seconds
     *            seconds left
     */
    public void setTimeRemaining(int seconds)
    {
        int s = Math.max(0, seconds);
        timeLabel.setText(String.format("Time %d:%02d", s / 60, s % 60));
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the level number.
     *
     * @param level
     *            the level
     */
    public void setLevel(int level)
    {
        levelLabel.setText("Level " + level);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows the level score.
     *
     * @param score
     *            the score
     */
    public void setScore(int score)
    {
        scoreLabel.setText("Score " + score);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows how many outfits are done out of how many are needed.
     *
     * @param completed
     *            outfits completed
     * @param minimum
     *            outfits needed to pass
     */
    public void setProgress(int completed, int minimum)
    {
        progressLabel.setText("Outfits " + completed + "/" + minimum);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows a message under the scene (correct / incorrect / invalid...).
     *
     * @param text
     *            the message
     */
    public void showFeedback(String text)
    {
        feedbackLabel.setText(text == null || text.isEmpty() ? " " : text);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Clears the feedback message.
     */
    public void clearFeedback()
    {
        feedbackLabel.setText(" ");
    }
 
 
    // ----------------------------------------------------------
    /**
     * Lets the controller hear about option clicks. Replaces looping over a
     * Map of buttons, since the buttons change per customer.
     *
     * @param listener
     *            receives the clicked item
     */
    public void setOptionListener(Consumer<ClothingItem> listener)
    {
        for (CategoryPanel panel : categoryPanels.values())
        {
            panel.setOptionListener(listener);
        }
    }
}