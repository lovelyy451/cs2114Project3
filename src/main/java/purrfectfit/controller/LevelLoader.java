package purrfectfit.controller;

import java.util.concurrent.ExecutionException;
import java.util.function.BooleanSupplier;
import javax.swing.SwingWorker;

// -------------------------------------------------------------------------
/**
 * Starts a level in the background so the window does not freeze.
 *
 * The model does all the real work itself: startLevel, nextLevel and
 * retryLevel each build the customer queue and prepare the first dialogue
 * (which may be a slow AI request). This class only runs that one call off
 * the Swing thread and then reports back on the Swing thread.
 */
public class LevelLoader
    extends SwingWorker<Boolean, Void>
{
    private final BooleanSupplier startAction;
    private final Runnable onFinished;
    private final Runnable onFailed;

    // ----------------------------------------------------------
    /**
     * Create a new LevelLoader object.
     *
     * @param startAction
     *            the model call to run in the background, for example
     *            () -> model.startLevel(1), model::nextLevel or
     *            model::retryLevel
     * @param onFinished
     *            runs on the Swing thread if the model accepted the call
     * @param onFailed
     *            runs on the Swing thread if the model refused the call
     *            (or it threw)
     */
    public LevelLoader(
        BooleanSupplier startAction,
        Runnable onFinished,
        Runnable onFailed)
    {
        this.startAction = startAction;
        this.onFinished = onFinished;
        this.onFailed = onFailed;
    }


    @Override
    protected Boolean doInBackground()
    {
        return startAction.getAsBoolean();
    }


    @Override
    protected void done()
    {
        try
        {
            if (get())
            {
                onFinished.run();
                return;
            }
        }
        catch (InterruptedException | ExecutionException e)
        {
            // fall through: treat like a refused call
        }
        onFailed.run();
    }
}