package purrfectfit.view;

// Imports :)

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import purrfectfit.model.ClothingCategory;
import purrfectfit.model.ClothingItem;

/**
 * A row of 3 option buttons for a single category.
 * It's used thrice for the hat, top, and shoes :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class CategoryPanel extends JPanel
{
    /** Choices shown per category. */
    public static final int OPTIONS = 3;
 
    private static final Color OUTLINE = new Color(200, 120, 130);
 
    private final ClothingCategory category;
    private final JButton[] buttons = new JButton[OPTIONS];
    private final ClothingItem[] items = new ClothingItem[OPTIONS];
    private ClothingItem highlightedItem;
    private Consumer<ClothingItem> optionListener;
 
 
    // ----------------------------------------------------------
    /**
     * Create a new CategoryPanel.
     *
     * @param category
     *            which clothing category this row is for
     */
    public CategoryPanel(ClothingCategory category)
    {
        this.category = category;
        setLayout(new BorderLayout(0, 2));
        setOpaque(false);
 
        String name = category.name().substring(0, 1)
            + category.name().substring(1).toLowerCase();
        JLabel title = new JLabel(name, SwingConstants.LEFT);
        title.setFont(Fonts.HUD);
        title.setForeground(Assets.TEXT);
        add(title, BorderLayout.NORTH);
 
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setOpaque(false);
        for (int i = 0; i < OPTIONS; i++)
        {
            final int slot = i;
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(96, 92));
            b.setFont(Fonts.BODY.deriveFont(13f));
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setBackground(Color.WHITE);
            b.setFocusPainted(false);
            b.setMargin(new java.awt.Insets(2, 2, 2, 2));
            b.addActionListener(e -> {
                if (optionListener != null && items[slot] != null)
                {
                    optionListener.accept(items[slot]);
                }
            });
            buttons[i] = b;
            row.add(b);
        }
        add(row, BorderLayout.CENTER);
        refreshButtons();
    }
 
 
    // ----------------------------------------------------------
    /**
     * Which category this row shows.
     *
     * @return the category
     */
    public ClothingCategory getCategory()
    {
        return category;
    }
 
 
    // ----------------------------------------------------------
    /**
     * Shows a new set of choices (called when the front customer changes).
     *
     * @param options
     *            up to three items; missing slots are blanked
     */
    public void setOptions(ClothingItem[] options)
    {
        for (int i = 0; i < OPTIONS; i++)
        {
            items[i] = (options != null && i < options.length) ? options[i] : null;
        }
        highlightedItem = null;
        refreshButtons();
    }
 
 
    // ----------------------------------------------------------
    /**
     * Outlines the chosen item (display only).
     *
     * @param item
     *            the chosen item, or null for none
     */
    public void setHighlightedItem(ClothingItem item)
    {
        highlightedItem = item;
        refreshButtons();
    }
 
 
    // ----------------------------------------------------------
    /**
     * Sets who hears about clicks. Called once by the controller.
     *
     * @param listener
     *            receives the clicked item
     */
    public void setOptionListener(Consumer<ClothingItem> listener)
    {
        this.optionListener = listener;
    }
 
 
    private void refreshButtons()
    {
        for (int i = 0; i < OPTIONS; i++)
        {
            JButton b = buttons[i];
            ClothingItem item = items[i];
            b.setEnabled(item != null);
            if (item == null)
            {
                b.setIcon(null);
                b.setText("");
                b.setBorder(BorderFactory.createLineBorder(OUTLINE, 1));
                continue;
            }
            b.setIcon(new ImageIcon(Assets.itemIcon(item)));
            b.setText(item.getName());
            boolean chosen = Assets.sameItem(item, highlightedItem);
            b.setBackground(chosen ? Assets.PINK_LIGHT : Color.WHITE);
            b.setBorder(BorderFactory.createLineBorder(chosen ? Assets.TEXT : OUTLINE,
                chosen ? 4 : 1));
        }
    }
}