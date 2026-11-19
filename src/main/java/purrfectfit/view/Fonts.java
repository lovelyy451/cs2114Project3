package purrfectfit.view;

// Imports :)

import java.awt.Font;
import java.io.InputStream;

/**
 * The fonts get loaded once, but it provides a fallback :D !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public final class Fonts
{
    /** Regular weight file. */
    public static final String REGULAR_PATH = "/fonts/Handjet-Regular.ttf";
    /** Bold weight file. */
    public static final String BOLD_PATH = "/fonts/Handjet-Bold.ttf";

    private static final Font REGULAR = load(REGULAR_PATH, Font.PLAIN);
    private static final Font BOLD = load(BOLD_PATH, Font.BOLD);


    private Fonts()
    {
        // static only
    }


    // ----------------------------------------------------------
    /**
     * Regular Handjet.
     *
     * @param size
     *            point size
     * @return the font
     */
    public static Font regular(float size)
    {
        return REGULAR.deriveFont(size);
    }


    // ----------------------------------------------------------
    /**
     * Bold Handjet.
     *
     * @param size
     *            point size
     * @return the font
     */
    public static Font bold(float size)
    {
        return BOLD.deriveFont(size);
    }


    private static Font load(String path, int fallbackStyle)
    {
        try (InputStream in = Fonts.class.getResourceAsStream(path))
        {
            if (in != null)
            {
                return Font.createFont(Font.TRUETYPE_FONT, in);
            }
        }
        catch (Exception e)
        {
            // fall through to the fallback
        }
        return new Font(Font.SANS_SERIF, fallbackStyle, 12);
    }
}