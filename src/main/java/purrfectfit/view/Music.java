package purrfectfit.view;

// Imports :)

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Plays music for the player :D !!
 * 
 * @author Amelia Vasquez Rosario (ameliav)
 * @version November 2026
 */

public class Music
{
    /** Where the song lives on the classpath (.wav works everywhere). */
    public static final String MUSIC_PATH = "/music/theme.wav";
 
    private Clip trackClip;
    private boolean muted;
 
 
    // ----------------------------------------------------------
    /**
     * Tries to load the song.
     */
    public Music()
    {
        try (var in = Music.class.getResourceAsStream(MUSIC_PATH))
        {
            if (in != null)
            {
                AudioInputStream audio = AudioSystem.getAudioInputStream(
                    new java.io.BufferedInputStream(in));
                trackClip = AudioSystem.getClip();
                trackClip.open(audio);
            }
        }
        catch (Exception e)
        {
            trackClip = null;
        }
    }
 
 
    // ----------------------------------------------------------
    /**
     * Starts the looped song (unless muted or missing).
     */
    public void play()
    {
        if (trackClip != null && !muted && !trackClip.isRunning())
        {
            trackClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
 
 
    // ----------------------------------------------------------
    /**
     * Mutes or unmutes.
     *
     * @param muted
     *            true to silence
     */
    public void setMuted(boolean muted)
    {
        this.muted = muted;
        if (trackClip == null)
        {
            return;
        }
        if (muted)
        {
            trackClip.stop();
        }
        else
        {
            play();
        }
    }
 
 
    // ----------------------------------------------------------
    /**
     * Whether the music is muted.
     *
     * @return true if muted
     */
    public boolean isMuted()
    {
        return muted;
    }
}