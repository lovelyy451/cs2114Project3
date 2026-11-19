package purrfectfit.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.Path2D;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

// -------------------------------------------------------------------------
/**
 * Music on/off button. If images/music_on.png and images/music_off.png exist
 * they are used; otherwise a speaker icon is painted with Java2D. Every
 * screen has its own copy, all sharing one Music, so the window repaints
 * after a click to keep them in sync.
 */
public class MuteButton
    extends JButton
{
    private final Music music;
    private boolean hover;


    // ----------------------------------------------------------
    /**
     * Create a new MuteButton.
     *
     * @param music
     *            the shared music player
     * @param bounds
     *            where it goes and how big
     */
    public MuteButton(Music music, Rectangle bounds)
    {
        this.music = music;
        setBounds(bounds);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Music on/off");
        addActionListener(e -> {
            music.setMuted(!music.isMuted());
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null)
            {
                w.repaint();
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e)
            {
                hover = true;
                repaint();
            }


            @Override
            public void mouseExited(java.awt.event.MouseEvent e)
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
        boolean off = music.isMuted();
        Image art = Assets.muteIcon(!off);
        if (art != null)
        {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(art, 0, 0, w, h, null);
            g2.dispose();
            return;
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hover ? Assets.TEXT : Assets.PINK_DARK);
        g2.fillRoundRect(0, 0, w - 1, h - 1, h, h);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // speaker, centered
        float cx = w / 2f - 6;
        float cy = h / 2f;
        Path2D sp = new Path2D.Float();
        sp.moveTo(cx - 6, cy - 3);
        sp.lineTo(cx - 2, cy - 3);
        sp.lineTo(cx + 4, cy - 8);
        sp.lineTo(cx + 4, cy + 8);
        sp.lineTo(cx - 2, cy + 3);
        sp.lineTo(cx - 6, cy + 3);
        sp.closePath();
        g2.fill(sp);
        if (off)
        {
            g2.drawLine((int)cx + 9, (int)cy - 5, (int)cx + 19, (int)cy + 5);
            g2.drawLine((int)cx + 9, (int)cy + 5, (int)cx + 19, (int)cy - 5);
        }
        else
        {
            g2.drawArc((int)cx + 5, (int)cy - 5, 8, 10, -60, 120);
            g2.drawArc((int)cx + 8, (int)cy - 9, 12, 18, -60, 120);
        }
        g2.dispose();
    }
}
