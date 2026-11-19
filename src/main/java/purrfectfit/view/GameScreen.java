package purrfectfit.view;

// Imports :)

import purrfectfit.model.ClothingCategory;
import purrfectfit.model.ClothingItem;
import purrfectfit.model.Customer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The main gameplay screen that has the
 * customer drawing, the three option rows,
 * the dialogue, the HUD and the swing timer :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class GameScreen
    extends JPanel
{
    private static final int ROWS = 3;
    private static final int COLS = 3;

    /** Submit the outfit. The controller attaches a listener. */
    public final JButton submitButton = new ImageButton("Submit", Layout.GAME_SUBMIT_BUTTON,
        Layout.GAME_SUBMIT_BUTTON_FONT_SIZE, Layout.GAME_SUBMIT_BUTTON_COLOR);

    /** Asks for a hint. The controller attaches a listener. */
    public final JButton hintButton = new ImageButton("Hint", Layout.GAME_HINT_BUTTON,
        Layout.GAME_HINT_BUTTON_FONT_SIZE, Layout.GAME_HINT_BUTTON_COLOR);
    /** Leaves the game. The controller attaches a listener. */
    public final JButton exitButton = new ImageButton("Exit", Layout.GAME_EXIT_BUTTON,
        Layout.GAME_EXIT_BUTTON_FONT_SIZE, Layout.GAME_EXIT_BUTTON_COLOR);

    private final PixelTextArea dialogueArea = new PixelTextArea(Layout.GAME_BUBBLE_TEXT,
        Layout.GAME_BUBBLE_TEXT_FONT_SIZE, Layout.GAME_BUBBLE_TEXT_COLOR);
    private final PixelLabel feedbackLabel = new PixelLabel(" ", Layout.GAME_FEEDBACK,
        Layout.GAME_FEEDBACK_FONT_SIZE, Layout.GAME_FEEDBACK_COLOR, Layout.GAME_FEEDBACK_ALIGN);
    private final PixelLabel hudLabel = new PixelLabel("", Layout.GAME_HUD,
        Layout.GAME_HUD_FONT_SIZE, Layout.GAME_HUD_COLOR, Layout.GAME_HUD_ALIGN);
    private final JButton[][] slotButtons = new JButton[ROWS][COLS];

    // what to show (set by the controller through the setters below)
    private Image baseImage;
    private final Image[] layers = new Image[ROWS]; // hat, top, shoes
    private final ClothingItem[][] options = new ClothingItem[ROWS][COLS];
    private final ClothingItem[] chosen = new ClothingItem[ROWS];
    private Consumer<ClothingItem> optionListener;
    private Customer shownCustomer;

    private int level = 1;
    private int seconds;
    private int score;
    private int completed;
    private int minimum;


    // ----------------------------------------------------------
    /**
     * Create a new GameScreen.
     *
     * @param music
     *            the shared music player (for the mute button)
     */
    public GameScreen(Music music)
    {
        setLayout(null);
        setPreferredSize(new Dimension(Layout.WIDTH, Layout.HEIGHT));

        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                final int row = r;
                final int col = c;
                JButton b = new JButton();
                b.setBounds(Layout.slotRect(r, c));
                b.setContentAreaFilled(false);
                b.setBorderPainted(false);
                b.setFocusPainted(false);
                b.setOpaque(false);
                b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                b.setEnabled(false);
                b.addActionListener(e -> {
                    ClothingItem item = options[row][col];
                    if (optionListener != null && item != null)
                    {
                        optionListener.accept(item);
                    }
                });
                slotButtons[r][c] = b;
                add(b);
            }
        }

        add(dialogueArea);
        add(hudLabel);
        add(feedbackLabel);
        add(submitButton);
        add(hintButton);
        add(exitButton);
        add(new MuteButton(music, Layout.GAME_MUTE_BUTTON));
        updateHud();
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
        for (int r = 0; r < ROWS; r++)
        {
            layers[r] = null;
            chosen[r] = null;
            for (int c = 0; c < COLS; c++)
            {
                options[r][c] = null;
            }
        }
        if (customer != null)
        {
            baseImage = Assets.catBase(customer.getPersonality());
            ClothingItem[][] given = customer.getAvailableOptions();
            for (ClothingItem[] row : given)
            {
                if (row != null && row.length > 0 && row[0] != null)
                {
                    int r = row[0].getCategory().ordinal(); // hat, top, shoes
                    for (int c = 0; c < COLS && c < row.length; c++)
                    {
                        options[r][c] = row[c];
                    }
                }
            }
        }
        else
        {
            baseImage = null;
        }
        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                slotButtons[r][c].setEnabled(options[r][c] != null);
            }
        }
        repaint();
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
     * Shows one chosen item on the cat and outlines its slot. Also clears
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
        clearFeedback();
        repaint();
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
        repaint();
    }


    private void applySelection(ClothingCategory category, ClothingItem item)
    {
        int r = category.ordinal();
        chosen[r] = item;
        layers[r] = item == null ? null : Assets.itemLayer(item);
    }


    // ----------------------------------------------------------
    /**
     * Shows the remaining time.
     *
     * @param secondsLeft
     *            seconds left
     */
    public void setTimeRemaining(int secondsLeft)
    {
        this.seconds = Math.max(0, secondsLeft);
        updateHud();
    }


    // ----------------------------------------------------------
    /**
     * Shows the level number.
     *
     * @param levelNumber
     *            the level
     */
    public void setLevel(int levelNumber)
    {
        this.level = levelNumber;
        updateHud();
    }


    // ----------------------------------------------------------
    /**
     * Shows the level score.
     *
     * @param levelScore
     *            the score
     */
    public void setScore(int levelScore)
    {
        this.score = levelScore;
        updateHud();
    }


    // ----------------------------------------------------------
    /**
     * Shows how many outfits are done out of how many are needed.
     *
     * @param done
     *            outfits completed
     * @param needed
     *            outfits needed to pass
     */
    public void setProgress(int done, int needed)
    {
        this.completed = done;
        this.minimum = needed;
        updateHud();
    }


    private void updateHud()
    {
        hudLabel.setText(String.format("Level %d      Time %d:%02d      Score %d      Outfits %d/%d",
            level, seconds / 60, seconds % 60, score, completed, minimum));
    }


    // ----------------------------------------------------------
    /**
     * Shows a message at the bottom (correct / incorrect / invalid...).
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
     * Shows a hint (in the same spot as the feedback message).
     *
     * @param hint
     *            the hint text
     */
    public void showHint(String hint)
    {
        showFeedback(hint);
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
     * Lets the controller hear about slot clicks.
     *
     * @param listener
     *            receives the clicked item
     */
    public void setOptionListener(Consumer<ClothingItem> listener)
    {
        this.optionListener = listener;
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // back to front
        draw(g2, Assets.background());
        draw(g2, Assets.platform());
        draw(g2, baseImage);
        draw(g2, layers[ClothingCategory.SHOES.ordinal()]);
        draw(g2, layers[ClothingCategory.TOP.ordinal()]);
        draw(g2, layers[ClothingCategory.HAT.ordinal()]);
        draw(g2, Assets.counter());
        draw(g2, Assets.speechBubble());
        draw(g2, Assets.slotsPanel());
        paintSlots(g2);
        draw(g2, Assets.hudBand());
        g2.dispose();
    }


    private void draw(Graphics2D g2, Image image)
    {
        if (image != null)
        {
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }


    private void paintSlots(Graphics2D g2)
    {
        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLS; c++)
            {
                ClothingItem item = options[r][c];
                if (item == null)
                {
                    continue;
                }
                Rectangle s = Layout.slotRect(r, c);

                Image icon = Assets.itemIcon(item, s.width - 8, s.height - 8);
                g2.drawImage(icon, s.x + 4, s.y + 4, null);

                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                drawName(g2, item.getName(), s);

                if (Assets.sameItem(item, chosen[r]))
                {
                    int t = Layout.SLOT_SELECT_THICKNESS;
                    g2.setColor(Layout.SLOT_SELECT_COLOR);
                    for (int i = 0; i < t; i++)
                    {
                        g2.drawRect(s.x - i, s.y - i, s.width - 1 + 2 * i, s.height - 1 + 2 * i);
                    }
                }
            }
        }
    }


    /** Name centered under the slot, shrunk so it fits the column. */
    private void drawName(Graphics2D g2, String name, Rectangle slot)
    {
        int maxWidth = Layout.GAME_SLOT_PITCH_X - 4;
        float size = Layout.SLOT_NAME_FONT_SIZE;
        Font f = Fonts.regular(size);
        FontMetrics fm = g2.getFontMetrics(f);
        while (size > 8 && fm.stringWidth(name) > maxWidth)
        {
            size -= 1;
            f = Fonts.regular(size);
            fm = g2.getFontMetrics(f);
        }
        g2.setFont(f);
        g2.setColor(Layout.SLOT_NAME_COLOR);
        int x = slot.x + (slot.width - fm.stringWidth(name)) / 2;
        int y = slot.y + slot.height + Layout.SLOT_NAME_GAP + fm.getAscent();
        g2.drawString(name, x, y);
    }
}