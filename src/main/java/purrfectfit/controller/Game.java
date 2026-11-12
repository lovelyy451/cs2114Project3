package purrfectfit.controller;

import javax.swing.SwingUtilities;
import purrfectfit.model.GameModel;
import purrfectfit.view.MainWindow;

// -------------------------------------------------------------------------
/**
 * Creates the model, view and controller and starts the game. Runs once at
 * startup; after that the listeners and the timer inside InputHandler drive
 * everything.
 */
public class Game
{
    // ----------------------------------------------------------
    /**
     * Program entry point.
     *
     * @param args
     *            unused
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            GameModel model = new GameModel();
            MainWindow window = new MainWindow();
            OutputHandler output = new OutputHandler(model, window);
            new InputHandler(model, output, window);
            output.showStart();
        });
    }
}