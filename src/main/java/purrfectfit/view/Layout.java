package purrfectfit.view;

import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.SwingConstants;

// -------------------------------------------------------------------------
/**
 * Every position, size and text style on screen, in 1024 x 576 art pixels
 * (the window is exactly this size, nothing is scaled).
 *
 * These constants use the same names as the "Java numbers" panel of the
 * layout editor, so you can paste the editor's output over the matching
 * lines here. Rectangles are x, y, width, height.
 *
 * Full-size layers (cats, clothes, counter, bubble, panel, HUD band) are
 * drawn at 0,0 and need no numbers. The *_GUIDE and *_BUBBLE rectangles are
 * only kept so a pasted export still compiles.
 */
public final class Layout
{
    /** Window / art width. */
    public static final int WIDTH = 1024;
    /** Window / art height. */
    public static final int HEIGHT = 576;

    /** White card drawn behind the text on the Start and Results screens. */
    public static final Rectangle MENU_CARD = new Rectangle(96, 80, 832, 420);

    // ---- Start screen ----
    /** Mute button. */
    public static final Rectangle START_MUTE_BUTTON = new Rectangle(926, 8, 84, 28);
    /** Title. */
    public static final Rectangle START_TITLE = new Rectangle(112, 120, 800, 100);
    /** Title font size. */
    public static final int START_TITLE_FONT_SIZE = 72;
    /** Title color. */
    public static final Color START_TITLE_COLOR = new Color(0xD65062);
    /** Title alignment. */
    public static final int START_TITLE_ALIGN = SwingConstants.CENTER;
    /** Subtitle. */
    public static final Rectangle START_SUBTITLE = new Rectangle(212, 230, 600, 40);
    /** Subtitle font size. */
    public static final int START_SUBTITLE_FONT_SIZE = 28;
    /** Subtitle color. */
    public static final Color START_SUBTITLE_COLOR = new Color(0xD65062);
    /** Subtitle alignment. */
    public static final int START_SUBTITLE_ALIGN = SwingConstants.CENTER;
    /** Start button. */
    public static final Rectangle START_START_BUTTON = new Rectangle(392, 330, 240, 72);
    /** Start button font size. */
    public static final int START_START_BUTTON_FONT_SIZE = 36;
    /** Start button text color. */
    public static final Color START_START_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Start button alignment. */
    public static final int START_START_BUTTON_ALIGN = SwingConstants.CENTER;
    /** Music credit. */
    public static final Rectangle START_CREDIT = new Rectangle(20, 530, 600, 30);
    /** Credit font size. */
    public static final int START_CREDIT_FONT_SIZE = 16;
    /** Credit color. */
    public static final Color START_CREDIT_COLOR = new Color(0xD65062);
    /** Credit alignment. */
    public static final int START_CREDIT_ALIGN = SwingConstants.LEFT;

    // ---- Game screen ----
    /** HUD text (level, time, score, progress). */
    public static final Rectangle GAME_HUD = new Rectangle(0, 0, 1024, 45);
    /** HUD font size. */
    public static final int GAME_HUD_FONT_SIZE = 24;
    /** HUD color. */
    public static final Color GAME_HUD_COLOR = new Color(0xFFFFFF);
    /** HUD alignment. */
    public static final int GAME_HUD_ALIGN = SwingConstants.CENTER;
    /** Mute button. */
    public static final Rectangle GAME_MUTE_BUTTON = new Rectangle(926, 8, 84, 28);
    /** Not used (the bubble image is full size). */
    public static final Rectangle GAME_BUBBLE = new Rectangle(156, 93, 260, 150);
    /** Wrapping dialogue text area inside the bubble. */
    public static final Rectangle GAME_BUBBLE_TEXT = new Rectangle(172, 108, 228, 114);
    /** Dialogue font size. */
    public static final int GAME_BUBBLE_TEXT_FONT_SIZE = 20;
    /** Dialogue color. */
    public static final Color GAME_BUBBLE_TEXT_COLOR = new Color(0xD65062);
    /** Dialogue alignment. */
    public static final int GAME_BUBBLE_TEXT_ALIGN = SwingConstants.LEFT;
    /** Not used (layers are full size). */
    public static final Rectangle GAME_HAT_GUIDE = new Rectangle(385, 95, 252, 105);
    /** Not used (layers are full size). */
    public static final Rectangle GAME_TOP_GUIDE = new Rectangle(455, 255, 117, 80);
    /** Not used (layers are full size). */
    public static final Rectangle GAME_SHOES_GUIDE = new Rectangle(490, 365, 50, 20);
    /** Feedback message. */
    public static final Rectangle GAME_FEEDBACK = new Rectangle(20, 500, 660, 60);
    /** Feedback font size. */
    public static final int GAME_FEEDBACK_FONT_SIZE = 28;
    /** Feedback color. */
    public static final Color GAME_FEEDBACK_COLOR = new Color(0xD65062);
    /** Feedback alignment. */
    public static final int GAME_FEEDBACK_ALIGN = SwingConstants.LEFT;
    /** First (top-left) item slot. Rows are hat, top, shoes; columns are the 3 choices. */
    public static final Rectangle GAME_SLOT = new Rectangle(751, 111, 69, 67);
    /** Distance between slot columns. */
    public static final int GAME_SLOT_PITCH_X = 81;
    /** Distance between slot rows. */
    public static final int GAME_SLOT_PITCH_Y = 132;
    /** Exit button (left end of the HUD band; position is a guess, move it). */
    public static final Rectangle GAME_EXIT_BUTTON = new Rectangle(10, 8, 80, 28);
    /** Exit button font size. */
    public static final int GAME_EXIT_BUTTON_FONT_SIZE = 18;
    /** Exit button text color. */
    public static final Color GAME_EXIT_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Hint button (next to Exit; position is a guess, move it). */
    public static final Rectangle GAME_HINT_BUTTON = new Rectangle(100, 8, 80, 28);
    /** Hint button font size. */
    public static final int GAME_HINT_BUTTON_FONT_SIZE = 18;
    /** Hint button text color. */
    public static final Color GAME_HINT_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Submit button. */
    public static final Rectangle GAME_SUBMIT_BUTTON = new Rectangle(769, 497, 194, 64);
    /** Submit font size. */
    public static final int GAME_SUBMIT_BUTTON_FONT_SIZE = 32;
    /** Submit text color. */
    public static final Color GAME_SUBMIT_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Submit alignment. */
    public static final int GAME_SUBMIT_BUTTON_ALIGN = SwingConstants.CENTER;

    // ---- Item names drawn under the slot icons (not in the editor) ----
    /** Gap between the bottom of a slot and its name. */
    public static final int SLOT_NAME_GAP = 4;
    /** Largest name font size. */
    public static final int SLOT_NAME_FONT_SIZE = 18;
    /** Name color. */
    public static final Color SLOT_NAME_COLOR = new Color(0xFFFFFF);
    /** Selection outline color. */
    public static final Color SLOT_SELECT_COLOR = new Color(0xD6336C);
    /** Selection outline thickness. */
    public static final int SLOT_SELECT_THICKNESS = 3;

    // ---- Results screen ----
    /** Mute button. */
    public static final Rectangle RESULTS_MUTE_BUTTON = new Rectangle(926, 8, 84, 28);
    /** Outcome text. */
    public static final Rectangle RESULTS_OUTCOME = new Rectangle(112, 130, 800, 100);
    /** Outcome font size. */
    public static final int RESULTS_OUTCOME_FONT_SIZE = 72;
    /** Outcome color. */
    public static final Color RESULTS_OUTCOME_COLOR = new Color(0xD65062);
    /** Outcome alignment. */
    public static final int RESULTS_OUTCOME_ALIGN = SwingConstants.CENTER;
    /** Level text. */
    public static final Rectangle RESULTS_LEVEL = new Rectangle(312, 240, 400, 44);
    /** Level font size. */
    public static final int RESULTS_LEVEL_FONT_SIZE = 34;
    /** Level color. */
    public static final Color RESULTS_LEVEL_COLOR = new Color(0xD65062);
    /** Level alignment. */
    public static final int RESULTS_LEVEL_ALIGN = SwingConstants.CENTER;
    /** Score text. */
    public static final Rectangle RESULTS_SCORE = new Rectangle(312, 290, 400, 44);
    /** Score font size. */
    public static final int RESULTS_SCORE_FONT_SIZE = 34;
    /** Score color. */
    public static final Color RESULTS_SCORE_COLOR = new Color(0xD65062);
    /** Score alignment. */
    public static final int RESULTS_SCORE_ALIGN = SwingConstants.CENTER;
    /** Next Level button. */
    public static final Rectangle RESULTS_NEXT_BUTTON = new Rectangle(392, 380, 240, 72);
    /** Next button font size. */
    public static final int RESULTS_NEXT_BUTTON_FONT_SIZE = 32;
    /** Next button text color. */
    public static final Color RESULTS_NEXT_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Next button alignment. */
    public static final int RESULTS_NEXT_BUTTON_ALIGN = SwingConstants.CENTER;
    /** Retry button. */
    public static final Rectangle RESULTS_RETRY_BUTTON = new Rectangle(392, 380, 240, 72);
    /** Retry font size. */
    public static final int RESULTS_RETRY_BUTTON_FONT_SIZE = 32;
    /** Retry text color. */
    public static final Color RESULTS_RETRY_BUTTON_COLOR = new Color(0xFFFFFF);
    /** Retry alignment. */
    public static final int RESULTS_RETRY_BUTTON_ALIGN = SwingConstants.CENTER;


    private Layout()
    {
        // constants only
    }


    // ----------------------------------------------------------
    /**
     * Rectangle of one item slot.
     *
     * @param row
     *            0 = hat, 1 = top, 2 = shoes
     * @param col
     *            which of the 3 choices
     * @return the slot rectangle
     */
    public static Rectangle slotRect(int row, int col)
    {
        return new Rectangle(GAME_SLOT.x + col * GAME_SLOT_PITCH_X,
            GAME_SLOT.y + row * GAME_SLOT_PITCH_Y, GAME_SLOT.width, GAME_SLOT.height);
    }
}