package purrfectfit.view;

// Imports :)

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.JPanel;

import purrfectfit.model.ClothingCategory;

/**
 * Displays the cat, the outfit layers,
 * the background, and the counter :D !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class ScenePanel extends JPanel
{
    /** How much the 320 x 180 art is enlarged. */
    public static final int SCALE = 2;
 
    private Image baseImage;
    private Image shoesImage;
    private Image topImage;
    private Image hatImage;
 
 
    // ----------------------------------------------------------
    /**
     * Create a new ScenePanel.
     */
    public ScenePanel()
    {
        setPreferredSize(new Dimension(Assets.SCENE_W * SCALE, Assets.SCENE_H * SCALE));
        setOpaque(true);
    }
 
 
    // ----------------------------------------------------------
    /**
     * Sets the undressed cat to draw.
     *
     * @param base
     *            the cat image (null draws no cat)
     */
    public void setBase(Image base)
    {
        this.baseImage = base;
        repaint();
    }
 
 
    // ----------------------------------------------------------
    /**
     * Sets (or clears) the clothing layer for one category.
     *
     * @param category
     *            which slot
     * @param image
     *            the layer, or null to leave the slot empty
     */
    public void setLayer(ClothingCategory category, Image image)
    {
        // Slot order is the enum order: 0 = hat, 1 = top, 2 = shoes.
        switch (category.ordinal())
        {
            case 0:
                hatImage = image;
                break;
            case 1:
                topImage = image;
                break;
            default:
                shoesImage = image;
                break;
        }
        repaint();
    }
 
 
    // ----------------------------------------------------------
    /**
     * Removes all clothes.
     */
    public void clearOutfit()
    {
        hatImage = null;
        topImage = null;
        shoesImage = null;
        repaint();
    }
 
 
    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        draw(g2, Assets.background());
        draw(g2, baseImage);
        draw(g2, shoesImage);
        draw(g2, topImage);
        draw(g2, hatImage);
        draw(g2, Assets.counter());
    }
 
 
    private void draw(Graphics2D g2, Image image)
    {
        if (image != null)
        {
            g2.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        }
    }
}