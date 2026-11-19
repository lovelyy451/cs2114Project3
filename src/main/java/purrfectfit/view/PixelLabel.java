package purrfectfit.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JLabel;

// -------------------------------------------------------------------------
/**
 * A label for the pixel font: text anti-aliasing is switched off so the
 * letters stay crisp. Placed with absolute bounds from Layout.
 */
public class PixelLabel
    extends JLabel
{
    // ----------------------------------------------------------
    /**
     * Create a new PixelLabel.
     *
     * @param text
     *            starting text
     * @param bounds
     *            where it goes
     * @param fontSize
     *            Handjet size
     * @param color
     *            text color
     * @param align
     *            a SwingConstants alignment
     */
    public PixelLabel(String text, Rectangle bounds, int fontSize, Color color, int align)
    {
        super(text, align);
        setBounds(bounds);
        setFont(Fonts.regular(fontSize));
        setForeground(color);
        setOpaque(false);
    }


    // ----------------------------------------------------------
    /**
     * Same as the other constructor but with an explicit font.
     *
     * @param text
     *            starting text
     * @param bounds
     *            where it goes
     * @param font
     *            the font
     * @param color
     *            text color
     * @param align
     *            a SwingConstants alignment
     */
    public PixelLabel(String text, Rectangle bounds, Font font, Color color, int align)
    {
        super(text, align);
        setBounds(bounds);
        setFont(font);
        setForeground(color);
        setOpaque(false);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        super.paintComponent(g);
    }
}
