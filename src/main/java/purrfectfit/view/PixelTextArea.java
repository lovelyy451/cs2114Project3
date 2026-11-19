package purrfectfit.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.JTextArea;

// -------------------------------------------------------------------------
/**
 * Transparent, read-only, word-wrapping text for the speech bubble. Sits on
 * top of the bubble image so the bubble shows through.
 */
public class PixelTextArea
    extends JTextArea
{
    // ----------------------------------------------------------
    /**
     * Create a new PixelTextArea.
     *
     * @param bounds
     *            the area inside the bubble
     * @param fontSize
     *            Handjet size
     * @param color
     *            text color
     */
    public PixelTextArea(Rectangle bounds, int fontSize, Color color)
    {
        setBounds(bounds);
        setFont(Fonts.regular(fontSize));
        setForeground(color);
        setOpaque(false);
        setEditable(false);
        setFocusable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setBorder(null);
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        super.paintComponent(g);
    }
}
