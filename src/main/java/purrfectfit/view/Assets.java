package purrfectfit.view;

// Imports :)

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

import purrfectfit.model.ClothingItem;
import purrfectfit.model.Personality;

/**
 * Has and loads every image once and caches them :) !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class Assets
{
    /** Native scene size (art is drawn at this size, then scaled up). */
    public static final int SCENE_W = 320;
    /** Native scene height. */
    public static final int SCENE_H = 180;
    /** Size of the little picture shown on each option button. */
    public static final int ICON_W = 80;
    /** Height of the option button picture. */
    public static final int ICON_H = 48;
    /** Number of different cat bases. */
    public static final int CAT_VARIANTS = 4;
 
    /** Colors shared by the whole UI (taken from the spec doc palette). */
    public static final Color PINK_DARK = new Color(232, 149, 154);
    /** Light pink. */
    public static final Color PINK_LIGHT = new Color(245, 200, 200);
    /** Very light pink for backgrounds. */
    public static final Color PINK_PALE = new Color(252, 230, 230);
    /** Text color. */
    public static final Color TEXT = new Color(214, 80, 98);
 
    // Where the dressed-up parts sit on the cat (native coordinates).
    // x, y, w, h
    private static final int[] HAT_BOX = { 136, 14, 48, 26 };
    private static final int[] TOP_BOX = { 134, 70, 52, 54 };
    private static final int[] SHOES_BOX = { 138, 144, 44, 14 };
 
    private static final Map<String, BufferedImage> CACHE = new HashMap<>();
 
 
    private Assets()
    {
        // static only
    }
 
 
    // ----------------------------------------------------------
    /**
     * Stable cache key for an item (does not rely on equals/hashCode).
     *
     * @param item
     *            the clothing item
     * @return a key like "0:Red Beanie"
     */
    public static String key(ClothingItem item)
    {
        return item.getCategory().ordinal() + ":" + item.getName();
    }
 
 
    // ----------------------------------------------------------
    /**
     * True when both items are the same clothing item (or both null).
     *
     * @param a
     *            first item
     * @param b
     *            second item
     * @return whether they are the same item
     */
    public static boolean sameItem(ClothingItem a, ClothingItem b)
    {
        if (a == null || b == null)
        {
            return a == b;
        }
        return key(a).equals(key(b));
    }
 
 
    // ----------------------------------------------------------
    /**
     * The room behind the cat.
     *
     * @return scene-sized image
     */
    public static Image background()
    {
        return cached("background", () -> {
            BufferedImage img = blankScene();
            Graphics2D g = flat(img);
            g.setColor(PINK_PALE);
            g.fillRect(0, 0, SCENE_W, SCENE_H);
            g.setColor(PINK_LIGHT); // wall stripes
            for (int x = 0; x < SCENE_W; x += 32)
            {
                g.fillRect(x, 0, 16, 160);
            }
            g.setColor(Color.WHITE); // window
            g.fillRect(30, 24, 52, 46);
            g.setColor(new Color(170, 215, 240));
            g.fillRect(34, 28, 44, 38);
            g.setColor(PINK_DARK); // floor
            g.fillRect(0, 160, SCENE_W, 20);
            g.dispose();
            return img;
        });
    }
 
 
    // ----------------------------------------------------------
    /**
     * The counter/desk drawn in front of the cat.
     *
     * @return scene-sized image (transparent except for the counter)
     */
    public static Image counter()
    {
        return cached("counter", () -> {
            BufferedImage img = blankScene();
            Graphics2D g = flat(img);
            g.setColor(new Color(150, 100, 80));
            g.fillRect(0, 162, SCENE_W, 18);
            g.setColor(new Color(190, 135, 105));
            g.fillRect(0, 162, SCENE_W, 4);
            g.dispose();
            return img;
        });
    }
 
 
    // ----------------------------------------------------------
    /**
     * The undressed cat. TEMPORARY: the Model has no "base" on Customer yet,
     * so the cat is picked from the personality. Once Customer exposes its
     * base (white/black/ginger/calico), call catBase(int) with that instead.
     *
     * @param personality
     *            the customer's personality
     * @return scene-sized image
     */
    public static Image catBase(Personality personality)
    {
        return catBase(personality == null ? 0 : personality.ordinal());
    }
 
 
    // ----------------------------------------------------------
    /**
     * The undressed cat.
     *
     * @param variant
     *            0 = white, 1 = black, 2 = ginger, 3 = calico
     * @return scene-sized image
     */
    public static Image catBase(int variant)
    {
        final int v = Math.floorMod(variant, CAT_VARIANTS);
        return cached("cat:" + v, () -> {
            Color[] fur = { new Color(250, 248, 245), new Color(52, 46, 58),
                new Color(231, 143, 62), new Color(250, 248, 245) };
            Color[] outline = { new Color(165, 160, 172), new Color(22, 20, 28),
                new Color(160, 88, 38), new Color(150, 140, 140) };
            boolean dark = v == 1;
 
            Area cat = new Area();
            Shape[] parts = { new Rectangle2D.Double(180, 104, 22, 8), // tail
                new Rectangle2D.Double(196, 90, 8, 22),
                new Rectangle2D.Double(146, 120, 10, 30),              // legs
                new Rectangle2D.Double(164, 120, 10, 30),
                new Rectangle2D.Double(132, 74, 8, 34),                // arms
                new Rectangle2D.Double(180, 74, 8, 34),
                new Rectangle2D.Double(140, 70, 40, 54),               // body
                new Ellipse2D.Double(138, 28, 44, 42),                 // head
                new java.awt.Polygon(new int[] { 140, 146, 157 },
                    new int[] { 40, 18, 32 }, 3),                      // ears
                new java.awt.Polygon(new int[] { 180, 174, 163 },
                    new int[] { 40, 18, 32 }, 3) };
            for (Shape s : parts)
            {
                cat.add(new Area(s));
            }
 
            BufferedImage img = blankScene();
            Graphics2D g = flat(img);
            g.setColor(outline[v]);
            g.setStroke(new BasicStroke(2f));
            g.draw(cat);
            g.setColor(fur[v]);
            g.fill(cat);
 
            if (v == 3) // calico patches, clipped to the cat's shape
            {
                g.setClip(cat);
                g.setColor(new Color(231, 143, 62));
                g.fillOval(134, 22, 26, 24);
                g.fillRect(140, 84, 22, 28);
                g.fillRect(180, 74, 10, 30);
                g.setColor(new Color(52, 46, 58));
                g.fillOval(166, 14, 20, 20);
                g.fillRect(158, 100, 26, 26);
                g.fillRect(146, 126, 10, 24);
                g.setClip(null);
            }
 
            g.setColor(dark ? new Color(255, 225, 110) : new Color(50, 40, 55)); // eyes
            g.fillRect(150, 46, 4, 6);
            g.fillRect(166, 46, 4, 6);
            g.setColor(dark ? new Color(210, 210, 215) : new Color(90, 80, 90)); // whiskers
            g.fillRect(142, 58, 12, 1);
            g.fillRect(166, 58, 12, 1);
            g.setColor(new Color(240, 130, 150)); // nose
            g.fillRect(158, 54, 4, 3);
            g.dispose();
            return img;
        });
    }
 
 
    // ----------------------------------------------------------
    /**
     * The clothing drawn in scene position, to be stacked over the cat.
     *
     * @param item
     *            the item
     * @return scene-sized image (transparent except for the item)
     */
    public static Image itemLayer(ClothingItem item)
    {
        return cached("layer:" + key(item), () -> {
            BufferedImage img = blankScene();
            Graphics2D g = flat(img);
            int[] box = boxFor(item);
            paintItem(g, item, box[0], box[1], box[2], box[3]);
            g.dispose();
            return img;
        });
    }
 
 
    // ----------------------------------------------------------
    /**
     * Small picture of the item for its option button.
     *
     * @param item
     *            the item
     * @return ICON_W x ICON_H image
     */
    public static Image itemIcon(ClothingItem item)
    {
        return cached("icon:" + key(item), () -> {
            BufferedImage img = new BufferedImage(ICON_W, ICON_H,
                BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            int[] box = boxFor(item);
            double s = Math.min((ICON_W - 6.0) / box[2], (ICON_H - 6.0) / box[3]);
            AffineTransform t = new AffineTransform();
            t.translate(ICON_W / 2.0, ICON_H / 2.0);
            t.scale(s, s);
            t.translate(-(box[0] + box[2] / 2.0), -(box[1] + box[3] / 2.0));
            g.transform(t);
            paintItem(g, item, box[0], box[1], box[2], box[3]);
            g.dispose();
            return img;
        });
    }
 
 
    // ----------------------------------------------------------
    /**
     * Loads a PNG from src/main/resources (for when real art exists).
     *
     * @param path
     *            e.g. "/images/cat_ginger.png"
     * @return the image, or null if it is missing
     */
    public static Image load(String path)
    {
        try (var in = Assets.class.getResourceAsStream(path))
        {
            return in == null ? null : ImageIO.read(in);
        }
        catch (java.io.IOException e)
        {
            return null;
        }
    }
 
 
    // ---------------- placeholder painting helpers ----------------
 
    private static int[] boxFor(ClothingItem item)
    {
        // Slot order is the enum order: 0 = hat, 1 = top, 2 = shoes.
        switch (item.getCategory().ordinal())
        {
            case 0:
                return HAT_BOX;
            case 1:
                return TOP_BOX;
            default:
                return SHOES_BOX;
        }
    }
 
 
    private static void paintItem(
        Graphics2D g,
        ClothingItem item,
        int x,
        int y,
        int w,
        int h)
    {
        String n = item.getName().toLowerCase();
        boolean known;
        switch (item.getCategory().ordinal())
        {
            case 0:
                known = paintHat(g, n, x, y, w, h);
                break;
            case 1:
                known = paintTop(g, n, x, y, w, h);
                break;
            default:
                known = paintShoes(g, n, x, y, w, h);
                break;
        }
        if (!known)
        {
            paintGeneric(g, item, x, y, w, h);
        }
    }
 
 
    private static boolean paintHat(Graphics2D g, String n, int x, int y, int w, int h)
    {
        int cx = x + w / 2;
        if (n.contains("crown"))
        {
            g.setColor(new Color(250, 208, 60));
            g.fillPolygon(new int[] { x + 6, x + 6, x + 14, cx, x + w - 14, x + w - 6, x + w - 6 },
                new int[] { y + h - 12, y + 4, y + 12, y + 2, y + 12, y + 4, y + h - 12 }, 7);
            g.fillRect(x + 6, y + h - 12, w - 12, 12);
            g.setColor(new Color(220, 50, 70));
            g.fillRect(cx - 2, y + h - 9, 4, 4);
            g.setColor(new Color(70, 110, 230));
            g.fillRect(x + 12, y + h - 9, 4, 4);
            g.fillRect(x + w - 16, y + h - 9, 4, 4);
        }
        else if (n.contains("top hat"))
        {
            g.setColor(new Color(40, 40, 50));
            g.fillRect(x + 10, y, w - 20, h - 6);
            g.fillRect(x + 2, y + h - 6, w - 4, 5);
            g.setColor(new Color(200, 60, 80));
            g.fillRect(x + 10, y + h - 12, w - 20, 5);
        }
        else if (n.contains("bow"))
        {
            int cy = y + h - 8;
            g.setColor(new Color(240, 110, 160));
            g.fillPolygon(new int[] { cx, x + 6, x + 6 }, new int[] { cy, cy - 9, cy + 9 }, 3);
            g.fillPolygon(new int[] { cx, x + w - 6, x + w - 6 },
                new int[] { cy, cy - 9, cy + 9 }, 3);
            g.setColor(new Color(200, 70, 120));
            g.fillRect(cx - 3, cy - 5, 6, 10);
        }
        else if (n.contains("sun hat"))
        {
            g.setColor(new Color(240, 215, 140));
            g.fillArc(x + 10, y + 2, w - 20, 2 * (h - 8), 0, 180);
            g.fillOval(x, y + h - 12, w, 12);
            g.setColor(new Color(220, 90, 110));
            g.fillRect(x + 10, y + h - 14, w - 20, 4);
        }
        else if (n.contains("cap"))
        {
            g.setColor(new Color(70, 110, 220));
            g.fillArc(x + 6, y + 2, w - 16, 2 * (h - 6), 0, 180);
            g.fillRect(cx - 7, y + 1, 3, 3);
            g.setColor(new Color(45, 75, 170));
            g.fillRect(cx, y + h - 8, w / 2, 4);
        }
        else
        {
            return false;
        }
        return true;
    }
 
 
    private static boolean paintTop(Graphics2D g, String n, int x, int y, int w, int h)
    {
        int cx = x + w / 2;
        if (n.contains("dress"))
        {
            g.setColor(new Color(170, 130, 220));
            g.fillRect(x + 8, y, w - 16, h / 2);
            g.fillPolygon(new int[] { x + 8, x + w - 8, x + w + 2, x - 2 },
                new int[] { y + h / 2, y + h / 2, y + h, y + h }, 4);
            g.fillOval(x + 2, y, 12, 12);
            g.fillOval(x + w - 14, y, 12, 12);
            g.setColor(new Color(250, 150, 190));
            g.fillRect(x + 8, y + h / 2 - 3, w - 16, 4);
        }
        else if (n.contains("suit"))
        {
            g.setColor(new Color(50, 55, 90));
            g.fillRect(x + 8, y, w - 16, h);
            g.fillRect(x, y, 10, h / 2 + 8);
            g.fillRect(x + w - 10, y, 10, h / 2 + 8);
            g.setColor(Color.WHITE);
            g.fillPolygon(new int[] { cx - 8, cx + 8, cx }, new int[] { y, y, y + 20 }, 3);
            g.setColor(new Color(200, 50, 70));
            g.fillRect(cx - 2, y + 7, 4, 18);
            g.setColor(new Color(30, 32, 60));
            g.fillRect(cx - 1, y + 26, 2, h - 26);
        }
        else if (n.contains("cute"))
        {
            g.setColor(new Color(255, 170, 200));
            g.fillRect(x + 8, y, w - 16, h - 14);
            g.fillRect(x + 2, y, 10, 12);
            g.fillRect(x + w - 12, y, 10, 12);
            g.setColor(new Color(250, 130, 170));
            g.fillRect(x + 4, y + h - 18, w - 8, 18);
            g.setColor(Color.WHITE);
            g.fillOval(cx - 7, y + 8, 7, 7);
            g.fillOval(cx, y + 8, 7, 7);
            g.fillPolygon(new int[] { cx - 7, cx + 7, cx }, new int[] { y + 13, y + 13, y + 22 }, 3);
        }
        else if (n.contains("beach"))
        {
            g.setColor(new Color(40, 190, 200));
            g.fillRect(x + 8, y, w - 16, h - 8);
            g.fillRect(x + 2, y, 10, 14);
            g.fillRect(x + w - 12, y, 10, 14);
            g.setColor(new Color(255, 140, 60));
            g.fillOval(x + 14, y + 8, 7, 7);
            g.fillOval(x + w - 22, y + 16, 7, 7);
            g.fillOval(x + 20, y + 28, 7, 7);
            g.setColor(new Color(255, 230, 80));
            g.fillRect(x + 16, y + 10, 3, 3);
            g.fillRect(x + w - 20, y + 18, 3, 3);
            g.fillRect(x + 22, y + 30, 3, 3);
            g.setColor(new Color(255, 200, 80));
            g.fillRect(x + 8, y + h - 10, w - 16, 10);
        }
        else if (n.contains("casual"))
        {
            g.setColor(new Color(120, 200, 120));
            g.fillRect(x + 8, y, w - 16, h / 2 + 2);
            g.fillRect(x + 2, y, 10, 14);
            g.fillRect(x + w - 12, y, 10, 14);
            g.setColor(new Color(70, 100, 170));
            g.fillRect(x + 8, y + h / 2 + 2, w - 16, h / 2 - 2);
            g.setColor(new Color(50, 75, 135));
            g.fillRect(cx - 1, y + h / 2 + 2, 2, h / 2 - 2);
        }
        else
        {
            return false;
        }
        return true;
    }
 
 
    private static boolean paintShoes(Graphics2D g, String n, int x, int y, int w, int h)
    {
        int sw = w / 2 - 2;
        if (!(n.contains("heel") || n.contains("fancy") || n.contains("mary")
            || n.contains("sandal") || n.contains("sneaker")))
        {
            return false;
        }
        for (int i = 0; i < 2; i++)
        {
            int sx = x + i * (w / 2 + 2);
            if (n.contains("heel"))
            {
                g.setColor(new Color(210, 40, 70));
                g.fillRect(sx + 2, y + 3, sw - 4, 6);
                g.fillRect(sx + sw - 6, y + 9, 2, 5);
                g.fillRect(sx + 2, y + 9, sw - 8, 2);
            }
            else if (n.contains("fancy"))
            {
                g.setColor(new Color(30, 30, 40));
                g.fillRect(sx, y + 2, sw, h - 4);
                g.setColor(new Color(120, 80, 60));
                g.fillRect(sx, y + h - 3, sw, 3);
                g.setColor(Color.WHITE);
                g.fillRect(sx + 3, y + 3, 6, 2);
            }
            else if (n.contains("mary"))
            {
                g.setColor(new Color(220, 70, 120));
                g.fillRect(sx, y + 2, sw, h - 2);
                g.setColor(Color.WHITE);
                g.fillRect(sx, y + 5, sw, 2);
                g.setColor(new Color(250, 210, 80));
                g.fillRect(sx + sw / 2 - 1, y + 4, 3, 4);
            }
            else if (n.contains("sandal"))
            {
                g.setColor(new Color(200, 160, 100));
                g.fillRect(sx, y + h - 4, sw, 4);
                g.setColor(new Color(235, 120, 60));
                g.fillRect(sx + 4, y + 3, 3, h - 5);
                g.fillRect(sx + sw - 8, y + 3, 3, h - 5);
                g.fillRect(sx + 2, y + 6, sw - 4, 2);
            }
            else
            {
                g.setColor(new Color(245, 245, 245));
                g.fillRect(sx, y + 2, sw, h - 5);
                g.setColor(new Color(130, 130, 140));
                g.drawRect(sx, y + 2, sw - 1, h - 6);
                g.setColor(new Color(120, 180, 230));
                g.fillRect(sx, y + h - 4, sw, 4);
                g.setColor(new Color(230, 70, 90));
                g.fillRect(sx + 4, y + 5, 10, 2);
            }
        }
        return true;
    }
 
 
    /** Fallback for a name the placeholder does not know. */
    private static void paintGeneric(
        Graphics2D g,
        ClothingItem item,
        int x,
        int y,
        int w,
        int h)
    {
        float hue = Math.floorMod(item.getName().hashCode(), 360) / 360f;
        g.setColor(Color.getHSBColor(hue, 0.55f, 0.95f));
        g.fillRect(x + 4, y + 2, w - 8, h - 4);
        g.setColor(Color.getHSBColor((hue + 0.5f) % 1f, 0.6f, 0.85f));
        g.fillRect(x + 4, y + h - 6, w - 8, 4);
    }
 
 
    private static BufferedImage blankScene()
    {
        return new BufferedImage(SCENE_W, SCENE_H, BufferedImage.TYPE_INT_ARGB);
    }
 
 
    /** Graphics with antialiasing off so the scene stays crisp/pixelly. */
    private static Graphics2D flat(BufferedImage img)
    {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setStroke(new BasicStroke(1));
        return g;
    }
 
 
    private static BufferedImage cached(
        String name,
        java.util.function.Supplier<BufferedImage> maker)
    {
        return CACHE.computeIfAbsent(name, n -> maker.get());
    }
}