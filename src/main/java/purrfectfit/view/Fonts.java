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

public class Fonts
{
    /** Where the custom font lives on the classpath. */
    public static final String FONT_PATH = "/fonts/main.ttf";
 
    private static final Font BASE = loadBase();
 
    /** Big title text. */
    public static final Font TITLE = BASE.deriveFont(Font.BOLD, 56f);
    /** Normal text (buttons, dialogue). */
    public static final Font BODY = BASE.deriveFont(Font.PLAIN, 18f);
    /** HUD numbers (time, score, level). */
    public static final Font HUD = BASE.deriveFont(Font.BOLD, 20f);
 
 
    private Fonts()
    {
        // static only
    }
 
 
    private static Font loadBase()
    {
        try (InputStream in = Fonts.class.getResourceAsStream(FONT_PATH))
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
        return new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    }
}