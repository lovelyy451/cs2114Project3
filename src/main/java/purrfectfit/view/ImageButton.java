package purrfectfit.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import javax.swing.JButton;

// -------------------------------------------------------------------------
/**
 * A button painted from the blank button_base image with a Handjet label on
 * top. Used for Submit, Start, Retry and Next Level so they all match.
 * Hovering darkens it a little and pressing nudges it down a pixel or two.
 * If the image is missing it paints a plain rounded rectangle instead.
 */
public class ImageButton
    extends JButton
{
    private final int fontSize;
    private final Color textColor;
    private boolean hover;
    private BufferedImage normal;
    private BufferedImage dark;


    // ----------------------------------------------------------
    /**
     * Create a new ImageButton.
     *
     * @param text
     *            the label
     * @param bounds
     *            where it goes and how big
     * @param fontSize
     *            label size (shrinks automatically if it does not fit)
     * @param textColor
     *            label color
     */
    public ImageButton(String text, Rectangle bounds, int fontSize, Color textColor)
    {
        super(text);
        this.fontSize = fontSize;
        this.textColor = textColor;
        setBounds(bounds);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                hover = true;
                repaint();
            }


            @Override
            public void mouseExited(MouseEvent e)
            {
                hover = false;
                repaint();
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g.create();
        int w = getWidth();
        int h = getHeight();
        int off = getModel().isPressed() ? 2 : 0;
        if (!isEnabled())
        {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
        }
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        prepare(w, h);
        boolean light = !hover || !isEnabled();
        if (normal != null)
        {
            g2.drawImage(light ? normal : dark, 0, off, null);
        }
        else
        {
            g2.setColor(hover ? Assets.TEXT : Assets.PINK_DARK);
            g2.fillRoundRect(0, off, w - 1, h - 1 - off, 24, 24);
            g2.setColor(Assets.TEXT);
            g2.drawRoundRect(0, off, w - 1, h - 1 - off, 24, 24);
        }

        // label, with text anti-aliasing off to keep the pixel look
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        float size = fontSize;
        Font f = Fonts.bold(size);
        FontMetrics fm = g2.getFontMetrics(f);
        while (size > 8 && fm.stringWidth(getText()) > w - 16)
        {
            size -= 1;
            f = Fonts.bold(size);
            fm = g2.getFontMetrics(f);
        }
        g2.setFont(f);
        g2.setColor(textColor);
        int x = (w - fm.stringWidth(getText())) / 2;
        int y = (h - fm.getHeight()) / 2 + fm.getAscent() + off;
        g2.drawString(getText(), x, y);
        g2.dispose();
    }


    private void prepare(int w, int h)
    {
        if (normal != null && normal.getWidth() == w && normal.getHeight() == h)
        {
            return;
        }
        Image base = Assets.buttonBase();
        if (base == null)
        {
            normal = null;
            return;
        }
        normal = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = normal.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(base, 0, 0, w, h, null);
        g.dispose();
        dark = new RescaleOp(new float[] { 0.85f, 0.85f, 0.85f, 1f }, new float[4], null)
            .filter(normal, null);
    }
}
